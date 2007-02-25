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

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.policy.config.ConfigurationException;
import org.policy.engine.PolicyEngine;

/**
 * <p>
 * 
 * </p><p>
 * $Id: ServiceHandlerRegistryImpl.java,v 1.1 2007/02/25 23:00:28 dolmedilla Exp $
 * <br/>
 * Date: Feb 17, 2007
 * <br/>
 * Last changed: $Date: 2007/02/25 23:00:28 $
 * by $Author: dolmedilla $
 * </p>
 * @author olmedilla
 */

public class ServiceHandlerRegistryImpl implements ServiceHandlerRegistry
{
	private static Logger log = Logger.getLogger(ServiceHandlerRegistryImpl.class);
	
	Hashtable<Class,PolicyEngine> _registry ;
	Vector<ServiceHandlerRegistryEntry> _elements ;
	
	public ServiceHandlerRegistryImpl ()
	{
		log.debug("$Id: ServiceHandlerRegistryImpl.java,v 1.1 2007/02/25 23:00:28 dolmedilla Exp $");
	}

	public void init() throws ConfigurationException
	{
		_registry = new Hashtable<Class,PolicyEngine> () ;
		
		if (_elements != null)
		{
			log.debug(_elements.size() + " passed to be initially loaded.") ;
			for (int i = 0 ; i < _elements.size() ; i++)
			{
				ServiceHandlerRegistryEntry entry = _elements.elementAt(i) ;
				register (entry.getHandler(), entry.getEngine()) ;
			}
		}
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
	
	public void setElements (Vector<ServiceHandlerRegistryEntry> elements)
	{
		_elements = elements ;
	}
}
