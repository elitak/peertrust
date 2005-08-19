/*
 * Created on 18.05.2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.peertrust.demo.client.applet;

import java.io.Serializable;
import java.net.URL;

/**
 * @author pat_dev
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class FinalResponse implements Serializable {
	private String key;
	private String resource;
	private URL resourceURL;
	
	public FinalResponse(String key, String resource, URL url){
		this.key= key;
		this.resource=resource;
		this.resourceURL=url;
		return;
	}
	
	
	public String getKey() {
		return key;
	}
	public String getResource() {
		return resource;
	}
	
	
	public URL getResourceURL() {
		return resourceURL;
	}
}
