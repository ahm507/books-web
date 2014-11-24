package waqf.test;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

public class TestConfig {

	static public String getBookIndexPath() {
		URL url = ClassLoader.getSystemResource("unit-test.properties");
		String path = url.getPath();
		path = path.substring(0, path.lastIndexOf("/")); // truncate file name
		path = path.substring(0, path.lastIndexOf("/"));//truncate "/classes"
		path = path.substring(0, path.lastIndexOf("/"));//truncate "/build"		
		path = path + "/test/book1/index";
		return  path;
	}

	public static Properties getUnitTestConfig() throws IOException {
		Properties props = new Properties();
		InputStream is = ClassLoader.getSystemResourceAsStream("unit-test.properties");
		props.load(is);
		return props;
	}

	public static final String TEST_ROOT_PATH = "/Users/Macpro/Projects/Eclipse-AlyRashed/Books2/test/";

	
	
}
