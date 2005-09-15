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
<!-- *****************************content************************************************ -->
		<table width="90%" border="0" align="center">
		  <tr>
	        <td colspan="2" align="center"><h3>CONFERENCE PRESENTATIONS</h3>
	        </td>
		    </tr>
		  <tr>
	        <td align="right" valign="top">2005</td>
	        <td align="left"><table width="95%" border="0" align="right">
	          <tr>
	            <td align="left" valign="top">28/07/05</td>
	            <td align="left"><p>Reasoning Web 2005<br>
	              Msida, Malta<br>
	                  <a href="demo/data/pdf/pub/20050728_REWERSE_SS.pdf">Negotiating Trust on the Semantic Web </a></p>
	              </td>
	          </tr>
	            <tr>
	              <td align="left" valign="top">07/07/05</td>
	              <td align="left"><p>                Dagstuhl Seminar on &quot;Semantic Grid: The Convergence of Technologies&quot;<br>
	                Schloss Dagstuhl, Wadern, Germany<br>
	                <a href="demo/data/pdf/20050707_Dagstuhl_SemGrid.pdf">Policy-Based Negotiation for Grid Service Authorization</a></p></td>
	            </tr>
	            <tr>
	              <td width="11%" align="left" valign="top">04/06/05</td>
	              <td align="left"><p>IEEE 6th International Workshop on Policies for Distributed Systems and Networks (POLICY 2005)<br>
	                Stockholm, Sweden<br>
	            <a href="demo/data/pdf/pub/20050604_POLICY.pdf">Driving and Monitoring Provisional Trust Negotiation with Metapolicies </a></p></td>
	            </tr>
	            <tr>
	              <td align="left" valign="top">01/06/05</td>
	              <td align="left"><p>2nd European Semantic Web Conference (ESWC'05)<br>
	            Heraklion, Greece <br>
	            <a href="demo/data/pdf/pub/20050601_ESWC05.pdf">Ontology Based Policy Specification and Management </a></p></td>
	            </tr>
	            <tr>
	              <td align="left" valign="top">10/05/05</td>
	              <td align="left"><p>Workshop on Interoperability of Web-Based Educational Systems<br>
	                14th International World Wide Web Conference (WWW'05) <br>
	            Chiba, Japan <br>
	            <a href="demo/data/pdf/pub/20050510_WWW05.pdf">Interoperability of Peer-to-Peer Networks: Opening P2P to the rest of the World</a></p></td>
	            </tr>
	        </table></td>
		    </tr>
		  <tr>
	        <td align="right" valign="top">2004</td>
	        <td align="left"><table width="95%" border="0" align="right">
	          <tr>
	            <td align="left" valign="top">07/11/04</td>
	            <td align="left"><p>Workshop on Trust, Security and Reputation on the Semantic Web<br>
	              3rd International Semantic Web Conference (ISWC 2004)<br>
	              Hiroshima, Japan<br>
	              <a href="demo/data/pdf/pub/20041107_ISWC04.pdf">How to Exploit Ontologies in Trust Negotiation</a></p>
	              </td>
	            </tr>
	          <tr>
	            <td align="left" valign="top">30/08/04</td>
	            <td align="left"><p>Workshop on Secure Data Management in a Connected
	                World (SDM'04)<br>
	                30th International Conference
	                on Very Large Data Bases<br>
	Toronto, Canada<br>
	<a href="demo/data/pdf/pub/20040830_SMD_Peertrust.pdf">PeerTrust: Automated Trust Negotiation for Peers on the Semantic Web</a> </p>
	            </td>
	            </tr>
	          <tr>
	            <td align="left" valign="top">24/08/04</td>
	            <td align="left"><p>3rd International Conference on Adaptive Hypermedia
	                and Adaptive
	Web-Based Systems<br>
	Eindhoven, Netherlands<br> 
	<a href="demo/data/pdf/pub/20040824_AH04.pdf">PROS: a Personalized Ranking Platform for Web Search</a> </p>
	            </td>
	            </tr>
	          <tr>
	            <td align="left" valign="top">22/05/04</td>
	            <td align="left"><p>Trust on the Web. Developers Day<br>
	              Thirteenth International World
	                  Wide Web Conference<br>
	                  New York, USA<br>
	                  <a href="demo/data/pdf/pub/20040522_WWW04_peertrust.pdf">PeerTrust</a></p>
	              </td>
	            </tr>
	          <tr>
	            <td align="left" valign="top">18/05/04</td>
	            <td align="left"><p>2nd Workshop on Semantics in P2P and Grid Computing<br>
	              Thirteenth
	                  International World Wide Web Conference<br>
	                  New York,
	                    USA<br>
	                    <a href="demo/data/pdf/pub/20040518_WWW04_negotiationGrid.pdf">Trust Negotiation on the Grid
	                    </a> </p>
	              </td>
	            </tr>
	            <tr>
	              <td width="11%" align="left" valign="top">12/05/04</td>
	              <td align="left"><p>1st European Semantic Web Symposium<br>
	                Heraklion,
	                    Greece<br>
	                    <a href="demo/data/pdf/pub/20040512_ESWS_noRegistration.pdf">No Registration Needed: How to Use Declarative Policies and Negotiation to Access Sensitive Resources on the Semantic Web </a></p>
	                </td>
	              </tr>
	          </table>
	        </td>
		    </tr>
		  <tr>
	        <td align="right" valign="top"> 2003</td>
	        <td align="left"><table width="95%" border="0" align="right">
	            <tr>
	              <td width="11%" align="left" valign="top">11/11/03</td>
	              <td align="left">1st LatinAmerican Web Congress<br>
	          Santiago, Chile<br>
	          <a href="demo/data/pdf/pub/20031111_LaWeb_hubs.pdf">Finding Related Hubs and Authorities </a></td>
	              </tr>
	            <tr>
	              <td align="left" valign="top">15/09/03</td>
	              <td align="left"> Tribunal de Estudios Avanzados<br>
	          Universidad Aut&oacute;noma de Madrid<br>
	          Madrid, Spain<br>
	          <a href="demo/data/pdf/pub/TEA.pdf">Finding Hubs for Personalized Search: Different Ranks to Different Users </a></td>
	              </tr>
	          </table>
	        </td>
		    </tr>
		  <tr>
	        <td colspan="2" align="center"><h3>&nbsp;</h3>
	        </td>
		    </tr>
	      <tr>
	        <td colspan="2" align="center"><h3>OTHERS</h3>
	        </td>
	      </tr>
	      <tr>
	        <td align="right" valign="top">2005</td>
	        <td align="left"><table width="95%" border="0" align="right">
	          <tr>
	            <td align="left" valign="top">10/06/05</td>
	            <td align="left">Laboratory for Intelligent Information Systems Seminar, Link&ouml;ping University<br>
	    Link&ouml;ping, Sweden <br>
	    <a href="demo/data/pdf/pub/20050610_Linkoping_Seminar.pdf">Negotiating Trust and Security on the Semantic Web </a></td>
	          </tr>
	            <tr>
	              <td width="11%" align="left" valign="top">27/05/05</td>
	              <td align="left">ELENA project EU Review<br>
	          Vienna, Austria <br>
	          <a href="demo/data/pdf/pub/20050527_ELENA_review.pdf">Automated Negotiation of Authentication and Authorization </a></td>
	            </tr>
	        </table></td>
	      </tr>
	      <tr>
	        <td width="6%" align="right" valign="top">2004</td>
	        <td width="92%" align="left"><table width="95%" border="0" align="right">
	          <tr>
	            <td align="left" valign="top">17/11/04</td>
	            <td align="left">Learning Lab Lower Saxony Infolunch<br>
	    Hanover, Germany<br>
	    <a href="demo/data/pdf/pub/20041117_Edutella_Interoperability.pdf">Increasing Interoperability in Edutella: Simple Query Interface and Semantic Mappings</a></td>
	            </tr>
	          <tr>
	            <td align="left" valign="top">24/08/04</td>
	            <td align="left"><p>The 2nd European Summer School on Ontological
	                Engineering and the Semantic Web (SSSW-2004)<br>
	      Cercedilla, Madrid<br>
	      Best Presentation Award
	      <br>
	      <a href="demo/data/pdf/pub/20040724_SSSW04.ppt">12 Steps to Academic Success </a> </p>
	              </td>
	            </tr>
			  <tr>
	            <td width="11%" align="left" valign="top">01/03/04</td>
	            <td align="left"><p>REWERSE kick-off meeting, WG I2<br>
	                Munich, Germany<br>
	                Trust Negotiation                 </p>
	              </td>
	            </tr>
			  <tr>
	            <td width="11%" align="left" valign="top">25/02/04</td>
	            <td align="left">Learning Lab Lower Saxony Infolunch<br>
	              Hanover, Germany<br>
	              <a href="demo/data/pdf/pub/TN_L3S_Infolunch_20040225.pdf">Trust Negotiation
	              </a><br></td>
	            </tr>
	          <tr>
	            <td width="11%" align="left" valign="top">22/01/04</td>
	            <td align="left">Digital Enterprise Research Institute<br>
	              Innsbruck, Austria<br>
	              <a href="demo/data/pdf/pub/trustNegotiation20040122.pdf">Trust Negotiation
	              </a><br></td>
	            </tr>
	        </table>
	        </td>
	      </tr>
	      <tr>
	        <td align="right" valign="top"> 2003</td>
	        <td align="left"><table width="95%" border="0" align="right">
	            <tr>
	              <td width="11%" align="left" valign="top">05/11/03</td>
	              <td align="left">L3S Research Seminar 2003<br>
	                Hanover, Germany<br>
	                <a href="demo/data/pdf/pub/automatedTrustNegotiation20031105.pdf">Automated Trust Negotiation (ATN) </a><br></td>
	              </tr>
	            <tr>
	              <td align="left" valign="top">26/02/03</td>
	              <td align="left"><p>Learning Lab Lower Saxony Infolunch<br>
	                Hanover, Germany<br>
	                <a href="demo/data/pdf/pub/googleForEducationalResources-Infolunch20030226.pdf">Google for Educational Resources</a> <br>
	              </p>
	                </td>
	              </tr>
	          </table>
	        </td>
	      </tr>
	    </table>
<!-- ************************************************************************************ -->		
	</div>

</body>
</html> 