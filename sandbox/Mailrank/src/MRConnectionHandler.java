
import java.util.Vector;

/**
 * Abstracts from the connection. Socket and mail implemented now.
 * See MRSocket and MRMail
 */
public interface MRConnectionHandler {
    /**
     * sends the Data into the Server
     * and receives if appropriate the query result.
     * The vector consists of MRData Objects
     */

    public Vector send(MRData myData);

}
