package waqf.books;

import static org.junit.Assert.*;

import java.io.IOException;

import org.apache.lucene.analysis.WhitespaceAnalyzer;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.junit.Test;
import waqf.books.*;
import waqf.test.TestConfig;

public class SearchTest {

	
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

	@Test
	public void testGetBookHitsCount() throws IOException, ParseException {
		
		String indexPath = TestConfig.getBookIndexPath();
		String queryText = "2";
		int count = Search.getBookHitsCount(indexPath, queryText);
		assertEquals(100, count);
	
	}
	
	
	@Test
	public void testSearchBook() throws CorruptIndexException, ParseException, IOException {

		String bookPath = TestConfig.getBookIndexPath();
		String query = "4";
		int startHit = 0, showCount = 11;
		Search.HitInfo[] hits = Search.SeachBook(bookPath, query, startHit, showCount);
		assertEquals(10, hits.length);
		
	}
	

}
