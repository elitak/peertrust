/*
 * Created on 18.05.2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.peertrust.demo.client.applet;

import java.io.Serializable;

/**
 * @author pat_dev
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class StartPeerTrustNegoCmd implements Serializable{
	private String resource;
	private String ptQueryGoal;
	
	public StartPeerTrustNegoCmd(String resource, String ptQueryGoal){
		this.resource=resource;
		this.ptQueryGoal=ptQueryGoal;
		return;
	}
	
	
	public String getPtQueryGoal() {
		return ptQueryGoal;
	}
	public String getResource() {
		return resource;
	}
}
