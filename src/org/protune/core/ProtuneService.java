package org.protune.core;

import java.text.ParseException;

import org.protune.api.Action;
import org.protune.api.FilteredPolicy;
import org.protune.api.Goal;
import org.protune.api.Notification;
import org.protune.api.PrologEngine;
import org.protune.api.QueryException;
import org.protune.net.EndNegotiationMessage;
import org.protune.net.NegotiationMessage;
import org.protune.net.OngoingNegotiationMessage;
import org.protune.net.Service;
import org.protune.net.SuccessfulNegotiationResult;
import org.protune.net.UnsuccessfulNegotiationResult;
import org.protune.net.WrongMessageTypeException;

/**
 * The class <tt>ProtuneService</tt> could be described as the most important class of the <i>Protune</i>
 * system, indeed it implements the algorithm the whole negotiation bases on. The main steps of this
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
 *    {@link org.protune.api.Action Action}[] aa = filter.extractActions(fpa);
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
public abstract class ProtuneService extends Service {

	protected PrologEngine engine;
	protected Checker checker;
	
	public NegotiationMessage eval(OngoingNegotiationMessage onm) throws WrongMessageTypeException{
		if(!(onm instanceof ProtuneMessage)) throw new WrongMessageTypeException();
		ProtuneMessage pm = (ProtuneMessage) onm;
		
		Goal g = pm.getGoal();
		FilteredPolicy fp = pm.getFilteredPolicy();
		Notification[] na = pm.getNotifications();
		
		engine.addReceived(fp);
		for(int i=0; i<na.length; i++) engine.addReceived(na[i]);
		for(int i=0; i<na.length; i++) try{
			engine.add(checker.checkNotification(na[i]));
		} catch(UnknownNotificationException une){
			// If the checker is not able to check the notification, a further check is simply not
			// added.
		}
		
		engine.increaseNegotiationStepNumber();
		
		Action[] laa;
		try {
			laa = engine.extractLocalActions(g);
			while(laa.length!=0){
				Notification[] lna = new Notification[laa.length];
				for(int i=0; i<lna.length; i++){
					lna[i] = laa[i].perform();
					engine.addLocal(lna[i]);
				}
				laa = engine.extractLocalActions(g);
			}
		} catch (QueryException e1) {
			// Should not happen.
		} catch (ParseException e1) {
			// Should not happen.
		}
		
		try {
			if(engine.isNegotiationSatisfied(g))
				return new EndNegotiationMessage(new SuccessfulNegotiationResult());
			if(engine.terminate())
				return new EndNegotiationMessage(new UnsuccessfulNegotiationResult());
		} catch (QueryException e) {
			// Should not happen.
		}
		
		Action[] eaa = new Action[0];
		try {
			eaa = engine.extractExternalActions(g);
		} catch (QueryException e1) {
			// Should not happen.
		} catch (ParseException e1) {
			// Should not happen.
		}
		Notification[] ena = new Notification[eaa.length]; 
		for(int i=0; i<eaa.length; i++) ena[i] = eaa[i].perform();
		
		try {
			fp = engine.filter(g);
		} catch (QueryException e1) {
			// Should not happen.
		} catch (ParseException e1) {
			// Should not happen.
		}
		
		engine.addSent(fp);
		for(int i=0; i<ena.length; i++) engine.addSent(ena[i]);
		
		engine.increaseNegotiationStepNumber();
		
		return new ProtuneMessage(g, fp, ena);
	}

}
