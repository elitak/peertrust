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
package org.peertrust.event;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

/**
 * $Id: PeerTrustEventDispatcher.java,v 1.1 2004/10/20 19:26:38 dolmedilla Exp $
 * @author olmedilla
 * @date 05-Dec-2003
 * Last changed  $Date: 2004/10/20 19:26:38 $
 * by $Author: dolmedilla $
 * @description
 */
public class PeerTrustEventDispatcher implements EventDispatcher {
	
	private static Logger log = Logger.getLogger(PeerTrustEventDispatcher.class);
	
	Hashtable registry ;
	
	public PeerTrustEventDispatcher() {
		super();
		log.debug("$Id: PeerTrustEventDispatcher.java,v 1.1 2004/10/20 19:26:38 dolmedilla Exp $");
		registry = new Hashtable() ;
	}

	/* (non-Javadoc)
	 * @see org.peertrust.event.EventDispatcher#register(org.peertrust.event.PeerTrustEvent)
	 */
	public void register(PeerTrustEventListener listener) {
		register(listener, PeerTrustEvent.class) ;
	}

	/* (non-Javadoc)
	 * @see org.peertrust.event.EventDispatcher#unregister(org.peertrust.event.PeerTrustEvent)
	 */
	public boolean unregister(PeerTrustEventListener listener) {
		boolean res = false ;
		Iterator it = registry.keySet().iterator() ;
		while (it.hasNext())
		{
			Vector vector = (Vector) registry.get(it.next()) ;
			if (vector.remove(listener) == true)
				res = true ;
			
		}
		return res ;
	}

	/* (non-Javadoc)
	 * @see org.peertrust.event.EventDispatcher#register(org.peertrust.event.PeerTrustEvent, java.lang.Class)
	 */
	public void register(PeerTrustEventListener listener, Class event) {
    	log.debug(".registering " + listener.getClass().getName() + " to event " + event.getName());
    	
        Vector vector = (Vector) registry.get(event);
    	
    	// Add a new vector if not existing already
    	if ( vector == null ) {
    		vector = new Vector();
    		registry.put(event, vector);
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
	public void event(PeerTrustEvent event) {
		log.debug("Distributing event " + event.getClass().getName() + " from " + event.getSource().getClass().getName());
		
		Vector vector = (Vector) registry.get(event.getClass());

		// No entries for this event, do a broadcast to all elements register to PeerTrustEvent
		if (vector == null)
			log.error("No listeners registered to catch event " + event.getClass().getName()) ;
		else
		{
			log.debug(vector.size() + " elements registered for the event " + event.getClass().getName() ) ;
			
			log.debug("Broadcasting event to listeners registered for the event " + event.getClass().getName());
			Iterator it = vector.iterator();
			dispatchEvent(event, it)  ;
		}
			
		
        vector = (Vector) registry.get(PeerTrustEvent.class);

        if (vector == null)
        	log.error("No listeners registered to catch event of type PeerTrustEvent") ;
        else
        {
        	log.debug(vector.size() + " elements registered for the event PeerTrustEvent" ) ;
        	
        	log.debug("Always broadcasting event to listeners registered for the parent PeerTrustEvent");

        	Iterator it = vector.iterator();
        
        	dispatchEvent(event, it) ;
        }
	}
	
    private void dispatchEvent(PeerTrustEvent event, Iterator it)
    {        
        while( it.hasNext() ) {
            Object listener =  it.next();
             
            if (listener instanceof PeerTrustEventListener)
            {
            	if (listener != event.getSource())
            		( (PeerTrustEventListener)listener).event(event) ;
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
