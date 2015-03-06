package waqf.indexer;

import junit.framework.Assert;
import org.junit.Test;
import waqf.test.TestConfig;

import java.io.IOException;

public class IndexerTest {

	@Test
	public void testIndexDoc() throws IOException, InterruptedException, Exception {

		String dataPath = TestConfig.getTestRootPath() + "book1";
		String indexPath = TestConfig.getTestRootPath() + "book1";
		Indexer indexer = new Indexer();
		// Just it will be successful if not throw an exception
		int recordsCount = indexer.indexDoc(dataPath, indexPath, null);
		int expected = 606;
		Assert.assertEquals(expected, recordsCount);
	}
}
