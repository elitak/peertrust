package org.peertrust.modeler.policysystem.model;

import org.peertrust.modeler.model.RDFModelManipulator;
import org.peertrust.modeler.policysystem.model.abtract.PSModelObject;


import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;

public interface HierarchyNodeCreationMechanism {
	public PSModelObject 	createNode(
									Model rdfModel, 
									String nodeName);
	
	public PSModelObject	createLink(
									Model rdfModel, 
									Resource node1, 
									Resource node2);
}
