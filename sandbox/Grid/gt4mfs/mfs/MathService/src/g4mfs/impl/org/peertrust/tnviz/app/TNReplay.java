/**
 * Copyright 2004
 * 
 * This file is part of Peertrust.
 * 
 * Peertrust is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * Peertrust is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Peertrust; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
*/
package g4mfs.impl.org.peertrust.tnviz.app;


import g4mfs.impl.org.peertrust.tnviz.gui.TNGui;

import java.awt.Color;
import java.util.Enumeration;
import java.util.Vector;


/**
 * <p>
 * 
 * </p><p>
 * $Id: TNReplay.java,v 1.1 2005/11/30 10:35:09 ionut_con Exp $
 * <br/>
 * Date: 10-Feb-2005
 * <br/>
 * Last changed: $Date: 2005/11/30 10:35:09 $
 * by $Author: ionut_con $
 * </p>
 * @author Sebastian Wittler and Michael Sch?fer
 */
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
