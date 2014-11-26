package waqf.epub;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;

import nl.siegmann.epublib.domain.Author;
import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.Metadata;
import nl.siegmann.epublib.domain.Resource;
import nl.siegmann.epublib.epub.EpubWriter;

import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.queryParser.ParseException;

import waqf.books.Display;
import waqf.books.Display.DocInfo;
import waqf.books.Search;
import waqf.books.Search.HitInfo2;

//FIXME: Add cover image for each book
//FIXME: Add copyright as creative common or whatever
//FIXME: Add contact person in case of error or feedback

//TODO: research chm book format as it has good indexing, but it must be open source. I can also find 
//a simple way to cluster big chapters into smaller ones. In Ahmed book, I need to group them! 


public class ePubGen {

	public static void main(String[] args) throws ParseException, Exception {
		String bookId = "g2b1";
		System.out.println(bookId + ": Starting conversion...");
		ePubGen gen = new ePubGen(bookId, getBookIndexRoot(bookId), getBookSourceRoot(bookId));
		gen.generateEpubBook();
		System.out.println(bookId + ": generation is OK");
	}

	
	String bookId;
	String indexPath;
	String bookPath;
	private String bookTitle;
	private Book book;
	
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
		return path;
	}	
	
	private InputStream getResource(String path) throws FileNotFoundException {
		String fullPath = getBookSourceRoot(bookId) + path;
		FileInputStream file = new FileInputStream(new File(fullPath));
		return file;
	}

	private InputStream getResourceFromStringContents(String content) throws UnsupportedEncodingException {
		return new ByteArrayInputStream(content.getBytes("UTF-8"));
	}

	private Resource getResourceByStringContents(String contents, String href)
			throws IOException {
		return new Resource(getResourceFromStringContents(contents), href);
	}
	
	private Resource getResource(String path, String href)
			throws IOException {
		return new Resource(getResource(path), href);
	}

	public void generateEpubBook() throws CorruptIndexException, ParseException, IOException, Exception {
		boolean showDiac = false;
		String searchId = "0"; //#L0
		Display.DocInfo book = Display.getDisplay(indexPath, searchId, showDiac);
		bookTitle = book.title;
		initBook(); //must be after initializing book title
		
		//#L1
		ArrayList<HitInfo2> kotob = Search.findItemKids(indexPath, "0");
		String chapterHtmlTemplate = getTemplateText();
		String hadithBabTemplate = "<div dir=\"rtl\" class=\"hadith-title\">HADITH_TITLE</div>\r\n" + 
				"<div dir=\"rtl\" class=\"hadith-content\">HADITH_CONTENT</div>\r\n" + 
				"<div dir=\"rtl\" class=\"hadith-words-meaning\">HADITH_WORDS_MEANING</div>\r\n\r\n";
		int chapter = 1;
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
			chapter++;
			String xhtmlLinker = String.format("chapter%d.xhtml", chapter-1);
			addBookChapter(kitab.title, chapterHtml, xhtmlLinker);
		}
		
		finishBook();
	}
	
	private void writeHtmlFile(String id, String chapterHtml) throws IOException {
		String sourceFolder = getBookSourceRoot(bookId);
//		String chapterFileName = sourceFolder + "/chapter"+ id+ ".xhtml";
		String chapterFileName = String.format("%s/chapter%s.xhtml", sourceFolder, id);
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

//	public void generateBook() throws IOException, FileNotFoundException {
//		String title;
//		String href;
//		String fileName;
//		initBook();
//		
//		//For all chapters : 
//		for(int ch = 1; ch <= chaptersCount; ch++) {
//			title = chaptersTitles.get(ch-1);
//			href = String.format("Chapter%d.xhtml", ch);
//			fileName = String.format("/chapter%d.xhtml", ch);
//			book.addSection(title, getResource(fileName, href));
//		}
//
//		finishBook();
//	}

	public void finishBook() throws IOException, FileNotFoundException {
		//		 Add css file
				book.getResources().add(getResource("/style.css", "style.css"));
		
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
				epubWriter.write(book, new FileOutputStream(getBookSourceRoot(bookId) + "/" + bookTitle + ".epub"));
	}

	public void initBook() throws IOException {
		book = new Book();
		Metadata metadata = book.getMetadata();
		metadata.addTitle(bookTitle);
		metadata.addAuthor(new Author("الإمام البخاري"));
		// book.setCoverImage(getResource("/cover.png", "cover.png") );
		String title = "مقدمة";
		String href = String.format("intro.xhtml");
		String fileName = String.format("/intro.xhtml");
		book.addSection(title, getResource(fileName, href));
//		String root = getBookSourceRoot(bookId);
//		String intoContents = readFile(root + fileName);
//		book.addSection(title, getResourceByStringContents(intoContents, "intro.xhtml"));
	}

	void addBookChapter(String title, String contents, String xhtmlLinker) throws IOException {
//		String root = getBookSourceRoot(bookId);
//		String intoContents = readFile(root + fileName);
		book.addSection(title, getResourceByStringContents(contents, xhtmlLinker)); //"intro.xhtml"
	}
	
}
