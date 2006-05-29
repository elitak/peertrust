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
	}
	
	
}
