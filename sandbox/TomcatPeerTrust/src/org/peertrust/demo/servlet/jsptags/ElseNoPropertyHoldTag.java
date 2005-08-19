/**
 * 
 */
package org.peertrust.demo.servlet.jsptags;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.JspTag;
import javax.servlet.jsp.tagext.Tag;

/**
 * @author pat_dev
 *
 */
public class ElseNoPropertyHoldTag extends IfPropertyHoldTag {

	public int doStartTag() throws JspTagException {
		System.out.println("+++++++starting ElseNoPropertyHoldTag");
		//check parent 
		//PTHeaderTag headerTag;
		JspTag tag =getParent();
	      if (tag == null) {
	        throw new JspTagException(
	        		"ElseNoPropertyHold Tag must be nested in pt create param tag; this tag does not have any parent");
	      }
	      
	      if(!(tag instanceof PTParamCreateTag)){
	    	  throw new JspTagException(
      			"ElseNoPropertyHild Tag must be nested in ptcreate param tag; the parent type is:"+tag.getClass());
	      }
	      
		return Tag.SKIP_BODY;
	}


	public int doEndTag() throws JspException {
		//set valueTosetPair to parent
		//((PTParamCreateTag)getParent()).addElseIfSpec(this.property,this.valueToSet);
		((PTParamCreateTag)getParent()).setElseSpecString(
											this.getValueToSet());
		return Tag.EVAL_PAGE;
	}
	
}
