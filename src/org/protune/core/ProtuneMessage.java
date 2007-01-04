package org.protune.core;

import org.protune.net.OngoingNegotiationMessage;
import org.protune.api.*;

/**
 * A {@link org.protune.net.OngoingNegotiationMessage} particularly suited for the <i>Protune</i>
 * environment: each <tt>ProtuneMessage</tt> contains a set of {@link org.protune.api.FilteredPolicy}
 * and a set of {@link org.protune.api.Notification}.
 * @author jldecoi
 */
public class ProtuneMessage extends OngoingNegotiationMessage {
	
	static final long serialVersionUID = 5111;
	
	Goal goal;
	FilteredPolicy filteredPolicy;
	Notification[] notifications;
	
	public ProtuneMessage(Goal g, FilteredPolicy fp, Notification[] na){
		goal = g;
		filteredPolicy = fp;
		notifications = na;
	}
	
	public Goal getGoal(){
		return goal;
	}
	
	public FilteredPolicy getFilteredPolicy(){
		return filteredPolicy;
	}
	
	public Notification[] getNotifications(){
		return notifications;
	}
	
}
