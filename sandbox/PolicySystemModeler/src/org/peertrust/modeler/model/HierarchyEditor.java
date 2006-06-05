package org.peertrust.modeler.model;

import java.awt.geom.Point2D;

import javax.swing.JFrame;

import org.jgraph.graph.DefaultGraphCell;
import org.peertrust.modeler.policysystem.gui.views.GraphEditor;


public class HierarchyEditor extends GraphEditor {

	private HierarchyEditor() {
		super();
	}

//	 Main Method
	public static void main(String[] args) {
		// Construct Frame
		JFrame frame = new JFrame("Hierarchy Editor");
		// Set Close Operation to Exit
		// frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// Add an Editor Panel
		frame.getContentPane().add(new HierarchyEditor());
		// Fetch URL to Icon Resource
//		URL jgraphUrl = GraphEd.class.getClassLoader().getResource(
//				"RESOURCE_BASE"+"jgraph.gif");
//		// If Valid URL
//		if (jgraphUrl != null) { 
//			// Load Icon
//			ImageIcon jgraphIcon = new ImageIcon(jgraphUrl);
//			// Use in Window
//			frame.setIconImage(jgraphIcon.getImage());
//		}
		// Set Default Size
		frame.setSize(520, 390);
		// Show Frame
		frame.setVisible(true);
	}
	
	//customized with user object point producer rdf res; set get
	//GraphModelListener will update
	//Insert a new Vertex at point
	public void insert(Point2D point) {
		// Construct Vertex with no Label
		DefaultGraphCell vertex = createDefaultGraphCell();
		// Create a Map that holds the attributes for the Vertex
		vertex.getAttributes().applyMap(createCellAttributes(point));
		// Insert the Vertex (including child port and attributes)
		graph.getGraphLayoutCache().insert(vertex);
	}
	
}
