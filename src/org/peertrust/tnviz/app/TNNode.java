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

import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.Port;
import org.jgraph.JGraph;

/**
 * <p>
 * The TNNode class is a custom version of the DefaultGraphCell class. It contains
 * needed information like ports, address and alias data. This data can be 
 * easily accessed through the class methods. 
 * </p><p>
 * $Id: TNNode.java,v 1.5 2005/06/09 05:51:31 dolmedilla Exp $
 * <br/>
 * Date: 10-Feb-2005
 * <br/>
 * Last changed: $Date: 2005/06/09 05:51:31 $
 * by $Author: dolmedilla $
 * </p>
 * @author Michael Schaefer and Sebastian Wittler
 */
final public class TNNode extends DefaultGraphCell {

    private static final long serialVersionUID = 1L;
    private JGraph graph;
    private boolean bExpanded = true;
    private int nStufe = 0;
    private int nX, nY;
    private Port port;
    private boolean invisible;
    private int nLabelWidth;
    private String title;
    private String id;
    private String peerAddress;
    private String peerAlias;
    private int peerPort;

    /**
     * Constructor of this class. The given object will be used as the label of
     * this node. The given JGraph object is the reference to the graph in which
     * this node exists.
     * @param object The label object.
     * @param graph The JGraph object.
     */
    public TNNode(Object object, JGraph graph) {
        super(object);
        this.graph = graph;
        nX = 0;
        nY = 0;
        title = "";
        id = "";
        peerAddress = "";
        peerAlias = "";
        peerPort = 0;
        port = null;
        invisible = false;
        nLabelWidth = graph.getFontMetrics(graph.getFont()).stringWidth(
                object.toString()) + 10;
    }

    /**
     * Sets the object of the node which represents the label to the gien object.
     * @param object The new label object.
     */
    public void setObject(Object object) {
        this.setUserObject(object);
        nLabelWidth = graph.getFontMetrics(graph.getFont()).stringWidth(
                object.toString()) + 10;
    }

    /**
     * Returns the object of the node.
     * @return The object.
     */
    public Object getObject() {
        return getUserObject();
    }

    /**
     * Sets the expanded flag to the given value.
     * @param expand The new flag.
     */
    public void setExpanded(boolean expand) {
        bExpanded = expand;
    }

    /**
     * Returns the expanded flag, which marks the node as expanded or collapsed.
     * @return boolean True, if the node is expanded, otherwise false. 
     */
    public boolean getExpanded() {
        return bExpanded;
    }

    /** 
     * Sets the level of this node in the tree to the given value. 
     * @param stufe The new level.
     */
    public void setStufe(int stufe) {
        nStufe = stufe;
    }

    /**
     * Returns the level of this node in the tree.
     * @return The level of this node.
     */
    public int getStufe() {
        return nStufe;
    }

    /**
     * Sets the x coordinate to the given value.
     * @param x The new x coordinate.
     */
    public void setX(int x) {
        nX = x;
    }

    /**
     * Returns the x coordinate of this node object.
     * @return The x coordinate.
     */
    public int getX() {
        return nX;
    }

    /**
     * Sets the y coordinate to the given value.
     * @param y The new y coordinate.
     */
    public void setY(int y) {
        nY = y;
    }

    /**
     * Returns the y coordinate of this node object.
     * @return The y coordinate.
     */
    public int getY() {
        return nY;
    }

    /**
     * Returns the title of this node object.
     * @return The title coordinate.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of this node to the given string.
     * @param title The new title.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Returns the id of this node object.
     * @return The id.
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the id of this node to the given string.
     * @param id The new id.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Returns the port object of this node. The port is created and assigned
     * when the node is created in the diagramm.
     * @return The port object.
     */
    public Port getPort() {
        return port;
    }

    /**
     * Sets the port object of this node to the given port.
     * @param port The new port.
     */
    public void setPort(Port port) {
        this.port = port;
    }

    /**
     * Returns the invisible flag of this node.
     * @return True, if the node is invisible, otherwise false.
     */
    public boolean isInvisible() {
        return invisible;
    }

    /**
     * Sets the invisible flag to the given value.
     * @param invisible The new flag.
     */
    public void setInvisible(boolean invisible) {
        this.invisible = invisible;
    }

    /**
     * Returns the width of the label of the node.
     * @return The width of the label.
     */
    public int getLabelWidth() {
        return nLabelWidth;
    }

    /**
     * Returns the peer address of this node.
     * @return The peer address.
     */
    public String getPeerAddress() {
        return peerAddress;
    }

    /**
     * Sets the peer address of this node object to the given string.
     * @param peerAddress The new peer address.
     */
    public void setPeerAddress(String peerAddress) {
        this.peerAddress = peerAddress;
    }

    /**
     * Returns the peer alias of this node.
     * @return The peer alias.
     */
    public String getPeerAlias() {
        return peerAlias;
    }

    /**
     * Sets the peer alias to the given string.
     * @param peerAlias The new peer alias.
     */
    public void setPeerAlias(String peerAlias) {
        this.peerAlias = peerAlias;
    }

    /**
     * Returns the peer port of this node object.
     * @return The peer port.
     */
    public int getPeerPort() {
        return peerPort;
    }

    /**
     * Sets the peer port to the given value.
     * @param peerPort The new peer port.
     */
    public void setPeerPort(int peerPort) {
        this.peerPort = peerPort;
    }

    /**
     * Returns the JGraph object in which this node object exists.
     * @return The JGraph object.
     */
    public JGraph getGraph() {
        return graph;
    }

    /**
     * Sets the JGraph object in which this node object exists to the given object.
     * @param graph The new graph.
     */
    public void setGraph(JGraph graph) {
        this.graph = graph;
    }

}