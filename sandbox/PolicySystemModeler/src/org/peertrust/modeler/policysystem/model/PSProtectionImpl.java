/**
 * 
 */
package org.peertrust.modeler.policysystem.model;

import java.io.File;
import java.util.Iterator;
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
	 * The condition for the protection
	 */
	private String condition;
	
	/**
	 * 
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
		if(overridden==null)
		{
			return;
		}
		
		if(overridden.equals(policy))
		{//override
			this.policy=oRule.getHasOverridder();
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
	
	public static Vector makeProtections(PSResource  psRes,PSFilter filter)
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
			
			Vector condValues=filter.getHasCondition();
			//make full path condition
			Vector conditions= new Vector();
			for(Iterator condValueIt=condValues.iterator();
				condValueIt.hasNext();)
			{
				conditions.add(identity+condValueIt.next());
			}
						
			Vector policies=filter.getIsprotectedBy();
			if(policies!=null)
			{
				PSPolicy curPol;
				String curCond;
				for(Iterator itPol=policies.iterator();itPol.hasNext();)
				{
					curPol=(PSPolicy)itPol.next();
					for(Iterator itCond=conditions.iterator();itCond.hasNext();)
					{
						curCond=(String)itCond.next();
						protections.add(
								new PSProtectionImpl(curPol,curCond));
					}
				}
			}
			
		}		
		return protections;
	}
	
	static public Vector makeProtections(PSResource psRes)
	{
		Vector protections= new Vector();
		if(psRes==null)
		{
			return protections;
		}
		Vector filters=psRes.getHasFilter();
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
