
import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: broso
 * Date: Aug 10, 2004
 * Time: 7:35:54 PM
 * To change this template use File | Settings | File Templates.
 */
public interface MRDatabaseHandler {
    /**
     * Init the connection to the database. Please edit the parameters to fit
     * our needs.
     * @return true if the connection works, otherwise throws an exception
     */
    public boolean init();
    /**
     * Closes the DB Connection
     * @return true if all goes well, otherwise throws an SQLException
     */
    public boolean close();
    /**
     * Writes the data (the data inside a MRData object)
     * into the database
     * @param myData a MRData object
     */
    public void setValues(MRData myData);
    /**
     * Testmethod, gets all entries sent by a specific user
     * @param myData a MRData object
     * @return a vector of MRData objects
     */
    public Vector getEntryByUser(MRData myData);
    /**
     * Testmethod, gets all entries of a specific spam mailaddress
     * @param myData a MRData object
     * @return a vector of MRData objects
     */
    public Vector getEntryBySpamaddress(MRData myData);
    /**
     * Testmethod, gets all entries of a specific score
     * @param myData a MRData object
     * @return a vector of MRData objects
     */
    public Vector getEntryByScore(MRData myData);
    /**
     * Testmethod, gets all entries of a specific count
     * @param myData a MRData object
     * @return a vector of MRData objects
     */
    public Vector getEntryByCount(MRData myData);
}
