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

/**
 * $Id: Query.java,v 1.6 2005/03/12 11:43:52 dolmedilla Exp $
 * @author olmedilla
 * @date 05-Dec-2003
 * Last changed  $Date: 2005/03/12 11:43:52 $
 * by $Author: dolmedilla $
 * @description
 */
public class Query extends Message implements Serializable {

	static public long NO_RELATED_QUERY = -1 ;
	
 	private String goal = null ;
 	private long reqQueryId = -1 ;
 	private long relatedQueryId = NO_RELATED_QUERY ;

	public Query(String goal, Peer origin, Peer target, long reqQueryId, long relatedQueryId) {
		super(origin, target) ;
		this.goal = goal ;
		this.reqQueryId = reqQueryId ;
		this.relatedQueryId = relatedQueryId ;
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
	 * @return Returns the relatedQueryId.
	 */
	public long getRelatedQueryId() {
		return relatedQueryId;
	}
}
