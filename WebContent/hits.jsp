<%@ page language="java" contentType="text/html; charset=Cp1256" pageEncoding="Cp1256"%>
<%@ page import="waqf.viewer.*,waqf.display.Search.HitInfo,java.net.URLEncoder"%> 
<%@ page errorPage="error.jsp"%>


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>äÊÇÆÌ ÇáÈÍË Ýí ÇáãßÊÈÉ ÇáÅÓáÇãíÉ</title>
<STYLE> <!--    @import url(style.css);		--> </STYLE>
</head>
<body dir=rtl>

<%@include file="header.jspf"%>

<br>

<%
//1) initialize settings file
String booksFile = application.getRealPath("/WEB-INF/books.properties");
//booksFile += "/";
Books books = new Books();
books.loadSettings(booksFile);
String indexPath = application.getRealPath("/WEB-INF/index");

String query0 = request.getParameter("q");
if(query0 == null || query0.length() == 0) {
	//query0 = "";
	response.sendRedirect("search.jsp?err=empty");
	return;
}

//* String query = new String(query0.getBytes("Cp1252"), "Cp1256");

String query = "";
//String queryEscaped = "";
////////////////////////////////////////////////////////
////////////////////////////////////////////////////////
//Handling stupid problem with Resin and Tomcat
String userDir = System.getProperty("user.dir"); 
//out.println(userDir);
if(userDir.indexOf("eclipse") != -1) { //Tomcat
//	String defaultEncodingName = request.getCharacterEncoding();
	query = new String(query0.getBytes(), "Cp1256"); //"Cp1252"
	
} else {  //almost Resin
	String defaultEncodingName = request.getCharacterEncoding();
	query = new String(query0.getBytes(defaultEncodingName), "Cp1256"); //"Cp1252"
	//queryEscaped = query;
	//queryEscaped.replaceAll("\\\"", "&quot;");
	
}
////////////////////////////////////////////////////////
////////////////////////////////////////////////////////

%>


<table width="771" align="center">
	<tr><td>
		
	  <form method="GET" name="SearchForm" action="hits.jsp">
<%
		String error = request.getParameter("err");
		if(error != null && error.length() != 0) {
			if(error.equals("empty")) {
				out.println("<font color=red>ÈÑÌÇÁ ßÊÇÈÉ ßáãÇÊ ÇáÈÍË</font><br>");
			}
		}
 %>    
		 
			<input type="text" dir="rtl" lang="ar" name="q" size="20"/>
			
			<input type="submit" value="Search" name="B1"/>
		  </form>
		
		  <ul>
		  <li>  
		  íÊã ÇáÈÍË ÈÃí ãä ÇáßáãÇÊ æíÊã ÊÑÊíÈ ÇáäÊÇÆÌ áÊÈÏÃ ÈÃßËÑ 
		  ÇáäÊÇÆÌ ÊÚáÞÇ ÈßáãÇÊ ÇáÈÍË
		  .
		  </li>
		  <li>  
		   ááÈÍË ÈßáãÇÊ ãÊØÇÈÞÉ ÅÓÊÎÏã ÚáÇãÇÊ ÇáÊäÕíÕ¡ Úáì ÓÈíá ÇáãËÇá 
		   <font color="red">
		  "ÞÇá Çááå ÚÒ æÌá" </font>

		  </li>
		</ul>
		
		  
	</td></tr>
</table>
		  
<hr style="width: 800px; " align="center">		  
		



<table width=771 align=center >
<tr> <td>

<%
	out.println("&nbsp;&nbsp;");
out.println("äÇÊÌ ÇáÈÍË Úä :");
out.println("\"" + query + "\"");

	

//scope All: Search all books in all groups
String scope = request.getParameter("scope");
if(scope == null || scope.length() == 0) {
	scope = "all";
}
if(scope.equals("all")) {
	out.println("<table border=0 cellspacing=5><tr><td>");
	int groupsCount = books.getGroupCount();
	for(int group = 1 ; group <= groupsCount; group++) {
		out.println("<tr><td colspan=2><b>" + books.getGroupTitle(group) + "</b></td></tr>");
		int booksCount = books.getGroupBooksCount(group);
		for(int book = 1 ; book <= booksCount ; book++) {
//*			String bookScope = String.format("g%db%d", group, book);
	//resin workaround {
	Object args[] = new Object[] {new Integer(group), new Integer(book)};
	String bookScope = String.format("g%db%d", args);
	//resin workaround }
	
//*			String bookPath = String.format("%s/g%db%d", indexPath, group, book);
	//resin workaround
	Object args2[] = new Object[] {indexPath, new Integer(group), new Integer(book)};
	String bookPath = String.format("%s/g%db%d", args2);

	int hitsCount = Search.getBookHitsCount(bookPath, query);
//*			String linkText = String.format("&nbsp;&nbsp;&nbsp;&nbsp;<tr><td><a href=book.jsp?bid=%s>%s</a> :<td><a href=hits.jsp?scope=%s&q=%s> %d ÍÏíË </a>", 
//*					bookScope, books.getBookTitle(group, book), bookScope, URLEncoder.encode(query0), hitsCount);
	//resin workaround
	Object args3[] = new Object[] {books.getBookTitle(group, book), bookScope, URLEncoder.encode(query, "Cp1256"), new Integer(hitsCount)};
	String linkText = "";
	if(hitsCount > 0) {
		linkText = String.format("&nbsp;&nbsp;&nbsp;&nbsp;<tr><td>%s :<td><a href=hits.jsp?scope=%s&q=%s> %d </a>", args3);
	} else {
		linkText = String.format("&nbsp;&nbsp;&nbsp;&nbsp;<tr><td>%s :<td><!--a href=hits.jsp?scope=%s&q=%s--> %d", args3);
	}

	out.println(linkText);
		}
	}
	out.println("</td></tr></table><br><br>");
}
////////////////////////////////////////////////////////////////////////////////////
//scope specific group: search all books in a specific group
else if(scope.indexOf("b") == -1) { //g1
//int bookCount = books.getGroupBooksCount();
	
	
}
////////////////////////////////////////////////////////////////////////////////////
//scope specific book: search that book and display hits 10 by 10
else {
	final int showHits = 20;
	String startWithStr = request.getParameter("start");
	int startWith = 0;
	if(startWithStr != null && startWithStr.length() != 0) {
		startWith = Integer.parseInt(startWithStr);
	}

	
//	Simply search and show hits
//*	String bookPath = String.format("%s/%s", indexPath, scope);
	//resin workaround
	Object args4 [] = new Object[] {indexPath, scope};
	String bookPath = String.format("%s/%s", args4);	

	HitInfo[] hits = Search.SeachBook(bookPath, query, startWith, showHits);
	int hitsCount = 0;
	if(hits.length > 0) {
		hitsCount = hits[0].totalCount;
	}
	
	int nextHits = Math.min(startWith + showHits, hitsCount);
	boolean noNextPage = false;
	boolean noPrevPage = false;
	if(startWith + showHits >= hitsCount) {
		noNextPage = true;
	}
	if(startWith - showHits < 0) {
		noPrevPage = true;
	}
	
	int prevHits = Math.max(0, startWith - showHits);

	String queryOrig = request.getParameter("q");
	if(queryOrig == null || queryOrig.length() == 0) {
		queryOrig = "";
	}

	out.println("&nbsp;&nbsp;&nbsp;");
	
//*	out.println(String.format("<a href='hits.jsp?scope=%s&q=%s&start=%d'> ÊÇáí </a> | ", 
//*							scope, query, nextHits ));
	//resin workaround
	Object args5[] = new Object[] {scope, query, new Integer(nextHits)};
	if(noNextPage == false) {
	out.println(String.format("<a href='hits.jsp?scope=%s&q=%s&start=%d'><b> ÊÇáí </b></a>", 
			args5 ));
	}
	
	if(noNextPage == false && noPrevPage == false) {
		out.println(" | ");
	}
	
//*	out.println(String.format("<a href='hits.jsp?scope=%s&q=%s&start=%d'> ÓÇÈÞ </a>", 
//*			scope, query, prevHits ));
	Object args6[] = new Object[] {scope, query, new Integer(prevHits)};
	if(noPrevPage == false) {
	out.println(String.format("<a href='hits.jsp?scope=%s&q=%s&start=%d'><b> ÓÇÈÞ </b></a>", 
	args6 ));
	}
	out.println("<br>&nbsp;&nbsp;&nbsp;ÚÑÖ ÇáäÊÇÆÌ ÈÏÇíÉ ãä ");
	out.println(startWith+1);
	out.println("<br>&nbsp;&nbsp;&nbsp;ÇáÚÏÏ Çáßáí ááäÊÇÆÌ  ");
	out.println(hitsCount);
	
	
	out.println("<br><br><table>");
	//for(int i = startWith ; i < Math.min(hitsCount, startWith+showHits) ; i++ ) {
		for(int i = 0 ; i < hits.length ; i++ ) {
//		Document doc = hits.doc(i);
		String id = hits[i].id;
		String title = hits[i].title;
		String summery = hits[i].summeryHighlighted;
		title = Display.cleanupTitle(title);
//*		String outText = String.format("<tr><td>%d&nbsp&nbsp</td><td><a href=book.jsp?bid=%s&id=%s> %s </a></td></tr>", 
//*				i+1, scope, id, title);
		//Object args7[] = new Object[] {new Integer(i+1+startWith), scope, id, title};
		Object args7[] = new Object[] {scope, id, title};
		String outText = String.format("<a href=book.jsp?bid=%s&id=%s> %s </a><br>", 
		args7);

		out.println(outText);
		out.println(summery);
		out.println("<br><br>");
	}
	out.println("</table><br><br>");
	//ins.close();
}
%>


</td></tr>
</table>

<%@include file="footer.jspf"%>


</body>
</html>

<%!


%>
