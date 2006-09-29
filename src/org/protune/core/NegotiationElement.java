package org.protune.core;

import java.util.Date;

import org.protune.api.Check;
import org.protune.api.FilteredPolicy;
import org.protune.api.Mapper;
import org.protune.api.Notification;

/**
 * As stated in {@link org.protune.core.Status}, a state should contain (at least)
 * <ul>
 * <li>sent/received {@link org.protune.api.FilteredPolicy}</li>
 * <li>{@link org.protune.api.Notification} of performed actions</li>
 * <li>{@link org.protune.api.Check} testing whether the notifications are (not) reliable</li>
 * </ul>
 * When inserted into a state, such entities are decorated with other information which are meant to
 * support advanced features. At present this additional information includes
 * <ul>
 * <li>whether the entity was sent/received</li>
 * <li>when the entity was added to the state</li>
 * <li>the number of the negotiation step during which the entity was added</li>
 * </ul>
 * A <tt>NegotiationElement</tt> object is a wrapper for these entities providing this additional
 * information. 
 * @author jldecoi
 */
public class NegotiationElement {
	
	public static final boolean SENT = true;
	public static final boolean RECEIVED = false;
	
	boolean sentOrReceived;
	Date timestamp;
	int negotiationStepNumber;
	/**
	 * Can be
	 * <ul>
	 * <li>either a {@link org.protune.api.FilteredPolicy}</li>
	 * <li>or a {@link org.protune.api.Notification}</li>
	 * <li>or a {@link org.protune.api.Check}</li>
	 * </ul>
	 */
	Object entity;
	
	public NegotiationElement(boolean b, int i, FilteredPolicy fp){
		sentOrReceived = b;
		timestamp = new Date();
		negotiationStepNumber = i;
		entity = fp;
	}
	
	public NegotiationElement(boolean b, int i, Notification n){
		sentOrReceived = b;
		timestamp = new Date();
		negotiationStepNumber = i;
		entity = n;
	}
	
	public NegotiationElement(boolean b, int i, Check c){
		sentOrReceived = b;
		timestamp = new Date();
		negotiationStepNumber = i;
		entity = c;
	}
	
	/**
	 * Returns whether the entity contained in the negotiation element (namely {@link
	 * org.protune.api.FilteredPolicy}, {@link org.protune.api.Notification} or {@link
	 * org.protune.api.Check}) was sent or received.
	 * @return <tt>true</tt> if the entity contained in the negotiation element was sent, <tt>false</tt>
	 * otherwise.
	 */
	public boolean wasSent(){
		return sentOrReceived;
	}
	
	/**
	 * Returns whether the entity contained in the negotiation element (namely {@link
	 * org.protune.api.FilteredPolicy}, {@link org.protune.api.Notification} or {@link
	 * org.protune.api.Check}) was received or sent.
	 * @return <tt>true</tt> if the entity contained in the negotiation element was received,
	 * <tt>false</tt> otherwise.
	 */
	public boolean wasReceived(){
		return !sentOrReceived;
	}
	
	/**
	 * Returns when the entity contained in the negotiation element (namely {@link
	 * org.protune.api.FilteredPolicy}, {@link org.protune.api.Notification} or {@link
	 * org.protune.api.Check}) was added to the state.
	 * @return When the entity contained in the negotiation element was added to the state.
	 */
	public Date getTimestamp(){
		return timestamp;
	}
	
	/**
	 * Returns the number of the negotiation step during which the entity contained in the negotiation
	 * element (namely {@link org.protune.api.FilteredPolicy}, {@link org.protune.api.Notification} or
	 * {@link org.protune.api.Check}) was added.
	 * @return The number of the negotiation step during which the entity contained in the negotiation
	 * element was added.
	 */
	public int getNegotiationStepNumber(){
		return negotiationStepNumber;
	}
	
	/**
	 * Returns the entity contained in the negotiation element (namely {@link
	 * org.protune.api.FilteredPolicy}, {@link org.protune.api.Notification} or {@link
	 * org.protune.api.Check}).
	 * @return the entity contained in the negotiation element.
	 */
	public Object getEntity(){
		return entity;
	}
	
	String accept(Mapper m){
		return m.toPrologRepresentation(this);
	}

}
