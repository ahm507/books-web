/**
 * 
 */
package waqf.viewer;

import static org.junit.Assert.*;

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

	/**
	 * Test method for {@link waqf.viewer.ParsedText#getTitle()}.
	 */
	@Test
	public void testGetTitle() {
		
		
		
		
	}

	/**
	 * Test method for {@link waqf.viewer.ParsedText#getText()}.
	 */
	@Test
	public void testParseText() {
		
		//1) Tests separation by ##
		
		String record = "#L1 This is title ##This is body of the text";
		ParsedText ptext = null;
		try {
			ptext = ParsedText.parseText(record, "##");
		} catch (Exception exp){
			fail(exp.getMessage());
		}
		
		String title = ptext.getTitle();
		if(! title.equals("#L1 This is title ")) {
			fail("Unable to extract title correctly");
		}
		String text = ptext.getText();
		if(! text.equals("This is body of the text")) {
			fail("Unable to extract text correctly");
		}
		
		
		//2) Tests separation by NEW LINE and last char has NO new newline
		
		record = "#L1 This is title\r\nThis is body of the text";
		try {
			ptext = ParsedText.parseText(record, "##");
		} catch (Exception exp){
			fail(exp.getMessage());
		}
		
		title = ptext.getTitle();
		if(! title.equals("#L1 This is title")) {
			fail("Unable to extract title correctly");
		}
		text = ptext.getText();
		if(! text.equals("This is body of the text")) {
			fail("Unable to extract text correctly");
		}
		
		//3) Tests separation by NEW LINE and last char HAS new newline
		
		record = "#L1 This is title\r\nThis is body of the text\r\n";
		try {
			ptext = ParsedText.parseText(record, "##");
		} catch (Exception exp){
			fail(exp.getMessage());
		}
		
		title = ptext.getTitle();
		if(! title.equals("#L1 This is title")) {
			fail("Unable to extract title correctly");
		}
		text = ptext.getText();
		if(! text.equals("This is body of the text\r\n")) {
			fail("Unable to extract text correctly");
		}
		
		
	}

	
	

}
