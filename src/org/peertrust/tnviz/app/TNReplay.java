package org.peertrust.tnviz.app;

import java.awt.Color;
import java.util.Enumeration;
import java.util.Vector;

import org.peertrust.tnviz.gui.TNGui;

public class TNReplay extends Thread {
	
	private TNGui gui;
	private Graphics graphics;
	private Vector graphPath;
	private boolean running;
	
	public TNReplay(TNGui gui, Graphics graphics, Vector graphPath) {
		this.gui = gui;
		this.graphics = graphics;
		this.graphPath = graphPath;
		running = true;
	}
	
	public void run() {
		String id = "";
		Object current = null;
		Object previous = null;
		
		for (Enumeration en=graphPath.elements(); en.hasMoreElements() ; ) {
			if (!running) {
				break;
			}
			current = (Object)en.nextElement();
			if (current instanceof TNNode) {
				// Test if the node is invisible.
				if (((TNNode)current).isInvisible()) {
					// If the graphPath has more elements, then continue with the next iteration. 
					// Otherwise reset the color of the last node.
					if (en.hasMoreElements()) {
						continue;
					}
					else {
						if (previous != null && previous instanceof TNNode) {
							graphics.setNodeColor((TNNode)previous,graphics.getNodeBorderColor(),graphics.getNodeBackgroundColor());
						}
						else if (previous != null && previous instanceof TNEdge) {
							graphics.setEdgeColor((TNEdge)previous,graphics.getEdgeColor());
						}
					}
				}
				graphics.setNodeColor((TNNode)current,graphics.getNodeBorderColor(),Color.GREEN);
			}
			else if (current instanceof TNEdge) {
				// Test if the edge is invisible.
				if (((TNEdge)current).isInvisible()) {
					// If the graphPath has more elements, then continue with the next iteration. 
					// Otherwise reset the color of the last node.
					if (en.hasMoreElements()) {
						continue;
					}
					else {
						if (previous != null && previous instanceof TNNode) {
							graphics.setNodeColor((TNNode)previous,graphics.getNodeBorderColor(),graphics.getNodeBackgroundColor());
						}
						else if (previous != null && previous instanceof TNEdge) {
							graphics.setEdgeColor((TNEdge)previous,graphics.getEdgeColor());
						}
					}
				}
				graphics.setEdgeColor((TNEdge)current,Color.GREEN);
			}
			if (previous != null && previous instanceof TNNode) {
				graphics.setNodeColor((TNNode)previous,graphics.getNodeBorderColor(),graphics.getNodeBackgroundColor());
			}
			else if (previous != null && previous instanceof TNEdge) {
				graphics.setEdgeColor((TNEdge)previous,graphics.getEdgeColor());
			}
			graphics.refreshGraph();
			previous = current;
			try {
				Thread.sleep(400);
			} 
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		if (current != null && current instanceof TNNode) {
			graphics.setNodeColor((TNNode)current,graphics.getNodeBorderColor(),graphics.getNodeBackgroundColor());
		}
		else if (current != null && current instanceof TNEdge) {
			graphics.setEdgeColor((TNEdge)current,graphics.getEdgeColor());
		}
		
		gui.setReplayEnabled(true);
		graphics.refreshGraph();
	}
	
	public void startReplay() {
		this.start();
	}
	
	public void stopReplay() {
		running = false;
	}

}
