
import com.sleepycat.bind.tuple.TupleBinding;
import com.sleepycat.bind.tuple.TupleInput;
import com.sleepycat.bind.tuple.TupleOutput;

/**
 * Created by IntelliJ IDEA.
 * User: broso
 * Date: Aug 1, 2004
 * Time: 10:09:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class MRDataBinding extends TupleBinding{

    public void objectToEntry(Object object, TupleOutput output) {

        MRData myData = (MRData)object;

        output.writeString(myData.getUser());
        output.writeString(myData.getAddress());
        output.writeDouble(myData.getScore());
        output.writeInt(myData.getCount());
        }


    public Object entryToObject(TupleInput input) {

        String sender = input.readString();
        String address = input.readString();
        double score = input.readDouble();
        int count = input.readInt();

        MRData myData = new MRData(sender, address, score, count);
        

        return myData;
    }

}
