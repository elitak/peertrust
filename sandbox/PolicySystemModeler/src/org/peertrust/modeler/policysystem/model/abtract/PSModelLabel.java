package org.peertrust.modeler.policysystem.model.abtract;

/**
 * Interface to implement by classes abtracting model object label or name
 * 
 * @author Patrice Congo
 */
public interface PSModelLabel extends PSModelPrimitive 
{
	/**
	 * Return the model object holding this label
	 * @return the model object thats holds this label
	 */
	public PSModelObject getLabelHolder();
	
	/**
	 * To set a new label holder
	 * @param labelHolder -- the new label holder
	 */
	public void setLabelHolder(PSModelObject labelHolder);
	
	/**
	 * To get the label string value
	 * @return the label value as string
	 */
	public String getValue();
	
	/**
	 * To set the label value
	 * @param value -- the new label value to set 
	 */
	public void setLabel(String value);
}
