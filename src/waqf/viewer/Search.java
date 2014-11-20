/**
 * 
 */
package waqf.viewer;

import java.io.IOException;
import org.apache.lucene.analysis.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.highlight.*;
import org.apache.lucene.analysis.TokenStream;
import java.io.StringReader;
/**
 * @author ahamad
 * 
 */
public class Search {

	static public int getBookHitsCount(String indexPath, String queryText)
			throws IOException, ParseException {

		QueryParser qp = new QueryParser("content2", new WhitespaceAnalyzer());
		Query q = qp.parse(queryText);
		IndexSearcher ins = new IndexSearcher(indexPath);
		Hits hits = ins.search(q);
		int count = hits.length();
		ins.close();
		return count;
	}

	static public class  HitInfo {
		public HitInfo (int totalCount, String id, String title, String summery) {
			this.totalCount = totalCount;
			this.id = id;
			this.title = title;
			this.summery = summery;
		}
		public int totalCount ;
		public String id;
		public String title;
		public String summery;
	}
	
	static public HitInfo[] SeachBook(String bookPath, String query, int startHit, int showCount) 
					throws ParseException, CorruptIndexException, IOException {

	    QueryParser qp = new QueryParser("content2", new WhitespaceAnalyzer());
	    Query q = qp.parse(query);
		IndexSearcher ins = new IndexSearcher(bookPath);
	    Hits hits = ins.search(q);
		int hitsCount =  hits.length();

		int endHit = Math.min(startHit+showCount, hitsCount);
		showCount = endHit - startHit;

        Highlighter highlighter = new Highlighter(new SimpleHTMLFormatter (), new QueryScorer(q));
        WhitespaceAnalyzer analyzer = new WhitespaceAnalyzer();
        
		HitInfo[] hitsArray = new HitInfo[showCount];
		for(int i = 0 ; i < hitsArray.length; i++) {
			Document doc = hits.doc(i+startHit);
			String id = doc.get("id");
			String title = doc.get("title");
			String text = doc.get("content"); //content2 is not saved
			text = Display.removeDiacritics(text);
			
			TokenStream tokenStream = analyzer.tokenStream("content", new StringReader(text));
            // Get 3 best fragments and separate with a "..."			
			String result = highlighter.getBestFragments(tokenStream, text, 3, "...");
			result = "..." + result + "...";
			result = result.replace('$', ' ');
			
			hitsArray[i] = new HitInfo(hitsCount, id, title, result);

		}
		
		ins.close();	
		return hitsArray;
		
	}
	
}

