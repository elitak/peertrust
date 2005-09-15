<%@page import="org.peertrust.demo.resourcemanagement.Resource"%>
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
	<script language="JavaScript" src="<%=base%>/demo/js/menu.js">
	</script>
</head>

<body>

	<jsp:include page="/demo/html/header.html"></jsp:include>	
	<jsp:include page="/demo/html/menu.html"></jsp:include>	
	<div class="main_display">	
		<table border="0" cellpadding="0" cellspacing="0" width="770">
				<tr>

				  <td valign="top">
					<h3>PeerTrust</h3>
					<b>Automated Trust Negotiation for Peers on the Semantic Web</b>
					<p>In the PeerTrust project we are developing and investigating policy languages to describe trust and security 
						requirements on the Semantic Web. Such policies will be one component of a run-time system that can
						negotiate to establish trust on the Semantic Web. The PeerTrust system uses guarded distributed logic programs as the basis 
						for a simple yet expressive policy and trust negotiation language, built upon the rule layer of the Semantic Web layer cake.
						<a href="demo/jsp/home.jsp" target="_blank"><br></a></p>
					<p align="center"><img src="demo/img/appletNetworkDiagram.gif" height="286" width="550"></p>
					</td>
				</tr>
		</table>
	</div>

</body>
</html> 