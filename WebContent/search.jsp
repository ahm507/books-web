<%@ page language="java" contentType="text/html; charset=Cp1256" pageEncoding="Cp1256"%>
<%@ page errorPage="error.jsp"%>


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>��� �� ������� ���������</title>
<STYLE> <!--    @import url(style.css);		--> </STYLE>

</head>
<body dir=rtl>

<%@include file="header.jspf"%>

<table width=771 align=center>
	<tr><td>
		
		  <p>�����:</p>
		  <form method="GET" name="SearchForm" action=hits.jsp>
		  <%
		String error = request.getParameter("err");
		if(error != null && error.length() != 0) {
			if(error.equals("empty")) {
				out.println("<font color=red>����� ����� ����� �����</font><br>");
			}
		}
		%>    
		 
			<input type="text" dir=rtl lang=ar name="q" size="20">
			
			<input type="submit" value="Search" name="B1">
			
			 
			
		  </form>
		  <br>
		  <ul>
		  <li>  
		  ��� ����� ��� �� ������� ���� ����� ������� ����� ����� 
		  ������� ����� ������ �����
		  .
		  </li>
		  <li>  
		   ����� ������ ������� ������ ������ ������ա ��� ���� ������ 
		   <font color="red">
		  "��� ���� �� ���" </font>

		  </li>
		</ul>
		  <br>  <br>  <br>
		  
	</td></tr>
</table>
		  
		  
		<%@include file="footer.jspf"%>  

</body>
</html>
