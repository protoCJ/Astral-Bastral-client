package network;

import game.Game;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.net.*;

/**
 * Created by protoCJ on 23.04.2017.
 */
public class NetworkHandler implements Runnable {

    private Game game;
    private UDPAccessPoint accessPoint;
    private boolean running;

    NetworkHandler(Game game, UDPAccessPoint accessPoint) throws UnknownHostException {
        this.game = game;
        this.accessPoint = accessPoint;
    }

    public void send(byte[] data) throws IOException {
        accessPoint.sendData(data);
    }

    public void stop() {
        System.out.println("Stopping UDP listening");
        running = false;
        accessPoint.close();
    }

    @Override
    public void run() {
        System.out.println("Starting UDP listening");
        running = true;
        while (running) {
            try {
                game.updateState(accessPoint.getData());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        accessPoint.close();
    }
}

