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
	Resource resourceToInclude= (Resource)request.getAttribute("resource");	
	String urlToInclude=(resourceToInclude!=null)?resourceToInclude.getUrl():"../html/no_content.html";
%>

<html>
<head>
	<link rel="stylesheet" type="text/css" href="../css/sddm.css" />
	<link rel="stylesheet" type="text/css" href="../css/pt_style.css" />
	<link rel="_script_applet_control" type="text/js" href="../js/applet_control.js"/>
	<link rel="_script_menu" type="text/js" href="../js/menu.js"/>
	
	<script language="JavaScript" src="../js/applet_control.js">
	</script>
</head>

<body onload="alertTest();">

	<div class="header_top">
	
	</div>
	
	<div class="menu_left">
	
		<script language="JavaScript" type="text/javascript" src="../js/menu.js"></script>
		
		<!--------Start Menu---------->
		<div class="mainDiv" state="0">
		<div class="topItem" classOut="topItem" classOver="topItemOver" onMouseOver="Init(this);" >&nbsp;Demo Info</div>
		<div class="dropMenu" >
		<div class="subMenu" state="0">
		<span class="subItem" classOut="subItem" classOver="subItemOver"><a href="includable/html/peertrust_explained.html">Peer trust explained</a></span><BR />
		<span class="subItem" classOut="subItem" classOver="subItemOver"><a href="includable/html/tomcat_demo.html">Demo</a></span><BR />
		<span class="subItem" classOut="subItem" classOver="subItemOver"><a href="includable/html/faq.html">FAQ</a></span><BR />
		<span class="subItem" classOut="subItem" classOver="subItemOver"><a href="includable/html/setup.html">Setup</a></span><BR />
		<span class="subItem" classOut="subItem" classOver="subItemOver"><a href="includable/html/configuration.html">Configuration</a></span><BR />
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
		<span class="subItem" classOut="subItem" classOver="subItemOver"><a href="javascript:toggleVisualization()">Toggle Visualization</a></span><BR />
		<span class="subItem" classOut="subItem" classOver="subItemOver"><a href="javascript:uninstallPeerTrust()">Uninstall PeerTrust</a></span>
		<span class="subItem" classOut="subItem" classOver="subItemOver"><a href="javascript:uninstallPeerTrust()">Credential management</a></span>
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
		<span class="subItem" classOut="subItem" classOver="subItemOver"><a href="includable/html/links.html">Links</a></span><BR />
		<span class="subItem" classOut="subItem" classOver="subItemOver"><a href="includable/html/presentations.html">Presentations</a></span><BR />
		<span class="subItem" classOut="subItem" classOver="subItemOver"><a href="includable/jsp/dev_corner.html">Developper Corner</a></span>
		</div>
		</div>
		</div>
		
		<!--------End Menu---------->
	</div>
	 <%System.err.println("service.jsp: mark1\n"+"including resource:"+resourceToInclude);%>
	<div class="main_display">	
	  	<jsp:include page="<%=urlToInclude%>"></jsp:include>	 
	</div>

</body>
</html> 