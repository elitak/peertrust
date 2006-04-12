/**
 * 
 */
package org.peertrust.modeler.policysystem.model;

import org.peertrust.modeler.policysystem.model.abtract.PSModelObject;
import org.peertrust.modeler.policysystem.model.abtract.PSModelLabel;

/**
 * Provide an implementation for the PSModelLabel interface
 * @author pat_dev
 *
 */
public class PSModelLabelImpl implements PSModelLabel 
{
	
	/**
	 * the model object that holds this label
	 */
	private PSModelObject labelHolder;
	
	/**
	 * The string value of this label
	 */
	private String value;
	
	/**
	 * Create PSModelLabelImpl with the provided label 
	 * holder and value
	 * @param labelHolder -- the label holder
	 * @param value -- the label value
	 */
	public PSModelLabelImpl(PSModelObject labelHolder, String value) 
	{
		super();
		this.labelHolder=labelHolder;
		this.value=value;
	}

	/**
	 * @see org.peertrust.modeler.policysystem.model.abtract.PSModelLabel#getLabelHolder()
	 */
	public PSModelObject getLabelHolder() 
	{
		return labelHolder;
	}

	/**
	 * @see org.peertrust.modeler.policysystem.model.abtract.PSModelLabel#setLabelHolder(org.peertrust.modeler.policysystem.model.abtract.PSModelObject)
	 */
	public void setLabelHolder(PSModelObject labelHolder) 
	{
		this.labelHolder=labelHolder;

	}

	/**
	 * @see org.peertrust.modeler.policysystem.model.abtract.PSModelLabel#getValue()
	 */
	public String getValue() 
	{
		return value;
	}

	/**
	 * @see org.peertrust.modeler.policysystem.model.abtract.PSModelLabel#setLabel(java.lang.String)
	 */
	public void setLabel(String value) 
	{
		this.value=value;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		//return super.toString();
		return value;
	}
	 
	
}
