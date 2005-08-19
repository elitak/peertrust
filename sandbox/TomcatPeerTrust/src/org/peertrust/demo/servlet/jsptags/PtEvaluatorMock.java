/**
 * 
 */
package org.peertrust.demo.servlet.jsptags;

import javax.servlet.jsp.PageContext;

/**
 * @author pat_dev
 *
 */
public class PtEvaluatorMock extends Object implements PTPropertyEvaluator {
	private int maxCount=0;
	private int actualCount=0;
	
	
	
	public PtEvaluatorMock() {
		super();
	}

	/* (non-Javadoc)
	 * @see org.peertrust.demo.servlet.jsptags.PTPropertyEvaluator#eval(java.lang.String)
	 */
	public boolean eval(String propertySpec) {
		if(actualCount<maxCount){
			actualCount++;
			return false;
		}else{
			maxCount=maxCount+1;
			actualCount=0;
			return true;
		}
	}

	/* (non-Javadoc)
	 * @see org.peertrust.demo.servlet.jsptags.PTPropertyEvaluator#init(javax.servlet.jsp.PageContext)
	 */
	public void init(PageContext context) {
		return;
	}


}
