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
package org.peertrust.tnviz.app;

import org.jgraph.graph.DefaultEdge;

/**
 * <p>
 * 
 * </p><p>
 * $Id: TNEdge.java,v 1.2 2005/05/22 17:56:44 dolmedilla Exp $
 * <br/>
 * Date: 10-Feb-2005
 * <br/>
 * Last changed: $Date: 2005/05/22 17:56:44 $
 * by $Author: dolmedilla $
 * </p>
 * @author Sebastian Wittler and Michael Sch?fer
 */
public class TNEdge extends DefaultEdge {

	private static final long serialVersionUID = 1L;
	
	private String id;
	private String label;
	private boolean invisible;
	
	private String goal;
	private long reqQueryId;
	private boolean answer;
	private boolean query;
	private int status;
	private String proof;
	private TNNode source;
	private TNNode target;
		
	public TNEdge(Object object) {
		super(object);
		id = "";
		label = object.toString();
		invisible = false;
		goal = "";
		reqQueryId = 0;
		answer = false;
		query = false;
		status = -1;
		proof = "";
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getLabel() {
		return label;
	}
	
	public void setLabel(String label) {
		this.label = label;
	}
	
	public boolean isInvisible() {
		return invisible;
	}
	
	public void setInvisible(boolean invisible) {
		this.invisible = invisible;
	}
	
	public boolean isAnswer() {
		return answer;
	}
	
	public void setAnswer(boolean answer) {
		this.answer = answer;
	}
	
	public String getGoal() {
		return goal;
	}
	
	public void setGoal(String goal) {
		this.goal = goal;
	}
	
	public boolean isQuery() {
		return query;
	}
	
	public void setQuery(boolean query) {
		this.query = query;
	}
	
	public long getReqQueryId() {
		return reqQueryId;
	}
	
	public void setReqQueryId(long reqQueryId) {
		this.reqQueryId = reqQueryId;
	}
	
	public int getStatus() {
		return status;
	}
	
	public void setStatus(int status) {
		this.status = status;
	}
	
	public String getProof() {
		return proof;
	}
	
	public void setProof(String proof) {
		this.proof = proof;
	}

	public TNNode getSourceNode() {
		return source;
	}

	public void setSourceNode(TNNode sourceNode) {
		source=sourceNode;
	}

	public TNNode getTargetNode() {
		return target;
	}

	public void setTargetNode(TNNode targetNode) {
		target=targetNode;
	}
	
}
