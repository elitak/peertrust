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

package org.peertrust.net;

import java.io.Serializable;

import org.peertrust.meta.Proof;
import org.peertrust.meta.Trace;

/**
 * <p>
 * 
 * </p><p>
 * $Id: Answer.java,v 1.6 2005/08/06 07:59:49 dolmedilla Exp $
 * <br/>
 * Date: 05-Dec-2003
 * <br/>
 * Last changed: $Date: 2005/08/06 07:59:49 $
 * by $Author: dolmedilla $
 * </p>
 * @author olmedilla 
 */
public class Answer extends Message implements Serializable {

 	public static final int FAILURE = 3 ;
 	public static final int ANSWER = 5 ;
 	public static final int LAST_ANSWER = 6 ;

 	private String goal = null ;
 	private Proof proof = null ;
 	private int status = -1 ;
 	private long reqQueryId = -1 ;
 	private Trace _trace = null ;
	
	public Answer(String goal, Proof proof, int status, long reqQueryId, Peer source, Peer target, Trace trace) {
		super(source, target, trace) ;
		this.goal = goal ;
		this.proof = proof ;
		this.status = status ;
		this.reqQueryId = reqQueryId ;
		_trace = trace ;
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
	public Proof getProof() {
		return proof;
	}
	/**
	 * @param proof The proof to set.
	 */
	public void setProof(Proof proof) {
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
