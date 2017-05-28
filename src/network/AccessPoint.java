package network;

import java.io.IOException;
import java.net.InetAddress;

/**
 * Created by Paweł Kopeć on 21.03.17.
 *
 * A class that provides a two-way connection
 * with another host;
 */
public interface AccessPoint {

    void sendData(byte[] data) throws IOException;

    byte[] getData() throws IOException;

    InetAddress getPeerAddress();

    int getPortIn();

    int getPortOut();

    int getPeerPort();

    void close();
}
