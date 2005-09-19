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
	<link rel="stylesheet" type="text/css" href="/demo/css/sddm.css" />
	<link rel="stylesheet" type="text/css" href="/demo/css/pt_style.css" />
	<link rel="stylesheet" type="text/css" href="/demo/css/l3s_staff.css" />
	<link rel="_script_applet_control" type="text/js" href="/demo/js/applet_control.js"/>
	<link rel="_script_menu" type="text/js" href="/demo/js/menu.js"/>
	
	<script language="JavaScript" src="/demo/js/applet_control.js">
	</script>
	<script language="JavaScript" src="/demo/js/menu.js">
	</script>
</head>

<body>

	<jsp:include page="/html/header.html"></jsp:include>	
	<jsp:include page="/html/menu.html"></jsp:include>	
	<div class="main_display">	
		<iframe src="http://www.l3s.de/~olmedilla/" style="width:100%;height:100%"></iframe>
	</div>

</body>
</html> 