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

<body onunload="destroyPTClient()">

<div class="header_top">



<!-- OBJECT classid="clsid:8AD9C840-044E-11D1-B3E9-00805F499D93" 
		name="pt_applet" 
		codebase="http://java.sun.com/products/plugin/1.2.2/jinstall-1_2_2-win.cab#Version=1,2,2,0"
		id="pt_applet" >
<PARAM name="java_code" value="org.peertrust.demo.client.applet.DemoApplet.class">
<PARAM name="java_codebase" value="<%=base%>">
<PARAM name="java_archive" value="<%=archives%>">
<PARAM name="type" value="application/x-java-applet;version=1.5">
<PARAM name="CODE_BASE_STR" value="http://127.0.0.1:7703/myapp-0.1-dev">
<PARAM name="negoSessionID" value="-4729128329947453543">

<PARAM name="negoResource" value="blablaRes">
<PARAM name="remotePeerIP" value="127.0.0.1">
<PARAM name="appContext" value="myapp-0.1-dev/">
<PARAM name="serviceServletPath" value="PeerTrustCommunicationServlet">
<PARAM name="webAppURLPath" value="/myapp-0.1-dev/PeerTrustCommunicationServlet">

<COMMENT>
<EMBED 	type="application/x-java-applet;version=1.5" 
		name="pt_applet" 
		pluginspage="http://java.sun.com/j2se/1.5.0/download.html" 
		java_code="org.peertrust.demo.client.applet.DemoApplet.class" 
		java_codebase="<%=base%>" 
		java_archive="<%=archives%>" 
		CODE_BASE_STR="http://127.0.0.1:7703/myapp-0.1-dev" 
		negoSessionID="<%=sessionRandom%>" 
		negoResource="blablaRes" 
		remotePeerIP="<%=request.getLocalPort()%>" 
		appContext="myapp-0.1-dev/" 
		serviceServletPath="PeerTrustCommunicationServlet" 
		webAppURLPath="/myapp-0.1-dev/PeerTrustCommunicationServlet"
		maiscript="true"/>
<NOEMBED>

       <p> Unable to start Plug-in. </p>

</NOEMBED>
</COMMENT>
</OBJECT -->
<applet  
		name="pt_applet" 
		id="pt_applet"
		codebase="<%=base%>"
		code="org.peertrust.demo.client.applet.DemoApplet.class"
		archive="<%=archives%>"
		mayscript
		class="pt_applet"
		width="100%"
		height="100%">
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

</applet>
</div>
<div class="menu_left">

<table class="menu_container">
<tr>
<td>
<ul>
<li class="section">Technolgy Section</li>
<li onmouseover="on_menu_item_mouseover(this)"
				 onmouseout="on_menu_item_mouseout(this)"
				   onclick="getResource('ai_basic')">AI Basics</li>
	<li onmouseover="on_menu_item_mouseover(this)"
				 onmouseout="on_menu_item_mouseout(this)"
				   onclick="onButtonClick('PeerTrust')">Ontologies</li>
	<li onmouseover="on_menu_item_mouseover(this)"
				 onmouseout="on_menu_item_mouseout(this)"
				   onclick="onButtonClick('PeerTrust')">PeerTrust</li>
</ul>
</td>
</tr>
<tr>
<td>
<ul>
	<li class="section">Another Section</li>
	<li onmouseover="on_menu_item_mouseover(this)"
					 onmouseout="on_menu_item_mouseout(this)"
					   onclick="onButtonClick('PeerTrust')">AI Basics</li>
		<li onmouseover="on_menu_item_mouseover(this)"
					 onmouseout="on_menu_item_mouseout(this)"
					   onclick="onButtonClick('PeerTrust')">Ontologies</li>
		<li onmouseover="on_menu_item_mouseover(this)"
					 onmouseout="on_menu_item_mouseout(this)"
					   onclick="onButtonClick('PeerTrust')">PeerTrust</li>

</ul>
</td>
</tr>
</table>
</div>

<div class="main_display">

<!--OBJECT codetype="application/java-archive"
        classid="java:org.peertrust.demo.client.applet.DemoApplet"
		 archive="DemoApplet.jar"
		 codebase="http://127.0.0.1:7703/myapp-0.1-dev/jws">
        
</OBJECT -->
<!-- http://127.0.0.1:7703/myapp-0.1-dev/DemoPeerTrustServlet?negoResource=dada.html -->

		 
 <IFrame name="_DisplayIFrame" id="_DisplayIFrame" src="" class="display_iframe"></IFRAME>
</div>

</body>
</html>
