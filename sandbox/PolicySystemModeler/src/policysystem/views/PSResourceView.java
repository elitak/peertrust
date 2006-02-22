package policysystem.views;

import java.io.File;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IElementFactory;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.internal.ViewSite;
import org.eclipse.ui.internal.Workbench;
import org.eclipse.ui.internal.WorkbenchPlugin;
import org.eclipse.ui.internal.part.SiteComposite;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.part.WorkbenchPart;
import org.eclipse.ui.views.navigator.ResourceNavigator;

import policysystem.model.PolicySystemResTreeContentProvider;
import policysystem.model.ProjectConfig;
import policysystem.model.abtract.ModelObjectWrapper;
import policysystem.model.abtract.PSOverrindingRule;

public class PSResourceView extends ViewPart
							implements ISelectionListener
{
	static final public String ID="FileSystemView";
	private ITreeContentProvider contentProvider;
	private TreeViewer treeView;
	private Logger logger;
	
	public PSResourceView()
	{
		logger=Logger.getLogger(PSResourceView.class);
	}
	
	public void createPartControl(Composite parent) 
	{
		
		treeView= new TreeViewer(parent);
		contentProvider= new PSResourceViewContentProvider(true);
		treeView.setContentProvider(contentProvider);
		treeView.setLabelProvider(new FileLabelProvider());
		treeView.setInput(getViewSite());
		getSite().setSelectionProvider(treeView);
		getSite().getPage().addSelectionListener(
				PolicySystemView.ID,
				(ISelectionListener)this);
		
		//PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView();
		//Workbench.getInstance().getElementFactory();
		
	}

	public void setFocus() 
	{
		
	}
	
	///////////////////////////////////////////////////////////////////
	////////////////////SELCTION LISTENER//////////////////////////////
	///////////////////////////////////////////////////////////////////

	public void selectionChanged(IWorkbenchPart part, ISelection selection) 
	{
		//part.getSite().getPage().getViewReferences()[0].
		if(selection instanceof StructuredSelection)
		{
			Object el=
				((StructuredSelection)selection).getFirstElement();
			if(el instanceof String)
			{
				if(el.equals(
						PolicySystemResTreeContentProvider.POLICY_SYSTEM_RES_RESOURCES))
				{
//					String rootDir=
//						ProjectConfig.getInstance().getProperty("rootDir");
//					if(rootDir==null)
//					{
//						return;
//					}
					treeView.setInput(el);//new File(rootDir));
				}
				else if(el.equals(
						PolicySystemResTreeContentProvider.POLICY_SYSTEM_RES_POLICIES))
				{
					treeView.setInput(el);
				}
				else if(el.equals(
						PolicySystemResTreeContentProvider.POLICY_SYSTEM_RES_OVERRIDDING_RULES))
				{
					treeView.setInput(el);
				}
				else
				{
					treeView.setInput(null);
				}
			}
			
			//System.out.println("dadadadad:"+el);
		}
	}
	
	///////////////////////////////////////////////////////////////////////
    /////////////////////////LABEL PROVIDER////////////////////////////////
    ///////////////////////////////////////////////////////////////////////
    /**
     * Label provider for java.io.File objects.
     */
    private static class FileLabelProvider extends LabelProvider {
        private static final Image IMG_FOLDER = PlatformUI.getWorkbench()
                .getSharedImages().getImage(ISharedImages.IMG_OBJ_FOLDER);

        private static final Image IMG_FILE = 
        		PlatformUI.getWorkbench()
                		.getSharedImages().getImage(ISharedImages.IMG_OBJ_FILE);
        private static final Image IMG_MODEL_ELEMENT = 
				        	PlatformUI.getWorkbench()
				        		.getSharedImages().getImage(
				        				ISharedImages.IMG_OBJ_ELEMENT);
        
        public Image getImage(Object element) {
            if (element instanceof File) {
                File curr = (File) element;
                if (curr.isDirectory()) {
                    return IMG_FOLDER;
                } else {
                    return IMG_FILE;
                }
            }
            else if(element instanceof ModelObjectWrapper)
            {
            	return IMG_MODEL_ELEMENT;
            }
            return null;
        }

        public String getText(Object element) {
            if (element instanceof File) {
                return ((File) element).getName();
            }
            else if(element instanceof PSOverrindingRule)
            {
            	return ((PSOverrindingRule)element).getLabel();
            }
            else
            {
            	return super.getText(element);
            }
        }
    }


    
}
