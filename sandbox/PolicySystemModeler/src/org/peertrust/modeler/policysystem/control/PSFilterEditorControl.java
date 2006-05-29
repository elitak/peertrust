package org.peertrust.modeler.policysystem.control;

import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.DialogPage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.peertrust.modeler.policysystem.model.abtract.PSFilter;
import org.peertrust.modeler.policysystem.model.abtract.PSModelObject;
import org.peertrust.modeler.policysystem.model.abtract.PSPolicy;

public class PSFilterEditorControl implements PSModelObjectEditControl {

	
	public PSFilterEditorControl(boolean showOwnSetButton) 
	{
		this.showOwnSetButton=showOwnSetButton;
	}

	public PSFilterEditorControl() 
	{
		this(false);
	}
	
	public void dispose() 
	{
		
	}

	public PSModelObject getModelObject() {
		return this.psFilter;//null;
	}

	public void setModelObject(PSModelObject psModelObject) 
	{
		if(psModelObject==null)
		{
			return;
		}
		else if(psModelObject instanceof PSFilter)
		{
			//this.psFilter=(PSFilter)psModelObject;
			setPSFilter((PSFilter)psModelObject);
			return;
		}
		else
		{
			return;
		}
			 
	}
	
	/**
	 * Separator for the conditions string values
	 */
	public static final String CONDITION_SEP=";";
	
	/**
	 * The top composite thats hold the list editor
	 */
	private Composite top;
	
	/**
	 * The filter to edit
	 */
	private PSFilter psFilter;
	
	/**
	 * Used to show the filter label
	 */
	private StringFieldEditor labelFieldEditor;
	
	/**
	 * Used to shows the filter condition
	 */
	private StringFieldEditor valueFieldEditor;
	
	/**
	 * Used to commit the changes
	 */
	private Button setButton;
	
	/**
	 * used to edit the filter policies
	 */
	private PSModelWrapperListEditor filterPolicyListEditor;
	
	private StringButtonFieldEditor filterPolicyFieldEditor;
	private PSPolicy actualPolicy;
	private PSPolicy selectedPolicy;
	
	/**
	 * Logger for the PSFilterEditorPage class
	 */
	private static final Logger logger= 
		Logger.getLogger(PSPolicyEditorPage.class);
	
	private boolean showOwnSetButton=false;
	/**
	 * @see org.eclipse.ui.part.IPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createControl(Composite parent) 
	{
		//this.parent=parent;
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
		header.setText("Edit Filter");
		
		header.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		headerdd= new Label(
					headerContainer,
					SWT.NONE|SWT.SEPARATOR|SWT.HORIZONTAL);
		headerdd.setLayoutData(
					new GridData(GridData.FILL_HORIZONTAL));
		
		////editor
		panel.setLayoutData(new GridData(GridData.FILL_BOTH));
		//labelFieldEditor=createLabelEditor("labelFieldEditor","Label",panel);
		labelFieldEditor= new StringFieldEditor("labelFieldEditor","Label",panel);		
		headerdd= new Label(panel,SWT.NONE);
		//conditions field
		valueFieldEditor=
			new StringFieldEditor("valueFieldEditor","Value",panel);
		headerdd= new Label(panel,SWT.NONE);
		//
		filterPolicyFieldEditor=
			new StringButtonFieldEditor(
					"filterPolicyEditor",
					"Policy",
					panel);
		filterPolicyFieldEditor.setFieldValueProvider(
									createOverriderProvider());
		
		headerdd= new Label(panel,SWT.NONE);
		if(showOwnSetButton)
		{
			setButton= new Button(panel,SWT.NONE);
			setButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			setButton.setText("set");
			setButton.addSelectionListener(new SelectionAdapter() 
					{
						public void widgetSelected(SelectionEvent e) 
						{	
							saveEdit();
						}
					});
		}
//		top = new Composite(parent, SWT.LEFT);
//		//top.setLayout(new GridLayout());
//		///////////////////////////////////////////////////////
//		
//		Label headerdd= new Label(top,SWT.NONE);		
//		Label header= new Label(top,SWT.BORDER|SWT.HORIZONTAL|SWT.FILL);
//		header.setText("Filter");
//		header.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
//		///
//		
//		labelFieldEditor=
//			new StringFieldEditor("labelFieldEditor","Label",top);
//		valueFieldEditor=
//			new StringFieldEditor("valueFieldEditor","Value",top);
//		headerdd= new Label(top,SWT.NONE);
//		headerdd.setText("Policies");
//		Composite com= new Composite(top,SWT.NONE);
//		headerdd= new Label(com,SWT.NONE);
//		headerdd.setText(
//				"                                            ");
//		headerdd= new Label(com,SWT.NONE);
//		
//		filterPolicyFieldEditor=
//			new StringButtonFieldEditor(
//					"filterPolicy",
//					"Policy",
//					top);
//		///
//		filterPolicyListEditor=
//			new PSModelWrapperListEditor("namele","",com,PSPolicy.class);
//		headerdd= new Label(top,SWT.NONE);
//		if(showOwnSetButton)
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

	/**
	 * @see org.eclipse.ui.part.IPage#getControl()
	 */
	public Control getControl() 
	{
		return top;
	}

	/**
	 * @see org.eclipse.ui.part.IPage#setFocus()
	 */
	public void setFocus() 
	{
		
	}

	/**
	 * @return returns the filter which is neing edited
	 */
	public PSFilter getPSFilter() 
	{
		return psFilter;
	}

	/**
	 * To set the filter to edit
	 * @param psFilter -- the filter to edit
	 */
	private void setPSFilter(PSFilter psFilter) 
	{
		this.psFilter = psFilter;
		if(psFilter!=null)
		{
			labelFieldEditor.setStringValue(
							psFilter.getLabel().getValue());
			String conditionsAsString=
				psFilter.getCondition();//conditionsToString(psFilter.getCondition());
			valueFieldEditor.setStringValue(
									conditionsAsString);
		}
		else
		{
			labelFieldEditor.setStringValue("");
			valueFieldEditor.setStringValue("");
		}
		Vector filterPolicies=psFilter.getIsprotectedBy();
		if(filterPolicyListEditor!=null)
		{
			filterPolicyListEditor.clear();
			final int MAX=filterPolicies.size();
			if(MAX>0)
			{
				PSModelObject modelObjects[]= new PSModelObject[MAX];
				filterPolicies.toArray(modelObjects);
				filterPolicyListEditor.addList(modelObjects);
			}
		}
		else if(filterPolicyFieldEditor!=null)
		{
			if(!filterPolicies.isEmpty())
			{
				actualPolicy=(PSPolicy)filterPolicies.elementAt(0);
				selectedPolicy=null;
				filterPolicyFieldEditor.setStringValue(actualPolicy.getLabel().toString());
			}
		}
		else
		{
			throw new RuntimeException("Filter policy editor control not available");
		}
		
	}
	
	/**
	 * Convert a condition to a string.
	 * The following format is used:<br/>
	 * cond1|cond2|cond3|
	 * @param conditions -- a vectorcontaining the conditions
	 * @return a string containing all the conditions
	 */
	private String conditionsToString(Vector conditions)
	{
		StringBuffer buffer= new StringBuffer();
		for(Iterator it=conditions.iterator(); it.hasNext();)
		{
			buffer.append(it.next());
			buffer.append(CONDITION_SEP);
		}
		
		return buffer.toString();	
	}
	
	
	/**
	 * Convert a String into an array of condition
	 * 
	 * @see PSFilterEditorPage#conditionsToString(Vector)
	 * @param condString -- the string representing the condition
	 * @return an array containing the computed conditions
	 */
	private String[] stringToCondition(String condString)
	{
		if(condString==null)
		{
			return new String[]{};
		}
		return condString.split(CONDITION_SEP);
		
	}
	
	/**
	 * Use to save the editor changes
	 */
	public int saveEdit()
	{
		try {
			if(psFilter==null)
			{
				return SAVE_RESULT_FAILURE_NULL_POINTER;
			}
			
			String newLabel=
				labelFieldEditor.getStringValue();
			String newValue=
				valueFieldEditor.getStringValue();
			if(newLabel==null || newValue==null)
			{
				return SAVE_RESULT_FAILURE_ILLEGAL_VALUE;
			}
			psFilter.setLabel(newLabel);
			
			psFilter.removeAllConditions();
			String[] conditions=stringToCondition(newValue);
			for(int i=0; i<conditions.length;i++)
			{
				psFilter.setHasCondition(conditions[i]);
			}
			
			if(filterPolicyListEditor!=null)
			{
				Vector policies=filterPolicyListEditor.getListModelObject();
				logger.info("\n\nFilter Policies:"+policies);
				for(Iterator it=policies.iterator();it.hasNext();)
				{
					psFilter.addIsProtectedBy((PSPolicy)it.next());
				}
			}
			else if(filterPolicyFieldEditor!=null)
			{
				psFilter.removeIsProtectedBy(actualPolicy);
				psFilter.addIsProtectedBy(selectedPolicy);
				actualPolicy=selectedPolicy;
				selectedPolicy=null;
			}
			else
			{
				logger.warn("not Filter policy editor set");
			}
			return SAVE_RESULT_OK;
		} 
		catch (Exception e) 
		{
			logger.warn("Error while saving filter changes",e);
			return SAVE_RESULT_FAILURE_EXCEPTION;			
		}
	}
	
	private FieldValueProvider createOverriderProvider()
	{
		FieldValueProvider pv=
			new FieldValueProvider()
			{

				public String getFieldValue() 
				{
					try {
						DialogPage p;
						String pageName="Choose new filter policy";
						ChooserWizard wiz=new ChooserWizard(pageName);
						ChooserWizardPage page=
							new ChooserWizardPage(pageName,PSPolicy.class);
						wiz.addPage(page);
						WizardDialog dlg=
							new WizardDialog(
									top.getParent().getShell(),//parent.getShell(),
									wiz);
						
						int resp=dlg.open();
						PSPolicy sel=
							(PSPolicy)wiz.getSelected();
						if(sel==null)
						{
							logger.warn("nothing selected returning null");
							return null;
						}
						//overridingRule.setHasOverrider(sel);
						///selectedOverridderOld=selectedOverridder;
						selectedPolicy=sel;
						String label=sel.getLabel().getValue();
						logger.info("Selected:"+sel);
						return label;
					} catch (Exception e) {
						e.printStackTrace();
						return null;
					}
				}
			
			};
		return pv;
	}

}
