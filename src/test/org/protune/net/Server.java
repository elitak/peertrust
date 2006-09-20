package test.org.protune.net;

import java.io.IOException;
import org.protune.net.DispatcherPeer;

public class Server {

	public static void main(String[] args) throws IOException, ClassNotFoundException{
		String[] sa = {"DummyService"};
		DispatcherPeer dp = new DispatcherPeer("localhost", 1234, sa);
		dp.init();
	}

}
