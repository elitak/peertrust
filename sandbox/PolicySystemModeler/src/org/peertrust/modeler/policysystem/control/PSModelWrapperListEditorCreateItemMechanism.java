package org.peertrust.modeler.policysystem.control;

import java.util.Hashtable;

import org.eclipse.swt.widgets.Shell;

/**
 * Specifies the interface to implement to provide a
 * mechanism to create a model object wrapper for the
 * list editor
 * 
 * @see org.peertrust.modeler.policysystem.control.PSModelWrapperListEditor
 * @author Patrice Congo
 *
 */
public interface PSModelWrapperListEditorCreateItemMechanism 
{
	/**
	 * To create a list elements
	 * @param listEditor
	 * @param modelObjectType
	 * @return
	 */
	public String create(
				PSModelWrapperListEditor listEditor,
				Class modelObjectType);
	

}
