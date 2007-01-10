package test.org.protune;

import java.io.File;

import org.protune.api.TuPrologWrapper;
import org.protune.core.ProtuneService;
import org.protune.net.Pointer;

public class TestServer extends ProtuneService {

	public TestServer(Pointer p){
		otherPeer = p;
		checker = new DummyChecker();
		engine = new TuPrologWrapper();
		try {
			File f = new File("C:/Programme/eclipse-SDK-3.2-win32/eclipse/workspace/Protune/src/Server.txt");
			engine.loadTheory(f);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
