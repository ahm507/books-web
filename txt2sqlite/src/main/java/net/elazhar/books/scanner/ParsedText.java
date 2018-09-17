package net.elazhar.books.scanner;

import java.io.UnsupportedEncodingException;

/**
 * Arabic text is parsed to get title and get a copy of the content without Arabic vowels.
 * @author ahamad
 *  
 */

public class ParsedText {
	private static final String NEW_LINE = "\r\n";
	private final String title;
	private final String text;
	private final String textNoVowels;
	
	public ParsedText(String title, String text, String textNoVowels) {
		this.title = title;
		this.text = text;
		this.textNoVowels = textNoVowels;
	}
	
/**
 * This function parses text according to the title separator rule.
 * @param text is the text to be parsed
 * @param titleSep the separator used to separate title form body
 * @return an object that has getters that are ready to be used
 * @throws UnsupportedEncodingException 
 */	
	public static ParsedText parseText(String text, String titleSep, String[] levelBreakers) 
								throws UnsupportedEncodingException {
		String title1="", text1="";
		String[] splitted = text.split(titleSep);
		if(splitted != null && splitted.length == 2) {
			title1 = splitted[0];
			text1 = splitted[1];
		} else if(splitted != null && splitted.length == 1) {
			//may be there is no separator. Use NewLine as a separator
			String[] splitted2 = text.split(NEW_LINE);
			title1 = splitted2[0];
			text1 = "";
			//if last char is not new newline remove last new line
			boolean lastCharNewLine = false;
			if(text.lastIndexOf(NEW_LINE) == text.length() -2) {
				lastCharNewLine = true;
			}
			//Collect all the other lines again
			for(int i=1 ; i < splitted2.length ; i++) {
				text1 = text1 + splitted2[i]; 
				if(i == splitted2.length-1) {
					if( lastCharNewLine == true) {
						text1 = text1 + NEW_LINE;
					}
				} else {
					text1 = text1 + NEW_LINE;
				}
			}
				
		} else {
			title1 = "";
			text1 = "";
		}
		
        //clean up title
        for(String level : levelBreakers) {
            title1 = title1.replaceAll(level, "");
        }

        
        return new ParsedText(title1, text1, Display.removeVowels(text1));
	}

	public static String adjustEncoding(String title1) throws UnsupportedEncodingException {
		return new String(title1.getBytes("Cp1252"), "Cp1256");
	}
	
	public String getTitle() {
		return title;
	}
	
	public String getText() {
		return text;
	}
	
	public String getTextNoVowels() {
		return textNoVowels;
	}
	
}
