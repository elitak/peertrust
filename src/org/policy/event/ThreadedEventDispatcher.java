/**
 * Copyright 2004
 * 
 * This file is part of Peertrust.
 * 
 * Peertrust is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * Peertrust is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Peertrust; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
*/
package org.policy.event;

import java.util.Iterator;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;
import org.policy.config.Configurable;
import org.policy.config.ConfigurationException;

/**
 * <p>
 * PeerTrust event dispatcher.
 * </p><p>
 * $Id: ThreadedEventDispatcher.java,v 1.2 2007/02/17 23:00:31 dolmedilla Exp $
 * <br/>
 * Date: 05-Dec-2003
 * <br/>
 * Last changed: $Date: 2007/02/17 23:00:31 $
 * by $Author: dolmedilla $
 * </p>
 * @author olmedilla 
 */
public class ThreadedEventDispatcher implements EventDispatcher, Configurable {
	
	private static Logger log = Logger.getLogger(ThreadedEventDispatcher.class);
	
	final int DEFAULT_NUMBER_OF_THREADS = 10 ;
	
	// ConcurrentHashMap provides safe concurrent access (including the iterators)
	ConcurrentHashMap<Class,Vector<EventListener>> _registry ;
	ExecutorService _threadPool ;
	
	int _numberOfThreads = DEFAULT_NUMBER_OF_THREADS ;
	
	public ThreadedEventDispatcher() {
		super();
		log.debug("$Id: ThreadedEventDispatcher.java,v 1.2 2007/02/17 23:00:31 dolmedilla Exp $");
	}
	
	public void init () throws ConfigurationException
	{
		_registry = new ConcurrentHashMap<Class,Vector<EventListener>>() ;
		_threadPool = Executors.newFixedThreadPool(_numberOfThreads) ;
	}

	/* (non-Javadoc)
	 * @see org.peertrust.event.EventDispatcher#register(org.peertrust.event.PeerTrustEvent)
	 */
	public void register(EventListener listener) {
		register(listener, Event.class) ;
	}

	/* (non-Javadoc)
	 * @see org.peertrust.event.EventDispatcher#unregister(org.peertrust.event.PeerTrustEvent)
	 */
	
	// register and unregister must be synchronized. The rest of the methods do not change the registry
	//     so there is no need for having them synchronized
	public synchronized boolean unregister(EventListener listener) {
		boolean res = false ;
		Iterator it = _registry.keySet().iterator() ;
		while (it.hasNext())
		{
			Vector<EventListener> vector = (Vector<EventListener>) _registry.get(it.next()) ;
			if (vector.remove(listener) == true)
				res = true ;
			
		}
		return res ;
	}

	/* (non-Javadoc)
	 * @see org.peertrust.event.EventDispatcher#register(org.peertrust.event.PeerTrustEvent, java.lang.Class)
	 */
	public synchronized void register(EventListener listener, Class event)
	{
    		log.debug(".registering " + listener.getClass().getName() + " to event " + event.getName());
    	
        Vector<EventListener> vector = (Vector<EventListener>) _registry.get(event);
    	
        // Add a new vector if not existing already
	    	if ( vector == null ) {
	    		vector = new Vector<EventListener>();
	    		_registry.put(event, vector);
	    	}
	    	
	    	// Only add not already registered components for the specified event
	    	if ( vector.contains(listener) ) {
	    		return;
	    	}
	    	
	    	vector.addElement(listener);		
	}

	/* (non-Javadoc)
	 * @see org.peertrust.event.PeerTrustEventListener#event(org.peertrust.event.PeerTrustEvent)
	 */
	public void event(Event event) {
		log.debug("Distributing event " + event.getClass().getName() + " from " + event.getSource().getClass().getName());
		
		Class currentClass = event.getClass() ;
		Vector<EventListener> vector ;
		
		// The set (vector) of listeners for the given event is retrieved and the event is dispatched to all
		//    of them. In addition, the event is also dispatched to all listeners of the superclasses of the
		//    given event. That means that a listener subscribed to events of type Event will be notified
		//    for any existing event
		while ( (currentClass != Object.class ) && (currentClass != null) )
		{
			vector = (Vector<EventListener>) _registry.get(currentClass);
	
			// No listeners for this event
			if (vector == null)
				log.debug("No listeners registered to catch event " + currentClass.getName()) ;
			else
			{
				log.debug(vector.size() + " elements registered for the event " + currentClass.getName() ) ;
				
				log.debug("Broadcasting event to listeners registered for the event " + currentClass.getName());
				Iterator it = vector.iterator();
				_threadPool.execute(new Dispatcher(event,it)) ;
			}
			
			currentClass = currentClass.getSuperclass() ;
		}	
	}

	/**
	 * @return Returns the numberOfThreads.
	 */
	public int getNumberOfThreads()
	{
		return _numberOfThreads;
	}

	/**
	 * @param numberOfThreads The numberOfThreads to set.
	 */
	public void setNumberOfThreads(int numberOfThreads)
	{
		this._numberOfThreads = numberOfThreads;
	}

	// This class uses a new thread in order to dispatch an event to a list of listeners (in a iterator)
	//    registered for notifications of such event
	private class Dispatcher implements Runnable
	{
		Event _event ;
		Iterator _it ;
		public Dispatcher (Event event, Iterator it)
		{
			_event = event ;
			_it = it ;
		}

		public void run()
		{
			while( _it.hasNext() )
	        {
	            Object listener =  _it.next();
	             
	            if (listener instanceof EventListener)
	            {
		            	if (listener != _event.getSource())
		            		( (EventListener)listener).event(_event) ;
	            }
				else
				{
					log.error ("Unknown object " + listener.getClass().getName()) ;
				}

	        }
		}
		
	}
}
