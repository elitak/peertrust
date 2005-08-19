/*
 * Created on 09.05.2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.peertrust.demo.client;

import org.peertrust.config.Configurable;
import org.peertrust.event.PTEvent;
import org.peertrust.event.PTEventListener;
import org.peertrust.exception.ConfigurationException;

/**
 * @author pat_dev
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ClientSidePTEventListener implements PTEventListener, Configurable{// extends TNVizListener {
	
	PTEventListener parent=null;
		
	public void event(PTEvent event){
		//super.event(event);
		System.out.println("*************-----event:"+event.toString());
		if(parent!=null){
			parent.event(event);
		}
		return;
	}
	
	
	public PTEventListener getParent() {
		return parent;
	}
	public void setParent(PTEventListener parent) {
		this.parent = parent;
	}


	/* (non-Javadoc)
	 * @see org.peertrust.config.Configurable#init()
	 */
	public void init() throws ConfigurationException {
		//nothing
	}
	
}
