// Copyright (c) 2002 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies.  This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason.  IN NO EVENT SHALL THE
// UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY PARTY FOR DIRECT, INDIRECT,
// SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES, INCLUDING LOST PROFITS,
// ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF
// THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE POSSIBILITY OF
// SUCH DAMAGE. THE UNIVERSITY OF CALIFORNIA SPECIFICALLY DISCLAIMS ANY
// WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
// MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE SOFTWARE
// PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF
// CALIFORNIA HAS NO OBLIGATIONS TO PROVIDE MAINTENANCE, SUPPORT,
// UPDATES, ENHANCEMENTS, OR MODIFICATIONS.

package org.argouml.model.uml;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import junit.framework.*;

import org.xml.sax.SAXException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.NamedNodeMap;

public class OptionalTestAgainstUmlModel extends TestCase {
    public OptionalTestAgainstUmlModel(String n) { super(n); }

    public void testDataModel()
    throws SAXException,
           IOException,
	   ParserConfigurationException {
	DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
	Document doc = builder.parse(new File("testmodels/01-12-02.xml"));
	NodeList list = doc.getElementsByTagName("Model:Class");

	// Found this by experimentation.  Just temporary.
	assertEquals(110, list.getLength());

	for (int i = 0; i < list.getLength(); i++) {
	    processNode (list.item(i));
	}
    }

    static void processNode(Node node) {
        int c = node.getChildNodes().getLength();
        int a = node.getAttributes().getLength();
	System.out.println ("Node:" + node + 
			    " (children:" + c + "," +
			    " attributes:" + a + ")");
        // TODO: Find the namespaces of the classes.
	//       Use that to find the right package.
	//       Use reflection to execute the create for the class.
	//       Make sure all classes in the OMG model have
	//       creates for them.
    }

}
