
import com.sleepycat.je.SecondaryKeyCreator;
import com.sleepycat.je.SecondaryDatabase;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.bind.tuple.TupleBinding;

import java.io.UnsupportedEncodingException;

/**
 * Created by IntelliJ IDEA.
 * User: broso
 * Date: Aug 2, 2004
 * Time: 5:11:08 PM
 * To change this template use File | Settings | File Templates.
 */
public class MRAddressKeyCreator implements SecondaryKeyCreator{

    private TupleBinding binding;

    public MRAddressKeyCreator(TupleBinding binding) {
        this.binding = binding;
    }

    /**
     * Creates the Index of Addresses in the secondary Database
     * @param mySecDB The secondary database
     * @param myKey The primary key
     * @param myData The data containing the new key
     * @param myNewKey The new key created from the data
     * @return
     */
    public boolean createSecondaryKey(SecondaryDatabase mySecDB,
                                      DatabaseEntry myKey, 
                                      DatabaseEntry myData, 
                                      DatabaseEntry myNewKey) {

        if (myData == null) {
            return false;
        } else {
            MRData myMRData = (MRData) binding.entryToObject(myData);
            String address = myMRData.getAddress();
            try {
                myNewKey.setData(address.getBytes("UTF-8"));
            } catch (UnsupportedEncodingException e) {
                // Will never occur, the primary DB will not run without UTF-8
                System.out.println("UTF-8 not supported on this platform");
                System.exit(-1);
            }
            return true;
        }

    }
}
