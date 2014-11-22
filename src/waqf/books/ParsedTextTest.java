/**
 * 
 */
package waqf.books;

import static org.junit.Assert.*;

import java.io.UnsupportedEncodingException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


/**
 * @author Ahmed Hammad
 *
 */
public class ParsedTextTest {

	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}


	@Test
	public void testSparationByNewLine() throws UnsupportedEncodingException {
		
		String record;
		ParsedText ptext;
		String title;
		String text;
		
		// Tests separation by NEW LINE and last char has NO new newline
		
		record = "#L1 This is title\r\nThis is body of the text";
		ptext = ParsedText.parseText(record, "##");
		
		title = ptext.getTitle();
		assertTrue(title.equals("#L1 This is title"));

		text = ptext.getText();
		assertTrue(text.equals("This is body of the text"));
		
		
	}

	@Test
	public void testSparationByNewLine2() throws UnsupportedEncodingException {
		String record;
		ParsedText ptext;
		String title;
		String text;
		//3) Tests separation by NEW LINE and last char HAS new newline
		
		record = "#L1 This is title\r\nThis is body of the text\r\n";
		ptext = ParsedText.parseText(record, "##");
		
		title = ptext.getTitle();
		assertTrue(title.equals("#L1 This is title"));

		text = ptext.getText();
		assertTrue(text.equals("This is body of the text\r\n"));
	}

	@Test
	public void testSeparationByTwoHashes() throws UnsupportedEncodingException {
		//1) Tests separation by ##
		
		String record = "#L1 This is title ##This is body of the text";
		ParsedText ptext = null;
		ptext = ParsedText.parseText(record, "##");
		
		String title = ptext.getTitle();
		assertTrue(title.equals("#L1 This is title "));

		String text = ptext.getText();
		assertTrue(text.equals("This is body of the text"));
	}
	

}
