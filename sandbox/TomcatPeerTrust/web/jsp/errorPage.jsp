<%@ page isErrorPage="true" import="javax.naming.*" %>
<%@ page import="java.io.ByteArrayOutputStream" %>
<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="org.apache.log4j.net.SocketAppender" %>
<%@ page import="org.apache.log4j.Level" %>
<%= (new InitialContext()).getNameInNamespace()%>

<%
		Logger logger= Logger.getLogger(this.getClass());
		logger.setLevel(Level.ALL);
        logger.setAdditivity(true);
        Logger.getRootLogger().addAppender(new SocketAppender("127.0.0.1",4445));
        logger.error("jsp_erro",exception);		
 %>
Error:<%=exception.toString()%>