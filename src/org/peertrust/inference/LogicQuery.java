package org.peertrust.inference ;

import org.apache.log4j.Logger;


/**
 * $Id: LogicQuery.java,v 1.1 2004/07/01 23:35:28 dolmedilla Exp $
 * @author $Author: dolmedilla $
 * @date 05-Dec-2003
 * Last changed  $Date: 2004/07/01 23:35:28 $
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
