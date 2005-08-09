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
package org.peertrust.net;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p><p>
 * $Id: Peer.java,v 1.6 2005/08/09 13:47:54 dolmedilla Exp $
 * <br/>
 * Date: 05-Dec-2003
 * <br/>
 * Last changed: $Date: 2005/08/09 13:47:54 $
 * by $Author: dolmedilla $
 * </p>
 * @author olmedilla 
 */
public class Peer implements Serializable
{
	public static final String UNSPECIFIED_ADDRESS = null ;
	public static final int UNSPECIFIED_PORT = -1 ;
//	private static final int DEFAULT_PORT = 30000 ;
	private String _alias ;
	private String _address ;
	private int _port ;
	
	public Peer (String alias, String address, int port)
	{
		this._alias = alias ;
		this._address = address ;
		this._port = port ;
	}
	
//	public Peer (String alias, String address)
//	{
//		this(alias, address, DEFAULT_PORT) ;
//	}
//	
//	public Peer (String alias)
//	{
//		this(alias, "", DEFAULT_PORT) ;
//	}
	
	public boolean equals (Object object)
	{
		Peer peer = (Peer) object ;
		// if (this._address.compareTo(peer.getAddress()) == 0)
		
		if ( (_alias.equals(peer.getAlias())) &&
				( (_address == null) || (peer.getAddress() == null) || (_address.equals(peer.getAddress())) )
				)
			return true ;
		else
			return false ;
	}
	
	public String getAlias() {
		return _alias ;
	}
	
	public void setAlias(String alias) {
		this._alias = alias ;
	}
	
	public String getAddress() {
		return _address ;
	}
	
	public void setAddress(String address) {
		this._address = address ;
	}
	
	public int getPort () {
		return _port ;
	}
	
	public void setPort(int port) {
		this._port = port ;
	}
	
	public String toString()
	{
		return "Peer " + _alias + " at " + _address + ":" + _port ;
	}
}
