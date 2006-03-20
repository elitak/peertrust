package policysystem.control;

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.DialogPage;
import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
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

import com.hp.hpl.jena.query.util.LabelToNodeMap;

import policysystem.model.abtract.PSOverrindingRule;
import policysystem.model.abtract.PSPolicy;

public class PSOverriddingRuleEditorPage extends Page 
{
	private Composite parent;
	private Composite top;
	private PSOverrindingRule overrindingRule;
	private StringFieldEditor labelFieldEditor;
	private StringButtonFieldEditor overridderFieldEditor;
	private StringButtonFieldEditor overriddenFieldEditor;
	private Button setButton;
	static private Logger logger= Logger.getLogger(PSOverriddingRuleEditorPage.class);
	private PSPolicy selectedOverridden=null;
	private PSPolicy selectedOverridder=null;
	
	public void createControl(Composite parent) 
	{
		System.out.println("SSSSSSSSSSSSSSSSSSSS:"+parent.getShell());
		this.parent=parent;
		top = new Composite(parent, SWT.LEFT);
		//top.setLayout(new GridLayout());
		///////////////////////////////////////////////////////
		Control c;
		Label headerdd= new Label(top,SWT.NONE);
		
		Label header= new Label(top,SWT.BORDER|SWT.HORIZONTAL|SWT.FILL);
		header.setText("Overridding");
		header.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		headerdd= new Label(top,SWT.NONE);
		Composite panel=
			top;//new Group(top);//new Composite(top,SWT.NONE);
		
		//labelFieldEditor=createLabelEditor("labelFieldEditor","Label",panel);
		labelFieldEditor= new StringFieldEditor("labelFieldEditor","Label",panel);
		headerdd= new Label(top,SWT.NONE);
//		
//			new StringButtonFieldEditor(
//					"labelFieldEditor","Label",panel);
		
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
		
		headerdd= new Label(top,SWT.NONE);
		setButton= new Button(top,SWT.NONE);
		setButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		setButton.setText("set");
		setButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {	
				saveEdit();
			}
		});
		
		///
		
//		headerdd= new Label(top,SWT.NONE);
		
//		Combo combo=new Combo(top,SWT.NONE);
//		combo.setVisibleItemCount(1);
//		combo.set
	}

	public Control getControl() 
	{
		return top;
	}

	public void setFocus() 
	{
		
	}

	
	
	public PSOverrindingRule getOverrindingRule() 
	{
		return overrindingRule;
	}

	public void setOverrindingRule(PSOverrindingRule overrindingRule) 
	{
		if(overrindingRule==null)
		{
			return;
		}
		try {
			this.overrindingRule = overrindingRule;
			PSPolicy psPol=overrindingRule.getHasOverridden();
			if(psPol!=null)
			{
				overriddenFieldEditor.setStringValue(psPol.getLabel());	
			}
			
			psPol=overrindingRule.getHasOverridder();
			if(psPol!=null)
			{
				overridderFieldEditor.setStringValue(psPol.getLabel());
			}
			
			labelFieldEditor.setStringValue(overrindingRule.getLabel());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	private void saveEdit()
	{
		if(overrindingRule==null)
		{
			return;
		}
		
		String overriddenLabel=
			overriddenFieldEditor.getStringValue();
		String overridderLabel=
			overridderFieldEditor.getStringValue();
		if(overriddenLabel==null || overridderLabel==null)
		{
			return;
		}
		
		if(selectedOverridden!=null)
		{
			overrindingRule.setHasOverriden(selectedOverridden);
		}
		
		if(selectedOverridder!=null)
		{
			overrindingRule.setHasOverrider(selectedOverridder);
		}
		String ruleLabel=labelFieldEditor.getStringValue();
		if(ruleLabel!=null || !"".equals(ruleLabel))
		{
			overrindingRule.setLabel(ruleLabel);
		}
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
						//overrindingRule.setHasOverriden(sel);
						selectedOverridden=sel;
						
						String label=sel.getLabel();
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
						//overrindingRule.setHasOverrider(sel);
						selectedOverridder=sel;
						String label=sel.getLabel();
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
						overrindingRule.setLabel(label);
					}
					return label;
				}
				
			};
		return ed;
	}
}
