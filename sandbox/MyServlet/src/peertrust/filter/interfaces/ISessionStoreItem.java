package peertrust.filter.interfaces;

/**
 * The ProtectedResourcePolicyFilter has a hastable in which the resource content
 * which the user requested is stored after trust negotiation with the session id
 * as the key. This interface represents this resource content in the hashtable.
 * @see SessionStoreItem
 * @author Sebastian Wittler
 */
public interface ISessionStoreItem {
	/**
	 * Returns the resource content as byte array.
	 * @return Resource Content.
	 */
	public byte[] getContentBytes();

	/**
	 * Returns the resource content as text string.
	 * @return Resource Content.
	 */
	public String getContentString();

	/**
	 * Returns the content type of the resource.
	 * @return Content type.
	 */
	public String getContentType();
}
