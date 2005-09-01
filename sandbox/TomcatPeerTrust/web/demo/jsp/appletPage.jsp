<%@ page import="java.security.SecureRandom" %>
<%@ page import="java.net.URL" %>
<%  
		String archives=
			"./jws/DemoApplet.jar,./jws/DemoClient.jar,./jws/jena.jar,"+
				"./jws/commons-httpclient-3.0-rc2.jar,./jws/xercesImpl.jar,"+
				"./jws/minrt.jar,./jws/peertrust.jar,./jws/bouncyCastle.jar,"+
				"./jws/commons-codec-1.3.jar,./jws/commons-logging.jar,"+
				"./jws/commons-logging-api.jar,./jws/commons-logging-optional.jar,"+
				"./jws/log4j.jar,./jws/icu4j.jar,./jws/jsse.jar,./jws/xercesImpl.jar"; 
			
		StringBuffer buf=new StringBuffer(128);
		//buf.append("window.open('");
		buf.append("http://");
		buf.append(request.getLocalAddr());
		buf.append(":");
		buf.append(request.getLocalPort());
		//buf.append("/");
		buf.append(request.getContextPath());
		 
		String base=buf.toString();
		
		String sessionRandom=Long.toString((new SecureRandom()).nextLong());
		session.setAttribute("sessionRandom",sessionRandom);
		//buf.delete(0,buf.length());
		//buf.append("window.open('");
		buf.insert(0,"window.open('");
		//buf.insert(0,"createProgressBar('_ProgressBarContainer',6);");
		buf.append("/DemoPeerTrustServlet/service?negoSessionID="); ///DemoPeerTrustServlet/service
		buf.append(sessionRandom);//session.getId());
		buf.append("&negoResource=");
		buf.append(request.getParameter("negoResource"));
		buf.append("','_self');");
		
		String reloadJsCmd=buf.toString();
		
		buf.setLength(0);//delete(0,buf.length());
		//http://127.0.0.1:7703/myapp-0.1-dev/DemoPeerTrustServlet/launch_1.jnlp
		buf.append(base);
		buf.append("/DemoPeerTrustServlet/launch_1.jnlp?negoSessionID="); 
		buf.append(sessionRandom);//session.getId());
		buf.append("&negoResource=");
		buf.append(request.getParameter("negoResource"));
		//buf.append("','_self')");
		
		String launchURL=buf.toString();
		session.setAttribute("WAIT","waiting");
		
		//URL urlBase= new URL(base);
%>
<html>
<head>
	<title>Peertrust Demo Start Page</title>
	<link rel="stylesheet" 
			href="<%=base%>/css/pt_style.css" 
			type="text/css" media="all" >
	
	<script language="JavaScript" 
			src="<%=base%>/js/menu_view.js"></script>
	<BASE href="<%=base%>">
</head>

<body onload="toggleApplet();registerSession('<%=session.getId()%>');"> 

<div style="width:100%;hight:100%">
<!-- applet  
		name="pt_applet" 
		id="pt_applet"
		codebase="<%=base%>"
		code="org.peertrust.demo.client.applet.DemoApplet.class"
		archive="<%=archives%>"
		mayscript
		width="100%"
		height="100%"
		style="width:100%;height:100%">
<PARAM name="java_code" value="org.peertrust.demo.client.applet.DemoApplet.class">
<PARAM name="java_codebase" value="<%=base%>">
<PARAM name="java_archive" value="<%=archives%>">
<PARAM name="type" value="application/x-java-applet;version=1.5">
<PARAM name="CODE_BASE_STR" value="<%=base%>">
<PARAM name="negoSessionID" value="<%=sessionRandom%>">
<PARAM name="negoResource" value="blablaRes">
<PARAM name="remotePeerIP" value="<%=request.getLocalAddr()%>">
<PARAM name="appContext" value="<%=request.getContextPath()%>">
<PARAM name="serviceServletPath" value="<%=application.getInitParameter("ServiceServletPath")%>">
<PARAM name="webAppURLPath" value="/myapp-0.1-dev/PeerTrustCommunicationServlet">
</applet -->

<!-- 000000000000000000000000000000000000000000000000000000000000000000000000000000000 -->
<%@ taglib uri="/WEB-INF/tld/c.tld" prefix="c" %>

<OBJECT classid="clsid:8AD9C840-044E-11D1-B3E9-00805F499D93" 
		name="pt_applet" 
		codebase="http://java.sun.com/products/plugin/1.2.2/jinstall-1_2_2-win.cab#Version=1,2,2,0"
		id="pt_object_applet" 
		style="width:100%;height:100%">
<PARAM name="code" value="org.peertrust.demo.client.applet.DemoApplet.class">
<PARAM name="codebase" value="<%=base%>">
<PARAM name="archive" value="<%=archives%>">
<PARAM name="type" value="application/x-java-applet;version=1.5">
<PARAM name="CODE_BASE_STR" value="<%=base%>">
<PARAM name="negoSessionID" value="-4729128329947453543">

<PARAM name="negoResource" value="blablaRes">
<PARAM name="remotePeerIP" value="<%=request.getLocalAddr()%>">
<PARAM name="appContext" value="<%=request.getContextPath()%>">
<PARAM name="serviceServletPath" value="<%=application.getInitParameter("ServiceServletPath")%>">
<PARAM name="webAppURLPath" value="/myapp-0.1-dev/PeerTrustCommunicationServlet">
<PARAM name="mayscript" value="true">
<COMMENT>
<EMBED 	type="application/x-java-applet;version=1.5" 
		name="pt_applet" 
		id="pt_embed_applet" 
		pluginspage="http://java.sun.com/j2se/1.5.0/download.html" 
		code="org.peertrust.demo.client.applet.DemoApplet.class" 
		codebase="<%=base%>" 
		archive="<%=archives%>" 
		CODE_BASE_STR="<%=base%>" 
		negoSessionID="111111" 
		negoResource="blablaRes" 
		remotePeerIP="<%=request.getLocalAddr()%>" 
		appContext="<%=request.getContextPath()%>" 
		serviceServletPath="<%=application.getInitParameter("ServiceServletPath")%>" 
		webAppURLPath="/myapp-0.1-dev/PeerTrustCommunicationServlet"
		mayscript="true"
		style="width:100%;height:100%"/>
<NOEMBED>
       <p> Unable to start Plug-in. </p>
</NOEMBED>
</COMMENT>
</OBJECT>

</div>
</body>

</html>