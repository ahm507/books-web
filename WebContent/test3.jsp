<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="java.util.Properties"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>


<%

Properties props = System.getProperties();
out.println(props);


String defaultEncodingName = System.getProperty( "file.encoding" );
out.println("<hr>" + defaultEncodingName);

//System.setProperty("file.encoding","ANSI_X3.4-1968");
//out.println("<hr>" + defaultEncodingName);

%>



</body>
</html>