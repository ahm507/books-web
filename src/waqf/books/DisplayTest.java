package waqf.books;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

import org.apache.lucene.queryParser.ParseException;
import org.junit.Test;

public class DisplayTest {

	//FIXME: fix this test or remove it
	@Test
	public void testGetShowItemKids() throws ParseException, IOException {
		
		String indexPath = "";
		String id = "";
		String formatPattern = "";
//		String kids = Display.getShowItemKids(indexPath, id, formatPattern);
		
		Properties conf = getUnitTestConfig();
		String str = conf.elements().toString();
	
	}

	Properties getUnitTestConfig() throws IOException {
		Properties props = new Properties();
		InputStream is = ClassLoader.getSystemResourceAsStream("unit-test.properties");
		props.load(is);
		return props;
	}
	
	@Test
	public void testRemoveDiacritics() throws UnsupportedEncodingException {
		String text = "« إِنَّمَا الأَعْمَالُ بِالنِّيَّاتِ ";
		String out = Display.removeDiacritics(text);
		assertEquals("« إنما الأعمال بالنيات ", out);
	}
	
	@Test
	public void testRemoveVowels() throws UnsupportedEncodingException {
		String text = "« إِنَّمَا الأَعْمَالُ بِالنِّيَّاتِ ";
		String out = Display.removeVowels(text);
		assertEquals("« إنما الأعمال بالنيات ", out);
	}
	
}
