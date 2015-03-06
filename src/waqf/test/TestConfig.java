package waqf.test;

import java.net.URL;

final public class TestConfig {

	private static final String CLASS_ROOT_FILE = "chapter-template.xhtml";

	private TestConfig() {
	}

	static public String getBookIndexPath() {
		URL url = ClassLoader.getSystemResource(CLASS_ROOT_FILE);
		String path = url.getPath();
		path = path.substring(0, path.lastIndexOf('/')); // truncate file name
		path = path.substring(0, path.lastIndexOf('/'));//truncate "/classes"
		path = path.substring(0, path.lastIndexOf('/'));//truncate "/build"		
		path = path + "/test/book1/index";
		return  path;
	}

	public static String getTestRootPath() {
		URL url = ClassLoader.getSystemResource(CLASS_ROOT_FILE);
		String path = url.getPath();
		path = path.substring(0, path.lastIndexOf('/')); // truncate file name
		path = path.substring(0, path.lastIndexOf('/'));//truncate "/classes"
		path = path.substring(0, path.lastIndexOf('/'));//truncate "/build"		
		path = path + "/test/";
		return  path;
	}
	
}
