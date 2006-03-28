package org.peertrust.modeler.policysystem.views;

import java.awt.Color;
import java.awt.Component;
import java.awt.PopupMenu;
import java.awt.ScrollPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Rectangle2D;
import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import org.jgraph.JGraph;
import org.jgraph.event.GraphSelectionEvent;
import org.jgraph.event.GraphSelectionListener;
import org.jgraph.graph.DefaultEdge;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.DefaultGraphModel;
import org.jgraph.graph.DefaultGraphSelectionModel;
import org.jgraph.graph.DefaultPort;
import org.jgraph.graph.GraphConstants;
import org.jgraph.graph.GraphModel;


import java.awt.Font;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import org.eclipse.ui.IViewSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.*;
import org.eclipse.swt.*;
import org.eclipse.swt.awt.SWT_AWT;

import org.eclipse.swt.widgets.*;

/**
 * <p>
 * This view is used to show the tree structure. This class contains methods to manipulate the tree.
 * </p><p>
 * $Id: CredentialEditorView.java,v 1.1 2006/03/28 20:54:58 token77 Exp $
 * <br/>
 * Date: 2005/08/04
 * <br/>
 * Last changed: $Date: 2006/03/28 20:54:58 $
 * by $Author: token77 $
 * </p>
 * @author Emre Cakar - Cem Dogan
 */
public class CredentialEditorView /*extends ViewPart*/{
//	/**
//	 * id =  "Editor.CredentialEditorView"
//	 */
//	public static final String ID = "Editor.CredentialEditorView";
//	/**
//	 * The frame which contains the ScrollPane object.
//	 */
//	public static java.awt.Frame frame;
//	private static Enumeration enu;
//	public static Composite composite;
//	private static String parentName = null;
//	private static Node selectedNode = null;
//	private static Node parentNode = null;
//	private static GraphModel model = new DefaultGraphModel();
//	/**
//	 * The JGraph object, where the tree is shown. 
//	 */
//	private static JGraph graph = new JGraph(model);
//	private static final PopupMenu pm = new PopupMenu();
//	private static java.awt.MenuItem mi = null;
//	private static DefaultGraphCell selectedCell = null;
//	private static boolean parentExists = false;
//	private static DefaultGraphCell cell = null;
//	private static ScrollPane sp = null;
//	private static Hashtable allCells = new Hashtable();
//	private DataModel dataModel;
//	
//	/* (non-Javadoc)
//	 * @see org.eclipse.ui.part.ViewPart#init(org.eclipse.ui.IViewSite)
//	 */
//	public void init(IViewSite site) throws PartInitException {
//		super.init(site);
//		dataModel=
//			(DataModel)site.getShell().getData(
//					DataModel.WORK_BENCH_KEY_CREDENTIAL_EDITOR_DATA_MODEL);
//	}
//	
//	/**
//	* The elements (nodes and their relationships) in the graph are removed and the graphical representation of the tree information in the run time 
//	* data model is generated and shown each time this method is invoked. 
//	* The tree is build starting from the root(s).
//	*/
//	public static void drawGraph (DataModel dataModel){
//		
//		Node node = null;
//		DefaultGraphCell[] cells = null;
//		int depth = 0, cellCount= 0 ; 
//		Vector v = new Vector(); 
//	
//		allCells.clear(); graph.getModel().remove(graph.getRoots());
//
//		enu = dataModel.nodes.elements();
//		while(enu.hasMoreElements()){
//			node = (Node)enu.nextElement();
//			if (node.depth == depth) v.add(node);
//			else if (node.depth > depth) {
//				depth = node.depth;
//				v.removeAllElements(); v.add(node);
//			}
//		}
//		getInitialGraph(dataModel,v, depth);
//		
//		cells = new DefaultGraphCell[allCells.size()];
//		enu = allCells.elements();
//		while(enu.hasMoreElements()){
//			cells[cellCount++] = (DefaultGraphCell)enu.nextElement();
//		}
//		
//		graph.getGraphLayoutCache().insert(cells);
//		graph.setVisible(true);
//		
//	}
//	
//
//	/**
//	 * This method is called recursively to create the objects (Nodes and their relationships) for the tree. 
//	 * @param v The vector v, which contains the node(s) with maximal depth, in this case the root(s).
//	 * @param dep The integer dep is the depth of the root(s).  
//	 */
//	private static void getInitialGraph(DataModel dataModel,Vector v, int dep){
//		Vector childNodes = new Vector();
//		DefaultGraphCell cell = null; 
//		DefaultGraphCell childCell = null;
//		int distance = 0;
//		Node node = null;
//		int depth = dep;
//		int nodeCountInDepth = v.size();
//		
//		distance = composite.getBounds().width / (nodeCountInDepth + 1);
//		for (int k = 0; k < v.size() ; ++k){
//			node = (Node)v.elementAt(k);
//			cell = createVertex(node, 
//					node.x = distance * (k + 1), 
//					node.y = composite.getBounds().height - (52 +(depth * 50)), 
//					75 , 30, node.isCredential ? !((Credential)node).existsInKeyStore ? Color.RED : Color.GREEN : Color.ORANGE, false);
//			allCells.put(node, cell);
//			enu = node.childNodes.elements();
//			while (enu.hasMoreElements()){
//				childNodes.add((Node)enu.nextElement());
//			}
//		}
//		
//		enu = dataModel.nodes.elements();
//		while(enu.hasMoreElements()){
//			node = (Node)enu.nextElement();
//			if (node.depth == depth - 1 && node.parentNode == null) childNodes.add(node);
//		}
//		
//		enu = allCells.keys();
//		while(enu.hasMoreElements()){
//			Object o = enu.nextElement();
//			if (Node.class.isInstance(o)){
//				node = (Node)o;
//				childCell = (DefaultGraphCell)allCells.get(node);
//				if (node.parentNode != null){
//					if ((cell = (DefaultGraphCell)allCells.get(node.parentNode)) != null){
//						DefaultEdge edge = new DefaultEdge();
//						edge.setSource(cell.getChildAt(0)); edge.setTarget(childCell.getChildAt(0));
//						GraphConstants.setLineEnd(edge.getAttributes(), GraphConstants.ARROW_CLASSIC);
//						GraphConstants.setEndFill(edge.getAttributes(), true); GraphConstants.setSelectable(edge.getAttributes(), true);
//						allCells.put(node.name + node.parentNode.name , edge);
//					}
//				}
//			}
//		}
//		
//		if (depth > 0 )
//			getInitialGraph(dataModel,childNodes,--depth);
//	}
//	
//	/**
//	* This method creates the nodes of the tree.
//	* @param The node object in the data model to show.
//	* @param The x value of the node.
//	* @param The y value of the node.
//	* @param The width of the node.
//	* @param The heigth of the node.
//	* @param The color of the node.
//	* @param The boolean to determine to use a raised border.
//	* @return The graphical representation of the Node object.
//	*/
//	
//	private static DefaultGraphCell createVertex(Node node, double x, double y, double w, double h, Color bg, boolean raised) {
//
//		// Create vertex with the given name
//		DefaultGraphCell cell = null;
//
//		if (node.isCredential)
//			cell = new DefaultGraphCell(((Credential)node).name + " Sn:" + ((Credential)node).serialNumber);
//		else
//			cell = new DefaultGraphCell(node.name);
//
//		// Set bounds
//		GraphConstants.setBounds(cell.getAttributes(), new Rectangle2D.Double(
//				x, y, w, h));
//		Font f= new Font("SansSerif", Font.BOLD , 11);
//		GraphConstants.setFont(cell.getAttributes(), f);
//		
//		// Set fill color
//		if (bg != null) {
//			GraphConstants.setGradientColor(cell.getAttributes(), bg);
//			GraphConstants.setOpaque(cell.getAttributes(), true);
//		}
//
//		// Set raised border
//		if (raised)
//			GraphConstants.setBorder(cell.getAttributes(), BorderFactory
//					.createRaisedBevelBorder());
//		else
//			// Set black border
//			GraphConstants.setBorderColor(cell.getAttributes(), Color.black);
//
//		// Add a Port
//		DefaultPort port = new DefaultPort();
//		cell.add(port);
//		port.setParent(cell);
//
//		return cell;
//	}
//	
//	/**
//	* In this method the Frame object is created, which contains the ScrollPane object. The ScrollPane object contains the DefaultGraph object.
//	* Two listeners GraphSelectionListener and MouseListener are added to the graph. 
//	* The GraphSelectionListener is used to identify the selected node in the graph.
//	* The MouseListener is used to show the popup menu.
//	* The popup menu, which can be used to delete node and add node is also created here.   
//	* @param The container object.
//	*/
//	public void createPartControl(Composite parent) {
//		DefaultGraphSelectionModel dgsm = new DefaultGraphSelectionModel(graph);
//		composite = new Composite (parent,SWT.EMBEDDED);
//		frame = SWT_AWT.new_Frame(composite);
//		frame.add(sp = new ScrollPane(ScrollPane.SCROLLBARS_AS_NEEDED));
//		
//		dgsm.setSelectionMode(DefaultGraphSelectionModel.SINGLE_GRAPH_SELECTION);
//		
//		graph.setCloneable(true);
//		graph.setInvokesStopCellEditing(true);
//		graph.setJumpToDefaultPort(true);
//		graph.setSelectionModel(dgsm);
//		
//		graph.addGraphSelectionListener(new GraphSelectionListener(){
//			public void valueChanged(GraphSelectionEvent arg0) {
//				String cellName = null;
//				if (arg0.getCell() != null && (cellName = arg0.getCell().toString()) !=null){
//					selectedCell = (DefaultGraphCell)arg0.getCell(); 
//					selectedNode = (Node)dataModel.nodes.get(arg0.getCell().toString());
//					if (selectedNode == null)
//						selectedNode = (Credential)dataModel.nodes.get(cellName.substring(cellName.indexOf(":")+1));
//				}
//			}
//		});
//		
//		
//		graph.addMouseListener(new MouseListener(){
//			public void mouseClicked(MouseEvent arg0) {
//				if (arg0.getButton() != 1 && graph.getSelectionCell() != null){
//					Component c = arg0.getComponent();
//					pm.show(c, arg0.getX(), arg0.getY());
//				}
//			}	
//			public void mousePressed(MouseEvent arg0) {}
//			public void mouseReleased(MouseEvent arg0) {}
//			public void mouseEntered(MouseEvent arg0) {}
//			public void mouseExited(MouseEvent arg0) {}
//			
//		});
//		
//		mi = new java.awt.MenuItem("Delete");
//	    mi.addActionListener(new ActionListener(){
//
//			public void actionPerformed(ActionEvent arg0) {
//				String cellName = null;
//				Enumeration en = null;
//				Object [] o = null;
//				Vector v = new Vector();
//				DefaultEdge e = null;
//				Node sourceNode = null , targetNode = null;
//				
//				if (!DefaultEdge.class.isInstance(graph.getSelectionCell())){
//					if (selectedNode.parentNode != null)
//						selectedNode.parentNode.childNodes.remove(selectedNode.name);
//					
//					en = selectedNode.childNodes.elements();
//					while (en.hasMoreElements())
//						((Node)en.nextElement()).parentNode = null;
//				
//					dataModel.nodes.remove(selectedNode.name);
//					
//					for (int  i = 0 ; i < graph.getRoots().length ; ++i){
//						if (DefaultEdge.class.isInstance(graph.getRoots()[i])){
//							e = (DefaultEdge)graph.getRoots()[i];
//							if (((DefaultPort)e.getSource()).getParent().equals(graph.getSelectionCell())) v.add(e); 
//							if (((DefaultPort)e.getTarget()).getParent().equals(graph.getSelectionCell())) v.add(e); 
//						}
//					}
//				}else{
//					e = (DefaultEdge)graph.getSelectionCell();
//					
//					cellName = ((DefaultGraphCell)((DefaultPort)e.getSource()).getParent()).toString();
//					if (cellName.contains("Sn:"))
//						cellName = cellName.substring(cellName.indexOf(":")+1);
//					sourceNode = (Node)dataModel.nodes.get(cellName);
//					
//					cellName = ((DefaultGraphCell)((DefaultPort)e.getTarget()).getParent()).toString();
//					if (cellName.contains("Sn:"))
//						cellName = cellName.substring(cellName.indexOf(":")+1);
//					targetNode = (Node)dataModel.nodes.get(cellName);
//					
//					sourceNode.childNodes.remove(targetNode.name); targetNode.parentNode = null;
//	
//				}
//				v.add(graph.getSelectionCell());
//				
//				o = new Object[v.size()];
//				for (int i = 0 ; i < v.size() ; ++i)
//					o[i] = v.elementAt(i);
//				
//				graph.getModel().remove(o);
//				
//			}
//	    	
//	    });
//	    pm.add(mi);
//
//	    mi = new java.awt.MenuItem("Add Parent");
//	    mi.addActionListener(new ActionListener(){
//	    	public void actionPerformed(ActionEvent arg0) {
//	    		if (selectedNode != null){
//	    			String childName = selectedNode.name,  question = "";
//		    		if (selectedNode.isCredential)
//		    			question = "Create parent for Credential with serial number "+ childName + ":";
//		    		else
//		    			question = "Create parent for " + childName + ":";  
//					
//		    		parentName = (String)JOptionPane.showInputDialog(
//			                frame, question, "Name of parent class", JOptionPane.PLAIN_MESSAGE, null, null, "");
//					
//		    		if (parentName.trim().compareTo("") == 0) JOptionPane.showMessageDialog(frame, "You have to specify a name for the parent class.");
//		    		else if(parentName.contains("Sn:")) JOptionPane.showMessageDialog(frame, "You can't use \"Sn:\" in the name of the parent. " +
//		    																		  "It is used the designate the serial number of the credentials.");
//		    		else{
//						DefaultGraphCell[] parent = new DefaultGraphCell[2];
//						DefaultEdge edge = new DefaultEdge();
//						if (selectedNode.parentNode != null){ 
//							JOptionPane.showMessageDialog(frame, "This node has already a parent.");
//						}else{
//							for (int i = 0; i < graph.getRoots().length ;++i){
//								String cellName = ((DefaultGraphCell)graph.getRoots()[i]).toString();
//								if(cellName != null && cellName.compareTo(parentName) == 0){
//									parentExists = true;
//									cell = (DefaultGraphCell)graph.getRoots()[i];
//								}
//							}
//							if (!parentExists){
//								selectedNode.parentNode = 
//										new Node(
//												dataModel, 
//												parentName, 
//												selectedNode.x , 
//												selectedNode.y - 50,
//												selectedNode, 
//												null, 
//												false, 
//												selectedNode.depth + 1);
//								parentNode = selectedNode.parentNode;
//								parent[0] = createVertex(parentNode, parentNode.x , parentNode.y , 75 , 30, Color.ORANGE, false);
//							}else{
//								parentNode= ((Node)dataModel.nodes.get(parentName));
//								if (!parentNode.equals(selectedNode)){
//									if (parentNode.depth < selectedNode.depth + 1){ 
//										parentNode.depth = selectedNode.depth + 1;
//										adjustDepth(parentNode);
//									}
//									parentNode.childNodes.put(selectedNode.name ,selectedNode);
//									selectedNode.parentNode = parentNode; 
//								}
//								parent[0] = cell;
//							}
//							edge.setSource(parent[0].getChildAt(0)); edge.setTarget(selectedCell.getChildAt(0));
//							GraphConstants.setLineEnd(edge.getAttributes(), GraphConstants.ARROW_CLASSIC);
//							GraphConstants.setLineStyle(edge.getAttributes(), GraphConstants.STYLE_BEZIER);
//							GraphConstants.setEndFill(edge.getAttributes(), true);
//							GraphConstants.setSelectable(edge.getAttributes(), true);
//							parent[1] = edge;
//							graph.getGraphLayoutCache().insert(parent);
//							System.out.println(parentNode.y  + "  "+ selectedNode.y );
//						}
//					}
//					parentExists = false; selectedNode = null; parentNode = null; cell = null; selectedCell =null; parentName=null; 
//	    		}
//	    	}
//	    });
//	    pm.add(mi);
//		graph.add(pm);
//		
//		sp.add(graph);
//	}
//	
//	private static void adjustDepth(Node node){
//		if (node.parentNode != null){
//			Node parentnode = node.parentNode;
//			parentnode.depth = node.depth + 1;
//			adjustDepth(parentnode);
//		}
//	}
//	
//	/**
//	* This method is empty.
//	*/
//	public void setFocus() {}
}