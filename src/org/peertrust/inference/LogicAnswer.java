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

import org.apache.log4j.Logger;
import org.peertrust.meta.Proof;
import org.peertrust.meta.Trace;

/**
 * <p>
 * Answer received from an inference engine.
 * </p><p>
 * $Id: LogicAnswer.java,v 1.4 2005/08/06 07:59:50 dolmedilla Exp $
 * <br/>
 * Date: 05-Dec-2003
 * <br/>
 * Last changed: $Date: 2005/08/06 07:59:50 $
 * by $Author: dolmedilla $
 * </p>
 * @author olmedilla
 */
public class LogicAnswer {

 	private String goal = null ;
 	private String subgoals = null ;
 	private Trace _trace = null ;
 	private Proof _proof = null ;
// 	private String proof = null ;
 	private String goalExpanded = null ;
 	private String delegator = null ;
 	private static Logger log = Logger.getLogger(LogicAnswer.class);
 	
	public LogicAnswer(String goal, String goalExpanded, String subgoals, String proof, String trace, String delegator) {
		this.goal = goal ;
		this.subgoals = subgoals ;
		setTrace(trace) ;
		setProof(proof) ;
		this.delegator = delegator ;
		this.goalExpanded = goalExpanded ;
		log.debug("Created: " + this.toString()) ;
	}

	/**
	 * @return Returns the delegator.
	 */
	public String getDelegator() {
		return delegator;
	}
	/**
	 * @param delegator The delegator to set.
	 */
	public void setDelegator(String delegator) {
		this.delegator = delegator;
	}
	/**
	 * @return Returns the proof.
	 */
	public Proof getProof() {
		return _proof ;
	}
	/**
	 * @param proof The proof to set.
	 */
	public void setProof(String proof) {
		_proof = new Proof(PrologTools.extractListTerms(proof));
	}
	/**
	 * @return Returns the trace.
	 */
	public Trace getTrace() {
		return _trace ;
	}
	/**
	 * @param trace The trace to set.
	 */
	public void setTrace(String trace) {
		_trace = new Trace(PrologTools.extractListTerms(trace));
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
		return "Goal: |" + goal + "| - Subgoals: |" + subgoals + "| - Proof: |" + _proof + "| - Trace: " + _trace + "|" ; 
	}
	/**
	 * @return Returns the goalExpanded.
	 */
	public String getGoalExpanded() {
		return goalExpanded;
	}
	/**
	 * @param goalExpanded The goalExpanded to set.
	 */
	public void setGoalExpanded(String goalExpanded) {
		this.goalExpanded = goalExpanded;
	}
}
