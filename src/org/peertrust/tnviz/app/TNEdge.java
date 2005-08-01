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
 * The TNEdge class is a custom version of the DefaultEdge class. It contains
 * needed information like the two nodes this edge connects, and proof and
 * reqQueryId data. This data can be easily accessed through the class methods. 
 * </p><p>
 * $Id: TNEdge.java,v 1.4 2005/08/01 11:50:36 dolmedilla Exp $
 * <br/>
 * Date: 10-Feb-2005
 * <br/>
 * Last changed: $Date: 2005/08/01 11:50:36 $
 * by $Author: dolmedilla $
 * </p>
 * @author Michael Schaefer and Sebastian Wittler
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
    
    /**
     * Constructor for this class. The given object will be used as the label
     * of this edge.
     * @param object The label object.
     */
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

    /**
     * Returns the id as string.
     * @return The id.
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the id to the given string.
     * @param id The new id.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Returns the label as string. This is only the internal label, not the
     * label object!
     * @return The label.
     */
    public String getLabel() {
        return label;
    }

    /**
     * Stes the label to the given string. This is only the internal label, not 
     * the label object!
     * @param label The new label.
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * Returns the invisible flag.
     * @return True, if the edge is invisible, otherwise false.
     */
    public boolean isInvisible() {
        return invisible;
    }

    /**
     * Sets the invisible flag to the given value.
     * @param invisible The new invisible flag.
     */
    public void setInvisible(boolean invisible) {
        this.invisible = invisible;
    }

    /**
     * Returns the answer flag.
     * @return True, if the edge is an answer, otherwise false.
     */
    public boolean isAnswer() {
        return answer;
    }

    /**
     * Sets the answer flag to the given value.
     * @param answer The new answer flag.
     */
    public void setAnswer(boolean answer) {
        this.answer = answer;
    }

    /**
     * Returns the goal as string.
     * @return The goal.
     */
    public String getGoal() {
        return goal;
    }

    /**
     * Sets the goal to the given string.
     * @param goal The new goal.
     */
    public void setGoal(String goal) { 
        this.goal = goal;
    }

	/**
	 * Returns the query flag.
	 * @return True, if the edge is a query, otherwise false.
	 */
    public boolean isQuery() {
        return query;
    }

    /**
     * Sets the query flag to the given value.
     * @param query The new query flag.
     */
    public void setQuery(boolean query) {
        this.query = query;
    }

    /**
     * Returns the reqQueryId.
     * @return The reqQueryId.
     */
    public long getReqQueryId() {
        return reqQueryId;
    }

    /**
     * Sets the reqQueryId to the given value.
     * @param reqQueryId The new reqQueryId.
     */
    public void setReqQueryId(long reqQueryId) {
        this.reqQueryId = reqQueryId;
    }

    /**
     * Returns the status.
     * @return The status.
     */
    public int getStatus() {
        return status;
    }

    /**
     * Sets the status to the given value.
     * @param status The new status.
     */
    public void setStatus(int status) {
        this.status = status;
    }

    /**
     * Returns the proof as string.
     * @return The proof.
     */
    public String getProof() {
        return proof;
    }

    /**
     * Sets the proof to the given string.
     * @param proof The new string.
     */
    public void setProof(String proof) {
        this.proof = proof;
    }

    /**
     * Returns the source node of this edge as a TNNode object.
     * @return The source node.
     */
    public TNNode getSourceNode() {
        return source;
    }

    /**
     * Sets the source node of this edge to the given TNNode.
     * @param sourceNode The new source node.
     */
    public void setSourceNode(TNNode sourceNode) {
        source = sourceNode;
    }

    /**
     * Returns the target node of this edge as a TNNode object.
     * @return The target node.
     */
    public TNNode getTargetNode() {
        return target;
    }

    /**
     * Sets the target node of this edge to the given TNNode. 
     * @param targetNode The new target node.
     */
    public void setTargetNode(TNNode targetNode) {
        target = targetNode;
    }

}
