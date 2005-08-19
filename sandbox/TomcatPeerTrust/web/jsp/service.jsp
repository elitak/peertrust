<%@ page import="java.security.SecureRandom" %>
<%@ page import="java.net.URL" %>
<%@ page import="org.peertrust.demo.servlet.NegotiationObjects" %>
<%@ page import="org.peertrust.demo.servlet.ServletPeerTrustEventListener" %>
<%@ page import="org.apache.log4j.Logger"%>
<%@ page import="org.peertrust.demo.servlet.NegotiationOutcome"%>

<!-- jsp:directive.taglib prefix="c" uri="http://java.sun.com/jsp/jstl/functions"/ -->
<!-- %@ taglib uri="/jslt/core_rt" prefix="c" % -->  

<%@ taglib uri="/WEB-INF/tld/c.tld" prefix="c" %>


<%  
		
		StringBuffer buf=new StringBuffer(128);
		buf.append("http://");
		buf.append(request.getLocalAddr());
		buf.append(":");
		buf.append(request.getLocalPort());
		buf.append(request.getContextPath());
		 
		String base=buf.toString();
		
		session.setAttribute("WAIT","waiting");
		
		NegotiationObjects negoObjects= 
				NegotiationObjects.createAndAddForAppContext(config);
		ServletPeerTrustEventListener ptEListener= 
					negoObjects.getPeerTrustEventListener();
		String negoID= (String)request.getParameter("negoSessionID");
		String negoResource=(String)request.getParameter("negoResource");
		
		String resPath=null;
		if(negoResource!=null){
			resPath=(String)application.getInitParameter(negoResource);
		}

		if(resPath==null){
			resPath=(String)application.getInitParameter("DefaultResource");	
		}	
		
		String userName= request.getParameter("userName");
		if(userName!=null){
			userName="username="+userName;
		}
		
		System.out.println("\nnegoResource:"+negoResource+
							" negoID:"+negoID+
							" userName:"+userName+
							" resPath:"+resPath);
		NegotiationOutcome negoOutcome=
			new NegotiationOutcome(request,config,application);
				
		System.out.println(
				"\n---------------"+
				negoOutcome);
		
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

<body onload="registerSession('<%=session.getId()%>')">

<div class="header_top">

</div>
<div class="menu_left">

<table class="menu_container">
<tr>
<td>
<ul>
<li class="section">Demo Info</li>
<li onmouseover="on_menu_item_mouseover(this)"
				 onmouseout="on_menu_item_mouseout(this)"
				   onclick="loadPage('<%=base%>/jsp/service.jsp?negoResource=setup')">Setup</li>
	<li onmouseover="on_menu_item_mouseover(this)"
				 onmouseout="on_menu_item_mouseout(this)"
				   onclick="getResource('configuration')">Configuration</li>
	<li onmouseover="on_menu_item_mouseover(this)"
				 onmouseout="on_menu_item_mouseout(this)"
				   onclick="toggleApplet()">Toggle Applet</li>	
</ul>
</td>
</tr>

<tr>
<td>
<ul>
	<li class="section">Service</li>
	<li onmouseover="on_menu_item_mouseover(this)"
					 onmouseout="on_menu_item_mouseout(this)"
					   onclick="getResource('publication')">Publication</li>
	<li onmouseover="on_menu_item_mouseover(this)"
				 onmouseout="on_menu_item_mouseout(this)"
				   onclick="loadPage('<%=base%>/jsp/service.jsp?negoResource=presentation')">Presentation</li>
	<li onmouseover="on_menu_item_mouseover(this)"
				 onmouseout="on_menu_item_mouseout(this)"
				   onclick="loadPage('<%=base%>/jsp/service.jsp?negoResource=links')">Links</li>

</ul>
</td>
</tr>
</table>
</div>
 
<div class="main_display">	
  <c:choose>
    <c:when test="<%=(negoResource==null)%>">
    	<jsp:text>no res requested!</jsp:text>
    </c:when>
    <c:otherwise>
    	<jsp:include page="<%=negoOutcome.getRealNegoResource()%>">								
					<jsp:param name="Dada" value="dada"/>
		</jsp:include>
	 </c:otherwise>
  </c:choose>	 
 
</div>

</body>
</html>