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
package g4mfs.impl.org.peertrust.config;


import g4mfs.impl.org.peertrust.exception.ConfigurationException;

import org.apache.log4j.Logger;

/**
 * <p>
 * Specify general options for the application:
 * <ul>
 * <li>Mode of execution: normal or demo</li>
 * </ul>
 * </p><p>
 * $Id: RunTimeOptions.java,v 1.1 2005/11/30 10:35:16 ionut_con Exp $
 * <br/>
 * Date: 05-Dec-2003
 * <br/>
 * Last changed: $Date: 2005/11/30 10:35:16 $
 * by $Author: ionut_con $
 * </p>
 * @author olmedilla 
 */
public class RunTimeOptions implements Configurable {
	
	private static Logger log = Logger.getLogger(RunTimeOptions.class);
	
	public static String NORMAL_MODE = "normal" ;
	public static String DEMO_MODE = "demo" ;
	
	private String DEFAULT_MODE = NORMAL_MODE ;
	
	String _runningMode ;
	
	/**
	 * 
	 */
	public RunTimeOptions() {
		super();
	}

	public void init() throws ConfigurationException {

	}
	/**
	 * @return Returns the runningMode.
	 */
	public String getRunningMode() {
		return _runningMode;
	}
	/**
	 * @param runningMode The runningMode to set.
	 */
	public void setRunningMode(String runningMode) {
		if (runningMode.equalsIgnoreCase(NORMAL_MODE) == true)
			_runningMode = NORMAL_MODE ;
		else if (runningMode.equalsIgnoreCase(DEMO_MODE) == true)
			_runningMode = DEMO_MODE ;
		else
		{
			log.warn ("Running mode '" + runningMode + "' not valid. Applying default mode '" + DEFAULT_MODE + "'") ;
			_runningMode = DEFAULT_MODE ;
		}
	}
}
