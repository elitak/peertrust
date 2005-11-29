/**
 * 
 */
package org.peertrust.demo.servlet.jsptags;


import java.util.Hashtable;


/**
 * Resposible for creating and caching properties held by a peer. 
 * @author pat_dev
 *
 */
public class PropertyCache 
{
	/**
	 * The hashtable which hold the properties
	 */
	Hashtable cache= new Hashtable();
	
	/**
	 * The peertrust evaluator
	 */
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
			cache.put(	creationSpec.getParamName(),
						creationSpec.computParamValue(ptEvaluator));
		} catch (BadObjectStateException e) {
			e.printStackTrace();throw new BadObjectStateException("could not put cache prop",e);
		}
	}

	/**
	 * 
	 * @return returns the peertrust evaluator
	 */
	public PTPropertyEvaluator getPtEvaluator() {
		return ptEvaluator;
	}

	/**
	 * Sets the peertrust evaluator
	 * @param ptEvaluator
	 */
	public void setPtEvaluator(PTPropertyEvaluator ptEvaluator) {
		this.ptEvaluator = ptEvaluator;
	}
	
	/**
	 * Returns a property
	 * @param parameterName
	 * @return
	 */
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
