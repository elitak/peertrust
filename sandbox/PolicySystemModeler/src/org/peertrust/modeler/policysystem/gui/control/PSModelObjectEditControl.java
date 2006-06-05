package org.peertrust.modeler.policysystem.gui.control;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.peertrust.modeler.policysystem.model.abtract.PSModelObject;

public interface PSModelObjectEditControl 
{

	/** 
	 * Return if save succeed
	 */
	static public final int SAVE_RESULT_OK = 0;

	/**
	 * return if the policy to edit is null while trying to save changes
	 */
	static public final int SAVE_RESULT_FAILURE_NULL_POINTER = -1;

	/**
	 * return for a failure due to an illegal value of the label or value
	 */
	static public final int SAVE_RESULT_FAILURE_ILLEGAL_VALUE = -2;

	/**
	 *return  for a failure caused by an exception 
	 */
	static public final int SAVE_RESULT_FAILURE_EXCEPTION = -3;

	/**
	 * return for a failure to a failed integrity check for the 
	 * ps policy setting
	 */
	static public final int SAVE_RESULT_FAILURE_INTEGRITY_CHECK = 0;

	/**
	 * does cleanups
	 */
	public abstract void dispose();

	/**
	 * Creates the editor controls
	 * @param parent -- 
	 */
	public abstract void createControl(Composite parent);

	/**
	 * To get the editor controls
	 * @return the top composite comtaining the editor control
	 */
	public abstract Control getControl();

	/**
	 * Saves the changes
	 */
	public abstract int saveEdit();
	
	public PSModelObject getModelObject();
	
	public void setModelObject(PSModelObject psModelObject); 

}