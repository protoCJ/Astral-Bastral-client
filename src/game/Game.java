package game;

import game.entities.*;
import game.input.ActionHandler;
import game.input.KeyInputHandler;
import network.ConnectionHandler;
import network.NetworkHandler;

import javax.swing.*;
import javax.xml.bind.DatatypeConverter;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by protoCJ on 23.04.2017.
 */
public class Game extends Canvas {

    NetworkHandler networkHandler;
    ActionHandler actionHandler;

    /** The strategy that allows us to use accelerate page flipping */
    private BufferStrategy strategy;
    /** True if the game is currently "running", i.e. the game loop is looping */
    private boolean gameRunning = true;

    // Limits for number of game entities present in-game at once and for
    // number of players.
    private final static int MAX_PLAYERS = 4;
    private final static int MAX_ENTITIES = 1024;

    // Unique no player value.
    private final static int NO_PLAYER = -1;

    // Number of entities send with single state refresh and last refresh
    // window index;
    private static final int STATE_REFRESH_SIZE = 64;
    private static final int STATE_REFRESH_WINDOW_SIZE = 16;

    // State update parameters.
    private static final int REFRESH_BYTES_OFFSET = 32;

    // Constant sizes.
    private static final int SHORT_SIZE = 2;
    private static final int INT_SIZE = 4;

    // Constant dimension of the window.
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;


    // Arrays of all in-game entities and players. On this client-side player
    // is represented as simple integer index to his turret in entities array.
    private int [] players;
    private GameEntity[] entities;

    // Local player's id.
    private int playerId;


    public Game(String hostName, Integer port) {
        try {
            this.networkHandler = new ConnectionHandler().initConnection(hostName, port, this);
        } catch (IOException e) {
            e.printStackTrace();
        }
        actionHandler = new ActionHandler(this);

        players = new int[MAX_PLAYERS];
        for (int i = 0; i < MAX_PLAYERS; i++) {
            players[i] = NO_PLAYER;
        }
        entities = new GameEntity[MAX_ENTITIES];

        entities[0] = new Turret(0, 0.0f, 0.0f, 0.0f);
    }

    public void updateState(byte[] data) throws Exception {
        System.out.println("Updating state with: " + DatatypeConverter.printHexBinary(data));

        ByteArrayInputStream byteStream = new ByteArrayInputStream(data);
        DataInputStream input = new DataInputStream(byteStream);

        playerId = input.readInt();
        int createdOffset = input.readInt();
        int destroyedOffset = input.readInt();
        int refreshOffset = input.readInt();
        int refreshIndex = input.readInt();
        float[] rotations = new float[MAX_PLAYERS];

        for (int i = 0; i < MAX_PLAYERS; i++) {
            rotations[i] = input.readFloat();
        }

        int currentPosition = REFRESH_BYTES_OFFSET;
        short readType;
        int readBytes;
        int readIndex;
        GameEntity createdEntity;

        // Parse created entities.
        while (currentPosition < destroyedOffset) {
            readIndex = input.readInt();
            readType = input.readShort();
            createdEntity = createFromType(readType);
            readBytes = createdEntity.readFrom(input);
            if (createdEntity.getType() == GameEntitiesTypes.TURRET) {
                players[((Turret) createdEntity).getPlayerId()] = readIndex;
            }
            entities[readIndex] = createdEntity;
            currentPosition += INT_SIZE + SHORT_SIZE + readBytes;
        }

        // Parse destroyed entities.
        while (currentPosition < refreshOffset) {
            readIndex = input.readInt();
            entities[readIndex] = null;
            currentPosition += INT_SIZE;
        }

        // Parse state refresh.
        for (int i = 0; i < STATE_REFRESH_SIZE; i++) {
            readType = input.readShort();
            createdEntity = createFromType(readType);
            readBytes = createdEntity.readFrom(input);
            if (createdEntity.getType() == GameEntitiesTypes.TURRET) {
                players[((Turret) createdEntity).getPlayerId()] =
                    i + refreshIndex * STATE_REFRESH_SIZE;
            }
            entities[i + refreshIndex * STATE_REFRESH_SIZE] = createdEntity;
        }

        // Rotate if turret's index is valid.
        for (int i = 0; i < MAX_PLAYERS; i++) {
            if (
                players[i] > 0 && players[i] < MAX_ENTITIES &&
                entities[players[i]] != null &&
                entities[players[i]].getType() == GameEntitiesTypes.TURRET
            ) {
                ((Turret) entities[players[i]]).rotate(rotations[i]);
            }
        }

    }

    // Creates new entity based on read short type value.
    private GameEntity createFromType(short value) {
        GameEntitiesTypes type = GameEntitiesTypes.getByValue(value);
        switch (type) {
            case ASTEROID:
                return new Asteroid();
            case ENEMY_MISSILE:
                return new EnemyMissile();
            case ENEMY_SHIP:
                return new EnemyShip();
            case MAIN_SHIP:
                return new MainShip();
            case FRIENDLY_MISSILE:
                return new FriendlyMissile();
            case TURRET:
                return new Turret();
        }
        return null;
    }

    public void start() {
        (new Thread(networkHandler)).start();
        actionHandler.start();
        startGameLoop();
    }

    private void startGameLoop() {
        // create a frame to contain our game
        JFrame container = new JFrame("Astral Bastral");

        // get hold the content of the frame and set up the resolution of the game
        JPanel panel = (JPanel) container.getContentPane();
        panel.setPreferredSize(new Dimension(800,600));
        panel.setLayout(null);

        // setup our canvas size and put it into the content of the frame
        setBounds(0,0,800,600);
        panel.add(this);

        // Tell AWT not to bother repainting our canvas since we're
        // going to do that our self in accelerated mode
        setIgnoreRepaint(true);

        // finally make the window visible
        container.pack();
        container.setResizable(false);
        container.setVisible(true);

        // add a listener to respond to the user closing the window. If they
        // do we'd like to exit the game

        container.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        // add a key input system (defined below) to our canvas
        // so we can respond to key pressed
        addKeyListener(new KeyInputHandler());

        // request the focus so key events come to us
        requestFocus();

        // create the buffering strategy which will allow AWT
        // to manage our accelerated graphics
        createBufferStrategy(2);
        strategy = getBufferStrategy();
        // initialise the entities in our game so there's something
        // to see at startup
        gameLoop();
    }

    /**
     * The main game loop. This loop is running during all game
     * play as is responsible for the following activities:
     * <p>
     * - Working out the speed of the game loop to update moves
     * - Moving the game entities
     * - Drawing the screen contents (entities, text)
     * - Updating game events
     * - Checking Input
     * <p>
     */
    public void gameLoop() {
        long lastLoopTime = System.currentTimeMillis();

        // keep looping round til the game ends

        while (gameRunning) {
            long delta = System.currentTimeMillis() - lastLoopTime;
            lastLoopTime = System.currentTimeMillis();

            Graphics2D g = (Graphics2D) strategy.getDrawGraphics();
            g.setColor(Color.black);
            g.fillRect(0, 0, WIDTH, HEIGHT);

            for (GameEntity entity : entities) {
                if (entity != null) {
                    entity.draw(g, WIDTH / 2, HEIGHT / 2);
                }
            }

            g.dispose();
            strategy.show();

            try { Thread.sleep(10); } catch (Exception e) {}
        }
    }

    public void sendData(byte[] toSend) {
        try {
            networkHandler.send(toSend);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
