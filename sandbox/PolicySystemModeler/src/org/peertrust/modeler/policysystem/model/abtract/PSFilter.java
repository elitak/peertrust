package org.peertrust.modeler.policysystem.model.abtract;

import java.util.Vector;


public interface PSFilter extends PSModelObject 
{

	public String getCondition();
	public void setHasCondition(String condition);
	public boolean containsCondition(String condition);
	public void removeCondition(String condition);
	public void removeAllConditions();
	
	public Vector getIsprotectedBy();
	public void addIsProtectedBy(PSPolicy policy) ;
	public void removeIsProtectedBy(PSPolicy policy);
}
