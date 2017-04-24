package network;

import game.Game;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Created by protoCJ on 24.04.2017.
 */
public class ConnectionHandler {

    Integer inPort = 5675;

    public NetworkHandler initConnection(String hostName, Integer port, Game game) throws IOException {
        System.out.println("Initiating connection on port: " + port + ", hostname: " + hostName + ".");
        Socket s = new Socket(hostName, port);
        System.out.println("Connected. Receiving UDP port for asynchronous transmission.");

        DataOutputStream serverOut = new DataOutputStream(s.getOutputStream());
        BufferedReader serverIn = new BufferedReader(new InputStreamReader(s.getInputStream()));
        Integer udpListenPort = serverIn.read();
        System.out.println("Port " + udpListenPort + " received. Sending listening port " + inPort + " to server.");

        serverOut.write(inPort);
        System.out.println("Port sent. Creating UPD sockets.");
        return new NetworkHandler(game, inPort, udpListenPort, hostName);
    }
}
