/**
 * Created by IntelliJ IDEA.
 * User: broso
 * Date: Jul 30, 2004
 * Time: 3:20:23 PM
 * To change this template use File | Settings | File Templates.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Vector;

public class MRSocketThread extends Thread implements MRConnectionHandler {
    private static final boolean DEBUG = true;
    private Socket socket = null;
    private PrintWriter out = null;
    private BufferedReader in = null;
    Vector result = new Vector();

    public MRSocketThread(Socket socket) {
        super("MRSocketThread");
        this.socket = socket;
    }

    /**
     * Gets a string from the client (see also bug, line 43), starts
     * the parser, queries the DB with the MRData object
     */
    public void run() {

        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            if (DEBUG) System.out.println("New Server Thread started");

            String input;
            MRDataParser myParser = new MRDataParser();

            //while (! (input = in.readLine()).equals(null)) {
            //todo WHY is the while loop not working??????? every line in the client a new connection???
            //going crazy about this!!!

            input = in.readLine();
            //Use the parser to get an MRData object
            //out of this line

            MRData myData = myParser.parse(input);

            //Check whether the MRData object returned by the parser
            //is not empty (in the case of mailformed data)

            if (myData.getCommand() != null) {
                result = send(myData);

                //Check whether the resultvector returned by the MRConnectionHandler
                //is not empty (in the case of sendValues)

                if (!result.isEmpty()) {

                    //write the result to the socket
                    for (int i = 0; i < result.size(); i++) {
                        MRData data = ((MRData) result.get(i));
                        out.println(data.getUser() + ":" +
                                data.getAddress() + ":" +
                                data.getScore() + ":" +
                                data.getCount());
                    }
                }
            }

            //}
            if (DEBUG) System.out.println("Closing Connection");
            closeConnection();

            if (DEBUG) System.out.println("Server Thread ended");

        } catch (IOException e) {
            System.out.println("I/O-Error");
            this.closeConnection();
        }

    }

    /**
     * closes the socket connection after the work is done
     */
    public void closeConnection() {
        out.close();
        try {
            in.close();
        } catch (IOException e) {
            System.out.println("Can't close the Connection.");
        }
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * If something happens, that's not good, interrupt() is called
     */
    public void interrupt() {
        System.out.println("Something really bad happens. Cleaning up the Thread.");

        out.close();
        try {
            in.close();
        } catch (IOException e) {
            System.out.println("Can't close the Connection.");
        }
    }

    /**
     * Takes a MRData object and asks the DB. Gets back a vector
     * of MRData objects
     * @param myData a MRData object
     * @return a Vector of MRData objects
     */
    public Vector send(MRData myData) {
        if (DEBUG) System.out.println("Asking the DB");
        MRServerChannel chann = new MRServerChannel(myData);
        return chann.doWork();
    }


}

