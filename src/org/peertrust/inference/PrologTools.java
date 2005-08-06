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
package org.peertrust.inference;

import java.util.Vector;

import org.apache.log4j.Logger;

/**
 * <p>
 * Tools to work with prolog
 * </p><p>
 * $Id: PrologTools.java,v 1.1 2005/08/06 07:59:50 dolmedilla Exp $
 * <br/>
 * Date: 05-Dec-2003
 * <br/>
 * Last changed: $Date: 2005/08/06 07:59:50 $
 * by $Author: dolmedilla $
 * </p>
 * @author olmedilla
 */public class PrologTools {

 	private static Logger log = Logger.getLogger(MinervaProlog.class);
 	
 	public static Vector extractTerms (String query)
	{
 		//log.debug("Extracting terms from " + query) ;
		Vector terms = new Vector () ;
		int numberBrackets = 0 ;
		int numberSquareBrackets = 0 ;
		StringBuffer currentTerm = new StringBuffer("") ;
		for (int i = 0 ; i < query.length() ; i++)
		{
			char car = query.charAt (i) ;
			if (car == '(')
				numberBrackets ++ ;
			else if (car == '[')
				numberSquareBrackets ++ ;
			else if (car == ')')
				numberBrackets -- ;
			else if (car == ']')
				numberSquareBrackets -- ;
			else if ( (car == ',') && (numberBrackets == 0) && (numberSquareBrackets == 0) )
			{ 
				terms.add(currentTerm.toString()) ;
				currentTerm = new StringBuffer ("") ;
				continue ;
			}
				currentTerm.append(car) ;
		}
		
		if (currentTerm.length() > 0)
			terms.add(currentTerm.toString()) ;
					
//		StringBuffer message = new StringBuffer("Extracted terms : ") ;
//		for (int i = 0 ; i < terms.size() ; i++)
//			message.append(terms.elementAt(i) + " | ") ;
//		log.debug(message.toString()) ;
		
		return terms ; 
	}

 	public static Vector extractListTerms (String query)
	{
 		//log.debug("Extracting list terms from " + query) ;
		Vector terms = new Vector () ;
		int numberBrackets = 0 ;
		int numberSquareBrackets = 0 ;
		StringBuffer currentTerm = new StringBuffer("") ;
		if ( (query.charAt(0) != '[') || (query.charAt(query.length()-1) != ']') )
		{
			log.error("Wrong list format") ;
			return null ;
		}

		for (int i = 1 ; i < query.length()-1 ; i++)
		{
			char car = query.charAt (i) ;
			if (car == '(')
				numberBrackets ++ ;
			else if (car == '[')
				numberSquareBrackets ++ ;
			else if (car == ')')
				numberBrackets -- ;
			else if (car == ']')
				numberSquareBrackets -- ;
			else if ( (car == ',') && (numberBrackets == 0) && (numberSquareBrackets == 0) )
			{ 
				terms.add(currentTerm.toString()) ;
				currentTerm = new StringBuffer ("") ;
				continue ;
			}
			
			currentTerm.append(car) ;
		}
		
		if (currentTerm.length() > 0)
			terms.add(currentTerm.toString()) ;
		
//		StringBuffer message = new StringBuffer("Extracted list terms : ") ;
//		for (int i = 0 ; i < terms.size() ; i++)
//			message.append(terms.elementAt(i) + " | ") ;
//		log.debug(message.toString()) ;
		
		return terms ; 
	}
 	
 	public static String createPrologList (Vector list)
 	{
 		StringBuffer sb = new StringBuffer("[") ;
 		for (int i = 0 ; i < list.size() ; i++)
 			sb.append(list.elementAt(i) + ",") ;
 		sb.deleteCharAt(list.size()) ;
 		sb.append("]") ;
 		
 		return sb.toString() ;
 	}
}
