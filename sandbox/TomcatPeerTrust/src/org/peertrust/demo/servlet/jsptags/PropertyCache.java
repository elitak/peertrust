/**
 * 
 */
package org.peertrust.demo.servlet.jsptags;

import java.sql.BatchUpdateException;
import java.util.Hashtable;
import java.util.Vector;

import sun.security.provider.ParameterCache;

/**
 * Resposible for creating and caching properties held by a peer. 
 * @author pat_dev
 *
 */
public class PropertyCache {
	
	Hashtable cache= new Hashtable();
	PTPropertyEvaluator ptEvaluator;
	
	public void cacheProperty(PTParamCreationSpecification creationSpec)
					throws NullPointerException, BadObjectStateException{
		if(creationSpec==null){
			throw new NullPointerException("Parameter creationSpec must not be null");
		}
		
		if(this.ptEvaluator==null){
			throw new BadObjectStateException("ptEvaluation must not be null");
		}
		
		try {
			cache.put(creationSpec.getParamName(),creationSpec.computParamValue(ptEvaluator));
		} catch (BadObjectStateException e) {
			e.printStackTrace();throw new BadObjectStateException("could not put cache prop",e);
		}
	}

	public PTPropertyEvaluator getPtEvaluator() {
		return ptEvaluator;
	}

	public void setPtEvaluator(PTPropertyEvaluator ptEvaluator) {
		this.ptEvaluator = ptEvaluator;
	}
	
	public String getParameter(String parameterName){
		if(parameterName==null){
			throw new NullPointerException("Parameter parameterName must not be null");
		}
		return (String)this.cache.get(parameterName);
	}

	public String toString() {
		return cache.toString();
	}
	
	
}
