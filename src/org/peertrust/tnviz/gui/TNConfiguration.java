package org.peertrust.tnviz.gui;

import java.awt.Color;
import java.awt.Container;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;

import org.peertrust.tnviz.app.Graphics;


public class TNConfiguration extends JDialog implements ActionListener {
	
	private static final long serialVersionUID = 1L;
	private Frame owner;
	private Graphics graphics;
	private JComboBox nodeColor;
	private JComboBox edgeColor;
	private JCheckBox nodesEditable;
	private JCheckBox nodesMovable;
	private JCheckBox edgesEditable;
	private JCheckBox edgesMovable;

	public TNConfiguration(Frame owner, Graphics graphics) {
		super(owner,"Configuration",true);
		this.setSize(400,200);
		this.setLocation(100,100);
		
		this.owner = owner;
		this.graphics = graphics;
		
		Container contentPane = this.getContentPane();
		contentPane.setLayout(new GridLayout(5,2));
		
		JLabel label = new JLabel("Color of nodes:");
		contentPane.add(label);
		nodeColor = new JComboBox(new Object[]{"Blue", "Green", "Red", "Yellow"});
		nodeColor.setEditable(false);
		Color currentNodeColor = graphics.getNodeBackgroundColor();
		if (currentNodeColor.equals(Color.blue)) {
			nodeColor.setSelectedItem("Blue");
		}
		else if (currentNodeColor.equals(Color.green)) {
			nodeColor.setSelectedItem("Green");
		}
		else if (currentNodeColor.equals(Color.red)) {
			nodeColor.setSelectedItem("Red");
		}
		else if (currentNodeColor.equals(Color.yellow)) {
			nodeColor.setSelectedItem("Yellow");
		}
		contentPane.add(nodeColor);
		
		label = new JLabel("Color of edges:");
		contentPane.add(label);
		edgeColor = new JComboBox(new Object[]{"Black", "Blue", "Green", "Red", "Yellow"});
		edgeColor.setEditable(false);
		Color currentEdgeColor = graphics.getEdgeColor();
		if (currentEdgeColor.equals(Color.black)) {
			edgeColor.setSelectedItem("Black");
		}
		else if (currentEdgeColor.equals(Color.blue)) {
			edgeColor.setSelectedItem("Blue");
		}
		else if (currentEdgeColor.equals(Color.green)) {
			edgeColor.setSelectedItem("Green");
		}
		else if (currentEdgeColor.equals(Color.red)) {
			edgeColor.setSelectedItem("Red");
		}
		else if (currentEdgeColor.equals(Color.yellow)) {
			edgeColor.setSelectedItem("Yellow");
		}
		contentPane.add(edgeColor);
		
		nodesEditable = new JCheckBox("Nodes are not editable");
		nodesEditable.setSelected(!graphics.getNodeEditable());
		contentPane.add(nodesEditable);
		
		nodesMovable = new JCheckBox("Nodes are not movable");
		nodesMovable.setSelected(!graphics.getNodeMovable());
		contentPane.add(nodesMovable);
		
		edgesEditable = new JCheckBox("Edges are not editable");
		edgesEditable.setSelected(!graphics.getEdgeEditable());
		contentPane.add(edgesEditable);
		
		edgesMovable = new JCheckBox("Edges are not movable");
		edgesMovable.setSelected(!graphics.getEdgeMovable());
		contentPane.add(edgesMovable);
		
		JButton okButton = new JButton("OK");
		okButton.addActionListener(this);
		contentPane.add(okButton);
		
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(this);
		contentPane.add(cancelButton);
		
	}

	public void actionPerformed(ActionEvent e) {
		
		if (e.getActionCommand().equals("OK")) {
			
			String selectedNodeColor = nodeColor.getSelectedItem().toString();
			if (selectedNodeColor.equals("Blue")) {
				graphics.setNodeColor(Color.black,Color.blue);
			}
			else if (selectedNodeColor.equals("Green")) {
				graphics.setNodeColor(Color.black,Color.green);
			}
			else if (selectedNodeColor.equals("Red")) {
				graphics.setNodeColor(Color.black,Color.red);
			}
			else if (selectedNodeColor.equals("Yellow")) {
				graphics.setNodeColor(Color.black,Color.yellow);
			}
			
			String selectedEdgeColor = edgeColor.getSelectedItem().toString();
			if (selectedEdgeColor.equals("Black")) {
				graphics.setEdgeColor(Color.black);
			}
			else if (selectedEdgeColor.equals("Blue")) {
				graphics.setEdgeColor(Color.blue);
			}
			else if (selectedEdgeColor.equals("Green")) {
				graphics.setEdgeColor(Color.green);
			}
			else if (selectedEdgeColor.equals("Red")) {
				graphics.setEdgeColor(Color.red);
			}
			else if (selectedEdgeColor.equals("Yellow")) {
				graphics.setEdgeColor(Color.yellow);
			}
			
			graphics.setNodeEditable(!nodesEditable.isSelected());
			graphics.setNodeMovable(!nodesMovable.isSelected());
			graphics.setEdgeEditable(!edgesEditable.isSelected());
			graphics.setEdgeMovable(!edgesMovable.isSelected());
			
			graphics.refreshGraph();
			owner.repaint();
			
			this.dispose();
			
		}
		else if (e.getActionCommand().equals("Cancel")) {
			this.dispose();
		}
		
	}
	
}
