package waqf.indexer;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


import java.io.*;

import junit.framework.Assert;

//import com.sun.java.util.jar.pack.Package.File;



public class IndexerTest {

	@Before
	public void setUp() throws Exception {
		

	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testIndexDoc() throws IOException, InterruptedException, Exception {
		
		String rootPath = "/Users/Macpro/Projects/Eclipse-AlyRashed/Books2/test/";

		String dataPath = rootPath + "book1";
		String indexPath = rootPath + "book1";
		
		Indexer indexer = new Indexer();
		// Just it will be successful if not throw an exception
		int indexedRecordsCount = indexer.indexDoc(dataPath, indexPath, null);
		int expected = 499;
		Assert.assertEquals(expected, indexedRecordsCount);

		
		
		
				
	}


}
