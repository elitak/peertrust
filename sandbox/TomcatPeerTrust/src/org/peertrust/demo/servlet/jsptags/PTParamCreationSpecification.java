/**
 * 
 */
package org.peertrust.demo.servlet.jsptags;

import java.util.Iterator;
import java.util.Vector;

import com.hp.hpl.jena.shared.BadCharLiteralException;


public class PTParamCreationSpecification{
	private String paramName=null;
	private PropertyValueToSetPair ifSpec;
	private Vector elseIfSpec=new Vector();
	private PropertyValueToSetPair elseSpec;
	private String paramValue=null;
	
	public PTParamCreationSpecification(String paramName) throws NullPointerException, IllegalArgumentException{
		if(paramName== null){
			throw new NullPointerException("Parameter paramName must not be null");
		}
		if(paramName.trim().length()==0){
			throw new IllegalArgumentException("paramName must not be empty");
		}
		
		this.paramName=paramName;
	}

	public Vector getElseIfSpec() {
		return elseIfSpec;
	}

//	public void setElseIfSpec(Vector elseIfSpec) {
//		this.elseIfSpec = elseIfSpec;
//	}

	public PropertyValueToSetPair getIfSpec() {
		return ifSpec;
	}

	public void setIfSpec(PropertyValueToSetPair ifSpec) {
		this.ifSpec = ifSpec;
	}
	
	
	
	public PropertyValueToSetPair getElseSpec() {
		return elseSpec;
	}

	public void setElseSpec(PropertyValueToSetPair elseSpec) {
		this.elseSpec = elseSpec;
	}

	public void addElseIfSpec(PropertyValueToSetPair elseIfSpec){
		this.elseIfSpec.add(elseIfSpec);
	}
	
	public void removeAllElseIfSpecs(){
		this.elseIfSpec.clear();
	}
	
	public String computParamValue(PTPropertyEvaluator evaluator)throws BadObjectStateException{
		if(paramValue!=null){
			return paramName;
		}else{
			if(ifSpec==null){
				throw new BadObjectStateException("ifSpec must not be null");
			}else{		
				if(evaluator.eval(ifSpec.getProperty())){
					paramValue=ifSpec.getValueToSet();
					return paramValue;
				}else{
					PropertyValueToSetPair pvPair;
					for(Iterator it=elseIfSpec.iterator();it.hasNext();){
						pvPair=(PropertyValueToSetPair)it.next();
						if(evaluator.eval(pvPair.getProperty())){
							paramValue=pvPair.getValueToSet();
							return paramValue;
						}
					}
					//else spec
					if(elseSpec==null){
						throw new BadObjectStateException("elseSpec Must Not Be Null");
					}
					paramValue=elseSpec.getValueToSet();
					return paramValue;
				}
			}			
		}
		
	}

	public String getParamName() {
		return paramName;
	}

	public String getParamValue() {
		return paramValue;
	}
	
	
}