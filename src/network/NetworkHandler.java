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
    private DatagramSocket in;
    private InetAddress serverAddress;
    private DatagramSocket out;
    private Integer outPort;
    private boolean running;

    public NetworkHandler(Game game, Integer inPortNumber, Integer outPortNumber, String address) throws UnknownHostException {
        this.game = game;
        this.serverAddress = InetAddress.getByName(address);
        this.outPort = outPortNumber;
        try {
            System.out.println("Creating UDP sockets.");
            in = new DatagramSocket(inPortNumber);
            out = new DatagramSocket();
        }
        catch (SocketException e) {
            e.printStackTrace();
        }
        System.out.println("UDP sockets created.");

    }

    public void send(byte[] data) throws IOException {
        out.send(new DatagramPacket(data, data.length, serverAddress, outPort));
        //System.out.println("Data sent: " + DatatypeConverter.printHexBinary(data) + " to " + serverAddress + " on port " + outPort);
    }

    public void stop() {
        System.out.println("Stopping UDP listening");
        running = false;
        in.close();
        out.close();
    }

    @Override
    public void run() {
        System.out.println("Starting UDP listening");
        running = true;
        byte[] buf = new byte[300];
        DatagramPacket p = new DatagramPacket(buf, 300);
        while (running) {
            try {
                in.receive(p);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                game.updateState(p.getData());
            }
            catch (Exception exception) {
                // TODO
            }
        }
    }
}

