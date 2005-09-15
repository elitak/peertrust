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
		
		<p align="justify">
		<font size="5"><b>The
		PeerTrust Project</b></font></p>
		<font size="3"><br>
		The
		PeerTrust project is investigating trust negotiation in Semantic Web
		and P2P environments. Within the program, digital credentials can be
		signed XML or RDF statements that express peer properties, and
		policies are expressed as logic programs that tie resource access
		to
		required credentials. The ability to refer to peers, to credentials,
		or to other resources in PeerTrust logic programs lets us express the
		iterative exchange of credentials during a trust negotiation process.</font><br>
		<br>
		<p align="justify"><big><font size="4"><big>The
		PeerTrust Prototype</big></font></big></p>
		
		<br>
		<font size="3">The
		PeerTrust 1.0 prototype is available free at <a
		 href="http://www.l3s.de/peertrust">http://www.l3s.de/peertrust</a>
		or
		<a href="http://sourceforge.net/projects/peertrust">http://sourceforge.net/projects/peertrust</a>.
		PeerTrust 1.0&rsquo;s outer
		layer is a signed Java application or applet program. It keeps queues
		of propositions that are in the process of being proved, parses
		incoming queries, translates them to the PeerTrust language, and
		passes them to the inner layer. Its inner layer answers queries by
		reasoning about PeerTrust policy rules and certificates using Prolog
		metainterpreters (in MINERVA Prolog, whose Java implementation
		offers excellent portability) and returns the answers to the outer
		layer. PeerTrust 1.0 imports RDF metadata to represent policies for
		access to resources and uses X.509 certificates and the Java
		Cryptography Architecture for signatures. It employs secure socket
		connections between negotiating parties, and its facilities for
		communication and access to security-related libraries are in Java.<br>
		<br>
		Currently, we provide two modes of execution: secure mode and demo
		mode. In the secure mode, secure channels are used for communication
		(currently secure sockets), credentials retrieved are verified and real
		credentials are sent over the network. As creating credentials is not
		an easy task, we provided a demo mode in which the credentials are
		faked (no real credentials exist) and the communication is over normal
		sockets (not secure). This ease the modification for demostration
		purposes.<br>
		<br>
		The following are three demos that shows how the visualization of our
		prototype works. Two representations are shown:<br>
		</font>
		<ul>
		
		  <li><font size="3">Sequence diagram: shows the interchange of
		messages between entities in the order they are sent or received.</font></li>
		  <li><font size="3">Tree diagram: this diagram focuses on
		displaying the evolution of the negotiation as a search tree. Our
		algorithm currently uses a breath-first-search mechanism (although it
		is configurable to use different algorithms) in order to find a
		solution. In addition, a feature called "replay" is added in order to
		show in this tree the order in which the messages were received.</font></li>
		</ul>
		The demos are
		available from the following links (Macromedia
		flash is required):<br>
		<ul>
		  <li><a href="demo/jsp/demoSequenceHigh.jsp">Demo Sequence
		Diagram</a></li>
		  <li><a href="demo/jsp/demoTreeHigh.jsp">Demo Tree Diagram</a></li>
		  <li><a href="demo/jsp/demoTreeReplayHigh.jsp">Demo Replay Function
		in Tree Diagram</a><br>
		
		  </li>
		</ul>
		<br>
		<br>

	</div>

</body>
</html> 