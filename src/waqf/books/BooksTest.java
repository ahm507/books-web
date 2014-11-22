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
/////
public class BooksTest {

	/**
	 * @throws java.lang.Exception
	 */
	
	Books books = null;
//	String rootPath = "D:\\docs\\Dr.AlyRashed\\Search\\junit test files\\";
	String rootPath = "/Users/Macpro/Projects/Eclipse-AlyRashed/Books2/test/";
	
	String booksFile = rootPath + "books.properties"; 
	@Before
	public void setUp() throws Exception {
		books = new Books();
		books.loadSettings(booksFile);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		
	}


	/**
	 * Test method for {@link waqf.books.Books#getGroupCount()}.
	 */
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

	/**
	 * Test method for {@link waqf.books.Books#getGroupBooksCount(int)}.
	 */
	@Test
	public void testGetGroupBooksCount() {
		
		assertEquals(2, books.getGroupBooksCount(1));// != 1) {
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
