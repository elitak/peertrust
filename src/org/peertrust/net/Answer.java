
package org.peertrust.net;

import java.io.Serializable;

import org.peertrust.meta.Peer;

/**
 * $Id: Answer.java,v 1.1 2004/07/01 23:36:50 dolmedilla Exp $
 * @author $Author: dolmedilla $
 * @date 05-Dec-2003
 * Last changed  $Date: 2004/07/01 23:36:50 $
 * @description
 */
public class Answer extends Message implements Serializable {

 	public static final int FAILURE = 3 ;
 	public static final int ANSWER = 5 ;
 	public static final int LAST_ANSWER = 6 ;

 	private String goal = null ;
 	private String proof = null ;
 	private int status = -1 ;
 	private long reqQueryId = -1 ;
	
	public Answer(String goal, String proof, int status, long reqQueryId, Peer delegator) {
		super(delegator) ;
		this.goal = goal ;
		this.proof = proof ;
		this.status = status ;
		this.reqQueryId = reqQueryId ;
	}
		
	/**
	 * @return Returns the goal.
	 */
	public String getGoal() {
		return goal;
	}
	/**
	 * @param goal The goal to set.
	 */
	public void setGoal(String goal) {
		this.goal = goal;
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
	 * @return Returns the reqQueryId.
	 */
	public long getReqQueryId() {
		return reqQueryId;
	}
	/**
	 * @param reqQueryId The reqQueryId to set.
	 */
	public void setReqQueryId(long reqQueryId) {
		this.reqQueryId = reqQueryId;
	}
	/**
	 * @return Returns the status.
	 */
	public int getStatus() {
		return status;
	}
	/**
	 * @param status The status to set.
	 */
	public void setStatus(int status) {
		this.status = status;
	}
}
