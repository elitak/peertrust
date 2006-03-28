/**
 * 
 */
package org.peertrust.modeler.policysystem.views;

import java.util.Vector;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.jgraph.event.GraphModelEvent;
import org.jgraph.event.GraphModelListener;
import org.jgraph.event.GraphSelectionEvent;
import org.jgraph.event.GraphSelectionListener;

/**
 * @author pat_dev
 *
 */
public class GraphSelectionProvider implements ISelectionProvider,
		GraphModelListener, GraphSelectionListener{

	private Vector selectionListeners; 
	
	private ISelection iSelection;
	
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ISelectionProvider#addSelectionChangedListener(org.eclipse.jface.viewers.ISelectionChangedListener)
	 */
	public void addSelectionChangedListener(ISelectionChangedListener listener) {
		if(listener==null)
		{
			return;
		}
		if(selectionListeners.contains(listener))
		{
			return;
		}
		else
		{
			selectionListeners.add(listener);
			return;
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ISelectionProvider#getSelection()
	 */
	public ISelection getSelection() {
		return iSelection;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ISelectionProvider#removeSelectionChangedListener(org.eclipse.jface.viewers.ISelectionChangedListener)
	 */
	public void removeSelectionChangedListener(
			ISelectionChangedListener listener) {
		if(listener==null)
		{
			return;
		}
		else
		{
			selectionListeners.remove(listener);
			return;
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ISelectionProvider#setSelection(org.eclipse.jface.viewers.ISelection)
	 */
	public void setSelection(ISelection selection) {
		iSelection=selection;
	}

	//////////////////////////////////////////////////////////////////////////
	//////////////////////////GRAPH PART//////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////
	/* (non-Javadoc)
	 * @see org.jgraph.event.GraphModelListener#graphChanged(org.jgraph.event.GraphModelEvent)
	 */
	public void graphChanged(GraphModelEvent gme) {
		System.out.println("XXXXXXXXXXXXXX="+gme.getChange().getSource());
	}

	//////////////////////////////////////////////////////////////////////////
	/////////////////////////graph selection listener/////////////////////////
	//////////////////////////////////////////////////////////////////////////
	public void valueChanged(GraphSelectionEvent selE) {
		System.out.println("selE:"+selE.getCell());		
	}

	
}
