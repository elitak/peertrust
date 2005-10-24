package org.peertrust.demo.client.applet;

import java.awt.Color;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.jgraph.JGraph;
import org.jgraph.graph.GraphConstants;
import org.peertrust.net.Answer;
import org.peertrust.net.Query;
import org.peertrust.tnviz.app.Graphics;
import org.peertrust.tnviz.app.TNEdge;
import org.peertrust.tnviz.app.TNGraphics;
import org.peertrust.tnviz.app.TNNode;
import org.peertrust.tnviz.app.TNReplay;
import org.peertrust.tnviz.app.TNSeqDiagramm;
import org.peertrust.tnviz.app.TNTreeDiagramm;
import org.peertrust.tnviz.gui.TNGui;

public class JPanelGuiBasedTNGraphics implements Graphics {
	//private final int MIN_X_ABSTAND = 110; //Minimal horizontal distance between the centers of two nodes

    //private final int MIN_Y_ABSTAND = 40; //Minimal vertical distance between the centers of two nodes
    
        
	private int nodeBoundsX;
    private int nodeBoundsY;
    private Color nodeBackgroundColor;
    private Color nodeBorderColor;
    private Color edgeColor;
    private boolean nodeEditable;
    private boolean nodeMovable;
    private boolean edgeEditable;
    private boolean edgeMovable;
    //private TNGui gui;
    private JPanelTNGui gui;
    
    private final int MIN_RADIUS = 100;
    private final int RADIUS_NORMAL = 110;
    private TNNode nodeRoot;
    private int nRadius = RADIUS_NORMAL;
    private List listVisibleNodes;
    private double fScaleX;
    private JPanelGuiBasedTNReplay replay;
    private int layout;
    private NonWindowTNGuiBasedTNTreeDiagramm treeDiagramm;
    private TNSeqDiagramm sequenceDiagramm;
    
    

    /**
     * The default constructor for the class. New empty diagramms are 
     * constructed and a new gui object is created.
     */
    public JPanelGuiBasedTNGraphics() {
        nodeBoundsX = /* 80 */nRadius;
        nodeBoundsY = /* 20 */nRadius;
        nodeBackgroundColor = Color.red;
        nodeBorderColor = Color.black;
        edgeColor = Color.black;
        nodeEditable = false;
        nodeMovable = false;
        edgeEditable = false;
        edgeMovable = false;
        replay = null;
        layout = TREE_LAYOUT;
        treeDiagramm = new NonWindowTNGuiBasedTNTreeDiagramm(this);
        sequenceDiagramm = new TNSeqDiagramm(this);

        nodeRoot = null;

        listVisibleNodes = new Vector();
        gui= new JPanelTNGui();
        //gui = new TNGui();

        switch (layout) {
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

    /**
     * Wipes the graphs by deleting all contents from the different diagramms
     * and by deleting status information.
     */
    public void wipeGraph() {
        treeDiagramm.wipeGraph();
        sequenceDiagramm.wipeGraph();
        nodeRoot = null;
        listVisibleNodes = new Vector();
        refreshGraph();
    }

    /**
     * Adds a given query to all diagramms. The query is forwarded to the
     * different diagramms which process the queries further and display it.
     * @param query The new query.
     */
    public void addQuery(Query query) {
    	System.out.println("adding:"+query);
        treeDiagramm.addQuery(query);
        sequenceDiagramm.addQuery(query);
    }

    /**
     * Adds a given answer to all diagramms. The answer is forwarded to the
     * different diagramms which process the answer further and display it.
     * @param answer The new answer.
     */
    public void addAnswer(Answer answer) {
        treeDiagramm.addAnswer(answer);
        sequenceDiagramm.addAnswer(answer);
    }

    /**
     * Returns the JGraph on which the diagramm is operating. Which JGraph 
     * object is being returned depends on which diagramm is currently being 
     * used (determined through the layout which can be set).
     * @return JGraph The JGraph object of the current diagramm.
     */
    public JGraph getGraph() {
        switch (layout) {
	        case TREE_LAYOUT:
	            return treeDiagramm.getGraph();
	        case SEQ_LAYOUT:
	            return sequenceDiagramm.getGraph();
	        default:
	            return treeDiagramm.getGraph();
        }
    }

    /**
     * Refreshes the graphs and repaints them. This method can be called after
     * a change was made in order to ensure that the change will be displayed
     * correctly. Only the current diagramm will be refreshed.
     */
    public void refreshGraph() {
        switch (layout) {
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

    /**
     * Returns the currently set color of edge objects.
     * @return Color The edge color.
     */
    public Color getEdgeColor() {
        return edgeColor;
    }

    /**
     * Sets the color of all edge objects to the given color.
     * @param color The new color.
     */
    public void setEdgeColor(Color color) {
        edgeColor = color;
        Object[] cells = treeDiagramm.getGraph().getRoots();
        for (int i = 0; i < cells.length; i++) {
            Object object = cells[i];
            if (object instanceof TNEdge) {
                TNEdge edge = (TNEdge) object;
                Map edgeAttributes = edge.getAttributes();
                GraphConstants.setLineColor(edgeAttributes, color);
            }
        }
        cells = sequenceDiagramm.getGraph().getRoots();
        for (int i = 0; i < cells.length; i++) {
            Object object = cells[i];
            if (object instanceof TNEdge) {
                TNEdge edge = (TNEdge) object;
                Map edgeAttributes = edge.getAttributes();
                GraphConstants.setLineColor(edgeAttributes, color);
            }
        }
    }

    /**
     * Sets the color of the given edge object to the given color.
     * @param edge The edge object whose color will be changed.
     * @param color The new color.
     */
    public void setEdgeColor(TNEdge edge, Color color) {
        Map edgeAttributes = edge.getAttributes();
        GraphConstants.setLineColor(edgeAttributes, color);
    }

    /**
     * Returns the color of the border of nodes.
     * @return Color The border color.
     */
    public Color getNodeBorderColor() {
        return nodeBorderColor;
    }

    /**
     * Returns the color of the background of nodes.
     * @return Color The background color.
     */
    public Color getNodeBackgroundColor() {
        return nodeBackgroundColor;
    }

    /**
     * Sets the color of all nodes to the given border and background color.
     * @param borderColor The new border color.
     * @param backgroundColor The new background color.
     */
    public void setNodeColor(Color borderColor, Color backgroundColor) {
        nodeBorderColor = borderColor;
        nodeBackgroundColor = backgroundColor;
        Object[] cells = treeDiagramm.getGraph().getRoots();
        for (int i = 0; i < cells.length; i++) {
            Object object = cells[i];
            if (!(object instanceof TNEdge) && (object instanceof TNNode)) {
                TNNode node = (TNNode) object;
                Map nodeAttributes = node.getAttributes();
                GraphConstants.setBorderColor(nodeAttributes, borderColor);
                GraphConstants.setBackground(nodeAttributes, backgroundColor);
                GraphConstants.setOpaque(nodeAttributes, true);
            }
        }
        cells = sequenceDiagramm.getGraph().getRoots();
        for (int i = 0; i < cells.length; i++) {
            Object object = cells[i];
            if (!(object instanceof TNEdge) && (object instanceof TNNode)) {
                TNNode node = (TNNode) object;
                Map nodeAttributes = node.getAttributes();
                GraphConstants.setBorderColor(nodeAttributes, borderColor);
                GraphConstants.setBackground(nodeAttributes, backgroundColor);
                GraphConstants.setOpaque(nodeAttributes, true);
            }
        }
    }

    /**
     * Sets the color of the given node to the given border and background 
     * color.
     * @param borderColor The new border color.
     * @param backgroundColor The new background color.
     */
    public void setNodeColor(TNNode node, Color borderColor,
            Color backgroundColor) {
        Map nodeAttributes = node.getAttributes();
        GraphConstants.setBorderColor(nodeAttributes, borderColor);
        GraphConstants.setBackground(nodeAttributes, backgroundColor);
        GraphConstants.setOpaque(nodeAttributes, true);
    }

    /**
     * Returns the edgeEditable flag, which determines whether an edge can
     * be edited in the graph.
     * @return boolean True, if the edge may be edited, otherwise false.
     */
    public boolean getEdgeEditable() {
        return edgeEditable;
    }

    /**
     * Sets the edgeEditable flag, which determines whether an edge can
     * be edited in the graph, to the given value. All edge will be updated
     * with the new value.
     * @param editable The new edgeEditable flag.
     */
    public void setEdgeEditable(boolean editable) {
        edgeEditable = editable;
        Object[] cells = treeDiagramm.getGraph().getRoots();
        for (int i = 0; i < cells.length; i++) {
            Object object = cells[i];
            if (object instanceof TNEdge) {
                TNEdge edge = (TNEdge) object;
                Map edgeAttributes = edge.getAttributes();
                GraphConstants.setEditable(edgeAttributes, edgeEditable);
            }
        }
        cells = sequenceDiagramm.getGraph().getRoots();
        for (int i = 0; i < cells.length; i++) {
            Object object = cells[i];
            if (object instanceof TNEdge) {
                TNEdge edge = (TNEdge) object;
                Map edgeAttributes = edge.getAttributes();
                GraphConstants.setEditable(edgeAttributes, edgeEditable);
            }
        }
    }

    /**
     * Returns the edgeMovable flag, which determines whether an edge may be
     * moved in the graph.
     * @return boolean True, if the edges may be moved, otherwise false.
     */
    public boolean getEdgeMovable() {
        return edgeMovable;
    }

    /**
     * Sets the edgeMovable flag, which determines whether an edge may be
     * moved in the graph, to the given value.
     * @param movable The new edgeMovable flag.
     */
    public void setEdgeMovable(boolean movable) {
        edgeMovable = movable;
        Object[] cells = treeDiagramm.getGraph().getRoots();
        for (int i = 0; i < cells.length; i++) {
            Object object = cells[i];
            if (object instanceof TNEdge) {
                TNEdge edge = (TNEdge) object;
                Map edgeAttributes = edge.getAttributes();
                GraphConstants.setMoveable(edgeAttributes, edgeMovable);
                GraphConstants.setBendable(edgeAttributes, edgeMovable);
                GraphConstants.setSizeable(edgeAttributes, edgeMovable);
                GraphConstants.setConnectable(edgeAttributes, edgeMovable);
                GraphConstants.setDisconnectable(edgeAttributes, edgeMovable);
            }
        }
        cells = sequenceDiagramm.getGraph().getRoots();
        for (int i = 0; i < cells.length; i++) {
            Object object = cells[i];
            if (object instanceof TNEdge) {
                TNEdge edge = (TNEdge) object;
                Map edgeAttributes = edge.getAttributes();
                GraphConstants.setMoveable(edgeAttributes, edgeMovable);
                GraphConstants.setBendable(edgeAttributes, edgeMovable);
                GraphConstants.setSizeable(edgeAttributes, edgeMovable);
                GraphConstants.setConnectable(edgeAttributes, edgeMovable);
                GraphConstants.setDisconnectable(edgeAttributes, edgeMovable);
            }
        }
    }

    /**
     * Returns the nodeEditable flag, which determines whether a node may be
     * edited in the graph.
     * @return boolean True, if the nodes may be edited, otherwise false.
     */
    public boolean getNodeEditable() {
        return nodeEditable;
    }

    /**
     * Sets the nodeEditable flag, which determines whether a node may be
     * edited in the graph, to the given value.
     * @param editable The new nodeEditable flag.
     */
    public void setNodeEditable(boolean editable) {
        nodeEditable = editable;
        Object[] cells = treeDiagramm.getGraph().getRoots();
        for (int i = 0; i < cells.length; i++) {
            Object object = cells[i];
            if (!(object instanceof TNEdge) && (object instanceof TNNode)) {
                TNNode node = (TNNode) object;
                Map nodeAttributes = node.getAttributes();
                GraphConstants.setBendable(nodeAttributes, nodeEditable);
                GraphConstants.setSizeable(nodeAttributes, nodeEditable);
                GraphConstants.setEditable(nodeAttributes, nodeEditable);
            }
        }
        cells = sequenceDiagramm.getGraph().getRoots();
        for (int i = 0; i < cells.length; i++) {
            Object object = cells[i];
            if (!(object instanceof TNEdge) && (object instanceof TNNode)) {
                TNNode node = (TNNode) object;
                Map nodeAttributes = node.getAttributes();
                GraphConstants.setBendable(nodeAttributes, nodeEditable);
                GraphConstants.setSizeable(nodeAttributes, nodeEditable);
                GraphConstants.setEditable(nodeAttributes, nodeEditable);
            }
        }
    }

    /**
     * Returns the nodeMovable flag, which determines whether a node may be
     * moved in the graph.
     * @return boolean True, if the nodes may be moved, otherwise false.
     */
    public boolean getNodeMovable() {
        return nodeMovable;
    }

    /**
     * Sets the nodeMovable flag, which determines whether a node may be
     * moved in the graph, to the given value.
     * @param movable The new nodeMovable flag.
     */
    public void setNodeMovable(boolean movable) {
        nodeMovable = movable;
        Object[] cells = treeDiagramm.getGraph().getRoots();
        for (int i = 0; i < cells.length; i++) {
            Object object = cells[i];
            if (!(object instanceof TNEdge) && (object instanceof TNNode)) {
                TNNode node = (TNNode) object;
                Map nodeAttributes = node.getAttributes();
                GraphConstants.setMoveable(nodeAttributes, nodeMovable);
            }
        }
        cells = sequenceDiagramm.getGraph().getRoots();
        for (int i = 0; i < cells.length; i++) {
            Object object = cells[i];
            if (!(object instanceof TNEdge) && (object instanceof TNNode)) {
                TNNode node = (TNNode) object;
                Map nodeAttributes = node.getAttributes();
                GraphConstants.setMoveable(nodeAttributes, nodeMovable);
            }
        }
    }
    
    /**
     * Collapses a given node. This is being done by traversing the subgraph
     * of this node and by setting all subnodes to invisible. This affects only
     * the current diagramm.
     * @param node The node which will be collapsed.
     */
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

    /**
     * Expands a given node. This is being done by traversing the subgraph
     * of this node and by setting all subnodes to visible. This affects only
     * the current diagramm.
     * @param node The node which will be expanded.
     */
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

    /**
     * Starts the replay function of the current diagramm. The replay visualizes
     * the order of changes made in the graph.
     */
    public void startReplayGraphPath() {
        switch (layout) {
	        case TREE_LAYOUT:
	            replay = new JPanelGuiBasedTNReplay(gui, this, treeDiagramm.getGraphPath());
	            break;
	        case SEQ_LAYOUT:
	            replay = new JPanelGuiBasedTNReplay(gui, this, sequenceDiagramm.getGraphPath());
	            break;
	        default:
	            replay = new JPanelGuiBasedTNReplay(gui, this, treeDiagramm.getGraphPath());
	            break;
        }
        replay.startReplay();
    }

    /**
     * Stops the replay function of the current diagramm. If the replay is not
     * running, nothing will happen.
     */
    public void stopReplayGraphPath() {
        replay.stopReplay();
        setNodeColor(nodeBorderColor, nodeBackgroundColor);
        refreshGraph();
    }

    /**
     * Returns the current layout. The layout determines which layout (Tree or
     * Sequence) will be currently viewed. This also affects several other
     * methods.
     * @return int Graphics.TREE_LAYOUT or Graphics.SEQ_LAYOUT.
     */
    public int getLayout() {
        return layout;
    }

    /**
     * Sets the current layout to the given value. The layout determines which 
     * layout (Tree or Sequence) will be currently viewed. This also affects 
     * several other methods.
     * @param layout The new layout: Graphics.TREE_LAYOUT or Graphics.SEQ_LAYOUT.
     */
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

    /**
     * Returns the x bounds of nodes.
     * @return int The x bounds.
     */
    public int getNodeBoundsX() {
        return nodeBoundsX;
    }

    /**
     * Sets the x bounds of nodes to the given value.
     * @param nodeBoundsX The new x bounds.
     */
    public void setNodeBoundsX(int nodeBoundsX) {
        this.nodeBoundsX = nodeBoundsX;
    }

    /**
     * Returns the y bounds of nodes.
     * @return int The y bounds.
     */
    public int getNodeBoundsY() {
        return nodeBoundsY;
    }

    /**
     * Sets the y bounds of nodes to the given value.
     * @param nodeBoundsY The new y bounds.
     */
    public void setNodeBoundsY(int nodeBoundsY) {
        this.nodeBoundsY = nodeBoundsY;
    }

    /**
     * Returns the radius of nodes.
     * @return int The radius.
     */
    public int getNRadius() {
        return nRadius;
    }

    /**
     * Sets the radius of nodes to the given value.
     * @param nRadius The new radius.
     */
    public void setNRadius(int nRadius) {
        this.nRadius = nRadius;
    }

    /**
     * Returns the root node of the graphs.
     * @return TNNode The root node.
     */
    public TNNode getRoot() {
        return nodeRoot;
    }

    /**
     * Sets the root node of the graphs to the given object. The graph will be
     * refreshed after the change.
     * @param node The new root node.
     */
    public void setRoot(TNNode node) {
        nodeRoot = node;
        refreshGraph();
    }

    /**
     * Returns the fScaleX of nodes.
     * @return double The fScaleX.
     */
    public double getFScaleX() {
        return fScaleX;
    }
    
    /**
     * Sets the fScaleX of nodes to the given value.
     * @param fScaleX The new fScaleX.
     */
    public void setFScaleX(double fScaleX) {

        this.fScaleX = fScaleX;

    }

    /**
     * Returns the default radius of nodes.
     * @return int The default radius.
     */
    public int getRadiusNormal() {
        return RADIUS_NORMAL;
    }

    /**
     * Return the TNGui object on which the class is operating.
     * @return TNGui The TNGui object.
     */
    public TNGui getGui() {
        return null;//gui;
    }
    
    public JPanelTNGui getJPanelTNGui(){
    	return gui;
    }
    
    /**
     * Returns the minimum radius of nodes.
     * @return int The minimum radius.
     */
    public int getMinRadius() {
        return MIN_RADIUS;
    }

    /**
     * Updates the graph by recalculating the layout of the graph. This only 
     * affects the Graphics.TREE_LAYOUT.
     */
    public void updateGraph() {
        if (layout == TREE_LAYOUT)
            treeDiagramm.calculateGraphLayout();
    }

   
    
}
