
package org.peertrust.tnviz.app;

import org.peertrust.tnviz.gui.TNGui;

import java.awt.Color;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.jgraph.JGraph;
import org.jgraph.graph.GraphConstants;

import org.peertrust.net.*;

public class TNGraphics implements Graphics {
	
	private int nodeBoundsX;
	private int nodeBoundsY;
	private Color nodeBackgroundColor;
	private Color nodeBorderColor;
	private Color edgeColor;
	private boolean nodeEditable;
	private boolean nodeMovable;
	private boolean edgeEditable;
	private boolean edgeMovable;
	private TNGui gui;
	private final int MIN_RADIUS=100;
	private final int RADIUS_NORMAL=110;
	private TNNode nodeRoot;
	private int nRadius=RADIUS_NORMAL;
	private List listVisibleNodes;
	private double fScaleX;
	private TNReplay replay;
	private int layout;
	private TNTreeDiagramm treeDiagramm;
	private TNSeqDiagramm sequenceDiagramm;

	public static void main(String[] args) {
		
		TNGraphics graphics = new TNGraphics();
		
	}
	
	public TNGraphics() {
		
		nodeBoundsX = /*80*/nRadius;
		nodeBoundsY = /*20*/nRadius;
		nodeBackgroundColor = Color.red;
		nodeBorderColor = Color.black;
		edgeColor = Color.black;
		nodeEditable = false;
		nodeMovable = false;
		edgeEditable = false;
		edgeMovable = false;
		replay = null;
		layout = TREE_LAYOUT;
		
		treeDiagramm = new TNTreeDiagramm(this);
		sequenceDiagramm = new TNSeqDiagramm(this);
						
		nodeRoot=null;
		listVisibleNodes=new Vector();
		
		gui = new TNGui();
		switch(layout) {
			case TREE_LAYOUT:
				gui.setCollapseEnabled(true);
				break;
			case SEQ_LAYOUT:
				gui.setCollapseEnabled(false);
				break;
			default:
				gui.setCollapseEnabled(true);
				break;
		}
		
		gui.setGraphics(this);
		
	}
	
	public void wipeGraph() {
		treeDiagramm.wipeGraph();
		sequenceDiagramm.wipeGraph();
		nodeRoot = null;
		listVisibleNodes = new Vector();
		refreshGraph();
	}
	
	public void addQuery(Query query) {
		treeDiagramm.addQuery(query);
		sequenceDiagramm.addQuery(query);
	}
	
	public void addAnswer(Answer answer) {
		treeDiagramm.addAnswer(answer);
		sequenceDiagramm.addAnswer(answer);
	}
	
	public JGraph getGraph() {
		switch(layout) {
			case TREE_LAYOUT:
				return treeDiagramm.getGraph();
			case SEQ_LAYOUT:
				return sequenceDiagramm.getGraph();
			default:
				return treeDiagramm.getGraph();
		}
	}
		
	public void refreshGraph() {
		switch(layout) {
			case TREE_LAYOUT:
				treeDiagramm.refreshGraph();
				break;
			case SEQ_LAYOUT:
				sequenceDiagramm.refreshGraph();
				break;
			default:
				treeDiagramm.refreshGraph();
				break;
		}
	}
		
	public Color getEdgeColor() {
		return edgeColor;
	}
		
	public void setEdgeColor(Color color) {
		edgeColor = color;
		Object[] cells = treeDiagramm.getGraph().getRoots();
		for (int i=0; i<cells.length; i++) {
			Object object = cells[i];
			if (object instanceof TNEdge) {
				TNEdge edge = (TNEdge)object;
				Map edgeAttributes = edge.getAttributes();
				GraphConstants.setLineColor(edgeAttributes,color);
			}
		}
		cells = sequenceDiagramm.getGraph().getRoots();
		for (int i=0; i<cells.length; i++) {
			Object object = cells[i];
			if (object instanceof TNEdge) {
				TNEdge edge = (TNEdge)object;
				Map edgeAttributes = edge.getAttributes();
				GraphConstants.setLineColor(edgeAttributes,color);
			}
		}
	}
	
	public void setEdgeColor(TNEdge edge, Color color) {
		Map edgeAttributes = edge.getAttributes();
		GraphConstants.setLineColor(edgeAttributes,color);
	}
	
	public Color getNodeBorderColor() {
		return nodeBorderColor;
	}
	
	public Color getNodeBackgroundColor() {
		return nodeBackgroundColor;
	}
	
	public void setNodeColor(Color borderColor, Color backgroundColor) {
		nodeBorderColor = borderColor;
		nodeBackgroundColor = backgroundColor;
		Object[] cells = treeDiagramm.getGraph().getRoots();
		for (int i=0; i<cells.length; i++) {
			Object object = cells[i];
			if (!(object instanceof TNEdge) && (object instanceof TNNode)) {
				TNNode node = (TNNode)object;
				Map nodeAttributes = node.getAttributes();
				GraphConstants.setBorderColor(nodeAttributes,borderColor);
			    GraphConstants.setBackground(nodeAttributes,backgroundColor);
			    GraphConstants.setOpaque(nodeAttributes,true);
			}
		}
		cells = sequenceDiagramm.getGraph().getRoots();
		for (int i=0; i<cells.length; i++) {
			Object object = cells[i];
			if (!(object instanceof TNEdge) && (object instanceof TNNode)) {
				TNNode node = (TNNode)object;
				Map nodeAttributes = node.getAttributes();
				GraphConstants.setBorderColor(nodeAttributes,borderColor);
			    GraphConstants.setBackground(nodeAttributes,backgroundColor);
			    GraphConstants.setOpaque(nodeAttributes,true);
			}
		}
	}
	
	public void setNodeColor(TNNode node, Color borderColor, Color backgroundColor) {
		Map nodeAttributes = node.getAttributes();
		GraphConstants.setBorderColor(nodeAttributes,borderColor);
	    GraphConstants.setBackground(nodeAttributes,backgroundColor);
	    GraphConstants.setOpaque(nodeAttributes,true);
	}
	
	public boolean getEdgeEditable() {
		return edgeEditable;
	}
	
	public void setEdgeEditable(boolean editable) {
		edgeEditable = editable;
		Object[] cells = treeDiagramm.getGraph().getRoots();
		for (int i=0; i<cells.length; i++) {
			Object object = cells[i];
			if (object instanceof TNEdge) {
				TNEdge edge = (TNEdge)object;
				Map edgeAttributes = edge.getAttributes();
				GraphConstants.setEditable(edgeAttributes,edgeEditable);
			}
		}
		cells = sequenceDiagramm.getGraph().getRoots();
		for (int i=0; i<cells.length; i++) {
			Object object = cells[i];
			if (object instanceof TNEdge) {
				TNEdge edge = (TNEdge)object;
				Map edgeAttributes = edge.getAttributes();
				GraphConstants.setEditable(edgeAttributes,edgeEditable);
			}
		}
	}
	
	public boolean getEdgeMovable() {
		return edgeMovable;
	}
	
	public void setEdgeMovable(boolean movable) {
		edgeMovable = movable;
		Object[] cells = treeDiagramm.getGraph().getRoots();
		for (int i=0; i<cells.length; i++) {
			Object object = cells[i];
			if (object instanceof TNEdge) {
				TNEdge edge = (TNEdge)object;
				Map edgeAttributes = edge.getAttributes();
				GraphConstants.setMoveable(edgeAttributes,edgeMovable);
				GraphConstants.setBendable(edgeAttributes,edgeMovable);
			    GraphConstants.setSizeable(edgeAttributes,edgeMovable);
			    GraphConstants.setConnectable(edgeAttributes,edgeMovable);
			    GraphConstants.setDisconnectable(edgeAttributes,edgeMovable);
			}
		}
		cells = sequenceDiagramm.getGraph().getRoots();
		for (int i=0; i<cells.length; i++) {
			Object object = cells[i];
			if (object instanceof TNEdge) {
				TNEdge edge = (TNEdge)object;
				Map edgeAttributes = edge.getAttributes();
				GraphConstants.setMoveable(edgeAttributes,edgeMovable);
				GraphConstants.setBendable(edgeAttributes,edgeMovable);
			    GraphConstants.setSizeable(edgeAttributes,edgeMovable);
			    GraphConstants.setConnectable(edgeAttributes,edgeMovable);
			    GraphConstants.setDisconnectable(edgeAttributes,edgeMovable);
			}
		}
	}
	
	public boolean getNodeEditable() {
		return nodeEditable;
	}
	
	public void setNodeEditable(boolean editable) {
		nodeEditable = editable;
		Object[] cells = treeDiagramm.getGraph().getRoots();
		for (int i=0; i<cells.length; i++) {
			Object object = cells[i];
			if (!(object instanceof TNEdge) && (object instanceof TNNode)) {
				TNNode node = (TNNode)object;
				Map nodeAttributes = node.getAttributes();
				GraphConstants.setBendable(nodeAttributes,nodeEditable);
				GraphConstants.setSizeable(nodeAttributes,nodeEditable);
			    GraphConstants.setEditable(nodeAttributes,nodeEditable);
			}
		}
		cells = sequenceDiagramm.getGraph().getRoots();
		for (int i=0; i<cells.length; i++) {
			Object object = cells[i];
			if (!(object instanceof TNEdge) && (object instanceof TNNode)) {
				TNNode node = (TNNode)object;
				Map nodeAttributes = node.getAttributes();
				GraphConstants.setBendable(nodeAttributes,nodeEditable);
				GraphConstants.setSizeable(nodeAttributes,nodeEditable);
			    GraphConstants.setEditable(nodeAttributes,nodeEditable);
			}
		}
	}
	
	public boolean getNodeMovable() {
		return nodeMovable;
	}
	
	public void setNodeMovable(boolean movable) {
		nodeMovable = movable;
		Object[] cells = treeDiagramm.getGraph().getRoots();
		for (int i=0; i<cells.length; i++) {
			Object object = cells[i];
			if (!(object instanceof TNEdge) && (object instanceof TNNode)) {
				TNNode node = (TNNode)object;
				Map nodeAttributes = node.getAttributes();
				GraphConstants.setMoveable(nodeAttributes,nodeMovable);
			}
		}
		cells = sequenceDiagramm.getGraph().getRoots();
		for (int i=0; i<cells.length; i++) {
			Object object = cells[i];
			if (!(object instanceof TNEdge) && (object instanceof TNNode)) {
				TNNode node = (TNNode)object;
				Map nodeAttributes = node.getAttributes();
				GraphConstants.setMoveable(nodeAttributes,nodeMovable);
			}
		}
	}
	
	public void collapse(TNNode node) {
		switch (layout) {
			case TREE_LAYOUT:
				treeDiagramm.collapse(node);
				break;
			case SEQ_LAYOUT:
				break;
			default:
				treeDiagramm.collapse(node);
				break;
		}
	}
	
	public void expand(TNNode node) {
		switch (layout) {
			case TREE_LAYOUT:
				treeDiagramm.expand(node);
				break;
			case SEQ_LAYOUT:
				break;
			default:
				treeDiagramm.expand(node);
				break;
		}
	}
	
	public void startReplayGraphPath() {
		switch (layout) {
			case TREE_LAYOUT:
				replay = new TNReplay(gui,this,treeDiagramm.getGraphPath());
				break;
			case SEQ_LAYOUT:
				replay = new TNReplay(gui,this,sequenceDiagramm.getGraphPath());
				break;
			default:
				replay = new TNReplay(gui,this,treeDiagramm.getGraphPath());
				break;
		}
		replay.startReplay();
	}
	
	public void stopReplayGraphPath() {
		replay.stopReplay();
		setNodeColor(nodeBorderColor,nodeBackgroundColor);
		refreshGraph();
	}
	
	public int getLayout() {
		return layout;
	}
	
	public void setLayout(int layout) {
		this.layout = layout;
		switch (layout) {
			case TREE_LAYOUT:
				treeDiagramm.calculateGraphLayout();
				gui.setCollapseEnabled(true);
				if (replay != null) {
					replay.stopReplay();
				}
				break;
			case SEQ_LAYOUT:
				gui.setCollapseEnabled(false);
				if (replay != null) {
					replay.stopReplay();
				}
				break;
			default:
				treeDiagramm.calculateGraphLayout();
				gui.setCollapseEnabled(true);
				if (replay != null) {
					replay.stopReplay();
				}
				break;
		}
	}
	
	public int getNodeBoundsX() {
		return nodeBoundsX;
	}
	
	public void setNodeBoundsX(int nodeBoundsX) {
		this.nodeBoundsX = nodeBoundsX;
	}
	
	public int getNodeBoundsY() {
		return nodeBoundsY;
	}
	
	public void setNodeBoundsY(int nodeBoundsY) {
		this.nodeBoundsY = nodeBoundsY;
	}
	
	public int getNRadius() {
		return nRadius;
	}
	
	public void setNRadius(int nRadius) {
		this.nRadius = nRadius;
	}
	
	public TNNode getRoot() {
		return nodeRoot;
	}

	public void setRoot(TNNode node) {
		nodeRoot=node;
		refreshGraph();
	}
	
	public double getFScaleX() {
		return fScaleX;
	}
	
	public void setFScaleX(double fScaleX) {
		this.fScaleX = fScaleX;
	}
	
	public int getRadiusNormal() {
		return RADIUS_NORMAL;
	}
	
	public TNGui getGui() {
		return gui;
	}
	
	public int getMinRadius() {
		return MIN_RADIUS;
	}

	public void updateGraph() {
		if(layout==TREE_LAYOUT)
			treeDiagramm.calculateGraphLayout();
	}		
}
