/**
 * 
 */
package org.peertrust.modeler.policysystem.model;

import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.peertrust.modeler.policysystem.model.abtract.PSApplyingPolicyResolver;
import org.peertrust.modeler.policysystem.model.abtract.PSOverridingRule;
import org.peertrust.modeler.policysystem.model.abtract.PSPolicySystem;
import org.peertrust.modeler.policysystem.model.abtract.PSResource;

/**
 * @author pat_dev
 *
 */
public class PSAPRPolicyBased implements PSApplyingPolicyResolver 
{
	
	private static final Logger logger=Logger.getLogger(PSAPRPolicyBased.class);
	
	private PSPolicySystem psSystem;
	/**
	 * 
	 */
	public PSAPRPolicyBased(PSPolicySystem psSystem) 
	{
		super();
		this.psSystem=psSystem;
	}

	/* (non-Javadoc)
	 * @see org.peertrust.modeler.policysystem.model.abtract.PSApplyingPolicyResolver#getApplyingPolicies(org.peertrust.modeler.policysystem.model.abtract.PSResource)
	 */
	public Vector getApplyingPolicies(PSResource psResource) 
	{
		Vector aps= getInheritedPolicies(psResource);
		
		return aps;		
	}

	/* (non-Javadoc)
	 * @see org.peertrust.modeler.policysystem.model.abtract.PSApplyingPolicyResolver#getApplyingPolicies(java.lang.String)
	 */
	public Vector getApplyingPolicies(String identity) 
	{
		return null;
	}
	
	private Vector getInheritedPolicies(PSResource psResource) 
	{
		if(psResource==null)
		{
			logger.warn("param psResurce must not be null");
			return new Vector(); 
		}
		
		Vector policies=new Vector();
		Vector paths=psSystem.getPathToAncestorRoots(psResource);
		logger.info("PATHS:"+paths);
		for(int i=paths.size()-1;i>=0;i--)
		{
			Vector path=(Vector)paths.get(i);
			
			policies.addAll(computePathPolicies(path));
		}
		
		Vector localORules=psResource.getIsOverrindingRule();
		logger.info("\n\tlocalOrules:"+localORules);
		PSOverridingRule oRule;
		for(Iterator it=localORules.iterator();it.hasNext();)
		{
			oRule=(PSOverridingRule)it.next();
			oRule.performOverridding(policies);
		}
		return policies;
	}
	
	private final Vector computePathPolicies(Vector path)
	{
		logger.info("getting policies for path:"+path);
		if(path==null)
		{
			return new Vector();
		}
		
		final int MAX=path.size()-1;
		if(MAX<0)
		{
			return new Vector();
		}
		
		Vector policies= new Vector();
		Vector oRules;
		Vector lPolicies;
		///add root policies
//		policies.addAll(
//				psSystem.getLocalPolicies(
//						(PSResource)path.get(0)));
		policies.addAll(
				((PSResource)path.get(0)).getIsProtectedBy());
		logger.info("Policy at 0:"+policies);
		///follow path; add polcies and do overriding
		PSResource curRes;
		PSOverridingRule rule;
		for(int i=1;i<=MAX;i++)
		{
			curRes=(PSResource)path.get(i);
			oRules=curRes.getIsOverrindingRule();//getOverriddingRule(curRes);
			for(Iterator it=oRules.iterator();it.hasNext();)
			{
				//PSOverridingRule rule=
				rule=
					(PSOverridingRule)it.next();
				rule.performOverridding(policies);
			}
			lPolicies=curRes.getIsProtectedBy();//getLocalPolicies(curRes);
			logger.info("Policy at "+i+" for "+curRes+" "+lPolicies);
			policies.addAll(lPolicies);
			
		}
		return policies;
	}
	
}
