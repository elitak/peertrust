package org.peertrust.net;

import java.io.Serializable;

import org.peertrust.meta.Peer;

/**
 * $Id: Query.java,v 1.1 2004/07/01 23:36:50 dolmedilla Exp $
 * @author $Author: dolmedilla $
 * @date 05-Dec-2003
 * Last changed  $Date: 2004/07/01 23:36:50 $
 * @description
 */
public class Query extends Message implements Serializable {

 	private String goal = null ;
 	private long reqQueryId = -1 ;
	
	public Query(String goal, Peer origin, long reqQueryId ) {
		super(origin) ;
		this.goal = goal ;
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
}
