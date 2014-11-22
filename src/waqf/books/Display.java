package waqf.books;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Vector;

import org.apache.lucene.analysis.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;

//TODO: Generate text files as the original files are corrupted. generate the same pattern as the old source files


public class Display {

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

	static public Display.DocInfo getDisplay(String indexPath, String searchTerm, boolean showDiac) 
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
	    String basicText = "", extendedText = "";
	    String quranImage = "", quranAudio = "";
	    
	    String text[] = content.split("\\x24"); //x24 is the $ in regular expressions, $ is a special character.
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
	    	basicText = Display.removeDiacritics(basicText); 
	    	extendedText = Display.removeDiacritics(extendedText);
	    }
	    
	    Display.DocInfo docInfo = new Display.DocInfo(id, parentID, title, basicText, extendedText, quranImage, quranAudio);
	    
	    return docInfo;
		
	}

	
	/**
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
	 * This function return a copy from the string that has no Arabic vowels
	 * @param text
	 * @return
	 */	
		//FIXME: Remove it, you have better version at Display.java 
		static String removeVowels(String text) {
	
			final int ARABIC_FATHATAN = 0x064B;
			final int ARABIC_DAMMATAN = 0x064C; 
			final int ARABIC_KASRATAN = 0x064D;
			final int ARABIC_FATHA = 0x064E;
			final int ARABIC_DAMMA = 0x064F;
			final int ARABIC_KASRA = 0x0650;
			final int ARABIC_SHADDA = 0x0651;
			final int ARABIC_SUKUN = 0x0652;
	
			StringBuffer text2 = new StringBuffer();
			for(int i=0 ; i < text.length(); i++) {
				switch(text.charAt(i)) {
				case ARABIC_FATHATAN:
				case ARABIC_DAMMATAN: 
				case ARABIC_KASRATAN:
				case ARABIC_FATHA:
				case ARABIC_DAMMA:
				case ARABIC_KASRA:
				case ARABIC_SHADDA:
				case ARABIC_SUKUN:
					break;
				default: //other chars
					text2.append(text.charAt(i));
					break;
				
				}
			}
			
			return text2.toString(); 
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

	static Vector<String> getItemPathInfo(String indexPath, String id)
									throws ParseException, IOException {
		
		Vector<String> pathInfo = new Vector<String>(); //initially empty
		IndexSearcher ins = null;
	
		QueryParser qp = new QueryParser("id", new WhitespaceAnalyzer());
		Query q = qp.parse(id);
		
	    ins = new IndexSearcher(indexPath);
	    Hits hits = ins.search(q);
	
	    if(hits.length() == 0) {
			ins.close();
			return pathInfo;
	    }
	
	    //Display the record
	    Document doc = hits.doc(0);
	    pathInfo.add(0, doc.get("id"));
	    pathInfo.add(1, doc.get("parentID"));
	    pathInfo.add(2, doc.get("title"));
	    ins.close();
	
		return pathInfo;
	}

	public static void getTreePathData(String indexPath, String title,
			String parentID, Vector<String> ids, Vector<String> titles)
			throws ParseException, IOException {
		ids.add("0");// I need not to store the read ID, I never uses it
		titles.add(title);
		
		Vector<String> pathInfo = getItemPathInfo(indexPath, parentID);
		while (pathInfo.size() > 0) {
			ids.add(pathInfo.get(0)); // id
			title = pathInfo.get(2);
			title = Display.cleanupTitle(title);
			titles.add(title); // title
			parentID = (String) pathInfo.get(1);// parentID
			pathInfo = getItemPathInfo(indexPath, parentID);
		}
	}


}
