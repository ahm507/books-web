package net.elazhar.books;

import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.sql.*;
import org.springframework.beans.factory.annotation.Value;

import java.util.logging.Logger;

@Component
public class SQLiteWriter {
	private String bookCode;
	private String indexPath;
	private Connection connection = null;
	private PreparedStatement insertStatement = null;
	private Logger logger = Logger.getLogger("SQLiteWriter");	

    //@Value("${mysqlite.connection}") 
    private String sqliteConnection = "jdbc:sqlite:/Users/ahm/projects/books-web/data/books.sqlite";

//    public SQLiteWriter(@Value("${mysqlite.connection}") String sqliteConnection) {
//        this.sqliteConnection = sqliteConnection;
//    }
	
    public void init(String bookCode) {

		this.bookCode = bookCode;
		try {
			Class.forName("org.sqlite.JDBC");
            logger.info("sqlite driver loaded");
			
            connection = DriverManager.getConnection(sqliteConnection);
            logger.info("Sqlite connection is created");

			connection.setAutoCommit(false);
			logger.info("auto commit is set to false");
		} catch ( Exception e ) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			System.exit(-1);
		}

    }

    public void close() throws SQLException {
		if(insertStatement != null) insertStatement.close();
		if(connection != null) connection.close();
	}


    public void createTables() throws SQLException {
		Statement stmt = null;
		try {
			stmt = connection.createStatement();
        	// just drop the table
            //String drop = "DROP TABLE IF EXISTS pages";
			//stmt.executeUpdate(drop);
            //connection.commit();
            //logger.info("pages table is dropped");

            String createFts3 = "CREATE VIRTUAL TABLE IF NOT EXISTS pages USING fts3(page_id, parent_id, book_code, title, page, page_fts);";
			stmt.executeUpdate(createFts3);
            connection.commit();
            logger.info("pages table is created - IF NOT EXISTS");

			//Ensure the book has no records
			logger.info("Checking if records are exist for this book, " + bookCode);
            String sqlCheckExist = "SELECT * FROM pages WHERE book_code = '" + bookCode + "'";
			ResultSet rs = stmt.executeQuery( sqlCheckExist);
			if (rs.next()) {
				rs.close();
				//System.out.print("Existing records exit, overwrite is not allowed.");
                logger.info("Existing records exit, old records will be removed.");
                
                stmt.executeUpdate("delete from pages where book_code='" + bookCode + "';");
                connection.commit();
			}

            if(insertStatement == null) {
                String insertSql = "INSERT INTO pages(page_id, parent_id, book_code, title, page, page_fts) VALUES(?, ?, ?, ?, ?, ?);";
			    insertStatement = connection.prepareStatement(insertSql);
                logger.info("Insertion PreparedStatement is initiated"); 
            }

        } finally {
			if(stmt != null) stmt.close();
		}

    }


    public void appendRecord(int pageId, String parentId, String title, String page, String pageNoVowels)
			throws SQLException, UnsupportedEncodingException {
        
        try {
			insertStatement.setInt(1, pageId);
			if(pageId == 0) parentId = "NO_PARENT";
			insertStatement.setString(2, parentId);
			insertStatement.setString(3, this.bookCode);
			insertStatement.setString(4, title);
			insertStatement.setString(5, page);
			insertStatement.setString(6, pageNoVowels);
			insertStatement.executeUpdate();
            } finally {
	    		connection.commit();
            }
    }

}
