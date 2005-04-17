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
package org.peertrust.meta;

import java.io.Serializable;
import java.util.Vector;

import org.apache.log4j.Logger;

/**
 * $Id: Trace.java,v 1.1 2005/04/17 20:44:42 dolmedilla Exp $
 * @author olmedilla 
 * @date 05-Dec-2003
 * Last changed  $Date: 2005/04/17 20:44:42 $
 * by $Author: dolmedilla $
 * @description
 */
public class Trace implements Cloneable, Serializable {

	private static Logger log = Logger.getLogger(Trace.class) ;
	
	static public String [] EMPTY_TRACE = null ;
	static public String FAILURE = "-fail-" ;
	
	// addition to distinguish different negotiation paths
	Vector _trace = new Vector() ;
	
	public Trace ()
	{
		this (null) ;
	}
	
	public Trace (String [] trace)
	{
		super() ;
		if (trace != null)
			for (int i = 0 ; i < trace.length ; i++)
				_trace.add(trace[i]) ;
	}
	
	public synchronized String [] getTrace ()
	{
		String [] array = new String[_trace.size()] ;
		for (int i = 0 ; i < _trace.size() ; i++)
			array[i] = (String) _trace.elementAt(i) ;
		return array ;
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
		return new Trace (this.getTrace()) ;
	}
	
	public boolean isEmptyTrace ()
	{
		if (_trace.size() == 0)
			return true ;
		else
			return false ;
	}
}
