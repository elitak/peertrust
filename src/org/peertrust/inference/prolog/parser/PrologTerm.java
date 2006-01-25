package org.peertrust.inference.prolog.parser;

import org.apache.log4j.Logger;

public abstract class PrologTerm {

	private static Logger log = Logger.getLogger(PrologTerm.class);
	
	String _text ;
	
	public PrologTerm (String text)
	{
		super() ;
		log.debug("$Id: PrologTerm.java,v 1.1 2006/01/25 16:07:45 dolmedilla Exp $");
		_text = text ;
	}
	
	public String getText()
	{
		return _text ;
	}
	
	public String toString()
	{
		return _text ;
	}
}
