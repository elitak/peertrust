package org.peertrust.tnviz.app;

import java.awt.Rectangle;
import java.awt.FontMetrics;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.jgraph.JGraph;
import org.jgraph.graph.ConnectionSet;
import org.jgraph.graph.DefaultCellViewFactory;
import org.jgraph.graph.DefaultGraphModel;
import org.jgraph.graph.DefaultPort;
import org.jgraph.graph.GraphConstants;
import org.jgraph.graph.GraphLayoutCache;
import org.jgraph.graph.GraphModel;
import org.jgraph.graph.Port;

import org.peertrust.net.Answer;
import org.peertrust.net.Query;

public class TNTreeDiagramm {
	
	private JGraph graph;
	private GraphModel model;
	private Hashtable graphElements;
	private Graphics graphics;
	private Vector graphPath;
	private List listVisibleNodes;
	private List listNodes;
	private final int MIN_X_ABSTAND=110;
	private final int MIN_Y_ABSTAND=40;
	private FontMetrics fontmetrics;
	
	public TNTreeDiagramm(Graphics graphics) {
		graph = new JGraph();
		model = new DefaultGraphModel();
		graphElements = new Hashtable();
		graphPath = new Vector();
		listVisibleNodes = new Vector();

		graph.setModel(model);
		graph.setGraphLayoutCache(new GraphLayoutCache(model,new DefaultCellViewFactory(),true));
		
		this.graphics = graphics;
		fontmetrics=graph.getFontMetrics(graph.getFont());
	}
	
	public void addQuery(Query query) {
		String sourceAddress = query.getSource().getAddress();
		String sourceAlias = query.getSource().getAlias();
		int sourcePort = query.getSource().getPort();
		String sourceIdentifier = sourceAlias+":"+sourceAddress+":"+sourcePort;
		
		String targetAddress = query.getTarget().getAddress();
		String targetAlias = query.getTarget().getAlias();
		int targetPort = query.getTarget().getPort();
		String targetIdentifier = targetAlias+":"+targetAddress+":"+targetPort;
		
		long reqQueryId = query.getReqQueryId();
		String goal = query.getGoal();
		
		if(graphics.getRoot() == null) {
			TNNode source = (TNNode)getElement(createNode(sourceAddress,reqQueryId,sourceAddress,sourceAlias,sourcePort));
			TNNode target = (TNNode)getElement(createNode(targetAddress,reqQueryId,targetAddress,targetAlias,targetPort));
			TNEdge edge = (TNEdge)getElement(connectNodes(source,target,goal,goal,reqQueryId,true,false,-1,""));
			graphics.setRoot(source);
			// Build graph path.
			if ((graphPath.size() == 0) || (!graphPath.lastElement().equals(source))) {
				graphPath.add(source);
			}
			graphPath.add(edge);
			graphPath.add(target);
		}
		else {
			for(long i=reqQueryId;i>=0;i--) {
				TNNode source = (TNNode)getElement("node:"+i+":"+sourceIdentifier);
				if(source != null) {
					TNNode target = (TNNode)getElement(createNode(targetAddress,reqQueryId,targetAddress,targetAlias,targetPort));
					TNEdge edge = (TNEdge)getElement(connectNodes(source,target,goal,goal,reqQueryId,true,false,-1,""));
					// Build graph path.
					if ((graphPath.size() == 0) || (!graphPath.lastElement().equals(source))) {
						graphPath.add(source);
					}
					graphPath.add(edge);
					graphPath.add(target);
					break;
				}
			}
		}
	}
	
	public void addAnswer(Answer answer) {
		String sourceAddress = answer.getSource().getAddress();
		String sourceAlias = answer.getSource().getAlias();
		int sourcePort = answer.getSource().getPort();
		String sourceIdentifier = sourceAlias+":"+sourceAddress+":"+sourcePort;
		
		String targetAddress = answer.getTarget().getAddress();
		String targetAlias = answer.getTarget().getAlias();
		int targetPort = answer.getTarget().getPort();
		String targetIdentifier = targetAlias+":"+targetAddress+":"+targetPort;
		
		String goal = answer.getGoal();
		long reqQueryId = answer.getReqQueryId();
		int status = answer.getStatus();
		String proof = answer.getProof();

		TNNode source = (TNNode)getElement("node:"+reqQueryId+":"+sourceIdentifier);
		if(source != null) {
			TNNode target = (TNNode)getElement(createNode(targetAddress,reqQueryId,targetAddress,targetAlias,targetPort));
			TNEdge edge = (TNEdge)getElement(connectNodes(source,target,goal,goal,reqQueryId,true,false,-1,""));
			// Build graph path.
			if ((graphPath.size() == 0) || (!graphPath.lastElement().equals(source))) {
				graphPath.add(source);
			}
			graphPath.add(edge);
			graphPath.add(target);
		}
	}
	
	public Object getElement(String id) {
		return graphElements.get(id);
	}
	
	private String createNode(Object object, int x, int y, long reqQueryId, String peerAddress, String peerAlias, int peerPort) {
		TNNode node = new TNNode(object,graph);
		
		setNodeInformation(node,object.toString(),"node:"+reqQueryId+":"+peerAlias+":"+peerAddress+":"+peerPort,peerAddress,peerAlias,peerPort);
		graphElements.put(node.getId(),node);
		
		DefaultPort port = new DefaultPort();
	    node.add(port);
	    node.setPort(port);
		Map nodeAttributes = new Hashtable();
	    Rectangle nodeBounds = new Rectangle(x,y,graphics.getNodeBoundsX(),graphics.getNodeBoundsY());
	    GraphConstants.setBounds(nodeAttributes,nodeBounds);
	    
	    GraphConstants.setMoveable(nodeAttributes,graphics.getNodeMovable());
	    GraphConstants.setBendable(nodeAttributes,graphics.getNodeEditable());
	    GraphConstants.setSizeable(nodeAttributes,graphics.getNodeEditable());
	    GraphConstants.setEditable(nodeAttributes,graphics.getNodeEditable());
	    
	    GraphConstants.setBorderColor(nodeAttributes,graphics.getNodeBorderColor());
	    GraphConstants.setBackground(nodeAttributes,graphics.getNodeBackgroundColor());
	    GraphConstants.setOpaque(nodeAttributes,true);
	    
	    Map attributes = new Hashtable();
	    attributes.put(node,nodeAttributes);
	    graph.getGraphLayoutCache().insert(new Object[]{node},attributes,null,null,null);
	    	    
	    return node.getId();
	}
	
	public String createNode(Object object, long reqQueryId, String peerAddress, String peerAlias, int peerPort) {
		return createNode(object,0,0,reqQueryId,peerAddress,peerAlias,peerPort);
	}
	
	private String connectNodes(String nodeSource, String nodeTarget, Object object, String goal, long reqQueryId, boolean query, boolean answer, int status, String proof) {
		return connectNodes((TNNode)getElement(nodeSource),(TNNode)getElement(nodeTarget),object,goal,reqQueryId,query,answer,status,proof);
	}
	
	private String connectNodes(TNNode nodeSource, TNNode nodeTarget, Object object, String goal, long reqQueryId, boolean query, boolean answer, int status, String proof) {
		DefaultPort portSource = (DefaultPort)nodeSource.getPort();
		DefaultPort portTarget = (DefaultPort)nodeTarget.getPort();
		
	    TNEdge edge = new TNEdge(object);
	    edge.setLabel(object.toString());
	    Map edgeAttributes = new Hashtable();
	    int arrow = GraphConstants.ARROW_CLASSIC;
	    GraphConstants.setLineEnd(edgeAttributes,arrow);
	    GraphConstants.setEndFill(edgeAttributes,true);
	    GraphConstants.setLabelAlongEdge(edgeAttributes, true);
	    GraphConstants.setDashPattern(edgeAttributes, new float[]{6});
	    
	    GraphConstants.setMoveable(edgeAttributes,graphics.getEdgeMovable());
	    GraphConstants.setConnectable(edgeAttributes,graphics.getEdgeMovable());
	    GraphConstants.setDisconnectable(edgeAttributes,graphics.getEdgeMovable());
	    GraphConstants.setBendable(edgeAttributes,graphics.getEdgeMovable());
	    GraphConstants.setSizeable(edgeAttributes,graphics.getEdgeMovable());
	    GraphConstants.setEditable(edgeAttributes,graphics.getEdgeEditable());
	    GraphConstants.setLineColor(edgeAttributes,graphics.getEdgeColor());
	    
	    Map attributes = new Hashtable();
	    attributes.put(edge,edgeAttributes);
	    ConnectionSet cs = new ConnectionSet();
	    cs.connect(edge,portSource,portTarget);
	    
	    setEdgeInformation(edge,edge.getLabel(),"edge:"+reqQueryId+":"+goal,goal,reqQueryId,query,answer,status,proof);
		edge.setSourceNode(nodeSource);
		edge.setTargetNode(nodeTarget);
	    graphElements.put(edge.getId(),edge);
	    	    
	    graph.getGraphLayoutCache().insert(new Object[]{edge},attributes,cs,null,null);
	    
	    return edge.getId();
	}
		
	private void setNodeInformation(TNNode node, String title, String id, String peerAddress, String peerAlias, int peerPort) {
		node.setTitle(title);
		node.setId(id);
		node.setPeerAddress(peerAddress);
		node.setPeerAlias(peerAlias);
		node.setPeerPort(peerPort);
	}
	
	private void setEdgeInformation(TNEdge edge, String label, String id, String goal, long reqQueryId, boolean query, boolean answer, int status, String proof) {
		edge.setId(id);
		edge.setLabel(label);
		edge.setGoal(goal);
		edge.setReqQueryId(reqQueryId);
		edge.setQuery(query);
		edge.setAnswer(answer);
		edge.setStatus(status);
		edge.setProof(proof);
	}
	
	public JGraph getGraph() {
		return graph;
	}
		
	public void refreshGraph() {
		graph.getGraphLayoutCache().reload();
		graph.repaint();
	}

	public Hashtable getGraphElements() {
		return graphElements;
	}
	
	public void setGraphElements(Hashtable graphElements) {
		this.graphElements = graphElements;
	}
	
	public Vector getGraphPath() {
		return graphPath;
	}
	
	public void setGraphPath(Vector graphPath) {
		this.graphPath = graphPath;
	}
	
	private void positionNode(TNNode node, int x, int y) {
		positionNode(node,x,y,graphics.getNodeBoundsX(),graphics.getNodeBoundsY());
		refreshGraph();
	}
	
	private void positionNode(TNNode node, int x, int y, int width, int height) {
		int real_width=Math.min(width,node.getLabelWidth());
		int real_x=x;
		if(real_width<width) {
			real_x=x+(width-real_width)/2;
			width=real_width;
		}
		Rectangle nodeBounds = new Rectangle(real_x,y,width,height);
		Map nodeAttributes = node.getAttributes();
	    GraphConstants.setBounds(nodeAttributes,nodeBounds);
	    node.setX(x);
		node.setY(y);
	    refreshGraph();
	}
	
	public void calculateGraphLayout() {
		if(graphics.getRoot()==null)
			return;
		getVisibleNodes();
		graphics.setNRadius(graphics.getRadiusNormal());
		graphics.setFScaleX(1.0);
		int stufen[]=getStufen(),a;
		int y_radius=35;
		int y_delta=(stufen.length>1) ?
			(graphics.getGui().getPaneHeight()-y_radius)/(stufen.length-1) : 0;
		if(y_delta<MIN_Y_ABSTAND) {
			/*a=y_delta;
			y_delta-=2*y_radius;
			graphics.setNRadius((MIN_Y_ABSTAND-a)/2);
			if(graphics.getNRadius()<graphics.getMinRadius())
				graphics.setNRadius(graphics.getMinRadius());
			y_delta+=2*graphics.getNRadius();
			if(y_delta<MIN_Y_ABSTAND)*/
				y_delta=MIN_Y_ABSTAND;
		}
		int anzahl,x_delta;
		TNNode node;
		for(int i=0;i<stufen.length;i++) {
			anzahl=(i<stufen.length-1) ? stufen[i+1]-stufen[i] :
				listVisibleNodes.size()-stufen[i];
			x_delta=(graphics.getGui().getPaneWidth()-graphics.getNRadius())/anzahl;
			if(x_delta<MIN_X_ABSTAND) {
				a=x_delta;
				x_delta-=2*graphics.getNRadius();
				graphics.setNRadius((MIN_X_ABSTAND-a)/2);
				if(graphics.getNRadius()<graphics.getMinRadius())
					graphics.setNRadius(graphics.getMinRadius());
				x_delta+=2*graphics.getNRadius();
				if(x_delta<MIN_X_ABSTAND)
					graphics.setFScaleX((double)(anzahl*MIN_X_ABSTAND)/(double)graphics.getGui().getPaneWidth());
			}
			for(int j=stufen[i];j<stufen[i]+anzahl;j++) {
				node=(TNNode)listVisibleNodes.get(j);
				positionNode(node,(j-stufen[i]+1)*graphics.getGui().getPaneWidth()/
					(anzahl+1)-graphics.getNRadius()/2,(node.getStufe()-1)*y_delta);
			}
		}
		//if((graphics.getFScaleX()>1.0)||(graphics.getNRadius()<graphics.getRadiusNormal())) {
			for(int i=0;i<listVisibleNodes.size();i++) {
				node=(TNNode)listVisibleNodes.get(i);
				positionNode(node,(int)(graphics.getFScaleX()*node.getX()),
					node.getY(),graphics.getNRadius(),y_radius);
			}
		//}
		refreshGraph();
	}

	private void getVisibleNodes() {
		listVisibleNodes.clear();
		int new_index=1;
		graphics.getRoot().setStufe(1);
		listVisibleNodes.add(graphics.getRoot());
		listVisibleNodes.add(graphics.getRoot());
		TNNode actualnode,child;
		Vector children;
		while(new_index>0) {
			actualnode=(TNNode)listVisibleNodes.get(0);
			if(actualnode.getExpanded()) {
				children=getChildren(actualnode);
				if(children!=null)
					for(int i=0;i<children.size();i++) {
						child=(TNNode)children.get(i);
						child.setStufe(actualnode.getStufe()+1);
						listVisibleNodes.add(new_index,child);
						new_index++;
						listVisibleNodes.add(child);
					}
			}
			new_index--;
			listVisibleNodes.remove(0);
		}
	}

	private int[] getStufen() {
		int max=0;
		for(int i=0;i<listVisibleNodes.size();i++)
			max=Math.max(max,((TNNode)listVisibleNodes.get(i)).getStufe());
		int stufen[]=new int[max];
		if(max==0)
			return stufen;
		stufen[0]=0;
		for(int i=1;i<listVisibleNodes.size();i++)
			if(((TNNode)listVisibleNodes.get(i)).getStufe()>
				((TNNode)listVisibleNodes.get(i-1)).getStufe())
				stufen[((TNNode)listVisibleNodes.get(i)).getStufe()-1]=i;
		return stufen;
	}
	
	private Vector getChildren(TNNode node) {
		Vector result = new Vector();
		List children = node.getChildren();
		Object object = null;
		DefaultPort port = null;
		Object[] edges = null;
		TNEdge edge = null;
		Object target = null;
		DefaultPort targetPort = null;
		TNNode childNode = null;
		
		for (int i=0; i<children.size(); i++) {
			object = children.get(i);
			if (object instanceof Port) {
				port = (DefaultPort)object;
				edges = port.getEdges().toArray();
				for (int j=0; j<edges.length; j++) {
					edge = (TNEdge)edges[j];
					target = edge.getTarget();
					if (target instanceof Port) {
						targetPort = (DefaultPort)target;
						childNode = (TNNode)targetPort.getParent();
						if ((!childNode.equals(node)) && (!result.contains(node))) {
							result.add(childNode);
						}
					}
				}
			}
		}
		
		return result;
	}
	
	private Vector getEdges(TNNode node) {
		Vector result = new Vector();
		DefaultPort port = null;
		Object[] edges = null;
		TNEdge edge = null;
		
		port = (DefaultPort)node.getPort();
		edges = port.getEdges().toArray();
		for (int i=0; i<edges.length; i++) {
			edge = (TNEdge)edges[i];
			result.add(edge);
		}
				
		return result;
	}
		
	public void collapse(TNNode node) {
		node.setExpanded(false);
		TNNode childNode = null;
		List children = getChildren(node);
		Vector visited = new Vector();
		Vector invisible = new Vector();
		visited.add(node);
		invisible.add(node); // Has to be set, as the node which will be collapsed shall not be made invisible.
				
		for (int i=0; i<children.size(); i++) {
			childNode = (TNNode)children.get(i);
			if (!visited.contains(childNode)) {
				visited.add(childNode);
				collapseChild(childNode,visited,invisible);
			}
			else if (!invisible.contains(childNode)) {
				collapseChild(childNode,visited,invisible);
			}
		}
		
		// Change the label of the node to symbolize that it was collapsed.
		if (children.size() > 0) {
			String nodeLabel = node.getUserObject().toString();
			if (!nodeLabel.startsWith("(+)")) {
				node.setObject("(+) "+nodeLabel);
			}
		}
		
		graph.getGraphLayoutCache().reload();
		calculateGraphLayout();
			
	}
	
	private void collapseChild(TNNode child, Vector visited, Vector invisible) {
		TNNode childNode = null;
		List children = getChildren(child);
				
		if (!invisible.contains(child)) {
			graph.getGraphLayoutCache().setVisible(new Object[]{child}, false);
			child.setInvisible(true);
			invisible.add(child);
		}
				
		for (int i=0; i<children.size(); i++) {
			childNode = (TNNode)children.get(i);
			if (!visited.contains(childNode)) {
				visited.add(childNode);
				collapseChild(childNode,visited,invisible);
			}
			else if (!invisible.contains(childNode)) {
				collapseChild(childNode,visited,invisible);
			}
		}
		
		Vector edges = getEdges(child);
		for (int i=0; i<edges.size(); i++) {
			TNEdge edge = (TNEdge)edges.get(i);
			edge.setInvisible(true);
		}
		
	}
	
	public void expand(TNNode node) {
		node.setExpanded(true);
		TNNode childNode = null;
		List children = getChildren(node);
		Vector visited = new Vector();
		Vector visible = new Vector();
		visited.add(node);
		visible.add(node); // Has to be set, as the node which will be expanded shall not be made visible, as it already is visible.
				
		for (int i=0; i<children.size(); i++) {
			childNode = (TNNode)children.get(i);
			if (!visited.contains(childNode)) {
				visited.add(childNode);
				expandChild(childNode,visited,visible);
			}
			else if (!visible.contains(childNode)) {
				expandChild(childNode,visited,visible);
			}
		}
		
		// Change the label of the node to symbolize that it is expanded.
		String nodeLabel = node.getUserObject().toString();
		if (nodeLabel.startsWith("(+)")) {
			node.setObject(nodeLabel.substring(4,nodeLabel.length()));	
		}
		
		graph.getGraphLayoutCache().reload();
		calculateGraphLayout();
		
	}
	
	private void expandChild(TNNode child, Vector visited, Vector visible) {
		TNNode childNode = null;
		List children = getChildren(child);
		
		if (!visible.contains(child)) {
			graph.getGraphLayoutCache().setVisible(new Object[]{child}, true);
			child.setInvisible(false);
			visible.add(child);
		}
		if(!child.getExpanded()) {
			return;
		}
		
		for (int i=0; i<children.size(); i++) {
			childNode = (TNNode)children.get(i);
			if (!visited.contains(childNode)) {
				visited.add(childNode);
				expandChild(childNode,visited,visible);
			}
			else if (!visible.contains(childNode)) {
				expandChild(childNode,visited,visible);
			}
		}
		
		Vector edges = getEdges(child);
		for (int i=0; i<edges.size(); i++) {
			TNEdge edge = (TNEdge)edges.get(i);
			edge.setInvisible(false);
		}
		
		// Change the label of the node to symbolize that it is expanded.
		String nodeLabel = child.getUserObject().toString();
		if (nodeLabel.startsWith("(+)")) {
			child.setObject(nodeLabel.substring(4,nodeLabel.length()));
		}
		
		graph.getGraphLayoutCache().reload();
		
	}
	
}
