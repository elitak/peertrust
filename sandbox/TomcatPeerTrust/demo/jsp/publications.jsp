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
<!-- *******************************************content**************************************** -->
		<table width="100%" border="0" align="center" cellspacing="10">
		      <tr>
		        <td colspan="3" align="center"><h3>PUBLICATIONS</h3>
		        </td>
		      </tr>
		      <tr>
		        <td align="left" valign="top">2005</td>
		        <td align="left">Stefaan Ternier, Daniel Olmedilla, Erik Duval <br>
		          <a href="demo/data/publications/pub/p2pFederatedSearch.pdf">Peer-to-Peer versus Federated Search: towards more Interoperable Learning Object Repositories</a> <br>
		    World Conference on Educational Multimedia, Hypermedia and Telecomunications (ED-MEDIA 2005), Jun./Jul. 2005, Montreal, Canada </td>
		        <td><a href="demo/data/publications/bibtex/p2pFederatedSearch.txt">bibtex</a></td>
		      </tr>
		      <tr>
		        <td align="left" valign="top">&nbsp;</td>
		        <td align="left">Rub&eacute;n Lara, Daniel Olmedilla<br>
		          <a href="demo/data/publications/pub/2005_Discovery_and_contracting.html">Discovery and Contracting of Semantic Web Services</a><br>
		    W3C Workshop on Frameworks for Semantic in Web Services, Jun. 2005, Innsbruck, Austria</td>
		        <td><a href="demo/data/publications/bibtex/2005_Discovery_and_contracting.txt">bibtex</a></td>
		      </tr>
		      <tr>
		        <td align="left" valign="top">&nbsp;</td>
		        <td align="left">Piero A. Bonatti, Daniel Olmedilla <br>
		            <a href="demo/data/publications/ieee/protune-policy.pdf">Driving and Monitoring Provisional Trust Negotiation with Metapolicies</a><br>
		    IEEE 6th International Workshop on Policies for Distributed Systems and Networks (POLICY 2005), Jun. 2005, Stockholm, Sweden</td>
		        <td><a href="demo/data/publications/bibtex/protune-policy.txt">bibtex</a></td>
		      </tr>
		      <tr>
		        <td align="left" valign="top">&nbsp;</td>
		        <td align="left"><p>W. Nejdl, D. Olmedilla, M. Winslett, C. C. Zhang<br>
		            <a href="demo/data/publications/pub/ontoESWC05.pdf">Ontology-Based Policy Specification and Management</a><br>
		European Semantic Web Conference (ESWC 2005), May/Jun. 2005, Heraklion, Greece </p></td>
		        <td><a href="demo/data/publications/bibtex/ontoESWC05.txt">bibtex</a></td>
		      </tr>
		      <tr>
		        <td align="left" valign="top">&nbsp;</td>
		        <td align="left"><p>B. Simon, D. Massart, F. van Assche, S. Ternier, E. Duval, S. Brantner, D. Olmedilla, Z. Mikl&oacute;s <br>
		            <a href="demo/data/publications/pub/sqi.pdf">A Simple Query Interface for Interoperable Learning Repositories </a><br>
		      Workshop on Interoperability of Web-Based Educational Systems in conjunction with 14th International World Wide Web Conference (WWW'05). May, 2005, Chiba, Japan </p></td>
		        <td><a href="demo/data/publications/bibtex/sqi.txt">bibtex</a></td>
		      </tr>
		      <tr>
		        <td align="left" valign="top">&nbsp;</td>
		        <td align="left"><p>Daniel Olmedilla, Matthias Palm&eacute;r <br>
		                <a href="demo/data/publications/pub/interopP2P.pdf">Interoperability for Peer-to-Peer Networks: Opening P2P to the rest of the World </a><br>
		          Workshop on Interoperability of Web-Based Educational Systems in conjunction with 14th International World Wide Web Conference (WWW'05). May, 2005, Chiba, Japan </p>
		          </td>
		        <td><a href="demo/data/publications/bibtex/interopP2P.txt">bibtex</a></td>
		      </tr>
		      <tr>
		        <td align="left" valign="top">&nbsp;</td>
		        <td align="left"><p>Piero A. Bonatti, Daniel Olmedilla<br>
		                <a href="demo/data/publications/pub/rewerseI2-D2.pdf">Policy Language Specification </a><br>
		      Project deliverable D2, Working Group I2, EU NoE REWERSE, Mar. 2005</p></td>
		        <td><a href="demo/data/publications/bibtex/rewerseI2-D2.txt">bibtex</a></td>
		      </tr>
		      <tr>
		        <td align="left" valign="top">2004</td>
		        <td align="left"><p>T. Leithead, W. Nejdl, D. Olmedilla, K. E. Seamons, M. Winslett, T. Yu, C. C. Zhang<br>
		                <a href="demo/data/publications/pub/ontoTSRSW04.pdf">How to Exploit Ontologies for Trust Negotiation</a><br>
		      Workshop on Trust, Security and Reputation on the Semantic Web in conjunction with the 3rd International Semantic Web Conference (ISWC 2004), Nov. 2004, Hiroshima, Japan</p>
		          </td>
				  <td><a href="demo/data/publications/bibtex/ontoTSRSW04.txt">bibtex</a></td>
		      </tr>
		      <tr>
		        <td align="left" valign="top">&nbsp;</td>
		        <td align="left"><p><SPAN class=author>S. Staab, B. Bhargava, L. Lilien, A. Rosenthal, M. Winslett, M. Sloman, T. S. Dillon, E. Chang, F. K. Hussain, W. Nejdl, D. Olmedilla, V. Kashyap</SPAN><br>
		                <SPAN class=title><a href="demo/data/publications/ieee/intelligentSystems.pdf">The Pudding of Trust</a></SPAN><br>
		      IEEE Intelligent Systems Journal, <SPAN class=pagenum><A><SPAN class=issue>Vol. 19(5)</SPAN>, p</A>p. 74-88, <A><SPAN class=issue>Sep./Oct.</SPAN></A></SPAN> 2004 </p></td>
			  <td><a href="demo/data/publications/bibtex/intelligentSystems.txt">bibtex</a></td>
		      </tr>
		      <tr>
		        <td align="left" valign="top">&nbsp;</td>
		        <td align="left"><p>P. A. Bonatti, N. Shahmehri, C. Duma, D. Olmedilla, W. Nejdl, M. Baldoni, C. Baroglio, A. Martelli, V. Patti, P. Coraggio, G. Antoniou, J. Peer, N. E. Fuchs<br>
		            <a href="demo/data/publications/pub/rewerseI2-D1.pdf">Rule-based Policy Specification: State of the Art and Future Work</a><br>
		          Project deliverable D1, Working Group I2, EU NoE REWERSE, Sep. 2004</p>
		          </td>
				  <td><a href="demo/data/publications/bibtex/rewerseI2-D1.txt">bibtex</a></td>
		      </tr>
		      <tr>
		        <td align="left" valign="top">&nbsp;</td>
		        <td align="left">Paul-Alexandru Chirita, Daniel Olmedilla, Wolfgang Nejdl<br>
		            <a href="demo/data/publications/ieee/hubsWI04.pdf">Finding Related Pages Using the Link Structure
		            of the WWW</a><br>
		    IEEE/WIC/ACM International Conference on Web Intelligence
		    (WI 2004), Sep. 2004, Beijing, China</td>
			<td><a href="demo/data/publications/bibtex/hubsWI04.txt">bibtex</a></td>
		      </tr>
		      <tr>
		        <td></td>
		        <td align="left">Wolfgang Nejdl, Daniel Olmedilla, Marianne Winslett<br>
		            <a href="demo/data/publications/pub/trustVLDB04.pdf">PeerTrust: Automated Trust Negotiation
		            for Peers on the Semantic Web</a><br>
		    Workshop on Secure Data Management in a Connected World (SDM'04)
		    in conjunction with 30th International Conference on 
		    Very Large Data Bases, Aug.-Sep. 2004, Toronto, Canada</td>
			<td><a href="demo/data/publications/bibtex/trustVLDB04.txt">bibtex</a></td>
		      </tr>
		      <tr>
		        <td align="left" valign="top">&nbsp;</td>
		        <td align="left">Daniel Olmedilla, Rub&eacute;n Lara, Axel Polleres,
		          Holger Lausen<br>            
		          <a href="demo/data/publications/pub/trustServices-swswpc04.pdf">Trust Negotiation for Semantic Web Services</a><br>
		1st International Workshop on Semantic Web Services and
		  Web Process Composition in conjunction with the 2004 IEEE International Conference
		  on Web Services, Jul.. 2004, San Diego, California, USA</td>
		  <td><a href="demo/data/publications/bibtex/trustServices-swswpc04.txt">bibtex</a></td>
		      </tr>
		      <tr>
		        <td align="left" valign="top">&nbsp;</td>
		        <td align="left"><p>Bernd Simon, Peter Dolog, Zolt&aacute;n Mikl&oacute;s,
		            Daniel Olmedilla, Michael Sintek<br>
		            <a href="demo/data/publications/pub/jime04.pdf">Conceptualising Smart Spaces for Learning</a><br>
		            Journal of
		              Interactive Media in Education, 2004 (1), Special Issue on the
		          Educational Semantic Web. ISSN:1365-893X</p>
		          </td>
				  <td><a href="demo/data/publications/bibtex/jime04.txt">bibtex</a></td>
		      </tr>
		      <tr>
		        <td align="left" valign="top">&nbsp;</td>
		        <td align="left">Paul-Alexandru Chirita, Daniel Olmedilla, Wolfgang Nejdl<br>
		            <a href="demo/data/publications/pub/prosAH04.pdf">PROS: a Personalized Ranking Platform
		            for Web Search</a><br>
		    3rd International Conference on Adaptive Hypermedia and
		    Adaptive Web-Based Systems, Aug. 2004, Eindhoven, Netherlands</td>
			<td><a href="demo/data/publications/bibtex/prosAH04.txt">bibtex</a></td>
		      </tr>
		      <tr>
		        <td align="left" valign="top">&nbsp;</td>
		        <td align="left">Jim Basney, Wolfgang Nejdl, Daniel Olmedilla, Von Welch,
		          Marianne Winslett<br>
		                      <a href="demo/data/publications/pub/negotiationOnTheGrid.pdf">Negotiating
		                      Trust on the Grid</a><br>
		 2nd Workshop on Semantics in P2P and Grid Computing at the
		 Thirteenth International World Wide Web Conference, May 2004, New York, USA</td>
		 <td><a href="demo/data/publications/bibtex/negotiationOnTheGrid.txt">bibtex</a></td>
		      </tr>
		      <tr>
		        <td></td>
		        <td align="left"><p>Rita Gavriloaie, Wolfgang Nejdl, Daniel Olmedilla,
		            Kent Seamons, Marianne Winslett<br>
		                  <a href="demo/data/publications/pub/PeerTrust-NoRegistration.pdf">No Registration Needed:
		                  How to Use Declarative Policies and Negotiation to Access Sensitive
		                  Resources on the Semantic Web</a><br>
		      1st European Semantic Web Symposium, May. 2004, Heraklion,
		      Greece</p>
		        </td>
				<td><a href="demo/data/publications/bibtex/PeerTrust-NoRegistration.txt">bibtex</a></td>
		      </tr>
		      <tr>
		        <td align="left" valign="top">&nbsp;</td>
		        <td align="left"><p>Peter Dolog, Barbara Kieslinger, Zoltan Miklos, Daniel
		            Olmedilla, and Bernd Simon<br>
		            <a href="demo/data/publications/pub/digicultIssue7Highres.pdf">Creating Smart Spaces for Learning</a><br>
		            Journal of Technology Challenges for Digital Culture, April 2004
		            (7). ISSN 1609-3941</p>
		          </td>
				  <td><a href="demo/data/publications/bibtex/jime04.txt">bibtex</a></td>
		      </tr>
				<tr>
		        <td width="6%" align="left" valign="top"> 2003</td>
		              <td align="left">Paul-Alexandru Chirita, Daniel Olmedilla, Wolfgang
		                Nejdl<br>
		PROS: a Personalized Ranking Platform for Web Search<br>
		Technical Report, L3S Research Center, Nov. 2003</td>
		              </tr>
				<tr>
				  <td></td>
				  <td align="left">Paul-Alexandru Chirita, Daniel Olmedilla, Wolfgang Nejdl<br>
		                <a href="demo/data/publications/pub/hubsLaWeb03.pdf">Finding Related Hubs and Authorities</a><br>
		1st LatinAmerican Web Congress, Nov. 2003, Santiago, Chile</td>
		          <td><a href="demo/data/publications/bibtex/hubsLaWeb03.txt">bibtex</a></td>
				<tr>
		          <td></td>
		          <td align="left">Wolfgang Nejdl, Daniel Olmedilla, Marianne Winslett<br>
		              <a href="demo/data/publications/pub/PeerTrust-ATN.pdf">PeerTrust: Automated Trust Negotiation
		              for Peers on the Semantic Web</a><br>
		    Technical Report, L3S Research Center, Oct. 2003</td>
			    </tr>
		            <tr><td></td>
		              <td align="left">Daniel Olmedilla<br>
		                
		                <a href="demo/data/publications/pub/TEA.pdf">Finding Hubs for Personalized Web Search</a><br>
		            Tribunal de Estudios Avanzados, Universidad Aut&oacute;noma de Madrid
		            (UAM) Sep. 2003, Madrid, Spain</td>
		              </tr>
		            <tr><td></td>
		              <td align="left"><a href="demo/data/publications/pub/D2.2artefactsAndServiceNetworkSpecification-v2.pdf">Artefacts and Service Network Architecture Specification
		                Report version 2</a><br>
		Technical Report, Elena Project Deliverable D2.2, Jul. 2003</td>
		              </tr>
		            <tr><td></td>
		              <td align="left"><p>Daniel Olmedilla<br>
		                  <a href="demo/data/publications/pub/webBaseGuide.pdf">WebBase Guide</a><br>
		                  Technical Report, L3S Research Center, Feb. 2003</p>
		              </td>
		              </tr>
		    </table>
<!-- ****************************************************************************************** -->
	</div>

</body>
</html> 