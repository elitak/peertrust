/**
 * 
 */
package org.peertrust.demo.client.applet;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.apache.log4j.Logger;
import org.jgraph.JGraph;
import org.peertrust.event.AnswerEvent;
import org.peertrust.event.EventDispatcher;
import org.peertrust.event.PTEvent;
import org.peertrust.event.PTEventListener;
import org.peertrust.event.QueryEvent;
import org.peertrust.net.Answer;
import org.peertrust.net.Peer;
import org.peertrust.net.Query;
import org.peertrust.tnviz.app.Graphics;
import org.peertrust.tnviz.app.TNEdge;
import org.peertrust.tnviz.app.TNGraphics;
import org.peertrust.tnviz.app.TNNode;
import org.peertrust.tnviz.app.TNSeqDiagramm;
import org.peertrust.tnviz.app.TNTreeDiagramm;
import org.peertrust.tnviz.app.TNVizListener;
import org.peertrust.tnviz.gui.TNGui;

/**
 * @author pat_dev
 *
 */
public class NegotiationVisualizationPane extends JPanel  implements PTEventListener {
		 
	private static Logger log = Logger.getLogger(NegotiationVisualizationPane.class);
	private Graphics graphics= new JPanelGuiBasedTNGraphics();
	//private boolean isVisible;
	private EventDispatcher _dispatcher;
	private Container container;
	
	/**
	 * @param isDoubleBuffered
	 */
	public NegotiationVisualizationPane(boolean isDoubleBuffered) {
		super(isDoubleBuffered);
		configGUI();
	}

	/**
	 * 
	 */
	public NegotiationVisualizationPane() {
		super();
		configGUI();
	}

	private void configGUI(){
		this.setLayout(new GridLayout(1,1));
		//this.setBackground(Color.BLACK);
		//this.add(graphics.getGraph());
		this.add(((JPanelGuiBasedTNGraphics)graphics).getJPanelTNGui());
		//((JPanelGuiBasedTNGraphics)graphics).getJPanelTNGui().setVisible(true);
		//((JPanelGuiBasedTNGraphics)graphics).refreshGraph();
		//setVisible(false);
		return;
	}
	/**
     * This method gets an event and processes it. Either new query or new
     * answer object are created according to the event information. Then this
     * information is forwarded to the graphics object which creates the
     * diagramms accordingly.
     * @param event The new event.
     */
    public void event(PTEvent event) {

        if (event instanceof QueryEvent) {
            Query query = ((QueryEvent) event).getQuery();
            log.debug("New query received from " + query.getSource().getAlias()
                    + ": " +
                    query.getGoal() + " - " + query.getReqQueryId() + " - "
                    + query.getTrace());
            graphics.addQuery(query);
            
        }
        else if (event instanceof AnswerEvent) {
            Answer answer = ((AnswerEvent) event).getAnswer();
            String message = "" ;
            switch(answer.getStatus())
			{
				case Answer.ANSWER:
					message += "New answer " ;
					break;
				case Answer.LAST_ANSWER:
					message += "New last answer " ;
					break;
				case Answer.FAILURE:
					message += "New failure " ;
					break;
				default:
					message += "New unknown answer " ;
			}
            log.debug(message + "received from "
                    + answer.getSource().getAlias() + ": " +
                    answer.getGoal() + " - " + answer.getReqQueryId() + " - "
                    + answer.getTrace());
            graphics.addAnswer(answer);
        }
        graphics.updateGraph();
    }

    
//    /**
//     * Returns the EventDispatcher.
//     * @return The EventDispatcher.
//     */
//    public EventDispatcher getEventDispatcher() {
//        return _dispatcher;
//    }
//
//    /**
//     * Sets the EventDispatcher to the given object.
//     * @param dispatcher The new EventDispatcher.
//     */
//    public void setEventDispatcher(EventDispatcher dispatcher) {
//        this._dispatcher = dispatcher;
//    }
    
/**
	 * @return Returns the container.
	 */
	public Container getContainer() {
		return container;
	}

	/**
	 * @param container The container to set.
	 */
	public void setContainer(Container container) {
		this.container = container;
	}

	//    public void startListenToPTEvent(EventDispatcher ed){
//    	ed.register(this,PTEvent.class);
//    }
//    
//    public void stopListenToPTEvent(EventDispatcher ed){
//    	ed.unregister(this);
//    }
//    
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		JFrame frame= new JFrame();
		frame.setSize(500,800);
		final NegotiationVisualizationPane v=
			new NegotiationVisualizationPane();
		Container c=frame.getContentPane();
		c.setLayout(new BorderLayout());
		//////////////////////////////////////////////
		JPanel bPanel= new JPanel();
		c.add(bPanel,BorderLayout.WEST);
		bPanel.setLayout(new GridLayout(3,1));
		ActionListener toggleActionListener=
			new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					v.toggleVisualization();
				}
			
			};
		JButton toggleButton= new JButton("toggle VIZ");
		toggleButton.addActionListener(toggleActionListener);
		bPanel.add(toggleButton);
		//////////////////////////////////////////////
		JPanel container= new JPanel();
		container.setLayout(new GridLayout(1,1));
		c.add(container,BorderLayout.CENTER);
		v.setContainer(container);
		
		//v.toggleVisualization();
		final Peer elearnPeer= new Peer("elearn","127.0.0.1",7703);
		final Peer alicePeer= new Peer("alice","127.0.0.1",7704);
		//frame.getContentPane Query(goal,origin,target,reqId,trace)
		final Query query=new Query("ieeeMember(alice)",elearnPeer,alicePeer,0,null);
		
		JButton addQButton= new JButton("addQuery");
		ActionListener addQActionListener=
			new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					PTEvent event= new QueryEvent(elearnPeer,query);
					v.event(event);
					//v.getJPanelGuiBasedTNGraphics().addQuery(query);
					//v.graphics.setLayout(Graphics.SEQ_LAYOUT);
					//v.graphics.refreshGraph();
					v.graphics.refreshGraph();
					
					System.out.println("setLayout:"+v.graphics.getLayout()+" seq:"+Graphics.SEQ_LAYOUT+" tree:"+Graphics.TREE_LAYOUT);;
				}
			
			};
		addQButton.addActionListener(addQActionListener);
		bPanel.add(addQButton);
		
		//v.getJPanelGuiBasedTNGraphics().addQuery(query);
		frame.setVisible(true);
		frame.validate();
		frame.repaint();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	/**
	 * @return Returns the graphics.
	 */
	public JPanelGuiBasedTNGraphics getJPanelGuiBasedTNGraphics() {		
		return (JPanelGuiBasedTNGraphics)graphics;
	}

//	/**
//	 * @param graphics The graphics to set.
//	 */
//	public void setGraphics(Graphics graphics) {
//		this.graphics = graphics;
//	}
	
	//////////////////////////////
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
    }
    
    private void startListenToPTEvent(){
    	System.out.println("startListenToPTEvent");
    	if(_dispatcher!=null){
    		_dispatcher.register(this,PTEvent.class);
    	}
    	TNGui gui=graphics.getGui();
    	//gui.setVisible(isVisible);
    	container.removeAll();
    	container.add(this);
    	setVisible(true);
    	((JPanelGuiBasedTNGraphics)graphics).getJPanelTNGui().setBackground(Color.black);//Visible(true);
    	
    	container.repaint();//validate();
    	((JPanelGuiBasedTNGraphics)graphics).refreshGraph();
    	System.out.println("root:"+((JPanelGuiBasedTNGraphics)graphics).getRoot());
    }
    
    private void stopListenToPTEvent(){
    	System.out.println("stopListenToPTEvent");
    	if(_dispatcher!=null){
    		_dispatcher.unregister(this);
    	}
    	TNGui gui=graphics.getGui();
    	container.removeAll();
    	setVisible(false);
    	System.out.println("isVisible:"+isVisible());
    	container.repaint();//validate();
    	//((JPanelGuiBasedTNGraphics)graphics).refreshGraph();
    }
    
	public void toggleVisualization(){
		if(isVisible()){
			stopListenToPTEvent();
		}else{
			startListenToPTEvent();
		}
	}

}
