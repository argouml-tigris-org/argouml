/**
 * XMLGenerator.java
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
import java.util.*;
import java.util.zip.*;
import java.awt.Rectangle;

import org.tigris.gef.base.*;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.util.Util;

import org.argouml.ui.ProjectBrowser;
import org.argouml.ui.ArgoDiagram;
import org.argouml.kernel.Project;

import ru.novosoft.uml.model_management.MPackage;
import ru.novosoft.uml.model_management.MModel;
import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.behavior.use_cases.*;
import ru.novosoft.uml.behavior.common_behavior.*;
import ru.novosoft.uml.foundation.data_types.MAggregationKind;
import ru.novosoft.uml.xmi.XMIWriter;
import org.tigris.gef.ocl.OCLExpander;
import org.tigris.gef.ocl.TemplateReader;

import org.xml.sax.SAXException;


/**
 * <code>XMLGenerator</code> is the class which generates XML
 * documents used by Elmuth
 */
public class XMLGenerator {
	private ZipFile jarFile = null;
	private ProjectBrowser browser;
	private Project project;
	private Vector PGMLDocuments = new Vector();
	
	/**
	 * Constructor.
	 */
	public XMLGenerator() {
		browser = ProjectBrowser.TheInstance;
		project =  browser.getProject();
	}
	
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

   /**
    * Creates the XML document for Data Dictionary.
    * @param <code>out</code> The Output stream where to write out the XML document. 
    */	
	public void makeDictionary(OutputStream out)
		throws	IOException,
				SAXException,
				ru.novosoft.uml.xmi.IncompleteXMIException {
		InputStream xsl = util.findZipEntry(jarFile,"elmuth_files/xsl/dictionary.xsl");
		if (xsl == null) throw new IOException("Can't find \"elmuth_files/xsl/dictionary.xsl\" in " + System.getProperty("JAR_FILE_NAME"));
					
		XSLTransformation xslt = new XSLTransformation(getXMIModel(),xsl);
		xslt.doXSLProcessing(out);
	}
	
   /**
    * Creates the XML document for Code Generator.
    * @param <code>out</code> The Output stream where to write out the XML document. 
    */	
	public void makeCode(OutputStream out)
		throws	IOException,
				SAXException,
				ru.novosoft.uml.xmi.IncompleteXMIException {
		InputStream xsl = util.findZipEntry(jarFile,"elmuth_files/xsl/code.xsl");
		if (xsl == null) throw new IOException("Can't find \"elmuth_files/xsl/code.xsl\" in " + System.getProperty("JAR_FILE_NAME"));
					
		XSLTransformation xslt = new XSLTransformation(getXMIModel(),xsl);
		xslt.doXSLProcessing(out);
	}
	
   /**
    * Creates the XML documents for left side menus and diagrams.
    * The number of XML documents for menus and diagrams equals the
	* number of ArgoUML diagrams in the project. XML documents are saved into files
	* named as: menu0.xml,...,menuN.xml and diagram0.xml,...,diagramN.xml; where N is
	* the number of ArgoUML diagrams in the project. these files are stored in the
	* subdirectory named "xml".
	* @return a <code>Hashtable</code> containing information about diagrams:
	* the key is an <code>Integer</code> from 0 to N.
	* the value is a <code>DiagramInfo</code> class.
    */		
	public Hashtable makeMenuAndSVG() throws Exception {
		Hashtable diagramsInfo = new Hashtable();		
		Vector diagrams = project.getDiagrams();
		Enumeration enum = diagrams.elements();
		for (int i=0; i<diagrams.size(); i++) {
			Object target = diagrams.elementAt(i);
			browser.setTarget(target);
			if( target instanceof Diagram ) {
				try {
					DiagramInfo diagramInfo = new DiagramInfo();
					Editor ce = Globals.curEditor();
					Diagram diagram = (Diagram)target;
							
					//pgml doc:
					InputStream pgml = getPGMLDocument((ArgoDiagram)diagram);
					PGMLParser pgmlParser = new PGMLParser(pgml);
					pgmlParser.parse();
					diagramInfo.setName(pgmlParser.getDiagramName());
					diagramInfo.setType(pgmlParser.getDiagramType());
					
					//menu:
					makeMenu("menu"+i,pgmlParser.getHrefs());
					FileInputStream MenuXMLDoc = new FileInputStream(System.getProperty("projectFullPath")+System.getProperty("file.separator")+"xml"+System.getProperty("file.separator")+"menu"+i+".xml");
					MenuParser mp = new MenuParser(MenuXMLDoc);
					mp.parse();
					MenuXMLDoc.close();
					diagramInfo.setNodesNumber(mp.getNodesNumber());
					
					//svg:
					Rectangle diagramSize = makeSVG(ce,diagram,"diagram"+i+".xml");
					
					//diagram info:
					diagramInfo.setSize(diagramSize);
					diagramsInfo.put(new Integer(i),diagramInfo);
				} catch (Exception e) {
					throw new Exception("Error writing :" + e.toString());
				}
			}
		}
		return diagramsInfo;
	}

   /**
	* Add Xlinks to menu 
    * @param <code>menuIndex</code> The index of this menu (e.g.: 0 for menu0.html)
	* @param <code>diagramsInfo</code> a <code>Hashtable</code> containing information about diagrams (name, type, size)
	*/	
	public void addXlinks(int menuIndex, Hashtable diagramsInfo) throws IOException {
		FileInputStream XMLInput = new FileInputStream(System.getProperty("projectFullPath")+System.getProperty("file.separator")+"xml"+System.getProperty("file.separator")+"menu"+menuIndex+".xml");
		ByteArrayOutputStream OutDoc = new ByteArrayOutputStream();
		util.copyInputStream(XMLInput,OutDoc);
		String XMLDocument = OutDoc.toString();
		
		String beforePath = "<menu.XMI xmi.version=\"1.0\">";
		int indexBefore = XMLDocument.indexOf(beforePath);
		String before = XMLDocument.substring(0,indexBefore+beforePath.length());
		String after = XMLDocument.substring(indexBefore+beforePath.length());
		
		String xlink = "";
		xlink += "<xlink.XLink type=\"extended\">\r\n";
		for (int i=0; i<diagramsInfo.size(); i++) {
			DiagramInfo info = (DiagramInfo)diagramsInfo.get(new Integer(i));
			xlink += "\t<xlink.Locator label=\""+info.getName()+"\" href=\"html"+i+".html\" target=\"html\"/>\r\n";
			xlink += "\t<xlink.Locator label=\""+info.getName()+"\" href=\"java:menu.display('xml/menu"+i+".xml')\"/>\r\n";
			xlink += "\t<xlink.Locator label=\""+info.getName()+"\" href=\"java:model.display('xml/diagram"+i+".xml')\"/>\r\n";
		}
		xlink += "\tProject Diagrams\r\n";
		xlink += "</xlink.XLink>\r\n";
		xlink += "<xlink.XLink type=\"extended\">\r\n";
		xlink += "\t<xlink.Locator label=\"Data Dictionary\" href=\"dictionary.html\" target=\"dictionary\"/>\r\n";
		xlink += "\t<xlink.Locator label=\"Code Generator\" href=\"code.html\" target=\"html\"/>\r\n";
		xlink += "\t<xlink.Locator label=\"Diagram Description\" href=\"html"+menuIndex+".html\" target=\"html\"/>\r\n";
		xlink += "\tSemantic\r\n";
		xlink += "</xlink.XLink>\r\n";
		
		String XMLDoc = before + "\r\n" + xlink + after;
		XMLInput.close();
		OutDoc.close();
		
		File toDelete = new File(System.getProperty("projectFullPath")+System.getProperty("file.separator")+"xml"+System.getProperty("file.separator")+"menu"+menuIndex+".xml");		
		toDelete.delete();
		util.copyString(XMLDoc,System.getProperty("projectFullPath")+System.getProperty("file.separator")+"xml"+System.getProperty("file.separator")+"menu"+menuIndex+".xml");

	}
	
   /**
    * Creates the XML document for left side menu.
    * @param <code>menuName</code> The file name of this menu
    * @param <code>hrefs</code> Uuids of the elements in the XMI document to be included in this menu
    */		
	private void makeMenu(String menuName,Vector hrefs)
		throws	IOException,
				SAXException,
				ru.novosoft.uml.xmi.IncompleteXMIException {
		InputStream xmi = getXMIModel();
		InputStream xsl = util.findZipEntry(jarFile,"elmuth_files/xsl/menu.xsl");
		if (xsl == null) throw new IOException("Can't find \"elmuth_files/xsl/menu.xsl\" in " + System.getProperty("JAR_FILE_NAME"));
		
		FileOutputStream MenuXMLDoc = new FileOutputStream(System.getProperty("projectFullPath")+System.getProperty("file.separator")+"xml"+System.getProperty("file.separator")+menuName+".xml");
		ByteArrayOutputStream XMIWriterOut = new ByteArrayOutputStream();
		XMIParser XmiParser = new XMIParser(xmi,XMIWriterOut);
		XmiParser.addHrefs(hrefs);
		xmi.close();
		InputStream xml = new ByteArrayInputStream(XMIWriterOut.toByteArray());	
		
		XSLTransformation xslt = new XSLTransformation(xml,xsl);
		xslt.doXSLProcessing(MenuXMLDoc);
	
		xml.close();
		MenuXMLDoc.close();
		xsl.close();
	}
	
   /**
    * Creates the XML document for diagrams.
    * @param <code>ce</code> Current ArgoUML Editor
    * @param <code>diagram</code> Current ArgoUML diagram
	* @param <code>diagram Name</code> Diagram file name
	* @return diagram size
    */
	private Rectangle makeSVG(Editor ce, Diagram diagram, String diagramName) throws Exception {
		//xsl doc:
		InputStream xsl = util.findZipEntry(jarFile,"elmuth_files/xsl/svg.xsl");
		if (xsl == null) throw new IOException("Can't find \"elmuth_files/xsl/svg.xsl\" in " + System.getProperty("JAR_FILE_NAME"));
	
		FileOutputStream SVGXMLDoc = new FileOutputStream(System.getProperty("projectFullPath")+System.getProperty("file.separator")+"xml"+System.getProperty("file.separator")+diagramName);
		ByteArrayOutputStream SVGWriterOut = new ByteArrayOutputStream();
		Rectangle diagramSize = computeSVGDimension(ce);
		DispletsSVGWriter svg = new DispletsSVGWriter(SVGWriterOut,diagramSize);
		for (Enumeration en = diagram.elements(); en.hasMoreElements();) {
			Fig fig = (Fig)en.nextElement();
			Object owner = fig.getOwner();
			MModelElement me = null;
			if (owner instanceof MModelElement) {
			    me = (MModelElement)owner;
				String uuid = me.getUUID();
				if (uuid == null) uuid = "";
				String name = me.getName();
				if (name == null) name = "";
				String type = getFigType(me);
				if (type == null) type = "";
				Hashtable groupAttributes = new Hashtable();
				groupAttributes.put("uuid",uuid);
				groupAttributes.put("type",type);
				groupAttributes.put("name",name);
				if (owner instanceof MAssociation) {
					MAssociation as = (MAssociation)owner;
					MAssociationEnd ae0 =  (MAssociationEnd)((Object[])(as.getConnections()).toArray())[0];
					MAssociationEnd ae1 =  (MAssociationEnd)((Object[])(as.getConnections()).toArray())[1];
					String ae0Kind = getAggregationKind(ae0.getAggregation());
					String ae1Kind = getAggregationKind(ae1.getAggregation());
					if (ae0Kind != null)
						groupAttributes.put("kind",ae0Kind);
					else if (ae1Kind != null)
						groupAttributes.put("kind",ae1Kind);						
					else 
						groupAttributes.put("kind",new String());
				}
				svg.createGroup(fig.getId(),groupAttributes);
				fig.paint(svg);
			}
			else {
				svg.createGroup(fig.getId(),new Hashtable());
				fig.paint(svg);
			}
		}		
		svg.dispose();
		
		InputStream xml = new ByteArrayInputStream(SVGWriterOut.toByteArray());
		SVGWriterOut.close();
		XSLTransformation xslt = new XSLTransformation(xml,xsl);
		xslt.doXSLProcessing(SVGXMLDoc);
					
		xml.close();
		SVGXMLDoc.close();
		xsl.close();
		return diagramSize;
	}
	
	private String getAggregationKind(MAggregationKind ak) {
		if (ak == null  ||  MAggregationKind.NONE.equals(ak))
			return null;
		else if (MAggregationKind.AGGREGATE.equals(ak))
			return "aggregate";
		else if (MAggregationKind.COMPOSITE.equals(ak))
	      return "composite";
		return null;  
	}
	
   /**
	* Gets the specific type of a generic ModelElement element (e.g. Class, Interface, etc.).
    * @param <code>owner</code> generic ModelElement element
	* @return The type of the element
	*/
	private String getFigType(MModelElement owner) {
		String type = null;
		//Package diagram:
		if (owner instanceof MPackage)
			type = "Package";
		//Class diagram:
		else if (owner instanceof MClass)
			type = "Class";
		else if (owner instanceof MInterface)
			type = "Interface";	
		else if (owner instanceof MAssociation)
			type = "Association";
		else if (owner instanceof MAbstraction)
			type = "Realization";	
		else if (owner instanceof MGeneralization)
			type = "Generalization";	
		else if (owner instanceof MComment)
			type = "Note";
		//Use Cases diagram:
		else if (owner instanceof MActor)
			type = "Actor";	
		else if (owner instanceof MUseCase)
			type = "UseCase";
		//Sequence diagram:	
		else if (owner instanceof MObject)
			type = "Object";
		else if (owner instanceof MLink)
			type = "Link";
		else if (owner instanceof MStimulus)
			type = "Stimulus";
		return type;	
	}
	
   /**
	* Computes the size of an ArgoUML diagram
    * @param <code>ce</code> Current ArgoUML Editor
	* @return The diagram size
	*/
	private Rectangle computeSVGDimension(Editor ce) {
		int xmin = 99999, ymin = 99999;
		Fig f = null;
		Rectangle rectSize = null;
		Rectangle drawingArea = new Rectangle( 0, 0 );
		Enumeration enum = ce.figs();
		while( enum.hasMoreElements() ) {
			f = (Fig) enum.nextElement();
			rectSize = f.getBounds();
			xmin = Math.min( xmin, rectSize.x );
			ymin = Math.min( ymin, rectSize.y );
			drawingArea.add( rectSize );
		}
		drawingArea.width -= xmin;
		drawingArea.height -= ymin;
		drawingArea.x = xmin;
		drawingArea.y = ymin;
		drawingArea.grow(50,80); // security border
		return drawingArea;
	}
	
   /**
	* Gets the XMI document for the current ArgoUML project.
	* @return The XMI document as an <code>InputStream</code>
	*/
	private InputStream getXMIModel() throws IOException, ru.novosoft.uml.xmi.IncompleteXMIException{
		MNamespace XMIModel = project.getModel();
		ByteArrayOutputStream XMIWriterOut = new ByteArrayOutputStream();
		PrintWriter writer = new PrintWriter(XMIWriterOut);
		XMIWriter xmiwriter = new XMIWriter((MModel)XMIModel,writer);
		xmiwriter.gen();
		writer.flush();
		InputStream xmi = new ByteArrayInputStream(XMIWriterOut.toByteArray());
		return xmi;
	}

   /**
	* Gets the PGML document for an ArgoUML diagram.
	* @return The PGML document as an <code>InputStream</code>
	*/	
	private InputStream getPGMLDocument(ArgoDiagram diagram) throws IOException {
		OCLExpander expander = new OCLExpander(TemplateReader.readFile(org.argouml.uml.diagram.ProjectMemberDiagram.PGML_TEE));
		ByteArrayOutputStream PGMLWriterOut = new ByteArrayOutputStream();
		PrintWriter writer = new PrintWriter(PGMLWriterOut);
		expander.expand(writer, diagram, "", "");
		writer.flush();
		return new ByteArrayInputStream(PGMLWriterOut.toByteArray());
	}
	
}