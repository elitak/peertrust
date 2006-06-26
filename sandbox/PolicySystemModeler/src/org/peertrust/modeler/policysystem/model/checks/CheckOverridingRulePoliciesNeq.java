package org.peertrust.modeler.policysystem.model.checks;

import org.apache.log4j.Logger;
import org.peertrust.modeler.policysystem.model.abtract.PSModelCheck;
import org.peertrust.modeler.policysystem.model.abtract.PSModelLabel;
import org.peertrust.modeler.policysystem.model.abtract.PSPolicy;


/**
 * Checks whether a overriding rule policies are equal 
 * @author pat_dev
 *
 */
public class CheckOverridingRulePoliciesNeq implements PSModelCheck 
{
	public static final String MESG_UNEQUAL="Policies must be different";
	
	/** the overrider PSPolicy*/
	protected PSPolicy overridder;
	
	/** the overriden PSPolicy*/
	protected PSPolicy overridden;
	
	static final private Logger logger=
				Logger.getLogger(CheckOverridingRulePoliciesNeq.class);
	
	/**
	 * Create a new CheckOverridingRulePoliciesNeq
	 * @param overridder -- the overriding policy
	 * @param overridden -- the overriden Policy
	 */
	public CheckOverridingRulePoliciesNeq(
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
		System.out.println("DDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDD");
		logger.debug(
				"\nCheck for"+
				"\n\toverridden="+overridden+
				"\n\toverridder="+overridder);
		System.out.println("DDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDSSSSSSSSSSSS"+
				"\n\toverridden="+overridden+
				"\n\toverridder="+overridder);
		if(overridden==null || overridder ==null)
		{
			return overridden!=overridder;
		}
		PSModelLabel label1=overridden.getLabel();
		PSModelLabel label2=overridder.getLabel();
		if(label1==null || label2==null)
		{
			return label1!=label2;
//			if(label1!=label2)
//			{
//				return true;
//			}
//			else
//			{//both equal null
//				return false;
//			}
		}
		else
		{
			String str1=label1.getValue();
			String str2=label2.getValue();
			return !str1.equals(str2);
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
