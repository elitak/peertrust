package org.peertrust.modeler.policysystem.model;

import org.peertrust.modeler.policysystem.model.abtract.PSModelObject;

public interface PSHierarchyRelationship extends PSModelObject 
{
	public boolean isDirected();
	
	public PSModelObject getSource();
	public void setSource(PSModelObject source);
	
	public PSModelObject getTarget();
	public void setTarget(PSModelObject target);
	
	public PSModelObject[] getHierarchyNodes();
}
