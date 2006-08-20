	/**
 * 
 */
package org.peertrust.modeler.policysystem.model;

import java.io.File;
import java.net.URI;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Image;
import org.peertrust.modeler.policysystem.model.abtract.PSModelLabel;
import org.peertrust.modeler.policysystem.model.abtract.PSModelObject;
import org.peertrust.modeler.policysystem.model.abtract.PSFilter;
import org.peertrust.modeler.policysystem.model.abtract.PSPolicy;
import org.peertrust.modeler.policysystem.model.abtract.PSPolicySystem;
import org.peertrust.modeler.policysystem.model.abtract.PSResource;
import org.peertrust.modeler.policysystem.model.abtract.PSResourceIdentityMaker;


import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;

/**
 * A implementation of the structure content provider that 
 * can provide policies associated to a resource.
 * The resource may be specify as a 
 * {@link org.peertrust.modeler.policysystem.model.abtract.PSResource} or
 * as a file
 * 
 * @author Patrice Congo
 *
 */
public class ResourcePolicyContentProvider 
		implements 	IStructuredContentProvider,
					ITableLabelProvider
{
	
	static public final Object[] EMPTY_ARRAY= new Object[0];
	
	/** logger for the ResourcePolicyContentProvider class*/
	static private Logger logger= 
		Logger.getLogger(ResourcePolicyContentProvider.class);
	
	/**
	 * A local intance of the current policy system
	 */
	static private PSPolicySystem/*PolicySystemRDFModel*/ psModel=
							PolicySystemRDFModel.getInstance();
	
	/**
	 * Th class  file identity maker
	 */
	static private PSResourceIdentityMaker identityMaker=
				psModel.getPSResourceIdentityMaker(File.class);
	
	public ResourcePolicyContentProvider()
	{
		//empty
	}
	
	/**
	 * Sets the {@link PSResource} parent resource. The file representing the
	 * parent resource is given as parameter. if the correspong 
	 * resource is not already in the model it is created.
	 *  
	 * @param file -- the parent resource as file
	 * @param psRes -- the child resource as {@link PSResource}
	 */
	private void setParentResource(File file, PSResource psRes)
	{
		if(file==null || psRes==null)
		{
			return;
		}
		
		//test for allready set parent
		PSResource aParent=psRes.getParent();
		if(aParent!=null)
		{
			logger.warn("parent res allready set");
			return;
		}
		
		File parentFile=file.getParentFile();
		//TODO change root 
		/*String rootFileName=*/
		URI root=	ProjectConfig.getInstance().getRootFor(
												parentFile.toURI());
			//ProjectConfig.getInstance().getProperty(ProjectConfig.ROOT_DIR);
		logger.info("setting parent res; res="+psRes+
				"\n\tparentFile:"+parentFile+
				"\n\trootFile:"+root/*rootFileName*/);
		File rootFile=null;
		PSResource parentRes=null;
		if(/*rootFileName*/root!=null)
		{
			//rootFile= new File(rootFileName);
			URI relFile=root.relativize(parentFile.toURI());
			if(!relFile.isAbsolute()/*parentFile.getAbsolutePath().startsWith(
								rootFile.getAbsolutePath())*/)
			{
				//String identity=identityMaker.makeIdentity(parentFile);
				parentRes=
					psModel.getPSResource(
						parentFile,//identity,//parentFile.getAbsolutePath(),//toString(),//TODO replacing by getAbs
						true
						);
				if(parentRes!=null)
				{
					psRes.setParent(parentRes);
				}
				else
				{
					logger.warn("parentResource not found; file="+parentFile);
				}
			}
			else
			{
				logger.warn("rootfile doe not contain parent file"+
							"\n\troot      ="+rootFile+
							"\n\tparentFile="+parentFile);
			}
		}
		else
		{
			logger.warn("Root file name is null");
		}
	}
	
	/**
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
					"\n\tclass="+inputElement.getClass());
		if(inputElement instanceof File)
		{
			try {
				File file=(File)inputElement;
				PSResource res= 
					psModel.getPSResource(
						file,//.getAbsolutePath(),//toString(),
						true);//new FileResourceSelector(file));
				logger.info("pres:"+res);
				setParentResource(file,res);
				//TODO check effect of not getting ProtectedBy
				List dirPolicies=null;//res.getIsProtectedBy();
				List filters = res.getHasFilter();
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
							
							//allPolicies.addAll(filter.getIsprotectedBy());
							allPolicies.add(filter.getIsProtectedBy());
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
			} 
			catch (Exception e) 
			{
				logger.warn(e);
				return EMPTY_ARRAY;
			}
			
		}
		else if(inputElement instanceof PSResource)
		{
			try {
				PSResource res=(PSResource)inputElement;
				//TODO check effect of not getting protectedBy
				List<PSPolicy> dirPolicies=null;//res.getIsProtectedBy();
				List<PSFilter> filters = res.getHasFilter();
				List<PSPolicy> allPolicies= new Vector();
				
				
				if(dirPolicies!=null)
				{
					allPolicies.addAll(dirPolicies);
				}
				
				if(filters!=null)
				{
						Iterator<PSFilter> it=filters.iterator();
						//PSFilter filter;
					
						for(;it.hasNext();)
						{
							//filter=it.next();
							//allPolicies.addAll(filter.getIsprotectedBy());
							allPolicies.add(it.next().getIsProtectedBy());
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
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
				return EMPTY_ARRAY;
			}
			
		}
		else
		{
			logger.warn("cannot handle this kind of input:"+inputElement);
			return EMPTY_ARRAY;
		}		
	}

	/**
	 * @see org.eclipse.jface.viewers.IContentProvider#dispose()
	 */
	public void dispose() 
	{
		
	}

	/**
	 * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
	 */
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) 
	{
		
	}


	
	///////////////////////////////////////////////////////////////////////////
	//////////////////LABEL PROVIDER //////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////
	
	/**
	 * Not emplemented allways return null
	 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnImage(java.lang.Object, int)
	 */
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
			return getFilterTableCellLabel((PSFilter)element,columnIndex);
		}
		else
		{
			logger.warn(
					"cannot handle this kind of object:"+element+
					"element"+element.getClass());
			return null;//return "ColA"+columnIndex+" el:"+element;
		}
		
	}

	/**
	 * Not implemented since state chance events are not available
	 * @see org.eclipse.jface.viewers.IBaseLabelProvider#addListener(org.eclipse.jface.viewers.ILabelProviderListener)
	 */
	public void addListener(ILabelProviderListener listener) 
	{
		//empty
	}

	/**
	 * @see org.eclipse.jface.viewers.IBaseLabelProvider#isLabelProperty(java.lang.Object, java.lang.String)
	 */
	public boolean isLabelProperty(Object element, String property) 
	{
		return false;
	}

	/**
	 * Not emplemented since state change events are not available
	 * @see org.eclipse.jface.viewers.IBaseLabelProvider#removeListener(org.eclipse.jface.viewers.ILabelProviderListener)
	 */
	public void removeListener(ILabelProviderListener listener) 
	{
		//empty
	}


	/////////////////////////////////////////////////////////////////////////
	//////////////////UTIL///////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////
	/**
	 * Utily method to get the policy table cell label.
	 * 
	 * @param policy -- the row policy
	 * @param colIndex -- the actual column index
	 * @return the label as string:
	 * 		<ul>
	 * 			<li>column 0 returns the policy label
	 * 			<li>column 1 returns the policy value
	 * 			<li>column 2 returns the guarded model object
 	 * 		</ul>
	 */
	final private String getPolicyTableCellLabel(
										PSPolicy policy,
										int colIndex)
	{
		switch(colIndex)
		{
			case 0://name
			{
				PSModelLabel label=policy.getLabel();
				String lValue=null;
				if(label!=null)
				{
					lValue=label.getValue();
				}
				if(lValue==null)
				{
					lValue=" ";
				}
				return lValue;				
			}
			case 1:///value 
			{
				return policy.getHasValue();
			}
			case 2:/// filter for policy is *
			{
				PSModelObject guarded=policy.getGuarded();
				if(guarded instanceof PSFilter)
				{
//					StringBuffer strBuf= new StringBuffer(512);
//					Iterator it=((PSFilter)guarded).getCondition().iterator();
//					for(;it.hasNext();)
//					{
//						strBuf.append(it.next());
//					}
//					return strBuf.toString();
					return ((PSFilter)guarded).getCondition();
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
	
	
	/**
	 * Utily method to get the filter table cell label.
	 * @param filter -- the row filter
	 * @param colIndex -- the column  index to get the index for
	 * @return return the label as string:
	 * 	<ul>
	 * 		<li>column 0 for the filter label
	 * 		<li>column 1 return "filter"
	 * 		<li>column 2 return the condition string
	 * 		<li>default return an empty string
	 * 	</ul>
	 */
	final private String getFilterTableCellLabel(
												PSFilter filter,
												int colIndex)
	{
		switch(colIndex)
		{
			case 0://name
			{
					return filter.getLabel().getValue();				
			}
			case 1:///value 
			{
				return "filter";
			}
				case 2:/// filter for policy is *
			{
//					StringBuffer strBuf= new StringBuffer(512);
//					Iterator it=filter.getCondition().iterator();
//					for(;it.hasNext();)
//					{
//						strBuf.append(it.next());
//					}
//					return strBuf.toString();
					return filter.getCondition();
			}
			default:
			{
				return "";
			}
		}
	
	}
	
	
	
}
