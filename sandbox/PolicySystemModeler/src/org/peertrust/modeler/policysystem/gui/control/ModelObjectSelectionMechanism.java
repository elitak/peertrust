package org.peertrust.modeler.policysystem.gui.control;


/**
 * Specifies the interface to implement to provide a
 * mechanism to create a model object wrapper for the
 * list editor
 * 
 * @see org.peertrust.modeler.policysystem.gui.control.PSModelWrapperListEditor
 * @author Patrice Congo
 *
 */
public interface ModelObjectSelectionMechanism 
{
	/**
	 * To create a list elements
	 * @param listEditor
	 * @param modelObjectType
	 * @param itemsToIgnore -- a string array containing 
	 * 			the labels items to ignore. It may be null
	 * @return
	 */
	public String select(
				PSModelWrapperListEditor listEditor,
				Class modelObjectType,
				String[] itemsToIgnore);
//	/**
//	 * Set the labels of the model object to ignore
//	 * during the selection
//	 * 
//	 * @param labels -- an array of string containing the
//	 * 			labels of the model object to ignore 
//	 */
//	public void setModelObjectToIgnore(String[] labels);

}
