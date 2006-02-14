/**
 * 
 */
package policysystem.model;

import java.io.File;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.eclipse.core.internal.runtime.Log;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Image;

import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;

/**
 * @author pat_dev
 *
 */
public class ResourcePolicyContentProvider 
		implements 	IStructuredContentProvider,
					ITableLabelProvider
{

	Logger logger;//= Logger.getLogger(ResourcePolicyContentProvider.class);
	
	public ResourcePolicyContentProvider()
	{
		logger= Logger.getLogger(ResourcePolicyContentProvider.class);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.Object)
	 */
	public Object[] getElements(Object inputElement) 
	{
		if(inputElement==null)
		{
			logger.warn("Param inputelement is null");
			return new Object[0];
		}
		
		
		logger.info("getElemnts:"+inputElement+ " class="+inputElement.getClass());
		if(inputElement instanceof File)
		{
			File file=(File)inputElement;
			if(file.isFile())
			{
				logger.warn("Cannot handle file["+file.toURI()+"]; directory expedted");
				return new Object[0];
			}
			
			PSResource res= PolicySystemRDFModel.getResource(file.toString(),true);
			
			Vector dirPolicies=res.getIsProtectedBy();
//					PolicySystemRDFModel.getMultipleProperty(
//						res,
//						PolicySystemRDFModel.PROP_IS_PROTECTED_BY);
			Vector filters = res.getHasFilter();
//					PolicySystemRDFModel.getMultipleProperty(
//							res,
//							PolicySystemRDFModel.PROP_HAS_FILTER);
			if(dirPolicies!=null)
			{
				if(filters!=null)
				{
					dirPolicies.addAll(filters);
				}
				return dirPolicies.toArray();
			}
			else
			{
				if(filters!=null)
				{
					return	filters.toArray();
				}
				return new Object[0];
			}
		}
		else
		{
			logger.warn("cannot handle this kind of input:"+inputElement);
			return new Object[0];
		}		
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.IContentProvider#dispose()
	 */
	public void dispose() {
		
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
	 */
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		
	}


	
	///////////////////////////////////////////////////////////////////////////
	//////////////////LABEL PROVIDER //////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////
	
	public Image getColumnImage(Object element, int columnIndex) 
	{
		logger.info("No immage for col:"+columnIndex);
		return null;
	}

	public String getColumnText(Object element, int columnIndex) 
	{
		if(element==null)
		{
			logger.warn("param element is null");
			return "";
		}
		
		if(element instanceof Resource)
		{
			Resource res=(Resource)element;
			if(PolicySystemRDFModel.isSubClassOf(
					res,PolicySystemRDFModel.CLASS_FILTER))
			{
				
				Statement stm= 
					res.getProperty(PolicySystemRDFModel.PROP_IS_PROTECTED_BY);
				return "fil"+columnIndex;
			}
			else if(PolicySystemRDFModel.isSubClassOf(
						res,PolicySystemRDFModel.CLASS_RESOURCE))
			{
				return "res"+columnIndex;
			}
			else
			{
				logger.warn("cannot handle this kind of resource:"+res);
				return "badRes"+columnIndex;
			}
		}
		else if(element instanceof PSPolicy)
		{
			return getPolicyTableCellLabel((PSPolicy)element,columnIndex);
		}
		else
		{
			logger.warn("cannot handle this kind of object:"+element);
			return "ColA"+columnIndex+" el:"+element;
		}
		
	}

	public void addListener(ILabelProviderListener listener) {
		
	}

	public boolean isLabelProperty(Object element, String property) {
		System.out.println("isLabelProperty");
		return false;
	}

	public void removeListener(ILabelProviderListener listener) {
		
	}


	/////////////////////////////////////////////////////////////////////////
	//////////////////UTIL///////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////
	static final private String getPolicyTableCellLabel(
										PSPolicy policy,
										int colIndex)
	{
		switch(colIndex)
		{
			case 0://name
			{
				return policy.getHasName();				
			}
			case 1:///value 
			{
				return policy.getHasValue();
			}
			case 2:/// filter for policy is *
			{
				return "*";
			}
			default:
			{
				return "";
			}
		}
		
	}
	
	
	
	
	
	
}
