package test.org.protune;

import org.protune.net.NegotiationMessage;
import org.protune.net.OngoingNegotiationMessage;
import org.protune.net.Pointer;
import org.protune.net.Service;

/**
 * Dummy implementation of the abstract class {@link org.protune.net.Service}, which echoes the received
 * messages.
 * @author jldecoi
 */
public class DummyService extends Service {
	
	public DummyService(Pointer p){
		otherPeer = p;
	}

	public NegotiationMessage eval(OngoingNegotiationMessage onm) {
		return onm;
	}

}
