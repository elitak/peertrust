package org.protune.net;

/**
 * In the <tt>Protune</tt> system we are mostly interested in negotiations among (pairs of) peers. A
 * negotiation (i) starts, (ii) goes on and (iii) finishes. The class {@link
 * org.protune.net.StartNegotiationMessage} (resp. {@link org.protune.net.OngoingNegotiationMessage},
 * <tt>EndNegotiationMessage</tt>) is meant to represent a message the peers exchange during (i) (resp.
 * (ii), (iii)).<br />
 * Therefore an <tt>EndNegotiationMessage</tt> should wrap all information needed to end a
 * negotiation. According to the <tt>Protune</tt> protocol, so far this information is just a
 * {@link org.protune.net.NegotiationResult}.
 * @author jldecoi
 */
public class EndNegotiationMessage implements NegotiationMessage {
	
	static final long serialVersionUID = 611;
	NegotiationResult negotiationResult;
	
	public EndNegotiationMessage(NegotiationResult nr){
		negotiationResult = nr;
	}
	
	NegotiationResult getNegotiationResult(){
		return negotiationResult;
	}
	
}
