//package org.peertrust.demo.client.applet;
//
//public class JPanelGuiBasedTNReplay {
//
//}

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
package org.peertrust.demo.client.applet;

import java.awt.Color;
import java.util.Enumeration;
import java.util.Vector;


import org.peertrust.tnviz.app.Graphics;
import org.peertrust.tnviz.app.TNEdge;
import org.peertrust.tnviz.app.TNNode;

/**
 * <p>
 * The TNReplay class provides the means to visualize the sequence in which a
 * graph was built. When activated, the class reads out the data from the given
 * diagramm and highlights each node and edge one after another in the correct
 * order in which they were created.
 * </p><p>
 * $Id: JPanelGuiBasedTNReplay.java,v 1.2 2005/10/31 00:07:37 token77 Exp $
 * <br/>
 * Date: 10-Feb-2005
 * <br/>
 * Last changed: $Date: 2005/10/31 00:07:37 $
 * by $Author: token77 $
 * </p>
 * @author Michael Schaefer and Sebastian Wittler
 */
public class JPanelGuiBasedTNReplay extends Thread {

    private JPanelTNGui gui;
    private Graphics graphics;
    private Vector graphPath;
    private boolean running;

    /**
     * Constructor for this class. In order to wirk correctly, the gui object,
     * the graphics object and the graphPath are needed. The graphPath is 
     * a vector with the graph elements in the order in which they have been
     * created.
     * @param gui The gui object.
     * @param graphics The graphics object.
     * @param graphPath The graphPath with the graph elements.
     */
    public JPanelGuiBasedTNReplay(JPanelTNGui gui, JPanelGuiBasedTNGraphics graphics, Vector graphPath) {
        this.gui = gui;
        this.graphics = graphics;
        this.graphPath = graphPath;
        running = true;
    }

    /**
     * This method reads out the data from the graphPath and then highlights
     * the graph elements on after another in the order in which they were
     * created. Each node and edge will be highlighted by changing its color
     * for 400 milliseconds. Only nodes which are not invisible will be
     * highlighted. Invisible nodes will be ignored.
     */
    public void run() {
        String id = "";
        Object current = null;
        Object previous = null;
        for (Enumeration en = graphPath.elements(); en.hasMoreElements();) {
            if (!running) {
                break;
            }
            current = (Object) en.nextElement();
            if (current instanceof TNNode) {
                // Test if the node is invisible.
                if (((TNNode) current).isInvisible()) {
                    // If the graphPath has more elements, then continue with
                    // the next iteration.
                    // Otherwise reset the color of the last node.
                    if (en.hasMoreElements()) {
                        continue;
                    }
                    else {
                        if (previous != null && previous instanceof TNNode) {
                            graphics.setNodeColor((TNNode) previous, graphics
                                    .getNodeBorderColor(), graphics
                                    .getNodeBackgroundColor());
                        }
                        else if (previous != null && previous instanceof TNEdge) {
                            graphics.setEdgeColor((TNEdge) previous, graphics
                                    .getEdgeColor());
                        }
                    }
                }
                graphics.setNodeColor((TNNode) current, graphics
                        .getNodeBorderColor(), Color.GREEN);
            }
            else if (current instanceof TNEdge) {
                // Test if the edge is invisible.
                if (((TNEdge) current).isInvisible()) {
                    // If the graphPath has more elements, then continue with
                    // the next iteration.
                    // Otherwise reset the color of the last node.
                    if (en.hasMoreElements()) {
                        continue;
                    }
                    else {
                        if (previous != null && previous instanceof TNNode) {
                            graphics.setNodeColor((TNNode) previous, graphics
                                    .getNodeBorderColor(), graphics
                                    .getNodeBackgroundColor());
                        }
                        else if (previous != null && previous instanceof TNEdge) {
                            graphics.setEdgeColor((TNEdge) previous, graphics
                                    .getEdgeColor());
                        }
                    }
                }
                graphics.setEdgeColor((TNEdge) current, Color.GREEN);
            }
            if (previous != null && previous instanceof TNNode) {
                graphics.setNodeColor((TNNode) previous, graphics
                        .getNodeBorderColor(), graphics
                        .getNodeBackgroundColor());
            }
            else if (previous != null && previous instanceof TNEdge) {
                graphics.setEdgeColor((TNEdge) previous, graphics
                        .getEdgeColor());
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
            graphics.setNodeColor((TNNode) current, graphics
                    .getNodeBorderColor(), graphics.getNodeBackgroundColor());
        }
        else if (current != null && current instanceof TNEdge) {
            graphics.setEdgeColor((TNEdge) current, graphics.getEdgeColor());
        }
        gui.setReplayEnabled(true);
        graphics.refreshGraph();
    }

    /**
     * Starts the replay thread.
     */
    public void startReplay() {
        this.start();
    }

    /** 
     * Stops the replay.
     */
    public void stopReplay() {
        running = false;
    }

}
