package network;

import game.Game;

import java.io.*;
import java.net.Socket;

/**
 * Created by protoCJ on 24.04.2017.
 */
public class ConnectionHandler {

    Integer inPort = 5677;

    public NetworkHandler initConnection(String hostName, Integer port, Game game) throws IOException {
        System.out.println("Initiating connection on port: " + port + ", hostname: " + hostName + ".");
        Socket s = new Socket(hostName, port);
        System.out.println("Connected. Receiving UDP port for asynchronous transmission.");
        DataOutputStream serverOut = new DataOutputStream(s.getOutputStream());
        DataInputStream serverIn = new DataInputStream(s.getInputStream());
        Integer udpListenPort = serverIn.readInt();
        System.out.println("Port " + udpListenPort + " received. Sending listening port " + inPort + " to server.");

        serverOut.writeInt(inPort.shortValue());
        System.out.println("Port sent. Creating UPD sockets.");
        return new NetworkHandler(game, inPort, udpListenPort, hostName);
    }
}
