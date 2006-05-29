package org.peertrust.modeler.policysystem.control;

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.DialogPage;
import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.Platform;

import org.eclipse.ui.IResourceActionFilter;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.editors.text.SelectResourcesDialog;
import org.eclipse.ui.part.Page;
import org.peertrust.modeler.policysystem.PolicysystemPlugin;
import org.peertrust.modeler.policysystem.model.abtract.PSOverridingRule;
import org.peertrust.modeler.policysystem.model.abtract.PSPolicy;
import org.peertrust.modeler.policysystem.model.checks.CheckORulePoliciesInSamePath;
import org.peertrust.modeler.policysystem.model.checks.CkeckOverridingRulePoliciesNeq;

import com.hp.hpl.jena.query.util.LabelToNodeMap;

/**
 * Page to edit an overriding rule model Object 
 * @author Patrice Congo
 *
 */
public class PSOverriddingRuleEditorPage 
					extends Page
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
	private PSOverridingRule overrindingRule;
	
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
	
	/**
	 * The logger for the PSPolicyOverriddingRuleEditorPage Class
	 */
	static private Logger logger= Logger.getLogger(PSOverriddingRuleEditorPage.class);
	
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
	
	PSOverriddingRuleEditControl orControl;
	/**
	 * @see org.eclipse.ui.part.IPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createControl(Composite parent) 
	{
		
		this.parent=parent;
		//Label headerdd;
		top = new Composite(parent, SWT.LEFT);
		top.setLayout(new GridLayout());
		orControl= new PSOverriddingRuleEditControl(true);
		orControl.createControl(top);
		///header
//		Composite headerContainer= 
//						new Composite(top,SWT.LEFT);
//		Composite panel=
//						new Composite(top,SWT.LEFT);
//		
//		headerContainer.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
//		headerContainer.setLayout(new GridLayout());
//		headerdd= new Label(headerContainer,SWT.NONE);
//		
//		Label header= 
//			new Label(
//					headerContainer,
//					SWT.LEFT|SWT.BORDER|SWT.HORIZONTAL);
//		header.setText("Overridding");
//		
//		header.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
//		headerdd= new Label(
//					headerContainer,
//					SWT.NONE|SWT.SEPARATOR|SWT.HORIZONTAL);
//		headerdd.setLayoutData(
//					new GridData(GridData.FILL_HORIZONTAL));
//
//		panel.setLayoutData(new GridData(GridData.FILL_BOTH));
//		//labelFieldEditor=createLabelEditor("labelFieldEditor","Label",panel);
//		labelFieldEditor= new StringFieldEditor("labelFieldEditor","Label",panel);
//		headerdd= new Label(panel,SWT.NONE);
//		overridderFieldEditor=
//			new StringButtonFieldEditor(
//					"overridderFieldEditor",
//					"Overridder",
//					panel);
//		overridderFieldEditor.setFieldValueProvider(
//									createOverriderProvider());
//		overriddenFieldEditor=
//			new StringButtonFieldEditor(
//					"overriddenFieldEditor","overridden",panel);
//		overriddenFieldEditor.setFieldValueProvider(createOverridenProvider());
//		
//		headerdd= new Label(panel,SWT.NONE);
//		setButton= new Button(panel,SWT.NONE);
//		setButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
//		setButton.setText("set");
//		setButton.addSelectionListener(new SelectionAdapter() 
//				{
//					public void widgetSelected(SelectionEvent e) 
//					{	
//						saveEdit();
//					}
//				});
//		//top.update();
	}

	public Control getControl() 
	{
		return top;
	}

	public void setFocus() 
	{
		
	}

	
	
	public PSOverridingRule getOverrindingRule() 
	{
		//return overrindingRule;
		return (PSOverridingRule)orControl.getModelObject();
	}

	public void setOverrindingRule(PSOverridingRule overrindingRule) 
	{
		orControl.setOverridingRule(overrindingRule);
//		if(overrindingRule==null)
//		{
//			return;
//		}
//		try {
//			System.out.println("overrindingRule:"+overrindingRule);
//			this.overrindingRule = overrindingRule;
//			PSPolicy psPol=overrindingRule.getHasOverridden();
//			System.out.println("overrindingRulePSPol:"+psPol);
//			if(psPol!=null)
//			{
//				overriddenFieldEditor.setStringValue(
//									psPol.getLabel().getValue());	
//			}
//			else
//			{
//				overriddenFieldEditor.setStringValue("");
//			}
//			
//			psPol=overrindingRule.getHasOverridder();
//			System.out.println("overrindingRulePSPol:"+psPol);
//			if(psPol!=null)
//			{
//				overridderFieldEditor.setStringValue(
//									psPol.getLabel().getValue());
//			}
//			else
//			{
//				overridderFieldEditor.setStringValue("");
//			}
//			
//			labelFieldEditor.setStringValue(
//					overrindingRule.getLabel().getValue());
//			selectedOverridden=null;
//			selectedOverridder=null;
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		
	}

//	private void saveEdit()
//	{
//		try {
//			if(overrindingRule==null)
//			{
//				return;
//			}
//			
//			String overriddenLabel=
//				overriddenFieldEditor.getStringValue();
//			String overridderLabel=
//				overridderFieldEditor.getStringValue();
//			String ruleLabel=labelFieldEditor.getStringValue();
//			if(ruleLabel!=null || !"".equals(ruleLabel))
//			{
//				overrindingRule.setLabel(ruleLabel);
//			}
//			
//			if(selectedOverridden==null || selectedOverridder==null)
//			{
//				PolicysystemPlugin.getDefault().showMessage(
//						"Selection incomplete:"+
//						"\n\t:overridder:"+selectedOverridder+
//						"\n\t:overridden:"+selectedOverridden);
//				return;
//			}
//			
//			CkeckOverridingRulePoliciesNeq check=
//				new CkeckOverridingRulePoliciesNeq(
//										selectedOverridder,
//										selectedOverridden);
//			if(check.doCheck()==false)
//			{
//				PolicysystemPlugin.getDefault().showMessage(
//						"Overridding and overridden policies must not be equals");
//				return;
//			}
//			
//			if(overriddenLabel==null || overridderLabel==null)
//			{
//				return;
//			}
//			
//			if(selectedOverridden!=null)
//			{
//				overrindingRule.setHasOverriden(selectedOverridden);
//			}
//			
//			if(selectedOverridder!=null)
//			{
//				overrindingRule.setHasOverrider(selectedOverridder);	
//			}
//			
////			String ruleLabel=labelFieldEditor.getStringValue();
////			if(ruleLabel!=null || !"".equals(ruleLabel))
////			{
////				overrindingRule.setLabel(ruleLabel);
////			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
	
//	private FieldValueProvider createOverridenProvider()
//	{
//		FieldValueProvider pv=
//			new FieldValueProvider()
//			{
//
//				public String getFieldValue() 
//				{
//					try {
//						DialogPage p;
//						String pageName="Choose overridden policy";
//						ChooserWizard wiz=new ChooserWizard(pageName);
//						ChooserWizardPage page=
//							new ChooserWizardPage(pageName,PSPolicy.class);
//						wiz.addPage(page);
//						//logger.info("SHELLLLLLLLLLLLLL:"+parent.getShell());
//						WizardDialog dlg=
//							new WizardDialog(
//									parent.getShell(),
//									wiz);
//						
//						int resp=dlg.open();
//						PSPolicy sel=
//							(PSPolicy)wiz.getSelected();
//						if(sel==null)
//						{
//							logger.warn("nothing selected returning null");
//							return null;
//						}
//						//overrindingRule.setHasOverriden(sel);
//						selectedOverridden=sel;
//						
//						String label=sel.getLabel().getValue();
//						logger.info("Selected:"+sel);
//						return label;
//					} catch (Exception e) {
//						e.printStackTrace();
//						return null;
//					}
//				}
//			
//			};
//		return pv;
//	}
	
//	private FieldValueProvider createOverriderProvider()
//	{
//		FieldValueProvider pv=
//			new FieldValueProvider()
//			{
//
//				public String getFieldValue() 
//				{
//					try {
//						DialogPage p;
//						String pageName="Choose overriding policy";
//						ChooserWizard wiz=new ChooserWizard(pageName);
//						ChooserWizardPage page=
//							new ChooserWizardPage(pageName,PSPolicy.class);
//						wiz.addPage(page);
//						//logger.info("SHELLLLLLLLLLLLLL:"+parent.getShell());
//						WizardDialog dlg=
//							new WizardDialog(
//									parent.getShell(),
//									wiz);
//						
//						int resp=dlg.open();
//						PSPolicy sel=
//							(PSPolicy)wiz.getSelected();
//						if(sel==null)
//						{
//							logger.warn("nothing selected returning null");
//							return null;
//						}
//						//overrindingRule.setHasOverrider(sel);
//						selectedOverridder=sel;
//						String label=sel.getLabel().getValue();
//						logger.info("Selected:"+sel);
//						return label;
//					} catch (Exception e) {
//						e.printStackTrace();
//						return null;
//					}
//				}
//			
//			};
//		return pv;
//	}
	
//	public class ChooserWizard extends Wizard
//	{
//		private Object selected;
//		public ChooserWizard()
//		{
//			
//		}
//		public boolean performFinish() 
//		{
//			selected=((ChooserWizardPage)
//						getPage("choose policy")).getSelected();;
//			return true;
//		}
//		public Object getSelected() {
//			return selected;
//		}
//		
//	}
	
//	private StringButtonFieldEditor createLabelEditor(
//							String name,//"labelFieldEditor",
//							String labelStext,//"Label",
//							Composite parent//panel
//							)
//	{
//		StringButtonFieldEditor ed= 
//			new StringButtonFieldEditor(name,labelStext,parent)
//			{
//	
//				protected String changePressed() {
//					String label=labelFieldEditor.getStringValue();
//					if(label!=null)
//					{
//						overrindingRule.setLabel(label);
//					}
//					return label;
//				}
//				
//			};
//		return ed;
//	}
//
//	/**
//	 * @see org.eclipse.jface.viewers.IDoubleClickListener#doubleClick(org.eclipse.jface.viewers.DoubleClickEvent)
//	 */
//	public void doubleClick(DoubleClickEvent event) 
//	{
//		ISelection sel=event.getSelection();
//		if(sel==null)
//		{
//			return;
//		}
//		if(sel instanceof IStructuredSelection)
//		{
//			IStructuredSelection ssel= (IStructuredSelection)sel;
//			Object ele0=ssel.getFirstElement();
//			if(ele0==null)
//			{
//				logger.warn("first selection is null");
//				return;
//			}
//			else if(ele0 instanceof PSPolicy)
//			{
//				PSModelObjectEditDialog dlg=
//					new PSModelObjectEditDialog(parent.getShell(),PSPolicy.class);
//				dlg.create();
//				dlg.setModelObject((PSPolicy)ele0);
//				dlg.open();
//			}
//			else if(ele0 instanceof PSOverridingRule)
//			{
//				logger.warn("no impl for PSOverrindingrule");
//			}
//			else
//			{
//				logger.warn(
//					"Cannot handle this kind of selection element:"+ele0);
//			}
//		}
//		else
//		{
//			logger.warn("Cannot handle this kind auf selection:"+sel);
//		}
//		
//	}
	
	
	
}
