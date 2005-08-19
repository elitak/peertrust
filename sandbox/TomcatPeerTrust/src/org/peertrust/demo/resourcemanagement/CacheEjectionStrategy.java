/**
 * 
 */
package org.peertrust.demo.resourcemanagement;

/**
 * @author pat_dev
 *
 */
public interface CacheEjectionStrategy {
	/**
	 * set cache size
	 * @param size
	 */
	public void setSize(int size);
	
	/**
	 * Indicate last access to the cache element ejector.
	 * @param key -- the key of the lastely access element
	 * @return -- the element to remove
	 */
	public Object indicateLastAccess(Object key);
	
}
