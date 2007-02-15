<%@ taglib uri="policytag" prefix="poljsp" %>
<html>
	<head>
		<script type="text/javascript" src="http://localhost:8081/MyServlet/link.js"></script>
		<title>Partners</title>
	</head>
	<body bgcolor="#EDEEDE">
		<table width="100%" height="100%" border="0">
			<tr>
				<td  align="left" valign="top">
					<a href="http://localhost:8081/MyServlet/home.html">Home</a><br><br>
					<a href="http://localhost:8081/MyServlet/project/project.html">Project</a>
					<ol>
						<li><a href="http://localhost:8081/MyServlet/project/overview.html">Overview</a></li>
						<li>Partner Information</li>
					</ol><br>
					<a href="http://localhost:8081/MyServlet/publications/publications.html">Publications</a><br><br>
					<a href="http://localhost:8081/MyServlet/download/download.html">Project Management</a><br><br>
				</td>
				<td>
					<center>
						<poljsp:policycondition policyname="IEEE_Member">
							<poljsp:iftrue>
								Information for IEEE Computer Society<BR>
							</poljsp:iftrue>
							<poljsp:iffalse>
								You have not proven that you are a member of IEEE<br>
							</poljsp:iffalse>
						</poljsp:policycondition>
				
						<poljsp:policycondition policyname="ACM_Member">
							<poljsp:iftrue>
								Information for ACM Computer Society<BR>
							</poljsp:iftrue>
							<poljsp:iffalse>
								You have not proven that you are a member of ACM<br>
							</poljsp:iffalse>
						</poljsp:policycondition>
				
						<poljsp:policycondition policyname="ACS_Member">
							<poljsp:iftrue>
								Information for ACS Computer Society<BR>
							</poljsp:iftrue>
							<poljsp:iffalse>
								You have not proven that you are a member of ACS<br>
							</poljsp:iffalse>
						</poljsp:policycondition>
					</center>
				</td>
			</tr>
		</table>
	</body>
</html>