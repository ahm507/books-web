package waqf.sqlite;

import org.apache.lucene.queryParser.ParseException;
import waqf.books.Display;
import waqf.books.Display.DocInfo;
import waqf.books.Search;

import java.io.UnsupportedEncodingException;
import java.sql.*;
import java.util.List;


public class GenSqlite {
	String bookCode;
	String indexPath;
	Connection connection = null;
	PreparedStatement statement;
	int count = 0;

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
	}

	public GenSqlite(String bookId, String indexFolder, String bookCode) {
//		super();
		this.bookCode = bookCode;
		this.indexPath = indexFolder;
//		this.bookPath = bookCode;
	}

	public void generateSqlite() throws Exception {

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

//			"page_id, parent_id, book_code, page, page_fts );";

			//FTS4 with no diacritics
			String createFts4 = "CREATE VIRTUAL TABLE IF NOT EXISTS pages USING fts3(" +
					"page_id, parent_id, book_code, title, page, page_fts);";
			stmt.executeUpdate(createFts4);

			//Ensure the book has no records
//			ResultSet rs = stmt.executeQuery( "select * from books where book_code = '" + this.bookCode + "'");
			String sqlCheckExist = "SELECT * FROM pages WHERE book_code  MATCH '" + bookCode + "'";
			ResultSet rs = stmt.executeQuery( sqlCheckExist);
			if (rs.next()) {
				rs.close();
				System.out.print("Existing records exit, overwrite?[y/n]: ");
				String input = System.console().readLine();
				if(input.equals("y")) {
					String sqlRemove = "DELETE FROM pages where book_code='" + bookCode + "'";
					stmt.executeUpdate(sqlRemove);
				} else {
					System.exit(-1);
				}
			}

			//Start querying Lucene index

//			boolean showDiac = true;
//			String searchId = "0"; //#L0
//			Display.DocInfo book = Display.getDisplay(indexPath, searchId, showDiac);


			//#L0

			//#L1

			count = 0;

			processLevel("0");

			System.out.println(".");

			//just display records
			String sqlDisplay = "SELECT * FROM pages where page_id MATCH '0' ";
			ResultSet displayRs = stmt.executeQuery( sqlCheckExist);
			if (displayRs.next()) {
				for(int i = 1 ; i <= 5; i++) {
					System.err.println(displayRs.getString(i));
				}
			}
			displayRs.close();
			System.out.println("\r\n" + count + " records of " + bookCode + ": is indexed");

		} finally
		{
			stmt.close();
			connection.commit();
			connection.close();
		}
	}


	private void processLevel(String pageId) throws Exception {

		boolean showDiac = true;
		DocInfo docInfo = Display.getDisplay(indexPath, pageId, showDiac);
		String page = getPage(docInfo);
//		String pageNoDiacritics = Display.removeDiacritics(page);
		String pageNoDiacritics = getPageNoDiacritics(docInfo);

				String insertSql = "INSERT INTO pages(page_id, parent_id, book_code, title, page, page_fts ) " +
				"VALUES(?, ?, ?, ?, ?, ?);";
		statement = connection.prepareStatement(insertSql);
		statement.setString(1, docInfo.id);
		statement.setString(2, docInfo.parentID);
		statement.setString(3, bookCode);
		statement.setString(3, docInfo.title);
		statement.setString(4, page);
		statement.setString(5, pageNoDiacritics);
		statement.executeUpdate();

		count++;
		if(count % 100 == 0) {
			System.out.print(".");
		}

		//for all kids
		List<Search.HitInfo2> hits = Search.findItemKids(indexPath, pageId);
		for (Search.HitInfo2 hit : hits) {
			processLevel(hit.id);
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


	private String getPage(DocInfo page3) {
		String template = "CONTENT\r\n##FOOTNOTE";
		String pageText = template.replaceAll("TITLE", page3.title);
		pageText = pageText.replaceAll("CONTENT", page3.basicText);
		pageText = pageText.replaceAll("FOOTNOTE", page3.extendedText);
		return pageText;
	}

	private String getPageNoDiacritics(DocInfo page3) throws UnsupportedEncodingException {
		String template = "TITLE\r\n##CONTENT\r\n##FOOTNOTE";
		String pageText = template.replaceAll("TITLE", page3.title);
		pageText = pageText.replaceAll("CONTENT", page3.basicText);
		pageText = pageText.replaceAll("FOOTNOTE", page3.extendedText);
		return Display.removeDiacritics(pageText);
	}



}
