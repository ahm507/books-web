/**
 * 
 */
package waqf.books;

import static org.junit.Assert.*;
import java.io.UnsupportedEncodingException;
import org.junit.Before;
import org.junit.Test;

import waqf.test.TestConfig;

/**
 * @author Ahmed Hammad
 *
 */

public class BooksTest {

	Books books;
	
	@Before
	public void setUp() throws Exception {
		String rootPath = TestConfig.getTestRootPath();
		String booksFile = rootPath + "books.properties"; 
		books = new Books();
		books.loadSettings(booksFile);
	}

	@Test
	public void testGetGroupCount() {
		int groupCount = books.getGroupCount();
		assertEquals(3, groupCount);
	}
	
	/**
	 * Test method for {@link waqf.books.Books#getGroupTitle(int)}.
	 * @throws UnsupportedEncodingException 
	 */
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

	/**
	 * Test method for {@link waqf.books.Books#isBookEnabled(int, int)}.
	 */
	@Test
	public void testIsBookEnabled() {
		assertTrue(books.isBookEnabled(1, 1));
		assertFalse(books.isBookEnabled(2, 1));
	}

	/**
	 * Test method for {@link waqf.books.Books#getBookTitle(int, int)}.
	 * @throws UnsupportedEncodingException 
	 */
	@Test
	public void testGetBookTitle() throws UnsupportedEncodingException {
		
		assertEquals("القرآن الكريم", books.getBookTitle(1, 1));
		
	}

}
