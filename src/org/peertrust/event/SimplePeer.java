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
import org.peertrust.PeertrustEngine;
import org.peertrust.net.Answer;
import org.peertrust.net.Query;

/**
 * $Id: SimplePeer.java,v 1.1 2004/10/20 19:26:38 dolmedilla Exp $
 * @author olmedilla 
 * @date 05-Dec-2003
 * Last changed  $Date: 2004/10/20 19:26:38 $
 * by $Author: dolmedilla $
 * @description
 */
public class SimplePeer implements PeerTrustEventListener {
	
	private static Logger log = Logger.getLogger(PeerTrustEventListener.class);
	
	/**
	 * 
	 */
	public SimplePeer() {
		super();
		PeertrustEngine.getDispatcher().register(this) ;
		
		log.debug("registered") ;

		PeertrustEngine.getDispatcher().event(new QueryEvent(this, new Query ("request(spanishCourse,Session) @ eLearn", null, 1))) ;
	}
	
	/* (non-Javadoc)
	 * @see org.peertrust.event.PeerTrustEventListener#event(org.peertrust.event.PeerTrustEvent)
	 */
	public void event(PeerTrustEvent event) {
		if (event instanceof QueryEvent)
		{
			Query query = ( (QueryEvent) event).getQuery() ;
			log.debug("New query " + query.getGoal() + " from " + query.getOrigin().getAlias()) ;
		}
		else if (event instanceof AnswerEvent)
		{
			Answer answer = ( (AnswerEvent) event).getAnswer() ;
			log.debug("New answer " + answer.getGoal() + " from " + answer.getOrigin().getAlias()) ;
		}
		else
			log.debug ("unknown event") ;	
	}
}
