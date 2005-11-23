/*
 * Created on Mar 24, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.peertrust.demo.servlet;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

/**
 * Makes the filtered hhtp rquest response non cachable 
 * @author Patrice Congo (token77)
 */
public class MainFilter implements Filter {

	public void destroy() {
		return;
	}
	
	public void doFilter(ServletRequest req, ServletResponse resp,
			FilterChain chain) throws IOException, ServletException {
		try{
//				((HttpServletResponse)resp).setHeader("Pragma","no-cache");
//				((HttpServletResponse)resp).setHeader("Cache-Control","no-cache");
//				((HttpServletResponse)resp).setHeader("Cache-Control","no-store");
				// Set to expire far in the past.
				((HttpServletResponse)resp).setHeader("Expires", "0");//"Sat, 6 May 1995 12:00:00 GMT");

				// Set standard HTTP/1.1 no-cache headers.
				//((HttpServletResponse)resp).setHeader("Cache-Control", "no-store, no-cache, must-revalidate");

				// Set IE extended HTTP/1.1 no-cache headers (use addHeader).
				//((HttpServletResponse)resp).addHeader("Cache-Control", "post-check=0, pre-check=0");

				// Set standard HTTP/1.0 no-cache header.
				//((HttpServletResponse)resp).setHeader("Pragma", "no-cache");
			}catch(ClassCastException castEx){
				castEx.printStackTrace();
			}
			chain.doFilter(req,resp);
			return;
	}
	
	public void init(FilterConfig filterConfig) throws ServletException {
		return;
	}
}
