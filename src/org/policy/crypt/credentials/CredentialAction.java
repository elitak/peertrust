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

package org.policy.crypt.credentials;

import java.net.URI;

import org.apache.log4j.Logger;
import org.policy.action.standard.PackageAction;
import org.policy.config.ConfigurationException;
import org.policy.model.Action;
import org.policy.model.ActionExecutionException;
import org.policy.model.MethodNotSupportedException;
import org.policy.model.StandardNotification;

/**
 * <p>
 * 
 * </p><p>
 * $Id: CredentialAction.java,v 1.1 2007/02/19 09:01:27 dolmedilla Exp $
 * <br/>
 * Date: Feb 18, 2007
 * <br/>
 * Last changed: $Date: 2007/02/19 09:01:27 $
 * by $Author: dolmedilla $
 * </p>
 * @author olmedilla
 */

public class CredentialAction implements Action
{
	// TODO Not yet clear whether this will be done like this or not
	
	private static Logger log = Logger.getLogger(CredentialAction.class);
	
	/* (non-Javadoc)
	 * @see org.policy.model.Action#getURI(java.lang.String)
	 */
	public URI getURI(String methodName) throws MethodNotSupportedException
	{
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.policy.model.Action#execute(java.net.URI, java.lang.String[])
	 */
	public StandardNotification execute(URI ref, String[] args)
			throws MethodNotSupportedException, ActionExecutionException
	{
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.policy.config.Configurable#init()
	 */
	public void init() throws ConfigurationException
	{
		// TODO Auto-generated method stub

	}

}
