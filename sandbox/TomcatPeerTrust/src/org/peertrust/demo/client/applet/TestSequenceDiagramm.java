package org.peertrust.demo.client.applet;

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


import java.awt.Color;
import java.awt.Rectangle;
import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;
import java.util.Enumeration;

import org.jgraph.JGraph;
import org.jgraph.graph.ConnectionSet;
import org.jgraph.graph.DefaultCellViewFactory;
import org.jgraph.graph.DefaultGraphModel;
import org.jgraph.graph.DefaultPort;
import org.jgraph.graph.GraphConstants;
import org.jgraph.graph.GraphLayoutCache;
import org.jgraph.graph.GraphModel;

import org.peertrust.event.AnswerEvent;
import org.peertrust.event.PTEvent;
import org.peertrust.event.PTEventListener;
import org.peertrust.event.QueryEvent;
import org.peertrust.net.Answer;
import org.peertrust.net.Query;
import org.peertrust.tnviz.app.TNEdge;
import org.peertrust.tnviz.app.TNNode;

/**
 * <p>
 * The TNSeqDiagramm class provides the means to create and maintain a
 * sequence diagramm. It is being used by the TNGraphics class in order to
 * visualize the sequence of queries and answers.
 * </p><p>
 * $Id: TestSequenceDiagramm.java,v 1.2 2005/11/23 23:24:02 token77 Exp $
 * <br/>
 * Date: 10-Feb-2005
 * <br/>
 * Last changed: $Date: 2005/11/23 23:24:02 $
 * by $Author: token77 $
 * Modified to get ride of the dependencies to Graphics and TNGui
 * </p>
 * @author Michael Schaefer and Sebastian Wittler
 */
public class TestSequenceDiagramm {

    private JGraph graph;
    private GraphModel model;
    private Hashtable graphElements;
    private final int NODE_HEIGHT = 40;
    private final int SEQ_START_DISTANCE_X = 30;
    private final int SEQ_START_DISTANCE_Y = 50;
    private final int SEQ_DISTANCE_Y = 40;
    private int SEQ_DISTANCE_X = 30;
    private int lastY;
    private int lastX;
    //private Graphics graphics;
    private Vector graphPath;
    private Vector nodes; // A vector with the main nodes in the order in which they are created.
    private Hashtable nodesInvisible; // A hashtable with the nodes as key and a vector with their invisible children.

    public PTEventListener ptListener= new PTEventListener(){

		public void event(PTEvent ptEvent) {
			if(ptEvent instanceof AnswerEvent){
				addAnswer(((AnswerEvent)ptEvent).getAnswer());
				refreshGraph();
			}else if(ptEvent instanceof QueryEvent){
				addQuery(((QueryEvent)ptEvent).getQuery());
				refreshGraph();
			}else{
				
			}
			return;
			
		}
    	
    };
    
    /**
     * The constructor of this class. It requires the graphics object with which
     * this diagramm will be used.
     * @param graphics The graphics object.
     */
    public TestSequenceDiagramm() {
        graph = new JGraph();
        model = new DefaultGraphModel();
        graphElements = new Hashtable();
        lastY = SEQ_START_DISTANCE_Y;
        lastX = SEQ_START_DISTANCE_X;
        graphPath = new Vector();
        nodes = new Vector();
        nodesInvisible = new Hashtable();
        graph.setModel(model);
        graph.setGraphLayoutCache(new GraphLayoutCache(model,
                new DefaultCellViewFactory(), true));
        //this.graphics = graphics;
    }

    /**
     * Cleans the graph by creating a new graph object and by resetting
     * status information like graphPath and graphElements.
     */
    public void wipeGraph() {
        graph = new JGraph();
        model = new DefaultGraphModel();
        graphElements = new Hashtable();
        lastY = SEQ_START_DISTANCE_Y;
        lastX = SEQ_START_DISTANCE_X;
        graphPath = new Vector();
        nodes = new Vector();
        nodesInvisible = new Hashtable();
        graph.setModel(model);
        graph.setGraphLayoutCache(new GraphLayoutCache(model,
                new DefaultCellViewFactory(), true));
    }

    /**
     * Adds a query. The query information is being read out and new nodes and
     * edges are created according to the sequence diagramm structure.
     * @param query The new query.
     */
    public void addQuery(Query query) {
        if (query.getSource() == null || query.getTarget() == null) {
            return;
        }
        String sourceAddress = query.getSource().getAddress();
        String sourceAlias = query.getSource().getAlias();
        int sourcePort = query.getSource().getPort();
        String sourceIdentifier = sourceAlias + ":" + sourceAddress + ":"
                + sourcePort;
        String targetAddress = query.getTarget().getAddress();
        String targetAlias = query.getTarget().getAlias();
        int targetPort = query.getTarget().getPort();
        String targetIdentifier = targetAlias + ":" + targetAddress + ":"
                + targetPort;
        String goal = query.getGoal();
        long reqQueryId = query.getReqQueryId();
        TNNode source = null;
        TNNode target = null;
        
        if (graphElements.containsKey("node:" + sourceIdentifier)) {
            source = (TNNode) graphElements.get("node:" + sourceIdentifier);
        }
        else {
            String id = createNode(sourceAlias, sourceAddress, sourceAlias,
                    sourcePort);
            source = getNode(id);
        }

        if (graphElements.containsKey("node:" + targetIdentifier)) {
            target = (TNNode) graphElements.get("node:" + targetIdentifier);
        }
        else {
            String id = createNode(targetAlias, targetAddress, targetAlias,
                    targetPort);
            target = getNode(id);
        }
        connectNodes(source, target, goal + " ?", goal, reqQueryId, true,
                false, -1, "");
    }

    /**
     * Adds an answer. The answer information is being read out and new nodes and
     * edges are created according to the sequence diagramm structure.
     * @param answer The new answer.
     */
    public void addAnswer(Answer answer) {
        if (answer.getSource() == null || answer.getTarget() == null) {
            return;
        }
        String sourceAddress = answer.getSource().getAddress();
        String sourceAlias = answer.getSource().getAlias();
        int sourcePort = answer.getSource().getPort();
        String sourceIdentifier = sourceAlias + ":" + sourceAddress + ":"
                + sourcePort;
        String targetAddress = answer.getTarget().getAddress();
        String targetAlias = answer.getTarget().getAlias();
        int targetPort = answer.getTarget().getPort();
        String targetIdentifier = targetAlias + ":" + targetAddress + ":"
                + targetPort;
        String goal = answer.getGoal();
        long reqQueryId = answer.getReqQueryId();
        int status = answer.getStatus();
        String proof = "" ;
        if (answer.getProof() != null)
        	proof = answer.getProof().toString();
        TNNode source = null;
        TNNode target = null;

        if (graphElements.containsKey("node:" + sourceIdentifier)) {
            source = (TNNode) graphElements.get("node:" + sourceIdentifier);
        }
        else {
            String id = createNode(sourceAlias, sourceAddress, sourceAlias,
                    sourcePort);
            source = getNode(id);
        }

        if (graphElements.containsKey("node:" + targetIdentifier)) {
            target = (TNNode) graphElements.get("node:" + targetIdentifier);
        }
        else {
            String id = createNode(targetAlias, targetAddress, targetAlias,
                    targetPort);
            target = getNode(id);
        }
        connectNodes(source, target, goal, goal, reqQueryId, false, true,
                status, proof);
    }

    /**
     * Returns the node with the given id.
     * @param id The id of the node.
     * @return The according node.
     */
    public TNNode getNode(String id) {
        return (TNNode) graphElements.get(id);
    }

    /**
     * Creates a new node with the given information.
     * @param object The label object.
     * @param peerAddress The peer address.
     * @param peerAlias The peer alias.
     * @param peerPort The peer port.
     * @return The id of the new node.
     */
    private String createNode(Object object, String peerAddress,
            String peerAlias, int peerPort) {
        TNNode node = new TNNode(object, graph);
        setNodeInformation(node, object.toString(), "node:" + peerAlias + ":"
                + peerAddress + ":" + peerPort, peerAddress, peerAlias,
                peerPort);
        graphElements.put(node.getId(), node);
        nodes.add(node);
        Vector elements = new Vector();
        elements.add(node);
        nodesInvisible.put(node, elements);
        DefaultPort port = new DefaultPort();
        node.add(port);
        node.setPort(port);
        Map nodeAttributes = new Hashtable();
        
        if (nodes.size() != 1) {
            lastX = lastX + SEQ_DISTANCE_X;
        }

        Rectangle nodeBounds = new Rectangle(lastX, SEQ_START_DISTANCE_Y, node
                .getLabelWidth(), NODE_HEIGHT);
        node.setX(lastX);
        node.setY(SEQ_START_DISTANCE_Y);
        GraphConstants.setBounds(nodeAttributes, nodeBounds);
        GraphConstants.setMoveable(	nodeAttributes, 
        							true/*graphics.getNodeMovable()*/);
        GraphConstants.setBendable(
        						nodeAttributes, 
        						true/*graphics.getNodeEditable()*/);
        GraphConstants.setSizeable(
        						nodeAttributes, 
        						false/*graphics.getNodeEditable()*/);
        GraphConstants.setEditable(
        						nodeAttributes, 
        						false/*graphics.getNodeEditable()*/);
        GraphConstants.setBorderColor(
        					nodeAttributes, 
        					Color.BLACK/*graphics.getNodeBorderColor()*/);
        GraphConstants.setBackground(
        				nodeAttributes, 
        				Color.LIGHT_GRAY/*graphics.getNodeBackgroundColor()*/);
        GraphConstants.setOpaque(
        					nodeAttributes, 
        					true);

        Map attributes = new Hashtable();
        attributes.put(node, nodeAttributes);
        graph.getGraphLayoutCache().insert(new Object[] { node }, attributes,
                null, null, null);

        lastX += node.getLabelWidth();

        return node.getId();
    }

    /**
     * Creates an invisible node which is needed to construct the sequence
     * diagramm structure.
     * @return The new invisible node.
     */
    private TNNode createInvisibleNode() {
        TNNode node = new TNNode("", graph);
        DefaultPort port = new DefaultPort();
        node.add(port);
        node.setPort(port);
        node.setInvisible(true);
        Map nodeAttributes = new Hashtable();
        Rectangle nodeBounds = new Rectangle(0, 0, 0, 0);
        
        GraphConstants.setBounds(nodeAttributes, nodeBounds);
        GraphConstants.setMoveable(
        					nodeAttributes, 
        					true/*graphics.getNodeMovable()*/);
        GraphConstants.setBendable(
        					nodeAttributes, 
        					true/*graphics.getNodeEditable()*/);
        GraphConstants.setSizeable(
        					nodeAttributes, 
        					true/*graphics.getNodeEditable()*/);
        GraphConstants.setEditable(
        					nodeAttributes, 
        					false/*graphics.getNodeEditable()*/);
        GraphConstants.setBorderColor(nodeAttributes, Color.WHITE);
        GraphConstants.setBackground(nodeAttributes, Color.WHITE);
        GraphConstants.setOpaque(nodeAttributes, true);

        Map attributes = new Hashtable();
        attributes.put(node, nodeAttributes);

        graph.getGraphLayoutCache().insert(new Object[] { node }, attributes,
                null, null, null);

        return node;
    }

//    /**
//     * Connects the two nodes with the given ids with a new edge with the given information.
//     * For this purpose, new invisible nodes and new visible edges are being
//     * created to construct the sequence diagramm structure.
//     * @param nodeSource The id of the source node.
//     * @param nodeTarget The id of the target node.
//     * @param object The label object.
//     * @param goal The goal.
//     * @param reqQueryId The reqQueryId.
//     * @param query True, if the connection represents a query, otherwise false.
//     * @param answer True, if the connection represents an answer, otherwise false.
//     * @param status The status.
//     * @param proof The proof.
//     * @return The id of the new edge.
//     */
//    private String connectNodes(String nodeSource, String nodeTarget,
//            Object object, String goal, long reqQueryId, boolean query,
//            boolean answer, int status, String proof) {
//        return connectNodes(getNode(nodeSource), getNode(nodeTarget), object,
//                goal, reqQueryId, query, answer, status, proof);
//    }

    /**
     * Connects the two given nodes with a new edge with the given information.
     * For this purpose, new invisible nodes and new visible edges are being
     * created to construct the sequence diagramm structure.
     * @param nodeSource The source node.
     * @param nodeTarget The target node.
     * @param object The label object.
     * @param goal The goal.
     * @param reqQueryId The reqQueryId.
     * @param query True, if the connection represents a query, otherwise false.
     * @param answer True, if the connection represents an answer, otherwise false.
     * @param status The status.
     * @param proof The proof.
     * @return The id of the new edge.
     */
    private String connectNodes(TNNode nodeSource, TNNode nodeTarget,
            Object object, String goal, long reqQueryId, boolean query,
            boolean answer, int status, String proof) {
        // Check the length of the edge label, and recompute the distance
        // between the nodes.
        int labelWidth = graph.getFontMetrics(graph.getFont()).stringWidth(
                object.toString()) + 10;

        if ((nodeTarget.getX() > nodeSource.getX())
                && (nodeTarget.getX() - nodeSource.getX()) < labelWidth) {
            SEQ_DISTANCE_X = labelWidth;
            repositionNodes();
        }
        else if ((nodeSource.getX() > nodeTarget.getX())
                && (nodeSource.getX() - nodeTarget.getX()) < labelWidth) {
            SEQ_DISTANCE_X = labelWidth;
            repositionNodes();
        }

        if (lastY == SEQ_START_DISTANCE_Y) {
            lastY = lastY + SEQ_DISTANCE_Y + NODE_HEIGHT;
        }
        else {
            lastY = lastY + SEQ_DISTANCE_Y;
        }
        
        int newY = lastY;

//        DefaultPort portSource = (DefaultPort) nodeSource.getPort();
//        DefaultPort portTarget = (DefaultPort) nodeTarget.getPort();
        TNNode lastInvisibleNodeSource;
        TNNode lastInvisibleNodeTarget;
        Vector sourceElements = (Vector) nodesInvisible.get(nodeSource);
        lastInvisibleNodeSource = (TNNode) sourceElements.lastElement();
        Vector targetElements = (Vector) nodesInvisible.get(nodeTarget);
        lastInvisibleNodeTarget = (TNNode) targetElements.lastElement();
        TNEdge invisibleEdgeSource = new TNEdge("");
        TNEdge invisibleEdgeTarget = new TNEdge("");
        Map edgeInvisibleSourceAttributes = new Hashtable();
        int arrowInvisible = GraphConstants.ARROW_NONE;

        GraphConstants
                .setLineEnd(edgeInvisibleSourceAttributes, arrowInvisible);
        GraphConstants.setEndFill(edgeInvisibleSourceAttributes, true);
        GraphConstants.setLabelAlongEdge(edgeInvisibleSourceAttributes, false);
        GraphConstants.setMoveable(
        				edgeInvisibleSourceAttributes, 
        				true/*graphics.getEdgeMovable()*/);
        GraphConstants.setConnectable(
        				edgeInvisibleSourceAttributes, 
        				true/*graphics.getEdgeMovable()*/);
        GraphConstants.setDisconnectable(
        			edgeInvisibleSourceAttributes,
        			true/*graphics.getEdgeMovable()*/);
        GraphConstants.setBendable(
        		edgeInvisibleSourceAttributes, 
        		true/*graphics.getEdgeMovable()*/);
        GraphConstants.setSizeable(
        			edgeInvisibleSourceAttributes, 
        			true/*graphics.getEdgeMovable()*/);
        GraphConstants.setEditable(
        		edgeInvisibleSourceAttributes, 
        		false/*graphics.getEdgeEditable()*/);
        GraphConstants.setLineColor(
        				edgeInvisibleSourceAttributes, 
        				Color.BLACK/*graphics.getEdgeColor()*/);

        Map edgeInvisibleTargetAttributes = new Hashtable();
        GraphConstants
                .setLineEnd(edgeInvisibleTargetAttributes, arrowInvisible);
        GraphConstants.setEndFill(edgeInvisibleTargetAttributes, true);
        GraphConstants.setLabelAlongEdge(edgeInvisibleTargetAttributes, false);
        GraphConstants.setMoveable(
        					edgeInvisibleTargetAttributes, 
        					true/*graphics.getEdgeMovable()*/);
        GraphConstants.setConnectable(
        			edgeInvisibleTargetAttributes, 
        			true/*graphics.getEdgeMovable()*/);
        GraphConstants.setDisconnectable(
        			edgeInvisibleTargetAttributes,
        			true/*graphics.getEdgeMovable()*/);
        GraphConstants.setBendable(
        		edgeInvisibleTargetAttributes, 
        		true/*graphics.getEdgeMovable()*/);
        GraphConstants.setSizeable(
        		edgeInvisibleTargetAttributes, 
        		true/*graphics.getEdgeMovable()*/);
        GraphConstants.setEditable(
        			edgeInvisibleTargetAttributes, 
        			false/*graphics.getEdgeEditable()*/);
        GraphConstants.setLineColor(
        			edgeInvisibleTargetAttributes, 
        			Color.BLACK/*graphics.getEdgeColor()*/);

        TNEdge edge = new TNEdge(object);
        edge.setLabel(object.toString());
        Map edgeAttributes = new Hashtable();
        int arrow;

        if (query) {
            arrow = GraphConstants.ARROW_SIMPLE;
        }
        else {
            arrow = GraphConstants.ARROW_CLASSIC;
        }

        GraphConstants.setLineEnd(edgeAttributes, arrow);
        GraphConstants.setEndFill(edgeAttributes, true);
        GraphConstants.setLabelAlongEdge(edgeAttributes, true);
        GraphConstants.setDashPattern(edgeAttributes, new float[] { 6 });
        GraphConstants.setMoveable(edgeAttributes, 
        							true/*graphics.getEdgeMovable()*/);
        GraphConstants.setConnectable(
        						edgeAttributes, 
        						true/*graphics.getEdgeMovable()*/);
        GraphConstants.setDisconnectable(
        						edgeAttributes, 
        						false/*graphics.getEdgeMovable()*/);
        GraphConstants.setBendable(
        						edgeAttributes, 
        						true/*graphics.getEdgeMovable()*/);
        GraphConstants.setSizeable(edgeAttributes, 
        							true/*graphics.getEdgeMovable()*/);
        GraphConstants.setEditable(
        						edgeAttributes, 
        						false/*graphics.getEdgeEditable()*/);
        GraphConstants.setLineColor(
        						edgeAttributes, 
        						Color.BLACK/*graphics.getEdgeColor()*/);

        Map attributes = new Hashtable();
        attributes.put(invisibleEdgeSource, edgeInvisibleSourceAttributes);
        ConnectionSet cs = new ConnectionSet();
        TNNode invisibleNodeSource = createInvisibleNode();
        cs.connect(invisibleEdgeSource, lastInvisibleNodeSource.getPort(),
                invisibleNodeSource.getPort());
        sourceElements.add(invisibleNodeSource);
        nodesInvisible.put(nodeSource, sourceElements);
        //positionInvisibleNode(invisibleNodeSource,nodeSource.getX()+(nodeSource.getLabelWidth()/2),lastY);
        positionInvisibleNode(invisibleNodeSource, nodeSource.getX()
                + (nodeSource.getLabelWidth() / 2), newY);
        graph.getGraphLayoutCache().insert(
                new Object[] { invisibleEdgeSource }, attributes, cs, null,
                null);

        attributes = new Hashtable();
        attributes.put(invisibleEdgeTarget, edgeInvisibleTargetAttributes);
        cs = new ConnectionSet();
        TNNode invisibleNodeTarget = createInvisibleNode();
        cs.connect(invisibleEdgeTarget, lastInvisibleNodeTarget.getPort(),
                invisibleNodeTarget.getPort());
        targetElements.add(invisibleNodeTarget);
        nodesInvisible.put(nodeTarget, targetElements);

        //positionInvisibleNode(invisibleNodeTarget,nodeTarget.getX()+(nodeTarget.getLabelWidth()/2),lastY);
        positionInvisibleNode(invisibleNodeTarget, nodeTarget.getX()
                + (nodeTarget.getLabelWidth() / 2), newY);
        graph.getGraphLayoutCache().insert(
                new Object[] { invisibleEdgeTarget }, attributes, cs, null,
                null);
        attributes = new Hashtable();
        attributes.put(edge, edgeAttributes);

        cs = new ConnectionSet();
        cs.connect(edge, invisibleNodeSource.getPort(), invisibleNodeTarget
                .getPort());

        graph.getGraphLayoutCache().insert(new Object[] { edge }, attributes,
                cs, null, null);
        setEdgeInformation(edge, edge.getLabel(), "edge:" + reqQueryId + ":"
                + goal, goal, reqQueryId, query, answer, status, proof);

        graphPath.add(edge);
        graphElements.put(edge.getId(), edge);

        return edge.getId();
    }

    /**
     * Updated the given node with the given information.
     * @param node The node which will be updated.
     * @param title The title.
     * @param id The id.
     * @param peerAddress The peer address.
     * @param peerAlias The peer alias.
     * @param peerPort The peer port.
     */
    private void setNodeInformation(TNNode node, String title, String id,
            String peerAddress, String peerAlias, int peerPort) {
        node.setTitle(title);
        node.setId(id);
        node.setPeerAddress(peerAddress);
        node.setPeerAlias(peerAlias);
        node.setPeerPort(peerPort);
    }

    /**
     * Updated the given edge with the given information.
     * @param edge The edge which will be updated.
     * @param label The label.
     * @param id The id.
     * @param goal The goal.
     * @param reqQueryId The reqQueryId.
     * @param True, if the edge represents a query, otherwise false.
     * @param answer True, if the edge represents an answer, otherwise false.
     * @param status The status.
     * @param proof The proof.
     */
    private void setEdgeInformation(TNEdge edge, String label, String id,
            String goal, long reqQueryId, boolean query, boolean answer,
            int status, String proof) {
        edge.setId(id);
        edge.setLabel(label);
        edge.setGoal(goal);
        edge.setReqQueryId(reqQueryId);
        edge.setQuery(query);
        edge.setAnswer(answer);
        edge.setStatus(status);
        edge.setProof(proof);
    }

    /**
     * Positions the given invisible node. Subsequently, the graph will be
     * refreshed. An invisible node is characterized by the fact that its
     * width and height is 0.
     * @param node The invisible node.
     * @param x The new x coordinate.
     * @param y The new y coordinate.
     */
    private void positionInvisibleNode(TNNode node, int x, int y) {
        Rectangle nodeBounds = new Rectangle(x, y, 0, 0);
        Map nodeAttributes = node.getAttributes();
        GraphConstants.setBounds(nodeAttributes, nodeBounds);
        node.setX(x);
        node.setY(y);
        refreshGraph();
    }

    /**
     * Positions the given node. Subsequently, the graph will be refreshed.
     * @param node The node.
     * @param x The new x coordinate.
     * @param y The new y coordinate.
     */
    private void positionNode(TNNode node, int x, int y) {
        Rectangle nodeBounds = new Rectangle(x, y, node.getLabelWidth(),
                NODE_HEIGHT);
        Map nodeAttributes = node.getAttributes();
        GraphConstants.setBounds(nodeAttributes, nodeBounds);
        node.setX(x);
        node.setY(y);
        refreshGraph();
    }

    /**
     * Repositions the nodes in the sequence diagramm. This is necessary when
     * a new query or answer is inserted whose label is wider than the currently
     * available space. all elements of the sequence diagramm are repositions
     * for this purpose.
     */
    private void repositionNodes() {
        lastX = SEQ_START_DISTANCE_X;
        for (Enumeration e1 = nodes.elements(); e1.hasMoreElements();) {
            TNNode node = (TNNode) e1.nextElement();
            if (!node.equals(nodes.firstElement())) {
                lastX += SEQ_DISTANCE_X;
            }
            positionNode(node, lastX, node.getY());
            Vector elements = (Vector) nodesInvisible.get(node);
            for (Enumeration e2 = elements.elements(); e2.hasMoreElements();) {
                TNNode invisibleNode = (TNNode) e2.nextElement();
                if (!node.equals(invisibleNode)) {
                    positionInvisibleNode(invisibleNode, node.getX()
                            + (node.getLabelWidth() / 2), invisibleNode.getY());
                }
            }
        }
    }

    /**
     * Returns the JGraph object on which this diagramm is operating.
     * @return The JGraph object.
     */
    public JGraph getGraph() {
        return graph;
    }

    /**
     * Refreshed the graphs. Should be called after changes were made.
     */
    public void refreshGraph() {
        graph.getGraphLayoutCache().reload();
        graph.repaint();
    }

    /**
     * Returns the graphPath of this diagramm. The graphPath vector contains
     * the graph elements in the order in which they were created.
     * Additional graph elements which were automatically created for the
     * structure of the diagramm are not included.
     * @return A vector with the graph elements.
     */
    public Vector getGraphPath() {
        return graphPath;
    }

    /**
     * Sets the graphPath to the given vector.
     * @param graphPath The new graphPath.
     */
    public void setGraphPath(Vector graphPath) {
        this.graphPath = graphPath;
    }

    /**
     * Returns a hashtable with all graph elements. The keys are the ids of the
     * elements, the value the according objects.
     * @return A hashtable with the graph elements.
     */
    public Hashtable getGraphElements() {
        return graphElements;
    }
    
    /**
     * Sets the graph elements to the given hashtable.
     * @param graphElements The new graph elements table.
     */
    public void setGraphElements(Hashtable graphElements) {
        this.graphElements = graphElements;
    }

}

