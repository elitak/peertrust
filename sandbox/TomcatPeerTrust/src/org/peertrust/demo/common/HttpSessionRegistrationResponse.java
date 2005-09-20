package org.peertrust.demo.common;

import java.io.Serializable;




public class HttpSessionRegistrationResponse implements Serializable{
	private boolean acknowledgment;
	private HttpSessionRegistrationRequest request;
	
	public HttpSessionRegistrationResponse(HttpSessionRegistrationRequest request, boolean ack){
		this.request=request;
		this.acknowledgment=ack;
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
