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
		System.out.println(
				"DADDADADADADADDDDDDDDDDADADADADDADADADAD:"+psResource);
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
//			/override
			applyORules(protections,curRes);
			protections.addAll(PSProtectionImpl.makeProtections(curRes));
			
		}
		System.out.println(
				"DADDADADADADADDDDDDDDDDADADADADDADADADAD_END:"+protections);
		return protections;
	}

	private static void applyORules(Vector protections, PSResource psRes)
	{
		Vector oRules=psRes.getIsOverrindingRule();
		System.out.println(
				"DADDADADADADADDDDDDDDDDADADADADDADADADADor:"+oRules);
		if(oRules!=null)
		{
			PSOverridingRule oRule;
			PSProtection protection;
			for(Iterator it=oRules.iterator();it.hasNext();)
			{
				oRule=(PSOverridingRule)it.next();
				System.out.println(
						"DADDADADADADADDDDDDDDDDADADADADDADADADADor1:"+
						oRule+" size="+protections);
				try {
					for(Iterator ptsIt=protections.iterator();ptsIt.hasNext();)
					{
						System.out.println(
								"DADDADADADADADDDDDDDDDDADADADADDADADADADoRule2");
						protection=(PSProtection)ptsIt.next();
						protection.override(oRule);
					}
				} catch (Exception e) {
					e.printStackTrace();
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
