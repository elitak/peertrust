/*
 * Created on 14.07.2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.peertrust.demo.servlet.jsptags;



import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.JspTag;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * @author pat_dev
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class IfPropertyHoldTag extends TagSupport{
	private String property;
	private String valueToSet;
	
	/**
	 * 
	 */
	public IfPropertyHoldTag() {
		super();
	}

	public int doStartTag() throws JspTagException {
		System.out.println("+++++++starting IfPropertyHoldTag");
		//check parent 
		//PTHeaderTag headerTag;
		JspTag tag =getParent();
	      if (tag == null) {
	        throw new JspTagException(
	        		"ptCreateTag must be nested in pt create param tag; this tag does not have any parent");
	      }
	      
	      if(!(tag instanceof PTParamCreateTag)){
	    	  throw new JspTagException(
      			"ptCreateTag must be nested in ptcreate param tag; the parent type is:"+tag.getClass());
	      }
	      
		return Tag.SKIP_BODY;
	}

	
	

	public int doEndTag() throws JspException {
		//app property, valueTosetPair to parent
		((PTParamCreateTag)getParent()).setIfSpec(this.property, this.valueToSet);
		return Tag.EVAL_PAGE;
	}

	
	
	public void release() {
		property=null;
		valueToSet=null;
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public String getValueToSet() {
		return valueToSet;
	}

	public void setValueToSet(String valueToSet) {
		this.valueToSet = valueToSet;
	}
	
	
	
}
