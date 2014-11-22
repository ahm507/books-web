package waqf.epub;

import static org.junit.Assert.*;

import org.junit.Test;

import waqf.indexer.IndexerTest;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.adobe.dp.epub.io.OCFContainerWriter;
import com.adobe.dp.epub.ncx.TOCEntry;
import com.adobe.dp.epub.opf.NCXResource;
import com.adobe.dp.epub.opf.OPSResource;
import com.adobe.dp.epub.opf.Publication;
import com.adobe.dp.epub.ops.Element;
import com.adobe.dp.epub.ops.OPSDocument;
public class ePubGenTest {

	@Test
	public void test() throws FileNotFoundException, IOException {

		 // create new EPUB document
	      Publication epub = new Publication();

	      // set up title, author and language
	      epub.addDCMetadata("title", "This is my book title");
	      epub.addDCMetadata("creator", "Ahmed Hammad");
	      epub.addDCMetadata("language", "en");//ar

	      // prepare table of contents
	      NCXResource toc = epub.getTOC();
	      TOCEntry rootTOCEntry = toc.getRootTOCEntry();

	      // create new chapter resource
	      OPSResource main = epub.createOPSResource(IndexerTest.TEST_ROOT_PATH + "book1/chapter.html");
	      epub.addToSpine(main);

	      // get chapter document
	      OPSDocument mainDoc = main.getDocument();

	      // add chapter to the table of contents
	      TOCEntry mainTOCEntry = toc.createTOCEntry("Intro", mainDoc.getRootXRef());
	      rootTOCEntry.add(mainTOCEntry);

	      // chapter XHTML body element
	      Element body = mainDoc.getBody();

	      // add a header
	      Element h1 = mainDoc.createElement("h1");
	      h1.add("My First EPUB");
	      body.add(h1);

	      // add a paragraph
	      Element paragraph = mainDoc.createElement("p");
	      paragraph.add("Hello, world!");
	      body.add(paragraph);
	      
	      // save EPUB to an OCF container
	      OCFContainerWriter writer = new OCFContainerWriter(
	          new FileOutputStream(IndexerTest.TEST_ROOT_PATH + "book1/hello.epub"));
	      epub.serialize(writer);
	
	
	}

}
