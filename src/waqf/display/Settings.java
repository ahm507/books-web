package waqf.display;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.Vector;
import java.util.logging.Logger;

/**
 * Load the settings of the book to index.
 * @author ahamad 
 * 
 */

public class Settings {

	String [] fileList;
	int docID;
	String [] levelsBreakers;
	String recordBreaker;
	String titleSep;

/**
 * Load the settings form the passed file. The passed flile name should have full path 
 * or in the current directory.
 * @param propertiesFile
 * @throws IOException 
 * @throws FileNotFoundException 
 */	
	
	public void loadSettings(String propertiesFile) 
					throws FileNotFoundException, IOException {
		
		Logger logger = Logger.getLogger("waqf.indexer.settings");
		
		Properties props = new Properties();

		
		props.load(new FileInputStream (propertiesFile));
		String bookID = props.getProperty("BookID");
		docID = Integer.parseInt(bookID);
		
		recordBreaker = props.getProperty("RecordBreaker");
		
		//Get file list
		Vector<String> vec = new Vector<String>(); 
		for (int i = 0 ;  ; i++) {
			String keyName = "";
			keyName = String.format("File%d", i+1);
			String value = props.getProperty(keyName);
			if(value == null || value.length() == 0)
				break;
			value = value.trim();
			vec.add(value);
		}
		fileList = new String[vec.size()];
		vec.toArray(fileList);
		
		logger.fine(String.format("files count=%d", fileList.length));
		//read levelsBreakers
		vec.clear();
		for (int i = 0 ;  ; i++) {
			String keyName = "";
			keyName = String.format("Level%d", i+1);
			String value = props.getProperty(keyName);
			if(value == null || value.length() == 0)
				break;
			
			vec.add(value);
		}
		levelsBreakers = new String[vec.size()];
		vec.toArray(levelsBreakers);
		logger.fine(String.format("Levels count=%d", levelsBreakers.length));
		titleSep = props.getProperty("TitleSperator");
	}
	
	public String [] getFileList() {
		return fileList;
	}
	
	public int getDocID() {
		return docID;
	}

	public String [] getLevelsBreakers() {
		return levelsBreakers;
	}
	
	public String getRecordBreaker() {
		return recordBreaker;
	}

	/**
	 * @return the titleSep
	 */
	public String getTitleSep() {
		return titleSep;
	}
	
}
