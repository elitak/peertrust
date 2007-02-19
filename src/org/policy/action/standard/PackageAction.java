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

package org.policy.action.standard;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.log4j.Logger;
import org.policy.config.ConfigurationException;
import org.policy.model.Action;
import org.policy.model.ActionExecutionException;
import org.policy.model.MethodNotSupportedException;
import org.policy.model.StandardNotification;

/**
 * <p>
 * This is specific kind of action which wraps all package calls made i.e. using the 
 *     in predicate in Protune. It contains a single method called "packageExecution"
 * </p><p>
 * $Id: PackageAction.java,v 1.1 2007/02/19 09:01:27 dolmedilla Exp $
 * <br/>
 * Date: Feb 18, 2007
 * <br/>
 * Last changed: $Date: 2007/02/19 09:01:27 $
 * by $Author: dolmedilla $
 * </p>
 * @author olmedilla
 */

public class PackageAction implements Action
{
	private static Logger log = Logger.getLogger(PackageAction.class);
	
	final String DEFAULT_URI = "http://www.L3S.de/~olmedilla/action#packageAction" ;  
	final String DEFAULT_METHOD_NAME = "packageExecution" ;
	
	String _URIString = DEFAULT_URI ;
	String _methodName = DEFAULT_METHOD_NAME ;
	
	URI _uri ;

	public PackageAction ()
	{
	}
	
	public void init() throws ConfigurationException
	{
		try
		{
			_uri = new URI(_URIString) ;
		} catch (URISyntaxException e)
		{
			log.error(e.getMessage()) ;
			new ConfigurationException (e.getMessage()) ;
		}
	}

	/* (non-Javadoc)
	 * @see org.policy.model.Action#getURI(java.lang.String)
	 */
	public URI getURI(String methodName) throws MethodNotSupportedException
	{
		if (_methodName.equalsIgnoreCase(methodName) == false)
			throw new MethodNotSupportedException ("Method " + methodName + " is not known in class " + this.getClass().getName()) ;
		
		return _uri ;
	}

	/* (non-Javadoc)
	 * @see org.policy.model.Action#execute(java.net.URI, java.lang.String[])
	 */
	public StandardNotification execute (URI ref, String[] args) throws MethodNotSupportedException, ActionExecutionException
	{
		if (_uri.equals(ref) == false)
			throw new MethodNotSupportedException ("URI " + ref + " is not supported in class " + this.getClass().getName()) ;

		// TODO wrap the package call around here
		
		return null ;
	}

	/**
	 * @param methodName The methodName to set.
	 */
	public void setMethodName(String methodName)
	{
		this._methodName = methodName;
	}

	/**
	 * @param uri The uRI to set.
	 */
	public void setURI(String uri)
	{
		_URIString = uri;
	}
}
