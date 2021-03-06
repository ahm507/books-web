package waqf.epub;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Properties;

public final class FileUtil {

	private FileUtil() { //means it is a utility class
	}
	
	public static void writeToFile(String fileName, String contents)
			throws IOException, FileNotFoundException {
		new FileOutputStream(fileName, true).close(); //create empty file if not exist
		PrintWriter out = new PrintWriter(fileName); //create the file if not exist
		out.println(contents); 
		out.close();
	}

	public static Properties loadProperties(String filePath) throws IOException {
		Properties props = new Properties();
		InputStream is = ClassLoader.getSystemResourceAsStream(filePath);
		props.load(is);
		return props;
	}

}
