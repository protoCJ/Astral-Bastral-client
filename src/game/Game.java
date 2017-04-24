package game;

import game.graphics.GameWindow;
import game.input.ActionHandler;
import network.ConnectionHandler;
import network.NetworkHandler;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;

/**
 * Created by protoCJ on 23.04.2017.
 */
public class Game {

    NetworkHandler networkHandler;
    GameWindow gameWindow;
    ActionHandler actionHandler;
    private float playerRotation;

    public Game(String hostName, Integer port) {
        try {
            this.networkHandler = new ConnectionHandler().initConnection(hostName, port, this);
        } catch (IOException e) {
            e.printStackTrace();
        }
        playerRotation = 0.1f;
        actionHandler = new ActionHandler(this);
        gameWindow = new GameWindow(this);
    }

    public void updateState(byte[] data) {
        System.out.println("Updating state with: " + DatatypeConverter.printHexBinary(data));
    }

    public void start() {
        (new Thread(networkHandler)).start();
        actionHandler.start();
        gameWindow.start();
    }

    public void sendData(byte[] toSend) {
        try {
            networkHandler.send(toSend);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public float getPlayerRotation() {
        return playerRotation;
    }

    public void setPlayerRotation(float playerRotation) {
        this.playerRotation = playerRotation;
    }
}
