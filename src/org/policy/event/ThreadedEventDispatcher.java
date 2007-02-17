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

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.policy.config.Configurable;
import org.policy.config.ConfigurationException;

/**
 * <p>
 * PeerTrust event dispatcher.
 * </p><p>
 * $Id: ThreadedEventDispatcher.java,v 1.1 2007/02/17 16:59:27 dolmedilla Exp $
 * <br/>
 * Date: 05-Dec-2003
 * <br/>
 * Last changed: $Date: 2007/02/17 16:59:27 $
 * by $Author: dolmedilla $
 * </p>
 * @author olmedilla 
 */
public class ThreadedEventDispatcher implements EventDispatcher, Configurable {
	
	private static Logger log = Logger.getLogger(ThreadedEventDispatcher.class);
	
	// TODO make the class thread safe
	//    - Add a Threadpool for the event dispatching
	//    - Make synchronized the access to the dispatcher
	//    - Hashtable and iterator may be concurrent
	
	Hashtable _registry ;
	Iterator _copiedIterator ;
	
	public ThreadedEventDispatcher() {
		super();
		log.debug("$Id: ThreadedEventDispatcher.java,v 1.1 2007/02/17 16:59:27 dolmedilla Exp $");
	}
	
	public void init () throws ConfigurationException
	{
		_registry = new Hashtable() ;
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
	public boolean unregister(EventListener listener) {
		boolean res = false ;
		Iterator it = _registry.keySet().iterator() ;
		while (it.hasNext())
		{
			Vector vector = (Vector) _registry.get(it.next()) ;
			if (vector.remove(listener) == true)
				res = true ;
			
		}
		return res ;
	}

	/* (non-Javadoc)
	 * @see org.peertrust.event.EventDispatcher#register(org.peertrust.event.PeerTrustEvent, java.lang.Class)
	 */
	public void register(EventListener listener, Class event)
	{
    		log.debug(".registering " + listener.getClass().getName() + " to event " + event.getName());
    	
        Vector vector = (Vector) _registry.get(event);
    	
        // Add a new vector if not existing already
	    	if ( vector == null ) {
	    		vector = new Vector();
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
		Vector vector ;
		
		while ( (currentClass != Object.class ) && (currentClass != null) )
		{
			vector = (Vector) _registry.get(currentClass);
	
			// No entries for this event, do a broadcast to all elements registered to PeerTrustEvent
			if (vector == null)
				log.debug("No listeners registered to catch event " + currentClass.getName()) ;
			else
			{
				log.debug(vector.size() + " elements registered for the event " + currentClass.getName() ) ;
				
				log.debug("Broadcasting event to listeners registered for the event " + currentClass.getName());
				Iterator it = vector.iterator();
				dispatchEvent(event, it)  ;
			}
			
			currentClass = currentClass.getSuperclass() ;
		}	
		
//        vector = (Vector) registry.get(PTEvent.class);
//
//        if (vector == null)
//        	log.debug("No listeners registered to catch event of type PeerTrustEvent") ;
//        else
//        {
//        	log.debug(vector.size() + " elements registered for the event PeerTrustEvent" ) ;
//        	
//        	log.debug("Always broadcasting event to listeners registered for the parent PeerTrustEvent");
//
//        	Iterator it = vector.iterator();
//        
//        	dispatchEvent(event, it) ;
//        }
	}
	
    private void dispatchEvent(Event event, Iterator it)
    {        
        while( it.hasNext() )
        {
            Object listener =  it.next();
             
            if (listener instanceof EventListener)
            {
	            	if (listener != event.getSource())
	            		( (EventListener)listener).event(event) ;
	/*            	try {
	                    if ( service != event.getSource() ) {
	                        service.event(event);
	                    }
	                } catch (Exception e) {
	                    log.error("Exception: ", e);
	                } */
            }
			else
			{
				log.error ("Unknown object " + listener.getClass().getName()) ;
			}

        }
    }

}
