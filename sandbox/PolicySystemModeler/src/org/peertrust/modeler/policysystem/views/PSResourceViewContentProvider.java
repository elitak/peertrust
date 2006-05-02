package org.peertrust.modeler.policysystem.views;

import java.io.File;
import java.io.FileFilter;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.peertrust.modeler.policysystem.model.PolicySystemRDFModel;
import org.peertrust.modeler.policysystem.model.PolicySystemResTreeContentProvider;
import org.peertrust.modeler.policysystem.model.ProjectConfig;
import org.peertrust.modeler.policysystem.model.abtract.PSResource;


/**
 * Content provider for the FileSystem view.
 * A customization for returning only directories 
 * or directories and files can be made by the constuctor showFiles 
 * Parameter  
 * 
 */
public class PSResourceViewContentProvider implements ITreeContentProvider 
{
	/** empty array*/
	private  static final Object[] EMPTY = new Object[0];

	/**
	 * a boolean thats indicates whether to show only directories if 
	 * false or true if file are also content elements
	 */
	private boolean showFiles;
	
	private Object root;
	
	/**
	 * Filter used for showing excusively directory or not
	 * it uses showFile member field to make the decision
	 */
    private FileFilter fileFilter=
    	new FileFilter() 
    	{
	        public boolean accept(File file) {
	            if (file.isFile() && showFiles == false)
	                return false;
	            return true;
	        }
    	};
    
    /**
     * Logger for the PSResourceViewprovider class
     */
    private static Logger logger=
    	Logger.getLogger(PSResourceViewContentProvider.class);;

    private PolicySystemRDFModel psModel=PolicySystemRDFModel.getInstance();
    
    /**
     * Creates a new instance of the receiver.
     * 
     * @param showFiles <code>true</code> files and folders are returned
     * 	by the receiver. <code>false</code> only folders are returned.
     */
    public PSResourceViewContentProvider(final boolean showFiles) 
    {
       
    	fileFilter = new FileFilter() {
            public boolean accept(File file) {
                if (file.isFile() && showFiles == false)
                    return false;
                return true;
            }
        };
    }

    /**
     * @return 
     * <ul>
     * 	<li/>	an Empty array if parentElement is null
     * 	<li/>	if parent element is a file returns
     * 			<ul/> 
     * 				<li/> its childrens if the file is a directory
     * 				<li/> an empty array otherwise
     * 			<ul/>
     *  <li/> if it is a string (representing the model object class to show)
     *  		<ul>
     *  			<li/> for POLICY_SYSTEM_RES_RESOURCES returns the root directory 
     *  			<li/> for <code>POLICY_SYSTEM_RES_POLICIES</code> returns all policies
     *  			<li/>for <code>POLICY_SYSTEM_RES_OVERRIDDING_RULES</code> returns all overriding rules
     *  			<li/>for <code>POLICY_SYSTEM_RES_FILTERS</code> return the all filters
     *  			<li/>otherwise returns an empty array 
     *  		</ul>	 
     * 	<li/>	
     * 	</ul>		
     * @see org.eclipse.jface.viewers.ITreeContentProvider#getChildren(java.lang.Object)
     */
    public Object[] getChildren(Object parentElement) 
    {
    	if(parentElement==null)
    	{
    		return EMPTY;
    	}
        
    	if (parentElement instanceof File) 
        {
    		
            File[] children = ((File) parentElement).listFiles(fileFilter);
            if (children != null) 
            {// a directory
                return children;
            }
            else
            {
            	return EMPTY;
            }
        }
    	else if(parentElement instanceof PSResource)
    	{
    		Vector children=((PSResource)parentElement).getChildren();
    		if(children==null)
    		{
    			return EMPTY;
    		}
    		else
    		{
    			return children.toArray();
    		}
    	}
        else if (parentElement instanceof String) 
        {
        	if(((String)parentElement).equals(
        			PolicySystemResTreeContentProvider.POLICY_SYSTEM_RES_RESOURCES))
        	{
        		String rootDir=
					ProjectConfig.getInstance().getProperty("rootDir");
        		File rootDirFile= new File(rootDir);
        		if(rootDirFile.exists())
        		{
        			return new File[]{new File(rootDir)};
        		}
        		else
        		{//return all ps resources without parent
        			Vector ress=psModel.getResources();
        			if(ress==null)
        			{
        				return null;
        			}
        			else
        			{
        				PSResource psRes;
        				PSResource resParent;
        				Vector roots=new Vector();
        				for(Iterator it=ress.iterator();it.hasNext();)
        				{
        					psRes=(PSResource)it.next();
        					resParent=psRes.getHasSuper();
        					if(resParent==null)
        					{//no super is a root
        						roots.add(psRes);
        					}
        					else
        					{
        						//empty
        					}        						
        				}
        				return roots.toArray();
        			}
        		}
        	}
        	else if(
        			((String)parentElement).equals(
        				PolicySystemResTreeContentProvider.POLICY_SYSTEM_RES_POLICIES))
        	{
        		return psModel.getPolicies().toArray();
        	}
        	else if(
        			((String)parentElement).equals(
        				PolicySystemResTreeContentProvider.POLICY_SYSTEM_RES_OVERRIDDING_RULES))
        	{
        		Vector oRules=psModel.getOverriddingRules(null);
        		
        		return oRules.toArray();
        	}
        	else if(
        			((String)parentElement).equals(
        				PolicySystemResTreeContentProvider.POLICY_SYSTEM_RES_FILTERS))
        	{
        		Vector oRules=psModel.getFilters(null);
        		
        		return oRules.toArray();
        	}
        	else
        	{
        		logger.warn("unknown string key for a model object class:"+parentElement);
        		return EMPTY;
        	}
        }
        logger.warn(
        	"Cannot handle this object returning empty array:"+
        	"\n\tparent.....="+parentElement+
        	"\n\tparentClass="+parentElement.getClass());
        return EMPTY;
    }

    /**
     * Only file element can have parent in the content model
     * @see org.eclipse.jface.viewers.ITreeContentProvider#getParent(java.lang.Object)
     */
    public Object getParent(Object element) 
    {
        if (element instanceof File) 
        {
            return ((File) element).getParentFile();
        }
        else if(element instanceof PSResource)
        {
        	PSResource parent=((PSResource)element).getHasSuper();
        	if(parent==null)
        	{//no parent a root
        		return root;
        	}
        	else
        	{
        		return parent;
        	}
//        	else if(parents.size()==1)
//        	{
//        		return parents.get(0);
//        	}
//        	else
//        	{
//        		logger.warn("Multiple inheritance for resource:"+element);
//        		return null;
//        	}
        		
        }
        else
        {
        	return root;//null;
        }
    }
    
    
    /**
     * @see org.eclipse.jface.viewers.ITreeContentProvider#hasChildren(java.lang.Object)
     */
    public boolean hasChildren(Object element) 
    {
        return getChildren(element).length > 0;
    }

    /**
     * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.Object)
     */
    public Object[] getElements(Object element) 
    {
    	root=element;
        return getChildren(element);
    }

    /**
     * @see org.eclipse.jface.viewers.IContentProvider#dispose()
     */
    public void dispose() 
    {
    	//nothing
    }

    /**
     * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
     */
    public void inputChanged(
    						Viewer viewer, 
    						Object oldInput, 
    						Object newInput) 
    {
    	root=newInput;//TODO test it
    }
}