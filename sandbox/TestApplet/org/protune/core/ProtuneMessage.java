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
	
	FilteredPolicy[] filteredPolicies;
	Notification[] notifications;
	
	public ProtuneMessage(FilteredPolicy[] fpa, Notification[] na){
		filteredPolicies = fpa;
		notifications = na;
	}
	
	public FilteredPolicy[] getFilteredPolicies(){
		return filteredPolicies;
	}
	
	public Notification[] getNotifications(){
		return notifications;
	}
	
}
