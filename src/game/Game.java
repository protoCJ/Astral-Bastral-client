package game;

import game.entities.*;
import game.input.ActionHandler;
import game.input.KeyInputHandler;
import game.sprites.Sprite;
import game.sprites.SpritesDepot;
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
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by protoCJ on 23.04.2017.
 */
public class Game extends Canvas {

    // Game specific enum containing allowed rotation directions.
    public enum RotationDirections {
        LEFT, RIGHT
    }


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
    // Size of update sent to sever.
    private static final int UPDATE_SIZE = 116;
    private static final int MAX_MISSILES_PER_ACTION = 8;
    // State update parameters.
    private static final int REFRESH_BYTES_OFFSET = 60;
    // Constant sizes.
    private static final int SHORT_SIZE = 2;
    private static final int INT_SIZE = 4;
    // Constant dimension of the window.
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    // Rotation increment value.
    private static final float ROTATION_INCREMENT = 0.05f;
    // Constant indicating empty rotation.
    private static final float EMPTY_ROTATION = -1.0f;
    // Key of background sprite and hp bar.
    private static final String BACKGROUND_SPRITE_KEY = "background.png";
    private static final String HP_BAR_SPRITE_KEY = "hp_bar.png";
    // Max main ship hp.
    private static final int MAX_MAIN_SHIP_HP = 5000;


    // Game specific network and action handlers.
    NetworkHandler networkHandler;
    ActionHandler actionHandler;
    // Game buffering strategy.
    private BufferStrategy strategy;
    // States whether the game is currently running.
    private boolean gameRunning = true;
    // Arrays of all in-game entities and players. On this client-side player
    // is represented as simple integer index to his turret in entities array.
    private int [] players;
    private GameEntity[] entities;
    // Local player's id.
    private int playerId;
    // Local update index.
    private int updateIndex = -1;
    // Game input state variables and their locks.
    private final Object rotatingLock = new Object();
    private boolean rotating = false;
    private RotationDirections rotationDirection;
    private final Object firingLock = new Object();
    private boolean firing = false;
    // Player state variables and their locks.
    private final Object rotationLock = new Object();
    private float rotation = 0.0f;
    private final Object fireCountLock = new Object();
    private int fireCount = 0;
    private int fireIntervalCounter = 0;
    // Game window background and hp bar.
    private Sprite background;
    private Sprite hpBar;
    // Main ship hp.
    private int mainShipHp = 10000;
    // This player score.
    private int score = 0;


    public Game(String hostName, Integer port) {
        // Create network and action handlers.
        try {
            networkHandler = new ConnectionHandler().initConnection(
                hostName, port, this
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
        actionHandler = new ActionHandler(this);
        // Create players and entities arrays.
        players = new int[MAX_PLAYERS];
        for (int i = 0; i < MAX_PLAYERS; i++) {
            players[i] = NO_PLAYER;
        }
        entities = new GameEntity[MAX_ENTITIES];
        // Load background sprite.
        background = SpritesDepot.getInstance().getSpriteByKey(
            BACKGROUND_SPRITE_KEY
        );
        hpBar = SpritesDepot.getInstance().getSpriteByKey(
            HP_BAR_SPRITE_KEY
        );
    }

    // Checks if player with given id exists and has his own turret.
    // TODO synchronize players, entities
    private boolean playerExists(int playerId) {
        return playerId != NO_PLAYER &&
               players[playerId] >= 0 && players[playerId] < MAX_ENTITIES &&
               entities[players[playerId]] != null &&
               entities[players[playerId]].getType() ==
                   GameEntitiesTypes.TURRET;
    }

    // Return turret for given player id. User must previously make sure that
    // the turret exists by calling playerExists.
    // TODO synchronize player, entites
    private Turret getPlayerTurret(int playerId) {
        return ((Turret) entities[players[playerId]]);
    }

    // #################
    // # GAME HANDLING #
    // #################

    // Start the game.
    public void start() {
        (new Thread(networkHandler)).start();
        actionHandler.start();
        startGameLoop();
    }

    // Game window initialization.
    private void startGameLoop() {
        String WINDOW_TITLE = "Astral Bastral";
        int NUMBER_OF_BUFFERS = 2;
        // Create a main window frame.
        JFrame container = new JFrame(WINDOW_TITLE);
        // Set size of the window.
        JPanel panel = (JPanel) container.getContentPane();
        panel.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        panel.setLayout(null);
        // Add this game to the frame as canvas.
        setBounds(0,0, WIDTH, HEIGHT);
        panel.add(this);
        // Force AWT to ignore repainting.
        setIgnoreRepaint(true);
        // Show the window.
        container.pack();
        container.setResizable(false);
        container.setVisible(true);
        // Add listener for window exit.
        container.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        // add a key input system (defined below) to our canvas
        // so we can respond to key pressed
        addKeyListener(new KeyInputHandler(this));
        requestFocus();
        // create the buffering strategy which will allow AWT
        // to manage our accelerated graphics
        createBufferStrategy(NUMBER_OF_BUFFERS);
        strategy = getBufferStrategy();
        // Start game loop.
        gameLoop();
    }

    // Main game loop.
    // TODO synchronize entities, playerID
    public void gameLoop() {
        int MILLISECONDS_DIVIDER = 1000;
        int LOOP_SLEEP = 10;
        // Variables used to manage the game time.
        long delta, lastLoopTime = System.currentTimeMillis();
        // Keep looping while the game is running.
        while (gameRunning) {
            // Find out the delta time of current game loop.
            delta = System.currentTimeMillis() - lastLoopTime;
            lastLoopTime = System.currentTimeMillis();
            // Acquire and clear graphics context.
            Graphics2D graphics = (Graphics2D) strategy.getDrawGraphics();
            background.draw(graphics, WIDTH / 2, HEIGHT / 2, 0);
            // Update rotation and fire count basing on current state.
            handleInputState();
            // Move and draw every entity.
            for (GameEntity entity : entities) {
                if (entity != null) {
                    if (entity.getType() == GameEntitiesTypes.ASTEROID) {
                    }
                    entity.move((float) delta / MILLISECONDS_DIVIDER);
                    entity.draw(graphics, WIDTH / 2, HEIGHT / 2);
                }
            }
            graphics.setColor(Color.DARK_GRAY);
            graphics.fillRect(WIDTH / 2 - 194, HEIGHT / 20 - 6, 391, 12);
            graphics.setColor(Color.RED);
            graphics.fillRect(WIDTH / 2 - 194, HEIGHT / 20 - 6, 391 * mainShipHp / MAX_MAIN_SHIP_HP, 12);
            hpBar.draw(graphics, WIDTH / 2, HEIGHT / 20, 0);
            graphics.drawString("Score: " + score, 10, 10);
            // Show updates.
            graphics.dispose();
            strategy.show();
            // Sleep.
            try {
                Thread.sleep(LOOP_SLEEP);
            }
            catch (Exception exception) {

            }
        }
        // Show end game.
        Graphics2D graphics = (Graphics2D) strategy.getDrawGraphics();

        graphics.dispose();
        strategy.show();
    }

    // Update the missiles fire count and player rotation accordingly to
    // current input state.
    private void handleInputState() {
        int FIRE_INTERVAL = 15;
        // If the player is currently rotating, update his rotation.
        synchronized (rotatingLock) {
            if (rotating) {
                synchronized (rotatingLock) {
                    rotation += rotationDirection == RotationDirections.LEFT ?
                                -ROTATION_INCREMENT : ROTATION_INCREMENT;
                    if (rotation < 0.0f) {
                        rotation += 2 * Math.PI;
                    }
                    if (rotation > 2 * Math.PI) {
                        rotation -= 2 * Math.PI;
                    }
                    // Rotate players turret locally too for smooth effect.
                    if (playerExists(playerId)) {
                        getPlayerTurret(playerId).setRotation(rotation);
                    }
                }
            }
        }
        // If the player is currently firing, update the fire count.
        synchronized (firingLock) {
            if (firing) {
                synchronized (fireCountLock) {
                    if (fireIntervalCounter == 0) {
                        fireCount += 1;
                    }
                    // Fire every 15 loops.
                    fireIntervalCounter = (fireIntervalCounter + 1) %
                    FIRE_INTERVAL;
                }
            }
        }
    }

    // ##############################
    // # SENDING AND RECEIVING DATA #
    // ##############################

    // Used to send data to game's network handler.
    public void sendData(byte[] toSend) {
        try {
            networkHandler.send(toSend);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    // Create bytes of update sent to server.
    // TODO synchronize entites, playerID
    public byte[] getUpdateData() {
        // Constant offset of spawned missiles.
        float MISSILE_SPAWN_OFFSET = 32.0f;
        ByteBuffer buffer = ByteBuffer.allocate(UPDATE_SIZE);
        synchronized (rotationLock) {
            buffer.putFloat(rotation);
            synchronized (fireCountLock) {
                int i = 0;
                boolean playerExists = playerExists(playerId);
                if (playerExists) {
                    // If player has his turret assigned to him, find out where
                    // fired missiles will be spawned.
                    float x, y;
                    float dx = (float) Math.cos(rotation);
                    float dy = (float) Math.sin(rotation);
                    Turret turret = getPlayerTurret(playerId);
                    x = turret.getX() + dx * MISSILE_SPAWN_OFFSET;
                    y = turret.getY() + dy * MISSILE_SPAWN_OFFSET;
                    // Output all fired missiles to buffer or as much as is
                    // allowed per action.
                    for ( ; i < fireCount & i < MAX_MISSILES_PER_ACTION; i++) {
                        putMissileInBuffer(
                            buffer, MissilesTypes.BASIC_MISSILE.getValue(),
                            rotation, x, y
                        );
                    }
                }
                fireCount -= i;
                for ( ; i < MAX_MISSILES_PER_ACTION; i++) {
                    // Output empty missile to buffer. Data is irrelevant.
                    putMissileInBuffer(
                        buffer, MissilesTypes.EMPTY_MISSILE.getValue(),
                        0.0f, 0.0f, 0.0f
                    );
                }
            }
        }
        buffer.flip();
        return buffer.array();
    }

    // Method for outputting missile data to byte buffer.
    private void putMissileInBuffer(
        ByteBuffer buffer, short type, float rotation, float x, float y
    ) {
        // Output missile data to buffer as bytes.
        buffer.putShort(type);
        buffer.putFloat(rotation);
        buffer.putFloat(x);
        buffer.putFloat(y);
    }

    // Parse local state update from data contained in provided byte array.
    // TODO synchronize entites, player, playerID
    public void updateState(byte[] data) throws Exception {
        ByteArrayInputStream byteStream = new ByteArrayInputStream(data);
        DataInputStream input = new DataInputStream(byteStream);
        // Read update header data.
        playerId = input.readInt();
        // It might be needed to check this.
        updateIndex = input.readInt();
        mainShipHp = input.readInt();
        int createdOffset = input.readInt();
        int destroyedOffset = input.readInt();
        int refreshOffset = input.readInt();
        int refreshIndex = input.readInt();
        // Read all player rotations.
        float[] rotations = new float[MAX_PLAYERS];
        for (int i = 0; i < MAX_PLAYERS; i++) {
            rotations[i] = input.readFloat();
        }
        int[] scores = new int[MAX_PLAYERS];
        for (int i = 0; i < MAX_PLAYERS; i++) {
            scores[i] = input.readInt();
        }
        score = scores[playerId];
        // Start reading the rest of update.
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
            if (createdEntity != null) {
                readBytes = createdEntity.readFrom(input);
                if (createdEntity.getType() == GameEntitiesTypes.TURRET) {
                    players[((Turret) createdEntity).getPlayerId()] =
                    i + refreshIndex * STATE_REFRESH_SIZE;
                }
            }
            entities[i + refreshIndex * STATE_REFRESH_SIZE] = createdEntity;
        }
        // Update very player rotation.
        for (int i = 0; i < MAX_PLAYERS; i++) {
            if (playerExists(i)) {
                getPlayerTurret(i).setRotation(rotations[i]);
            }
        }
    }

    // Creates new entity based on read, short type value.
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

    // #######################
    // # HANDLING USER INPUT #
    // #######################

    // Start rotating in given direction.
    public void startRotating(RotationDirections direction) {
        synchronized (rotatingLock) {
            rotating = true;
            rotationDirection = direction;
        }
    }

    // Stop rotation if direction of stopping matches current rotation
    // direction.
    public void stopRotating(RotationDirections direction) {
        synchronized (rotatingLock) {
            if (direction == rotationDirection) {
                rotating = false;
            }
        }
    }

    // Start firing the player's turret.
    public void startFiring() {
        synchronized (firingLock) {
            if (!firing) {
                firing = true;
                fireIntervalCounter = 0;
            }
        }
    }

    // Stop firing the player's turret.
    public void stopFiring() {
        synchronized (firingLock) {
            firing = false;
        }
    }

    // Fire one missile.
    public void fireOnce() {
        synchronized (fireCountLock) {
            fireCount += 1;
            fireIntervalCounter = 0;
        }
    }

}
