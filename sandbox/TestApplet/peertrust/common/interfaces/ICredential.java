package peertrust.common.interfaces;

import java.io.Serializable;

public interface ICredential extends Serializable {
	public String toStringRepresentation();
	public boolean equalsCredential(ICredential cred);
}
