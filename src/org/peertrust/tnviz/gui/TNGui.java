
package org.peertrust.tnviz.gui;

import java.awt.Cursor;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ButtonGroup;
import javax.swing.JRadioButtonMenuItem;

import org.jgraph.graph.DefaultGraphCell;
import org.peertrust.tnviz.app.Graphics;
import org.peertrust.tnviz.app.TNEdge;
import org.peertrust.tnviz.app.TNNode;


public class TNGui extends JFrame implements MouseListener, KeyListener, ActionListener {
	
	private static final long serialVersionUID = 1L;
	private Graphics graphics;
	private JScrollPane pane;
	private JPopupMenu popupMenu;
	private TNNode selectedNode;
	private JTextArea informationArea;
	private JMenuItem expandItem; 
	private JMenuItem collapseItem;
	private JMenuItem startReplayItem;
	private JMenuItem stopReplayItem;
	private boolean replayEnabled;
	private boolean collapseEnabled;
	
	public TNGui() {
		super("Trust Negotiation Vizualization");
		selectedNode = null;
		
		GridBagLayout gbl = new GridBagLayout();
		this.getContentPane().setLayout(gbl);
		
		/*
		 * Main ScrollPane
		 */
	    pane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
	    
	    // Layout for the ScrollPane.
	    GridBagConstraints gbcScrollPane = new GridBagConstraints();
	    gbcScrollPane.gridx = 0;
	    gbcScrollPane.gridy = 0;
	    gbcScrollPane.gridwidth = 2;
	    gbcScrollPane.gridheight = 2;
	    gbcScrollPane.weightx = 100;
	    gbcScrollPane.weighty = 100;
	    gbcScrollPane.fill = GridBagConstraints.BOTH;
	    gbcScrollPane.ipadx = 200;
	    gbcScrollPane.anchor = GridBagConstraints.NORTHWEST;
	    gbl.setConstraints(pane,gbcScrollPane);
	    
	    this.getContentPane().add(pane);
	    
	    /*
	     * Information panel.
	     */
	    JPanel emptyPanel = new JPanel();
	    emptyPanel.setVisible(true);
	    
	    // Layout for the information panel.
	    GridBagConstraints gbcPanel = new GridBagConstraints();
		gbcPanel.gridx = 0;
		gbcPanel.gridy = 2;
		gbcPanel.gridwidth = 2;
		gbcPanel.gridheight = 1;
		gbcPanel.weightx = 0;
		gbcPanel.weighty = 0;
		gbcPanel.fill = GridBagConstraints.HORIZONTAL;
		gbcPanel.ipady = 200;
		gbcPanel.anchor = GridBagConstraints.NORTHWEST;
	    
	    gbl.setConstraints(emptyPanel,gbcPanel);
	    this.getContentPane().add(emptyPanel);
	    
	    // Layout for the text area.
	    emptyPanel.setLayout(null);
	    
	    informationArea = new JTextArea("");
	    informationArea.setEditable(false);
	    informationArea.setBounds(new Rectangle((int)getToolkit().getScreenSize().getWidth(),200));
	    
	    emptyPanel.add(informationArea);
	    
	    /*
	     * Main menu.
	     */
	    JMenuBar menuBar = new JMenuBar();
	    
	    JMenu fileMenu = new JMenu("File");
	    JMenuItem exitItem = new JMenuItem("Exit",'E');
	    exitItem.addActionListener(this);
	    fileMenu.add(exitItem);
	    menuBar.add(fileMenu);
	    
	    JMenu optionsMenu = new JMenu("Options");
	    JMenuItem preferencesItem = new JMenuItem("Preferences",'P');
	    preferencesItem.addActionListener(this);
	    optionsMenu.add(preferencesItem);
	    JMenu layoutMenu = new JMenu("Layout");
	    ButtonGroup layoutSelect = new ButtonGroup();
	    JMenuItem treeLayoutItem = new JRadioButtonMenuItem("Tree Layout",true);
	    treeLayoutItem.addActionListener(this);
	    layoutSelect.add(treeLayoutItem);
	    layoutMenu.add(treeLayoutItem);
	    JMenuItem seqLayoutItem = new JRadioButtonMenuItem("Sequence Layout");
	    seqLayoutItem.addActionListener(this);
	    layoutSelect.add(seqLayoutItem);
	    layoutMenu.add(seqLayoutItem);
	    optionsMenu.add(layoutMenu);
	    menuBar.add(optionsMenu);
	    
	    this.setJMenuBar(menuBar);
	    
	    /*
	     * Popup menu.
	     */
	    popupMenu = new JPopupMenu();
	    expandItem = new JMenuItem("Expand",'E');
	    expandItem.addActionListener(this);
	    popupMenu.add(expandItem);
	    collapseItem = new JMenuItem("Collapse",'C');
	    collapseItem.addActionListener(this);
	    popupMenu.add(collapseItem);
	    startReplayItem = new JMenuItem("Start Replay",'R');
	    startReplayItem.addActionListener(this);
	    popupMenu.add(startReplayItem);
	    stopReplayItem = new JMenuItem("Stop Replay",'S');
	    stopReplayItem.addActionListener(this);
	    stopReplayItem.setEnabled(false);
	    popupMenu.add(stopReplayItem);
	    
	    setCollapseEnabled(false);
	    
	    /*
	     * Frame.
	     */
	    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    this.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
	    this.setSize(getToolkit().getScreenSize());
	    this.setExtendedState(JFrame.MAXIMIZED_BOTH);
	    this.setVisible(true);
	}
	
	public void setGraphics(Graphics graphics) {
		this.graphics = graphics;
		graphics.getGraph().addMouseListener(this);
		graphics.getGraph().addKeyListener(this);
		graphics.getGraph().add(popupMenu);
		pane.setViewportView(graphics.getGraph());
		pane.repaint();
	}
	
	public void mousePressed(MouseEvent e) { 
		
		Point2D point = graphics.getGraph().fromScreen(new Point(e.getPoint()));
		DefaultGraphCell node = (DefaultGraphCell)graphics.getGraph().getFirstCellForLocation(point.getX(),point.getY());
		
		if (e.isPopupTrigger()) {
			// Show the popup menu.
			if (!(node instanceof TNEdge) && (node instanceof TNNode)) {
				selectedNode = (TNNode)node;
				popupMenu.show(e.getComponent(),e.getX(),e.getY());
			}
		}
		
		// Show the object information.
		if ((node instanceof TNEdge) && (node != null)) {
			TNEdge clickedEdge = (TNEdge)node;
			String informationText = 	"Edge Information: \n" +
								"Label: "+clickedEdge+"\n"+
								"ID: "+clickedEdge.getId()+"\n"+
								"Goal: "+clickedEdge.getGoal()+"\n"+
								"ReqQueryId: "+clickedEdge.getReqQueryId()+"\n";
			if (clickedEdge.isAnswer()) {
				informationText += 	"Type: Answer\n"+
									"Status: "+clickedEdge.getStatus()+"\n"+
									"Proof: "+clickedEdge.getProof();
			}
			else if (clickedEdge.isQuery()) {
				informationText += "Type: Query";
			}
			informationArea.setText(informationText);
			this.repaint();
		}
		else if ((node instanceof TNNode) && (node != null)) {
			TNNode clickedNode = (TNNode)node;
			informationArea.setText(
					"Node Information: \n" +
					"Title: "+clickedNode.getTitle()+"\n"+
					"ID: "+clickedNode.getId()+"\n"+
					"Peer Address: "+clickedNode.getPeerAddress()+"\n"+
					"Peer Alias: "+clickedNode.getPeerAlias()+"\n"+
					"Peer Port: "+clickedNode.getPeerPort());
			this.repaint();
		}
		else {
			informationArea.setText("");
			this.repaint();
		}
		
		
		
	}
	
	public void mouseExited(MouseEvent e) {}
	
	public void mouseEntered(MouseEvent e) {}
	
	public void mouseClicked(MouseEvent e) {}
	
	public void mouseReleased(MouseEvent e) {
		
		if (e.isPopupTrigger()) {
			Point2D point = graphics.getGraph().fromScreen(new Point(e.getPoint()));
			DefaultGraphCell node = (DefaultGraphCell)graphics.getGraph().getFirstCellForLocation(point.getX(),point.getY());
			if (!(node instanceof TNEdge) && (node instanceof TNNode)) {
				selectedNode = (TNNode)node;
				popupMenu.show(e.getComponent(),e.getX(),e.getY());
			}
		}
		
	}
	
	public void keyPressed(KeyEvent e) {
		/*
		if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			TNNode node = graphics.createNode("Node Neu");
			graphics.positionNode(node,100,100);
		}
		*/
	}
	
	public void keyReleased(KeyEvent e) {}
	
	public void keyTyped(KeyEvent e) {}
	
	public void actionPerformed(ActionEvent e) {
		
		if (e.getActionCommand().equals("Exit")) {
			System.exit(0);
		}
		else if (e.getActionCommand().equals("Preferences")) {
			TNConfiguration conf = new TNConfiguration(this,graphics);
			conf.show();
		}
		else if (e.getActionCommand().equals("Expand")) {
			if (selectedNode != null) {
				graphics.expand(selectedNode);
			}
		}
		else if (e.getActionCommand().equals("Collapse")) {
			if (selectedNode != null) {
				graphics.collapse(selectedNode);
			}
		}
		else if (e.getActionCommand().equals("Start Replay")) {
			startReplayItem.setEnabled(false);
			stopReplayItem.setEnabled(true);
			graphics.startReplayGraphPath();
		}
		else if (e.getActionCommand().equals("Stop Replay")) {
			startReplayItem.setEnabled(true);
			stopReplayItem.setEnabled(false);
			graphics.stopReplayGraphPath();
		}
		else if (e.getActionCommand().equals("Tree Layout")) {
			graphics.setLayout(Graphics.TREE_LAYOUT);
			setGraphics(graphics);
			setCollapseEnabled(true);
		}
		else if (e.getActionCommand().equals("Sequence Layout")) {
			graphics.setLayout(Graphics.SEQ_LAYOUT);
			setGraphics(graphics);
			setCollapseEnabled(false);
		}
		
	}
	
	public void setReplayEnabled(boolean enabled) {
		replayEnabled = enabled;
		if (enabled) {
			startReplayItem.setEnabled(true);
			stopReplayItem.setEnabled(false);
		}
		else {
			startReplayItem.setEnabled(false);
			stopReplayItem.setEnabled(true);
		}
	}
	
	public int getPaneHeight() {
		return pane.getViewport().getHeight();
	}

	public int getPaneWidth() {
		return pane.getViewport().getWidth();
	}
	
	public boolean isCollapseEnabled() {
		return collapseEnabled;
	}
	
	public void setCollapseEnabled(boolean collapseEnabled) {
		this.collapseEnabled = collapseEnabled;
		if (collapseEnabled) {
			expandItem.setEnabled(true);
			collapseItem.setEnabled(true);
		}
		else {
			expandItem.setEnabled(false);
			collapseItem.setEnabled(false);
		}
	}
}
