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

<pt:PT_Header 	ptEvaluatorClassName="org.peertrust.demo.servlet.jsptags.PtEvaluatorMock"
				cacheKey="cacheKey">
	<pt:PT_ParamCreate name="param1">
		<pt:IfPropertyHold 	property="prop10"
							valueToSet="value10"/>
		
		<pt:ElseIfPropertyHold 	property="prop11"
							valueToSet="value11"/>
		
		<pt:ElseIfPropertyHold 	property="prop12"
							valueToSet="value12"/>
		
		<pt:ElseIfPropertyHold 	property="prop13"
							valueToSet="value13"/>

		<pt:ElseNoPropertyHold 	valueToSet="value1No"/>
	</pt:PT_ParamCreate>
	
	<pt:PT_ParamCreate name="param2">
		<pt:IfPropertyHold 	property="prop20"
							valueToSet="value20"/>
		
		<pt:ElseIfPropertyHold 	property="prop21"
							valueToSet="value21"/>
		
		<pt:ElseIfPropertyHold 	property="prop22"
							valueToSet="value22"/>
		
		<pt:ElseIfPropertyHold 	property="prop23"
							valueToSet="value23"/>
							
		<pt:ElseNoPropertyHold 	valueToSet="value2No"/>
	</pt:PT_ParamCreate>
	
	<pt:PT_ParamCreate name="param3">
		<pt:IfPropertyHold 	property="prop30"
							valueToSet="value30"/>
		
		<pt:ElseNoPropertyHold 	valueToSet="value3No"/>
	</pt:PT_ParamCreate>
</pt:PT_Header>

<html>
	<body>
		<!-- %=pageContext.getAttribute(PTHeaderTag.NEGO_PARAMS_CACHE).toString() % -->
		<%System.out.println("////////////***********************************//////////////////"); %>
		<%=pageContext.getAttribute("cacheKey").toString() %>	
		<br/>
		<h1>content for param1</h1><br/>
		<pt:PT_NegotiableContent cacheKey="cacheKey">
			<pt:IfExhibitAttribute name="param1" value="value3243">
				content for param1/value3243
			</pt:IfExhibitAttribute>
			<pt:ElseIfExhibitAttribute name="param1" value="value103">
				content for param1/value103 at <%=(new java.util.Date()).toString() %>
			</pt:ElseIfExhibitAttribute>
			<pt:ElseIfExhibitAttribute name="param1" value="value10">
				content for param1/value10 at <%=(new java.util.Date()).toString() %>
				<br/>
				your ip:<%=request.getRemoteAddr()%>
			</pt:ElseIfExhibitAttribute>
			<pt:ElseExhibitNoAttribute>
				nothing for you!
			</pt:ElseExhibitNoAttribute>
		</pt:PT_NegotiableContent>
 
		
		<h1>content for param2</h1><br/>
		<pt:PT_NegotiableContent cacheKey="cacheKey">
			<pt:IfExhibitAttribute name="param2" value="value3243">
				content for param2/value3243
			</pt:IfExhibitAttribute>
			<pt:ElseIfExhibitAttribute name="param2" value="value103">
				content for param2/value103 at <%=(new java.util.Date()).toString() %>
			</pt:ElseIfExhibitAttribute>
			<pt:ElseIfExhibitAttribute name="param2" value="value10">
				content for param2/value10 at <%=(new java.util.Date()).toString() %>
				<br/>
				your ip:<%=request.getRemoteAddr()%>
			</pt:ElseIfExhibitAttribute>
			<pt:ElseExhibitNoAttribute>
				nothing for you yet! please subscribe for param2! 
			</pt:ElseExhibitNoAttribute>
		</pt:PT_NegotiableContent>
		
		<h1>content for param3</h1><br/>
		<pt:PT_NegotiableContent cacheKey="cacheKey">
			<pt:IfExhibitAttribute name="param3" value="value3No">
				content for param3/value3No
			</pt:IfExhibitAttribute>
			<pt:ElseExhibitNoAttribute> 
			</pt:ElseExhibitNoAttribute>
		</pt:PT_NegotiableContent>
	</body>
</html>