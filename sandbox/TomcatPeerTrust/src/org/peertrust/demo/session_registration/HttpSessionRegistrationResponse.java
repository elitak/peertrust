package org.peertrust.demo.session_registration;

import java.io.Serializable;

import org.peertrust.net.Peer;




/**
 * HttpSessionRegistrationResponse represents a protocol object
 * used to answer a registration request.
 * 
 * @author Patrice Congo
 */
public class HttpSessionRegistrationResponse implements Serializable
{
	/** 
	 * acknoledgement to the request, to if registration
	 * was successfull.
	 */
	private boolean acknowledgment;
	
	/** 
	 * the request which is being answered
	 */
	private HttpSessionRegistrationRequest request;
	
	/** 
	 * A peer representing the trust peer of the httpserver.
	 */
	private Peer httpServerPeer;
	
	/**
	 * Build a response to request
	 * @param request -- the request to respond to
	 * @param 	ack -- a booleab representing the response,
	 * 			true if the registration was successful 
	 * @param 	httpServerPeer -- the answering peer on the http
	 * 			server.
	 */
	public HttpSessionRegistrationResponse(
								HttpSessionRegistrationRequest request, 
								boolean ack,
								Peer httpServerPeer){
		this.request=request;
		this.acknowledgment=ack;
		this.httpServerPeer=httpServerPeer;
	}

	/**
	 * @return Returns the acknowledgment.
	 */
	public boolean isAcknowledgment() {
		return acknowledgment;
	}

	/**
	 * @param acknowledgment The acknowledgment to set.
	 */
	public void setAcknowledgment(boolean acknowledgment) {
		this.acknowledgment = acknowledgment;
	}

	/**
	 * @return Returns the request.
	 */
	public HttpSessionRegistrationRequest getRequest() {
		return request;
	}

	/**
	 * @param request The request to set.
	 */
	public void setRequest(HttpSessionRegistrationRequest request) {
		this.request = request;
	}

	/**
	 * 
	 * @return returns the answering peer.
	 */
	public Peer getHttpServerPeer() {
		return httpServerPeer;
	}

	/**
	 * Sets the answering peer
	 * @param httpServerPeer -- the new value for the answering peer
	 */
	public void setHttpServerPeer(Peer httpServerPeer) {
		this.httpServerPeer = httpServerPeer;
	}
}
