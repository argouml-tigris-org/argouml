/**
 * HTMLGenerator.java
 * @author  Paolo Pinciani (pinciani@cs.unibo.it)
 *
 * Copyright 2001-2002 Department of Computer Science
 * University of Bologna
 * Mura Anteo Zamboni 7, 40127 Bologna, ITALY
 * Tel: +39 051 35.45.16
 * Fax: +39 051 35.45.10
 * Web: http://cs.unibo.it
 */
package Argo2Elmuth;

import java.awt.Rectangle;
import java.awt.Rectangle;
import java.util.Hashtable;
import java.util.Vector;
import java.util.Enumeration;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipFile;

/**
 * <code>HTMLGenerator</code> generates dynamic HTML pages of Elmuth.
 */
public class HTMLGenerator {
	private ZipFile jarFile = null;
	private int menuHeight = 0;
	private int diagramHeight = 0;
	private int diagramWidth = 0;

   /**
	* Constructor.
	*/
	public HTMLGenerator() { }
	
	/**
	 * Opens Argo2Elmuth main jar archive.
	 */
	public void openJar() throws IOException {
		jarFile = new ZipFile(System.getProperty("JAR_FILE_NAME"));
	}
	
	/**
	 * Closes Argo2Elmuth main jar archive.
	 */
	public void closeJar() throws IOException {
		jarFile.close();
	}
	
	public void computeHeights(Hashtable diagramsInfo) {
		for (Enumeration en=diagramsInfo.keys(); en.hasMoreElements();) {
			Integer key = (Integer)en.nextElement();
			DiagramInfo info = (DiagramInfo)diagramsInfo.get(key);
			
			//menu max height:
			int menuNodesHeight = 20*(info.getNodesNumber()+1)+40;
			if (menuNodesHeight > menuHeight)
				menuHeight = menuNodesHeight;
					
			//diagrams max size:
			Rectangle diagramSize = info.getSize();
			if (diagramSize.width > diagramWidth)
				diagramWidth = diagramSize.width;
			if (diagramSize.height > diagramHeight)
				diagramHeight = diagramSize.height;
		}
	}
	
   /**
	* Generates the HTML source for "index.html"
	* @return The generated HTML file
	*/
	public String makeIndex() throws IOException {
		template t = getTemplate("index.html");
		return t.getContent();
	}
	
   /**
	* Generates the HTML source for an Elmuth menu.
	* @return The generated HTML file
	*/
	public String makeMenu() throws IOException {
		template t = getTemplate("menu.html");
		Hashtable vals = new Hashtable();
		vals.put("height", new String(""+menuHeight));
		vals.put("xml", "xml/menu0.xml");
		t.cast(vals);
		return t.getContent();
	}

   /**
	* Generates the HTML source for an Elmuth diagram (upper right frame).
	* @return The generated HTML file
	*/		
	public String makeModel() throws IOException {
		template t = getTemplate("diagram.html");
		Hashtable vals = new Hashtable();
		vals.put("width", new String(""+diagramWidth));
		vals.put("height", new String(""+diagramHeight));
		vals.put("xml", "xml/diagram0.xml");
		t.cast(vals);
		
		return t.getContent();
	}

   /**
	* Generates the HTML source for an Elmuth html description (bottom right frame).
    * @param <code>n</code> The index of the HTML file (e.g.: html0: the index is 0))
	* @return The generated HTML file
	*/		
	public String makeHtml(int n) throws IOException {
		template t = getTemplate("html.html");
		
		Hashtable vals = new Hashtable();
		vals.put("name", "html"+n+".html");
		t.cast(vals);
		
		return t.getContent();
	}

   /**
	* Generates the HTML source for "dictionary.html"
	* @return The generated HTML file
	*/	
	public String makeDictionary() throws IOException {
		template t = getTemplate("dictionary.html");
		return t.getContent();
	}
	
	/**
	* Generates the HTML source for "code.html"
	* @return The generated HTML file
	*/	
	public String makeCode() throws IOException {
		template t = getTemplate("code.html");
		return t.getContent();
	}
	
	private template getTemplate(String name) throws IOException {
		template t = null;
		try {
			InputStream html = util.findZipEntry(jarFile,"elmuth_files/templates/"+name);
			if (html == null) throw new IOException("Can't find \"templates/"+name+"\" in " + System.getProperty("JAR_FILE_NAME"));
			t = new template(html);
		}
		catch (Exception e) {
			throw new IOException("Error processing HTML template: " + e.getMessage());
		}
		return t;
	}
	
}