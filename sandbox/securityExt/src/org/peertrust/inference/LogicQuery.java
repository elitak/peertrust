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
package org.peertrust.inference ;

import org.apache.log4j.Logger;

/**
 * $Id: LogicQuery.java,v 1.1 2004/12/21 11:09:34 seb0815 Exp $
 * @author olmedilla
 * @date 05-Dec-2003
 * Last changed  $Date: 2004/12/21 11:09:34 $
 * by $Author: seb0815 $
 * @description
 */
public class LogicQuery {

 	private String goal = null ;
 	private String subgoals = null ;
	private String requester = null ;
	
	private static Logger log = Logger.getLogger(LogicQuery.class);
	
	public LogicQuery(String goal, String subgoals, String requester) {
		this.goal = goal ;
		this.subgoals = subgoals ;
		this.requester = requester ;
		log.debug("Created: " + this.toString()) ;
	}

	/**
	 * @return Returns the query.
	 */
	public String getGoal() {
		return goal;
	}
	/**
	 * @param query The query to set.
	 */
	public void setGoal(String goal) {
		this.goal = goal;
	}
	/**
	 * @return Returns the requester.
	 */
	public String getRequester() {
		return requester;
	}
	/**
	 * @param requester The requester to set.
	 */
	public void setRequester(String requester) {
		this.requester = requester;
	}
	/**
	 * @return Returns the subgoals.
	 */
	public String getSubgoals() {
		return subgoals;
	}
	/**
	 * @param subgoals The subgoals to set.
	 */
	public void setSubgoals(String subgoals) {
		this.subgoals = subgoals;
	}
	
	public String toString ()
	{
		return "Query: |" + goal + "| - Subgoals: |" + subgoals  + "| Requester: |" + requester + "|" ;
	}
}
