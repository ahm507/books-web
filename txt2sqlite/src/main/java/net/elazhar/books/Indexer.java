/**
 * 
 */
package net.elazhar.books;

//import org.apache.lucene.document.Document;
//import org.apache.lucene.document.Field;
//import org.apache.lucene.index.IndexWriter;
//import org.apache.lucene.store.Directory;
//import org.apache.lucene.store.FSDirectory;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Stack;
import java.util.Vector;
import java.util.logging.Logger;


/**
 * This class is the main driver to the indexer
 * @author ahamad
 *
 */
public class Indexer {
	private Settings settings = new Settings();
	private Logger logger = Logger.getLogger("Indexer");	
	private int indexedRecordsCount; 

//	IndexWriter indexWriter = null;
//   	Directory indexDirectory = null;
	

	public int indexDoc() throws Exception, IOException, InterruptedException {

        indexedRecordsCount = 0;

	    String inputPath  = new java.io.File( "." ).getCanonicalPath();
        String indexPath = inputPath;

		settings.loadSettings(inputPath + "/build.properties");

//		indexWriter = new IndexWriter(indexPath, new SimpleAnalyzer());
		
		//get array of all text files to index from build.ini
		String [] filesNames = settings.getFileList();
		if(filesNames == null || filesNames.length == 0) {
			throw new Exception("No files specified to index!");
		}
		int bookID = settings.getDocID();
		int recordID = 0;
		int parentID = -1;
		Stack<Integer> parentStack = new Stack<Integer>();
		parentStack.push(0);
		int curLevel = 0;
		boolean atRecordZero = true; 
		
		int outCounter = 0; 
				
		//for all files, one by one, get record
		for(int i =0 ; i < filesNames.length ; i++) {
			String fileName =  filesNames[i];
			Vector<String> lines = readLines(inputPath, fileName);
			logger.info(String.format("File is started to be indexed: %s", fileName));
			String record = "";
			for(int j = 0 ; j < lines.size() ; j++) {
				
				if(isNewRecord (lines.elementAt(j)) ) { //i.e. starts with #L

//////////////////////////////////////					
					if(atRecordZero == true) { //this is the initial record 0
						
						atRecordZero = false;
						record = lines.elementAt(j) + "\r\n";
						continue;
					}
		
					ParsedText ptext = ParsedText.parseText(record, settings.getTitleSep()); 
					
					indexRecord(recordID, bookID, parentID, ptext.getTitle(), 
							ptext.getText(), ptext.getTextNoVowels());
					outCounter++;
					//if(outCounter % 100 == 0) { 
                    //    logger.info(String.format("Record number %d is indexed", outCounter));
			        //    //Thread.sleep(500); //!!!
					//}
					
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
////////////////////////////////////////					
					
					record += lines.elementAt(j) + "\r\n"; //add new lines again
				
				}
////////////////////////////////////////
				
				if(j == lines.size() -1) {
					//Write any remaining record that could be missed
					record = record.trim();
					if(record.length() > 0) {
					ParsedText ptext = ParsedText.parseText(record, settings.getTitleSep()); 
					indexRecord(recordID, bookID, parentID, ptext.getTitle(), 
							ptext.getText(), ptext.getTextNoVowels());
					}
					
				}				
				
			} // cur file end
			
		} // all files end
		
		//Close the index
//		if(out != null) {
//			out.println("Indexing is OK. Now will optimize the index.<br>");
//			logger.info("Indexing is OK. Now will optimize the index.");
//		}
//		indexWriter.optimize();
//
//		indexWriter.close();
		
//		if(out != null) {
//			out.println("Build is completed successfully<br>");
        logger.info("Build is completed successfully");
//			out.flush();
//		}
		
		return indexedRecordsCount;
	}
	
	/**
	 * decide about the level number of the record by checking record string with the marks
	 * @param record the string recoed
	 * @return the level number
	 */
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
	
	/**
	 * check settings for the record breaker mark and check if the string starts with it and report the result.
	 * @param line line of text
	 * @return true of the line starts with a new record breaker
	 */
	private boolean isNewRecord(String line) {
		
		line = line.trim();
		if(line.startsWith(settings.getRecordBreaker()) == false) {
			return false;
		}
		return true;
	}

	
	/**
 * Read all lines from a text in one shot
 * @param fileName
 * @return 
 * @return array of all read lines
 * @throws FileNotFoundException 
 */	
	private Vector<String> readLines(String inputPath, String fileName) 
							throws FileNotFoundException,  IOException{
	    Vector<String> lines = new Vector<String>(); 
		BufferedReader fileReader = new BufferedReader(new FileReader (inputPath + "/" + fileName));
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
	
	
	/**
	 * Parses record text according to some hardcoded rules and using some settings settings, 
	 * the title breaker if any is used to determine the title, else it is assumed to 
	 * be the first line. 
	 * @param recordID unique ID that starts with 0
	 * @param bookID ID that starts with 0
	 * @param parentID parent ID to be able to get the tree reationship
	 * @param title 
	 * @param text text with the Arabic vowels
	 * @param textNoVoweles text without the Arabic vowels
	 * @throws IOException 
	 */
	public void indexRecord(int recordID, int bookID, int parentID, String title, 
			String text, String textNoVoweles) throws IOException {

//		Document doc = new Document();
//			doc.add(new Field("id", String.valueOf(recordID), Field.Store.YES, Field.Index.UN_TOKENIZED));
//			doc.add(new Field("docID", String.valueOf(bookID), Field.Store.YES, Field.Index.UN_TOKENIZED));
//			doc.add(new Field("parentID", String.valueOf(parentID), Field.Store.YES, Field.Index.UN_TOKENIZED));
//			doc.add(new Field("title", title, Field.Store.YES, Field.Index.TOKENIZED));
//			doc.add(new Field("content", text, Field.Store.YES, Field.Index.NO)); //Stored but not indexed
//			doc.add(new Field("content2", textNoVoweles, Field.Store.NO, Field.Index.TOKENIZED)); //Indexed but not stored
//			indexWriter.addDocument(doc);

			//This should be injected to SQLite record

        logger.info("insert record: " + title);
		indexedRecordsCount ++;

	}
	
}
