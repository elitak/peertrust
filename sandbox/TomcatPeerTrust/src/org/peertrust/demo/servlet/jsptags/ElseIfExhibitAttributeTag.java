/**
 * 
 */
package org.peertrust.demo.servlet.jsptags;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * @author pat_dev
 *
 */
public class ElseIfExhibitAttributeTag extends ExhibitAttributeTag{
	public int doEndTag() throws JspException {
		return Tag.EVAL_PAGE;
	}

	public int doStartTag() throws JspException {
		System.out.println("ElseIfExhibitAttributeTag.doStartTag");
		//check parent
		Tag parentTag= getParent();
		if(!(parentTag instanceof PTNegotiableContentTag)){
			throw 
				new JspException(
						"IfExhibitAttribute Tag must have a PTNegotiableContent Tag;"+
						" it has the folowing parent tag "+parentTag.getClass());
		}
		
		if(((PTNegotiableContentTag)parentTag).isMatchAlreadyFound()){
			return Tag.SKIP_BODY;
		}
		
		if(((PTNegotiableContentTag)parentTag).elseIfExibitsAttribute(getName(),getValue())){
			return Tag.EVAL_BODY_INCLUDE;
		}
		
		return Tag.SKIP_BODY;
	}

	public void release() {
		super.release();
	}
}
