/**
 * 
 */
package org.peertrust.demo.resourcemanagement;

/**
 * Implementation of this CacheEjectionstrategy prove the Cache
 * with an ejection mechanism.
 * 
 * @author Patrice Congo(token77)
 *
 */
public interface CacheEjectionStrategy {
	/**
	 * set cache size
	 * @param size
	 */
	public void setSize(int size);
	
	/**
	 * Indicate last access cache element to the ejector.
	 * If an element need to be removed the key of the element
	 * is return.
	 * @param key -- the key of the lastely access element
	 * @return -- the key of the element to remove
	 */
	public Object indicateLastAccess(Object key);
	
}
