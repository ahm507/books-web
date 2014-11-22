<%@ page language="java" contentType="text/html; charset=Cp1256"
	pageEncoding="Cp1256"%>
<%@ page
	import="javax.servlet.http.Cookie, waqf.viewer.*,waqf.viewer.DisplayHtml.DocInfo"%>
<%@ page errorPage="error.jsp"%>

<%
	String bid = request.getParameter("bid");
	if (bid == null || bid.length() == 0) {
		bid = "g1b1";
	}
	String bidPar = "bid=" + bid;

	String searchTerm = request.getParameter("id");
	if (searchTerm == null) {
		searchTerm = "0";
	}

	//1) Get current status
	Cookie[] cookies = request.getCookies();
	String showDiac = DisplayHtml.getDiacCookieStatus(cookies);

	//2) Process command if any
	String cmd = request.getParameter("cmd");
	if (cmd != null && cmd.equals("showDiac")) {
		showDiac = "true";
	} else if (cmd != null && cmd.equals("hideDiac")) {
		showDiac = "false";
	}

	//3) Write current status
	Cookie cookie1 = new Cookie("showDiac", showDiac);
	cookie1.setMaxAge(30 * 24 * 60 * 60); //1 month
	response.addCookie(cookie1);
%>


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<title>⁄—÷ «·„ﬂ »… «·≈”·«„Ì…</title>
<STYLE>
<!--
@import url(style.css);
-->
</STYLE>


</head>

<body dir=rtl>

	<%@include file="header.jspf"%>

	<br>

	<table width=771 align=center border=2 cellpadding="5">
		<tr>
			<td align="center">
				<%
					String nextPrev = DisplayHtml.getDisplayNextAndPrev(bidPar, searchTerm);
							out.println(nextPrev);
							if (showDiac.equals("true")) {
								out.println(" | <a href=book.jsp?" + bidPar + "&id="
										+ searchTerm + "&cmd=hideDiac> ≈Œ›«¡ «· ‘ﬂÌ·  </a>");
							} else {
								out.println(" | <a href=book.jsp?" + bidPar + "&id="
										+ searchTerm + "&cmd=showDiac> ≈ŸÂ«— «· ‘ﬂÌ· </a>");
							}
				%>

			</td>
		</tr>

		<tr>
			<td>
				<%
					String indexPath = application.getRealPath("/WEB-INF/index");
						boolean showDiacVar = true;
						if (showDiac.equals("false")) {
							showDiacVar = false;
						}
						String telawaPath = application.getRealPath("quran/telawa/");
						DisplayHtml.displayHtml(indexPath, telawaPath, bid, searchTerm, out, showDiacVar);
				%>


			</td>
		</tr>
	</table>
	<br>
	<br>

	<%@include file="footer.jspf"%>


</body>
</html>

