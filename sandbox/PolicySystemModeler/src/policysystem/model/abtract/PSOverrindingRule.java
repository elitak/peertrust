package policysystem.model.abtract;



/**
 * 
 *@author congo
 *
 */
public interface PSOverrindingRule extends ModelObjectWrapper 
{
	
	public PSPolicy getHasOverridden();
	public void setHasOverriden(PSPolicy policy);
	
	public PSPolicy getHasOverridder();
	public void setHasOverrider(PSPolicy policy);
	
//	public boolean getIsInheritable();
//	public void setIsInheritable(boolean isInheritable);
	
	public Object getModelObject();
	
}
