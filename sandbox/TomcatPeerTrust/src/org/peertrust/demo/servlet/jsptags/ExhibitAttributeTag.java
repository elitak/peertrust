/**
 * 
 */
package org.peertrust.demo.servlet.jsptags;

import javax.servlet.jsp.tagext.TagSupport;

/**
 * @author pat_dev
 *
 */
public abstract class ExhibitAttributeTag extends TagSupport {
	private String name;
	private String value;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	public void resetAttibutes(){
		name=null;
		value=null;
		return;
	}
	public void release() {
		resetAttibutes();
	}
	
	
}
