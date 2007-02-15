package peertrust.tag;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * JSP Tag handler for the case in which the client fulfills the PolicyConditionTag's
 * requirements (<poljsp:iftrue>).
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
public class ConditionTrueTag extends TagSupport {
	private static final long serialVersionUID = 1L;

	/**
	 * @see TagSupport.doStartTag
	 */
	public int doStartTag() throws JspTagException {
		// Get parent tag (must be PolicyConditionTag)
//TODO: cast error
		PolicyConditionTag parent=(PolicyConditionTag)findAncestorWithClass(
			this,PolicyConditionTag.class);
		if(parent==null)
			throw new JspTagException("iftrue- not inside poliycondition-tag");
		// Only process tag's content if client satisfied the requirements of the
		// parent PolicyConditionTag
		if(parent.getFulfilled())
			return(EVAL_BODY_INCLUDE);
		return(SKIP_BODY);
	}
}
