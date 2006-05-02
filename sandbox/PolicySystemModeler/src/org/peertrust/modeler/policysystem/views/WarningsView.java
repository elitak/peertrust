package org.peertrust.modeler.policysystem.views;

import java.io.File;
import java.io.IOException;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.ContentViewer;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.ViewPart;
import org.peertrust.modeler.policysystem.PolicysystemPlugin;
import org.peertrust.modeler.policysystem.model.FileResourceSelector;
import org.peertrust.modeler.policysystem.model.PolicySystemRDFModel;
import org.peertrust.modeler.policysystem.model.ProjectConfig;
import org.peertrust.modeler.policysystem.model.abtract.PSFilter;
import org.peertrust.modeler.policysystem.model.abtract.PSOverridingRule;
import org.peertrust.modeler.policysystem.model.abtract.PSPolicy;
import org.peertrust.modeler.policysystem.model.abtract.PSResource;
import org.peertrust.modeler.policysystem.model.abtract.PSResourceIdentityMaker;


public class WarningsView 	extends ViewPart 
							implements ISelectionListener
{

	/**
	 * The ID for this view
	 */
	static final public String ID="org.peertrust.modeler.policysystem.WarningsView";
	//ContentViewer view;
	
	/**
	 * The view used to show the model object lists
	 */
	//ListViewer listViewer;
	TreeViewer listViewer;
	/**
	 * Content provider for the list view
	 */
	private IStructuredContentProvider contentProvider;
	
	/**
	 * The logger for the this class
	 */
	static private final Logger logger= Logger.getLogger(WarningsView.class);
	
	/**
	 * the plicy system implementation
	 */
	private PolicySystemRDFModel psSystem;
	
	private PSResourceIdentityMaker identityMaker;
	
	private PSPolicySystemLabelProvider labelProvider;
	
	private ProjectConfig projectConfig;//=.getInstance()
	
	public WarningsView() 
	{
		super();
		psSystem=PolicySystemRDFModel.getInstance();
		PolicysystemPlugin.getDefault().getImageRegistry();
		identityMaker=psSystem.getPSResourceIdentityMaker(File.class);
		projectConfig=ProjectConfig.getInstance();
	}

	public void createPartControl(Composite parent) 
	{
			//listViewer= new ListViewer(parent);
			listViewer= new TreeViewer(parent);
			
//			contentProvider=makeContentProvider();
//			listViewer.setContentProvider(contentProvider);
			listViewer.setContentProvider(makeITreeCP());
			///
			labelProvider= new PSPolicySystemLabelProvider();
			listViewer.setLabelProvider(labelProvider);
			
			getSite().getPage().addSelectionListener(
					PSResourceView.ID,
					(ISelectionListener)this);
			getSite().getPage().addSelectionListener(
					PolicySystemView.ID,
					(ISelectionListener)this);
	}

	public void setFocus() 
	{
	
	}

	private ITreeContentProvider makeITreeCP()
	{
		ITreeContentProvider cp= new ITreeContentProvider()
		{

			public Object[] getChildren(Object parentElement) 
			{
				return null;
			}

			public Object getParent(Object element) {
				return null;
			}

			public boolean hasChildren(Object element) 
			{
				return false;
			}

			public Object[] getElements(Object inputElement) 
			{
				if(inputElement==null)
				{
					return new Object[0];
				}
				else if(inputElement instanceof PSResource)
				{
					Vector pols=
						//PolicySystemRDFModel.getInstance()
						psSystem.getInheritedPolicies(
											(PSResource)inputElement);
					return pols.toArray();
				}
				else if(inputElement instanceof PSPolicy)
				{
					Vector pols=
						psSystem.getLinkedModelObjects(
													(PSPolicy)inputElement,
													null);
					return pols.toArray();
				}
				else if(inputElement instanceof PSFilter)
				{
					Vector pols=
						psSystem.getLinkedModelObjects(
													(PSFilter)inputElement,
													null);
					return pols.toArray();
				}
				else if(inputElement instanceof PSOverridingRule)
				{
					Vector pols=
						psSystem.getLinkedModelObjects(
												(PSOverridingRule)inputElement,
												null);
					return pols.toArray();
				}
				else if(inputElement instanceof Vector)
				{
//					Vector pols=
//						psSystem.computePathPolicies(
//										(Vector)inputElement);
//					return pols.toArray();
					throw new RuntimeException("Path input not suppoerted:"+inputElement);
				}
				else
				{
					return new Object[0];
				}
				//return null;
			}

			public void dispose() {
				//empty				
			}

			public void inputChanged(
							Viewer viewer, 
							Object oldInput, 
							Object newInput) 
			{
				//empty				
			}
			
		};
		
		return cp;
	}
	
	private IStructuredContentProvider makeContentProvider()
	{
		IStructuredContentProvider cp= new IStructuredContentProvider()
		{

			public Object[] getElements(Object inputElement) {
				if(inputElement==null)
				{
					return new Object[0];
				}
				else if(inputElement instanceof PSResource)
				{
					Vector pols=
						psSystem.getInheritedPolicies(
										(PSResource)inputElement);
					return pols.toArray();
				}
				else if(inputElement instanceof PSPolicy)
				{
					Vector pols=
						psSystem.getLinkedModelObjects(
								(PSPolicy)inputElement,
								null);
					return pols.toArray();
				}
				else if(inputElement instanceof PSFilter)
				{
					Vector pols=
						psSystem.getLinkedModelObjects(
											(PSFilter)inputElement,
											null);
					return pols.toArray();
				}
				else if(inputElement instanceof PSOverridingRule)
				{
					Vector pols=
						psSystem.getLinkedModelObjects(
											(PSOverridingRule)inputElement,
											null);
					return pols.toArray();
				}
				else if(inputElement instanceof Vector)
				{
//					Vector pols=
//						psSystem.computePathPolicies(
//										(Vector)inputElement);
//					return pols.toArray();
					throw new RuntimeException("Path input not supported:"+inputElement);
				}
				else
				{
					return new Object[0];
				}
			}

			public void dispose() 
			{
				
			}

			public void inputChanged(
								Viewer viewer, 
								Object oldInput, 
								Object newInput) 
			{
				
			}

			
		};
		return cp;
	}
	///////////////////////////////////////////////////////////////////////////
	//////////////////////ISelectionListener//////////////////////////////////
	//////////////////////////////////////////////////////////////////////////
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		if(part==null || selection==null)
		{
			listViewer.setInput(null);
		}
		
		if(!(selection instanceof IStructuredContentProvider))
		{
			listViewer.setInput(null);
		}
		Object sel=((IStructuredSelection)selection).getFirstElement();
		if(part instanceof PolicySystemView)
		{
			listViewer.setInput(null);
		}
		else if(part instanceof PSResourceView)
		{
			if(sel instanceof File)
			{
				//TODO check algorithm
				
				try {
					File file=(File)sel;
					String root=
						projectConfig.getProperty(ProjectConfig.ROOT_DIR);
					if(root==null)
					{
						return;
					}
					File rootFile=new File(root);
					root=rootFile.getAbsolutePath();					
					File parentFile=file;
					logger.info("parentFile:"+parentFile+
								"\n....root:"+rootFile);
					Vector path=new Vector();
					PSResource pRes=null,tmpRes;
					while(parentFile.getAbsolutePath().startsWith(root))
					{
						logger.info("parentFile:"+parentFile+
									"\nroot+++++:"+rootFile);
						parentFile=parentFile.getParentFile();
						
						tmpRes= 
							psSystem.getPSResource(parentFile,true);
						if(pRes!=null)
						{
							if(tmpRes!=null)
							{
								if(tmpRes.getHasSuper()!=null && !(parentFile.equals(root)))
								{//no super set yet and not roo
									tmpRes.addHasSuper(pRes);
								}
							}
						}
						pRes=tmpRes;
						if(pRes!=null)
						{
							path.add(0,pRes);
						}
						
					}
					logger.info("Pathhh:"+path);
					
					PSResource res=
						psSystem.getPSResource(file,true);
					path.addElement(res);
					//listViewer.setInput(path);
					//TODO remove path computation
					listViewer.setInput(res);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			else if(sel instanceof PSPolicy)
			{
				listViewer.setInput((PSPolicy)sel);
			}
			else if(sel instanceof PSFilter)
			{
				listViewer.setInput((PSFilter)sel);
			}
			else if(sel instanceof PSOverridingRule)
			{
				listViewer.setInput((PSOverridingRule)sel);
			}
			else
			{
				listViewer.setInput(null);
			}
		}
		else
		{
			listViewer.setInput(null);
		}
		
	}
	
	
	
	
}
