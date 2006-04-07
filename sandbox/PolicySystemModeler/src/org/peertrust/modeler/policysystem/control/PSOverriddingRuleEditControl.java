/**
 * 
 */
package org.peertrust.modeler.policysystem.control;

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.DialogPage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
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
import org.peertrust.modeler.policysystem.PolicysystemPlugin;
import org.peertrust.modeler.policysystem.control.PSOverriddingRuleEditorPage.ChooserWizard;
import org.peertrust.modeler.policysystem.model.abtract.PSModelObject;
import org.peertrust.modeler.policysystem.model.abtract.PSOverridingRule;
import org.peertrust.modeler.policysystem.model.abtract.PSPolicy;
import org.peertrust.modeler.policysystem.model.checks.CkeckOverridingRulePoliciesNeq;

/**
 * Provide a panel for editing an overriding model object.
 * 
 * @author Patrice Congo
 */
public class PSOverriddingRuleEditControl 
				implements PSModelObjectEditControl
{

	/**
	 * to cache localy the parent composite that holds
	 * all editor controls
	 */
	private Composite parent;
	
	/**
	 * The top container panel for the editor controls
	 */
	private Composite top;
	
	/** 
	 * The ps overridding rule to edit
	 */
	private PSOverridingRule overridingRule;
	
	/**
	 * The field edito used to edit the overridding rule 
	 * model object label
	 */
	private StringFieldEditor labelFieldEditor;
	
	/**
	 * The field editos used to edit the overridding rule 
	 * overridder policy
	 */
	private StringButtonFieldEditor overridderFieldEditor;
	
	/**
	 * The field editr used to edit the overridding rule 
	 * oberridden policy
	 */
	private StringButtonFieldEditor overriddenFieldEditor;
	
	/** the change comit button*/
	private Button setButton;
	
	private boolean doCreateSetButtion=false;
	
	/**
	 * The logger for the PSPolicyOverriddingRuleEditControl Class
	 */
	static private Logger logger= 
		Logger.getLogger(PSOverriddingRuleEditControl.class);
	
	/** 
	 * the selected new replacement rule model object 
	 * for the overridden rule
	 */ 
	private PSPolicy selectedOverridden=null;
	
	/** 
	 * the selected new replacement rule model object 
	 * for the overridder rule
	 */
	private PSPolicy selectedOverridder=null;
	
	/**
	 * Creates the controls; use the provided composite as parent container
	 */
	public void createControl(Composite parent) 
	{
		
		this.parent=parent;
		Label headerdd;
		top = new Composite(parent, SWT.LEFT);
		top.setLayout(new GridLayout());
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
		panel.setLayoutData(new GridData(GridData.FILL_BOTH));
		//labelFieldEditor=createLabelEditor("labelFieldEditor","Label",panel);
		labelFieldEditor= new StringFieldEditor("labelFieldEditor","Label",panel);
		headerdd= new Label(panel,SWT.NONE);
		overridderFieldEditor=
			new StringButtonFieldEditor(
					"overridderFieldEditor",
					"Overridder",
					panel);
		overridderFieldEditor.setFieldValueProvider(
									createOverriderProvider());
		overriddenFieldEditor=
			new StringButtonFieldEditor(
					"overriddenFieldEditor","overridden",panel);
		overriddenFieldEditor.setFieldValueProvider(createOverridenProvider());
		
		headerdd= new Label(panel,SWT.NONE);
		if(doCreateSetButtion)
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
		
		//top.update();
	}

	/**
	 * @return the top editor control
	 */
	public Control getControl() 
	{
		return top;
	}

	public void setFocus() 
	{
		
	}

	
	
	/**
	 * @return the rule which is being edited
	 */
	public PSOverridingRule getOverridingRule() 
	{
		return overridingRule;
	}

	/**
	 * Sets a new model overriding rule to edit
	 * @param overridingRule -- a new overriding rule to edit
	 */
	public void setOverridingRule(PSOverridingRule overridingRule) 
	{
		if(overridingRule==null)
		{
			return;
		}
		try {
			logger.info("new overridingRule:"+overridingRule);
			this.overridingRule = overridingRule;
			PSPolicy psPol=overridingRule.getHasOverridden();
			
			if(psPol!=null)
			{
				overriddenFieldEditor.setStringValue(
									psPol.getLabel().getValue());	
			}
			else
			{
				overriddenFieldEditor.setStringValue("");
			}
			
			psPol=overridingRule.getHasOverridder();
			//System.out.println("overrindingRulePSPol:"+psPol);
			if(psPol!=null)
			{
				overridderFieldEditor.setStringValue(
									psPol.getLabel().getValue());
			}
			else
			{
				overridderFieldEditor.setStringValue("");
			}
			
			labelFieldEditor.setStringValue(
					overridingRule.getLabel().getValue());
			selectedOverridden=null;
			selectedOverridder=null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	public int saveEdit()
	{
		try {
			if(overridingRule==null)
			{
				return PSModelObjectEditControl.SAVE_RESULT_FAILURE_NULL_POINTER;
			}
			
			String overriddenLabel=
				overriddenFieldEditor.getStringValue();
			String overridderLabel=
				overridderFieldEditor.getStringValue();
			String ruleLabel=labelFieldEditor.getStringValue();
			if(ruleLabel!=null || !"".equals(ruleLabel))
			{
				overridingRule.setLabel(ruleLabel);
			}
			
			if(selectedOverridden==null || selectedOverridder==null)
			{
				PolicysystemPlugin.getDefault().showMessage(
						"Selection incomplete:"+
						"\n\t:overridder:"+selectedOverridder+
						"\n\t:overridden:"+selectedOverridden);
				return PSModelObjectEditControl.SAVE_RESULT_FAILURE_ILLEGAL_VALUE;
			}
			
			CkeckOverridingRulePoliciesNeq check=
				new CkeckOverridingRulePoliciesNeq(
										selectedOverridder,
										selectedOverridden);
			if(check.doCheck()==false)
			{
				PolicysystemPlugin.getDefault().showMessage(
						"Overridding and overridden policies must not be equals");
				return PSModelObjectEditControl.SAVE_RESULT_FAILURE_INTEGRITY_CHECK;
			}
			
			if(overriddenLabel==null || overridderLabel==null)
			{
				return PSModelObjectEditControl.SAVE_RESULT_FAILURE_ILLEGAL_VALUE;
			}
			
			if(selectedOverridden!=null)
			{
				overridingRule.setHasOverriden(selectedOverridden);
			}
			
			if(selectedOverridder!=null)
			{
				overridingRule.setHasOverrider(selectedOverridder);	
			}
			
//			String ruleLabel=labelFieldEditor.getStringValue();
//			if(ruleLabel!=null || !"".equals(ruleLabel))
//			{
//				overridingRule.setLabel(ruleLabel);
//			}
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		return PSModelObjectEditControl.SAVE_RESULT_OK;
	}
	
	private FieldValueProvider createOverridenProvider()
	{
		FieldValueProvider pv=
			new FieldValueProvider()
			{

				public String getFieldValue() 
				{
					try {
						DialogPage p;
						ChooserWizard wiz=new ChooserWizard();
						ChooserWizardPage page=
							new ChooserWizardPage("choose policy",PSPolicy.class);
						wiz.addPage(page);
						logger.info("SHELLLLLLLLLLLLLL:"+parent.getShell());
						WizardDialog dlg=
							new WizardDialog(
									parent.getShell(),
									wiz);
						
						int resp=dlg.open();
						PSPolicy sel=
							(PSPolicy)wiz.getSelected();
						if(sel==null)
						{
							logger.warn("nothing selected returning null");
							return null;
						}
						//overridingRule.setHasOverriden(sel);
						selectedOverridden=sel;
						
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
	
	private FieldValueProvider createOverriderProvider()
	{
		FieldValueProvider pv=
			new FieldValueProvider()
			{

				public String getFieldValue() 
				{
					try {
						DialogPage p;
						ChooserWizard wiz=new ChooserWizard();
						ChooserWizardPage page=
							new ChooserWizardPage("choose policy",PSPolicy.class);
						wiz.addPage(page);
						logger.info("SHELLLLLLLLLLLLLL:"+parent.getShell());
						WizardDialog dlg=
							new WizardDialog(
									parent.getShell(),
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
						selectedOverridder=sel;
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
	
	public class ChooserWizard extends Wizard
	{
		private Object selected;
		public ChooserWizard()
		{
			
		}
		public boolean performFinish() 
		{
			selected=((ChooserWizardPage)
						getPage("choose policy")).getSelected();;
			return true;
		}
		public Object getSelected() {
			return selected;
		}
		
	}
	
	private StringButtonFieldEditor createLabelEditor(
							String name,//"labelFieldEditor",
							String labelStext,//"Label",
							Composite parent//panel
							)
	{
		StringButtonFieldEditor ed= 
			new StringButtonFieldEditor(name,labelStext,parent)
			{
	
				protected String changePressed() {
					String label=labelFieldEditor.getStringValue();
					if(label!=null)
					{
						overridingRule.setLabel(label);
					}
					return label;
				}
				
			};
		return ed;
	}

	/**
	 * @see org.eclipse.jface.viewers.IDoubleClickListener#doubleClick(org.eclipse.jface.viewers.DoubleClickEvent)
	 */
	public void doubleClick(DoubleClickEvent event) 
	{
		ISelection sel=event.getSelection();
		if(sel==null)
		{
			return;
		}
		if(sel instanceof IStructuredSelection)
		{
			IStructuredSelection ssel= (IStructuredSelection)sel;
			Object ele0=ssel.getFirstElement();
			if(ele0==null)
			{
				logger.warn("first selection is null");
				return;
			}
			else if(ele0 instanceof PSPolicy)
			{
				PSPolicyEditDialog dlg=
					new PSPolicyEditDialog(parent.getShell(),PSPolicy.class);
				dlg.create();
				dlg.setModelObject((PSPolicy)ele0);
				dlg.open();
			}
			else if(ele0 instanceof PSOverridingRule)
			{
				logger.warn("no impl for PSOverrindingrule");
			}
			else
			{
				logger.warn(
					"Cannot handle this kind of selection element:"+ele0);
			}
		}
		else
		{
			logger.warn("Cannot handle this kind auf selection:"+sel);
		}
		
	}

	public void dispose() 
	{
		// nothing
	}

	public PSModelObject getModelObject() 
	{
		return this.getOverridingRule();
	}

	public void setModelObject(PSModelObject psModelObject) 
	{
		if(psModelObject instanceof PSOverridingRule)
		{
			this.setOverridingRule((PSOverridingRule)psModelObject);
		}
	}
}
