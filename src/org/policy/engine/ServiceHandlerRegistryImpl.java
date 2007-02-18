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

import java.util.Hashtable;

import org.policy.config.ConfigurationException;
import org.policy.model.ServiceHandler;

/**
 * <p>
 * 
 * </p><p>
 * $Id: ServiceHandlerRegistryImpl.java,v 1.1 2007/02/18 00:38:12 dolmedilla Exp $
 * <br/>
 * Date: Feb 17, 2007
 * <br/>
 * Last changed: $Date: 2007/02/18 00:38:12 $
 * by $Author: dolmedilla $
 * </p>
 * @author olmedilla
 */

public class ServiceHandlerRegistryImpl implements ServiceHandlerRegistry
{
	Hashtable<Class,PolicyEngine> _registry ;
	
	public ServiceHandlerRegistryImpl ()
	{
		// TODO add a set method so the registry can be filled up with the configuration file
	}

	public void init() throws ConfigurationException
	{
		_registry = new Hashtable<Class,PolicyEngine> () ;
	}

	public void register(ServiceHandler handler, PolicyEngine engine)
	{
		_registry.put(handler.getClass(), engine) ;
	}

	public PolicyEngine retrieveServiceHandler(ServiceHandler handler) throws UnavailableServiceHandlerException
	{
		PolicyEngine engine = _registry.get(handler.getClass()) ;
		
		if (engine == null)
			throw new UnavailableServiceHandlerException ("No engine available for the service handler " + handler.getClass().getName()) ;
		
		return engine;
	}
}
