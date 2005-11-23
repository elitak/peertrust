/**
 * 
 */
package org.peertrust.demo.common;

import java.io.Serializable;

import org.peertrust.net.Peer;

/**
 * This class represents the client peer which may work with an http server peer
 * to provide peer trust communication over http. The communication is done throw
 * a servlet(or a jsp). 
 *   
 * 
 * @see org.peertrust.demo.servlet.PeertrustCommunicationServlet.
 * @author Patrice Congo (token77)
 *
 */
public class HttpClientPeer extends Peer implements Serializable{
	/**
	 * The relative path to a peertrust communication server.
	 * E.g. htpp://autority/;ltthttlSendPath;gt?;lthttpSendQuery;gt
	 */
	private String httpSendPath;
	
	/**
	 * The relative path to a peertrust communication server.
	 * E.g. htpp://autority/;ltthttlSendPath;gt?;lthttpSendQuery;gt
	 */
	private String httpSendQuery;
	
	/**
	 * @param alias 
	 * @param adress
	 * @param port
	 * @param httpSendPath
	 * @param httpSendQuery
	 */
	public HttpClientPeer(
				String alias, 
				String address, 
				int port, 
				String httpSendPath,
				String httpSendQuery ) {
		super(alias, address, port);
		this.httpSendPath=httpSendPath;
		this.httpSendQuery=httpSendQuery;
	}

	/**
	 * @return Returns the httpSendPath.
	 */
	public String getHttpSendPath() {
		return httpSendPath;
	}

	/**
	 * @param httpSendPath The httpSendPath to set.
	 */
	public void setHttpSendPath(String httpSendPath) {
		this.httpSendPath = httpSendPath;
	}

	/**
	 * @return Returns the httpSendQuery.
	 */
	public String getHttpSendQuery() {
		return httpSendQuery;
	}

	/**
	 * @param httpSendQuery The httpSendQuery to set.
	 */
	public void setHttpSendQuery(String httpSendQuery) {
		this.httpSendQuery = httpSendQuery;
	}
	
	public String getServerHTTPUrl(){
		
		//server.setAlias(server.getAlias());
		StringBuffer strBuffer= new StringBuffer(128);
		//strBuffer.delete(0,strBuffer.length());
		
		strBuffer.append("http://");
		strBuffer.append(this.getAddress());
		strBuffer.append(":");
		strBuffer.append(this.getPort());
		strBuffer.append(httpSendPath);
		strBuffer.append("?");
		strBuffer.append(httpSendQuery);
				
		return strBuffer.toString();
	}

}
