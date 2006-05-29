package org.peertrust.modeler.policysystem.control;

import org.apache.log4j.Logger;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.part.IPage;
import org.peertrust.modeler.policysystem.model.abtract.PSModelCheck;
import org.peertrust.modeler.policysystem.model.abtract.PSModelCheckStore;
import org.peertrust.modeler.policysystem.model.abtract.PSModelObject;
import org.peertrust.modeler.policysystem.model.abtract.PSPolicy;

/**
 * Provides the controls for editing a ps policy
 * @author Patrice Congo
 *
 */
public class PSPolicyEditorControl implements PSModelObjectEditControl
{
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
	
	
	/* (non-Javadoc)
	 * @see org.peertrust.modeler.policysystem.control.PSModelObjectEditControl#dispose()
	 */
	public void dispose() 
	{
		//nthing
	}
	
	/* (non-Javadoc)
	 * @see org.peertrust.modeler.policysystem.control.PSModelObjectEditControl#createControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createControl(Composite parent) 
	{
		Label headerdd;
		top = new Composite(parent, SWT.LEFT);
		top.setLayout(new GridLayout());
		top.setLayoutData(new GridData(GridData.FILL_BOTH));
		///header
		Composite headerContainer= 
						new Composite(top,SWT.LEFT);
		Composite panel=
						new Composite(top,SWT.LEFT);
		
		headerContainer.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		headerContainer.setLayout(new GridLayout());
		headerdd= new Label(headerContainer,SWT.NONE);
		
		Label header= 
			new Label(
					headerContainer,
					SWT.LEFT|SWT.BORDER|SWT.HORIZONTAL);
		header.setText("Overridding");
		
		header.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		headerdd= new Label(
					headerContainer,
					SWT.NONE|SWT.SEPARATOR|SWT.HORIZONTAL);
		headerdd.setLayoutData(
					new GridData(GridData.FILL_HORIZONTAL));
		
		////editor
		///////////////////////////////////////////////////////
		panel.setLayoutData(new GridData(GridData.FILL_BOTH));
		///
		
		labelFiledEditor=
			new StringFieldEditor("labelFieldEditor","Label",panel);
		valueFieldEditor=
			new StringFieldEditor("valueFieldEditor","Value",panel);
		headerdd= new Label(panel,SWT.NONE);
		
		
		if(doConstructSetButton)
		{
			setButton= new Button(panel,SWT.NONE);
			setButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			setButton.setText("set");
			setButton.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {	
					saveEdit();
				}
			});
		}
//		top = new Composite(parent, SWT.LEFT);
//		//top.setLayout(new GridLayout());
//		///////////////////////////////////////////////////////
//		Label headerdd= new Label(top,SWT.NONE);		
//		Label header= new Label(top,SWT.BORDER|SWT.HORIZONTAL|SWT.FILL);
//		header.setText("Policy");
//		header.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
//		///
//		
//		labelFiledEditor=
//			new StringFieldEditor("labelFieldEditor","Label",top);
//		valueFieldEditor=
//			new StringFieldEditor("valueFieldEditor","Value",top);
//		headerdd= new Label(top,SWT.NONE);
//		
//		
//		if(doConstructSetButton)
//		{
//			setButton= new Button(top,SWT.NONE);
//			setButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
//			setButton.setText("set");
//			setButton.addSelectionListener(new SelectionAdapter() {
//				public void widgetSelected(SelectionEvent e) {	
//					saveEdit();
//				}
//			});
//		}
	}

	/* (non-Javadoc)
	 * @see org.peertrust.modeler.policysystem.control.PSModelObjectEditControl#getControl()
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
	
	/* (non-Javadoc)
	 * @see org.peertrust.modeler.policysystem.control.PSModelObjectEditControl#saveEdit()
	 */
	public int saveEdit()
	{
		if(psPolicy==null)
		{
			return SAVE_RESULT_FAILURE_NULL_POINTER;
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

	public PSModelObject getModelObject() {
		return this.getPsPolicy();
	}

	public void setModelObject(PSModelObject psModelObject) 
	{
		if(psModelObject instanceof PSPolicy)
		{
			this.setPsPolicy((PSPolicy)psModelObject);
		}

	}
}
