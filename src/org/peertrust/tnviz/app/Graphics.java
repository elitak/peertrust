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
package org.peertrust.tnviz.app;

import java.awt.Color;
import org.jgraph.JGraph;
import org.peertrust.net.Query;
import org.peertrust.net.Answer;
import org.peertrust.tnviz.gui.TNGui;

/**
 * <p>
 * 
 * </p><p>
 * $Id: Graphics.java,v 1.3 2005/05/22 17:56:44 dolmedilla Exp $
 * <br/>
 * Date: 10-Feb-2005
 * <br/>
 * Last changed: $Date: 2005/05/22 17:56:44 $
 * by $Author: dolmedilla $
 * </p>
 * @author Sebastian Wittler and Michael Sch?fer
 */
public interface Graphics {
	
	public static final int TREE_LAYOUT = 0;
	public static final int SEQ_LAYOUT = 1;
	
	public abstract void wipeGraph();
	
	public abstract JGraph getGraph();
	
	public abstract void refreshGraph();
	
	public abstract void addQuery(Query query);
	
	public abstract void addAnswer(Answer answer);
	
	public abstract Color getEdgeColor();
	
	public abstract void setEdgeColor(Color color);
	
	public abstract void setEdgeColor(TNEdge edge, Color color);
		
	public abstract Color getNodeBorderColor();
	
	public abstract Color getNodeBackgroundColor();
	
	public abstract void setNodeColor(Color borderColor, Color backgroundColor);
	
	public abstract void setNodeColor(TNNode node, Color borderColor, Color backgroundColor);
	
	public abstract boolean getEdgeEditable();
	
	public abstract void setEdgeEditable(boolean editable);
	
	public abstract boolean getEdgeMovable();
	
	public abstract void setEdgeMovable(boolean movable);
	
	public abstract boolean getNodeEditable();
	
	public abstract void setNodeEditable(boolean editable);
	
	public abstract boolean getNodeMovable();
	
	public abstract void setNodeMovable(boolean movable);
	
	public abstract void collapse(TNNode node);
	
	public abstract void expand(TNNode node);
	
	public abstract void startReplayGraphPath();
	
	public abstract void stopReplayGraphPath();
	
	public abstract int getNodeBoundsX();
	
	public abstract void setNodeBoundsX(int nodeBoundsX);
	
	public abstract int getNodeBoundsY();
	
	public abstract void setNodeBoundsY(int nodeBoundsY);
		
	public abstract int getNRadius();
	
	public abstract void setNRadius(int nRadius);
	
	public abstract int getLayout();
	
	public abstract void setLayout(int layout);
	
	public abstract TNNode getRoot();

	public abstract void setRoot(TNNode node);
	
	public abstract void setFScaleX(double fScaleX);
	
	public abstract double getFScaleX();
	
	public abstract int getRadiusNormal();
	
	public abstract TNGui getGui();
	
	public abstract int getMinRadius();

	public abstract void updateGraph();
}
