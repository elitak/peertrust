/**
 * 
 */
package org.peertrust.modeler.policysystem.model;

import java.util.Iterator;
import java.util.Vector;

import org.peertrust.modeler.policysystem.model.abtract.PSApplyingPolicyResolver;
import org.peertrust.modeler.policysystem.model.abtract.PSOverridingRule;
import org.peertrust.modeler.policysystem.model.abtract.PSPolicySystem;
import org.peertrust.modeler.policysystem.model.abtract.PSProtection;
import org.peertrust.modeler.policysystem.model.abtract.PSResource;

/**
 * Provides the algorithm to comput the applying policies
 * for the filter based policy attachment scheme
 * @author Patrice Congo
 */
public class PSFilterBasedAPR implements PSApplyingPolicyResolver 
{

	/**
	 * The policy system
	 */
	private PSPolicySystem psModel;
	
	/**
	 * 
	 */
	public PSFilterBasedAPR(PSPolicySystem psModel) 
	{
		super();
		this.psModel=psModel;
	}

	/* (non-Javadoc)
	 * @see org.peertrust.modeler.policysystem.model.abtract.PSApplyingPolicyResolver#getApplyingPolicies(org.peertrust.modeler.policysystem.model.abtract.PSResource)
	 */
	public Vector getApplyingPolicies(PSResource psResource) 
	{
		Vector protections= new Vector();
		if(psResource==null)
		{
			return protections;
		}
		
		Vector path=psModel.getPathToAncestorRoots(psResource);//psModel.getDirectParent(psResource);
		PSResource curRes;
		
		for(Iterator it=path.iterator();it.hasNext();)
		{
			curRes=(PSResource)it.next();
			protections.addAll(PSProtectionImpl.makeProtections(curRes));
			///override
			applyORules(protections,curRes);
		}
		return protections;
	}

	private static void applyORules(Vector protections, PSResource psRes)
	{
		Vector oRules=psRes.getIsOverrindingRule();
		if(oRules!=null)
		{
			PSOverridingRule oRule;
			PSProtection protection;
			for(Iterator it=oRules.iterator();it.hasNext();)
			{
				oRule=(PSOverridingRule)it.next();
				for(Iterator ptsIt=protections.iterator();it.hasNext();)
				{
					protection=(PSProtection)ptsIt.next();
					protection.override(oRule);
				}
			}
		}
	}
	/* (non-Javadoc)
	 * @see org.peertrust.modeler.policysystem.model.abtract.PSApplyingPolicyResolver#getApplyingPolicies(java.lang.String)
	 */
	public Vector getApplyingPolicies(String identity) 
	{
		//((PolicySystemRDFModel)psModel).getPSResource()
		PSResource res=psModel.getPSResource(identity,false);
		return getApplyingPolicies(res);
	}
	

}
