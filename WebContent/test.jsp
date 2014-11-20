<%@ page language="java" contentType="text/html; charset=Cp1256" pageEncoding="Cp1256"%>
<%@ page import="java.net.*"%> 

<%

out.println("file encoding=" + System.getProperty("file.encoding") + "<br>");
out.println("jnu encoding=" + System.getProperty("sun.jnu.encoding") + "<br>");
out.println("def request encoding=[" + request.getCharacterEncoding() + "]<br>");
 
String query0 = request.getParameter("q");
//query0 = URLDecoder.decode(query0);
//query0 = new String(query0.getBytes("Cp1252"), "Cp1256");
out.println("As it is: " + query0 + "<br>");


////////////////////////////////////////////////////////
////////////////////////////////////////////////////////
//Handling stupid problem with Resin and Tomcat
String query;
String userDir = System.getProperty("user.dir"); 
//out.println(userDir);
if(userDir.indexOf("eclipse") != -1) { //Tomcat
//String defaultEncodingName = request.getCharacterEncoding();
query = new String(query0.getBytes(), "Cp1256"); //"Cp1252"

} else {  //almost Resin
String defaultEncodingName = request.getCharacterEncoding();
query = new String(query0.getBytes(defaultEncodingName), "Cp1256"); //"Cp1252"
//queryEscaped = query;
//queryEscaped.replaceAll("\\\"", "&quot;");

}

out.println("Second: " + query + "<br>");

////////////////////////////////////////////////////////
////////////////////////////////////////////////////////


%>

		  <form method="GET" name="SearchForm">
			<input type="text" dir=rtl lang=ar name="q" size="20">
			<input type="submit" value="Try" name="B1">
		  </form>

