/**
 * 
 */
package org.peertrust.demo.resourcemanagement;

import java.util.HashMap;
import java.util.Vector;

import com.hp.hpl.jena.util.cache.CacheManager;
import com.sun.org.apache.xalan.internal.xsltc.runtime.Hashtable;

/**
 * @author pat_dev
 *
 */
public class Cache {
	
	class FIFOEjectionStrategy implements CacheEjectionStrategy{
		private int size;
		private Vector keyFiFo=new Vector();
		/* (non-Javadoc)
		 * @see org.peertrust.demo.resourcemanagement.CacheEjectionStrategy#setSize(int)
		 */
		public void setSize(int size) {
			this.size=size;			
		}

		/* (non-Javadoc)
		 * @see org.peertrust.demo.resourcemanagement.CacheEjectionStrategy#indicateLastAccess(java.lang.Object)
		 */
		public Object indicateLastAccess(Object key) {
			keyFiFo.add(key);
			if(keyFiFo.size()>size){
				Object toRemoveKey=keyFiFo.firstElement();
				keyFiFo.remove(toRemoveKey);
				return toRemoveKey;
			}else{
				return null;
			}
		}
		
	}
	
	static final public int DEFAULT_SIZE=64;
	
	private CacheElementCreator cacheElementCreator;
	private Hashtable cacheElements= new Hashtable();
	private CacheEjectionStrategy ejectionStrategy;
	
	
	
	/**
	 * 
	 */
	public Cache(CacheElementCreator cacheElementCreator) {
		super();
		ejectionStrategy=new FIFOEjectionStrategy();
		ejectionStrategy.setSize(DEFAULT_SIZE);
		this.cacheElementCreator=cacheElementCreator;
	}

	
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
	
	synchronized public void clear(){
		cacheElements.clear();
	}
	
	
	
	public void setSize(int size) {
		if(ejectionStrategy!=null){
			this.setSize(size);
		}
	}
	
	
	
	static public void main(String[] args){
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
