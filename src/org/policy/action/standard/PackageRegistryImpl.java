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

import java.util.Hashtable;

import org.policy.config.ConfigurationException;

/**
 * <p>
 * 
 * </p><p>
 * $Id: PackageRegistryImpl.java,v 1.1 2007/02/19 09:01:27 dolmedilla Exp $
 * <br/>
 * Date: Feb 18, 2007
 * <br/>
 * Last changed: $Date: 2007/02/19 09:01:27 $
 * by $Author: dolmedilla $
 * </p>
 * @author olmedilla
 */

public class PackageRegistryImpl implements PackageRegistry
{

	Hashtable<String,AbstractExecutorWrapper> _registry ;
	
	public PackageRegistryImpl ()
	{
		
	}
	
	public void init() throws ConfigurationException
	{
		_registry = new Hashtable<String,AbstractExecutorWrapper> () ;
	}
	
	/* (non-Javadoc)
	 * @see org.policy.action.standard.PackageRegistry#addPackage(java.lang.String, org.policy.action.standard.AbstractExecutorWrapper)
	 */
	public void addPackage(String packageName, AbstractExecutorWrapper packageExecutor)
	{
		_registry.put(packageName, packageExecutor) ;
	}

	/* (non-Javadoc)
	 * @see org.policy.action.standard.PackageRegistry#removePackage(java.lang.String)
	 */
	public AbstractExecutorWrapper removePackage(String packageName)
	{
		return _registry.remove(packageName) ;
	}

	/* (non-Javadoc)
	 * @see org.policy.action.standard.PackageRegistry#getPackageExecutor(java.lang.String)
	 */
	public AbstractExecutorWrapper getPackageExecutor(String packageName)
	{
		return _registry.get(packageName) ;
	}

}
