/** 
 * 
 */
package waqf.display;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

 
/** 
 * Encabsulate the books storage file
 * @author ahamad
 * 
 */
public class Books {
	Properties props = null;
	
	public void loadSettings(String propertiesFile) 
			throws FileNotFoundException, IOException {
		props = new Properties();
			props.load(new FileInputStream (propertiesFile));
	}
	
	public int getGroupCount() {
		int count = 0;
		int group = 1;
		for(;;) {
			String title = props.getProperty(String.format("g%d.title", group ));
			if(title == null || title.length() == 0) {
				break;
			}
			else {
				group++;
				count++;
			}
		}
		return count;
	}
	
	public String getGroupTitle(int group) 
					throws UnsupportedEncodingException {
		String title = props.getProperty(String.format("g%d.title", group ));
		return new String(title.getBytes("Cp1252"), "Cp1256");
	}
	
	public int getGroupBooksCount(int group) {
		int count = 0;
		int book = 1;
		for(;;) {
			String title = props.getProperty(String.format("g%db%d.title", group, book ));
			if(title == null || title.length() == 0) {
				break;
			}
			else {
				book++;
				count++;
			}
		}
		return count;
	}
	
	public boolean isBookEnabled(int group, int book) {
		String enabled = props.getProperty(String.format("g%db%d.enable", group, book ));
		if("true".equals(enabled)) {
			return true;
		}
		return false;
	}
	
	public String getBookTitle(int group, int book) 
							throws UnsupportedEncodingException {
		String title = props.getProperty(String.format("g%db%d.title", group, book ));
		return new String(title.getBytes("Cp1252"), "Cp1256");
	}
	
	
}
