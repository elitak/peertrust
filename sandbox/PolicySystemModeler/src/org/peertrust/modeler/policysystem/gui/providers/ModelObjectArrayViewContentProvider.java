package org.peertrust.modeler.policysystem.gui.providers;

import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.peertrust.modeler.policysystem.model.abtract.PSModelObject;
import org.peertrust.modeler.policysystem.model.abtract.PSFilter;
import org.peertrust.modeler.policysystem.model.abtract.PSOverridingRule;
import org.peertrust.modeler.policysystem.model.abtract.PSPolicy;
import org.peertrust.modeler.policysystem.model.abtract.PSResource;

public class ModelObjectArrayViewContentProvider 
			implements ITreeContentProvider 
{
	static private final Object EMPTY_ARRAY []= {}; 
	/** 
	 * Represents the model object whose children are
	 * to be shown.
	 * E.g. 
	 */
	private PSModelObject modelObject;
	
	/**
	 * Represents the type of the model Object children to show
	 * in the view
	 */
	private Class childrenClass;
	
	/**
	 * The logger for the ModelObjectArrayViewContentProvider class
	 */
	static private final Logger logger=
		Logger.getLogger(ModelObjectArrayViewContentProvider.class);
	/**
	 * Creator a view Contents provider that provides a view 
	 * with the children of the specified model Object
	 * @param modelObject -- the parent model object which children
	 * 		will be return as content 
	 * @param childrenClass -- the type of the children e.g. PSPolicy
	 */
	public ModelObjectArrayViewContentProvider(
							PSModelObject modelObject,
							Class childrenClass) 
	{
		super();
		this.modelObject=modelObject;
		this.childrenClass=childrenClass;
	}

	/**
	 * The Object returns depends on the children tyoe
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#getChildren(java.lang.Object)
	 */
	public Object[] getChildren(Object parentElement) 
	{
		if(parentElement==null)
		{
			return EMPTY_ARRAY;
		}
		else 
		{	logger.info("Getting children:"+
					"\n\tparentElement:"+parentElement+
					"\n\tclass:"+parentElement.getClass());
			if(parentElement instanceof PSResource)
			{
				return getChildren((PSResource)parentElement,childrenClass);
			}
			else if(parentElement instanceof PSOverridingRule)
			{
				PSOverridingRule rule=
					(PSOverridingRule)parentElement;
				return new Object[]{rule.getHasOverridden(),rule.getHasOverridder()};
			}
			else
			{
				return EMPTY_ARRAY;
			}
		}
	}

	private static final Object[] getChildren(
								PSResource psResource, 
								Class childrenClass)
	{
		if(childrenClass==null || psResource==null)
		{
			return EMPTY_ARRAY;
		}
		else if(childrenClass.equals(PSOverridingRule.class))
		{
			return psResource.getIsOverrindingRule().toArray();
		}
		else if(childrenClass.equals(PSFilter.class))
		{
			return psResource.getHasFilter().toArray();
		}
		else if(childrenClass.equals(PSPolicy.class))
		{
			return psResource.getIsProtectedBy().toArray();
		}
		else
		{
			return EMPTY_ARRAY;
		}
	}
	
	/**
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#getParent(java.lang.Object)
	 */
	public Object getParent(Object element) 
	{
		if(element==null || modelObject==null || childrenClass==null)
		{
			return null;
		}
		else if(element.getClass().isInstance(childrenClass))
		{
		 	return modelObject;
		}
		else
		{
			return null;
		}
	}

	public boolean hasChildren(Object element) 
	{
		if(element ==null || modelObject==null || childrenClass==null)
		{
			return false;
		}
		else if(element instanceof PSResource)
		{
			return true;//hasChildren((PSResource)modelObject,childrenClass,element);
		}
		else if(element instanceof PSOverridingRule)
		{
			return true;//hasChildren((PSResource)modelObject,childrenClass,element);
		}
		else 
		{
			return false;
		}
	}

	static private final boolean hasChildren(
								PSResource modelObject, 
								Class childrenClass,
								Object element)
	{
		if(modelObject==null || childrenClass==null || element==null)
		{
			return false;
		}
		else if(element.getClass().isInstance(childrenClass))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	/**
	 * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.Object)
	 */
	public Object[] getElements(Object inputElement) 
	{
		modelObject=null;
		if(inputElement==null)
		{
			return EMPTY_ARRAY;
		}
		else
		{
			logger.info("\n\tGetting element for Input:"+inputElement+ 
					"\n\tclass:"+inputElement.getClass()+
					"\n\tChildren class:"+childrenClass);
			if(inputElement instanceof PSResource)
			{
				modelObject=(PSResource)inputElement;
				return getChildren((PSResource)inputElement,childrenClass);
			}
			else
			{
				logger.info("\n\tgetElements(null) returns empty array");
				return EMPTY_ARRAY;
			}
		}
		
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
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) 
	{
		//nothing
	}

}
