package org.peertrust.inference;

import org.apache.log4j.Logger;

/**
 * $Id: LogicAnswer.java,v 1.1 2004/07/01 23:35:28 dolmedilla Exp $
 * @author $Author: dolmedilla $
 * @date 05-Dec-2003
 * Last changed  $Date: 2004/07/01 23:35:28 $
 * @description
 */
public class LogicAnswer {

 	private String goal = null ;
 	private String subgoals = null ;
 	private String proof = null ;
 	private String goalExpanded = null ;
 	private String delegator = null ;
 	private static Logger log = Logger.getLogger(LogicAnswer.class);
 	
	public LogicAnswer(String goal, String goalExpanded, String subgoals, String proof, String delegator) {
		this.goal = goal ;
		this.subgoals = subgoals ;
		this.proof = proof ;
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
	public String getProof() {
		return proof;
	}
	/**
	 * @param proof The proof to set.
	 */
	public void setProof(String proof) {
		this.proof = proof;
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
		return "Goal: |" + goal + "| - Subgoals: |" + subgoals + "| - Proof: |" + proof + "|" ; 
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
