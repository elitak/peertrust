<%@ page import="java.security.SecureRandom" %>
<%@ page import="org.peertrust.demo.resourcemanagement.Resource" %>
<%@ page import="java.net.URL" %>
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

		Resource resourceToInclude= (Resource)request.getAttribute("resource");	
		String postponedUrl=resourceToInclude.getUrl();//"../html/no_content.html";
		
//		Resource resourceToInclude= (Resource)request.getAttribute("resource");	
//		String postponedUrl=request.getRequestURL().toString();//"../html/no_content.html";
//		if(resourceToInclude!=null){
//			String completeURL=resourceToInclude.getUrl();
//			if(completeURL!=null){				
//				if(completeURL.startsWith(request.getContextPath())){
//					postponedUrl=completeURL.substring(completeURL.length());
//				}else{
//					postponedUrl=completeURL;
//				}
//			}
//		}	
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

<body onload="registerSession('<%=session.getId()%>');window.status = 'registering session .....!';loadPage('<%=postponedUrl%>');">

	<jsp:include page="/html/header.html"></jsp:include>	
	<jsp:include page="/html/menu.html"></jsp:include>	
	<div class="main_display">	
		<span style="text-align:center;"><big>Registering pt session ....</big></span>
	</div>

</body>
</html> 
