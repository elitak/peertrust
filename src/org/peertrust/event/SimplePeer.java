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

import org.apache.log4j.Logger;
import org.peertrust.config.Configurable;
import org.peertrust.event.error.PTErrorEvent;
import org.peertrust.exception.ConfigurationException;
import org.peertrust.net.Answer;
import org.peertrust.net.Message;
import org.peertrust.net.Peer;
import org.peertrust.net.Query;

/**
 * <p>
 * Simple listener which prints a trace with the events received.
 * </p><p>
 * $Id: SimplePeer.java,v 1.8 2006/04/24 12:02:07 dolmedilla Exp $
 * <br/>
 * Date: 05-Dec-2003
 * <br/>
 * Last changed: $Date: 2006/04/24 12:02:07 $
 * by $Author: dolmedilla $
 * </p>
 * @author olmedilla 
 */
public class SimplePeer implements PTEventListener, Configurable {
	
	private static Logger log = Logger.getLogger(PTEventListener.class);
	
	EventDispatcher _dispatcher ;
	
	/**
	 * 
	 */
	public SimplePeer() {
		super();
	}
	
	public void init() throws ConfigurationException
	{
		String msg = null ;
		if (_dispatcher == null)
		{
			msg = "There not exist an event dispatcher" ;
			log.error (msg) ;
			throw new ConfigurationException(msg) ;
		}
		
		_dispatcher.register(this) ;
		
		log.debug("Event listener registered") ;

		//_dispatcher.event(new QueryEvent(this, new Query ("request(spanishCourse,Session) @ eLearn", null, 1))) ;
	}
	
	/* (non-Javadoc)
	 * @see org.peertrust.event.PeerTrustEventListener#event(org.peertrust.event.PeerTrustEvent)
	 */
	public void event(PTEvent event) {
		if (event instanceof NewMessageEvent)
		{
			Message message = ( (NewMessageEvent) event).getMessage() ;
			String source, target ;
			
			Peer tmp = message.getSource() ;
			
			if (tmp == null)
				source = null ;
			else
				source = tmp.getAlias() ;
			
			tmp = message.getTarget() ;
			if (tmp == null)
				target = null ;
			else
				target = tmp.getAlias() ;
	
			if (event instanceof QueryEvent)
			{
				Query query = ( (QueryEvent) event).getQuery() ;
				
				log.debug("New query " + query.getGoal() + " from " + source + " to " + target) ;
				log.debug("reQueryId: " + query.getReqQueryId()) ;
			}
			else if (event instanceof AnswerEvent)
			{
				Answer answer = ( (AnswerEvent) event).getAnswer() ;
				log.debug("New answer " + answer.getGoal() + " from " + source + " to " + target) ;
				log.debug("reqQueryId: " + answer.getReqQueryId()) ;
				switch(answer.getStatus())
				{
					case Answer.ANSWER:
						log.info("Request successful.The answer is " + answer.getGoal()) ;
						break;
					case Answer.LAST_ANSWER:
						log.info("Request successful.The last answer is " + answer.getGoal()) ;
						break;
					case Answer.FAILURE:
						log.info("Request failed") ;
						break;
					default:
						log.info("Request status unknown") ;
				}
			}
		}
		else if (event instanceof PTErrorEvent)
		{
			log.error("PT Error: " + ((PTErrorEvent)event).getMessage() ) ;
		}
		else
			log.debug ("Unknown event") ;	
	}
	/**
	 * @param _dispatcher The _dispatcher to set.
	 */
	public void setEventDispatcher(EventDispatcher _dispatcher) {
		this._dispatcher = _dispatcher;
	}
}
