package waqf.viewer;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import org.apache.lucene.queryParser.ParseException;
import org.junit.Test;

public class DisplayHtmlTest {

	@Test
	public void testGetItemKidsAsHtml() throws IOException, ParseException {
		String indexPath = getBookIndexPath();
		String id = "0";
		String formatPattern = "%s-%s ";
		String html = DisplayHtml.getItemKidsAsHtml(indexPath, id, formatPattern);
		String expected = "1-1 - بدء الوحى - صحيح البخارى. 15-2 - الإيمان - صحيح البخارى. 110-3 - العلم - صحيح البخارى. 240-4 - الوضوء - صحيح البخارى. 304-1 - بدء الوحى - صحيح البخارى. 318-2 - الإيمان - صحيح البخارى. 413-3 - العلم - صحيح البخارى. 543-4 - الوضوء - صحيح البخارى. ";
		assertEquals(expected, html);
	
	}

	Properties getUnitTestConfig() throws IOException {
		Properties props = new Properties();
		InputStream is = ClassLoader.getSystemResourceAsStream("unit-test.properties");
		props.load(is);
		return props;
	}

	String getBookIndexPath() {
		URL url = ClassLoader.getSystemResource("unit-test.properties");
		String path = url.getPath();
		path = path.substring(0, path.lastIndexOf("/")); // truncate file name
		path = path.substring(0, path.lastIndexOf("/"));//truncate "/classes"
		path = path.substring(0, path.lastIndexOf("/"));//truncate "/build"		
		path = path + "/test/book1/index";
		return  path;
	}
	
	
}
