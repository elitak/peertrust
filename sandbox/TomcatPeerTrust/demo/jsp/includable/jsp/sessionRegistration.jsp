<%@ page import="java.security.SecureRandom" %>
<%@ page import="org.peertrust.demo.resourcemanagement.Resource" %>
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

		Resource resourceToInclude= (Resource)request.getAttribute("resource");	
		String postponedUrl=request.getRequestURL().toString();//"../html/no_content.html";
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
	
	<title>Peertrust Demo Start Page</title>
	<BASE href="<%=base%>">
	<link rel="js_applet_control_2" type="text/js" href="<%=base%>/demo/js/applet_control.js">
	<script language="JavaScript" 
			src="<%=base%>/demo/js/applet_control.js"></script>
</head>

<body onload="window.status = "registering session .....!";registerSession('<%=session.getId()%>');" --> 

<div style="width:100%;hight:100%">
	<big>Registering pt session ....</big>
</div>
</body>

</html>