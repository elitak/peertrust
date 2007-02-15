package peertrust.common;

import peertrust.common.impl.Policy;
import peertrust.common.interfaces.IPolicy;

public class PolicyFactory {
	private static PolicyFactory factory=null;
	
	public static PolicyFactory getInstance() {
		if(factory==null)
			factory=new PolicyFactory();
		return factory;
	}
	
	public IPolicy createPolicy(String policy) {
		IPolicy pol=new Policy(policy);
		return pol;
	}
	
	private PolicyFactory() {
	}
}
