package policysystem.model;

import java.util.Enumeration;
import java.util.Vector;

public class HierarchyNode implements HierarchyNodeInspectionMechanism{
	private Vector parents= new Vector();
	private Vector children=new Vector();
	private Object nodeData;
	private HierarchyNodeInspectionMechanism inspectionMechanism;
	
	public HierarchyNode(
					Object nodeData,
					HierarchyNodeInspectionMechanism inspectionMechanism){
		super();
		this.nodeData=nodeData;
		this.inspectionMechanism=inspectionMechanism;
	}

	public void addParent(HierarchyNode parent){
		parents.add(parent);
	}
	
	public void addChild(HierarchyNode child){
		children.add(child);
	}
	
	public Enumeration getParents(){
		return parents.elements();
	}
	
	public Enumeration getChildren(){
		return children.elements();
	}

	/* (non-Javadoc)
	 * @see policysystem.model.HierarchyNodeInspectionMechanism#getName()
	 */
	public String getName() {
		return inspectionMechanism.getName();
	}
	
	
}
