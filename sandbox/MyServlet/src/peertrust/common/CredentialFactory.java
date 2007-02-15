package peertrust.common;

import peertrust.common.impl.Credential;
import peertrust.common.interfaces.ICredential;

public class CredentialFactory {
	private static CredentialFactory factory=null;
	
	public static CredentialFactory getInstance() {
		if(factory==null)
			factory=new CredentialFactory();
		return factory;
	}

	public ICredential createCredential(String credential) {
		ICredential cred=new Credential(credential);
		return cred;
	}
	
	private CredentialFactory() {
	}
}
