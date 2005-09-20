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
				
		buf.setLength(0);//delete(0,buf.length());
		buf.append(base);
		buf.append("/jws/DemoApplet.jar,");
		buf.append(base);
		buf.append("/jws/DemoClient.jar,");
		buf.append(base);
		buf.append("/jws/jena.jar,");
		buf.append(base);
		buf.append("/jws/commons-httpclient-3.0-rc2.jar,");
		buf.append(base);
		buf.append("/jws/xercesImpl.jar,");
		buf.append(base);
		buf.append("/jws/minrt.jar,");
		buf.append(base);
		buf.append("/jws/peertrust.jar,");
		buf.append(base);
		buf.append("/jws/bouncyCastle.jar,");
		buf.append(base);
		buf.append("/jws/commons-codec-1.3.jar,");
		buf.append(base);
		buf.append("/jws/commons-logging.jar,");
		buf.append(base);
		buf.append("/jws/commons-logging-api.jar,");
		buf.append(base);
		buf.append("/jws/commons-logging-optional.jar,");
		buf.append(base);
		buf.append("/jws/log4j.jar,");
		buf.append(base);
		buf.append("/jws/icu4j.jar,");
		buf.append(base);
		buf.append("/jws/jsse.jar,");
		buf.append(base);
		buf.append("/jws/xercesImpl.jar"); 
		archives=buf.toString();
%>
<html>
<head>
	
	<title>Peertrust Demo Start Page</title>
	<BASE href="<%=base%>">
	<!-- link rel="stylesheet" 
			href="<%=base%>/css/pt_style.css" 
			type="text/css" media="all" -->
	<link rel="js_applet_control_1" type="text/js" href="js/applet_control.js">
	<link rel="js_applet_control_2" type="text/js" href="<%=base%>/demo/js/applet_control.js">
	
	<script language="JavaScript" 
			src="<%=base%>/demo/js/applet_control.js"></script>
	
</head>

<body><!--  onload="registerSession('<%=session.getId()%>');" --> 

<div style="width:100%;hight:100%">

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
<PARAM name="negoSessionID" value="<%=session.getId()%>">
<PARAM name="serverPeerName" value="eLearn">
<PARAM name="negoResource" value="blablaRes">
<PARAM name="remotePeerIP" value="<%=request.getLocalAddr()%>">
<PARAM name="appContext" value="<%=request.getContextPath()%>">
<PARAM name="serviceServletPath" value="<%=application.getInitParameter("ServiceServletPath")%>">
<PARAM name="webAppURLPath" value="/demo/PeerTrustCommunicationServlet">
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
		negoSessionID="<%=session.getId()%>" 
		serverPeerName="eLearn"
		negoResource="blablaRes" 
		remotePeerIP="<%=request.getLocalAddr()%>" 
		appContext="<%=request.getContextPath()%>" 
		serviceServletPath="<%=application.getInitParameter("ServiceServletPath")%>" 
		webAppURLPath="/demo/PeerTrustCommunicationServlet"
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