package org.peertrust.modeler.policysystem.model.checks;

import org.peertrust.modeler.policysystem.model.abtract.PSModelCheck;
import org.peertrust.modeler.policysystem.model.abtract.PSModelLabel;
import org.peertrust.modeler.policysystem.model.abtract.PSOverridingRule;
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
	 * @see org.peertrust.modeler.policysystem.model.abtract.PSModelCheck#doCheck()
	 */
	public boolean doCheck() 
	{
		if(overridden==null || overridder ==null)
		{
			return false;
		}
		PSModelLabel label1=overridden.getLabel();
		PSModelLabel label2=overridder.getLabel();
		if(label1==null || label2==null)
		{
			if(label1!=label2)
			{
				return true;
			}
			else
			{//both equal null
				return false;
			}
		}
		else
		{
			return !label1.equals(label2);
		}
	}
	
	/**
	 * @see org.peertrust.modeler.policysystem.model.abtract.PSModelCheck#getMessage()
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
