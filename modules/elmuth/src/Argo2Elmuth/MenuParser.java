package Argo2Elmuth;

import java.io.*;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.SAXParser;
import org.xml.sax.*;

public class MenuParser implements DocumentHandler  {
	private InputStream menuDoc;
	private int nodesNumber = 0;

	public MenuParser(InputStream menuDoc) {
		this.menuDoc = menuDoc;
	}
	
	public void parse() {
		try {
			Parser parser;
			SAXParserFactory factory = SAXParserFactory.newInstance();
			factory.setNamespaceAware(false);
			factory.setValidating(false);
			SAXParser sp = factory.newSAXParser();
			parser = sp.getParser();
			parser.setDocumentHandler(this);
			parser.parse(new InputSource(menuDoc));
		} catch (Exception e) {
			System.out.println("Exception during XML parsing: " + e.getMessage());
		}
	}
	
	public int getNodesNumber() {
		return nodesNumber;
	}
	
	public void setDocumentLocator (Locator l) { }
	public void startDocument () { }
	public void endDocument () { }
	public void startElement (String tag, AttributeList attrs) {
		if (tag.equals("menu.Node"))
			nodesNumber++;			
	}
	public void endElement (String name) { }
	public void characters (char buf [], int offset, int len) { }
	public void ignorableWhitespace (char buf [], int offset, int len) { }
	public void processingInstruction (String target, String data) { }
}