/**
 * Copyright 2004
 * 
 * This file is part of Peertrust.
 * 
 * Peertrust is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * Peertrust is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Peertrust; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA
*/

package org.policy.engine;

import org.apache.log4j.Logger;
import org.policy.communication.LocalPeerEngine;
import org.policy.communication.Peer;
import org.policy.communication.message.ServiceMessage;
import org.policy.communication.net.NetworkCommunicationFactory;
import org.policy.config.ConfigurationException;
import org.policy.event.CommunicationEvent;
import org.policy.model.RequestIdentifier;

/**
 * <p>
 * 
 * </p><p>
 * $Id: PolicyEngineImpl.java,v 1.1 2007/02/17 16:59:29 dolmedilla Exp $
 * <br/>
 * Date: Feb 14, 2007
 * <br/>
 * Last changed: $Date: 2007/02/17 16:59:29 $
 * by $Author: dolmedilla $
 * </p>
 * @author olmedilla
 */

public class PolicyEngineImpl implements PolicyEngine
{
	private static Logger log = Logger.getLogger(PolicyEngineImpl.class);

	static final String DEFAULT_ALIAS = "PolicyEngine" ;
	
	String _alias = DEFAULT_ALIAS ;
	LocalPeerEngine _peer ;
	
	/* (non-Javadoc)
	 * @see org.policy.engine.PolicyEngine#sendRequest(org.policy.communication.message.ServiceRequest, org.policy.communication.Peer)
	 */
	public RequestIdentifier sendRequest(ServiceMessage request)
	{		
		CommunicationEvent event = new CommunicationEvent (request) ;

		_dispatcher.(event) ;
		// TODO Auto-generated method stub
		return null;
	}

	public void init() throws ConfigurationException
	{
		_peer = new LocalPeerEngine (_alias) ;
		
		// TODO Auto-generated method stub
		
	}

	public void setCommunicationChannel(NetworkCommunicationFactory factory)
	{
		// TODO Auto-generated method stub
		
	}

	public LocalPeerEngine getLocalPeerServer()
	{
		// TODO Auto-generated method stub
		return null;
	}

}
