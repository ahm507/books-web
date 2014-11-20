<%@ page language="java" contentType="text/html; charset=Cp1256" pageEncoding="Cp1256"%>
<%@ page import="waqf.indexer.*"%>
<%@ page errorPage="error.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Build Book</title>
<STYLE> <!--    @import url(../style.css);		--> </STYLE>
</head>
<body dir=rtl>

<%@include file="header.jspf"%>
 
<br/>
<table cellpadding="5"> <tr> <td>

<h1> Build Book Index</h1>



<%
String bid = request.getParameter("bid");
if(bid == null || bid.length() == 0) {
	out.println("No book to build!");
	return;
}

String rootPath = application.getRealPath("/WEB-INF/");
String dataPath = rootPath + "/data/" + bid ;
String indexPath = rootPath + "/index/" + bid;

Indexer indexer = new Indexer();
indexer.indexDoc(dataPath, indexPath, out);

%>

 

</td> </tr> </table>
<br/>

<%@include file="footer.jspf"%>


</body>
</html>