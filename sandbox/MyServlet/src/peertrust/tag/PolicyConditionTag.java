package peertrust.tag;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import peertrust.tag.factory.JSPTagCheckerSingletonFactory;
import peertrust.tag.interfaces.IJSPTagChecker;

/**
 * JSP Tag handler for the PolicyConditionTag (<poljsp:policycondition>) tag which
 * content can only be accessed if the client specifies the policies that protect
 * it. This way, parts of web pages can be protected.
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
public class PolicyConditionTag extends TagSupport {
	private static final long serialVersionUID = 4350029432672765513L;

	// Name of policies that protect this tag's content
	private String strName="";
	// Does client satisfy the tag's requirements?
	private boolean bFulfilled=false;

	/**
	 * Called internally to set the value for the name-attribute.
	 * @param name Value for name attribute.
	 */
	public void setPolicyname(String name) {
		strName=name;
	}

	/**
	 * Does client satisfy the tag's requirements (policies)?
	 * @return Can tag's content be accessed?
	 */
	public boolean getFulfilled() {
		return bFulfilled;
	}

	/**
	 * @see TagSupport.doStartTag
	 */
	public int doStartTag() throws JspException {
		// Check if client satisfies the tag's requirements (for example policies)
		IJSPTagChecker checker=JSPTagCheckerSingletonFactory.
			getInstance().getChecker();
		bFulfilled=checker.accept((HttpServletRequest)pageContext.getRequest(),strName);
		return(EVAL_BODY_INCLUDE);
	}
}
