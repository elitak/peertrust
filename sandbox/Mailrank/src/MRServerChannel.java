
import java.util.Vector;

/**
 * This is the adaptorclass to abtract from the communication channel
 * It handles all the communication between the MRDatabaseHandler interface
 * and the MRConnectionHandler interface
 */
public class MRServerChannel {


    MRData myData = null;
    Vector result = new Vector();
    MRDatabaseHandler myDB = new MRMySQLDatabase();

    MRServerChannel(MRData myData) {
        this.myData = myData;
    }

    /**
     * Like the name this method does all the work.
     * In case of extending the MRData class (trustvalue),
     * this method must be extended
     * @return a Vector that consists of MRData objects
     */

    public Vector doWork() {

        if (myDB.init()) {  
            if (myData.getCommand().equals("setValues")) {
                myDB.setValues(myData);
                //in this case result is an empty vector
            } else if (myData.getCommand().equals("getEntryByUser")) {
                result = myDB.getEntryByUser(myData);
            } else if (myData.getCommand().equals("getEntryBySpamaddress")) {
                result = myDB.getEntryBySpamaddress(myData);
            } else if (myData.getCommand().equals("getEntryByScore")) {
                result = myDB.getEntryByScore(myData);
            } else if (myData.getCommand().equals("getEntryByCount")) {
                result = myDB.getEntryByCount(myData);
            }
            myDB.close();
        } else {
              System.out.println("DB Connection failed");
        }
        return result;
    }

}
