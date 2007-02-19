<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="policytag" prefix="poljsp" %>
<html><!-- InstanceBegin template="/Templates/WebSite.dwt" codeOutsideHTMLIsLocked="false" -->
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<!-- InstanceBeginEditable name="doctitle" -->
<!-- InstanceParam name="MENU" type="text" value="overview" -->
		<script type="text/javascript" src="http://localhost:8081/MyServlet/link.js"></script>
		<title>Protune Web Demo</title>
<!-- InstanceEndEditable -->
<!-- InstanceBeginEditable name="head" -->


<!-- InstanceEndEditable -->
</head>
	<body bgcolor="#EDEEDE">
		<table width="800" align="center">
		<tr>
		<td align="left"><img src="../images/WGI2-medium.png" width="200" height="67"></td>
		<td align="center"><h2>Policy Specification, <br>
		  Enforcement and Integration</h2></td>
		<td align="right">
		  <img src="../images/REWERSE-250-medium.gif" width="200" height="61"> </td>
		</tr>
		<tr><td colspan="3"><hr></td></tr>
		<tr><td colspan="3">&nbsp;</td></tr>
		</table>
		
		<table width="800" align="center">
			<tr>
				<td width="180" align="left" valign="top">
				<table cellpadding="0" width="100%">
              <tr><td width="90"><img src="../images/home.jpg" width="58" height="40" border="1" align="absmiddle"></td>
              <td colspan="2"><a href="../home.html">Home</a></td></tr>
              <tr><td><img src="../images/overview.jpg" width="58" height="40" border="1" align="absmiddle"></td>
              <td colspan="2"> <a href="overview.html">Overview </a></td>
              </tr>
			  
			      <tr><td align="right"><img src="../images/partners.jpg" width="58" height="40" border="1" align="absmiddle"></td>
			      <td>&nbsp;&nbsp;</td>
			        <td> <a href="partners.jsp">Partners </a></td>
			      </tr>
			      <tr>
			        <td align="right"><img src="../images/project.jpg" width="58" height="40" border="1" align="absmiddle"></td>
			        <td>&nbsp;&nbsp;</td>
			        <td> <a href="project.html">Project</a></td>
		          </tr>
              <tr><td><img src="../images/publications.jpg" width="58" height="40" border="1" align="absmiddle"></td>
                <td colspan="2"> <a href="../publications/publications.html">Publications</a>	</td>
              </tr>
              <tr><td><img src="../images/internal.gif" width="58" height="40" border="1" align="absmiddle"></td><td colspan="2"> <a href="internal.html">Members </a></td>
              </tr>
			  </table>
			  </td>
				<td colspan="2" valign="top">
				<table width="95%" cellspacing="10" align="center">
				  <tr><td>
				  <!-- InstanceBeginEditable name="Content" -->
				<table border="1" align="center" cellpadding="10">
				<tr>
				  <td><strong>PARTNER NAME </strong></td>
					<poljsp:policycondition policyname="Rewerse_Member"><poljsp:iftrue>
					<td><strong>BUDGET</strong></td>
  					</poljsp:iftrue><poljsp:iffalse></poljsp:iffalse></poljsp:policycondition>
						
				</tr>
				<tr>
				  <td>Hannover University and L3S Research Center </td>
					<poljsp:policycondition policyname="Rewerse_Member"><poljsp:iftrue>
					  <td>123234 &#8364;</td>
  					</poljsp:iftrue><poljsp:iffalse></poljsp:iffalse></poljsp:policycondition>
				</tr>
				<tr>
				  <td>Heraklion (FORTH) </td>
					<poljsp:policycondition policyname="Rewerse_Member"><poljsp:iftrue>
					  <td>123234 &#8364;</td>
  					</poljsp:iftrue><poljsp:iffalse></poljsp:iffalse></poljsp:policycondition>
				  </tr>
				<tr>
				  <td>Linkoeping University </td>
					<poljsp:policycondition policyname="Rewerse_Member"><poljsp:iftrue>
					  <td>123234 &#8364;</td>
  					</poljsp:iftrue><poljsp:iffalse></poljsp:iffalse></poljsp:policycondition>
				  </tr>
				<tr>
				  <td>Naples University </td>
					<poljsp:policycondition policyname="Rewerse_Member"><poljsp:iftrue>
					  <td>123234 &#8364;</td>
  					</poljsp:iftrue><poljsp:iffalse></poljsp:iffalse></poljsp:policycondition>
				  </tr>
				<tr>
				  <td>St. Gallen University</td>
					<poljsp:policycondition policyname="Rewerse_Member"><poljsp:iftrue>
					  <td>123234 &#8364;</td>
  					</poljsp:iftrue><poljsp:iffalse></poljsp:iffalse></poljsp:policycondition>
				  </tr>
				<tr>
				  <td>Turin University </td>
					<poljsp:policycondition policyname="Rewerse_Member"><poljsp:iftrue>
					  <td>123234 &#8364;</td>
  					</poljsp:iftrue><poljsp:iffalse></poljsp:iffalse></poljsp:policycondition>
				  </tr>
				<tr>
				  <td>Vienna University</td>
					<poljsp:policycondition policyname="Rewerse_Member"><poljsp:iftrue>
					  <td>123234 &#8364;</td>
  					</poljsp:iftrue><poljsp:iffalse></poljsp:iffalse></poljsp:policycondition>
				  </tr>
				<tr>
				  <td>Zurich University </td>
					<poljsp:policycondition policyname="Rewerse_Member"><poljsp:iftrue>
					  <td>123234 &#8364;</td>
  					</poljsp:iftrue><poljsp:iffalse></poljsp:iffalse></poljsp:policycondition>
				  </tr>
																
				</table>
				  <!-- InstanceEndEditable -->
				  </td></tr></table>
		 	  </td>
			</tr>
	</table>
</body>
<!-- InstanceEnd --></html>
