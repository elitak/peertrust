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

import org.policy.config.Configurable;

/**
 * <p>
 * 
 * </p><p>
 * $Id: ActionRegistry.java,v 1.1 2007/02/21 06:52:48 dolmedilla Exp $
 * <br/>
 * Date: Feb 20, 2007
 * <br/>
 * Last changed: $Date: 2007/02/21 06:52:48 $
 * by $Author: dolmedilla $
 * </p>
 * @author olmedilla
 */

public interface ActionRegistry extends Configurable
{
	void addActionExecutor (URI uri, ActionExecutor executor) ;
	
	ActionExecutor removeActionExecutor (URI uri) ;
	
	ActionExecutor getActionExecutor (URI uri) throws UnavailableActionExecutorException ;
}
