/**
 * 
 */
package test;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.viewers.IStructuredSelection;

class ModelObjectISelection implements IStructuredSelection
{
	/**
	 * A list  to hold the selected element
	 */
	private final List selectedElements;
	
	/**
	 * Create a ModelObjectISelection without selected objects
	 */
	public ModelObjectISelection()
	{
		 this.selectedElements= new ArrayList(); 
	}
	
	/**
	 * Create a ModelObjectSelectionWith that holds the given
	 * selection.
	 * 
	 * @param selection -- specifies the selection.
	 * 			<ul>
	 * 				<li/>if it is an array the array elements are considered to be the selection
	 * 				<li/>if selection is a collection ths collection elements are 
	 * 					supossed to be the selected elements.
	 * 				<li/>else the selection is condiered to be the single selected 
	 * 					object itself
	 * 			</ul>
	 */
	public ModelObjectISelection(Object selection)
	{
		 this.selectedElements= new ArrayList(); 
		 if(selection==null)
		 {
			 return;
		 }
		 else if(selection.getClass().isArray())
		 {
			 for(int i=Array.getLength(selection)-1;i>=0;i--)
			 {
				 selectedElements.add(0,Array.get(selection,i));	
			 }
		 }
		 else if(selection instanceof Collection)
		 {
			 selectedElements.addAll((Collection)selection);
		 }
		 else
		 {
			 selectedElements.add(selection);
		 }
	}
	
	/**
	 * @see org.eclipse.jface.viewers.IStructuredSelection#getFirstElement()
	 */
	public Object getFirstElement() {
		if(selectedElements.isEmpty())
		{
			return null;
		}
		else
		{
			return selectedElements.get(0);
		}
	}

	/**
	 * @see org.eclipse.jface.viewers.IStructuredSelection#iterator()
	 */
	public Iterator iterator() {
		return new ArrayList(selectedElements).iterator();
	}

	/**
	 * @see org.eclipse.jface.viewers.IStructuredSelection#size()
	 */
	public int size() {
		return selectedElements.size();
	}

	/**
	 * @see org.eclipse.jface.viewers.IStructuredSelection#toArray()
	 */
	public Object[] toArray() {
		return selectedElements.toArray();
	}

	/**
	 * @see org.eclipse.jface.viewers.IStructuredSelection#toList()
	 */
	public List toList() {
		return new ArrayList(selectedElements);
	}

	/**
	 * @see org.eclipse.jface.viewers.ISelection#isEmpty()
	 */
	public boolean isEmpty() {
		return selectedElements.isEmpty();
	}		
}