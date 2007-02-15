package peertrust.common.impl;

import peertrust.common.interfaces.ICredential;

public class Credential implements ICredential {
	private static final long serialVersionUID = -1020299493502312660L;

	private String strCredential=null;
	
	public Credential(String credential) {
		strCredential=credential;
	}

	public String toStringRepresentation() {
		return strCredential;
	}

	public boolean equalsCredential(ICredential cred) {
		if(cred instanceof Credential)
			return toStringRepresentation().equals(((Credential)cred).toStringRepresentation());
		return false;
	}
}
