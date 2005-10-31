<%@ page import="java.security.SecureRandom" %>
<%@ page import="org.peertrust.demo.resourcemanagement.Resource" %>
<%@ page import="org.peertrust.demo.resourcemanagement.ProtectedResource" %>
<%@ page import="java.net.URL" %>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="org.peertrust.demo.servlet.NegotiationObjects" %>
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
		Vector creds=((ProtectedResource)resourceToInclude).getCredentials();
		NegotiationObjects negoObjects=NegotiationObjects.createAndAddForAppContext(application);
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

<body onload="window.status = 'credential download page!';">

	<jsp:include page="/html/header.html"></jsp:include>	
	<jsp:include page="/html/menu.html"></jsp:include>	
	<div class="main_display">	
		<span style="text-align:center;">
			<big>The Negotiation has failed.</big><br/>
			<big>Please download the cedentialtials bellow</big>			
		</span>
		<table style='width:100%'>
				<tr><td>Nr.</td><td>Credential Link</td></tr>
				<%
				  	int i=1;
					Iterator it=creds.iterator();
					String name;
					String desc;
					while(it.hasNext()){
						name=(String)it.next();
						desc=negoObjects.getTrustManager().getCredentialDescription(name);
						if(desc==null){
							desc=name;
						}
					    out.println("<tr><td>"+i+"</td>"+"<td>");
					    out.println(
					    		"<a href=\"javascript:requestCredential('"+
					    		name+"')\">"+
					    		desc+"</a>");
					    out.println("</td></tr>");
					    i++;
				  	} 
				%>
			</table>
	</div>

</body>
</html> 
