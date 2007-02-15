package org.protune.net;

import java.io.Serializable;

/**
 * Interface <tt>NegotiationResult</tt> represents the generic result of a negotiation. So far this
 * interface is implemented by the classes {@link org.protune.net.SuccessfulNegotiationResult} and
 * {@link org.protune.net.UnsuccessfulNegotiationResult}. Exensions are possible in the future.
 * @author jldecoi
 */
public interface NegotiationResult extends Serializable{}
