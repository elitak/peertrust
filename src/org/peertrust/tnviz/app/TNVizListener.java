package org.peertrust.tnviz.app;

import org.apache.log4j.Logger;
import org.peertrust.event.*;
import org.peertrust.config.Configurable;
import org.peertrust.exception.ConfigurationException;
import org.peertrust.net.Answer;
import org.peertrust.net.Query;

public class TNVizListener implements PTEventListener, Configurable {
	private static Logger log = Logger.getLogger(TNVizListener.class);
	
	EventDispatcher _dispatcher;
	private Graphics graphics;

	public TNVizListener() {
		super();
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
			log.debug("New query received: " + query.getGoal() + " - " + query.getReqQueryId()) ;
			graphics.addQuery(query);
		}
		else if (event instanceof AnswerEvent)
		{
			Answer answer = ( (AnswerEvent) event).getAnswer() ;
			log.debug("New answer received: " + answer.getGoal() + " - " + answer.getReqQueryId()) ;
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
		listener.deliverEvent(new QueryEvent(null,new Query("hallo, sebastian",chen,sebastian,0)));
		listener.deliverEvent(new AnswerEvent(null,new Answer("hallo, chen","",Answer.ANSWER,0,sebastian,chen)));
		listener.deliverEvent(new QueryEvent(null,new Query("sebastian?",chen,sebastian,1)));
		listener.deliverEvent(new QueryEvent(null,new Query("michael?",chen,michael,2)));
		listener.deliverEvent(new AnswerEvent(null,new Answer("wo ai ni","",Answer.ANSWER,1,sebastian,chen)));
		listener.deliverEvent(new AnswerEvent(null,new Answer("chen??","",Answer.ANSWER,2,michael,chen)));
		listener.deliverEvent(new QueryEvent(null,new Query("michael?",chen,michael,3)));
		listener.deliverEvent(new AnswerEvent(null,new Answer("chen?!?","",Answer.ANSWER,3,michael,chen)));
	}*/
	
}
