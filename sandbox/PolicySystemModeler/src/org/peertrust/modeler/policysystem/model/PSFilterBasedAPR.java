
package org.peertrust.modeler.policysystem.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
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
	 * Logger for the class <code>PSFilterBasedAPR</code>
	 */
	static final private Logger logger=
				Logger.getLogger(PSFilterBasedAPR.class);

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

	/**
	 * @see org.peertrust.modeler.policysystem.model.abtract.PSApplyingPolicyResolver#getApplyingPolicies(org.peertrust.modeler.policysystem.model.abtract.PSResource)
	 */
	public List getApplyingPolicies(PSResource psResource) 
	{
		
		logger.debug(
				"\ncall :getApplyingPolicies(psResource)"+
				"\n\tpsResource="+psResource);
		Vector protections= new Vector();
		if(psResource==null)
		{
			return protections;
		}
		
		List path=psModel.getPathToAncestorRoots(psResource);//psModel.getDirectParent(psResource);
		PSResource curRes;
		
		for(Iterator it=path.iterator();it.hasNext();)
		{
			curRes=(PSResource)it.next();
//			/override
			applyORules(protections,curRes);
			protections.addAll(PSProtectionImpl.makeProtections(curRes));
			applyConditions(protections,curRes);
		}
		logger.info(
				"\nApplying policies"+
				"\n\tresource="+psResource+
				"\n\tpolicies="+protections);
		return protections;
	}

	/**
	 * Aplly condition by removing protection for which the resouce identity 
	 * does not match with the protection condition
	 * 
	 * @param protections -- contains the conditions
	 * @param resource -- the resource to test
	 */
	private static void applyConditions(Vector protections,PSResource resource)
	{
		logger.info(
				"\napplyConditions(Vector protections,PSResource resource)"+
				"\n\tprotections="+protections+
				"\n\tresource="+resource);
		if(protections==null || resource==null)
		{
			return;
		}
		
		if(resource.canHaveChild())
		{//do not filter for directories
			//TODO ask for it: applying to the folders
			//TODO ask for regular expression \. .* and so on
			return;
		}
		
		List toRemove= new ArrayList();
		PSProtection protection;
		String condition=null;
		String identity=resource.getHasMapping();
		for(Iterator it=protections.iterator();it.hasNext();)
		{
			try {
				protection=(PSProtection)it.next();
				condition=protection.getCondition();
				
				if(!Pattern.compile(condition).matcher(identity).find())
				{
					toRemove.add(protection);
				}
			} catch (Throwable th) {
				logger.warn(
						"\nException while applying condition"+
						"\n\tcondition="+condition+
						"\n\tresourceIdentity="+identity,
						th);
			}
		}
		
		protections.removeAll(toRemove);
	}
	
	/**
	 * Override the protection using the overriding rules linked
	 * to the given resource
	 * @param protections -- contains the protection 
	 * @param psRes -- the resource which overriding rules will be applied 
	 */
	private static void applyORules(Vector protections, PSResource psRes)
	{
		List oRules=psRes.getIsOverrindingRule();
		logger.debug("\noverriding rule to apply:"+oRules);
		if(oRules!=null)
		{
			PSOverridingRule oRule=null;
			PSProtection protection=null;
			for(Iterator it=oRules.iterator();it.hasNext();)
			{
				oRule=(PSOverridingRule)it.next();
				logger.debug(
						"\n\tCurrent rule:"+oRule+
						"\n\tremaining policies="+protections);
				try {
					for(Iterator ptsIt=protections.iterator();ptsIt.hasNext();)
					{
						protection=(PSProtection)ptsIt.next();
						protection.override(oRule);
					}
				} catch (Exception e) {
					logger.warn(
							"\nException while overriding "+
								"\n\toRule="+oRule+
								"\n\tprotection="+protection,
							e);
				}
				
			}
		}
	}
	/**
	 * @see org.peertrust.modeler.policysystem.model.abtract.PSApplyingPolicyResolver#getApplyingPolicies(java.lang.String)
	 */
	public List getApplyingPolicies(String identity) 
	{
		logger.debug(
				"\nCall getApplyingPolicies(String identity)"+
				"\n\tidentity="+identity);
		PSResource res=psModel.getPSResource(identity,false);
		return getApplyingPolicies(res);
	}
	

}
