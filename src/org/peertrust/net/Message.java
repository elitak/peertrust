
package org.peertrust.net;

import java.io.Serializable;

import org.peertrust.meta.Peer;

/**
 * $Id: Message.java,v 1.1 2004/07/01 23:36:50 dolmedilla Exp $
 * @author $Author: dolmedilla $
 * @date 05-Dec-2003
 * Last changed  $Date: 2004/07/01 23:36:50 $
 * @description
 */public class Message implements Serializable {
 	private Peer origin = null ;
 	
 	Message (Peer origin) {
 		this.origin  = origin ;
 	}
	/**
	 * @return Returns the origin.
	 */
	public Peer getOrigin() {
		return origin;
	}
	/**
	 * @param origin The origin to set.
	 */
	public void setOrigin(Peer origin) {
		this.origin = origin;
	}
 }
