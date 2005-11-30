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
package g4mfs.impl.org.peertrust.tnviz.app;

import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.Port;
import org.jgraph.JGraph;

/**
 * <p>
 * 
 * </p><p>
 * $Id: TNNode.java,v 1.1 2005/11/30 10:35:09 ionut_con Exp $
 * <br/>
 * Date: 10-Feb-2005
 * <br/>
 * Last changed: $Date: 2005/11/30 10:35:09 $
 * by $Author: ionut_con $
 * </p>
 * @author Sebastian Wittler and Michael Sch?fer
 */
final public class TNNode extends DefaultGraphCell {
	
	private static final long serialVersionUID = 1L;
	private JGraph graph;
	private boolean bExpanded=true;
	private int nStufe=0;
	private int nX,nY;
	private Port port;
	private boolean invisible;
	private int nLabelWidth;
	
	private String title;
	private String id;
	private String peerAddress;
	private String peerAlias;
	private int peerPort;

	public TNNode(Object object,JGraph graph) {
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
		nLabelWidth=graph.getFontMetrics(graph.getFont()).stringWidth(object.toString())+10;
	}
	
	public void setObject(Object object) {
		this.setUserObject(object);
		nLabelWidth=graph.getFontMetrics(graph.getFont()).stringWidth(object.toString())+10;
	}

	public Object getObject() {
		return getUserObject();
	}

	public void setExpanded(boolean expand) {
		bExpanded=expand;
	}

	public boolean getExpanded() {
		return bExpanded;
	}

	public void setStufe(int stufe) {
		nStufe=stufe;
	}

	public int getStufe() {
		return nStufe;
	}
	
	public void setX(int x) {
		nX=x;
	}

	public int getX() {
		return nX;
	}
	
	public void setY(int y) {
		nY=y;
	}

	public int getY() {
		return nY;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public Port getPort() {
		return port;
	}
	
	public void setPort(Port port) {
		this.port = port;
	}
		
	public boolean isInvisible() {
		return invisible;
	}
	
	public void setInvisible(boolean invisible) {
		this.invisible = invisible;
	}

	public int getLabelWidth() {
		return nLabelWidth;
	}
	
	public String getPeerAddress() {
		return peerAddress;
	}
	
	public void setPeerAddress(String peerAddress) {
		this.peerAddress = peerAddress;
	}
	
	public String getPeerAlias() {
		return peerAlias;
	}
	
	public void setPeerAlias(String peerAlias) {
		this.peerAlias = peerAlias;
	}
	
	public int getPeerPort() {
		return peerPort;
	}
	
	public void setPeerPort(int peerPort) {
		this.peerPort = peerPort;
	}
	
	public JGraph getGraph() {
		return graph;
	}
	
	public void setGraph(JGraph graph) {
		this.graph = graph;
	}
	
}