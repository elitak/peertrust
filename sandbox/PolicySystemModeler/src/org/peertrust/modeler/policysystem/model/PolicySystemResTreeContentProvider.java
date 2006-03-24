package org.peertrust.modeler.policysystem.model;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.ui.IViewSite;

public class PolicySystemResTreeContentProvider implements ITreeContentProvider{
	static final public String POLICY_SYSTEM_RES="PolicySystem"; 
	static final public String POLICY_SYSTEM_RES_POLICIES="Policies";
	static final public String POLICY_SYSTEM_RES_RESOURCES="Resources";
	static final public String POLICY_SYSTEM_RES_ASSERTIONS="Assertions";
	static final public String POLICY_SYSTEM_RES_FILTERS="Filters";
	static final public String POLICY_SYSTEM_RES_CONFLICT_RESOLUTION=
											"Inheritance Conflict Resolition";
	static final public String 
		POLICY_SYSTEM_RES_OVERRIDDING_RULES= "Overriding Rules";
	
	public PolicySystemResTreeContentProvider() {
		super();
	}
	
	static public boolean isPolicySystemRes(String name){
		if(name==null)
		{
			return false;
		}
		else
		{
			return name.equals(POLICY_SYSTEM_RES_ASSERTIONS)||
					name.equals(POLICY_SYSTEM_RES_CONFLICT_RESOLUTION)||
					name.equals(POLICY_SYSTEM_RES_FILTERS)||
					name.equals(POLICY_SYSTEM_RES_POLICIES)||
					name.equals(POLICY_SYSTEM_RES_RESOURCES)||
					name.equals(POLICY_SYSTEM_RES_OVERRIDDING_RULES);
		}
			
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#getChildren(java.lang.Object)
	 */
	public Object[] getChildren(Object parentElement) 
	{
		System.out.println("parentElement="+parentElement);
		if(POLICY_SYSTEM_RES.equals(parentElement))
		{
			return new String[]{
							POLICY_SYSTEM_RES_RESOURCES,
							POLICY_SYSTEM_RES_FILTERS,
							POLICY_SYSTEM_RES_OVERRIDDING_RULES,
							POLICY_SYSTEM_RES_POLICIES
							/*POLICY_SYSTEM_RES_ASSERTIONS,*/
							/*POLICY_SYSTEM_RES_CONFLICT_RESOLUTION*/};
		}
		else
		{
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#getParent(java.lang.Object)
	 */
	public Object getParent(Object element) 
	{
		System.out.println("element="+element);
		if(	POLICY_SYSTEM_RES_ASSERTIONS.equals(element)||
				POLICY_SYSTEM_RES_ASSERTIONS.equals(element)||
				POLICY_SYSTEM_RES_CONFLICT_RESOLUTION.equals(element)||
				POLICY_SYSTEM_RES_FILTERS.equals(element) ||
				POLICY_SYSTEM_RES_POLICIES.equals(element) ||
				POLICY_SYSTEM_RES_OVERRIDDING_RULES.equals(element))
		{
			return POLICY_SYSTEM_RES;
		}
		else
		{
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#hasChildren(java.lang.Object)
	 */
	public boolean hasChildren(Object element) 
	{
		if(POLICY_SYSTEM_RES.equals(element))
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.Object)
	 */
	public Object[] getElements(Object inputElement) 
	{
		System.out.println("inputEle="+inputElement);
		
		if(inputElement instanceof IViewSite)
		{
			return new Object[]{POLICY_SYSTEM_RES};
		}
		else if(POLICY_SYSTEM_RES.equals(inputElement))
		{
			return new String[]{
							POLICY_SYSTEM_RES_RESOURCES,
							POLICY_SYSTEM_RES_POLICIES,
							POLICY_SYSTEM_RES_FILTERS,
							POLICY_SYSTEM_RES_OVERRIDDING_RULES//ASSERTIONS,
							/*POLICY_SYSTEM_RES_ASSERTIONS*/};
		}
		else
		{
			return null;
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

}
