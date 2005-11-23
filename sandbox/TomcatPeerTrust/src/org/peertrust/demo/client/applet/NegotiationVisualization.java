
package org.peertrust.demo.client.applet;



import javax.swing.JFrame;

import org.apache.log4j.Logger;
import org.peertrust.event.AnswerEvent;
import org.peertrust.event.EventDispatcher;
import org.peertrust.event.PTEvent;
import org.peertrust.event.PTEventListener;
import org.peertrust.event.QueryEvent;
import org.peertrust.net.Answer;
import org.peertrust.net.Query;
import org.peertrust.tnviz.app.Graphics;
import org.peertrust.tnviz.app.TNGraphics;
import org.peertrust.tnviz.gui.TNGui;

/**
 * A Wrapper for the TNGui for easy intergration in the demo applet.
 * @author Congo Patrice (token77)
 *
 */
public class NegotiationVisualization  implements PTEventListener {
	
	/** logger*/
	private static Logger log = Logger.getLogger(NegotiationVisualizationPane.class);
	
	/** visualization graphics object*/
	private Graphics graphics;//= new TNGraphics();
	
	/** peertrust client event dispatcher*/
	private EventDispatcher _dispatcher;
	
	/**
	 * Creates and show a new NegotiationVisualization
	 */
	public NegotiationVisualization() {		
		configGUI();
	}

	/**
	 * configure the grapghics object and set its gui visible 
	 */
	private void configGUI(){
		graphics= new TNGraphics();
		TNGui gui=graphics.getGui();
		//isVisible=true;
		gui.setVisible(true);
		//gui.doLayout();
		gui.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		
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
        if(graphics.getGui().isVisible()){
        	dispatcher.register(this);
        }
    }
    
    /**
     * connect this NegotiationVisualization to the
     * event dispatcher and set it visible
     */
    private void startListenToPTEvent(){
    	_dispatcher.register(this,PTEvent.class);
    	TNGui gui=graphics.getGui();
    	gui.setVisible(true);
    }
    
    /**
     * disconnect this NegotiationVisualization to the event 
     * dispatcher and hide the visualization windows
     */
    private void stopListenToPTEvent(){
    	_dispatcher.unregister(this);
    	TNGui gui=graphics.getGui();
    	//isVisible=false;
    	gui.setVisible(false);
    	
    	gui.validate();
    }
    
    /**
     * Toggle to visualization window visibility
     *
     */
	public void toggleVisualization(){
		//System.out.println("isVisible:"+isVisible+ " panelIsv:"+graphics.getGui().isVisible());
		if(graphics.getGui().isVisible()){
			stopListenToPTEvent();
		}else{
			startListenToPTEvent();
		}
	}

	/**
	 * @return Returns the graphics.
	 */
	public JPanelGuiBasedTNGraphics getJPanelGuiBasedTNGraphics() {		
		return (JPanelGuiBasedTNGraphics)graphics;
	}
	

}
