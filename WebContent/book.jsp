<%@ page language="java" contentType="text/html; charset=Cp1256" pageEncoding="Cp1256"%>
<%@ page import="javax.servlet.http.Cookie, waqf.viewer.*, waqf.viewer.Display.DocInfo"%>
<%@ page errorPage="error.jsp"%>

<%

String bid = request.getParameter("bid");
if( bid == null || bid.length() == 0) {
	bid = "g1b1";
}
String bidPar = "bid=" + bid;

String searchTerm = request.getParameter("id");
if(searchTerm == null) {
    //out.println("<h2>No record ID to display");
    //out.close();
    //return;
	searchTerm = "0";
}

//1) Get current status
Cookie[] cookies = request.getCookies();
String showDiac = Display.getDiacCookieStatus(cookies);

//2) Process command if any
String cmd = request.getParameter("cmd");
if(cmd != null && cmd.equals("showDiac")) {
	showDiac = "true";
} else if(cmd != null && cmd.equals("hideDiac")) {
	showDiac = "false";
}

//3) Write current status
Cookie cookie1 = new Cookie("showDiac", showDiac);
cookie1.setMaxAge(30*24*60*60); //1 month
response.addCookie(cookie1);

%>


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head><title>⁄—÷ «·„ﬂ »… «·≈”·«„Ì…</title>
<STYLE> <!--    @import url(style.css);		--> </STYLE>


</head>

<body dir=rtl>

<%@include file="header.jspf"%>

<br>

<table width=771 align=center border=2 cellpadding="5">
<tr><td align="center">

<%

	String nextPrev = Display.getDisplayNextAndPrev(bidPar, searchTerm);
	out.println(nextPrev);
	if(showDiac.equals("true")) {
		out.println(" | <a href=book.jsp?" + bidPar + "&id=" + searchTerm + "&cmd=hideDiac> ≈Œ›«¡ «· ‘ﬂÌ·  </a>");
	}
	else {
		out.println(" | <a href=book.jsp?" + bidPar + "&id=" + searchTerm + "&cmd=showDiac> ≈ŸÂ«— «· ‘ﬂÌ· </a>");
	}
		
%>

</td></tr>

<tr><td>



<%

    String indexPath = application.getRealPath("/WEB-INF/index");
    out.println("<!--" + indexPath + "-->");
    indexPath += "/" + bid;
	
	boolean showDiacVar = true;
		if(showDiac.equals("false")) {
			showDiacVar = false;
		}
		    
	DocInfo doc = Display.getDisplay(indexPath, searchTerm, showDiacVar); 
//displayPath
	if(doc != null) { 
		
		String dispPath = Display.getDisplayPath(bidPar, indexPath, doc.title, doc.parentID);
		out.println(dispPath);
	
	    //out.println("<br> <hr>");
	    out.println("<!-- ID=" + doc.id + "-->");   //this is just debug info

		if(doc.quranImage.length() > 0) {  //this is a special case in quran application
			//In Quran, it is 3 parts; text, image, and voice file
			out.println("<p align=center>");
			out.println("<img align=center src=\"quran/image/" + doc.quranImage + "\">");
			out.println("</p>");
			
			//get the full path of real player sound
			
			String telawaPath = application.getRealPath("quran/telawa/");
			//String qrnAudio = Display.fixAudioFileName(doc.quranAudio, telawaPath);
			String qrnAudio = doc.quranAudio;
			if( ! qrnAudio.equals(doc.quranAudio)) {
				out.println("<!-- Quran Audio FIXED:" + telawaPath + " -->");
			}
			
			out.println("<p align=center><a href=quran/telawa/" + qrnAudio + "><img border=0 src=images/play.gif></a></p>");
			
			//Print Text			
			String basicText = doc.basicText.replaceAll("\\n", "<br/>");
			out.println("<br> <hr>");
			out.println(basicText);		
		} else { 
			String basicText = doc.basicText;
			basicText = basicText.replaceAll("\\n", "<br/>");
			if(basicText.length() > 0) {
				out.println("<br><hr>");
				out.println(basicText);
	
			}
			
			String extendedText = doc.extendedText;
			extendedText = extendedText.replaceAll("\\n", "<br/>");
			if(extendedText.length() > 0) {
				out.println("<br><hr>");
				out.println(extendedText);		
				}			
			}
		
		out.println("<br>");
		//Show kids if any
	    
	    String kids = Display.getShowItemKids(indexPath, doc.id, "<a href=book.jsp?" + bidPar + "&id=%s>%s</a><br>"); 
	    if(kids.length() > 0) {
	    	out.println("<hr>");
	    	out.println(kids);
	    }
	}
	else {
		out.println("<br>");
		out.println("·«  ÊÃœ ‰ «∆Ã");
		out.println("<br><br>");
	}

%>




</td></tr></table>
<br><br>

<%@include file="footer.jspf"%>




 
</body>
</html>

