/*
 * Created on 20.04.2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.peertrust.demo.common;

import java.util.EventObject;

/**
 * @author pat_dev
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class NewsEvent extends EventObject {
	private String news=null;
	/**
	 * @param arg0
	 */
	public NewsEvent(Object source, String news) {
		super(source);
		this.news=news;
	}

	
	/**
	 * @return Returns the news.
	 */
	public String getNews() {
		return news;
	}
}
