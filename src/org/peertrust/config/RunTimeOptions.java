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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
*/
package org.peertrust.config;

import org.apache.log4j.Logger;
import org.peertrust.exception.ConfigurationException;

/**
 * $Id: RunTimeOptions.java,v 1.1 2005/02/22 08:47:22 dolmedilla Exp $
 * @author olmedilla 
 * @date 05-Dec-2003
 * Last changed  $Date: 2005/02/22 08:47:22 $
 * by $Author: dolmedilla $
 * @description
 */
public class RunTimeOptions implements Configurable {
	
	private static Logger log = Logger.getLogger(RunTimeOptions.class);
	
	boolean isCredentialVerification ;
	
	/**
	 * 
	 */
	public RunTimeOptions() {
		super();
	}

	public void init() throws ConfigurationException {
		
	}
}
