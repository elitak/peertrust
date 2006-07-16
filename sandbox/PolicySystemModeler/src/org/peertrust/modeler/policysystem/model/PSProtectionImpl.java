package org.peertrust.modeler.policysystem.model;


import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.peertrust.modeler.policysystem.model.abtract.PSFilter;
import org.peertrust.modeler.policysystem.model.abtract.PSModelLabel;
import org.peertrust.modeler.policysystem.model.abtract.PSOverridingRule;
import org.peertrust.modeler.policysystem.model.abtract.PSPolicy;
import org.peertrust.modeler.policysystem.model.abtract.PSProtection;
import org.peertrust.modeler.policysystem.model.abtract.PSResource;

/**
 * Default implementation of PSProtection.
 * 
 * @author Patrice Congo
 *
 */
public class PSProtectionImpl implements PSProtection 
{

	/**
	 * The protection policy
	 */
	private PSPolicy policy;
	
	/**
	 * The condition for the protection to apply
	 */
	private String condition;
	
	
	/**
	 * Create a PSProtectionImpl with the given policy and the condition
	 *  
	 * @param policy -- the protecting policy
	 * @param condition -- the condition for the protection to apply
	 */
	public PSProtectionImpl(PSPolicy policy, String condition) 
	{
		super();
		this.policy=policy;
		this.condition=condition;
	}

	/* (non-Javadoc)
	 * @see org.peertrust.modeler.policysystem.model.abtract.PSProtection#getPolicy()
	 */
	public PSPolicy getPolicy() 
	{
		return this.policy;
	}

	/* (non-Javadoc)
	 * @see org.peertrust.modeler.policysystem.model.abtract.PSProtection#setPolicy(org.peertrust.modeler.policysystem.model.abtract.PSPolicy)
	 */
	public void setPolicy(PSPolicy policy) 
	{
		this.policy=policy;
	}

	/* (non-Javadoc)
	 * @see org.peertrust.modeler.policysystem.model.abtract.PSProtection#getCondition()
	 */
	public String getCondition() 
	{
		return this.condition;
	}

	/* (non-Javadoc)
	 * @see org.peertrust.modeler.policysystem.model.abtract.PSProtection#removeCondition(java.lang.String)
	 */
	public void setCondition(String condition) 
	{
		this.condition=condition;
	}


	/* (non-Javadoc)
	 * @see org.peertrust.modeler.policysystem.model.abtract.PSProtection#override(org.peertrust.modeler.policysystem.model.abtract.PSOverridingRule)
	 */
	public void override(PSOverridingRule oRule) 
	{
		
		if(oRule==null)
		{
			return;
		}
		
		PSPolicy overridden=oRule.getHasOverridden();
		PSPolicy overridder=oRule.getHasOverridder();
		if(overridden==null || overridder==null)
		{
			return;
		}
		
		if(overridden.equals(policy))
		{//override
			this.policy=overridder;
		}
	}

	private static String endsWithSep(String identity)
	{
		String sep=null;
		if(identity.contains("\\"))
		{
			sep="\\";
		}
		else
		{
			sep="/";
		}
		
		if(identity.endsWith(sep))
		{
			return identity;
		}
		else
		{
			return identity+sep;
		}
				
	}
	
	public static List makeProtections(PSResource  psRes,PSFilter filter)
	{
		Vector protections=new Vector();
		if(filter!=null || psRes!=null)
		{
			//TODO generalize with type attribute in resource
			String identity=psRes.getHasMapping();
			if(identity==null)
			{
				return protections;
			}
			
			identity=endsWithSep(identity);
			
			//Vector condValues=filter.getCondition();
			//make full path condition
			String conditions= identity+filter.getCondition();
			PSPolicy filterPolicy=filter.getIsProtectedBy();
			protections.add(
					new PSProtectionImpl(filterPolicy,conditions));
//			List policies=filter.getIsprotectedBy();
//			if(policies!=null)
//			{
//				PSPolicy curPol;
//				//String curCond;
//				for(Iterator itPol=policies.iterator();itPol.hasNext();)
//				{
//					curPol=(PSPolicy)itPol.next();
//					protections.add(
//							new PSProtectionImpl(curPol,conditions));
//				}
//			}
			
		}		
		return protections;
	}
	
	static public List makeProtections(PSResource psRes)
	{
		Vector protections= new Vector();
		if(psRes==null)
		{
			return protections;
		}
		List filters=psRes.getHasFilter();
		if(filters==null)
		{
			return protections;
		}
		
		for(Iterator it=filters.iterator();it.hasNext();)
		{
			protections.addAll(makeProtections(psRes,(PSFilter)it.next()));
		}
		return protections;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		PSModelLabel label=policy.getLabel();
		String lValue=null;
		if(label!=null)
		{
			lValue=label.getValue();
		}
		return lValue+"["+condition+"]";
		//return super.toString();
	}
	
	
}
