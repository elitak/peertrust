
import com.sleepycat.bind.tuple.TupleBinding;
import com.sleepycat.je.*;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: broso
 * Date: Jul 30, 2004
 * Time: 3:30:27 PM
 * To change this template use File | Settings | File Templates.
 */
public class MRDatabase { //todo evtl. extends Thread ??

    private MRMessage mrmessage = null;

    public MRDatabase(MRMessage mrmessage) {
        this.mrmessage = mrmessage;
    }
    //Later with properties

    final String DATADIR = "/Users/broso/Desktop/Mailrankserver/classes/dbdata";
    final boolean DEBUG = true;

    private Environment myDBEnv = null;
    private Database myDB = null;
    private SecondaryDatabase mySecDB = null;
    private Transaction trans = null;


    public boolean run() {//todo restliche commandos implementieren
        if (mrmessage.getCommand() == "setValues") {
            for (int i =0; i<mrmessage.getMRData().size(); i++) {
                 setValues((MRData) mrmessage.getMRData().get(i));
            }
            //while (mrmessage.getMRData().listIterator().hasNext()) {
            //    MRData myData = (MRData) mrmessage.getMRData().listIterator().next();
            //    setValues(myData);
            //}

        } else if (mrmessage.getCommand() == "getEntryByUser") {
            Vector result = new Vector();  //Vector of Vectors including MRData Objects
            for (int i =0; i<mrmessage.getMRData().size(); i++) {

                 result.add(getEntryBySender((MRData) mrmessage.getMRData().get(i)));

              }
              mrmessage.setMRData(result);
            }

    return true;
    }

    public boolean init() {
        try {

            /*
             * Open the environment. Create it if it does not already exist.
             */

            EnvironmentConfig envConf = new EnvironmentConfig();
            envConf.setAllowCreate(true);
            envConf.setTransactional(true);
            myDBEnv = new Environment(new File(DATADIR), envConf);

            /*
             * Open the database. Create it if it does not already exist.
             */

            Transaction trans = myDBEnv.beginTransaction(null, null);
            DatabaseConfig dbConf = new DatabaseConfig();
            dbConf.setAllowCreate(true);
            dbConf.setTransactional(true);
            //dbConf.setSortedDuplicates(true);

            // The secondary DB
            SecondaryConfig secDBConf = new SecondaryConfig();
            secDBConf.setAllowCreate(true);
            secDBConf.setSortedDuplicates(true);
            secDBConf.setTransactional(true);

            TupleBinding mySecBinding = new MRDataBinding();
            MRAddressKeyCreator myAdrKC = new MRAddressKeyCreator(mySecBinding);
            secDBConf.setKeyCreator(myAdrKC);

            String secDbName = "AddressIndex";

            //Open the primary DB
            myDB = myDBEnv.openDatabase(trans, DATADIR, dbConf);
            //Open the secondary DB
            mySecDB = myDBEnv.openSecondaryDatabase(null, secDbName, myDB, secDBConf);

            trans.commit();

        } catch (DatabaseException dbe) {

            System.out.println("Please create the directory: " + DATADIR);
            return false;
        }
        return true;
    }

    public boolean close() {
        try {
            mySecDB.close();
            myDB.close();
            myDBEnv.close();
        } catch (DatabaseException e) {
            System.out.println("Houston we've got a big problem. Can't close the DB and the environment");
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public Environment getEnvironment() {
        return myDBEnv;
    }

    public Database getDatabase() {
        return myDB;
    }

    public SecondaryDatabase getSecondaryDatabase() {
        return mySecDB;
    }

    public Vector getEntryByAddress(MRData myData) {


        if (DEBUG) System.out.println("getEntryBySpamaddress received. Key: " + myData.getAddress());
        //todo Secondary indexes - suchen darŸber

        try {
            trans = myDBEnv.beginTransaction(null, null);
            TupleBinding dataBinding = new MRDataBinding();
            DatabaseEntry key = null;
            try {
                key = new DatabaseEntry(myData.getAddress().getBytes("UTF-8"));
            } catch (UnsupportedEncodingException e) {
                System.out.println("UTF-8 is not supported on this platform.");
                //this.interrupt();
            }
            DatabaseEntry data = new DatabaseEntry();


            Cursor myCursor = myDB.openCursor(trans, null);

            Vector result = new Vector();

            if (myCursor.getCurrent(key, data, LockMode.DEFAULT) == OperationStatus.NOTFOUND) {
                if (DEBUG) System.out.println("Entry not found");

                myCursor.close();
                trans.commit();
                mrmessage.setStatus("NOTFOUND");
                return null;

            } else {
                while (myCursor.getNext(key, data, LockMode.DEFAULT) == OperationStatus.SUCCESS) {
                    MRData mydata = (MRData) dataBinding.entryToObject(data);
                    result.add(mydata);
                }

                myCursor.close();
                trans.commit();
                mrmessage.setStatus("OK");
                return result;
            }

        } catch (DatabaseException e) {
            System.out.println("Transaction failed!!");
            System.out.println("Can't clean up!!");

            //this.interrupt();

        }
        return null;
    }

    public Vector getEntryBySender(MRData myData) {


        if (DEBUG) System.out.println("getEntryByUser received. Key: " + myData.getUser());


        try {
            trans = myDBEnv.beginTransaction(null, null);
            TupleBinding dataBinding = new MRDataBinding();
            DatabaseEntry key = null;
            try {
                key = new DatabaseEntry(myData.getUser().getBytes("UTF-8"));
            } catch (UnsupportedEncodingException e) {
                System.out.println("UTF-8 is not supported on this platform.");
                //this.interrupt();
            }
            DatabaseEntry data = new DatabaseEntry();


            Cursor myCursor = myDB.openCursor(trans, null);

            //OperationStatus status = myDB.get(trans, key, data, null);

            Vector result = new Vector();
            //Note: return Vector of MRData Object(s)
            //ToDO if Abfrage anpassen

            //if (myCursor.getNext(key, data, LockMode.DEFAULT) == OperationStatus.NOTFOUND) {
            //    if (DEBUG) System.out.println("Entry not found");
            //    mrmessage.setStatus("NOTFOUND");

            //    myCursor.close();
            //    trans.commit();
            //    return null;      todo: nicht null sondern leeres Object zurŸckgeben

            //} else {
                while (myCursor.getNext(key, data, LockMode.DEFAULT) == OperationStatus.SUCCESS) {
                    if (DEBUG) System.out.println("Entry found for " + myData.getUser());
                    MRData mydata = (MRData) dataBinding.entryToObject(data);
                    if (DEBUG) System.out.println(mydata.getUser() + " " + mydata.getAddress() + " " + mydata.getScore() + " " + mydata.getCount());
                    result.add(mydata);
             //   }

                myCursor.close();
                trans.commit();
                mrmessage.setStatus("OK");
                return result;
            }

        } catch (DatabaseException e) {
            System.out.println("Transaction failed!!");
            System.out.println("Can't clean up!!");

            //this.interrupt();

        }
        return null;
    }

    public void setValues(MRData myData) {


        if (DEBUG) System.out.println("sendValues received");
        if (DEBUG) System.out.println("Params: " + myData.getUser() + " " + myData.getAddress() + " " + myData.getScore() + " " + myData.getCount());

        try {
            trans = myDBEnv.beginTransaction(null, null);
            TupleBinding dataBinding = new MRDataBinding();

            DatabaseEntry myKey = null;
            DatabaseEntry mySecKey = null;
            try {
                myKey = new DatabaseEntry(myData.getUser().getBytes("UTF-8"));
                mySecKey = new DatabaseEntry(myData.getAddress().getBytes("UTF-8"));
            } catch (UnsupportedEncodingException e) {
                System.out.println("UTF-8 is not supported on this platform.");

            }
            DatabaseEntry data = new DatabaseEntry();
            dataBinding.objectToEntry(myData, data);
            DatabaseEntry toReplace = new DatabaseEntry();


            SecondaryCursor mySecCursor = mySecDB.openSecondaryCursor(trans, null);

            /*
            * Try to retrieve an Entry
            * The Sender and Address are used to find an Entry.
            * These Sender/Address pairs are unique.
            * retrieve the old one and overwrite
            * OR
            * Simply add to the Database.
            */

            if (mySecCursor.getSearchBoth(mySecKey, myKey, toReplace, LockMode.DEFAULT) == OperationStatus.SUCCESS) {
                // On the Position found, delete the old data
                // and put in the new data
                mySecCursor.delete();        //todo nochmla genau prŸfen, EintrŠge stimmen nicht
                //todo insbesondere put
                //todo evtl. kŸnstliche ID einfŸhren und SecIndexes Ÿber Sender und Address

                OperationStatus status = myDB.put(trans, myKey, data);
                if (status != OperationStatus.SUCCESS) {
                    mrmessage.setStatus("NOTOK");
                    if (DEBUG) System.out.println("NOTOK");
                    mySecCursor.close();
                    trans.abort();
                    throw new DatabaseException("Data insertion got status " +
                            status);
                } else {
                    if (DEBUG) System.out.println("OK");
                    mySecCursor.close();
                    trans.commit();
                    mrmessage.setStatus("OK");

                }
            } else {
                OperationStatus status = myDB.put(trans, myKey, data);
                if (status != OperationStatus.SUCCESS) {
                    mrmessage.setStatus("NOTOK");
                    if (DEBUG) System.out.println("NOTOK");
                    mySecCursor.close();
                    trans.abort();
                    throw new DatabaseException("Data insertion got status " +
                            status);
                } else {
                    if (DEBUG) System.out.println("OK");
                    mySecCursor.close();
                    trans.commit();
                    mrmessage.setStatus("OK");

                }
            }


        } catch (DatabaseException e) {
            System.out.println("Transaction failed");
            e.printStackTrace();

        }

    }


}
