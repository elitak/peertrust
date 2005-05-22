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
package org.peertrust.exception;

/**
 * <p>
 * Exception produced at the inference engine.
 * </p><p>
 * $Id: InferenceEngineException.java,v 1.2 2005/05/22 17:56:48 dolmedilla Exp $
 * <br/>
 * Date: 05-Dec-2003
 * <br/>
 * Last changed: $Date: 2005/05/22 17:56:48 $
 * by $Author: dolmedilla $
 * </p>
 * @author olmedilla
 */
public class InferenceEngineException extends PeertrustException {
	/**
	 * 
	 */
	public InferenceEngineException() {
		super();
		// TODO Auto-generated constructor stub
	}
	/**
	 * @param message
	 */
	public InferenceEngineException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}
	/**
	 * @param arg0
	 */
	public InferenceEngineException(Throwable arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}
	/**
	 * @param arg0
	 * @param arg1
	 */
	public InferenceEngineException(String arg0, Throwable arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}
}
