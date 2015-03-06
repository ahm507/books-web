package waqf.epub;

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

import java.io.*;
import java.net.URL;
import java.util.List;

//FIXME: Add cover image for each book
//FIXME: Add copyright as creative common or whatever
//FIXME: Add contact person in case of error or feedback

public class EPubGen {
	String bookId;
	String indexPath;
	String bookPath;
	String bookTitle;
	Book book; //epub
	static final int CHAPTER_SIZE_MAX = 25*1024; //25K
	private static final String HADITH_TEMPLATE =  
			"<div dir=\"rtl\" class=\"hadith-title\">HADITH_TITLE</div>\r\n" + 
			"<div dir=\"rtl\" class=\"hadith-content\">HADITH_CONTENT</div>\r\n" + 
			"<div dir=\"rtl\" class=\"hadith-words-meaning\">HADITH_WORDS_MEANING</div>\r\n\r\n";

	public static void main(String[] args) throws ParseException, Exception {
		String bookId = "g2b1";
		System.out.println(bookId + ": Starting conversion...");
		EPubGen gen = new EPubGen(bookId, getBookIndexRoot(bookId), getBookSourceRoot(bookId));
		gen.generateEpubBook();
		System.out.println(bookId + ": generation is OK");
	}

	public EPubGen(String bookId, String indexFolder, String bookFolder) {
		super();
		this.bookId = bookId;
		this.indexPath = indexFolder;
		this.bookPath = bookFolder;
	}

	public void generateEpubBook() throws CorruptIndexException, ParseException, IOException, Exception {
		boolean showDiac = false;
		String searchId = "0"; //#L0
		Display.DocInfo book = Display.getDisplay(indexPath, searchId, showDiac);
		bookTitle = book.title;
		initBook(); //must be after initializing book title
		//#L1 
		List<HitInfo2> kotob = Search.findItemKids(indexPath, "0");
		int chapter = 1;
		for(HitInfo2 kitab : kotob) { //#L1 كتاب
			//#L3: باب أو حديث
			List<HitInfo2> hadiths = Search.findItemKids(indexPath, kitab.id);
			StringBuffer hadithAcc = new StringBuffer();
			int chapterPart = 1;
			for(HitInfo2 hadith : hadiths) {
				DocInfo hadith2 = Display.getDisplay(indexPath, hadith.id, showDiac);
				String hadithBab = prepareHadithBab(hadith2);
				hadithAcc.append(hadithBab).append("<br/><br/>");
				if(hadithAcc.length() > CHAPTER_SIZE_MAX) {
					genChapter(hadithAcc, chapter, kitab.title, chapterPart);
					hadithAcc = new StringBuffer();
					chapterPart ++; //The book may contain more than one chapter
				}
			}
			chapter++;
			chapterPart = 1;
		}
		finishBook();
	}

	private String prepareHadithBab(DocInfo hadith2) {
		String basicText = hadith2.basicText.replaceAll("\\n", "<br/>");
		String extendedtext = hadith2.extendedText.replaceAll("\\n", "<br/>");
		String hadithBab = HADITH_TEMPLATE.replaceAll("HADITH_TITLE", hadith2.title);
		hadithBab = hadithBab.replaceAll("HADITH_CONTENT", basicText);
		hadithBab = hadithBab.replaceAll("HADITH_WORDS_MEANING", extendedtext);
		return hadithBab;
	}

	private void genChapter(final StringBuffer hadithAcc, final int chapter, String kitabTitle, int chapterPart)
			throws IOException {
		String chapterHtml = getTemplateText();
		chapterHtml = chapterHtml.replaceAll("APPLICATION_CAPTION_TITLE", bookTitle);////book.title
		if(chapterPart > 1) {
			kitabTitle = kitabTitle + " - تابع";
		}
		chapterHtml = chapterHtml.replaceAll("CHAPTER_TITLE", kitabTitle);
		chapterHtml = chapterHtml.replaceAll("AHADITH_CONTENTS", hadithAcc.toString());
		String xhtmlLinker = String.format("chapter%d-%d.xhtml", chapter-1, chapterPart);
		addBookChapter(kitabTitle, chapterHtml, xhtmlLinker);
	}

	private void writeHtmlFile(final String chapterNo, final String chapterHtml) throws IOException {
		String sourceFolder = getBookSourceRoot(bookId);
		String chapterFileName = String.format("%s/chapter%s.xhtml", sourceFolder, chapterNo);
		FileUtil.writeToFile(chapterFileName, chapterHtml);
	}

	private static String getBookSourceRoot(String bookId) {
		URL url = ClassLoader.getSystemResource("epub.conf");
		String path = url.getPath();
		path = path.substring(0, path.lastIndexOf('/')); // truncate file name
		path = path.substring(0, path.lastIndexOf('/'));// truncate "/classes"
		path = path.substring(0, path.lastIndexOf('/'));// truncate "/build"
		path = path + "/WebContent/WEB-INF/data-generated/" + bookId;
		return path;
	}

	private static String getBookIndexRoot(String bookId) {
		URL url = ClassLoader.getSystemResource("epub.conf");
		String path = url.getPath();
		path = path.substring(0, path.lastIndexOf('/')); // truncate file name
		path = path.substring(0, path.lastIndexOf('/'));// truncate "/classes"
		path = path.substring(0, path.lastIndexOf('/'));// truncate "/build"
		path = path + "/WebContent/WEB-INF/index/" + bookId;
		return path;
	}	

	private static String getHtmlTemplateFileName() {
		URL url = ClassLoader.getSystemResource("chapter-template.xhtml");
		return url.getPath();
	}	
	
	private InputStream getResource(String path) throws FileNotFoundException {
		String fullPath = getBookSourceRoot(bookId) + path;
		return new FileInputStream(new File(fullPath));
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
	
	private String getTemplateText() throws IOException {
		String fileName = EPubGen.getHtmlTemplateFileName();
		return readFile(fileName);
	}

	static private String readFile(String path) 
			  throws IOException {
		java.io.RandomAccessFile raf = new java.io.RandomAccessFile(path, "r");
        byte[] buffer = new byte[(int)raf.length()];
        raf.readFully(buffer);
        raf.close();
        return new String(buffer, "UTF-8");
	}

	private void finishBook() throws IOException, FileNotFoundException {
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

	private void initBook() throws IOException {
		book = new Book();
		Metadata metadata = book.getMetadata();
		metadata.addTitle(bookTitle);
		metadata.addAuthor(new Author("الإمام البخاري"));
		// book.setCoverImage(getResource("/cover.png", "cover.png") );
		String title = "مقدمة";
		String href = String.format("intro.xhtml");
		String fileName = String.format("/intro.xhtml");
		book.addSection(title, getResource(fileName, href));
	}

	private void addBookChapter(String title, String contents, String xhtmlLinker) throws IOException {
//		String root = getBookSourceRoot(bookId);
//		String intoContents = readFile(root + fileName);
		book.addSection(title, getResourceByStringContents(contents, xhtmlLinker)); //"intro.xhtml"
	}
	
}
