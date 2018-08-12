package waqf.sqlite;

import org.apache.lucene.queryParser.ParseException;
import waqf.books.Display;
import waqf.books.Display.DocInfo;
import waqf.books.Search;

import java.io.UnsupportedEncodingException;
import java.sql.*;
import java.util.List;


public class GenSqlite2 {
	String bookCode;
	String indexPath;
	Connection connection = null;
	PreparedStatement statement1, statement2;
	int count = 0;

	int publicId = 1;

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
		GenSqlite2 gen = new GenSqlite2(bookCode, indexPath, bookCode);
		gen.generateSqlite();
	}

	public GenSqlite2(String bookId, String indexFolder, String bookCode) {
//		super();
		this.bookCode = bookCode;
		this.indexPath = indexFolder;
//		this.bookPath = bookCode;
	}

	public void generateSqlite() throws Exception {

		try {
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection("jdbc:sqlite:sonna.sqlite");
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
//			String createFts4 = "CREATE VIRTUAL TABLE IF NOT EXISTS pages USING fts3(" +
//					"page_id, parent_id, book_code, title, page, page_fts);";
			String createFts4 = "CREATE VIRTUAL TABLE IF NOT EXISTS pages USING fts4(" +
					"content='', book_code, parent_id, page_fts);";
			stmt.executeUpdate(createFts4);

			String createTable = "CREATE TABLE IF NOT EXISTS details (" +
					"page_id, book_code, parent_id, title, page);";
			stmt.executeUpdate(createTable);

			//Ensure the book has no records
//			ResultSet rs = stmt.executeQuery( "select * from books where book_code = '" + this.bookCode + "'");
			String sqlCheckExist = "SELECT * FROM details WHERE book_code = '" + bookCode + "'";
			ResultSet rs = stmt.executeQuery( sqlCheckExist);
			if (rs.next()) {
				rs.close();
				System.out.print("Existing records exit, overwrite is not allowed.");
//				System.out.print("Existing records exit, overwrite?[y/n]: ");
//				String input = System.console().readLine();
//				if(input.equals("y")) {
//					String sqlRemove = "DELETE FROM pages where book_code='" + bookCode + "'";
//					stmt.executeUpdate(sqlRemove);
//				} else {
				System.exit(-1);
//				}
			}

			count = 0;
			processLevel("0");
			//Optimize the indexes
			//INSERT INTO pages(pages) VALUES('optimize');
			System.out.println(".");

			//just display records
			String sqlDisplay = "SELECT * FROM details where page_id = '0' ";
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

		String insertSql = "INSERT INTO pages(rowid, book_code, parent_id, page_fts) VALUES(?, ?, ?, ?);";
		String parentID = docInfo.parentID;
		if(docInfo.id.equals("0")) {
			parentID = "NO_PARENT";
		}
//		if(docInfo.id.equals("0")) {
//			statement2.setString(3, "NO_PARENT"); //especial value for sqlite, as -1 MATCH 1
//		} else {
//			statement2.setString(3, docInfo.parentID);
//		}

		statement1 = connection.prepareStatement(insertSql);
//		statement1.setString(1, Integer.toString(publicId));
		statement1.setInt(1, publicId);
		statement1.setString(2, bookCode);
		statement1.setString(3, docInfo.parentID);
		statement1.setString(4, pageNoDiacritics);

		statement1.executeUpdate();
		publicId ++;

		///////////////////////////////////////////////////////////
		//Insert into table 2
		String insertSql2 = "INSERT INTO details(page_id, book_code, parent_id, title, page) VALUES(?, ?, ?, ?, ?);";
		statement2 = connection.prepareStatement(insertSql2);
		statement2.setString(1, docInfo.id);
		statement2.setString(2, bookCode);
		statement2.setString(3, docInfo.parentID);
		statement2.setString(4, docInfo.title);
		statement2.setString(5, page);
		statement2.executeUpdate();

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
