/**
 * XSLTransformation.java
 * @author  Paolo Pinciani (pinciani@cs.unibo.it)
 *
 * Copyright 2001-2002 Department of Computer Science
 * University of Bologna
 * Mura Anteo Zamboni 7, 40127 Bologna, ITALY
 * Tel: +39 051 35.45.16
 * Fax: +39 051 35.45.10
 * Web: http://cs.unibo.it
**/
  
package Argo2Elmuth;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import org.xml.sax.SAXException;
import org.apache.xalan.xslt.XSLTProcessorFactory;
import org.apache.xalan.xslt.XSLTProcessor;
import org.apache.xalan.xslt.XSLTInputSource;
import org.apache.xalan.xslt.XSLTResultTarget;
import org.apache.xalan.xpath.xml.ProblemListener;

/**
 * <code>XSLTransformation</code> class performs a XSLT trasformation. It is based on Apache Xalan-Java1.
 */
public class XSLTransformation implements ProblemListener {
	private InputStream XMLdoc;
	private InputStream XSLdoc;

	/**
	 * Constructor.
	 * @param <code>XMLdoc</code> The XML document to which the XSLT trasformation is applied
 	 * @param <code>XMLdoc</code> The XSL stylesheet
	 */
	public XSLTransformation(InputStream XMLdoc, InputStream XSLdoc) {
		this.XMLdoc = XMLdoc;
		this.XSLdoc = XSLdoc;
	}

	/**
	 * Performs a XSLT trasformation
	 * @param <code>os</code> The <code>OutputStream</code> where to write the resulting XML document.
	 */	
	public void doXSLProcessing(OutputStream os) throws SAXException {
		XSLTProcessor proc = null;
		PrintWriter out = null;
		try {
			out = new PrintWriter(os);
			proc = XSLTProcessorFactory.getProcessor();
			//proc.setQuietConflictWarnings(true);
			proc.setProblemListener(this);
			proc.process(new XSLTInputSource(XMLdoc),new XSLTInputSource(XSLdoc),new XSLTResultTarget(out));
						
		} catch (SAXException ex) {
			throw new SAXException("error applayng stylesheets:\n" + ex.getMessage());
		}
		finally {
			if (proc != null)
				proc.reset();
			out.close();	
		}	
	}
	
	public boolean problem(	short where,short classification,Object styleNode,org.w3c.dom.Node sourceNode,String msg,String id,int lineNo,int charOffset) {
		if (classification != ProblemListener.WARNING)
            return false;
		if (msg.startsWith("Illegal attribute name:"))
			return false;	
		System.err.println("XSLT Error: " + msg + ". Line " + lineNo + ", column " + charOffset);	
		return true;
	}
	public boolean message(java.lang.String msg) { return false; }
}	