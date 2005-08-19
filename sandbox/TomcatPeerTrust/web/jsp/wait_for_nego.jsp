<%@page contentType="text/html"%>
<%@ page import="java.security.SecureRandom" %>
<!-- %@page import=% -->
<%  
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
%>
<html>
<head>
	<title>Please Wait...</title>
	<base href="<%=base%>"/> 	
	<script language="JavaScript" type="text/javascript" src="<%=base%>/js/menu_view.js"></script>
	<style>
		div.progressBar{
			width:400px;
			height:100px;
			border-color="black";
			border-style="solid";
		}
		
		iframe{
			visibility:hidden;
		}
	</style>
</head>
<body onload="<%=reloadJsCmd%>">

<div id="_ProgressBarContainer" class="progressBar">Negotiating </div>
	
	<!-- script language="JavaScript" type="text/javascript">
			///createProgressBar("_ProgressBarContainer",6);
			//setInterval("stopAndHideProgressBar('_ProgressBarContainer')",120000);				
	</script -->
	<iframe src="<%=launchURL%>"></iframe>
</body>
</html>