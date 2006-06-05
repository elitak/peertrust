package org.peertrust.modeler.policysystem.gui.views;

import java.io.File;

import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.Page;
import org.eclipse.ui.part.PageBook;
import org.eclipse.ui.part.ViewPart;
import org.peertrust.modeler.policysystem.gui.control.NewProjectDlgEditor;
import org.peertrust.modeler.policysystem.gui.control.PSFilterEditorPage;
import org.peertrust.modeler.policysystem.gui.control.PSHierarchyVisualizationPage;
import org.peertrust.modeler.policysystem.gui.control.PSOverriddingRuleEditorPage;
import org.peertrust.modeler.policysystem.gui.control.PSPolicyEditorPage;
import org.peertrust.modeler.policysystem.gui.control.PSResourcePolicyEditorPage;
import org.peertrust.modeler.policysystem.model.abtract.PSFilter;
import org.peertrust.modeler.policysystem.model.abtract.PSOverridingRule;
import org.peertrust.modeler.policysystem.model.abtract.PSPolicy;
import org.peertrust.modeler.policysystem.model.abtract.PSResource;



/**
 * A view that that shows a resource filters and overridding rule
 * and provide interface to attach and dettach filters and overridding 
 * rule.
 *  
 * @author Patrice Congo
 *
 */
public class PSResourcePolicyView extends ViewPart 
				implements ISelectionListener
{
	static final public String ID="org.peertrust.modeler.policysystem.ResourcePolicyView";
	
	//private TableViewer localPolicyView;
	
	//private TableViewer inheritedPolcyView;
	
	private NewProjectDlgEditor pjtEd;
	
	/**
	 * A blank page used as default page
	 */
	private Page blankPage;
	
	/**
	 * The page showing the graphical editor
	 */
	private PSHierarchyVisualizationPage resPolViz;
	
	/**
	 * The page showing the overriding rule editor page
	 */
	private PSOverriddingRuleEditorPage overriddingRuleEditorPage;
	
	/**
	 * The <code>PSResourcePolicyView</code> class logger
	 */
	private static final Logger logger=
						Logger.getLogger(PSResourcePolicyView.class);
	
	/**
	 * The view page book
	 */
	private PageBook pageBook;
	
	/**
	 * A page showing the graphical editor
	 */
	private PSHierarchyVisualizationPage vizPage;
	
	/**
	 * The page showing the policy editor
	 */
	private PSPolicyEditorPage policyEditorPage;
	
	/**
	 * The page showing the resource policy editor
	 */
	private PSResourcePolicyEditorPage resourcePolicyEditorPage;
	
	/**
	 * The page showing the filter editor
	 */
	private PSFilterEditorPage filterEditorPage;
	
	
	public PSResourcePolicyView() 
	{
		super();		
	}

	/**
	 * @see org.eclipse.ui.IWorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createPartControl(Composite parent) 
	{
		pageBook= new PageBook(parent,SWT.NONE);
		
		getSite().getPage().addSelectionListener(
				PSResourceView.ID,
				(ISelectionListener)this);
		getSite().getPage().addSelectionListener(
				PolicySystemView.ID,
				(ISelectionListener)this);
		pjtEd=new NewProjectDlgEditor();
		pjtEd.createControl(pageBook);
		
		resPolViz= new PSHierarchyVisualizationPage();
		resPolViz.createControl(pageBook);
		
		vizPage= new PSHierarchyVisualizationPage();
		vizPage.createControl(pageBook);
		
		blankPage=createBlankViewPage();
		blankPage.createControl(pageBook);
		
		policyEditorPage= new PSPolicyEditorPage();
		policyEditorPage.createControl(pageBook);
		
		overriddingRuleEditorPage=
			new PSOverriddingRuleEditorPage();
		overriddingRuleEditorPage.createControl(pageBook);
		//pageBook.showPage(blankPage.getControl());
		//pageBook.showPage(overriddingRuleEditorPage.getControl());
		resourcePolicyEditorPage=
			new PSResourcePolicyEditorPage();
		resourcePolicyEditorPage.createControl(pageBook);
		
		filterEditorPage= new PSFilterEditorPage();
		filterEditorPage.createControl(pageBook);
		
		pageBook.showPage(resourcePolicyEditorPage.getControl());
		
	}

	public void setFocus() 
	{
		//empty
	}

	
//	private void showMessage(String message) {
//		MessageDialog.openInformation(
//			this.getSite().getShell(),
//			"PolicySystemView",
//			message);
//	}

	/**
	 * @see org.eclipse.ui.ISelectionListener#selectionChanged(org.eclipse.ui.IWorkbenchPart, org.eclipse.jface.viewers.ISelection)
	 */
	public void selectionChanged(IWorkbenchPart part, ISelection selection) 
	{
		
		if(selection==null)
		{
			logger.warn("param selection is null");
			return;
		}
		
		Object sel0=((StructuredSelection)selection).getFirstElement();
		
		if(sel0==null)
		{
			logger.warn("selection first element is null");
			return;
		}
		
		logger.info("==>Selection changed:"+
				"\n\tpart="+part +
				"\n\tselection="+selection +
				"\n\tselectionclass:"+sel0.getClass());
		
		try {
			
			
			if(part instanceof PolicySystemView)
			{
				pageBook.showPage(blankPage.getControl());
			}
			else if(sel0 instanceof PSPolicy)
			{			
					
					if(policyEditorPage!=null)
					{
						pageBook.showPage(policyEditorPage.getControl());
						policyEditorPage.setPsPolicy(
								(PSPolicy)sel0);
					}
			}
			else if(sel0 instanceof PSOverridingRule)
			{
				pageBook.showPage(overriddingRuleEditorPage.getControl());
				overriddingRuleEditorPage.setOverrindingRule(
						(PSOverridingRule)sel0);
			}
			else if(sel0 instanceof PSFilter)
			{
				pageBook.showPage(filterEditorPage.getControl());
				filterEditorPage.setPSFilter((PSFilter)sel0);
			}
			else if(sel0 instanceof File)
			{
				File selFile= (File)sel0;
//				if(selFile.isFile())
//				{
//					int decision=askYesNoQuestion(
//						"You have selected a file not a directory.\n"+
//						"Do you want show the parent directory",
//						part.getSite().getShell());
//					if(decision==SWT.YES)
//					{
//						selFile=selFile.getParentFile();
//					}
//					else
//					{
//						return;
//					}
//				}
				
//			localPolicyView.setInput(selFile);
//			pageBook.showPage(localPolicyView.getControl());
				resourcePolicyEditorPage.setInput(selFile);
				pageBook.showPage(resourcePolicyEditorPage.getControl());
			}
			else if(sel0 instanceof PSResource)
			{
				resourcePolicyEditorPage.setInput(sel0);
				pageBook.showPage(resourcePolicyEditorPage.getControl());
			}
			else
			{
				//logger.warn("Cannoet handle selection of this class:"+sel0.getClass());
				pageBook.showPage(blankPage.getControl());
			}
		} 
		catch (Exception e) 
		{
			logger.warn("could nor change view according to selection:",e);
		}
		
		return;
	}
	
	/**
	 * Ask a yes no question to the user.
	 * 
	 * @param message 
	 * @param parent
	 * @return  
	 */
	static final int askYesNoQuestion(String message, Shell parent)
	{
		int ret=SWT.CANCEL;
		if(message==null || parent==null)
		{
			throw new IllegalArgumentException(
					"params must not be null: message= "+message +" parent="+parent);
		}
		MessageBox mb=
				new MessageBox(parent, SWT.YES|SWT.NO|SWT.CANCEL);
		mb.setMessage(message);
		ret=mb.open();
		return ret;
	}
////////////////////////////////////////////////////////////////////////////////
//////////////////page book/////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
	public static final Page createBlankViewPage()
	{
		return new Page()
		{
			Composite c;
			
			public void createControl(Composite parent) 
			{
				c= new Composite(parent,SWT.NONE);
			}

			public Control getControl() 
			{
				return c;
			}

			public void setFocus() 
			{	
				//empty
			}
			
		};
	}
	
}

