

import java.util.Vector;
import java.sql.*;


/**
 * This class implements the connection to a MySQL Server
 */

public class MRMySQLDatabase implements MRDatabaseHandler{

    /**
     * A statement for the database, like 'SELECT bla FROM foo'
     */
    private Statement myStMt = null;

    /**
     * A result of a query
     */
    private ResultSet myResSet = null;
    /**
     * The database connection
     */
    private Connection conn = null;
    /**
     * if true, more output - useful for debugging
     */
    private final boolean DEBUG = false;

    /**
     * Init the connection to the database. Please edit the parameters to fit
     * our needs.
     * @return true if the connection works, otherwise throws an exception
     */

    public boolean init() {

        //Load the Driver
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
            return false;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return false;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }

        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost/mailrank?user=mailrank&password=mailrank");


        } catch (SQLException e) {
            // handle any errors
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
            return false;
        }
        return true;
    }

    /**
     * Closes the DB Connection
     * @return true if all goes well, otherwise throws an SQLException
     */

    public boolean close() {
        try {
            conn.close();
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
            return false;
        }
        return true;
    }

        /**
         * Checks if a user has already an entry in the DB
         *
         * @param mydata a MRData object
         * @return true if the user is found, otherwise false
         */

    private boolean isUserInDB(MRData mydata) {

        boolean returnvalue = false;
        try {

            myStMt = conn.createStatement();

            //Tries to find user in the DB
            myResSet = myStMt.executeQuery("SELECT ID" +
                    " FROM users WHERE" +
                    " User = \"" + mydata.getUser() + "\";");
            if (myResSet.next()) {
                returnvalue = true;
            } else {
                returnvalue = false;
            }
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        } finally {
            //release the resources

            if (myResSet != null) {
                try {
                    myResSet.close();
                } catch (SQLException e) {
                    myResSet = null;
                }
                if (myStMt != null) {
                    try {
                        myStMt.close();
                    } catch (SQLException e) {
                        myStMt = null;
                    }
                }
            }
        }
        return returnvalue;
    }

    /**
     * Checks if an entry for a specific spam mailaddress
     * is already in the DB
     * @param mydata a MRData object
     * @return true if the spam mailaddress is found
     */
    private boolean isEntryInSpamtable(MRData mydata) {

        /**
         * Method tries to find user/spamaddress pair in the DB
         *
         * @param mydata A MRData object
         * @return true if the user/spamaddress pair is found otherwise false
         */

        boolean returnvalue = false;
        try {
            myStMt = conn.createStatement();

            //Try to find Sender/Mailaddress pair in the DB
            myResSet = myStMt.executeQuery("SELECT User, Address" +
                    " FROM users,spam WHERE" +
                    " User = \"" + mydata.getUser() + "\"" +
                    " AND " +
                    "Address = \"" + mydata.getAddress() + "\"" +
                    " AND " +
                    "users.ID=spam.ID;"
                    );
            if (myResSet.next()) {
                returnvalue = true;
            } else {
                returnvalue = false;
            }
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        } finally {
            //release the resources

            if (myResSet != null) {
                try {
                    myResSet.close();
                } catch (SQLException e) {
                    myResSet = null;
                }
                if (myStMt != null) {
                    try {
                        myStMt.close();
                    } catch (SQLException e) {
                        myStMt = null;
                    }
                }
            }
        }

        return returnvalue;
    }

    /**
     * insert a new MRData object (spamaddress, score, count) into the spam table
     * @param mydata a MRData object
     */
    private void insertIntoSpamtable(MRData mydata) {

        try {
            myStMt = conn.createStatement();
            myResSet = myStMt.executeQuery("SELECT ID" +
                    " FROM users WHERE" +
                    " User = \"" + mydata.getUser() + "\";");
            myResSet.next();

            //add the data for the user
            myStMt.addBatch("START TRANSACTION;");
            myStMt.addBatch("INSERT INTO spam" +
                    " (ID, Address, Score, Count) " +
                    " VALUES (" +
                    myResSet.getString("ID") + ", " +
                    "\"" + mydata.getAddress() + "\", " +
                    mydata.getScore() + ", " +
                    mydata.getCount() +
                    ");");
            myStMt.addBatch("COMMIT;");
            myStMt.executeBatch();

        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        } finally {
            //release the resources

            if (myResSet != null) {
                try {
                    myResSet.close();
                } catch (SQLException e) {
                    myResSet = null;
                }
                if (myStMt != null) {
                    try {
                        myStMt.close();
                    } catch (SQLException e) {
                        myStMt = null;
                    }
                }
            }
        }
    }
    /**
     * Updates the spamtable (values for spamaddress, score, count)
     * @param mydata a MRData object
     */
    private void updateSpamtable(MRData mydata) {
        try {
            myStMt = conn.createStatement();
            myResSet = myStMt.executeQuery("SELECT users.ID, Count, Score" +
                    " FROM users,spam WHERE" +
                    " User = \"" + mydata.getUser() + "\"" +
                    " AND " +
                    " users.ID=spam.ID;");
            myResSet.next();

            //Do nothing if the count and score are the same as in the DB
            if ((mydata.getCount() != myResSet.getInt("Count")) ||
                (mydata.getScore() != myResSet.getDouble("Score"))
            ) {
                if (DEBUG) System.out.println("Actually starting the transaction");
                myStMt.addBatch("START TRANSACTION;");
                myStMt.addBatch("UPDATE spam SET" +
                        " Score = " + mydata.getScore() + "," +
                        " Count = " + mydata.getCount() +
                        " WHERE " +
                        " ID=" + myResSet.getString("ID") + " AND" +
                        " Address = " + "\"" + mydata.getAddress() + "\"" +
                        " ;");
                myStMt.addBatch("COMMIT;");
                myStMt.executeBatch();
            } else {
                if (DEBUG) System.out.println("I do nothing - the count and score are the same as in the DB");
            }
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        } finally {
            //release the resources

            if (myResSet != null) {
                try {
                    myResSet.close();
                } catch (SQLException e) {
                    myResSet = null;
                }
                if (myStMt != null) {
                    try {
                        myStMt.close();
                    } catch (SQLException e) {
                        myStMt = null;
                    }
                }
            }
        }
    }

    /**
     * Add a new user into the user table
     * @param mydata a MRData object
     */
    private void addNewUser(MRData mydata) {
        try {
            myStMt = conn.createStatement();
            myStMt.addBatch("START TRANSACTION;");
            myStMt.addBatch("INSERT INTO users" +
                    " (ID, User)" +
                    " VALUES (" +
                    "NULL, " +
                    "\"" + mydata.getUser() + "\");");
            myStMt.addBatch("COMMIT;");
            myStMt.executeBatch();

        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        } finally {
            //release the resources

            if (myResSet != null) {
                try {
                    myResSet.close();
                } catch (SQLException e) {
                    myResSet = null;
                }
                if (myStMt != null) {
                    try {
                        myStMt.close();
                    } catch (SQLException e) {
                        myStMt = null;
                    }
                }
            }
        }
    }

    /**
     * Writes the data (the data inside a MRData object)
     * into the database
     * @param myData a MRData object
     */
    public void setValues(MRData myData) {

        if (isUserInDB(myData)) {
            if (DEBUG) System.out.println("User is known");
            if (isEntryInSpamtable(myData)) {
                if (DEBUG) System.out.println("Entry for spamaddress already in table");
                if (DEBUG) System.out.println("Updating spam table");
                updateSpamtable(myData);
            } else {
                if (DEBUG) System.out.println("Entry for spamaddress is not in table");
                if (DEBUG) System.out.println("Inserting data into spam table");
                insertIntoSpamtable(myData);
            }

        } else {
            if (DEBUG) System.out.println("User not known: Adding new user");
            addNewUser(myData);
            if (DEBUG) System.out.println("Inserting data into spam table");
            insertIntoSpamtable(myData);
        }

    }

    /**
     * Testmethod, gets all entries sent by a specific user
     * @param myData a MRData object
     * @return a vector of MRData objects
     */
    public Vector getEntryByUser(MRData myData) {

        Vector myResult = new Vector();

        try {
            myStMt = conn.createStatement();


            //Try to find Sender in the DB

            myResSet = myStMt.executeQuery("SELECT User, Address, Score, Count" +
                    " FROM users,spam WHERE" +
                    " User = \"" + myData.getUser() + "\" AND" +
                    " users.ID=spam.ID;");

            while (myResSet.next()) {
                String sender = myResSet.getString("User");
                String address = myResSet.getString("Address");
                double score = myResSet.getDouble("Score");
                int count = myResSet.getInt("Count");
                if (DEBUG) System.out.println(sender + " " + address + " " + score + " " + count);
                MRData myDataObj = new MRData(sender, address, score, count);
                myResult.add(myDataObj);
            }

        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());

        } finally {
            //release the resources

            if (myResSet != null) {
                try {
                    myResSet.close();
                } catch (SQLException e) {
                    myResSet = null;
                }
                if (myStMt != null) {
                    try {
                        myStMt.close();
                    } catch (SQLException e) {
                        myStMt = null;
                    }
                }
            }
        }

        return myResult;
    }

    /**
     * Testmethod, gets all entries of a specific spam mailaddress
     * @param myData a MRData object
     * @return a vector of MRData objects
     */
    public Vector getEntryBySpamaddress(MRData myData) {

        Vector myResult = new Vector();

        try {
            myStMt = conn.createStatement();

            //Try to find Sender in the DB
            //myResSet = myStMt.executeQuery("SELECT Sender, Address, Score, Count" +
            //        " FROM mailrank WHERE" +
            //        " Address = \"" + myData.getAddress() + "\";");
            myResSet = myStMt.executeQuery("SELECT User, Address, Score, Count" +
                    " FROM users,spam WHERE" +
                    " users.ID=spam.ID AND" +
                    " Address = \"" + myData.getAddress() + "\";");

            while (myResSet.next()) {
                String sender = myResSet.getString("User");
                String address = myResSet.getString("Address");
                double score = myResSet.getDouble("Score");
                int count = myResSet.getInt("Count");
                if (DEBUG) System.out.println(sender + " " + address + " " + score + " " + count);
                MRData myDataObj = new MRData(sender, address, score, count);
                myResult.add(myDataObj);
            }

        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());

        } finally {
            //release the resources

            if (myResSet != null) {
                try {
                    myResSet.close();
                } catch (SQLException e) {
                    myResSet = null;
                }
                if (myStMt != null) {
                    try {
                        myStMt.close();
                    } catch (SQLException e) {
                        myStMt = null;
                    }
                }
            }
        }

        return myResult;
    }

    /**
     * Testmethod, gets all entries of a specific score
     * @param myData a MRData object
     * @return a vector of MRData objects
     */
    public Vector getEntryByScore(MRData myData) {

        Vector myResult = new Vector();

        try {
            myStMt = conn.createStatement();

            //Try to find Sender in the DB
            myResSet = myStMt.executeQuery("SELECT User, Address, Score, Count" +
                    " FROM users,spam WHERE" +
                    " users.ID=spam.ID AND" +
                    " Score = \"" + myData.getScore() + "\";");

            while (myResSet.next()) {
                String sender = myResSet.getString("User");
                String address = myResSet.getString("Address");
                double score = myResSet.getDouble("Score");
                int count = myResSet.getInt("Count");
                if (DEBUG) System.out.println(sender + " " + address + " " + score + " " + count);
                MRData myDataObj = new MRData(sender, address, score, count);
                myResult.add(myDataObj);
            }

        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());

        } finally {
            //release the resources

            if (myResSet != null) {
                try {
                    myResSet.close();
                } catch (SQLException e) {
                    myResSet = null;
                }
                if (myStMt != null) {
                    try {
                        myStMt.close();
                    } catch (SQLException e) {
                        myStMt = null;
                    }
                }
            }
        }

        return myResult;
    }

    /**
     * Testmethod, gets all entries of a specific count
     * @param myData a MRData object
     * @return a vector of MRData objects
     */
    public Vector getEntryByCount(MRData myData) {

        Vector myResult = new Vector();

        try {
            myStMt = conn.createStatement();

            //Try to find Sender in the DB
            myResSet = myStMt.executeQuery("SELECT User, Address, Score, Count" +
                    " FROM users,spam WHERE" +
                    " users.ID=spam.ID AND" +
                    " Count = \"" + myData.getCount() + "\";");

            while (myResSet.next()) {
                String sender = myResSet.getString("User");
                String address = myResSet.getString("Address");
                double score = myResSet.getDouble("Score");
                int count = myResSet.getInt("Count");
                if (DEBUG) System.out.println(sender + " " + address + " " + score + " " + count);
                MRData myDataObj = new MRData(sender, address, score, count);
                myResult.add(myDataObj);
            }

        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());

        } finally {
            //release the resources

            if (myResSet != null) {
                try {
                    myResSet.close();
                } catch (SQLException e) {
                    myResSet = null;
                }
                if (myStMt != null) {
                    try {
                        myStMt.close();
                    } catch (SQLException e) {
                        myStMt = null;
                    }
                }
            }
        }

        return myResult;
    }

}
