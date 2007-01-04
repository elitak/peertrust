package org.protune.net;

import java.io.File;

import org.protune.api.TuPrologWrapper;
import org.protune.core.DummyChecker;
import org.protune.core.ProtuneService;

public class TestProtuneService extends ProtuneService {

	public TestProtuneService(Pointer p){
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
