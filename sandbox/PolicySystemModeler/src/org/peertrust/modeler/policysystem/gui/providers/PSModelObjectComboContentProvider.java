/**
 * 
 */
package org.peertrust.modeler.policysystem.gui.providers;

import java.lang.reflect.Array;
import java.util.Collection;

import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

/**
 * The content provider for combo boxes containing model objects.
 * Its #getElements() expects an array or a collection as argument.
 * 
 * @author Patrice Congo
 *
 */
public class PSModelObjectComboContentProvider implements IStructuredContentProvider
{
	
	/**
	 * The logger for the <code>PSModelObjectComboContentProvider</code> class
	 */
	private static final Logger logger=
		Logger.getLogger(PSModelObjectComboContentProvider.class);
	
	/**
	 * Empty array used as return value
	 */
	private static final Object[] EMPTY_OBJ_ARRAY= new Object[0];
	
	/**
	 * default constructor
	 */
	public PSModelObjectComboContentProvider()
	{
		//empty
	}

	/**
	 * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.Object)
	 */
	public Object[] getElements(Object inputElement) {
		logger.info("Getting input for: "+inputElement);
		if(inputElement==null)
		{
			logger.warn("getElements(Object inputElement) argument is null; "+
						"returning an empty array");
			return EMPTY_OBJ_ARRAY;
		}
		else if(inputElement.getClass().isArray())
		{
			
			int len=Array.getLength(inputElement);
			Object[] objs= new Object[len];
			for(len=len-1;len>=0;len--)
			{
				objs[len]=Array.get(inputElement,len);
			}
			logger.warn("Save:"+(Object[])inputElement +
					"\n\t objs:"+objs+
					"\n\tlen="+objs.length);
			return objs;//(Object[])inputElement;
		}
		else if(inputElement instanceof Collection)
		{
			return ((Collection)inputElement).toArray();
		}
		else
		{
			logger.warn(
					"getElements(Object inputElement) argument object type is neither an "+
					" array nor a Collection; returning empty array");
			return EMPTY_OBJ_ARRAY;
		}
	}

	/**
	 * @see org.eclipse.jface.viewers.IContentProvider#dispose()
	 */
	public void dispose() {
		//empty
	}

	/**
	 * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
	 */
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		//empty
	}
	
		
}