package org.peertrust.tnviz.app;

import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.Port;
import org.jgraph.JGraph;

final public class TNNode extends DefaultGraphCell {
	
	private static final long serialVersionUID = 1L;
	private JGraph graph;
	private boolean bExpanded=true;
	private int nStufe=0;
	private int nX,nY;
	private TNNode lastSeqNode;
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
		lastSeqNode = this;
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
		
	public TNNode getLastSeqNode() {
		return lastSeqNode;
	}
	
	public void setLastSeqNode(TNNode lastSeqNode) {
		this.lastSeqNode = lastSeqNode;
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
	
}