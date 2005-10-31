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


import org.apache.log4j.Logger;
import org.jgraph.JGraph;
import org.peertrust.event.EventDispatcher;
import org.peertrust.event.PTEvent;
import org.peertrust.event.PTEventListener;

/**
 * @author pat_dev
 *
 */
public class TestNegotiationVisualizationPane extends JPanel  implements PTEventListener {
	
	private static final Dimension MIN_DIM= new Dimension(800,1600);
	private static Logger log = Logger.getLogger(NegotiationVisualizationPane.class);
	private EventDispatcher _dispatcher;
	private boolean isVisible;
	private TestSequenceDiagramm seqDiagramm= new TestSequenceDiagramm();
	private TestTreeDiagramm treeDiagramm= new TestTreeDiagramm();
	
	private Object showingDiagramm;
	//private JScrollPane diaPane;
	private JPanel diaPane;
	private Container parentContainer;
	
	/**
	 * 
	 */
	public TestNegotiationVisualizationPane() {
		
		configGUI();
	}

	private void configGUI(){
		isVisible=false;
		this.setLayout(new BorderLayout());
		this.add(makeCmdPanel(),BorderLayout.NORTH);
		
		JScrollPane _diaPane= 
			new JScrollPane(	
					JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,		
					JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		diaPane= new JPanel();
		//diaPane.setPreferredSize(new Dimension(800,800));
		_diaPane.setViewportView(diaPane);
		//diaPane.setLayout(new GridLayout(1,1));
//		JPanel p= new JPanel();
//		p.add(diaPane);
		this.add(_diaPane,BorderLayout.CENTER);
		
		/////
		Dimension minDim= new Dimension(800,1600);
		seqDiagramm.getGraph().setMinimumSize(minDim);
		seqDiagramm.getGraph().setPreferredSize(minDim);
		treeDiagramm.getGraph().setMinimumSize(minDim);
		seqDiagramm.getGraph().setPreferredSize(minDim);//getToolkit().getScreenSize()
		////
		showSeqDiagramm();
		return;
	}
	
	JPanel makeCmdPanel(){
		JPanel panel= new JPanel();
		panel.setLayout(new GridLayout(1,3));
		panel.add(showSeqButton());
		panel.add(showTreeDiagrammButton());
		panel.add(wipeDiagrammButton());
		return panel;
	}
	
	JButton showSeqButton(){
		JButton b= new JButton("Show Seq Graph");
		ActionListener al= new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				showSeqDiagramm();
			}
			
		};
		b.addActionListener(al);
		return b;
	}
	
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
//					parentContainer.invalidate();
//					parentContainer.doLayout();
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
	
	private void showSeqDiagramm(){
		if(showingDiagramm!=seqDiagramm){
			diaPane.removeAll();
			//diaPane.setViewportView(seqDiagramm.getGraph());
			diaPane.add(seqDiagramm.getGraph());
			diaPane.setBackground(Color.LIGHT_GRAY);
			diaPane.invalidate();
			//this.repaint(500);
			if(parentContainer!=null){
//				//parentContainer.repaint(500);
//				treeDiagramm.getGraph().setPreferredSize(parentContainer.getPreferredSize());
//				parentContainer.validate();
				this.invalidate();
				parentContainer.getParent().validate();
			}
			showingDiagramm=seqDiagramm;
		}
	}
	
	private void showTreeDiagramm(){
		if(showingDiagramm!=treeDiagramm){
			diaPane.removeAll();
			//seqDiagramm.getGraph().setPreferredSize(new Dimension(800,800));
			//diaPane.getViewport().setPreferredSize(new Dimension(800,800));
			//diaPane.setViewportView(seqDiagramm.getGraph());
			diaPane.add(treeDiagramm.getGraph());
			diaPane.setBackground(Color.BLUE);
			diaPane.invalidate();
			//parentContainer.validate();
			//this.repaint(500);
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
    
    private void stopListenToPTEvent(){
    	if(_dispatcher!=null){
    		_dispatcher.unregister(this);
    	}
    	isVisible=false;
    	parentContainer.removeAll();
    	//parentContainer.invalidate();
    	//this.setVisible(isVisible);
    	this.invalidate();
    	parentContainer.invalidate();
//    	parentContainer.validate();
//    	parentContainer.getParent().validate();
    	parentContainer.repaint(
				parentContainer.getX(),
				parentContainer.getY(),
				parentContainer.getWidth(),
				parentContainer.getHeight());
    	
    }
    
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
