
package org.peertrust.tnviz.app;

import java.awt.Color;
import org.jgraph.JGraph;
import org.peertrust.net.Query;
import org.peertrust.net.Answer;
import org.peertrust.tnviz.gui.TNGui;

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
