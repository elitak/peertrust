


/**
 * Parses given strings, that look like:
 * command:userhash:spamhash:score:count
 * or
 * command:{userhash|spamhash|score|count}
 */
public class MRDataParser {
    /**
     * if true, more output - useful for debugging
     */
    final static boolean DEBUG = false;
    private String myCommand = null;
    private String mySender = null;
    private String myAddress = null;
    private double myScore = 0.0;
    private int myCount = 0;

    /**
     * Parses a line that have to look like:
     * in the case of setValues: setValues:userhash:spamhash:score:count
     * in the case of getEntryByUser: getEntryByUser:userhash
     * in the case of getEntryBySpamaddress: getEntryBySpamaddress:spamhash
     * in the case of getEntryByScore: getEntryByScore:score
     * in the case of getEntryByCount: getEntryByCount:count
     * Otherwise the String will be discared and an empty MRData object is returned
     * For a new command this parse have to be extended
     * @param line a String that fullfills the above requirements
     * @return a MRData object with the data given, in the case of malformed data empty
     */
    public MRData parse(String line) {
        String Data[];

        Data = line.split(":");

        if (DEBUG) {
            System.out.println("Processing: " + line);
            for (int i = 0; i < Data.length; i++) {
                System.out.println("Parser:" + Data[i]);
            }
        }

        /*
         * This String Array is used to store the data:
         * [0] is the command
         * [1] is the hash of the mailaddress of the user
         * [2] is the hash of the spamaddress
         * [3] is the score
         * [4] is the count
         *
         * Received data must be like
         * command:hashOfSender:hash:score:count
         *
         * in case of getValues only fields [0] and [1]
         */

        if (line.startsWith("setValues") && Data.length == 5) {

            myCommand = Data[0];
            mySender = Data[1];
            myAddress = Data[2];

            try {
                myScore = Double.parseDouble(Data[3]);
                myCount = Integer.parseInt(Data[4]);

            } catch (NumberFormatException e) {

                if (DEBUG) {
                    System.out.println(e.getMessage());
                    System.out.println("Malformed Data received");
                }

            }
            return new MRData(myCommand, mySender, myAddress, myScore, myCount);


        } else if (line.startsWith("getEntryByUser") && Data.length == 2) {

            myCommand = Data[0];
            mySender = Data[1];
            myAddress = "";
            myScore = 0.0;
            myCount = 0;

            return new MRData(myCommand, mySender, myAddress, myScore, myCount);


        } else if (line.startsWith("getEntryBySpamaddress") && Data.length == 2) {

            myCommand = Data[0];
            mySender = "";
            myAddress = Data[1];
            myScore = 0.0;
            myCount = 0;

            return new MRData(myCommand, mySender, myAddress, myScore, myCount);

        } else if (line.startsWith("getEntryByScore") && Data.length == 2) {

            myCommand = Data[0];
            mySender = "";
            myAddress = "";

            try {
                myScore = Double.parseDouble(Data[1]);

            } catch (NumberFormatException e) {

                if (DEBUG) {
                    System.out.println("Malformed Data received");
                }

            }

            myCount = 0;

            return new MRData(myCommand, mySender, myAddress, myScore, myCount);

        } else if (line.startsWith("getEntryByCount") && Data.length == 2) {

            myCommand = Data[0];
            mySender = "";
            myAddress = "";
            myScore = 0.0;

            try {
                myCount = Integer.parseInt(Data[1]);

            } catch (NumberFormatException e) {
                System.out.println("Malformed Data received");

            }

            return new MRData(myCommand, mySender, myAddress, myScore, myCount);

        } else {
            if (DEBUG) {
                System.out.println("Mailformed Data received");
            }

            return new MRData(myCommand, mySender, myAddress, myScore, myCount);
        }

    }
}
