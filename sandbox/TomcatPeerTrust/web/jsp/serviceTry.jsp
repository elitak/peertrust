<%@ page import="java.security.SecureRandom" %>
<%@ page import="java.net.URL" %>
<%@ page import="org.peertrust.demo.servlet.NegotiationObjects" %>
<%@ page import="org.peertrust.demo.servlet.ServletPeerTrustEventListener" %>
<%@ page import="org.apache.log4j.Logger"%>
<%@ page import="org.peertrust.demo.servlet.NegotiationOutcome"%>

<!-- jsp:directive.taglib prefix="c" uri="http://java.sun.com/jsp/jstl/functions"/ -->
<!-- %@ taglib uri="/jslt/core_rt" prefix="c" % -->  

<%@ taglib uri="/WEB-INF/tld/c.tld" prefix="c" %>
<!-- %@ taglib uri="/peertrustTags" prefix="pt" % -->  

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

<!--tag>
	<description>To protect child teag with peer trust or password</description>
	<name>Protect</name>
	<tag-class>org.peertrust.demo.servlet.jsptags.ProtectTag</tag-class>	
	<body-content>JSP</body-content>
</tag>
<tag>
	<description>perform the peer trust negotiation.</description>
	<name>PeerTrustProtect</name>
	<tag-class>org.peertrust.demo.servlet.jsptags.PeerTrustProtectTag</tag-class>	
	<body-content>JSP</body-content>
</tag>
<tag>
	<description>body content of PeerTrust</description>
	<name>IfAccessGranted</name>
	<tag-class>org.peertrust.demo.servlet.jsptags.IfAccessGrantedTag</tag-class>
	
	<body-content>JSP</body-content>
</tag>
<tag>
	<description>To show if access denied</description>
	<name>IfAccessDenied</name>
	<tag-class>org.peertrust.demo.servlet.jsptags.IfAccessDeniedTag</tag-class>	
	<body-content>JSP</body-content>
</tag -->
<pt:Protect>
<pt:PeerTrustProtect 	type="send"
									query="asAccess(_resource,username)"></pt:PeerTrustProtect>

<pt:IfAccessGranted>
	<html>
			<body>
					<big>Granted</big>
			</body>
	</html>
</pt:IfAccessGranted>

<pt:AccessDenied>
	<html>
			<body>
					<big>Granted</big>
			</body>
	</html>
</pt:AccessDenied>
</pt:Protect>