package peertrust.tag.factory;

import peertrust.filter.impl.SessionStoreItem;
import peertrust.filter.interfaces.ISessionStoreItem;

/**
 * Factory that delivers an item for storing the resource content after a trust
 * negotiation (SessionStoreItem).
 * @see ISessionStoreItem
 * @author Sebastian Wittler
 */
public class SessionStoreItemFactory {
	// Singleton for factory
	private static SessionStoreItemFactory factory=null;
	
	/**
	 * Returns a singleton instance of the factory.
	 * @return Factory.
	 */
	public static SessionStoreItemFactory getInstance() {
		if(factory==null)
			factory=new SessionStoreItemFactory();
		return factory;
	}

	/**
	 * Create SessionStoreItem.
	 * @param data The byte data of the resource content.
	 * @param contenttype The contenttype of the resource.
	 * @return The SessionStoreItem.
	 */
	public ISessionStoreItem createSessionStoreItem(
		byte data[],String contenttype) {
		return new SessionStoreItem(data,contenttype);
	}
	
	/**
	 * Create SessionStoreItem.
	 * @param data The resource content as textstring.
	 * @param contenttype The contenttype of the resource.
	 * @return The SessionStoreItem.
	 */
	public ISessionStoreItem createSessionStoreItem(
		String text,String contenttype) {
		return new SessionStoreItem(text.getBytes(),contenttype);
	}

	/**
	 * Constructor private because of singleton.
	 */
	private SessionStoreItemFactory() {
	}
}
