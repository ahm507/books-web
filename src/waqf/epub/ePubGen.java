package waqf.epub;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.queryParser.ParseException;

import com.sun.tools.javac.util.Paths;

import sun.security.action.GetLongAction;

import waqf.books.Display;
import waqf.books.Display.DocInfo;
import waqf.books.Search;
import waqf.books.Search.HitInfo;
import waqf.books.Search.HitInfo2;

import nl.siegmann.epublib.domain.Author;
import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.Metadata;
import nl.siegmann.epublib.domain.Resource;
import nl.siegmann.epublib.domain.TOCReference;
import nl.siegmann.epublib.epub.EpubWriter;

//TODO: generate a complete book in epub format
public class ePubGen {

	public static void main(String[] args) throws ParseException, Exception {
		String bookId = "g2b1";
		System.out.println(bookId + ": Starting conversion...");
		ePubGen gen = new ePubGen(bookId, getBookIndexRoot(bookId), getBookSourceRoot(bookId));
		gen.generateHTMLChapters();
		System.out.println(bookId + ": html generation is OK");
		gen.generateBook();
		System.out.println(bookId + ": epub generation is OK");
	}

	String bookId;
	String indexPath;
	String bookPath;
	private int chaptersCount;
	private ArrayList<String> chaptersTitles;
	private String bookTitle;
	
	public ePubGen(String bookId, String indexFolder, String bookFolder) {
		super();
		this.bookId = bookId;
		this.indexPath = indexFolder;
		this.bookPath = bookFolder;
	}

	private static String getBookSourceRoot(String bookId) {
		URL url = ClassLoader.getSystemResource("epub.conf");
		String path = url.getPath();
		path = path.substring(0, path.lastIndexOf("/")); // truncate file name
		path = path.substring(0, path.lastIndexOf("/"));// truncate "/classes"
		path = path.substring(0, path.lastIndexOf("/"));// truncate "/build"
		path = path + "/WebContent/WEB-INF/data-generated/" + bookId;
		return path;
	}

	private static String getBookIndexRoot(String bookId) {
		URL url = ClassLoader.getSystemResource("epub.conf");
		String path = url.getPath();
		path = path.substring(0, path.lastIndexOf("/")); // truncate file name
		path = path.substring(0, path.lastIndexOf("/"));// truncate "/classes"
		path = path.substring(0, path.lastIndexOf("/"));// truncate "/build"
		path = path + "/WebContent/WEB-INF/index/" + bookId;
		return path;
	}	

	private static String getHtmlTemplateFileName() {
		URL url = ClassLoader.getSystemResource("chapter-template.html");
		String path = url.getPath();
//		path = path.substring(0, path.lastIndexOf("/")); // truncate file name
//		path = path.substring(0, path.lastIndexOf("/"));// truncate "/classes"
//		path = path.substring(0, path.lastIndexOf("/"));// truncate "/build"
//		path = path + "/WebContent/WEB-INF/index/" + bookId;
		return path;
	}	
	
	private InputStream getResource(String path) throws FileNotFoundException {
		String fullPath = getBookSourceRoot(bookId) + path;
		//return ePubGen.class.getResourceAsStream(fullPath);
//		return ClassLoader.getSystemResourceAsStream(fullPath);
		FileInputStream file = new FileInputStream(new File(fullPath));
		return file;
	}

	private Resource getResource(String path, String href)
			throws IOException {
		return new Resource(getResource(path), href);
	}

	public void generateHTMLChapters() throws CorruptIndexException, ParseException, IOException, Exception {
		boolean showDiac = false;
		String searchId = "0"; //#L0
		Display.DocInfo book = Display.getDisplay(indexPath, searchId, showDiac);
		bookTitle = book.title;
		//#L1
		ArrayList<HitInfo2> kotob = Search.findItemKids(indexPath, "0");
		String chapterHtmlTemplate = getTemplateText();
		String hadithBabTemplate = "<div dir=\"rtl\" class=\"hadith-title\">HADITH_TITLE</div>\r\n" + 
				"<div dir=\"rtl\" class=\"hadith-content\">HADITH_CONTENT</div>\r\n" + 
				"<div dir=\"rtl\" class=\"hadith-words-meaning\">HADITH_WORDS_MEANING</div>\r\n\r\n";
		int chapter = 1;
		chaptersCount = 0;
		chaptersTitles = new ArrayList<String>(); 
		for(HitInfo2 kitab : kotob) { //#L1 كتاب
			//#L3: باب أو حديث
			ArrayList<HitInfo2> hadiths = Search.findItemKids(indexPath, kitab.id);
			StringBuffer hadithAcc = new StringBuffer();
			for(HitInfo2 hadith : hadiths) {
				DocInfo hadith2 = Display.getDisplay(indexPath, hadith.id, showDiac);
				String hadithBab = hadithBabTemplate.replaceAll("HADITH_TITLE", hadith2.title);
				String basicText = hadith2.basicText;
				basicText = basicText.replaceAll("\\n", "<br/>");
				hadithBab = hadithBab.replaceAll("HADITH_CONTENT", basicText);
				String extendedtext = hadith2.extendedText;
				extendedtext = extendedtext.replaceAll("\\n", "<br/>");
				hadithBab = hadithBab.replaceAll("HADITH_WORDS_MEANING", extendedtext);
				hadithAcc.append(hadithBab);
				hadithAcc.append("<br/><br/>");
			}
			String chapterHtml = chapterHtmlTemplate;
			chapterHtml = chapterHtml.replaceAll("APPLICATION_CAPTION_TITLE", book.title);
			chapterHtml = chapterHtml.replaceAll("CHAPTER_TITLE", kitab.title);
			chapterHtml = chapterHtml.replaceAll("AHADITH_CONTENTS", hadithAcc.toString());
			writeHtmlFile(String.format("%d", chapter), chapterHtml);
			chaptersCount = chapter;
			chaptersTitles.add(kitab.title);
			chapter++;
		}
		
	}

	private void writeHtmlFile(String id, String chapterHtml) throws IOException {
		String sourceFolder = getBookSourceRoot(bookId);
		String chapterFileName = sourceFolder + "/chapter"+ id+ ".html";
		new FileOutputStream(chapterFileName, true).close(); //create empty file if not exist
		PrintWriter out = new PrintWriter(chapterFileName); //create the file if not exist
		out.println(chapterHtml);
		out.close();
	}

	private String getTemplateText() throws IOException {
		String fileName = ePubGen.getHtmlTemplateFileName();
		return readFile(fileName);
	}

	static String readFile(String path) 
			  throws IOException {
		java.io.RandomAccessFile raf = new java.io.RandomAccessFile(path, "r");
        byte[] buffer = new byte[(int)raf.length()];
        raf.readFully(buffer);
        raf.close();
        return new String(buffer, "UTF-8");
	}

	public void generateBook() throws IOException, FileNotFoundException {
		// Create new Book
		Book book = new Book();
		Metadata metadata = book.getMetadata();

		// Set the title
		metadata.addTitle(bookTitle);

		// Add an Author
		metadata.addAuthor(new Author("الإمام البخاري"));

		// Set cover image
		// book.setCoverImage(
		// getResource("/book1/test_cover.png", "cover.png") );

		String title = "مقدمة";
		String href = String.format("intro.html");
		String fileName = String.format("/" + "intro.html");
		book.addSection(title, getResource(fileName, href));

		//For all chapters : 
		for(int ch = 1; ch <= chaptersCount; ch++) {
//			title = String.format("Chapter%d", ch);
			title = chaptersTitles.get(ch-1);
			href = String.format("Chapter%d.html", ch);
			fileName = String.format("/chapter%d.html", ch);
			book.addSection(title, getResource(fileName, href));
		}
//		// Add Chapter 1
//		book.addSection("Chapter1",
//				getResource("/chapter1.html", "chapter1.html"));

		// Add css file
//		book.getResources().add(getResource("/book1/book1.css", "book1.css"));

		// Add Chapter 2
//		TOCReference chapter2 = book.addSection("Second Chapter",
//				getResource("/chapter2.html", "chapter2.html"));

		// Add image used by Chapter 2
//		book.getResources().add(
//				getResource("/flowers_320x240.jpg", "flowers.jpg"));

		// Add Chapter2, Section 1
//		book.addSection(chapter2, "Chapter 2, section 1",
//				getResource("/chapter2_1.html", "chapter2_1.html"));

		// Add Chapter 3
//		book.addSection("Conclusion",
//				getResource("/chapter3.html", "chapter3.html"));

		// Create EpubWriter
		EpubWriter epubWriter = new EpubWriter();

		//FIXME: Add copyright as creative common or whatever
		//FIXME: Add contact person in case of error or feedback
		
		// Write the Book as Epub
		epubWriter.write(book, new FileOutputStream(getBookSourceRoot(bookId) + "/" + bookId + ".epub"));
	}

}
