package org.peertrust.tnviz.app;

import org.jgraph.graph.DefaultEdge;

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
