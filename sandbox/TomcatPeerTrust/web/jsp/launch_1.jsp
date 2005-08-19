<%@ page errorPage="errorPage.jsp" %>
<%@page contentType="application/x-java-jnlp-file"%>
<%@page import="java.util.Date"%>
<!-- %@page import="org.peertrust.demo.servlet.PeerTrustEngin"% -->
<!-- %@page import="org.peertrust.demo.servlet.NegotiationState"% -->
<%@page import="javax.naming.*"%>
<%@page import="org.peertrust.demo.servlet.*"%>
<%@page import="org.peertrust.demo.client.*"%>
<%@page import="org.peertrust.demo.common.*"%>
<%
  //response.setHeader("Pragma", "no-cache");
  response.setHeader("Cache-Control", "no-cache");
  response.setHeader("Cache-Control","no-store" );
  response.setDateHeader("Expires", 0);
%>

<% 
	//PeerTrustEngin trustEngin=(PeerTrustEngin)application.getAttribute("negotiations");
	//NegotiationState negoState=trustEngin.get
	ServletPeerTrustEventListener trustEngin=null;
	String jnlpRelURL=null;
	String base=null;
	String resourceURL=null;
	
		NegotiationObjects negoObjects = 
				 	(NegotiationObjects) application.getAttribute(NegotiationObjects.class.getName());
		trustEngin=negoObjects.getPeerTrustEventListener();
		
		jnlpRelURL=	"/myapp-0.1-dev/jsp/launch_1.jsp?negoSessionID="+
							request.getParameter("negoSessionID")+//RequestedSessionId()+//Session().getId()+
							"&negoResource="+ request.getParameter("negoResource");
		resourceURL=	"/myapp-0.1-dev/DemoPeerTrustServlet?negoSessionID="+
							request.getParameter("negoSessionID")+//RequestedSessionId()+//Session().getId()+
							"&negoResource="+ request.getParameter("negoResource");
												
		base=	"http://"+
						request.getLocalAddr()+
						":" + request.getServerPort();
	StringBuffer parNames= new StringBuffer();
	java.util.Enumeration e= request.getParameterNames();

	while(e.hasMoreElements()){
		parNames.append(e.nextElement());
	}
	
	String myCodeBase=base+request.getContextPath();
	String jnlpFileVersion="0.0."+System.currentTimeMillis();
%>

<?xml version="1.0" encoding="utf-8"?>
<!-- JNLP File for PeerTrust Demo Application -->
<jnlp
  spec="1.0+"
  codebase="<%=base%>"
  href="<%=jnlpRelURL%>"
  version="<%=jnlpFileVersion%>">
  <information>
    <title>PeerTrust Demo</title>
    <vendor>L3S</vendor>
    <homepage href="/myapp-0.1-dev/jws/help.html"/>
    <description>Establish Trust</description>
    <description kind="short">PeerTrust application.</description>
    <icon href="/myapp-0.1-dev/images/PeerTrust.gif"/>
    <!-- offline-allowed -->
  </information>
  <security>
      <all-permissions/>
  </security>
  <resources>
    <j2se version="1.5+"/>
    <jar href="/myapp-0.1-dev/jws/DemoClient.jar"/>
    <jar href="/myapp-0.1-dev/jws/peertrust.jar"/>
<jar href="/myapp-0.1-dev/jws/commons-httpclient-3.0-rc2.jar"/>
<jar href="/myapp-0.1-dev/jws/commons-codec-1.3.jar" />
<jar href="/myapp-0.1-dev/jws/commons-logging-api.jar" />
<jar href="/myapp-0.1-dev/jws/commons-logging.jar" />
<jar href="/myapp-0.1-dev/jws/commons-logging-optional.jar" />
<jar href="/myapp-0.1-dev/jws/log4j.jar" />
<jar href="/myapp-0.1-dev/jws/jena.jar" />
<jar href="/myapp-0.1-dev/jws/xercesImpl.jar" />
<jar href="/myapp-0.1-dev/jws/bouncyCastle.jar" />
<jar href="/myapp-0.1-dev/jws/minrt.jar" />
<jar href="/myapp-0.1-dev/jws/jsse.jar" />
<jar href="/myapp-0.1-dev/jws/icu4j.jar" />
<jar href="/myapp-0.1-dev/jws/jgraph.jar" />
  </resources>
  <application-desc main-class="org.peertrust.demo.client.WebStartPeerTrustClient">
	<argument>actualSessionID=<%=request.getSession().getId()%> </argument>
  	<argument>negoSessionID=<%=request.getParameter("negoSessionID")%> </argument>
  	<argument>negoResource=<%=request.getParameter("negoResource")%> </argument>
  	<argument>remotePeerIP=<%=request.getLocalAddr()%></argument>
  	<argument>remotePeerPort=<%=request.getLocalPort()%></argument>
  	<argument>timeStamp=<%=(new Date()).toString()%></argument>
  	<argument>appContext=<%=request.getContextPath()%></argument>
  	<argument>serviceServletPath=<%=application.getInitParameter("ServiceServletPath")%></argument>
  	<argument><%=ClientConstants.CODEBASE_URL_STR_KEY%>=<%=myCodeBase%></argument>
  <application-desc/>
</jnlp> 