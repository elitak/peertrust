package peertrust.common.impl;

import peertrust.common.interfaces.IPolicy;

public class Policy implements IPolicy {
	private static final long serialVersionUID = 6079982591684979876L;
	
	private String strPolicy=null;
	
	public Policy(String policy) {
		strPolicy=policy;
	}

	public String toStringRepresentation() {
		return strPolicy;
	}
}
