	/**
 * 
 */
package policysystem.model;

import java.io.File;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.eclipse.core.internal.filebuffers.ResourceFileBuffer;
import org.eclipse.core.internal.runtime.Log;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.internal.registry.StickyViewDescriptor;

import policysystem.model.abtract.ModelObjectWrapper;
import policysystem.model.abtract.PSFilter;
import policysystem.model.abtract.PSPolicy;
import policysystem.model.abtract.PSResource;

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

	static public final Object[] EMPTY_ARRAY= new Object[0];
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
			return EMPTY_ARRAY;
		}
		
		
		logger.info("getElemnts:"+inputElement+ 
					" class="+inputElement.getClass());
		if(inputElement instanceof File)
		{
			try {
				File file=(File)inputElement;
				if(file.isFile())
				{
					logger.warn("Cannot handle file["+file.toURI()+
								"]; directory expedted");
					return EMPTY_ARRAY;//new Object[0];
				}
				
				PSResource res= 
					PolicySystemRDFModel.getInstance().getResource(
												file.toString(),
												true,
												new FileResourceSelector(file));
				
				Vector dirPolicies=res.getIsProtectedBy();
				Vector filters = res.getHasFilter();
				Vector allPolicies= new Vector();
				
				if(dirPolicies!=null)
				{
					allPolicies.addAll(dirPolicies);
				}
				
				if(filters!=null)
				{
						Iterator it=filters.iterator();
						PSFilter filter;
						
						for(;it.hasNext();)
						{
							filter=(PSFilter)it.next();
							allPolicies.addAll(filter.getIsprotectedBy());
						}
				}
				if(allPolicies.size()>0)
				{
					return allPolicies.toArray();
				}
				else
				{
					logger.info("no policy found for:"+inputElement);
					return EMPTY_ARRAY;
				}
			} catch (RuntimeException e) {
				return EMPTY_ARRAY;
			}
			
		}
		else
		{
			logger.warn("cannot handle this kind of input:"+inputElement);
			return EMPTY_ARRAY;
		}		
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.IContentProvider#dispose()
	 */
	public void dispose() 
	{
		
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
	 */
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) 
	{
		
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
			if(PolicySystemRDFModel.getInstance().isSubClassOf(
					res,PolicySystemRDFModel.CLASS_FILTER))
			{
				
				Statement stm= 
					res.getProperty(PolicySystemRDFModel.PROP_IS_PROTECTED_BY);
				return "fil"+columnIndex;
			}
			else if(PolicySystemRDFModel.getInstance().isSubClassOf(
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
		else if(element instanceof PSFilter)
		{
			return getFlterTableCellLabel((PSFilter)element,columnIndex);
		}
		else
		{
			logger.warn(
					"cannot handle this kind of object:"+element+
					"element"+element.getClass());
			return null;//return "ColA"+columnIndex+" el:"+element;
		}
		
	}

	public void addListener(ILabelProviderListener listener) 
	{
		
	}

	public boolean isLabelProperty(Object element, String property) 
	{
		System.out.println("isLabelProperty");
		return false;
	}

	public void removeListener(ILabelProviderListener listener) 
	{
		
	}


	/////////////////////////////////////////////////////////////////////////
	//////////////////UTIL///////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////
	final private String getPolicyTableCellLabel(
										PSPolicy policy,
										int colIndex)
	{
		switch(colIndex)
		{
			case 0://name
			{
				return policy.getLabel();				
			}
			case 1:///value 
			{
				return policy.getHasValue();
			}
			case 2:/// filter for policy is *
			{
				ModelObjectWrapper guarded=policy.getGuarded();
				if(guarded instanceof PSFilter)
				{
					StringBuffer strBuf= new StringBuffer(512);
					Iterator it=((PSFilter)guarded).getHasCondition().iterator();
					for(;it.hasNext();)
					{
						strBuf.append(it.next());
					}
					return strBuf.toString();
				}
				else if(guarded instanceof PSResource)
				{
					return "*";
				}
				else
				{
					logger.warn("Policy Guarded type is unkown:"+guarded+
							" policy:"+policy.getClass());
					return "";
				}
			}
			default:
			{
				return "";
			}
		}
		
	}
	
	
	final private String getFlterTableCellLabel(
												PSFilter filter,
												int colIndex)
	{
		switch(colIndex)
		{
			case 0://name
			{
					return filter.getLabel();				
			}
			case 1:///value 
			{
				return "filter";
			}
				case 2:/// filter for policy is *
			{
					StringBuffer strBuf= new StringBuffer(512);
					Iterator it=filter.getHasCondition().iterator();
					for(;it.hasNext();)
					{
						strBuf.append(it.next());
					}
					return strBuf.toString();
			}
			default:
			{
				return "";
			}
		}
	
	}
	
	
	
}
