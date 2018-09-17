/**
 * 
 */
package net.elazhar.books;

//import org.apache.lucene.document.Document;
//import org.apache.lucene.document.Field;
//import org.apache.lucene.index.IndexWriter;
//import org.apache.lucene.store.Directory;
//import org.apache.lucene.store.FSDirectory;

import java.io.*;
import java.util.Stack;
import java.util.Vector;
import java.util.logging.Logger;

import org.springframework.stereotype.Service;


/**
 * This class is the main driver to the indexer
 * @author ahamad
 *
 */

@Service
public class Indexer {
	private Settings settings = new Settings();
	private Logger logger = Logger.getLogger("Indexer");	
	private int indexedRecordsCount;
	IndexWriter sqliteWriter;

	public int indexDoc() throws Exception, IOException, InterruptedException, WiterMissingException {

		if(sqliteWriter == null) {

			throw new WiterMissingException("A SearchWriter object must be set");
		}

        
        indexedRecordsCount = 0;

	    String inputPath  = new java.io.File( "." ).getCanonicalPath();
        String indexPath = inputPath;

		settings.loadSettings(inputPath + "/build.properties");

		//get array of all text files to index from build.ini
		String [] filesNames = settings.getFileList();
		if(filesNames == null || filesNames.length == 0) {
			throw new Exception("No files specified to index!");
		}
		String bookID = settings.getDocID();
        sqliteWriter.init(bookID);

        int recordID = 0;
		int parentID = -1;
		Stack<Integer> parentStack = new Stack<Integer>();
		parentStack.push(0);
		int curLevel = 0;
		boolean atRecordZero = true; 
		
        String record = "";
		//for all files, one by one, get record
		for(int i =0 ; i < filesNames.length ; i++) {
			String fileName =  filesNames[i];
			Vector<String> lines = readLines(inputPath, fileName);
			logger.info(String.format("File is started to be indexed: %s", fileName));
			for(int j = 0 ; j < lines.size() ; j++) {
				
				if(isNewRecord (lines.elementAt(j)) ) { //i.e. starts with #L

					if(atRecordZero == true) { //this is the initial record 0
						atRecordZero = false;
						record = lines.elementAt(j) + "\r\n";
						continue;
					}
		
					ParsedText ptext = ParsedText.parseText(record, settings.getTitleSep(), settings.getLevelsBreakers()); 
					
					indexRecord(recordID, bookID, parentID, ptext.getTitle(), 
							ptext.getText(), ptext.getTextNoVowels());

                   	recordID++;

					record = lines.elementAt(j) + "\r\n";
				
					int newRecordLevel = getRecordLevel(record);
					
					if(newRecordLevel > curLevel) {
						curLevel = newRecordLevel;
						parentStack.push(parentID);
						parentID = recordID-1;
					}
					else if (newRecordLevel < curLevel) {
						//Why loop? The problem is in Muslim book. You can find level 
						//1 just after level 3. So we have to handle it generally.
						int diff = curLevel - newRecordLevel;

						for(int k = 0 ; k < diff; k++) {
							parentID = parentStack.pop();
							curLevel = newRecordLevel;
						}
						
					}
					
				} else {
					
					record += lines.elementAt(j) + "\r\n"; //add new lines again
				
				}
			
			} // cur file end
			
		} // all files end


		
    	//Handle last file last record
        record = record.trim();
        ParsedText ptext = ParsedText.parseText(record, settings.getTitleSep(), settings.getLevelsBreakers()); 
        indexRecord(recordID, bookID, parentID, ptext.getTitle(), 
            ptext.getText(), ptext.getTextNoVowels());

		
        logger.info("Build is completed successfully");
        
        sqliteWriter.close();
    
    	
	    return indexedRecordsCount;
	}
	
	private int getRecordLevel(String record) {
		record = record.trim();
		String []levelBreakers = settings.getLevelsBreakers();
		for(int i=0; i<levelBreakers.length; i++) {
			if(record.indexOf(levelBreakers[i]) != -1) 
				return i;
		}
		
		logger.severe(String.format("Level is 0 [%s]", record.substring(0, 20)));
		return -1;
	}
	
	
    private boolean isNewRecord(String line) {
		
		line = line.trim();
		if(line.startsWith(settings.getRecordBreaker()) == false) {
			return false;
		}
		return true;
	}


	private Vector<String> readLines(String inputPath, String fileName) 
							throws FileNotFoundException,  IOException{
	    Vector<String> lines = new Vector<String>(); 
		
        String encoding = "Cp1256";
        BufferedReader fileReader = new BufferedReader(new InputStreamReader(
                new FileInputStream (inputPath + "/" + fileName), encoding));

		for(;;) {
			String line = fileReader.readLine();
			if(line == null) {  // end of file
				break;
			}
			if(line.length() > 0)
				lines.add(line);
		}			
		fileReader.close();
		return lines;
	}
	
	
	public void indexRecord(int recordID, String bookID, int parentID, String title
        , String text, String textNoVoweles) throws Exception {

        //This should be injected to SQLite record
        String parentIdString = String.valueOf(parentID);
        if(parentID == -1) parentIdString = "NO_PARENT";
            
        sqliteWriter.appendRecord(recordID, parentIdString, title, text, textNoVoweles);
        logger.info(String.format("Record %s-%d-%s:%s", bookID, recordID, parentIdString, title));
		indexedRecordsCount ++;

	}

	public void setSetWriter(IndexWriter sqliteWriter) {
		this.sqliteWriter = sqliteWriter;
	}
}
