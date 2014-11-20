<%@ page language="java" contentType="text/html; charset=Cp1256" pageEncoding="Cp1256"%>
<%@ page isErrorPage="true" %>

<html>
<head>
<title>Error </title>
	<STYLE> <!--    @import url(style.css);		--> </STYLE>
</head>


<body dir=rtl>

<%@ include file="header.jspf" %>

<table cellpadding=0 cellspacing=0 border="0" width="100%">
  <tr>
    <td valign=top>
	<table border="0" width="100%">
	  <tr>
		<td>
		<br><br>

<%					
					
	//Exception exception = new Exception();
	//Exception exp = new Exception();
	//exp.getClass().getName()
	if(exception != null)
	{
		//out.print("<!--");
		//out.print("<b>Technical details:</b><br>");
		out.print("<blockquote><p align=left dir=rtl>");
		String error = exception.getMessage();
		if(error == null)
			error = "";
		
		out.flush();
		exception.printStackTrace(response.getWriter());
		//exception.
		//out.print("-->");
		out.print("Excption name: " + exception.getClass().getName() + "<br>");
		
		out.print(exception);//.getMessage()
	
	}

%>

	
		<br>
		<br>
		<br>
		<br>
		<br>

					</td>
					</table>

	</td>

</table>


<!-- footer -->

<%@ include file="footer.jspf" %>
