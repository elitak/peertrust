


/**
 * This class encapsulate all the data.
 * Must be extended when trustvalues implemented.
 *
 */

public class MRData {
    /**
     * A command that can be one of the following:
     * getValues, getEntryByUser, getEntryBySpamaddress, getEntryByScore, getEntryByCount
     * The getEntryBy* commands are just for testing purposes
     */
    private String command;
    /**
     * A score is a double and is calculated in spamassassin, can be negative.
     * SCORE is the overall score of all tests made for a mail.
     * Every sender has an entry in the auto-white-list, together with the longterm-score MEAN.
     * This score is the sum of all scores made in the past for this sender (TOTAL) divided by the
     * count of all received mails from this sender (COUNT). The portion of the auto-white-list on
     * the all-over-score (FINALSCORE) can the changed with the auto-white-list factor (FACTOR)
     * FINALSCORE = SCORE + (( TOTAL / COUNT ) ? SCORE) *? FACTOR
     */
    private double score;
    /**
     * A count is always an integer and reflects the count of mail I've received from
     * a specific sender.
     */
    private int count;
    /**
     * The hash of the emailaddress of user sending this data
     */
    private String user;
    /**
     * The hash of the spam-mailaddress of a sender, sending me unwanted messages
     */
    private String address;

    /**
     * This constructor is used, when querying the DB.
     * All the data can be get out of the autowhitelist of spamassassin
     * see the perlclients included in this distribution
     * @param command getValues, getEntryByUser, getEntryBySpamaddress, getEntryByScore, getEntryByCount
     * @param user the hash of the usermailaddress
     * @param address the hash of the spam-mailaddress
     * @param score the score (see autowhitelist in spamassassin)
     * @param count the count (see autowhitelist in spamassassin)
     */
    MRData(String command, String user, String address, double score, int count) {
        this.command = command;
        this.user = user;
        this.address = address;
        this.score = score;
        this.count = count;
    }

    /**
     * This constructor is used, when receiving the result of a query.
     * All the data can be get out of the autowhitelist of spamassassin
     * see the perlclients included in this distribution
     *
     * @param user the hash of the usermailaddress
     * @param address the hash of the spam-mailaddress
     * @param score the score (see autowhitelist in spamassassin)
     * @param count the count (see autowhitelist in spamassassin)
     */

    MRData(String user, String address, double score, int count) {
        this.user = user;
        this.address = address;
        this.score = score;
        this.count = count;
    }

    public void setCommand(String data) {
        command = data;
    }

    public String getCommand() {
        return command;
    }

    public void setUser(String data) {
        user = data;
    }

    public String getUser() {
        return user;
    }

    public void setScore(double data) {
        score = data;
    }

    public void setCount(int data) {
        count = data;
    }

    public double getScore() {
        return score;
    }

    public int getCount() {
        return count;
    }

    public void setAddress(String data) {
        address = data;
    }

    public String getAddress() {
        return address;
    }

    public String toString() {
        return command + ":" + user + ":" + address + ":" + score + ":" + count;
    }
}