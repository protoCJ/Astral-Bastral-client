package network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Arrays;

/**
 * Created by Paweł Kopeć on 21.03.17.
 * <p>
 * Implementation of two-way connection
 * implemented with UDP protocol.
 */
class UDPAccessPoint implements AccessPoint {

    private static final int MAX_IN_BUFF_SIZE = 65507, MAX_OUT_BUFF_SIZE = 65507;

    private static final String IN_BUFF_SIZE_NON_POSITIVE = "Input buffer size must be positive.";
    private static final String IN_BUFF_SIZE_OUT_OF_RANGE = "Input buffer size must be not greater than " + MAX_IN_BUFF_SIZE + ".";
    private static final String DATA_SIZE_OUT_OF_RANGE = "Size of data to be sent cannot be bigger than " + MAX_OUT_BUFF_SIZE + ".";

    private DatagramSocket in;
    private DatagramSocket out;
    private InetAddress peerAddress;
    private int peerPort;

    private byte inBuff[];

    UDPAccessPoint(int portIn, int portOut, int peerPort, InetAddress address, int inBuffSize) throws SocketException {
        if (inBuffSize <= 0) {
            throw new IllegalArgumentException(IN_BUFF_SIZE_NON_POSITIVE);
        }

        if (MAX_IN_BUFF_SIZE < inBuffSize) {
            throw new IllegalArgumentException(IN_BUFF_SIZE_OUT_OF_RANGE);
        }

        if (!Ports.isValidListeningPortNumber(portIn) ||
                !Ports.isValidListeningPortNumber(portOut) ||
                !Ports.isValidPortNumber(peerPort)) {
            throw new IllegalArgumentException(Ports.INVALID_PORT_NUMBER);
        }

        in = new DatagramSocket(portIn);
        out = new DatagramSocket(portOut);

        this.peerPort = peerPort;
        peerAddress = address;

        inBuff = new byte[inBuffSize];
    }

    @Override
    public synchronized void sendData(byte[] data) throws IOException {
        if (MAX_OUT_BUFF_SIZE < data.length) {
            throw new IllegalArgumentException(DATA_SIZE_OUT_OF_RANGE);
        }

        out.send(new DatagramPacket(data, data.length, peerAddress, peerPort));
    }

    @Override
    public synchronized byte[] getData() throws IOException {
        DatagramPacket packet = new DatagramPacket(inBuff, inBuff.length);
        in.receive(packet);

        return Arrays.copyOf(packet.getData(), packet.getLength());
    }

    @Override
    public InetAddress getPeerAddress() {
        return peerAddress;
    }

    @Override
    public int getPortIn() {
        return in.getLocalPort();
    }

    @Override
    public int getPortOut() {
        return out.getLocalPort();
    }

    @Override
    public int getPeerPort() {
        return peerPort;
    }

    @Override
    public void close() {
        in.close();
        out.close();
    }
}
