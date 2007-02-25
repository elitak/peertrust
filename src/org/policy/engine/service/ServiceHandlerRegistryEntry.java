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

package org.policy.engine.service;

import org.apache.log4j.Logger;
import org.policy.config.Configurable;
import org.policy.config.ConfigurationException;
import org.policy.engine.PolicyEngine;

/**
 * <p>
 * 
 * </p><p>
 * $Id: ServiceHandlerRegistryEntry.java,v 1.1 2007/02/25 23:00:28 dolmedilla Exp $
 * <br/>
 * Date: Feb 25, 2007
 * <br/>
 * Last changed: $Date: 2007/02/25 23:00:28 $
 * by $Author: dolmedilla $
 * </p>
 * @author olmedilla
 */

public class ServiceHandlerRegistryEntry implements Configurable
{
	private static Logger log = Logger.getLogger(ServiceHandlerRegistryEntry.class);
	
	ServiceHandler _handler ;
	PolicyEngine _engine ;
	Class _class ;
	
	public ServiceHandlerRegistryEntry()
	{
		log.debug("$Id: ServiceHandlerRegistryEntry.java,v 1.1 2007/02/25 23:00:28 dolmedilla Exp $");
	}

	/* (non-Javadoc)
	 * @see org.policy.config.Configurable#init()
	 */
	public void init() throws ConfigurationException
	{
		if (_engine == null)
			throw new ConfigurationException("A policy engine has not been specified.") ;
		
		if (_handler == null)
			throw new ConfigurationException("A class name has not been specified.") ;
	}

	/**
	 * @return Returns the handler
	 */
	public ServiceHandler getHandler()
	{
		return _handler;
	}

	/**
	 * @param handler The service handler to set.
	 */
	public void setHandler(ServiceHandler handler)
	{
		_handler = handler ;
	}

	/**
	 * @return Returns the engine.
	 */
	public PolicyEngine getEngine()
	{
		return _engine;
	}

	/**
	 * @param engine The engine to set.
	 */
	public void setEngine(PolicyEngine engine)
	{
		this._engine = engine;
	}

}
