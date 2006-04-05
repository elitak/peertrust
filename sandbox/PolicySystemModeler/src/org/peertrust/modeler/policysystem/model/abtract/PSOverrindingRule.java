package org.peertrust.modeler.policysystem.model.abtract;

import java.util.Collection;



/**
 * 
 *@author congo
 *
 */
public interface PSOverrindingRule extends PSModelObject 
{
	
	public PSPolicy getHasOverridden();
	public void setHasOverriden(PSPolicy policy);
	
	public PSPolicy getHasOverridder();
	public void setHasOverrider(PSPolicy policy);
	
//	public boolean getIsInheritable();
//	public void setIsInheritable(boolean isInheritable);
	
	public Object getModelObject();
	public void performOverridding(Collection policies);
	
}
