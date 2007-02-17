package org.policy.communication;

import org.policy.communication.message.ServiceMessage;
import org.policy.model.ClientRequestId;

public interface Replyable
{
	public void reply (ClientRequestId identifier, ServiceMessage response) ;
}
