/**
 * Created by IntelliJ IDEA.
 * User: broso
 * Date: Jul 30, 2004
 * Time: 11:01:29 AM
 * To change this template use File | Settings | File Templates.
 */

import com.sleepycat.je.*;

import java.io.File;

public class TestSecondaryIndexes {
    public static void main(String[] args) {

        Environment myEnv;
        Database myDatabase;

        try {
            EnvironmentConfig envConfig = new EnvironmentConfig();
            envConfig.setAllowCreate(true);
            myEnv = new Environment(new File("/Users/broso/testDB"), envConfig);



// Environment open omitted for clarity
        DatabaseConfig myDbConfig = new DatabaseConfig();
        SecondaryConfig mySecConfig = new SecondaryConfig();
        myDbConfig.setAllowCreate(true);
        mySecConfig.setAllowCreate(true);
// Duplicates are frequently required for secondary databases.
        mySecConfig.setSortedDuplicates(true);

// Open the primary

            String dbName = "myPrimaryDatabase";
            Database myDb = myEnv.openDatabase(null, dbName, myDbConfig);

            // Open the secondary.
            // Key creators are described in the next section.
            //AKeyCreator akc = new AKeyCreator();

            // Get a secondary object and set the key creator on it.
            //mySecConfig.setKeyCreator(keyCreator);

            // Perform the actual open
            String secDbName = "mySecondaryDatabase";
            SecondaryDatabase mySecDb =
                    myEnv.openSecondaryDatabase(null, secDbName, myDb, mySecConfig);
        } catch (DatabaseException de) {
            // Exception handling goes here
        }
    }
}
