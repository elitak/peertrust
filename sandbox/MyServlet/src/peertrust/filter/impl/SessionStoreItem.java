package peertrust.filter.impl;

import peertrust.filter.interfaces.ISessionStoreItem;

/**
 * The ProtectedResourcePolicyFilter has a hastable in which the resource content
 * which the user requested is stored after trust negotiation with the session id
 * as the key. This class represents this resource content in the hashtable.
 * @see ISessionStoreItem
 * @author Sebastian Wittler
 */
public class SessionStoreItem implements ISessionStoreItem {
	// The resource content as a byte array 
	private byte nData[];
	// The content type of the resource content
	private String strContentType;

	/**
	 * Constructor.
	 * @param data The byte array of the resource content.
	 * @param contenttype The content type of the resource content.
	 */
	public SessionStoreItem(byte data[],String contenttype) {
		nData=data;
		strContentType=contenttype;
	}
	
	/**
	 * Constructor.
	 * @param content The resource content as a text string (important for jsps).
	 * @param contenttype The content type of the resource content.
	 */
	public SessionStoreItem(String content,String contenttype) {
		this(content.getBytes(),contenttype);
	}

	/**
	 * @see ISessionStoreItem.getContentBytes
	 */
	public byte[] getContentBytes() {
		return nData;
	}
	
	/**
	 * @see ISessionStoreItem.getContentString
	 */
	public String getContentString() {
		return new String(nData);
	}
	
	/**
	 * @see ISessionStoreItem.getContentType
	 */
	public String getContentType() {
		return strContentType;
	}
}
