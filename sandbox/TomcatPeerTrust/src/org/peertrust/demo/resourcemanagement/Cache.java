/**
 * 
 */
package org.peertrust.demo.resourcemanagement;



import com.sun.org.apache.xalan.internal.xsltc.runtime.Hashtable;

/**
 *Cache provide a cache implementation which ejection strategy
 *and element creation can be customized.
 * 
 * @author Patrice Congo (token77)
 */
public class Cache 
{
	
	static final public int DEFAULT_SIZE=64;
	
	/**
	 * the cache element creator
	 */
	private CacheElementCreator cacheElementCreator;
	
	/**
	 * the hashtable that hold the elements
	 */
	private Hashtable cacheElements= new Hashtable();
	
	/**
	 * the ejection strategy use by the cache
	 */
	private CacheEjectionStrategy ejectionStrategy;
	
	
	
	/**
	 * To create a new cache which uses the provide element
	 * creator. This cache has the default FIFO ejection strategy
	 * @param cacheElementCreator -- the element creator for the new cache 
	 */
	public Cache(CacheElementCreator cacheElementCreator) 
	{
		super();
		ejectionStrategy=new FIFOEjectionStrategy();
		ejectionStrategy.setSize(DEFAULT_SIZE);
		this.cacheElementCreator=cacheElementCreator;
	}

	/**
	 * To create a new cache which uses the provide element
	 * creator and ejection strategy. 
	 * @param cacheElementCreator -- the element creator for the new cache
	 * @param ejectionStrategy -- the ejection strategy to use in the new cache 
	 */
	public Cache(
				CacheElementCreator cacheElementCreator,
				CacheEjectionStrategy ejectionStrategy) 
	{
		super();
		ejectionStrategy=new FIFOEjectionStrategy();
		ejectionStrategy.setSize(DEFAULT_SIZE);
		this.cacheElementCreator=cacheElementCreator;
		this.ejectionStrategy=ejectionStrategy;
	}
	
	/**
	 * To get a cache element.
	 * @param key -- the key of the element to get
	 * @return the cache element with the key as provided
	 * @throws NullPointerException
	 */
	synchronized public Object get(Object key) throws NullPointerException{
		if(key==null){
			throw new NullPointerException("kex must not be null");
		}
		
		Object element=cacheElements.get(key);
		
		if(element==null){
			element=cacheElementCreator.createCacheElement(key);
		}
		
		if(element!=null){
			Object toEjectKey=ejectionStrategy.indicateLastAccess(key);
			if(toEjectKey!=null){
				cacheElements.remove(toEjectKey);
				//System.out.println("removing:"+toEjectKey+" key:"+key);
			}
			cacheElements.put(key,element);
		}
		
		return element;
	}
	
	/**
	 * clear the cache.
	 */
	synchronized public void clear(){
		cacheElements.clear();
	}
	
	
	/**
	 * Set the cache size
	 * @param size
	 */
	public void setSize(int size) 
	{
		if(ejectionStrategy!=null){
			this.setSize(size);
		}
	}
	
	
	
	/**
	 * @return Returns the cacheElementCreator.
	 */
	public CacheElementCreator getCacheElementCreator() 
	{
		return cacheElementCreator;
	}

	/**
	 * @param cacheElementCreator The cacheElementCreator to set.
	 */
	public void setCacheElementCreator(CacheElementCreator cacheElementCreator) 
	{
		this.cacheElementCreator = cacheElementCreator;
	}

	/**
	 * @return Returns the ejectionStrategy.
	 */
	public CacheEjectionStrategy getEjectionStrategy() 
	{
		return ejectionStrategy;
	}

	/**
	 * @param ejectionStrategy The ejectionStrategy to set.
	 */
	public void setEjectionStrategy(CacheEjectionStrategy ejectionStrategy) 
	{
		this.ejectionStrategy = ejectionStrategy;
	}

	static public void main(String[] args)
	{
		CacheElementCreator creator= new CacheElementCreator(){

			public Object createCacheElement(Object key) {
				return "created"+key;
			}
			
		};
		
		Cache cache= new Cache(creator);
		for(int i=0;i<70;i++){
			cache.get("_key_"+i);
		}
		
		System.out.println("content:"+cache.get("_key_64"));
	}
}
