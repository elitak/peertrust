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

package org.policy.action;

import java.net.URI;
import java.util.Hashtable;

import org.policy.config.ConfigurationException;

/**
 * <p>
 * 
 * </p><p>
 * $Id: ActionRegistryImpl.java,v 1.1 2007/02/21 06:52:48 dolmedilla Exp $
 * <br/>
 * Date: Feb 20, 2007
 * <br/>
 * Last changed: $Date: 2007/02/21 06:52:48 $
 * by $Author: dolmedilla $
 * </p>
 * @author olmedilla
 */

public class ActionRegistryImpl implements ActionRegistry
{
	Hashtable<URI,ActionExecutor> _registry ;
	String _initializationFile ;
	
	public ActionRegistryImpl ()
	{
		
	}
	
	/* (non-Javadoc)
	 * @see org.policy.config.Configurable#init()
	 */
	public void init() throws ConfigurationException
	{
		_registry = new Hashtable<URI,ActionExecutor> () ;
		if (_initializationFile != null)
		{
			// TODO read from file and load into registry
		}
	}

	/* (non-Javadoc)
	 * @see org.policy.action.ActionRegistry#addActionExecutor(java.net.URI, org.policy.action.ActionExecutor)
	 */
	public void addActionExecutor(URI uri, ActionExecutor executor)
	{
		_registry.put(uri, executor) ;
	}

	/* (non-Javadoc)
	 * @see org.policy.action.ActionRegistry#removeActionExecutor(java.net.URI)
	 */
	public ActionExecutor removeActionExecutor(URI uri)
	{
		return _registry.remove(uri) ;
	}

	/* (non-Javadoc)
	 * @see org.policy.action.ActionRegistry#getActionExecutor(java.net.URI)
	 */
	public ActionExecutor getActionExecutor(URI uri)
			throws UnavailableActionExecutorException
	{
		ActionExecutor executor = _registry.get(uri) ;
		
		if (executor == null)
			throw new UnavailableActionExecutorException ("Executor for URI " + uri + " has not been found.") ;
		else
			return executor ;
	}

	/**
	 * @param initializationFile The initializationFile to set.
	 */
	public void setInitializationFile(String initializationFile)
	{
		_initializationFile = initializationFile;
	}
}
