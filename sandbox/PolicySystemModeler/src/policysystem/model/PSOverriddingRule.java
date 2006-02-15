package policysystem.model;


public interface PSOverriddingRule extends ModelObjectWrapper
{
	public PSPolicy getOverridder();
	public void setOverridder(PSPolicy overridder);
	
	public PSPolicy getOverridden();
	public void setOverridden(PSPolicy overridden);

	
}
