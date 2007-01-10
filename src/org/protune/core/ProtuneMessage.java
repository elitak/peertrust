package org.protune.core;

import org.protune.net.OngoingNegotiationMessage;

/**
 * A {@link org.protune.net.OngoingNegotiationMessage} particularly suited for the <i>Protune</i>
 * environment: each <tt>ProtuneMessage</tt> contains a set of {@link org.protune.core.FilteredPolicy}
 * and a set of {@link org.protune.core.Notification}.
 * @author jldecoi
 */
public class ProtuneMessage extends OngoingNegotiationMessage {
	
	static final long serialVersionUID = 5111;
	
	private Goal goal;
	private FilteredPolicy filteredPolicy;
	private Notification[] notifications;
	
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
