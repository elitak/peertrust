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
 * $Id: Query.java,v 1.9 2005/04/17 20:44:42 dolmedilla Exp $
 * @author olmedilla
 * @date 05-Dec-2003
 * Last changed  $Date: 2005/04/17 20:44:42 $
 * by $Author: dolmedilla $
 * @description
 */
public class Query extends Message implements Serializable {
	
 	private String _goal = null ;
 	private long _reqQueryId = -1 ;

	public Query(String goal, Peer origin, Peer target, long reqQueryId, Trace trace)
	{
		super(origin, target, trace) ;
		this._goal = goal ;
		this._reqQueryId = reqQueryId ;
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
}
