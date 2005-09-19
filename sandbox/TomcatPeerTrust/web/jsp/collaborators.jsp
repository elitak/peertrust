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
		<table border="0" cellpadding="0" cellspacing="0" width="770">
				<tr>
				  <td valign="top">
					<p><b>Collaborations</b></p>
					<ul>
						<li>University of Illinois at Urbana-Champaign, <img src="/demo/img/note1c.gif" height="9" width="13" border="0">
						<a href="http://dais.cs.uiuc.edu/trustbuilder/index.html" target="_blank">TrustBuilder
						Project</a>                        
						<li><a href="http://www.learninglab.de/english/projects/rewerse.html">REWERSE</a> Network
						  of Excellence
                        <li>IST/EU <a href="http://www.learninglab.de/english/projects/elena.html">ELENA</a> project
                        <li>Internet Security Research Lab at Brigham Young University, <img src="/demo/img/note1c.gif" height="9" width="13" border="0"> <a href="http://isrl.cs.byu.edu/" target="_blank">TrustBuilder
					        Project</a>					
                      <li>The National Center for Supercomputing Applications (<a href="http://www.ncsa.uiuc.edu/">NCSA</a>)
                      <li>Digital Enterprise Research Institute (<a href="http://www.deri.org">DERI</a>)
					</ul>

					<p><b>				    Home Web Page at Sourceforge.net:</b> <a href="http://sourceforge.net/projects/peertrust/">http://sourceforge.net/projects/peertrust/</a></p>
				</td>
			</tr>
		</table>
					
	</div>

</body>
</html> 