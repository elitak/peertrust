
import java.util.Properties;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * The mainclass for the whole project
 */
public class MRServer {
    /**
     * starts a new instance of MRSocket and MRMail
     * todo: using 2 Threads, so the 2 parts can be started
     * in parallel
     * @param args not used
     */
    public static void main(String[] args) {

        int port = -1;
        int popinterval = -1;


        Properties myProperties = new Properties();
        try {
            FileInputStream in = new FileInputStream("defaultProperties");
            myProperties.load(in);
            in.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        try {
            port = Integer.parseInt(myProperties.getProperty("socket.port"));
            popinterval = Integer.parseInt(myProperties.getProperty("mail.popinterval"));
        } catch (NumberFormatException e) {
            System.out.println(e.getMessage());
            System.out.println("Please verify your settings in the properties file.");
        }

        MRSocket mrsock = new MRSocket();
        MRMail mail = new MRMail();

        //if (port == -1) {
        //    System.out.println("You must specify the port!");
        //} else {
        //    mrsock.init(port);
        //}

        if (popinterval == -1)
            System.out.println("You must specify the interval!");
        else {
            mail.run(popinterval);
        }
    }
}
