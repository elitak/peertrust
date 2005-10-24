/**
 * 
 */
package org.peertrust.demo.common;

import org.peertrust.net.Peer;

/**
 * @author pat_dev
 *
 */
public class HttpServerPeer extends Peer {

	/**
	 * The relative path to a peertrust communication server.
	 * E.g. htpp://autority/;ltthttlSendPath;gt?;lthttpSendQuery;gt
	 */
	private String httpSendPath;
	
	
	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 */
	public HttpServerPeer(String arg0, String arg1, int arg2) {
		super(arg0, arg1, arg2);
	}

}
