package org.peertrust.modeler.policysystem.control;

import java.util.Hashtable;

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
	public String create(Hashtable modelWrapperStore);
	

}
