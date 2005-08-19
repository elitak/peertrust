/*
 * Created on 27.04.2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.peertrust.demo.common;

import java.io.Serializable;

import org.peertrust.net.Peer;

/**
 * @author pat_dev
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class IdentifiablePeer extends Peer implements Serializable {
	String id=null;
	/**
	 * @param alias
	 * @param address
	 * @param port
	 */
	public IdentifiablePeer(String alias, String address, int port, String uniqueID) {
		super(alias, address, port);
		id=uniqueID;
	}

}
