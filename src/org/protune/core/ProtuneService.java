package org.protune.core;

import org.protune.net.*;
import org.protune.api.*;

import java.util.Vector;
import java.text.ParseException;

/**
 * The class <tt>ProtuneService</tt> could be described as the most important class of the Protune
 * systems, indeed it implements the algorithm the whole negotiation bases on. The main steps of this
 * algorithm are sketched in an informal way in Fig. 1, a closer sight is of course provided by the
 * code.
 * <table border="1" cellspacing="0"><tbody><tr><td><pre>
 * {@link org.protune.net.NegotiationMessage NegotiationMessage} eval({@link org.protune.api.FilteredPolicy FilteredPolicy}[] fpa, {@link org.protune.api.Notification Notification}[] na){
 *    {@link org.protune.core.Status status}.add(fpa);
 *    staus.add(na);
 *    status.add({@link org.protune.core.Checker checker}.checkNotification(na))
 *    
 *    status.increaseNegotiationStepNumber();
 *    
 *    if({@link org.protune.core.FilterEngine filter}.isNegotiationSatisfied(status))
 *    	  return new {@link org.protune.net.EndNegotiationMessage EndNegotiationMessage}(new {@link org.protune.net.SuccessfulNegotiationResult SuccessfulNegotiationResult}());
 *    if({@link org.protune.core.TerminationAlgorithm terminationAlgorithm}.terminate(mapper, status))
 *    	  return new EndNegotiationMessage(new {@link org.protune.net.UnsuccessfulNegotiationResult UnsuccessfulNegotiationResult}());
 *    
 *    {@link org.protune.api.Action Action}[]	aa = filter.extractActions(fpa);
 *    FilteredPolicy[] fp = filter.filter(policy, status, aa));
 *    
 *    Action[] unlocked;
 *    FilteredPolicy[] fpToSend;
 *    for(int i=0; aa.length; i++)
 *       if(filter.isUnlocked(aa[i])) unlocked.add(aa[i]);
 *    	 else fpToSend.add(fp[i]);
 *    
 *    Action[] toPerform = {@link org.protune.core.ActionSelectionFunction actionSelectionFunction}.selectActions(toPerform, status);
 *    Notification[] naToSend = Action.perform(toPerform);
 *    
 *    status.addNegotiationElement(fpToSend);
 *    status.addNegotiationElement(naToSend);
 *    
 *    status.increaseNegotiationStepNumber();
 *    
 *    return new {@link org.protune.core.ProtuneMessage ProtuneMessage}(fpToSend, naToSend);
 * }
 * </pre></td></tr></tbody></table>
 * <b>Fig. 1</b> - Main step of the algorithm implemented by <tt>ProtuneService</tt>.<br/>
 * Basically the algorithm works out the message received from the other peer and sends it a new one.
 * During the computation the negotiation status (most likely) changes.
 * @author jldecoi
 */
public class ProtuneService extends Service {
	
	Status status;
	Checker checker;
	TerminationAlgorithm terminationAlgorithm;
	FilterEngine filter;
	ActionSelectionFunction actionSelectionFunction;
	Policy policy;
	Pointer otherPeer;
	Mapper mapper;
	
	ProtuneService(
			Status s,
			Checker c,
			TerminationAlgorithm ta,
			ProtuneFilterEngine f,
			ActionSelectionFunction asf,
			Policy pl,
			Pointer pi,
			Mapper m
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
	 * Works out the message received from the other Peer and sends it a new negotiation message.
	 * @param onm The message received from the other Peer.<br />
	 * <b>NOTE:</b> Must be an instance of {@link org.protune.core.ProtuneMessage}.
	 * @return The message to be sent to the other Peer.
	 */
	public NegotiationMessage eval(OngoingNegotiationMessage onm) throws WrongMessageTypeException{
		if(!(onm instanceof ProtuneMessage)) throw new WrongMessageTypeException(); 
		ProtuneMessage pm = (ProtuneMessage) onm;
		
		FilteredPolicy[] fpa = pm.getFilteredPolicies();
		Notification[] na = pm.getNotifications();
		
		int negotiationStep = status.getCurrentNegotiationStepNumber();
		for(int i=0; i<fpa.length; i++)
			status.addNegotiationElement(new NegotiationElement(NegotiationElement.RECEIVED, negotiationStep, fpa[i]));
		for(int i=0; i<na.length; i++)
			status.addNegotiationElement(new NegotiationElement(NegotiationElement.RECEIVED, negotiationStep, na[i]));
		for(int i=0; i<na.length; i++) try{
			status.addNegotiationElement(new NegotiationElement(
					NegotiationElement.RECEIVED, negotiationStep, checker.checkNotification(na[i])
			));
		} catch(UnknownNotificationException une){
			// If the checker is not able to check the notification, a further check is simply not
			// added.
		}
		
		status.increaseNegotiationStepNumber();
		negotiationStep = status.getCurrentNegotiationStepNumber();
		
		if(filter.isNegotiationSatisfied(status))
			return new EndNegotiationMessage(new SuccessfulNegotiationResult());
		if(terminationAlgorithm.terminate(mapper, status))
			return new EndNegotiationMessage(new UnsuccessfulNegotiationResult());
		
		Vector<Action> v = new Vector<Action>();
		Action[] aa = new Action[0];
		for(int i=0; i<fpa.length; i++){
			aa = filter.extractActions(fpa[i]);
			for(int j=0; i<aa.length; j++) v.add(aa[i]);
		}
		aa = v.toArray(aa);
		
		Vector<FilteredPolicy> vfp = new Vector<FilteredPolicy>();
		for(int i=0; i<aa.length; i++) try{
			vfp.add(filter.filter(policy, status, aa[i]));
		}
		catch(ParseException pe){
			// It should not happen.
		}
		catch(Exception e){}
		
		Vector<Action> unlocked = new Vector<Action>();
		Vector<FilteredPolicy> fpToSend = new Vector<FilteredPolicy>();
		for(int i=0; i<vfp.size(); i++) try{
			if(filter.isUnlocked(aa[i])) unlocked.add(aa[i]);
			else fpToSend.add(vfp.get(i));
		} catch(Exception e){}
		
		Action[] toPerform = new Action[0];
		toPerform = actionSelectionFunction.selectActions(unlocked.toArray(toPerform), status);
		fpa = fpToSend.toArray(fpa);
		Notification[] naToSend = Action.perform(toPerform);
		for(int i=0; i<fpa.length; i++)
			status.addNegotiationElement(new NegotiationElement(NegotiationElement.SENT, negotiationStep, fpa[i]));
		for(int i=0; i<naToSend.length; i++)
			status.addNegotiationElement(new NegotiationElement(NegotiationElement.SENT, negotiationStep, naToSend[i]));
		
		status.increaseNegotiationStepNumber();
		
		return new ProtuneMessage(fpa, naToSend);
	}

}
