<!--  %@ page import="java.security.SecureRandom" % -->
<!--  %@ page import="java.net.URL" % -->
<!--  %@ page import="org.peertrust.demo.servlet.NegotiationObjects" % -->
<!--  %@ page import="org.peertrust.demo.servlet.ServletPeerTrustEventListener" % -->
<!-- %@ page import="org.apache.log4j.Logger"% -->
<!-- %@ page import="org.peertrust.demo.servlet.NegotiationOutcome"% -->
<%@page import="org.peertrust.demo.resourcemanagement.Resource"%>
<!-- jsp:directive.taglib prefix="c" uri="http://java.sun.com/jsp/jstl/functions"/ -->
<!-- %@ taglib uri="/jslt/core_rt" prefix="c" % -->  

<%@ taglib uri="/WEB-INF/tld/c.tld" prefix="c" %>



<html>
<head>
	<link rel="stylesheet" type="text/css" href="../css/sddm.css" />
</head>

<body>

	<div class="header_top">
	
	</div>
	
	<div class="menu_left">
	
		<script language="JavaScript" type="text/javascript" src="../js/menu.js"></script>
		
		<!--------Start Menu---------->
		<div class="mainDiv" state="0">
		<div class="topItem" classOut="topItem" classOver="topItemOver" onMouseOver="Init(this);" >&nbsp;Demo Menu 1</div>
		<div class="dropMenu" >
		<div class="subMenu" state="0">
		<span class="subItem" classOut="subItem" classOver="subItemOver"><a href="http://www.dynamicdrive.com">Dynamic Drive</a></span><BR />
		<span class="subItem" classOut="subItem" classOver="subItemOver"><a href="http://www.javascriptkit.com">JavaScript Kit</a></span><BR />
		<span class="subItem" classOut="subItem" classOver="subItemOver"><a href="http://www.codingforums.com">Coding Forums</a></span><BR />
		<span class="subItem" classOut="subItem" classOver="subItemOver"><a href="http://www.builder.com">Builder.com</a></span><BR />
		<span class="subItem" classOut="subItem" classOver="subItemOver"><a href="http://www.cssdrive.com">CSS Drive</a></span>
		</div>
		</div>
		</div>
		
		<!--------End Menu---------->
		<BR />
		<!--------Start Menu---------->
		<div class="mainDiv" state="0">
		<div class="topItem" classOut="topItem" classOver="topItemOver" onMouseOver="Init(this)" >&nbsp;Demo Menu 2</div>
		<div class="dropMenu" >
		<div class="subMenu" state="0">
		<span class="subItem" classOut="subItem" classOver="subItemOver"><a href="http://www.slashdot.org">Slash Dot</a></span><BR />
		<span class="subItem" classOut="subItem" classOver="subItemOver"><a href="http://news.com">News.com</a></span><BR />
		<span class="subItem" classOut="subItem" classOver="subItemOver"><a href="http://wired.com">Wired News</a></span>
		</div>
		</div>
		</div>
		
		<!--------End Menu---------->
		<BR />
		<!--------Start Menu---------->
		<div class="mainDiv" state="0">
		<div class="topItem" classOut="topItem" classOver="topItemOver" onMouseOver="Init(this)" >&nbsp;Demo Menu 3</div>
		<div class="dropMenu" >
		<div class="subMenu" state="0">
		<span class="subItem" classOut="subItem" classOver="subItemOver"><a href="http://cnn.com">CNN</a></span><BR />
		<span class="subItem" classOut="subItem" classOver="subItemOver"><a href="http://msnbc.com">MSNBC</a></span><BR />
		<span class="subItem" classOut="subItem" classOver="subItemOver"><a href="http://news.bbc.co.uk">BBC News</a></span>
		</div>
		</div>
		</div>
		
		<!--------End Menu---------->
	</div>
	 <%System.out.println("service.jsp: mark1");%>
	<div class="main_display">	
	  	<jsp:include page="<%=((Resource)request.getAttribute("resource")).getUrl()%>"></jsp:include>	 
	</div>

</body>
</html> 