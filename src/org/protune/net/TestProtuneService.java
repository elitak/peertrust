package org.protune.net;

import org.protune.core.ProtuneService;
import org.protune.core.DummyChecker;
import org.protune.api.LoadTheoryException;
import org.protune.api.TuPrologWrapper;

public class TestProtuneService extends ProtuneService {

	public TestProtuneService(Pointer p){
		otherPeer = p;
		checker = new DummyChecker();
		engine = new TuPrologWrapper();
		try {
			engine.loadTheory("./Server.txt");
		}
		catch (LoadTheoryException e) {
			System.out.println("LoadTheoryException");
		}
	}
	
}
