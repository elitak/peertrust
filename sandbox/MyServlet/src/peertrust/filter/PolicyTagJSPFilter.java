package peertrust.filter;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import peertrust.tag.factory.JSPTagCheckerSingletonFactory;
import peertrust.tag.interfaces.IJSPTagChecker;

/**
 * A filter who filters request for jsp files. This way, the custom JSP Protect Tag
 * (PolicyConditionTag etc.) and the resulting authorization and trust negotiation
 * can be realized.
 * @see PolicyConditionTag
 * @author Sebastian Wittler
 */
public class PolicyTagJSPFilter implements Filter {
	// The filter config object.
	private FilterConfig config;

	// Logger
	private static Logger log = Logger.getLogger(PolicyTagJSPFilter.class);

	/**
	 * @see Filter.init
	 */
	public void init(FilterConfig _config) throws ServletException {
		config = _config;
	}

	/**
	 * @see Filter.destroy
	 */
	public void destroy() {
		config = null;
	}

	/**
	 * @see Filter.doFilter
	 */
	public void doFilter(ServletRequest req, ServletResponse resp,
		FilterChain chain) throws IOException, ServletException {
		log.debug("doFilter started");
		if(config==null)
			return;
		HttpServletRequest http_req=(HttpServletRequest)req;
		// Wrap request for JSP file, response can then be forwarded to the
		// client or not
		ProtectedResponseWrapper wrapper = new ProtectedResponseWrapper(
			(HttpServletResponse)resp);
		chain.doFilter(req,wrapper);
		// Checker for the custom protection JSP tag (PolicyCondition)
		IJSPTagChecker checker=JSPTagCheckerSingletonFactory.
			getInstance().getChecker();
		// URL parameter indicating that the applet accesses the jsp file
		String strAppletAccess=req.getParameter("applet_access");
		// If jsp file has the custom protection JSP Tag (PolicyCondition)
		if(checker.hasPolicyJSPTag(http_req)) {
			// If handling of tag was finished and an applet accessed this jsp file
			if((checker.isPolicyJSPFinished(http_req))&&(strAppletAccess!=null)) {
				log.debug("Ok");
				// Response the wrappers response to the client
				sendContent(resp,wrapper.toString());
			}
			// Otherwise send error page to client
			else {
				log.debug("Error "+true+" "+strAppletAccess);
//TODO: Error page besser
				sendContent(resp,"<html><head><title>Error</title></head><body>Sorry, this page is protected, you will have to provide credentials in order to access it</body></html>");
			}
		}
		// If jsp file has not the custom protection JSP Tag (PolicyCondition)
		else {
			log.debug("Ok2");
			// Response the wrappers response to the client
			sendContent(resp,wrapper.toString());
		}
	}

	/**
	 * Send specified text back to the client.
	 * @param resp The http response.
	 * @param content The text to send.
	 * @throws IOException
	 */
	private void sendContent(ServletResponse resp,String content) throws IOException {
//TODO: Vielleicht in Util-Klasse
		PrintWriter writer=resp.getWriter();
		writer.write(content);
		writer.close();
	}
}
