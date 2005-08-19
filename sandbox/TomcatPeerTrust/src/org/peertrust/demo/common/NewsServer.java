/*
 * Created on 20.04.2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.peertrust.demo.common;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Vector;

/**
 * @author pat_dev
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class NewsServer  {
	
	Vector newsListeners= new Vector();
	
	public NewsServer(){
		
	}
	
	public void addNewsListener(NewsEventListener l){
		newsListeners.add(l);
		return;
	}
	
	public void removeNewsListener(NewsEventListener l){
		newsListeners.remove(l);
		return;
	}
	
	public void removeAllNewsListener(){
		newsListeners.removeAllElements();
		return;
	}
	
	public void fireNewsEvent(NewsEvent newsEvents){
		synchronized(newsListeners){
			int LISTENER_COUNT=newsListeners.size();
			for(int i=0; i<LISTENER_COUNT;i++){
				((NewsEventListener)newsListeners.elementAt(i)).onNews(newsEvents);
			}
		}
		return;
	}
	
	public void fireNewsEvent(Object source,Throwable th){
		
		StringWriter strWriter= new StringWriter();
		PrintWriter printWriter= new PrintWriter(strWriter, true);
		th.printStackTrace(printWriter);
		fireNewsEvent(new NewsEvent(source, strWriter.toString()));
		return;
	}
}
