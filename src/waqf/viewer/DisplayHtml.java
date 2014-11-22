package waqf.viewer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;
import javax.servlet.http.Cookie;
import javax.servlet.jsp.JspWriter;

import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.queryParser.ParseException;

import waqf.books.Display;
import waqf.books.Search;
import waqf.books.Search.HitInfo2;
 
/**
 * 
 * Helper functions for the display related jsp files 
 * @author Ahmed Hammad
 *
 */

public class DisplayHtml {

	static public String getDiacCookieStatus(Cookie[] cookies) {
		String showDiac = "true"; //this is the default status
	    //Cookie[] cookies = request.getCookies();
	    for(int loopIndex = 0; loopIndex < cookies.length; loopIndex++) { 
	        Cookie cookie1 = cookies[loopIndex];
	        if (cookie1.getName().equals("showDiac")) {
	          showDiac = cookie1.getValue();
	        }
	      }    
	    if( ! showDiac.equals("true") && ! showDiac.equals("false"))
	    	showDiac = "true";
	    return showDiac;
	}
	
	static public String getDisplayPathAsHtml(String bidPar, String indexPath, String title,
			String parentID) throws ParseException, IOException {

		Vector<String> ids = new Vector<String>();
		Vector<String> titles = new Vector<String>();

		Display.getTreePathData(indexPath, title, parentID, ids, titles);

		// And now I am going to print the items in reverse
		int n = 0;
		StringBuffer dispPath = new StringBuffer();
		for (int i = ids.size() - 1; i >= 0; i--) {
			String spaces = getHtmlSpaces(n * 4);
			dispPath.append(spaces);
			n++;
			title = (String) titles.get(i);
//			title = Display.cleanupTitle(title);
			String title2 = title;// new String(title.getBytes("Cp1252"), "Cp1256");

			if (i == 0)
				dispPath.append(title2);
			else
				dispPath.append("<a href=book.jsp?" + bidPar + "&id="
						+ ids.get(i) + ">" + title2 + "</a>");

			if (i != 0)
				dispPath.append("<br>");

		}

		return dispPath.toString();
	}

	static private String getHtmlSpaces(int n) {
		StringBuffer spaces = new StringBuffer();
		while (n >= 0) {
			spaces.append("&nbsp;");
			n--;
		}
		return spaces.toString();
	}

	static public String getDisplayNextAndPrev(String bidPar, String searchTerm) {

		StringBuffer result = new StringBuffer();
		int next = Integer.parseInt(searchTerm);
		next++;
		int previous = Integer.parseInt(searchTerm);
		previous--;
		if (previous > 0) {
			result.append("  <a href=book.jsp?" + bidPar + "&id="
					+ Integer.toString(previous) + "> <<<< </a> ");
		} else {
			result.append("<<<<");
		}
		result.append(" | <a href=book.jsp?" + bidPar + "&id="
				+ Integer.toString(next) + "> >>>> </a> ");
		return result.toString();
	}

	static public void displayHtml(String indexPath, String telawaPath, String bid, String searchTerm, JspWriter out, boolean showDiacVar) throws CorruptIndexException, ParseException, IOException, Exception {

		//out.println("<!--" + indexPath + "-->");
		indexPath += "/" + bid;
		String bidPar = "bid=" + bid;
		Display.DocInfo doc = Display.getDisplay(indexPath, searchTerm, showDiacVar);
		if (doc != null) {
			diplayPathHtml(indexPath, out, bidPar, doc);
			//Display Body of of text
			if (doc.quranImage.length() > 0) { //this is a special case in Quran application
				displayQuranHtml(telawaPath, out, doc);
			} else {
				displayHadithHtml(out, doc);
			}
			displayKidsHtml(indexPath, out, bidPar, doc);
		} else {
			out.println("<br>");
			out.println("لا توجد نتائج");
			out.println("<br><br>");
		}
	}

	static public void displayHadithHtml(JspWriter out, Display.DocInfo doc)
			throws IOException {
		String basicText = doc.basicText;
		basicText = basicText.replaceAll("\\n", "<br/>");
		if (basicText.length() > 0) {
			out.println("<br><hr>");
			out.println(basicText);
		}
		String extendedText = doc.extendedText;
		extendedText = extendedText.replaceAll("\\n", "<br/>");
		if (extendedText.length() > 0) {
			out.println("<br><hr>");
			out.println(extendedText);
		}
	}

	static public void displayQuranHtml(String telawaPath, JspWriter out, Display.DocInfo doc)
			throws IOException {
		//In Quran, it is 3 parts; text, image, and voice file
		out.println("<p align=center>");
		out.println("<img align=center src=\"quran/image/"
				+ doc.quranImage + "\">");
		out.println("</p>");
		String qrnAudio = doc.quranAudio;
		if (!qrnAudio.equals(doc.quranAudio)) {
			out.println("<!-- Quran Audio FIXED:" + telawaPath + " -->");
		}

		out.println("<p align=center><a href=quran/telawa/"
				+ qrnAudio
				+ "><img border=0 src=images/play.gif></a></p>");

		//Print Text			
		String basicText = doc.basicText.replaceAll("\\n", "<br/>");
		out.println("<br> <hr>");
		out.println(basicText);
	}

	static public void displayKidsHtml(String indexPath, JspWriter out, String bidPar,
			Display.DocInfo doc) throws IOException, ParseException {
		//Show kids if any
		out.println("<br>");
		String kids = DisplayHtml.getItemKidsAsHtml(indexPath, doc.id, "<a href=book.jsp?" + bidPar + "&id=%s>%s</a><br>");
		if (kids.length() > 0) {
			out.println("<hr>");
			out.println(kids);
		}
	}
	
	/**
	 *
	 * Get item kids parsed as html string using the passed formatPattern.  
	 * If you want to change the format pattern, be very carful to not break the code.
	 * @param dir
	 * @param id
	 * @param formatPattern This is the default pattern "<a href=index.jsp?id=%d>%s</a><br>"
	 * @return
	 * @throws ParseException
	 * @throws IOException
	 */
	static public String getItemKidsAsHtml(String indexPath, String id, String formatPattern) 
					throws ParseException, IOException {
		if(formatPattern.length() == 0) {
			formatPattern = "<a href=index.jsp?id=%s>%s</a><br>";
		}
		ArrayList<HitInfo2>hits = Search.findItemKids(indexPath, id);
		StringBuffer result = new StringBuffer();
		//Display the records
		for(int i=0;i < hits.size(); i++) {
			Search.HitInfo2 doc = hits.get(i);
		    String id2 = doc.id;
		    String title2 = doc.title;
		    //cleanupTitle should be in Display.java unless I need the dirty one sometimes ?!
//		    title2 = Display.cleanupTitle(title2); //remove #L0, ...
	  	    String title3 = title2;
	  	    result.append(String.format(formatPattern, id2, title3));
		}
	    return result.toString();
	}
	
	static public void diplayPathHtml(String indexPath, JspWriter out, String bidPar,
			Display.DocInfo doc) throws ParseException, IOException {
		// DISPLAY PATH
		String dispPath = DisplayHtml.getDisplayPathAsHtml(bidPar, indexPath,
				doc.title, doc.parentID);
		out.println(dispPath);

		out.println("<!-- ID=" + doc.id + "-->"); //this is just debug info
	}
	
}
