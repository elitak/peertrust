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


/**
 * @author pat_dev
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ElseIfPropertyHoldTag extends IfPropertyHoldTag{
	//private String valueToSet;
	//private String property;
	
	
	/**
	 * 
	 */
	public ElseIfPropertyHoldTag() {
		super();
	}

	
	
	public int doStartTag() throws JspTagException {
		System.out.println("+++++++starting ElseIfPropertyHoldTag");
		//check parent 
		//PTHeaderTag headerTag;
		JspTag tag =getParent();
	      if (tag == null) {
	        throw new JspTagException(
	        		"ElseIfPropertyHold Tag must be nested in pt create param tag; this tag does not have any parent");
	      }
	      
	      if(!(tag instanceof PTParamCreateTag)){
	    	  throw new JspTagException(
      			"ElseIfPropertyHild Tag must be nested in ptcreate param tag; the parent type is:"+tag.getClass());
	      }
	      
		return Tag.SKIP_BODY;
	}


	public int doEndTag() throws JspException {
		//set valueTosetPair to parent
		//((PTParamCreateTag)getParent()).addElseIfSpec(this.property,this.valueToSet);
		((PTParamCreateTag)getParent()).addElseIfSpec(
											this.getProperty(),
											this.getValueToSet());
		return Tag.EVAL_PAGE;
	}

//	public String getProperty() {
//		return property;
//	}

//	public void setProperty(String property) {
//		this.property = property;
//	}

//	public String getValueToset() {
//		return valueToSet;
//	}

//	public void setValueToset(String valueToset) {
//		this.valueToSet = valueToset;
//	}

	
}
