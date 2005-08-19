/**
 * 
 */
package org.peertrust.demo.servlet.jsptags;

import javax.servlet.jsp.PageContext;

/**
 * @author pat_dev
 *
 */
public interface PTPropertyEvaluator {
	/**
	 * To evaluate the property spec using peertrust
	 * @param propertySpec
	 * @return 
	 */
	public boolean eval(String propertySpec);
	
	public void init(PageContext context);
}
