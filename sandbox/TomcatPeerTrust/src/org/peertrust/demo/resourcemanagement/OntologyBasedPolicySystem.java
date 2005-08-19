/**
 * 
 */
package org.peertrust.demo.resourcemanagement;

import java.util.Collections;
import java.util.Hashtable;
import java.util.Vector;



/**
 * @author pat_dev
 *
 */
public class OntologyBasedPolicySystem implements PolicySystem {
	private Cache policyCache; 
	
	/**
	 * 
	 */
	public OntologyBasedPolicySystem(String xmlSetupFilePath) {
		super();
		policyCache= new Cache(new TestElementCreator());
	}

	/* (non-Javadoc)
	 * @see org.peertrust.demo.resourcemanagement.PolicySystem#getPolicies(java.lang.String)
	 */
	public Vector getPolicies(String policyName) {
		
		Policy pol=(Policy)policyCache.get(policyName);
		Vector policies=new Vector();
		while(pol!=null){
			policies.add(pol);
			pol=pol.getIncludedPolicy();
		}
		Collections.reverse(policies);//policies.
		return policies;
	}

	/* (non-Javadoc)
	 * @see org.peertrust.demo.resourcemanagement.PolicySystem#setup(java.lang.String)
	 */
	public void setup(String xmlSetupFileName)
			throws UnsupportedFormatException {
		

	}

	


	class TestElementCreator implements CacheElementCreator{
		Hashtable policyTable= new Hashtable();
		TestElementCreator(){
			Policy pol= new Policy("acmMember", "acmMember(alice)@ acm @ alice");
			policyTable.put(pol.getPolicyName(),pol);
			
			pol= new Policy("ieeeMember", "ieeeMember(alice)@ ieee @ alice");
			policyTable.put(pol.getPolicyName(),pol);
			
		}
		
		/* (non-Javadoc)
		 * @see org.peertrust.demo.resourcemanagement.CacheElementCreator#createCacheElement(java.lang.Object)
		 */
		public Object createCacheElement(Object key) {
			if(key==null){
				return null;
			}
			return policyTable.get(key);
		}
		
	}
	
}
