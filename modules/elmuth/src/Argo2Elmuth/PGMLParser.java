/**
 * PGMLParser.java
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

import java.io.*;
import java.util.Hashtable;
import java.util.Vector;
import java.util.StringTokenizer;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.SAXParser;
import org.xml.sax.*;

/**
 * <code>PGMLParser</code> parses a PGML to collect information about a diagram.
 */
public class PGMLParser implements DocumentHandler  {
	private InputStream PGMLDoc;
	private Hashtable groups = new Hashtable();
	private Vector hrefs = new Vector();
	private String diagramType = new String();
	private String diagramName = new String();

   /**
	* Constructor.
	* @param <code>PGMLDoc</code> The input PGML document
	*/
	public PGMLParser(InputStream PGMLDoc) {
		this.PGMLDoc = PGMLDoc;
	}

   /**
	* Parses the PGML document
	*/	
	public void parse() {
		try {
			PGMLDoc = util.deleteDTD(PGMLDoc);
			Parser parser;
			SAXParserFactory factory = SAXParserFactory.newInstance();
			factory.setNamespaceAware(false);
			factory.setValidating(false);
			SAXParser sp = factory.newSAXParser();
			parser = sp.getParser();
			parser.setDocumentHandler(this);
			parser.parse(new InputSource(PGMLDoc));
		} catch (Exception e) {
			System.out.println("Exception during PGML parsing: " + e.getMessage());
		}
	}

   /**
	* Returns PGML groups in the PGML document.
	* @return <code>groups</code>: PGML groups in the PGML document.
	*/		
	public Hashtable getGroups() {
		return groups;
	}

   /**
	* Returns hrefs attributes in the PGML document.
	* @return <code>hrefs</code>: PGML hrefs attributes in the PGML document.
	*/	
	public Vector getHrefs() {
		return hrefs;
	}
 	
   /**
	* Returns diagram type (e.g.: "UMLClassDiagram")
	* @return <code>diagramType</code>: Diagram type 
	*/	
	public String getDiagramType() {
		return diagramType;
	}

   /**
	* Returns diagram name (e.g.: "class diagrma 1")
	* @return <code>diagramName</code>: Diagram name 
	*/	
	public String getDiagramName() {
		return diagramName;
	}
	
	public void setDocumentLocator (Locator l) { }
	public void startDocument () { }
	public void endDocument () { }
	public void startElement (String tag, AttributeList attrs) {
		if (tag.equals("pgml")) {
			for (int i = 0; i < attrs.getLength (); i++)
				if (attrs.getName(i).equals("description")) {
					String href = getDiagramInfo(attrs.getValue(i));
					//hrefs.addElement(href);
				}
				else if (attrs.getName(i).equals("name")) {
					diagramName = attrs.getValue(i);
				}
		}
		if (tag.equals("group")) {
			Hashtable attributes = new Hashtable();
			String groupId = null;
			for (int i = 0; i < attrs.getLength (); i++) {
				if (attrs.getName(i).equals("href"))
					hrefs.addElement(attrs.getValue(i));
				if (attrs.getName(i).equals("name"))
					groupId = attrs.getValue(i);
				else if (attrs.getName(i).equals("href") || attrs.getName(i).equals("description"))	
					attributes.put(attrs.getName(i),attrs.getValue(i));
			}
			groups.put(groupId,attributes);			
		}			
	}
	public void endElement (String name) { }
	public void characters (char buf [], int offset, int len) { }
	public void ignorableWhitespace (char buf [], int offset, int len) { }
	public void processingInstruction (String target, String data) { }
	
	private String getDiagramInfo(String description) {
		StringTokenizer st = new StringTokenizer(description,"|");
		String type = st.nextToken();
		diagramType = type.substring(type.lastIndexOf(".")+1);
		return st.nextToken();
	}
}