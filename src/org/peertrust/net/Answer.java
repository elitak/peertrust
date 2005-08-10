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
 * $Id: Answer.java,v 1.9 2005/08/10 12:02:43 dolmedilla Exp $
 * <br/>
 * Date: 05-Dec-2003
 * <br/>
 * Last changed: $Date: 2005/08/10 12:02:43 $
 * by $Author: dolmedilla $
 * </p>
 * @author olmedilla 
 */
public class Answer extends NegotiationMessage implements Serializable {

 	public static final int FAILURE = 3 ;
 	public static final int ANSWER = 5 ;
 	public static final int LAST_ANSWER = 6 ;

 	private String _goal = null ;
 	private Proof _proof = null ;
 	private int _status = -1 ;
 	private long _reqQueryId = -1 ;
	
	public Answer(String goal, long negotiationId, Proof proof, int status, long reqQueryId, Peer source, Peer target, Trace trace) {
		super(source, target, negotiationId, trace) ;
		this._goal = goal ;
		this._proof = proof ;
		this._status = status ;
		this._reqQueryId = reqQueryId ;
		_trace = trace ;
	}
			
	/**
	 * @return Returns the goal.
	 */
	public String getGoal() {
		return _goal;
	}
	/**
	 * @param goal The goal to set.
	 */
	public void setGoal(String goal) {
		this._goal = goal;
	}
	/**
	 * @return Returns the proof.
	 */
	public Proof getProof() {
		return _proof;
	}
	/**
	 * @param proof The proof to set.
	 */
	public void setProof(Proof proof) {
		this._proof = proof;
	}
	/**
	 * @return Returns the reqQueryId.
	 */
	public long getReqQueryId() {
		return _reqQueryId;
	}
	/**
	 * @param reqQueryId The reqQueryId to set.
	 */
	public void setReqQueryId(long reqQueryId) {
		this._reqQueryId = reqQueryId;
	}
	/**
	 * @return Returns the status.
	 */
	public int getStatus() {
		return _status;
	}
	/**
	 * @param status The status to set.
	 */
	public void setStatus(int status) {
		this._status = status;
	}

	public String toString()
	{
		return getStatusString(_status) + super.toString() +
		"\n\t| - Goal: " + _goal +
		"\n\t| - ReqQueryId: " + _reqQueryId +
		"\n\t| - Proof: " + _proof +
		"\n\t| - Status: " + _status ;
	}
	
	public static String getStatusString (int status)
	{
		String message ;
		
		switch (status)
		{
		case ANSWER:	
			message = "ANSWER " ;
			break ;
		case LAST_ANSWER:
			message = "LAST ANSWER " ;
			break ;
		case FAILURE:
			message = "FAILURE " ;
			break ;
		default:
			message = "UNKNOWN " ;
		}
		
		return message ;
	}
}
