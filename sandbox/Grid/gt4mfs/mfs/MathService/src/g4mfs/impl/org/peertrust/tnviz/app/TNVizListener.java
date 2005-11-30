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
package g4mfs.impl.org.peertrust.tnviz.app;

import g4mfs.impl.org.peertrust.config.Configurable;
import g4mfs.impl.org.peertrust.event.AnswerEvent;
import g4mfs.impl.org.peertrust.event.EventDispatcher;
import g4mfs.impl.org.peertrust.event.PTEvent;
import g4mfs.impl.org.peertrust.event.PTEventListener;
import g4mfs.impl.org.peertrust.event.QueryEvent;
import g4mfs.impl.org.peertrust.exception.ConfigurationException;

import org.apache.log4j.Logger;
import g4mfs.impl.org.peertrust.event.*;
import g4mfs.impl.org.peertrust.net.Answer;
import g4mfs.impl.org.peertrust.net.Query;

/**
 * <p>
 * 
 * </p><p>
 * $Id: TNVizListener.java,v 1.1 2005/11/30 10:35:09 ionut_con Exp $
 * <br/>
 * Date: 10-Feb-2005
 * <br/>
 * Last changed: $Date: 2005/11/30 10:35:09 $
 * by $Author: ionut_con $
 * </p>
 * @author Sebastian Wittler and Michael Sch?fer
 */
public class TNVizListener implements PTEventListener, Configurable {
	
	private static Logger log = Logger.getLogger(TNVizListener.class);
	EventDispatcher _dispatcher;
	private Graphics graphics;

	public TNVizListener() {
		//super();
		graphics = new TNGraphics();
	}
	
	public void init() throws ConfigurationException
	{
		String msg = null ;
		if (_dispatcher == null)
		{
			msg = "There not exist an event dispatcher" ;
			throw new ConfigurationException(msg) ;
		}
		_dispatcher.register(this) ;
	}

	public void event(PTEvent event) {
		if (event instanceof QueryEvent)
		{
			Query query = ( (QueryEvent) event).getQuery() ;
			log.debug("New query received from " + query.getSource().getAlias() + ": " + 
					query.getGoal() + " - " + query.getReqQueryId() + " - " + query.getTrace().printTrace()) ;
			graphics.addQuery(query);
		}
		else if (event instanceof AnswerEvent)
		{
			Answer answer = ( (AnswerEvent) event).getAnswer() ;
			log.debug("New answer received from " + answer.getSource().getAlias() + ": " + 
					answer.getGoal() + " - " + answer.getReqQueryId() + " - " + answer.getTrace().printTrace()) ;
			graphics.addAnswer(answer) ;
		}
		graphics.updateGraph();
	}

	public EventDispatcher getEventDispatcher() {
		return _dispatcher;
	}

	/**
	 * @param _dispatcher The _dispatcher to set.
	 */
	public void setEventDispatcher(EventDispatcher dispatcher) {
		this._dispatcher = dispatcher;
	}
	
	/*
	public void deliverEvent(PTEvent event) {
		if(event instanceof QueryEvent) {
			Query query=((QueryEvent)event).getQuery();
			graphics.addQuery(query);
		}
		else if(event instanceof AnswerEvent) {
			Answer answer=((AnswerEvent)event).getAnswer();
			graphics.addAnswer(answer);
		}
		graphics.updateGraph();
	}

	public static void main(String args[]) {
		TNVizListener listener=new TNVizListener();
		Peer chen=new Peer("chen","www.chen.de",828282);
		Peer sebastian=new Peer("sebastian","www.sebastian.de",282828);
		Peer michael=new Peer("michael","www.michael.de",121212);
		/*listener.deliverEvent(new QueryEvent(null,new Query("hallo, sebastian",chen,sebastian,0)));
		listener.deliverEvent(new AnswerEvent(null,new Answer("hallo, chen","",Answer.ANSWER,0,sebastian,chen)));
		listener.deliverEvent(new QueryEvent(null,new Query("sebastian?",chen,sebastian,1)));
		listener.deliverEvent(new QueryEvent(null,new Query("michael?",chen,michael,2)));
		listener.deliverEvent(new AnswerEvent(null,new Answer("wo ai ni","",Answer.ANSWER,1,sebastian,chen)));
		listener.deliverEvent(new AnswerEvent(null,new Answer("chen??","",Answer.ANSWER,2,michael,chen)));
		listener.deliverEvent(new QueryEvent(null,new Query("michael??????????????????????????????????????",chen,michael,3)));
		listener.deliverEvent(new AnswerEvent(null,new Answer("chen?!?","",Answer.ANSWER,3,michael,chen)));*/
	/*
		listener.deliverEvent(new QueryEvent(null,new Query("b",sebastian,michael,1,0)));
		listener.deliverEvent(new QueryEvent(null,new Query("a",chen,sebastian,0,Query.NO_RELATED_QUERY)));
		listener.deliverEvent(new AnswerEvent(null,new Answer("c","",Answer.ANSWER,1,michael,sebastian)));
		listener.deliverEvent(new AnswerEvent(null,new Answer("d","",Answer.ANSWER,0,sebastian,chen)));
		listener.deliverEvent(new QueryEvent(null,new Query("a",michael,sebastian,0,Query.NO_RELATED_QUERY)));
	}*/
	
}
