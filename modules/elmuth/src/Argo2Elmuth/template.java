package Argo2Elmuth;

import java.io.*;
import java.util.*;
import org.apache.xerces.utils.regex.*;

/**
 * a <code>tempate</code> management class
 */
public class template {
	private String content;
	private String output = "";

	public template(String filename) throws java.io.IOException {
		content = readFileAsString(filename);
	}
	
	public template(InputStream htmlInput) throws java.io.IOException {
		content = readInputStreamAsString(htmlInput);
	}

	public void cast(Hashtable values) {
		for (Enumeration e = values.keys(); e.hasMoreElements();) {
			String key = (String)e.nextElement();
			String v = (String)values.get(key);
			content = replaceAll(content,"<!--cgi:\\s*"+key+"-->",v);
		}
	}

	public void castLoop(String name, Hashtable values) {
		String result = "";
		RegularExpression re = new RegularExpression("<!--\\s*loop:\\s*"+name+"\\s*-->(.*)<!--\\s*end:\\s*"+name+"\\s*-->","s");
		Match match = new Match();
		if (re.matches(content, match)) {
			for (int i=1; i<match.getNumberOfGroups(); i++) {
				String pattern = match.getCapturedText(i);
				for (Enumeration e = values.keys(); e.hasMoreElements();) {
					String key = (String)e.nextElement();
					String v = (String)values.get(key);
					pattern = replaceAll(pattern,"<!--item:\\s*"+key+"-->",v);
				}
				output += pattern;
				output = replaceAll(output,"<!--\\s*item:.*-->",""); //to delete item not in the Hashtable
			}
		}
	}	
	
	
	public void finalizeLoop(String name) {
		content = replaceAll(content,"<!--\\s*loop:\\s*"+name+"\\s*-->(.*)<!--\\s*end:\\s*"+name+"\\s*-->",output);
	}
	
	public String getContent() {
		return content;
	}
	
	private String replaceAll(String content, String regex, String replacement) {
		String result = "";
		RegularExpression re = new RegularExpression(regex,"s");
		Match match = new Match();
		while (re.matches(content, match)) {
			result += content.substring(0,match.getBeginning(0)) + replacement;
			content = content.substring(match.getBeginning(0)+match.getCapturedText(0).length());
		}
		result += content;
		return result;
	}
	
	private String readFileAsString(String path) throws IOException {
		File file = new File(path);
		int length = (int)file.length();
		byte[] content = new byte[length];
		FileInputStream fis = new FileInputStream(file);
		fis.read(content);
		fis.close();
		String s = new String(content);
		return s;
	}
	
	private String readInputStreamAsString(InputStream input) throws IOException {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		util.copyInputStream(input, output);
		return output.toString();
	}
	
}