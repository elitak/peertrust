/**
 * 
 */
package org.peertrust.demo.resourcemanagement;

import java.util.Vector;

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