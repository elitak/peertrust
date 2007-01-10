package org.protune.net;

/**
 * In the <i>Protune</i> system we are mostly interested in negotiations among (pairs of) peers. A
 * negotiation (i) starts, (ii) goes on and (iii) finishes. The class {@link
 * org.protune.net.StartNegotiationMessage} (resp. {@link org.protune.net.OngoingNegotiationMessage},
 * <tt>EndNegotiationMessage</tt>) is meant to represent a message the peers exchange during (i) (resp.
 * (ii), (iii)).<br />
 * Therefore an <tt>EndNegotiationMessage</tt> should wrap all information needed to terminate a
 * negotiation. According to the <i>Protune</i> protocol, so far this information is just a
 * {@link org.protune.net.NegotiationResult}.
 * @author jldecoi
 */
public class EndNegotiationMessage implements NegotiationMessage {
	
	static final long serialVersionUID = 611;
	private NegotiationResult negotiationResult;
	
	public EndNegotiationMessage(NegotiationResult nr){
		negotiationResult = nr;
	}
	
	public NegotiationResult getNegotiationResult(){
		return negotiationResult;
	}
	
}
