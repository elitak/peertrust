package org.peertrust.meta;

import java.io.Serializable;

/**
 * $Id: Peer.java,v 1.1 2004/07/01 23:35:58 dolmedilla Exp $
 * @author $Author: dolmedilla $
 * @date 05-Dec-2003
 * Last changed  $Date: 2004/07/01 23:35:58 $
 * @description
 */
public class Peer implements Serializable
{
	private static final int DEFAULT_PORT = 30000 ;
	private String alias ;
	private String address ;
	private int port ;
	
	public Peer (String alias, String address, int port)
	{
		this.alias = alias ;
		this.address = address ;
		this.port = port ;
	}
	
	public Peer (String alias, String address)
	{
		this(alias, address, DEFAULT_PORT) ;
	}
	
	public Peer ()
	{
		this("", "", DEFAULT_PORT) ;
	}
	
	public boolean equals (Object object)
	{
		Peer peer = (Peer) object ;
		if (this.address.compareTo(peer.getAddress()) == 0)
			return true ;
		else
			return false ;
	}
	
	public String getAlias() {
		return alias ;
	}
	
	void setAlias(String alias) {
		this.alias = alias ;
	}
	
	String getAddress() {
		return address ;
	}
	
	void setAddress(String address) {
		this.address = address ;
	}
	
	int getPort () {
		return port ;
	}
	
	void setPort(int port) {
		this.port = port ;
	}
	
}
