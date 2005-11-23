/**
 * 
 */
package org.peertrust.demo.client.applet;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;


//import org.apache.log4j.Logger;
import org.jgraph.JGraph;
import org.peertrust.event.EventDispatcher;
import org.peertrust.event.PTEvent;
import org.peertrust.event.PTEventListener;

/**
 * @author Patrice Congo (token77)
 */
public class TestNegotiationVisualizationPane 
									extends JPanel  
									implements PTEventListener 
{
	/** the minimal dimension of the visualization pane*/
	private static final Dimension MIN_DIM= new Dimension(800,1600);
	
	/** the peertrust client event dispatcher*/
	private EventDispatcher _dispatcher;
	
	/** the visibility state of the vizualisation pane*/
	private boolean isVisible;
	
	/** A sequemce diagramm implementaion which does depend on Graphics*/
	private TestSequenceDiagramm seqDiagramm= new TestSequenceDiagramm();
	
	/**A tree diagramm implementation which does not depends on Graphics*/
	private TestTreeDiagramm treeDiagramm= new TestTreeDiagramm();
	
	/** holds the diagramm which is curently showing.*/
	private Object showingDiagramm;
	
	/** The container pane for the diagramm*/
	private JPanel diaPane;
	
	/** the parent container where the diagramm is to be displayed*/
	private Container parentContainer;
	
	/**
	 * Creates a new TestNegotiationVisualizationPane and makes its layout.
	 */
	public TestNegotiationVisualizationPane() {
		configGUI();
	}
	/**
	 * Makes the layout of the visualization pane.
	 *
	 */
	private void configGUI(){
		isVisible=false;
		this.setLayout(new BorderLayout());
		//command buttons
		this.add(makeCmdPanel(),BorderLayout.NORTH);
		
		JScrollPane _diaPane= 
			new JScrollPane(	
					JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,		
					JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		//diagramm and show sequence diagramm 
		diaPane= new JPanel();
		_diaPane.setViewportView(diaPane);
		this.add(_diaPane,BorderLayout.CENTER);
		Dimension minDim= new Dimension(800,1600);
		seqDiagramm.getGraph().setMinimumSize(minDim);
		seqDiagramm.getGraph().setPreferredSize(minDim);
		treeDiagramm.getGraph().setMinimumSize(minDim);
		seqDiagramm.getGraph().setPreferredSize(minDim);//getToolkit().getScreenSize()
		showSeqDiagramm();
		return;
	}
	/** 
	 * make the command panel. It hold buttons to change the
	 * showing diagramm or wipe it.
	 * @return a panel containing the control buttons
	 */
	private JPanel makeCmdPanel(){
		JPanel panel= new JPanel();
		panel.setLayout(new GridLayout(1,3));
		panel.add(showSeqButton());
		panel.add(showTreeDiagrammButton());
		panel.add(wipeDiagrammButton());
		return panel;
	}
	/** 
	 * Create a button, which, when it is klicked, shows the
	 * sequence diagramm. 
	 * @return the button to show the sequence diagramm
	 */
	private JButton showSeqButton(){
		JButton b= new JButton("Show Seq Graph");
		ActionListener al= new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				showSeqDiagramm();
			}
			
		};
		b.addActionListener(al);
		return b;
	}
	/**
	 * Creates a button, which showsthe tree diagramm, when it klicked.
	 * @return the button to show the tree diagramm
	 */
	JButton showTreeDiagrammButton(){
		JButton b= new JButton("Show Tree Graph");
		ActionListener al= new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				showTreeDiagramm();
			}
			
		};
		b.addActionListener(al);
		return b;
	}
	/**
	 * Creates a button, which wipe the diagramms when it is klicked
	 * @return the button to wipe the diagramms
	 */
	private JButton wipeDiagrammButton(){
		JButton b= new JButton("Wipe Graph");
		ActionListener al= new ActionListener(){

			public void actionPerformed(ActionEvent e) {		
				
					treeDiagramm.wipeGraph();				
					treeDiagramm.getGraph().setMinimumSize(MIN_DIM);
					treeDiagramm.refreshGraph();
				
					seqDiagramm.wipeGraph();				
					seqDiagramm.getGraph().setMinimumSize(MIN_DIM);
					seqDiagramm.refreshGraph();
					diaPane.removeAll();
					JGraph graph;
					//because graoh still shows  
					if(showingDiagramm instanceof TestTreeDiagramm){
						graph= ((TestTreeDiagramm)showingDiagramm).getGraph();
						diaPane.add(graph);
					}else if(showingDiagramm instanceof TestSequenceDiagramm){
						graph= ((TestSequenceDiagramm)showingDiagramm).getGraph();
						diaPane.add(graph);
					}
					invalidate();
					parentContainer.getParent().validate();
			}
			
		};
		b.addActionListener(al);
		return b;
	}
	
	/**
	 * shows the sequence diagramm 
	 */
	private void showSeqDiagramm(){
		if(showingDiagramm!=seqDiagramm){
			diaPane.removeAll();
			//diaPane.setViewportView(seqDiagramm.getGraph());
			diaPane.add(seqDiagramm.getGraph());
			diaPane.setBackground(Color.LIGHT_GRAY);
			diaPane.invalidate();
			//this.repaint(500);
			if(parentContainer!=null){
				this.invalidate();
				parentContainer.getParent().validate();
			}
			showingDiagramm=seqDiagramm;
		}
	}
	
	/**
	 * Shows the tree diagramm
	 *
	 */
	private void showTreeDiagramm(){
		if(showingDiagramm!=treeDiagramm){
			diaPane.removeAll();
			diaPane.add(treeDiagramm.getGraph());
			diaPane.setBackground(Color.BLUE);
			diaPane.invalidate();
			if(parentContainer!=null){
				this.invalidate();
				parentContainer.getParent().validate();
			}
			
			showingDiagramm=treeDiagramm;
		}
	}
	
	/**
     * This method gets an event and processes it. Either new query or new
     * answer object are created according to the event information. Then this
     * information is forwarded to the graphics object which creates the
     * diagramms accordingly.
     * @param event The new event.
     */
    public void event(PTEvent event) {
    	treeDiagramm.ptListener.event(event);
    	seqDiagramm.ptListener.event(event);
    }

    /**
     * Returns the EventDispatcher.
     * @return The EventDispatcher.
     */
    public EventDispatcher getEventDispatcher() {
        return _dispatcher;
    }

    /**
     * Sets the EventDispatcher to the given object.
     * @param dispatcher The new EventDispatcher.
     */
    public void setEventDispatcher(EventDispatcher dispatcher) {
        this._dispatcher = dispatcher;
        if(isVisible){
        	dispatcher.register(this,PTEvent.class);
        }
    }
    
    /**
     * Registers the TestNegotiationVisualization to receives
     * peertrust events and sets it visible.
     */
    private void startListenToPTEvent(){
    	if(_dispatcher!=null){
    		_dispatcher.register(this,PTEvent.class);
    	}
    	isVisible=true;
    	parentContainer.removeAll();
		parentContainer.add(this);
		this.setVisible(true);
		this.validate();
		//parentContainer.invalidate();
		parentContainer.validate();
		//parentContainer.getParent().validate();//doLayout();//repaint(500);
		parentContainer.repaint(
						parentContainer.getX(),
						parentContainer.getY(),
						parentContainer.getWidth(),
						parentContainer.getHeight());
    }
    /**
     * Unregisters this TestNegotiationVisualizationPane as listener
     * for peertrust event and hides it.
     *
     */
    private void stopListenToPTEvent(){
    	if(_dispatcher!=null){
    		_dispatcher.unregister(this);
    	}
    	isVisible=false;
    	parentContainer.removeAll();
    	this.invalidate();
    	parentContainer.invalidate();
    	parentContainer.repaint(
				parentContainer.getX(),
				parentContainer.getY(),
				parentContainer.getWidth(),
				parentContainer.getHeight());
    	
    }
  
    /**
     * Toggle the visibility of the visualization pane.
     */
	public void toggleVisualization(){
		if(isVisible){
			stopListenToPTEvent();
		}else{
			startListenToPTEvent();
		}
	}

	
	/**
	 * @return Returns the parentContainer.
	 */
	public Container getParentContainer() {
		return parentContainer;
	}

	/**
	 * @param parentContainer The parentContainer to set.
	 */
	public void setParentContainer(Container parentContainer) {
		this.parentContainer = parentContainer;
	}
	/**
	 * @return true if the visualization is visible; false if not.
	 */
	public  boolean getIsDisplayed(){
		return isVisible;
	}
	
	static public void main(String[] args){
		JFrame frame= new JFrame();
		frame.setSize(500,800);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		TestNegotiationVisualizationPane vp=
					new TestNegotiationVisualizationPane();
		vp.setParentContainer(frame.getContentPane());
		
		//Container c=frame.getRootPane();
		//c.setLayout(new GridLayout(1,1));
		
		frame.setVisible(true);
		vp.toggleVisualization();
		
		
	}

	
}
