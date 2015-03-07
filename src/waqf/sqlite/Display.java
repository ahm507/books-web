package waqf.sqlite;

import org.apache.lucene.queryParser.ParseException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;


public class Display {
//	String bookCode;
//	String indexPath;

	public static void main(String[] args) throws ParseException, Exception {

//		String sqliteFile;// = "g2b1";
		if(args.length < 2) {
			System.err.println( "Parameters Error!");
			System.err.println( "Usage$ display.sh path/to/sqlit.db queryString");
			System.err.println("");
			System.exit(-1);
		}
		String sqliteFile = args[0];
		String query = args[1];
		Display gen = new Display();
		gen.generateSqlite(sqliteFile, query);
	}

	public void generateSqlite(String sqliteFile, String queryString) throws Exception {

//		super();
//		this.bookCode = bookCode;
//		this.indexPath = queryString;
//		this.bookPath = bookCode;

		Connection connection = null;
		try {
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection("jdbc:sqlite:" + sqliteFile);
			connection.setAutoCommit(false);

		} catch ( Exception e ) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			System.exit(-1);
		}

		Statement stmt = null;
		try {
			//Handle Books Table of titles only
			stmt = connection.createStatement();


			//just display records
			String sqlDisplay = "SELECT * FROM pages where page MATCH '" + queryString + "'";
			ResultSet displayRs = stmt.executeQuery( sqlDisplay);
			int count = 0;
			while (displayRs.next()) {
				for(int i = 1 ; i <= 10; i++) {
					System.out.println(displayRs.getString("page"));
				}
				count ++;
				if(count > 10) break;
			}

			System.out.println(count + " records are displayed");

			displayRs.close();


		} finally
		{
			stmt.close();
			connection.commit();
			connection.close();
		}
	}

}
