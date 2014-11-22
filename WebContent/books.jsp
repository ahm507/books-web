<%@ page language="java" contentType="text/html; charset=Cp1256" pageEncoding="Cp1256"%>
<%@ page import="waqf.viewer.*, waqf.books.*"%>
<%@ page errorPage="error.jsp"%>

 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Cp1256">
<title>استعراض كتب المكتبة الإسلامية</title>
<STYLE> <!--    @import url(style.css);		--> </STYLE>
</head>
<body dir=rtl> 

<%@include file="header.jspf"%>

<br>
<br>


<table width="771" align="center">
	<tr><td>
		
		  
		  <form method="GET" name="SearchForm" action="hits.jsp">
<%
		String error = request.getParameter("err");
		if(error != null && error.length() != 0) {
			if(error.equals("empty")) {
				out.println("<font color=red>برجاء كتابة كلمات البحث</font><br>");
			}
		}
%>   
		 
			<input type="text" dir="rtl" lang="ar" name="q" size="20"/>
			
			<input type="submit" value="Search" name="B1"/>
		  </form>


		  
		  <ul>
		  <li>  
		  يتم البحث بأي من الكلمات ويتم ترتيب النتائج لتبدأ بأكثر 
		  النتائج تعلقا بكلمات البحث
		  .
		  </li>
		  <li>  
		   للبحث بكلمات متطابقة إستخدم علامات التنصيص، على سبيل المثال 
		   <font color="red">
		  "قال الله عز وجل" </font>

		  </li>
		</ul>
		      
		  
	</td></tr>
</table>
		  
		  
		

<table width=771 align=center cellpadding="5" cellspacing="5"> <tr> <td>

	
<%
//Read preperties files and list all avilable books with the right URL.
//The philosophy is render all groups and books until I find no match

//Logger logger = Logger.getLogger("books.jsp");

String booksFile = application.getRealPath("/WEB-INF/books.properties");
Books books = new Books();
books.loadSettings(booksFile);

boolean groupRendered = false;
int groupsCount = books.getGroupCount();
for(int group = 1 ; group <= groupsCount; group++) {
	groupRendered = false;
	String groupTitle = books.getGroupTitle(group);

	int booksCount = books.getGroupBooksCount(group);
	for (int book = 1 ; book <= booksCount ; book++) {
		boolean bookEnable = books.isBookEnabled(group, book);
		if(bookEnable) {
			if( ! groupRendered) {
				out.println("</blockquote><b>" + groupTitle + "</b><blockquote>");
				groupRendered = true;
			}
			String bookTitle = books.getBookTitle(group, book);
			//resin workaround
			Object args[] = new Object[] {new Integer(group), new Integer(book), bookTitle};
			out.println(String.format("<li><a href=book.jsp?bid=g%db%d>%s</a></li>", args));
		}
	}
}
	
%>
 
</td> </tr> </table>
<br>
<br>


<%@include file="footer.jspf"%>

</body>
</html>
