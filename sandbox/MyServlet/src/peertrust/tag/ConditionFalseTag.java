package peertrust.tag;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * JSP Tag handler for the case in which the client didn't fulfil the
 * PolicyConditionTag's requirements (<poljsp:iffalse>).
 * Example:
 * For example
 *		<poljsp:policycondition policyname="KBS">
 *			<poljsp:iftrue>
 *				Information for KBS institute<BR>
 *			</poljsp:iftrue>
 *			<poljsp:iffalse>
 *			</poljsp:iffalse>
 *		</poljsp:policycondition>
 *
 * @author Sebastian Wittler
 */
public class ConditionFalseTag extends TagSupport {
	private static final long serialVersionUID = 7612104260726776399L;

	/**
	 * @see TagSupport.doStartTag
	 */
	public int doStartTag() throws JspTagException {
		// Get parent tag (must be PolicyConditionTag)
//TODO: cast error
		PolicyConditionTag parent=(PolicyConditionTag)findAncestorWithClass(
			this,PolicyConditionTag.class);
		if(parent==null)
			throw new JspTagException("iffalse- not inside poliycondition-tag");
		// Only process tag's content if client didn't satisfy the requirements of the
		// parent PolicyConditionTag
		if(!parent.getFulfilled())
			return(EVAL_BODY_INCLUDE);
		return(SKIP_BODY);
	}
}
