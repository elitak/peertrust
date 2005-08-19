<%@ taglib uri="/WEB-INF/tld/peertrust_demo_taglib.tld" prefix="pt" %>
 
<%@page contentType="text/html"%>
<!-- %@ taglib uri="/WEB-INF/peertrust_demo_taglib.tld" prefix="pt" % -->
<%@ page import="org.peertrust.demo.servlet.jsptags.PTHeaderTag" %>
<%  
  response.setHeader("Pragma", "no-cache");
  response.setHeader("Cache-Control", "no-cache");
  response.setHeader("Cache-Control","no-store" );
  response.setDateHeader("Expires", 0);
  //String cacheKey="cccacheKey";
%>

<pt:PT_Header 	ptEvaluatorClassName="org.peertrust.demo.servlet.jsptags.TagsPeerTrustEvaluator"
				cacheKey="cacheKey">
	<pt:PT_ParamCreate name="membership">
		<pt:IfPropertyHold 	property="acmMember(alice)"
							valueToSet="acm"/>
		
		<pt:ElseIfPropertyHold 	property="ieeeMember(alice)"
							valueToSet="ieee"/>
		
		<pt:ElseNoPropertyHold 	valueToSet="nothing"/>
	</pt:PT_ParamCreate>
</pt:PT_Header>	

<html>
	<body>
		<!-- %=pageContext.getAttribute(PTHeaderTag.NEGO_PARAMS_CACHE).toString() % -->
		<%System.out.println("////////////***********************************//////////////////"); %>
		<%=pageContext.getAttribute("cacheKey").toString() %>	
		<br/>
		<h1>content for Membership</h1><br/>
		<pt:PT_NegotiableContent cacheKey="cacheKey">
			<pt:IfExhibitAttribute name="membership" value="acm">
				content for acm member
			</pt:IfExhibitAttribute>
			<pt:ElseIfExhibitAttribute name="membership" value="ieee">
				content for ieee member
			</pt:ElseIfExhibitAttribute>
			<pt:ElseExhibitNoAttribute>
				you are not member of acm nor ieee!
			</pt:ElseExhibitNoAttribute>
		</pt:PT_NegotiableContent> 
	</body>
</html>