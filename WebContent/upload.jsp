<!-- upload.jsp -->
<%@ page import="java.io.*" %>

<%
String contentType = request.getContentType();
System.out.println("Content type is :: " +contentType);
if ((contentType != null) && (contentType.indexOf("multipart/form-data") >= 0)) {
DataInputStream in = new DataInputStream(request.getInputStream());
int formDataLength = request.getContentLength();

byte dataBytes[] = new byte[formDataLength];
int byteRead = 0;
int totalBytesRead = 0;
while (totalBytesRead < formDataLength) {
	byteRead = in.read(dataBytes, totalBytesRead, formDataLength);
	totalBytesRead += byteRead;
}

String file = new String(dataBytes);
String saveFile = file.substring(file.indexOf("filename=\"") + 10);
saveFile = saveFile.substring(0, saveFile.indexOf("\n"));
saveFile = saveFile.substring(saveFile.lastIndexOf("\\") + 1,saveFile.indexOf("\""));

//out.print(dataBytes);

int lastIndex = contentType.lastIndexOf("=");
String boundary = contentType.substring(lastIndex + 1,contentType.length());
//out.println(boundary);
int pos;
pos = file.indexOf("filename=\"");

pos = file.indexOf("\n", pos) + 1;

pos = file.indexOf("\n", pos) + 1;

pos = file.indexOf("\n", pos) + 1;


int boundaryLocation = file.indexOf(boundary, pos) - 4;
int startPos = ((file.substring(0, pos)).getBytes()).length;
int endPos = ((file.substring(0, boundaryLocation)).getBytes()).length;

FileOutputStream fileOut = new FileOutputStream(saveFile);


//fileOut.write(dataBytes);
fileOut.write(dataBytes, startPos, (endPos - startPos));
fileOut.flush();
fileOut.close();

out.println("File saved as " +saveFile);

}
%>