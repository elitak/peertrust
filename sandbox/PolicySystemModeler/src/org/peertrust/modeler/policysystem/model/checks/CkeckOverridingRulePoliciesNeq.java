package org.peertrust.modeler.policysystem.model.checks;

import org.peertrust.modeler.policysystem.model.PSModelCheck;
import org.peertrust.modeler.policysystem.model.abtract.PSOverrindingRule;
import org.peertrust.modeler.policysystem.model.abtract.PSPolicy;


/**
 * Checks whether a overriding rule policies are equal 
 * @author pat_dev
 *
 */
public class CkeckOverridingRulePoliciesNeq implements PSModelCheck 
{
	public static final String MESG_UNEQUAL="Policies must be different";
	
	/** the overrider PSPolicy*/
	protected PSPolicy overridder;
	
	/** the overriden PSPolicy*/
	protected PSPolicy overridden;
	
	/**
	 * Create a new CkeckOverridingRulePoliciesNeq
	 * @param overridder -- the overriding policy
	 * @param overridden -- the overriden Policy
	 */
	public CkeckOverridingRulePoliciesNeq(
						PSPolicy overridder,
						PSPolicy overridden)
	{
		this.overridden=overridden;
		this.overridder=overridder;
	}
	
	
	/**
	 * @see org.peertrust.modeler.policysystem.model.PSModelCheck#doCheck()
	 */
	public boolean doCheck() 
	{
		if(overridden==null || overridden ==null)
		{
			return false;
		}
		
		return !overridden.getLabel().equals(overridder.getLabel());
	}
	
	/**
	 * @see org.peertrust.modeler.policysystem.model.PSModelCheck#getMessage()
	 */
	public String getMessage() 
	{
		return MESG_UNEQUAL;
	}

	/**
	 * @return the overridden pspolicy
	 */
	public PSPolicy getOverridden() 
	{
		return overridden;
	}

	/**
	 * To set a new overridden policy
	 * @param overridden -- the new overriden policy
	 */
	public void setOverridden(PSPolicy overridden) 
	{
		this.overridden = overridden;
	}

	/**
	 * @return the overrider policy
	 */
	public PSPolicy getOverridder() 
	{
		return overridder;
	}

	/**
	 * To ser the overridder policy
	 * @param overridder
	 */
	public void setOverridder(PSPolicy overridder) 
	{
		this.overridder = overridder;
	}

	
}
