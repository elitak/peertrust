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
import org.peertrust.inference.PrologTools;

/**
 * <p>
 * Proof of a goal by the inference engine
 * </p><p>
 * $Id: Proof.java,v 1.1 2005/08/06 07:59:50 dolmedilla Exp $
 * <br/>
 * Date: 05-Dec-2003
 * <br/>
 * Last changed: $Date: 2005/08/06 07:59:50 $
 * by $Author: dolmedilla $
 * </p>
 * @author olmedilla 
 */
public class Proof implements Cloneable, Serializable {

	private static Logger log = Logger.getLogger(Proof.class) ;
	
	static public String [] EMPTY_PROOF = null ;
	
	// addition to distinguish different negotiation paths
	Vector _proof = null ;
	
	public Proof ()
	{
		this ((String) null) ;
	}
	
	public Proof (String proof)
	{
		super() ;
		//log.debug("Creating from " + proof) ;
		if (proof != null)
			_proof = PrologTools.extractListTerms(proof) ;
		else
			_proof = new Vector() ;
		log.debug("Created: " + this) ;
	}
	
	public Proof (Vector proof)
	{
		super() ;
		//log.debug("Creating from proof " + proof) ;
		if (proof != null)
			_proof = proof ;
		else
			_proof = new Vector() ;
		log.debug("Created: " + this) ;
	}
	
	public Proof appendProof (String proof2)
	{
		Vector tmp = PrologTools.extractListTerms(proof2) ;
		return appendProof (new Proof(tmp)) ;
	}
	
	public Proof appendProof (Proof proof2)
	{
		_proof.addAll(proof2.getProof()) ;
		return this ;
	}
	
	public synchronized Vector getProof ()
	{
		return _proof ;
	}
	
	public synchronized String printProof()
	{
		String list = "[" ;
		
		if (_proof != null)
		{
			for (int i = 0 ; i < _proof.size() ; i++)
			{
				list += _proof.elementAt(i) ;
				
				if (i != _proof.size()-1)
					list += ","  ;
			}
		}
		list += "]" ;
		
		return list ;
	}
	
	public String toString ()
	{
		return printProof() ;
	}
	
	public Object clone ()
	{
		return new Proof ((Vector)_proof.clone()) ;
	}
	
	public boolean isEmptyTrace ()
	{
		if (_proof.size() == 0)
			return true ;
		else
			return false ;
	}
}
