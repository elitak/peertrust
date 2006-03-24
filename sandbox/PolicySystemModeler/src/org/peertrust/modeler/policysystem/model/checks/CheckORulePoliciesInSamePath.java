/**
 * 
 */
package org.peertrust.modeler.policysystem.model.checks;

import java.util.Vector;

import org.peertrust.modeler.policysystem.model.PolicySystemRDFModel;
import org.peertrust.modeler.policysystem.model.abtract.PSPolicy;
import org.peertrust.modeler.policysystem.model.abtract.PSResource;


/**
 * Check whether the overriding rule is for a resource which 
 * inherits the resource    
 * @author pat_dev
 *
 */
public class CheckORulePoliciesInSamePath extends
		CkeckOverridingRulePoliciesNeq 
{
	private PSResource ruleResource;
	/**
	 * Key to identify the message to return,
	 * if -1 none, if 0 super.getMessage() else
	 * local message
	 */
	private int mesgKey=-1;
	/**
	 * create a new CheckORulePoliciesInSamePath
	 * @param ruleRes -- the PSResource where the overridding is to take place
	 * @param overridder -- the replacement policy
	 * @param overridden -- the policy to replace
	 */
	public CheckORulePoliciesInSamePath(
									PSResource ruleRes,
									PSPolicy overridder, 
									PSPolicy overridden) 
	{
		super(overridder, overridden);

	}

	/**
	 * @see org.peertrust.modeler.policysystem.model.PSModelCheck#doCheck()
	 */
	public boolean doCheck() 
	{
		if(!super.doCheck())
		{
			mesgKey=0;
			return false;
		}
		Vector parentPolicies=
			PolicySystemRDFModel.getInstance().getInheritedPolicies(
														ruleResource);
		if(parentPolicies.contains(super.overridden))
		{
			return true;
		}
		else
		{
			mesgKey=1;
			return false;
		}
	}

	/**
	 * @see org.peertrust.modeler.policysystem.model.PSModelCheck#getMessage()
	 */
	public String getMessage() {
		if(mesgKey==-1)
		{
			return null;
		}
		else if(mesgKey==0)
		{
			return super.getMessage();
		}
		else
		{
			return overridden.getLabel()+
						" does not protect a parent of "+
						ruleResource.getHasMapping(); 
		}
	}
	
}
