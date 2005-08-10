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

import org.peertrust.meta.Trace;

/**
 * <p>
 * 
 * </p><p>
 * $Id: Query.java,v 1.13 2005/08/10 12:02:43 dolmedilla Exp $
 * <br/>
 * Date: 05-Dec-2003
 * <br/>
 * Last changed: $Date: 2005/08/10 12:02:43 $
 * by $Author: dolmedilla $
 * </p>
 * @author olmedilla 
 */
public class Query extends NegotiationMessage implements Serializable {
	
 	private String _goal = null ;
 	private long _reqQueryId = -1 ;

 	static private long _currentNegotiationId = 0 ;

	public Query(String goal, long negotiationId, Peer origin, Peer target, long reqQueryId, Trace trace)
	{
		super(origin, target, negotiationId, trace) ;
		this._goal = goal ;
		this._reqQueryId = reqQueryId ;
	}
	
	// new Query without negotiationId, what means that it starts a new negotiation
	public Query(String goal, Peer origin, Peer target, long reqQueryId, Trace trace)
	{
		this (goal, getNewNegotiationId(), origin, target, reqQueryId, trace) ;
	}
	
	static synchronized long getNewNegotiationId ()
 	{
 		_currentNegotiationId += 1 ;
 		return _currentNegotiationId ;
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
	
	public String toString()
	{
		String message = super.toString() ;
		return "QUERY "+ message +
		"\n\t| - Goal: " + _goal +
		"\n\t| - ReqQueryId: " + _reqQueryId ;
	}
}
