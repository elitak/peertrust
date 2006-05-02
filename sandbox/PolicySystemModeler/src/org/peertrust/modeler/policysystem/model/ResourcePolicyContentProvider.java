	/**
 * 
 */
package org.peertrust.modeler.policysystem.model;

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
import org.peertrust.modeler.policysystem.model.abtract.PSModelLabel;
import org.peertrust.modeler.policysystem.model.abtract.PSModelObject;
import org.peertrust.modeler.policysystem.model.abtract.PSFilter;
import org.peertrust.modeler.policysystem.model.abtract.PSPolicy;
import org.peertrust.modeler.policysystem.model.abtract.PSResource;
import org.peertrust.modeler.policysystem.model.abtract.PSResourceIdentityMaker;


import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.SimpleSelector;
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
	
	/** logger for the ResourcePolicyContentProvider class*/
	static private Logger logger= 
		Logger.getLogger(ResourcePolicyContentProvider.class);
	
	static private PolicySystemRDFModel psModel=
							PolicySystemRDFModel.getInstance();
	static private PSResourceIdentityMaker identityMaker=
				psModel.getPSResourceIdentityMaker(File.class);
	
	public ResourcePolicyContentProvider()
	{
		//empty
	}
	
	private void addParentResource(File file, PSResource psRes)
	{
		if(file==null || psRes==null)
		{
			return;
		}
//		//identity
//		if(psRes.getHasMapping()==null)
//		{
//			psRes.setHasMapping(identityMaker.makeIdentity(file));
//		}
		//test for allready set parent
		PSResource aParent=psRes.getHasSuper();
		if(aParent!=null)
		{
			logger.warn("parent res allready set");
			return;
		}
		
		File parentFile=file.getParentFile();		
		String rootFileName=
			ProjectConfig.getInstance().getProperty(ProjectConfig.ROOT_DIR);
		logger.info("setting parent res; res="+psRes+
				"\n\tparentFile:"+parentFile+
				"\n\trootFile:"+rootFileName);
		File rootFile=null;
		PSResource parentRes=null;
		if(rootFileName!=null)
		{
			rootFile= new File(rootFileName);
			if(parentFile.getAbsolutePath().startsWith(
								rootFile.getAbsolutePath()))
			{
				//String identity=identityMaker.makeIdentity(parentFile);
				parentRes=
					psModel.getPSResource(
						parentFile,//identity,//parentFile.getAbsolutePath(),//toString(),//TODO replacing by getAbs
						true
						);
				if(parentRes!=null)
				{
					psRes.addHasSuper(parentRes);
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
				System.out.println("pres:"+res);
				addParentResource(file,res);
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
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
				return EMPTY_ARRAY;
			}
			
		}
		else if(inputElement instanceof PSResource)
		{
			try {
				PSResource res=(PSResource)inputElement;
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
					return filter.getLabel().getValue();				
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
