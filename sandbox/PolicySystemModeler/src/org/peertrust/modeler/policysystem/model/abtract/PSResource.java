package org.peertrust.modeler.policysystem.model.abtract;

import java.util.Vector;



public interface PSResource extends PSModelObject {
	
	public String getHasMapping();
	public void setHasMapping(String name);
	
	
	public Vector getIsOverrindingRule();
	public void addIsOverrindingRule(PSOverridingRule rule);
	
	public Vector getHasSuper();
	public void addHasSuper(PSModelObject res);
	
	public Vector getIsProtectedBy();
	public void addIsProtectedBy(PSPolicy policy);
	
	public Vector getHasFilter();
	public void addHasFilter(PSFilter filter);
}
