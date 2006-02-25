package policysystem.views;

import java.io.File;
import java.io.FileFilter;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import policysystem.model.PolicySystemRDFModel;
import policysystem.model.PolicySystemResTreeContentProvider;
import policysystem.model.ProjectConfig;

/**
 * Content provider for java.io.File objects.
 */
public class PSResourceViewContentProvider implements ITreeContentProvider 
{
    private  static final Object[] EMPTY = new Object[0];

    private FileFilter fileFilter;
    private Logger logger;

    /**
     * Creates a new instance of the receiver.
     * 
     * @param showFiles <code>true</code> files and folders are returned
     * 	by the receiver. <code>false</code> only folders are returned.
     */
    public PSResourceViewContentProvider(final boolean showFiles) {
       logger=Logger.getLogger(PSResourceViewContentProvider.class);
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
    	if(parentElement==null)
    	{
    		return EMPTY;
    	}
        if (parentElement instanceof File) {
            File[] children = ((File) parentElement).listFiles(fileFilter);
            if (children != null) {
                return children;
            }
        }
        else if (parentElement instanceof String) 
        {
        	PolicySystemRDFModel modelImpl=PolicySystemRDFModel.getInstance();
        	
        	if(((String)parentElement).equals(
        			PolicySystemResTreeContentProvider.POLICY_SYSTEM_RES_RESOURCES))
        	{
        		String rootDir=
					ProjectConfig.getInstance().getProperty("rootDir");
        		return new File[]{new File(rootDir)};
        	}
        	else if(
        			((String)parentElement).equals(
        				PolicySystemResTreeContentProvider.POLICY_SYSTEM_RES_POLICIES))
        	{
        		return PolicySystemRDFModel.getInstance().getPolicies().toArray();
        	}
        	else if(
        			((String)parentElement).equals(
        				PolicySystemResTreeContentProvider.POLICY_SYSTEM_RES_OVERRIDDING_RULES))
        	{
        		Vector oRules=modelImpl.getOverriddingRules(null);
        		
        		return oRules.toArray();
        	}
        	else if(
        			((String)parentElement).equals(
        				PolicySystemResTreeContentProvider.POLICY_SYSTEM_RES_FILTERS))
        	{
        		Vector oRules=modelImpl.getFilters(null);
        		
        		return oRules.toArray();
        	}
        	else
        	{
        			
        	}
        }
        logger.warn(
        	"Cannot handle this object returning empty array:"+
        	"\n\tparent.....="+parentElement+
        	"\n\tparentClass="+parentElement.getClass());
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