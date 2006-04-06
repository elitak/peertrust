package org.peertrust.modeler.policysystem.control;

import org.apache.log4j.Logger;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.part.IPage;
import org.peertrust.modeler.policysystem.model.abtract.PSModelCheck;
import org.peertrust.modeler.policysystem.model.abtract.PSModelCheckStore;
import org.peertrust.modeler.policysystem.model.abtract.PSPolicy;

/**
 * Provides the controls for editing a ps policy
 * @author Patrice Congo
 *
 */
public class PSPolicyEditorControl
{
	/** 
	 * Return if save succeed
	 */
	static public final int SAVE_RESULT_OK=0;
	/**
	 * return if the policy to edit is null while trying to save changes
	 */
	static public final int SAVE_RESULT_FAILURE_POLICY_NULL=-1;
	
	/**
	 * return for a failure due to an illegal value of the label or value
	 */
	static public final int SAVE_RESULT_FAILURE_ILLEGAL_VALUE=-2;
	
	/**
	 *return  for a failure caused by an exception 
	 */
	static public final int SAVE_RESULT_FAILURE_EXCEPTION=-3;
	
	/**
	 * return for a failure to a failed integrity check for the 
	 * ps policy setting
	 */
	static public final int SAVE_RESULT_FAILURE_INTEGRITY_CHECK=0;
	
	/** 
	 * the PSPolicyEditorControl logger
	 */
	private static final Logger logger= 
		Logger.getLogger(PSPolicyEditorControl.class);

	
	/**
	 * The top composite that holds all other editor controls
	 */
	private Composite top;
	
	/**
	 * The ps Policy to edit
	 */
	private PSPolicy psPolicy;
	
	/**
	 * Used to edit the ps policy label
	 */
	private StringFieldEditor labelFiledEditor;
	
	/**
	 * used to edit the ps policy value
	 */
	private StringFieldEditor valueFieldEditor;
	
	/**
	 * Button used to commit the changes. 
	 * Its creation is controled by <code>doConstructSetButton</code>
	 */
	private Button setButton;
	
	/**
	 * Used to control the creation of the set button
	 */
	private boolean doConstructSetButton=false;
	
	/**
	 * Check whether the new value sytisfyies the integrity constrainst
	 */
	private PSModelCheckStore modelCheckStore; 
	
	/**
	 * Construct a PSPolicyEditorControl without
	 * Set button
	 */
	public PSPolicyEditorControl() 
	{
		super();
		this.doConstructSetButton=false;
		modelCheckStore=null;
	}
	
	/**
	 * Construct a PSPolicyEditorControl with odet without
	 * Set button
	 * @param doConstructSetButton
	 */
	public PSPolicyEditorControl(boolean doConstructSetButton) 
	{
		super();
		this.doConstructSetButton=doConstructSetButton;
	}
	
	
	/**
	 * does cleanups
	 */
	public void dispose() 
	{
		//nthing
	}
	
	/**
	 * Creates the editor controls
	 * @param parent -- 
	 */
	public void createControl(Composite parent) 
	{
		top = new Composite(parent, SWT.LEFT);
		//top.setLayout(new GridLayout());
		///////////////////////////////////////////////////////
		Label headerdd= new Label(top,SWT.NONE);		
		Label header= new Label(top,SWT.BORDER|SWT.HORIZONTAL|SWT.FILL);
		header.setText("Policy");
		header.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		///
		
		labelFiledEditor=
			new StringFieldEditor("labelFieldEditor","Label",top);
		valueFieldEditor=
			new StringFieldEditor("valueFieldEditor","Value",top);
		headerdd= new Label(top,SWT.NONE);
		
		
		if(doConstructSetButton)
		{
			setButton= new Button(top,SWT.NONE);
			setButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			setButton.setText("set");
			setButton.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {	
					saveEdit();
				}
			});
		}
	}

	/**
	 * To get the editor controls
	 * @return the top composite comtaining the editor control
	 */
	public Control getControl() 
	{
		return top;
	}

	/**
	 * to call when the editor control get focused
	 */
	public void setFocus() 
	{
		//nothing
	}

	/**
	 * To get the ps policy which is being edited
	 * @return the policy which is being edited
	 */
	public PSPolicy getPsPolicy() 
	{
		return psPolicy;
	}

	/**
	 * Sets a policy to edit
	 * @param psPolicy -- the new policy to edit
	 */
	public void setPsPolicy(PSPolicy psPolicy) 
	{
		this.psPolicy = psPolicy;
		if(psPolicy!=null)
		{
			labelFiledEditor.setStringValue(
								psPolicy.getLabel().getValue());
			valueFieldEditor.setStringValue(psPolicy.getHasValue());
		}
	}
	
	/**
	 * Saves the changes
	 */
	public int saveEdit()
	{
		if(psPolicy==null)
		{
			return SAVE_RESULT_FAILURE_POLICY_NULL;
		}
		
		String newLabel=
			labelFiledEditor.getStringValue();
		String newValue=
			valueFieldEditor.getStringValue();
		if(newLabel==null || newValue==null)
		{
			return SAVE_RESULT_FAILURE_ILLEGAL_VALUE;
		}
		
		if(!doModelChecks(newLabel,newValue))
		{
			return SAVE_RESULT_FAILURE_INTEGRITY_CHECK;
		}
		
		try {
			psPolicy.setLabel(newLabel);
			psPolicy.setHasValue(newValue);
		} 
		catch (Exception e) 
		{
			logger.error("Error while saving policy changes",e);
			return SAVE_RESULT_FAILURE_EXCEPTION;
		}
		
		return SAVE_RESULT_OK;
	}
	
	/**
	 * Performs the model checks 
	 * @return true if passed otherwise false
	 */
	private boolean doModelChecks(String newLabel, String newValue)
	{
		if(modelCheckStore==null)
		{
			//TODO modelc heck for policy editing
			//return false actualy
		}
		return true;
	}
}
