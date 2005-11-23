<!--  %@ page import="java.security.SecureRandom" % -->
<!--  %@ page import="java.net.URL" % -->
<!--  %@ page import="org.peertrust.demo.servlet.NegotiationObjects" % -->

<!-- %@ page import="org.apache.log4j.Logger"% -->
<!-- %@ page import="org.peertrust.demo.servlet.NegotiationOutcome"% -->
<%@page import="org.peertrust.demo.resourcemanagement.Resource"%>
<!-- jsp:directive.taglib prefix="c" uri="http://java.sun.com/jsp/jstl/functions"/ -->
<!-- %@ taglib uri="/jslt/core_rt" prefix="c" % -->  

<%@ taglib uri="/WEB-INF/tld/c.tld" prefix="c" %>
<%
	String contextPath=request.getContextPath();

	StringBuffer buf=new StringBuffer(128);
	//buf.append("window.open('");
	buf.append("http://");
	buf.append(request.getLocalAddr());
	buf.append(":");
	buf.append(request.getLocalPort());
	//buf.append("/");
	buf.append(contextPath);//.getContextPath());
	 
	String base=buf.toString();
	Resource resourceToInclude= (Resource)request.getAttribute("resource");	
	
	String urlToInclude="../html/no_content.html";
	if(resourceToInclude!=null){
		String completeURL=resourceToInclude.getUrl();
		if(completeURL!=null){
			if(completeURL.startsWith(contextPath)){
				urlToInclude=completeURL.substring(contextPath.length());
			}
		}
	}
%>

<html>

<head>
	<base href="<%=base%>/">
	<link rel="stylesheet" type="text/css" href="<%=base%>/demo/css/sddm.css" />
	<link rel="stylesheet" type="text/css" href="<%=base%>/demo/css/pt_style.css" />
	<link rel="stylesheet" type="text/css" href="<%=base%>/demo/css/l3s_staff.css" />
	<link rel="_script_applet_control" type="text/js" href="<%=base%>/demo/js/applet_control.js"/>
	<link rel="_script_menu" type="text/js" href="<%=base%>/demo/js/menu.js"/>
	
	<script language="JavaScript" src="<%=base%>/demo/js/applet_control.js">
	</script>
</head>

<body>

	<div class="header_top">
	
	</div>
	
	<div class="menu_left">
	
		<script language="JavaScript" type="text/javascript" src="<%=base%>/demo/js/menu.js"></script>
		
		<!--------Start Menu---------->
		<div class="mainDiv" state="0">
		<div class="topItem" classOut="topItem" classOver="topItemOver" onMouseOver="Init(this);" >&nbsp;Demo Info</div>
		<div class="dropMenu" >
		<div class="subMenu" state="0">
		<span class="subItem" classOut="subItem" classOver="subItemOver"><a href="<%=base%>/demo/jsp/includable/html/trust.html">Peer trust explained</a></span><BR />
		<span class="subItem" classOut="subItem" classOver="subItemOver"><a href="<%=base%>/demo/jsp/includable/html/tomcat_demo.html">Demo</a></span><BR />
		<span class="subItem" classOut="subItem" classOver="subItemOver"><a href="<%=base%>/demo/jsp/includable/html/faq.html">FAQ</a></span><BR />
		<!-- span class="subItem" classOut="subItem" classOver="subItemOver"><a href="<%=base%>/demo/jsp/includable/html/setup.html">Setup</a></span><BR /> -->
		<!-- span class="subItem" classOut="subItem" classOver="subItemOver"><a href="<%=base%>/demo/jsp/includable/html/configuration.html">Configuration</a></span><BR /> -->
		</div>
		</div>
		</div>
		
		<!--------End Menu---------->
		<BR />
		<!--------Start Menu---------->
		<div class="mainDiv" state="0">
		<div class="topItem" classOut="topItem" classOver="topItemOver" onMouseOver="Init(this)" >&nbsp;Session control</div>
		<div class="dropMenu" >
		<div class="subMenu" state="0">
		<span class="subItem" classOut="subItem" classOver="subItemOver"><a href="javascript:toggleApplet()">Toggle Applet</a></span><BR />
		<span class="subItem" classOut="subItem" classOver="subItemOver"><a href="javascript:toggleExternalVisualization()">Toggle Visualization</a></span><BR />
		<span class="subItem" classOut="subItem" classOver="subItemOver"><a href="javascript:uninstallPeerTrust()">Uninstall PeerTrust</a></span><BR />
		<span class="subItem" classOut="subItem" classOver="subItemOver"><a href="javascript:managePolicies()">Policy management</a></span>
		</div>
		</div>
		</div>
		
		<!--------End Menu---------->
		<BR />
		<!--------Start Menu---------->
		<div class="mainDiv" state="0">
		<div class="topItem" classOut="topItem" classOver="topItemOver" onMouseOver="Init(this)" >&nbsp;Service</div>
		<div class="dropMenu" >
		<div class="subMenu" state="0">
		<span class="subItem" classOut="subItem" classOver="subItemOver"><a href="<%=base%>/demo/jsp/includable/html/links.html">Links</a></span><BR />
		<span class="subItem" classOut="subItem" classOver="subItemOver"><a href="<%=base%>/demo/jsp/includable/html/presentations.html">Presentations</a></span><BR />
		<span class="subItem" classOut="subItem" classOver="subItemOver"><a href="<%=base%>/demo/jsp/includable/html/publications.html">Publications</a></span><BR />
		<span class="subItem" classOut="subItem" classOver="subItemOver"><a href="<%=base%>/demo/jsp/includable/jsp/dev_corner.jsp">Developper Corner</a></span>
		</div>
		</div>
		</div>
		
		<!--------End Menu---------->
	</div>
	 <%System.err.println("service.jsp: mark1\n"+"including resource:"+resourceToInclude);%>
	<div class="main_display">	
		<table style="border-style:outset;">
	  		<jsp:include page="<%=urlToInclude%>"></jsp:include>	 
	  	</table>
	</div>

</body>
</html> 