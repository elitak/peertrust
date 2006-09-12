package org.protune.core;

import java.util.Vector;
import java.util.Date;

import org.protune.api.*;

/**
 * The class {@link org.protune.core.Status} represents the state of a negotiation. So far a Status
 * contains
 * <ul>
 * <li>sent/received filtered policies</li>
 * <li>notifications of performed actions</li>
 * <li>checks which test whether the notifications are (not) truthful</li>
 * </ul>
 * Therefore the class {@link org.protune.core.Status} should provide methods allowing the addition of
 * such entities to the state itself. <em>How</em> this entities are stored into a status (i.e. the
 * internal representation of a state) is an implementation issue which most likely will change in the
 * future. At present a trivial solution was chosen.
 * @author jldecoi
 */
public class Status {
	
	public static final boolean SENT = true;
	public static final boolean RECEIVED = false;
	
	Vector<NegotiationStep> internalRepresentation;
	int currentNegotiationStepNumber;
	NegotiationStep currentNegotiationStep;
	/**
	 * {@link #SENT} if the last element was sent, {@link #RECEIVED}
	 * otherwise. Is initialised at {@link #SENT}
	 */
	private boolean lastAdded;
	
	Status(){
		internalRepresentation = new Vector<NegotiationStep>();
		currentNegotiationStepNumber = -1;
		lastAdded = Status.SENT;
	}
	
	void add(boolean b, FilteredPolicy fp){
		if(b == lastAdded) currentNegotiationStep.add(b, fp);
		else{
			currentNegotiationStepNumber++;
			currentNegotiationStep = new NegotiationStep();
			internalRepresentation.add(currentNegotiationStep);
			currentNegotiationStep.add(b, fp);
			lastAdded = !lastAdded;
		}
	}
	
	void add(boolean b, FilteredPolicy[] fpa){
		for(int i=0; i<fpa.length; i++) add(b, fpa[i]);
	}
	
	void add(boolean b, Notification n){
		if(b == lastAdded) currentNegotiationStep.add(b, n);
		else{
			currentNegotiationStepNumber++;
			currentNegotiationStep = new NegotiationStep();
			internalRepresentation.add(currentNegotiationStep);
			currentNegotiationStep.add(b, n);
			lastAdded = !lastAdded;
		}
	}
	
	void add(boolean b, Notification[] na){
		for(int i=0; i<na.length; i++) add(b, na[i]);
	}
	
	void add(boolean b, Check c){
		if(b == lastAdded) currentNegotiationStep.add(b, c);
		else{
			currentNegotiationStepNumber++;
			currentNegotiationStep = new NegotiationStep();
			internalRepresentation.add(currentNegotiationStep);
			currentNegotiationStep.add(b, c);
			lastAdded = !lastAdded;
		}
	}
	
	void add(boolean b, Check[] ca){
		for(int i=0; i<ca.length; i++) add(b, ca[i]);
	}
	
	FilteredPolicy[] getFilteredPolicies(int i){
		return internalRepresentation.get(i).getFilteredPolicies();
	}
	
	Notification[] getNotifications(int i){
		return internalRepresentation.get(i).getNotifications();
	}
	
	Check[] getChecks(int i){
		return internalRepresentation.get(i).getChecks();
	}
	
	NotificationReliable[] getReliableChecks(int i){
		return internalRepresentation.get(i).getReliableChecks();
	}
	
	int getCurrentNegotiationStepNumber(){
		return currentNegotiationStepNumber + 1;
	}
	
	/**
	 * Returns a string representation of the state as a Prolog theory. This theory is meant to be
	 * loaded by the inference engine and used in the filtering process as well as in testing whether an
	 * action is (un)locked and maybe also in the process of extracting actions to be executed from a
	 * filtered policy.<br />
	 * <b>OPEN ISSUE:</b> To be implemented.
	 * @return A string representation of the state as a Prolog theory. This theory is meant to be
	 * loaded by the inference engine and used in the filtering process as well as in testing whether an
	 * action is (un)locked and maybe also in the process of extracting actions to be executed from a
	 * filtered policy.<br />
	 * <b>OPEN ISSUE:</b> To be implemented.
	 */
	String toTheory(){
		return "";
	}
	
}

class NegotiationStep{
	
	Vector<NegotiationStepElement> stepItems;
	
	NegotiationStep(){
		stepItems = new Vector<NegotiationStepElement>();
	}
	
	void add(boolean b, FilteredPolicy fp){
		stepItems.add(new NegotiationStepElement(b, fp));
	}
	
	void add(boolean b, Notification n){
		stepItems.add(new NegotiationStepElement(b, n));
	}
	
	void add(boolean b, Check c){
		stepItems.add(new NegotiationStepElement(b, c));
	}
	
	FilteredPolicy[] getFilteredPolicies(){
		Vector<FilteredPolicy> v = new Vector<FilteredPolicy>();
		
		for(int i=0; i<stepItems.size(); i++){
			NegotiationStepElement se = stepItems.get(i); 
			if(se.containsFilteredPolicy()) v.add((FilteredPolicy) se.getObject()); 
		}
		
		FilteredPolicy[] fpa = new FilteredPolicy[0];
		return v.toArray(fpa);
	}
	
	Notification[] getNotifications(){
		Vector<Notification> v = new Vector<Notification>();
		
		for(int i=0; i<stepItems.size(); i++){
			NegotiationStepElement se = stepItems.get(i); 
			if(se.containsNotification()) v.add((Notification) se.getObject()); 
		}
		
		Notification[] na = new Notification[0];
		return v.toArray(na);
	}
	
	Check[] getChecks(){
		Vector<Check> v = new Vector<Check>();
		
		for(int i=0; i<stepItems.size(); i++){
			NegotiationStepElement se = stepItems.get(i); 
			if(se.containsCheck()) v.add((Check) se.getObject()); 
		}
		
		Check[] ca = new Check[0];
		return v.toArray(ca);
	}
	
	NotificationReliable[] getReliableChecks(){
		Vector<NotificationReliable> v = new Vector<NotificationReliable>();

		Check[] ca = getChecks();
		for(int i=0; i<ca.length; i++){
			Object o = stepItems.get(i).getObject();
			if(o instanceof NotificationReliable) v.add((NotificationReliable) o);
		}
		
		NotificationReliable[] nra = new NotificationReliable[0];
		return v.toArray(nra);
	}
	
}

class NegotiationStepElement{
	
	boolean sentOrReceived;
	/**
	 * Is set to
	 * <ul>
	 * <li>0</li>
	 * <li>1</li>
	 * <li>2</li>
	 * </ul>
	 * if {@link #FilteredPolicyOrNotificationOrCheck} is a
	 * <ul>
	 * <li>{@link org.protune.api.FilteredPolicy}</li>
	 * <li>{@link org.protune.api.Notification}</li>
	 * <li>{@link org.protune.api.Check}</li>
	 * </ul>
	 */
	int Class;
	Date timestamp;
	Object FilteredPolicyOrNotificationOrCheck;
	
	public NegotiationStepElement(boolean b, FilteredPolicy fp){
		sentOrReceived = b;
		timestamp = new Date();
		Class = 0;
		FilteredPolicyOrNotificationOrCheck = fp;
	}
	
	public NegotiationStepElement(boolean b, Notification n){
		sentOrReceived = b;
		timestamp = new Date();
		Class = 1;
		FilteredPolicyOrNotificationOrCheck = n;
	}
	
	public NegotiationStepElement(boolean b, Check c){
		sentOrReceived = b;
		timestamp = new Date();
		Class = 2;
		FilteredPolicyOrNotificationOrCheck = c;
	}
	
	public boolean containsFilteredPolicy(){
		return Class == 0;
	}
	
	public boolean containsNotification(){
		return Class == 1;
	}
	
	public boolean containsCheck(){
		return Class == 2;
	}
	
	public boolean wasSent(){
		return sentOrReceived;
	}
	
	public boolean wasReceived(){
		return !sentOrReceived;
	}
	
	public Date getTimestamp(){
		return timestamp;
	}
	
	public Object getObject(){
		return FilteredPolicyOrNotificationOrCheck;
	}
	
}