package policysystem.views;

import java.io.File;
import java.io.FileFilter;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
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

public class FileSystemView extends ViewPart
							implements ISelectionListener
{
	static final public String ID="FileSystemView";
	private ITreeContentProvider contentProvider;
	private TreeViewer treeView;
	
	public void createPartControl(Composite parent) 
	{
		treeView= new TreeViewer(parent);
		contentProvider= new FileContentProvider(true);
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

	public void setFocus() {
		
	}
	
	///////////////////////////////////////////////////////////////////
	////////////////////SELCTION LISTENER//////////////////////////////
	///////////////////////////////////////////////////////////////////

	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
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
					String rootDir=
						ProjectConfig.getInstance().getProperty("rootDir");
//					if(rootDir==null)
//					{
//						return;
//					}
					treeView.setInput(rootDir);
				}
				else
				{
					treeView.setInput(null);
				}
			}
			
			//System.out.println("dadadadad:"+el);
		}
	}
	
	///////////////////////////////////////////////////////////////////
	///////////////CONTENT PROVIDER////////////////////////////////////
	///////////////////////////////////////////////////////////////////
	/**
     * Content provider for java.io.File objects.
     */
    private static class FileContentProvider implements ITreeContentProvider 
    {
        private static final Object[] EMPTY = new Object[0];

        private FileFilter fileFilter;

        /**
         * Creates a new instance of the receiver.
         * 
         * @param showFiles <code>true</code> files and folders are returned
         * 	by the receiver. <code>false</code> only folders are returned.
         */
        public FileContentProvider(final boolean showFiles) {
            fileFilter = new FileFilter() {
                public boolean accept(File file) {
                    if (file.isFile() && showFiles == false)
                        return false;
                    return true;
                }
            };
        }

        public Object[] getChildren(Object parentElement) 
        {
            if (parentElement instanceof File) {
                File[] children = ((File) parentElement).listFiles(fileFilter);
                if (children != null) {
                    return children;
                }
            }
            else if (parentElement instanceof String) 
            {
            	System.out.println("test:****************************");
            	return new File[]{new File((String)parentElement)};
            }
            
            	
            return EMPTY;
        }

        public Object getParent(Object element) {
            if (element instanceof File) {
                return ((File) element).getParentFile();
            }
            return null;
        }

        public boolean hasChildren(Object element) {
            return getChildren(element).length > 0;
        }

        public Object[] getElements(Object element) {
            return getChildren(element);
        }

        public void dispose() {
        }

        public void inputChanged(
        						Viewer viewer, 
        						Object oldInput, 
        						Object newInput) 
        {
        	
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

        private static final Image IMG_FILE = PlatformUI.getWorkbench()
                .getSharedImages().getImage(ISharedImages.IMG_OBJ_FILE);

        public Image getImage(Object element) {
            if (element instanceof File) {
                File curr = (File) element;
                if (curr.isDirectory()) {
                    return IMG_FOLDER;
                } else {
                    return IMG_FILE;
                }
            }
            return null;
        }

        public String getText(Object element) {
            if (element instanceof File) {
                return ((File) element).getName();
            }
            return super.getText(element);
        }
    }


    
}
