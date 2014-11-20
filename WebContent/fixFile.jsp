<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" import="java.io.*"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<% 
//Some of the audio files start with capital S and the other sarts with small letter s. 
//While in the data file all are small letters. This page rename all files to be lowercase
//typically changing the charecter s.  
//
//

try {

String path = application.getRealPath("books2/quran/telawa/");
//path += "s1/S1a0.rm";
out.println("visiting path:" + path);
String str = visitAllFiles(new File(path));

//String str = processFile(new File(path));
out.println(str);

} catch(Exception exp) {
	out.println(exp.getMessage());
}
 
 %>
<%!

// Fix file names

    
    // Process only files under dir
    public static String visitAllFiles(File dir) {
        String str = "";
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i=0; i<children.length; i++) {
                visitAllFiles(new File(dir, children[i]));
            }
        } else { //file
            str += "processing file: " + dir + "<br>";
            str += processFile(dir);
        }
        return str;
    }
    
public static String processFile(File file) {
	//check to see if capital and rename to small letters
	String path = file.getPath();
	StringBuffer strb = new StringBuffer(path);
	int index = strb.lastIndexOf("/");
	strb.replace(index+1, index+2 , "s"); //put small letter
	String str = "rename from[" + path + "] to path [" + strb.toString() + "]";
	
	boolean ok = file.renameTo(new File(strb.toString()));
	return str + "; status = " + ok; 
}
public static boolean isFileExist(String file) {
	boolean fileExist = true;
		try {
			FileInputStream fis = new FileInputStream( file);
			fis.close();
		} catch (Exception exp) {
			fileExist = false;
		}
		return fileExist;
}

 %>


</body>
</html>