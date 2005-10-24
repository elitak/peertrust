
<%@page import="org.peertrust.demo.resourcemanagement.Resource"%>
<%@ taglib uri="/WEB-INF/tld/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tld/peertrust_demo_taglib.tld" prefix="pt" %>

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

<pt:PT_Header 	ptEvaluatorClassName="org.peertrust.demo.servlet.jsptags.TagsPeerTrustEvaluator"
				cacheKey="cacheKey">
	<pt:PT_ParamCreate name="membership">
		<pt:IfPropertyHold 	property="peertrustCommunityMemberPolicy(Requester)"
							valueToSet="ptComMember"/>
		
		<!-- pt:ElseIfPropertyHold 	property="ieeeMemberPolicy(Requester)"	valueToSet="ieee"/ -->
		
		<pt:ElseNoPropertyHold 	valueToSet="nothing"/>
	</pt:PT_ParamCreate>
</pt:PT_Header>	


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
		<pt:PT_NegotiableContent cacheKey="cacheKey">
			<pt:IfExhibitAttribute name="membership" value="ptComMember">
				<table border="1" style="vertical-align: middle;width: 100%;">
					<tr style="background-color: buttonface">
						<td>Name</td>
						<td>Email</td>
						<td>Role/Task</td>
					</tr>
					<tr>
						<td>Daniel</td>
						<td>daniel@l3s.de</td>
						<td>Project owner, main developer</td>
					</tr>
					<tr>
						<td>Patrice</td>
						<td>patrice@l3s.de</td>
						<td>developer</td>
					</tr>
					<tr>
						<td>congo</td>
						<td>congo@l3s.de</td>
						<td>developer</td>
					</tr>
				</table>
			</pt:IfExhibitAttribute>
			<pt:ElseExhibitNoAttribute>
				<table 	border="1"
						style="vertical-align: middle;width: 100%;">
					<tr style="background-color: buttonface">
						<td>Name</td>
						<td>Role/Task</td>
					</tr>
					<tr>
						<td>Daniel</td>
						<td>Project owner, main developer</td>
					</tr>
					<tr>
						<td>Patrice</td>
						<td>developer</td>
					</tr>
				</table>
				<br/>
				 <a href="javascript:requestCredential('peertrustCommunityMember')">Get peertrust comminity membership to get developper email</a>
			</pt:ElseExhibitNoAttribute>
		</pt:PT_NegotiableContent> 
	</div>

</body>
</html> 