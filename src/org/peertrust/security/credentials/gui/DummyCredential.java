package org.peertrust.security.credentials.gui;

import org.peertrust.security.credentials.*;

/**
 * Dummy for the Editor-GUI.
 */
public class DummyCredential extends Credential {

	public String getStringRepresentation() {
		return "-@-";
	}

	public String getSignerName() {
		return "-";
	}

	protected void initCredential( Object arg ) {
	}


	/* (non-Javadoc)
	 * @see credentials.Credential#getEncoded()
	 */
	public Object getEncoded() {
		
		return getSignerName();
	}
}	
