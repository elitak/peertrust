
import java.io.IOException;
import java.net.ServerSocket;

/**
 * Class for the Socketserver
 */
public class MRSocket {
    ServerSocket socket = null;

    /**
     * Binds a port to a socket and wait for connections
     * if a connection attempt happend it starts a thread, that
     * handles all
     * @param port the listen port
     */
    public void init(int port) {
        try {

            socket = new ServerSocket(port);


            while (socket.isBound()) {
                new MRSocketThread(socket.accept()).start();
            }

        } catch (IOException e) {
            System.err.println("Could not listen on port " + port);
            

        }

    }

    /**
     * close the socket
     * @param socket the Serversocket
     * @return true if all goes well
     */
    public boolean closeConnection(ServerSocket socket) {
        try {
            socket.close();
        } catch (IOException e) {

        }
        return true;
    }
}
