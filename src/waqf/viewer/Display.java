package waqf.viewer;

//import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Vector;

import javax.servlet.http.Cookie;

import org.apache.lucene.analysis.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
 
/**
 * 
 * Helper functions for the display related jsp files 
 * @author Ahmed Hammad
 *
 */



public class Display {
 
/**
 * 
 * 
 * 
 * @param str The input string without Arabic diacritics
 * @return
 * @throws UnsupportedEncodingException
 */
	
	
	static public String removeDiacritics(String str) throws UnsupportedEncodingException {

//		$vowels = chr(0xF0).chr(0xF1).chr(0xF2).chr(0xF3).chr(0xF5).chr(0xF6).chr(0xF8).chr(0xFA);

	    String str1252 = new String(str.getBytes("Cp1256"), "Cp1252");
	    str1252 = str1252.replaceAll("[\\xF0-\\xFA]", ""); //1252 encoding
	    str = new String(str1252.getBytes("Cp1252"), "Cp1256");
	   
		//str = str.replaceAll("[\\x64B-\\x652]", ""); //1252 encoding


	    return str;

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
	static public String getShowItemKids(String indexPath, String id, String formatPattern)
					throws ParseException, IOException {

		if(formatPattern.length() == 0) {
			formatPattern = "<a href=index.jsp?id=%s>%s</a><br>";
		}
		StringBuffer result = new StringBuffer();
	    IndexSearcher ins = null;

		//Query q = QueryParser.parse(id, "parentID", new WhitespaceAnalyzer());
	    QueryParser qp = new QueryParser("parentID", new WhitespaceAnalyzer());
	    Query q = qp.parse(id);
		ins = new IndexSearcher(indexPath);
		Hits hits = ins.search((Query)q);

		if(hits.length() == 0) {
		    ins.close();
		    return "";
		}

		//Display the recordes
		for(int i=0;i < hits.length(); i++) {
		    Document doc = hits.doc(i);
		    String id2 = doc.get("id");
		    String title2 = doc.get("title");
		    title2 = Display.cleanupTitle(title2); //remove #L0, ...
	  	    String title3 = title2;

	  	    result.append(String.format(formatPattern, id2, title3));

		}
		ins.close();


	    return result.toString();
	}

	static private Vector<String> getItemPathInfo(String indexPath, String id) 
									throws ParseException, IOException {
		
		Vector<String> v = new Vector<String>(); //initially empty
		IndexSearcher ins = null;

		QueryParser qp = new QueryParser("id", new WhitespaceAnalyzer());
		Query q = qp.parse(id);
		
	    ins = new IndexSearcher(indexPath);
	    Hits hits = ins.search(q);

	    if(hits.length() == 0) {
			ins.close();
			return v;
	    }

	    //Display the record
	    Document doc = hits.doc(0);
	    v.add(0, doc.get("id"));
	    v.add(1, doc.get("parentID"));
	    v.add(2, doc.get("title"));
	    ins.close();

		return v;
	}
/**
 * This is hardcoded shortcut to clean up markup tags typically #L0 to #L10
 * It uses regular expression
 * @param title The text to be cleaned
 * @return
 */
	static public String cleanupTitle(String title) {
	
		title = title.replaceAll("#L\\d+\\s", "");//remove #L0, #L1, and so on.
	
		return title;
		
		
	}

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
	
	static public String getDisplayPath(String bidPar, String indexPath, String title,
			String parentID) throws ParseException, IOException {
		// First get the path titles and IDs, Then print them in reverse order,
		// so
		// I used two vectors
		StringBuffer dispPath = new StringBuffer();

		// Vector<String> ids = new Vector<String>();
		Vector<String> ids = new Vector<String>();
		Vector<String> titles = new Vector<String>();
		ids.add("0");// I need not to store the read ID, I never uses it
		titles.add(title);
		// out.println(title + "<br>");
		Vector<String> vec = Display.getItemPathInfo(indexPath, parentID);
		while (vec.size() > 0) {
			ids.add(vec.get(0)); // id
			titles.add(vec.get(2)); // title
			parentID = (String) vec.get(1);// parentID
			vec = Display.getItemPathInfo(indexPath, parentID);
		}

		// And now I am going to print the items in reverse
		int n = 0;
		for (int i = ids.size() - 1; i >= 0; i--) {
			String spaces = getPrintSpaces(n * 4);
			dispPath.append(spaces);
			n++;
			title = (String) titles.get(i);
			title = Display.cleanupTitle(title);
			String title2 = title;// new String(title.getBytes("Cp1252"),
									// "Cp1256");

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

	static private String getPrintSpaces(int n) {
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
		// result.append("<a href=book.jsp?" + bidPar + "&id=" +
		// Integer.toString(next) + "> >> ���� >> </a> ");
		int previous = Integer.parseInt(searchTerm);
		previous--;
		if (previous > 0) {
			result.append("  <a href=book.jsp?" + bidPar + "&id="
					+ Integer.toString(previous) + "> << ���� << </a> ");
		} else {
			result.append("<< ���� <<");
		}

		result.append(" | <a href=book.jsp?" + bidPar + "&id="
				+ Integer.toString(next) + "> >> ���� >> </a> ");

		return result.toString();
	}

	public static class DocInfo {
		public DocInfo(String id, String parentID, String title, String basicText, 
				String extendedText, String quranImage, String quranAudio) {
			this.id = id;
			this.parentID = parentID;
			this.title = title;
			this.basicText = basicText;
			this.extendedText = extendedText;
			this.quranImage = quranImage;
			this.quranAudio = quranAudio;
			
		}
		public String id;
		public String parentID;
		public String title;
		public String basicText; 
		public String extendedText;
		public String quranImage;
		public String quranAudio;			
		
	}
/* 		
	static public String fixAudioFileName(String name, String telawaPath) {
		
		String fullPath = telawaPath + name;
		boolean fileExist = true;
		try {
			FileInputStream fis = new FileInputStream( fullPath);
			fis.close();
		} catch (Exception exp) {
			fileExist = false;
		}

		if(fileExist == false) {
			//replace last occurance of s letter to be a capital letter
			StringBuffer strb = new StringBuffer (name);
			int index = strb.lastIndexOf("s");
			strb.replace(index, index+1, "S");
			name = strb.toString();
			//name = name.replace('s', 'S'); //only one s exist
		}
		
		return name;
	}
*/	
	static public DocInfo getDisplay(String indexPath, String searchTerm, boolean showDiac) 
				throws ParseException, CorruptIndexException, IOException, Exception {
		
	    QueryParser qp = new QueryParser("id", new WhitespaceAnalyzer());
	    Query q = qp.parse(searchTerm);
	    
	    IndexSearcher ins = new IndexSearcher(indexPath); 
	    Hits hits = ins.search(q);
	    if(hits.length() == 0) {
	    	ins.close();
	    	//throw new Exception ("No record has been founded to display in book.jsp with id=["+searchTerm+"]");
	    	return null;
	    }

	    //Display the record
	    Document doc = hits.doc(0);
	    String id = doc.get("id");
	    String parentID = doc.get("parentID");
	    String title = doc.get("title");
	    String content = doc.get("content");

//	    QueryScorer qs = new QueryScorer(q); 
//	    Highlighter ht = new Highlighter(new SimpleHTMLFormatter(), new SimpleHTMLEncoder(), qs);
//	    content = ht.getBestFragment(new WhitespaceAnalyzer(), "content", content);
	    
	    String basicText = "", extendedText = "";
	    String quranImage = "", quranAudio = "";
	    
	    String text[] = content.split("\\x24"); //x24 is the $ in regular expressions, $ is a special charecter.
	    if(text.length == 1) { 
	    	basicText = text[0].trim();
	    } else if(text.length == 2) { //Strange words meaning
			basicText = text[0].trim();
			extendedText = text[1].trim();
	    } else if (text.length == 3) { // Quran application with image and audio info
	    	basicText = text[0].trim();
	    	quranImage = text[1].trim() + ".gif";
	    	//Process audio
	    	String [] segs = text[2].split("a");
			String folder = segs[0].trim();

			quranAudio = folder + "/" + text[2].trim() + ".rm";
			quranAudio = quranAudio.toLowerCase();
	    }
	    
	    ins.close();
	    
	    if(showDiac == false) {
	    	basicText = removeDiacritics(basicText); 
	    	extendedText = removeDiacritics(extendedText);
	    }
	    
	    DocInfo docInfo = new DocInfo(id, parentID, title, basicText, extendedText, quranImage, quranAudio);
	    
	    return docInfo;
		
	}
	
	String Highlight(String text, String searchTerm) {
		
		//if Search have double quetes, remove them and treat all as one word
		//else break words by space and treat each one as a separate word
		
		
		//Remove diacritics
		//Get the word number of search terms
		//Apply highlighting on the original voweled text using word number 
		
	
		return "";
	}
}
