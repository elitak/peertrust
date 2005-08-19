/*
 * Created on 16.06.2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.peertrust.demo.client.applet;

/**
 * @author pat_dev
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class StartPasswordNegoCmd {
	private String resource;
	private String password;
	private String userName;
	
	public StartPasswordNegoCmd(String res, String userName, String pwd){
		this.password=pwd;
		this.resource=res;
		this.userName=userName;
	}
	
	
	
	public String getPassword() {
		return password;
	}
	
	public String getResource() {
		return resource;
	}
	
	public String getUserName() {
		return userName;
	}
	
}
