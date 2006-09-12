package org.protune.core;

import org.protune.net.OngoingNegotiationMessage;
import org.protune.api.*;

public class ProtuneMessage extends OngoingNegotiationMessage {
	
	static final long serialVersionUID = 5111;
	
	FilteredPolicy[] filteredPolicies;
	Notification[] notifications;
	
	public ProtuneMessage(FilteredPolicy[] fpa, Notification[] na){
		filteredPolicies = fpa;
		notifications = na;
	}
	
	FilteredPolicy[] getFilteredPolicies(){
		return filteredPolicies;
	}
	
	Notification[] getNotifications(){
		return notifications;
	}
	
}
