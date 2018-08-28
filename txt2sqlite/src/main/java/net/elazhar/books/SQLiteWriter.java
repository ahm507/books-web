package net.elazhar.books;

import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.sql.*;

@Service
public class SQLiteWriter {
	String bookCode;
	String indexPath;
	Connection connection = null;
	PreparedStatement preparedStatement = null;
	
//	int count = 0;

//	int publicId = 1;

//	public static void main(String[] args) throws ParseException, Exception {
//
//		String bookCode;// = "g2b1";
//		if(args.length < 2) {
//			System.err.println( "Parameters Error!");
//			System.err.println( "Usage$ indexer.sh path/to/lucene/index  book_code");
//			System.err.println("");
//			System.exit(-1);
//		}
//		String indexPath = args[0];
//		bookCode = args[1];
//		System.out.println(bookCode + ": Starting conversion...");
//		GenSqlite2 gen = new GenSqlite2(bookCode, indexPath, bookCode);
//		gen.generateSqlite();
//	}


    public void init(String bookCode) {

		this.bookCode = bookCode;
		try {
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection("jdbc:sqlite:books.sqlite");
			connection.setAutoCommit(false);
			String insertSql = "INSERT INTO pages(page_id, parent_id, book_code, title, page, page_fts) VALUES(?, ?, ?, ?, ?, ?);";
			preparedStatement = connection.prepareStatement(insertSql);

		} catch ( Exception e ) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			System.exit(-1);
		}

    }

    public void close() throws SQLException {
		if(preparedStatement != null) preparedStatement.close();
		if(connection != null) connection.close();
	}


    public void createTables() throws SQLException {
		Statement stmt = null;
		try {
			stmt = connection.createStatement();
        	// just drop the table
			String drop = "DROP TABLE pages";
			stmt.executeUpdate(drop);

            String createFts3 = "CREATE VIRTUAL TABLE IF NOT EXISTS pages USING fts3(page_id, parent_id, book_code, title, page, page_fts);";
			stmt.executeUpdate(createFts3);

			//Ensure the book has no records
			String sqlCheckExist = "SELECT * FROM pages WHERE book_code = '" + bookCode + "'";
			ResultSet rs = stmt.executeQuery( sqlCheckExist);
			if (rs.next()) {
				rs.close();
				System.out.print("Existing records exit, overwrite is not allowed.");
				System.exit(-1);
			}

        } finally {
			if(stmt != null) stmt.close();
			connection.commit();
		}

    }


    public void appendRecord(int pageId, String parentId, String title, String page, String pageNoVowels)
			throws SQLException, UnsupportedEncodingException {
        
        //String createFts3 = "CREATE VIRTUAL TABLE IF NOT EXIST pages USING fts3(page_id, parent_id, book_code, title, page, page_fts);";
        
        try {
			preparedStatement.setInt(1, pageId);
			if(pageId == 0) parentId = "NO_PARENT";
			preparedStatement.setString(2, parentId);
			preparedStatement.setString(3, this.bookCode);
			preparedStatement.setString(4, title);
			preparedStatement.setString(5, page);
			preparedStatement.setString(6, pageNoVowels);
			preparedStatement.executeUpdate();
            } finally {
	    		connection.commit();
            }

    }

/*

	private void processLevel(String pageId) throws Exception {

		boolean showDiac = true;
		DocInfo docInfo = Display.getDisplay(indexPath, pageId, showDiac);
		String page = getPage(docInfo);
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

		stmt = connection.prepareStatement(insertSql);
//		stmt.setString(1, Integer.toString(publicId));
		stmt.setInt(1, publicId);
		stmt.setString(2, bookCode);
		stmt.setString(3, docInfo.parentID);
		stmt.setString(4, pageNoDiacritics);

		stmt.executeUpdate();
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

*/


}
