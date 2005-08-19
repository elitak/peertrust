/*
 * Created on 20.04.2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.peertrust.demo.common;

import java.util.EventListener;

import org.peertrust.demo.client.applet.FinalResponse;


/**
 * @author pat_dev
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public interface NewsEventListener extends EventListener{
	public void onNews(NewsEvent newsEvents);
	public void onNews(FinalResponse finalResponse);
}
