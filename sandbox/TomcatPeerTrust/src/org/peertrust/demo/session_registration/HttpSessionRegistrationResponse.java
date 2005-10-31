package org.peertrust.demo.session_registration;

import java.io.Serializable;

import org.peertrust.net.Peer;





public class HttpSessionRegistrationResponse implements Serializable{
	private boolean acknowledgment;
	private HttpSessionRegistrationRequest request;
	private Peer httpServerPeer;
	
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
	
	

}
