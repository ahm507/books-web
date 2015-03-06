package waqf.sqlite;

import org.apache.lucene.queryParser.ParseException;
import waqf.books.Display;
import waqf.books.Display.DocInfo;
import waqf.books.Search;

import java.sql.*;
import java.util.List;


public class GenSqlite {
	String bookCode;
	String indexPath;
//	String bookPath;
	String bookTitle;
	//Book book; epub
//	static final int CHAPTER_SIZE_MAX = 25*1024; //25K
	private static final String PAGE_TEMPLATE =
			"HADITH_TITLE\r\n" +
			"HADITH_CONTENT\r\n" +
			"HADITH_WORDS_MEANING\r\n";

	public static void main(String[] args) throws ParseException, Exception {

		String bookCode;// = "g2b1";
		if(args.length < 2) {
			System.err.println( "Parameters Error!");
			System.err.println( "Usage$ indexer.sh path/to/lucene/index  book_code");
			System.err.println("");
			System.exit(-1);
		}
		String indexPath = args[0];
		bookCode = args[1];
		System.out.println(bookCode + ": Starting conversion...");
		GenSqlite gen = new GenSqlite(bookCode, indexPath, bookCode);
		gen.generateSqlite();
		System.out.println(bookCode + ": generation is OK");
	}

	public GenSqlite(String bookId, String indexFolder, String bookCode) {
//		super();
		this.bookCode = bookCode;
		this.indexPath = indexFolder;
//		this.bookPath = bookCode;
	}

	public void generateSqlite() throws Exception {

		Connection connection = null;
		try {
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection("jdbc:sqlite:sqlite.db");
			connection.setAutoCommit(false);

		} catch ( Exception e ) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			System.exit(-1);
		}

		Statement stmt = null;
		try {
			//Handle Books Table of titles only
			stmt = connection.createStatement();

//			createTablesSimple(stmt);


			// just drop the table
//			String drop = "DROP TABLE pages";
//			stmt.executeUpdate(drop);

//			"page_id, parent_id, book_code, book_title, title, main_text, footnote, image, audio, full_text );";

			//FTS4 with no diacritics
			String createFts4 = "CREATE VIRTUAL TABLE IF NOT EXISTS pages USING fts4(" +
					"page_id, parent_id, book_code, book_title, title, main_text, footnote, image, audio, full_text );";
			stmt.executeUpdate(createFts4);

			//Ensure the book has no records
//			ResultSet rs = stmt.executeQuery( "select * from books where book_code = '" + this.bookCode + "'");
			String sqlCheckExist = "SELECT * FROM pages WHERE book_code  MATCH '" + bookCode + "'";
			ResultSet rs = stmt.executeQuery( sqlCheckExist);
			if (rs.next()) {
				rs.close();
				System.out.print("Existing records exit, overwrite?[y/n]");
				String input = System.console().readLine();
				if(input.equals("y")) {
					String sqlRemove = "DELETE FROM pages where book_code='" + bookCode + "'";
					stmt.executeUpdate(sqlRemove);
				} else {
					System.exit(-1);
				}
			}

			//Start querying Lucene index

			boolean showDiac = true;
			String searchId = "0"; //#L0
			Display.DocInfo book = Display.getDisplay(indexPath, searchId, showDiac);
			bookTitle = book.title;

			//#L1
			List<Search.HitInfo2> kotob = Search.findItemKids(indexPath, "0");
			for (Search.HitInfo2 kitab : kotob) { //#L1 كتاب
				//#L3: باب أو حديث
				List<Search.HitInfo2> hadiths = Search.findItemKids(indexPath, kitab.id);
				for (Search.HitInfo2 hadith : hadiths) {
					showDiac = true;
					DocInfo hadith2 = Display.getDisplay(indexPath, hadith.id, showDiac);

//				"page_id, parent_id, book_code, book_title, title, main_text, footnote, image, audio, full_text );";

					String insertSql = "INSERT INTO pages(page_id, parent_id, book_code, book_title, title, main_text, footnote, image, audio, full_text ) " +
							"VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
					PreparedStatement statement = connection.prepareStatement(insertSql);
					statement.setString(1, hadith2.id);
					statement.setString(2, hadith2.parentID);
					statement.setString(3, bookCode);
					statement.setString(4, bookTitle);
					statement.setString(5, hadith2.title);
					statement.setString(6, hadith2.basicText);
					statement.setString(7, hadith2.extendedText);
					statement.setString(8, hadith2.quranImage);
					statement.setString(9, hadith2.quranAudio);
					showDiac = false;
					DocInfo hadithNoDiac = Display.getDisplay(indexPath, hadith.id, showDiac);
					statement.setString(10, getPageForFTS(hadithNoDiac));
					statement.executeUpdate();

					break;
				}
				break;
			}

			//just display records

			String sqlDisplay = "SELECT * FROM pages";
			ResultSet displayRs = stmt.executeQuery( sqlCheckExist);
			while (rs.next()) {
				for(int i = 1 ; i <= 10; i++) {
					System.err.println(displayRs.getString(i));
				}
			}
			displayRs.close();

		} finally
		{
			stmt.close();
			connection.commit();
			connection.close();
		}
	}

//
//	private void createTablesSimple(Statement stmt) throws SQLException {
//
//		//FTS4 with no diacritics
//...
//
// 	}
//
//	private void createTables(Statement stmt) throws SQLException {
//		String createSql = "CREATE TABLE IF NOT EXISTS books " +
//                "(book_code CHAR(10) PRIMARY KEY	NOT NULL," +
//                " title   TEXT  			 NOT NULL)";
//		stmt.executeUpdate(createSql);
//
//		//Table to include contents with full diacritics
//		String createSql2 = "CREATE TABLE IF NOT EXISTS pages " +
//                "(page_id INT PRIMARY KEY AUTOINCREMENT NOT NULL," +
//                "parent_page_id INT," +
//                "book_code CHAR(10)," +
//                "title TEXT," +
//                "main_text TEXT," +
//                "footnote  TEXT," +
//                "quran_image  CHAR(20)," +
//                "quran_audio  CHAR(20))";
//		stmt.executeUpdate(createSql2);
//
//		//FTS4 with no diacritics
//		String createFts4 = "CREATE VIRTUAL TABLE IF NOT EXISTS pages_fts USING fts4(page_id, body);";
//		stmt.executeUpdate(createFts4);
//	}


	private String getPageForFTS(DocInfo page3) {
		String template = PAGE_TEMPLATE;
		String pageText = template.replaceAll("HADITH_TITLE", page3.title);
		pageText = pageText.replaceAll("HADITH_CONTENT", page3.basicText);
		pageText = pageText.replaceAll("HADITH_WORDS_MEANING", page3.extendedText);
		return pageText;
	}


}
