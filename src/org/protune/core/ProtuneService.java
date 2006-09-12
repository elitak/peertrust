package org.protune.core;

import org.protune.net.*;
import org.protune.api.*;
import java.util.Vector;

public class ProtuneService extends Service {

	Status status;
	Checker checker;
	TerminationAlgorithm terminationAlgorithm;
	Filter filter;
	ActionSelectionFunction actionSelectionFunction;
	Policy policy;
	Pointer otherPeer;
	
	ProtuneService(
			Status s,
			Checker c,
			TerminationAlgorithm ta,
			Filter f,
			ActionSelectionFunction asf,
			Policy pl,
			Pointer pi
	){
		status = s;
		checker = c;
		terminationAlgorithm = ta;
		filter = f;
		actionSelectionFunction = asf;
		policy = pl;
		otherPeer = pi;
	}
	
	/**
	 * Works out the message received from the other Peer and sends it a new negotiation message.<br />
	 * <b>OPEN ISSUE:</b> How can I know whether the negotiation terminated (un)successfully?
	 * @param onm The message received from the other Peer.<br />
	 * <b>NOTE:</b> Must be an instance of {@link org.protune.core.ProtuneMessage}.
	 * @return The message to be sent to the other Peer.
	 */
	public NegotiationMessage eval(OngoingNegotiationMessage onm) throws WrongMessageTypeException{
		if(!(onm instanceof ProtuneMessage)) throw new WrongMessageTypeException(); 
		ProtuneMessage pm = (ProtuneMessage) onm;
		
		FilteredPolicy[] fpa = pm.getFilteredPolicies();
		Notification[] na = pm.getNotifications();
		
		status.add(Status.RECEIVED, fpa);
		status.add(Status.RECEIVED, na);
		for(int i=0; i<na.length; i++) try{
			status.add(Status.RECEIVED, checker.checkNotification(na[i]));
		} catch(UnknownNotificationException une){}
		
		if(terminationAlgorithm.terminate(status)) return new EndNegotiationMessage();
		
		Action[] aa = filter.extractActions(fpa);
		Vector<FilteredPolicy> vfp = new Vector<FilteredPolicy>();
		for(int i=0; i<aa.length; i++) try{
			vfp.add(filter.filter(policy, status, aa[i]));
		} catch(Exception e){}
		
		Vector<Action> unlocked = new Vector<Action>();
		Vector<FilteredPolicy> fpToSend = new Vector<FilteredPolicy>();
		for(int i=0; i<vfp.size(); i++)
			if(filter.isUnlocked(aa[i])) unlocked.add(aa[i]);
			else fpToSend.add(vfp.get(i));
		
		Action[] toPerform = new Action[0];
		toPerform = actionSelectionFunction.selectActions(unlocked.toArray(toPerform), status);
		fpa = fpToSend.toArray(fpa);
		Notification[] naToSend = Action.perform(toPerform);
		status.add(Status.SENT, fpa);
		status.add(Status.SENT, naToSend);
		
		return new ProtuneMessage(fpa, naToSend);
	}

}
