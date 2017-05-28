package network;

import game.Game;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by protoCJ on 24.04.2017.
 */
public class ConnectionHandler {

    private int portIn, portOut;

    public NetworkHandler initConnection(String hostName, int port, Game game) throws IOException {
        readLocalPorts();
        int peerPort = exchangeUDPPorts(hostName, port);
        InetAddress address = InetAddress.getByName(hostName);
        UDPAccessPoint accessPoint = new UDPAccessPoint(portIn, portOut, peerPort, address, 300);

        return new NetworkHandler(game, accessPoint);
    }

    private void readLocalPorts() {
        System.out.println("Give a local UDP in port number:");
        portIn = new Scanner(System.in).nextInt();
        System.out.println("Give a local UDP in port number:");
        portOut = new Scanner(System.in).nextInt();
    }

    private int exchangeUDPPorts(String hostName, Integer port) throws IOException {
        System.out.println("Initiating connection on port: " + port + ", hostname: " + hostName + ".");
        Socket s = new Socket(hostName, port);
        System.out.println("Connected. Receiving UDP port for asynchronous transmission.");

        DataOutputStream serverOut = new DataOutputStream(s.getOutputStream());
        DataInputStream serverIn = new DataInputStream(s.getInputStream());
        int peerPort = serverIn.readInt();

        if (peerPort == Ports.NO_PORT) {
            return peerPort;
        }

        System.out.println("Port " + peerPort + " received. Sending listening port " + portIn + " to server.");

        serverOut.writeInt((short)portIn);
        System.out.println("Port sent. Creating UPD sockets.");

        return peerPort;
    }
}
