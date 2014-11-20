<%@ page language="java" contentType="text/html; charset=Cp1256" pageEncoding="Cp1256"%>
<%@ page import="waqf.indexer.*, waqf.viewer.*"%>
<%@ page errorPage="error.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Book Administration</title>
<STYLE> <!--    @import url(../style.css);		--> </STYLE>
<script language="javascript">
function build(bid) 
{
	var answer = confirm ("This will rebuild the index files of the document, " +
	"if it is already exist it will be removed then rebuilt, press OK if this is what you want.");
	if(answer) 
	{
		window.location="build.jsp?bid=" + bid;
	}

}


</script>


</head>
<body dir=rtl>

<%@include file="header.jspf"%>

<br/>
<table cellpadding="5"> <tr> <td>

<table border=1>

<%
//Read preperties files and list all avilable books with the right URL.
//The philosophy is render all groups and books until I find no match

//Logger logger = Logger.getLogger("books.jsp");

String booksFile = application.getRealPath("/WEB-INF/index");
booksFile = application.getRealPath("/WEB-INF/books.properties");
Books books = new Books();
books.loadSettings(booksFile);

boolean groupRendered = false;
int groupsCount = books.getGroupCount();
for(int group = 1 ; group <= groupsCount; group++) {
	groupRendered = false;
	String groupTitle = books.getGroupTitle(group);

	int booksCount = books.getGroupBooksCount(group);
	for (int book = 1 ; book <= booksCount ; book++) {
		//boolean bookEnable = books.isBookEnabled(group, book);
		//if(bookEnable) {
		if( ! groupRendered) {
			out.println("<tr><td></blockquote><b>" + groupTitle + "</b><blockquote></td></tr>");
			groupRendered = true;
		}
		String bookTitle = books.getBookTitle(group, book);
		out.println("<tr>");

		Object args1[] = new Object[] {bookTitle};
		out.println(String.format("<td colspan=2>%s<br></td>", args1));
		//out.println(String.format("<td><a href=build.jsp?bid=g%db%d>Build the book</a></li><br></td>", group, book));
		
		Object args2[] = new Object[] {new Integer(group), new Integer(book)};
		out.println(String.format("<td><a href='javascript:build(\"g%db%d\");'>Build the book</a></li><br></td>", args2));
		
		out.println("</tr>");
		//}
	}
}
	
%>

</table>


</td> </tr> </table>
<br/>

<%@include file="footer.jspf"%>

</body>
</html>