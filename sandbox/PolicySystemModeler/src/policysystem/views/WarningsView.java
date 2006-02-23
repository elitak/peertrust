package policysystem.views;

import java.io.File;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.ContentViewer;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.ViewPart;

import policysystem.model.FileResourceSelector;
import policysystem.model.PolicySystemRDFModel;
import policysystem.model.abtract.PSResource;

public class WarningsView 	extends ViewPart 
							implements ISelectionListener
{
	static final public String ID="policysystem.WarningsView";
	ContentViewer view;
	ListViewer listViewer;
	IStructuredContentProvider contentProvider;
	Logger logger= Logger.getLogger(WarningsView.class);
	
	public WarningsView() 
	{
		super();
	}

	public void createPartControl(Composite parent) 
	{
			listViewer= new ListViewer(parent);
			contentProvider=makeContentProvider();
			listViewer.setContentProvider(contentProvider);
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
						PolicySystemRDFModel.getInstance().getInheritedPolicies(
															(PSResource)inputElement);
					return pols.toArray();
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
				File file=(File)sel;
							
				PSResource res= 
					PolicySystemRDFModel.getInstance().getResource(
												file.toString(),
												true,
												new FileResourceSelector(file));
				listViewer.setInput(res);
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
