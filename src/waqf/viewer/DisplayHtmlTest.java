package waqf.viewer;

import static org.junit.Assert.*;

import java.io.IOException;
import org.apache.lucene.queryParser.ParseException;
import org.junit.Test;
import waqf.test.TestConfig;

public class DisplayHtmlTest {

	@Test
	public void testGetItemKidsAsHtml() throws IOException, ParseException {
		String indexPath = TestConfig.getBookIndexPath();
		String id = "0";
		String formatPattern = "%s-%s ";
		String html = DisplayHtml.getItemKidsAsHtml(indexPath, id, formatPattern);
		String expected = "1-1 - بدء الوحى - صحيح البخارى. 15-2 - الإيمان - صحيح البخارى. 110-3 - العلم - صحيح البخارى. 240-4 - الوضوء - صحيح البخارى. 304-1 - بدء الوحى - صحيح البخارى. 318-2 - الإيمان - صحيح البخارى. 413-3 - العلم - صحيح البخارى. 543-4 - الوضوء - صحيح البخارى. ";
		assertEquals(expected, html);
	
	}
	
	
}
