package waqf.indexer;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


import java.io.*;

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
		
		String rootPath = "/Users/Macpro/Projects/Dr.AlyRashed/WebBooks/books2/test/";

		String dataPath = rootPath + "book1";
		String indexPath = rootPath + "book1";
		
		//Remove old index files
		new File(indexPath + "\\_8.cfs").delete(); 
		new File(indexPath + "\\segments.gen").delete();
		new File(indexPath + "\\segments_j").delete();

		Indexer indexer = new Indexer();
		// Just it will be successful if not throw an exception
		indexer.indexDoc(dataPath, indexPath, null);
				
	}


}
