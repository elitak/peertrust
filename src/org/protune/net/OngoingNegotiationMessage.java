package org.protune.net;

/**
 * In the <i>Protune</i> system we are mostly interested in negotiations among (pairs of) peers. A
 * negotiation (i) starts, (ii) goes on and (iii) finishes. The class {@link
 * org.protune.net.StartNegotiationMessage} (resp. <tt>OngoingNegotiationMessage</tt>,
 * {@link org.protune.net.EndNegotiationMessage}) is meant to represent a message the peers exchange
 * during (i) (resp. (ii), (iii)).
 * @author jldecoi
 */
public class OngoingNegotiationMessage implements NegotiationMessage {
	
	static final long serialVersionUID = 511;
	
}
