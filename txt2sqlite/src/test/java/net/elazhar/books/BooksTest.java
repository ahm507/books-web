/**
 * 
 */
package net.elazhar.books;

import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

import static org.junit.Assert.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author Ahmed Hammad
 *
 */

public class BooksTest {

    private static final Logger logger = LoggerFactory.getLogger(BooksTest.class);

	Books books;
	
	@Before
	public void setUp() throws Exception {
		
        String current = new java.io.File( "." ).getCanonicalPath();
        logger.info("Current directory is: {}", current);
        
        books = new Books();
		Properties props = new Properties();
		InputStream is = BooksTest.class.getClassLoader().getResourceAsStream("books.properties");
		props.load(is);
		books.setPoperties(props); //for testing purposes

	}

	@Test
	public void testGetGroupCount() {
		int groupCount = books.getGroupCount();
		assertEquals(3, groupCount);
	}
	
	@Test
	public void testGetGroupTitle() throws UnsupportedEncodingException {
		String title = books.getGroupTitle(2);
		String expected = "موسوعة الحديث الشريف";
		assertEquals(expected, title);
	}

	@Test
	public void testGetGroupBooksCount() {
		assertEquals(2, books.getGroupBooksCount(1));
		assertEquals(12, books.getGroupBooksCount(2));		
		assertEquals(3, books.getGroupBooksCount(3));		
	}

	@Test
	public void testIsBookEnabled() {
		assertTrue(books.isBookEnabled(1, 1));
		assertFalse(books.isBookEnabled(2, 1));
	}

	@Test
	public void testGetBookTitle() throws UnsupportedEncodingException {
		
		assertEquals("القرآن الكريم", books.getBookTitle(1, 1));
		
	}

}
