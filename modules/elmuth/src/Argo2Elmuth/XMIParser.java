/**
 * XMIParser.java
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
import java.util.Enumeration;
import java.util.Vector;
import java.util.StringTokenizer;

/**
 * Utility class for managing a XMI document.
 */
public class XMIParser {
	private InputStream XMIinput;
	private Vector hrefs;
	private PrintWriter XMIoutput;

   /**
	* Constructor.
	* @param <code>XMIinput</code> The input XMI document
	* @param <code>XMIoutput</code> The <code>OutputStream</code> where writing updates to the XMI document.
	*/
	public XMIParser(InputStream XMIinput,OutputStream XMIoutput) {
		this.XMIinput = XMIinput;
		this.XMIoutput = new PrintWriter(XMIoutput);
	}
	
   /**
	* Add to the XMI document the tag "<code>XMI.uuidrefs</code>".
	* This tag has an attribute named <code>values</code> which specify
	* all the <code>UUIDs</code> to be used during the generation of a left-side menu.
	* @param <code>hrefs</code> Uuids to be added as value of the attribute <code>values</code>
	*/	
	public void addHrefs(Vector hrefs) throws IOException {
		String values = ",";
		for (Enumeration enum=hrefs.elements(); enum.hasMoreElements();)
			values = values + (String)enum.nextElement() + ",";
		ByteArrayOutputStream OutDoc = new ByteArrayOutputStream();
		util.copyInputStream(XMIinput,OutDoc);
		String XMIDoc = OutDoc.toString();
		StringTokenizer st = new StringTokenizer(XMIDoc,"\r\n");
		String token;
		boolean found = false;
		while (st.hasMoreTokens()) {
			token = st.nextToken();
			if (!found  &&  token.trim().equals("<XMI.header>")) {
				XMIoutput.println("  <XMI.uuidrefs values=\""+values+"\"/>");
				found = true;
			}
			XMIoutput.println(token);
		}
		if (XMIoutput.checkError())
			throw new IOException("java.io.PrintWriter error");
		XMIoutput.close();	
	}
	
}