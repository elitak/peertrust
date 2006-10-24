package test.org.protune.net;

import java.io.IOException;
import org.protune.net.DispatcherPeer;

public class Server {

	public static void main(String[] args) throws IOException, ClassNotFoundException{
		//System.out.println(0);
		String[] sa = {"DummyService"};
		//System.out.println(1);
		DispatcherPeer dp = new DispatcherPeer("localhost", 1234, sa);
		//System.out.println(2);
		dp.init();
		System.out.println(3);
	}

}
