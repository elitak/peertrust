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
package org.policy.peertrust.engine.meta;

import java.io.Serializable;
import java.util.Vector;

import org.apache.log4j.Logger;

/**
 * <p>
 * Trace of the inference engine
 * </p><p>
 * $Id: Trace.java,v 1.1 2007/02/28 08:40:15 dolmedilla Exp $
 * <br/>
 * Date: 05-Dec-2003
 * <br/>
 * Last changed: $Date: 2007/02/28 08:40:15 $
 * by $Author: dolmedilla $
 * </p>
 * @author olmedilla 
 */
public class Trace implements Cloneable, Serializable {

	private static Logger log = Logger.getLogger(Trace.class) ;
	
	static public String [] EMPTY_TRACE = null ;
	static public String FAILURE = "-fail-" ;
	
	// addition to distinguish different negotiation paths
	Vector _trace = null ;
	
	public Trace ()
	{
		this ((String) null) ;
	}
	
	public Trace (String trace)
	{
		super() ;
		//log.debug("Creating from " + trace) ;
		if (trace != null)
			_trace = PrologTools.extractListTerms(trace) ;
		else
			_trace = new Vector() ;
		log.debug("Created: " + this) ;
	}
	
	public Trace (Vector trace)
	{
		super() ;
		//log.debug("Creating from trace " + trace + " with " + trace.size() + " elements.") ;
		if (trace != null)
			_trace = trace ;
		else
			_trace = new Vector() ;
		log.debug("Created: " + this) ;
	}
	
	public synchronized Vector getTrace ()
	{
		return _trace ;
	}
	
	public synchronized String printTrace()
	{
		String list = "[" ;
		
		if (_trace != null)
		{
			for (int i = 0 ; i < _trace.size() ; i++)
			{
				list += _trace.elementAt(i) ;
				
				if (i != _trace.size()-1)
					list += ","  ;
			}
		}
		list += "]" ;
		
		return list ;
	}
	
	public String toString ()
	{
		return printTrace() ;
	}
	
	public Trace appendTrace (String trace2)
	{
		Vector tmp = PrologTools.extractListTerms(trace2) ;
		return appendTrace (new Trace(tmp)) ;
	}
	
	public Trace appendTrace (Trace trace2)
	{
		_trace.addAll(trace2.getTrace()) ;
		return this ;
	}

	private synchronized Trace addElement (String element)
	{
		_trace.add(element) ;
		return this ;
	}
	
	public Trace addQuery (String element)
	{
		return addElement (element + "?") ;
	}
	
	public Trace addAnswer (String element)
	{
		return addElement ("<" + element + ">") ;
	}
	
	public Trace addFailure (String element)
	{
		return addElement ("-fail-" + element) ;
	}
	
	public Object clone ()
	{
		return new Trace ((Vector)_trace.clone()) ;
	}
	
	public boolean isEmptyTrace ()
	{
		if (_trace.size() == 0)
			return true ;
		else
			return false ;
	}
}
