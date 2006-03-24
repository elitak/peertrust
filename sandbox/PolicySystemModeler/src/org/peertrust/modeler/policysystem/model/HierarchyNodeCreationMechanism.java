package org.peertrust.modeler.policysystem.model;

import org.peertrust.modeler.model.RDFModelManipulator;
import org.peertrust.modeler.policysystem.model.abtract.ModelObjectWrapper;


import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;

public interface HierarchyNodeCreationMechanism {
	public ModelObjectWrapper 	createNode(
									Model rdfModel, 
									String nodeName);
	
	public ModelObjectWrapper	createLink(
									Model rdfModel, 
									Resource node1, 
									Resource node2);
}
