/* $Id$
 *****************************************************************************
 * Copyright (c) 2009 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    tfmorris
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

// Copyright (c) 2003-2007 The Regents of the University of California. All
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

package org.argouml.model;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Test against the UML model.
 */
public class TestAgainstUmlModel extends TestCase {

    private static Set<String> dontTest = new HashSet<String>();

    private static Hashtable<String, String> remap =
            new Hashtable<String, String>();

    private static boolean uml2 = false;
    
    /**
     * The constructor.
     *
     * @param n the name
     */
    public TestAgainstUmlModel(String n) {
        super(n);
    }

    /**
     * @throws SAXException when things go wrong with SAX
     * @throws IOException when there's an IO error
     * @throws ParserConfigurationException when the parser finds wrong syntax
     * 
     * TODO: Unused?
     */
    public void testDataModel()
	throws SAXException,
	       IOException,
	       ParserConfigurationException {
	Document doc = prepareDocument();
	if (doc == null) {
	    return;		// Could not find model.
	}

	List<String> classNames = getMetaclassNames(doc);
	for (String className : classNames ) {
	    processClass (className);
	}
    }

   /*
    * Get a list of UML metaclass names from the XMI document.
    * <p>
    * Though some of the DOM methods such as getAttributes
    * may return null values under other conditions,
    * in the context of this test
    * and assuming a valid XMI file
    * none should occur.
    * <p>
    * Hence there is no special checking for those abnormal
    * cases, allowing the test to fail simply with a
    * NullPointerException, with this comment indicating that
    * either the input data is incorrect or the test needs
    * to be improved.
    */
    private static List<String> getMetaclassNames(Document doc) {
        List<String> result = new ArrayList<String>();
        int abstractCount = 0;
        if (uml2) {
            // The UML 2.1.1 metamodel is a MOF 2.0 CMOF model, so
            // we want nodes with
            // tag="ownedMember", attribute xmi:type="cmof:Class"
            // TODO: This code is untested - tfm
            NodeList list = doc.getElementsByTagName("ownedMember");
            for (int i = 0; i < list.getLength(); i++) {
                Node node = list.item(i);
                Node typeAttr = node.getAttributes().getNamedItem("xmi:type");
                if ("cmof:Class".equals(typeAttr.getNodeValue())) {
                    if (!isAbstract(node)) {
                        result.add(getNames(node));
                    } else {
                        abstractCount++;
                    }
                }
            }
        } else {
            // Handle UML 1.4 metamodel which is a MOF 1.3 model
            NodeList list = doc.getElementsByTagName("Model:Class");
            for (int i = 0; i < list.getLength(); i++) {
                Node node = list.item(i);
                if (!isAbstract(node)) {
                    result.add(getNames(node));
                } else {
                    abstractCount++;
                }
            }
        }
        System.out.println("Skipping " + abstractCount + " abstract elements");
        return result;
    }

    /*
     * Get a node's name along with the name of its parent (which we'll use to
     * find the factory to create it with.
     */
    private static String getNames(Node node) {
        // TODO: Do we want the top level package here instead of the immediate
        // parent?
        
        // Because UML 1.4 & 2.1 metamodels are organized differently we need
        // to traverse the hierarchy looking for our owning Package.
        Node pkg = node;
        while (pkg != null && !isPackage(pkg)) {
            pkg = pkg.getParentNode();
        }
        if (pkg == null) {
            return getName(node);
        } 
        return  getName(pkg) + ":" + getName(node);
    }

    private static boolean isPackage(Node node) {
        if ("Model:Package".equals(node.getNodeName())) {
            // UML 1.4
            return true;
        } else {
            Node type = node.getAttributes().getNamedItem("xmi:type");
            if (type != null && "cmof:Package".equals(type.getNodeValue())) {
                // UML 2.x
                return true;
            }
        }
        return false;
    }
    
    private static String getName(Node node) {
        return node.getAttributes().getNamedItem("name").getNodeValue();
    }
    
    private static boolean isAbstract(Node node) {
        Node isAbstract = node.getAttributes().getNamedItem("isAbstract");
        return isAbstract != null && "true".equals(isAbstract.getNodeValue());
    }


    /**
     * Print a message that this test case is inconclusive because of
     * the UML file missing.
     *
     * @param message that is to me printed.
     */
    private static void printInconclusiveMessage(String message) {
	System.out.println(TestAgainstUmlModel.class.getName()
                + ": WARNING: INCONCLUSIVE TEST!");
	System.out.println(message);
	System.out.println("You will have to fetch the model using the command"
			   + " ant junit-get-model");
    }

    /**
     * Make all preparations for the tests by preparing the document.
     *
     * @return the document or null if not available.
     * @throws SAXException when things go wrong with SAX
     * @throws IOException when there's an IO error
     * @throws ParserConfigurationException when the parser finds wrong syntax
     */
    private static Document prepareDocument()
	throws ParserConfigurationException, SAXException, IOException {
	DocumentBuilder builder =
	    DocumentBuilderFactory.newInstance().newDocumentBuilder();
	String fileName = System.getProperty("test.model.uml");
	if (fileName == null) {
	    printInconclusiveMessage("The property test.model.uml "
				     + "is not set.");
	    return null;
	}
	File file = new File(fileName);
	if (!file.exists()) {
	    printInconclusiveMessage("The file " + fileName
				     + " cannot be found.");
	    return null;
	}
        Document document = builder.parse(file);

        Element root = document.getDocumentElement();
        NamedNodeMap attributes = root.getAttributes();
        Node versionNode = attributes.getNamedItem("xmi:version"); // XMI 2.1
        if (versionNode == null) {
            versionNode = attributes.getNamedItem("xmi.version"); // XMI 1.1
        }
        String version = versionNode.getNodeValue();
        
        // This is the XMI version used to encode the metamodel.  We could
        // parse deeper to pull out the actual UML version.  The UML 1.4
        // version is at XMI/XMI.header/XMI.model[@xmi.version].  The UML 2.1.1
        // metamodel doesn't actually seem to contain its version.
        if ("1.1".equals(version)) {
            uml2 = false;
        } else if ("2.1".equals(version)) {
            uml2 = true;
        } else {
            System.out.println("Unknown metamodel type");
        }
        
	return document;
    }


    /**
     * Walk through the UML Classes found.

     */
    private void processClass(String className) {

        // Remap specific classes
        String name;
        if (remap.containsKey(className)) {
            name = remap.get(className);
        } else {
            name = className;
        }
        
        String[] pieces = name.split(":");
        String umlclass = pieces[1];
        
        String pkgName = pieces[0].replaceAll("_", "");
        // Only remap package if we didn't remap specific class
        if (className.equals(name) && remap.containsKey(pkgName)) {
            pkgName = remap.get(pkgName);
        }
        
        String getter = "get" + pkgName + "Factory";
        try {
            Method getMethod = Model.class.getMethod(getter, new Class[0]);
            Object factory = getMethod.invoke(null, new Object[0]);
            if (!(factory instanceof Factory)) {
                fail("Factory for " + name
                        + "isn't an instanceof Model.Factory");
            }
            if ( dontTest.contains(umlclass) || dontTest.contains(name)) {
                System.out.println("Skipping " + name);
                return;
            }
            String[] classarg = {umlclass, null};
            CheckUMLModelHelper.createAndRelease(factory, classarg);
        } catch (Exception e) {
            fail("Failed to get factory for " + name + " - " + e);
        }


    }

    /**
     * @return the test suite
     */
    public static Test suite() {
        TestSuite suite =
	    new TestSuite("Tests for "
			  + TestAgainstUmlModel.class.getPackage().getName());

	Document doc = null;
	try {
	    doc = prepareDocument();
	} catch (ParserConfigurationException e) {
	    e.printStackTrace();
	} catch (SAXException e) {
	    e.printStackTrace();
	} catch (IOException e) {
	    e.printStackTrace();
	}
	if (doc == null) {
	    return suite;	// Could not find model.
	}

        for (String metaclassName : getMetaclassNames(doc)) {
            suite.addTest(new TestAgainstUmlModel(metaclassName));            
        }

        return suite;
    }

    protected void runTest() throws Throwable {
        processClass(getName());
    }

    

    /**
     * Initialize the lookup map to link the uml class names
     * to the factories.
     *
     * This brute force method should be investigated
     * in favor of determining the Uml Class namespace from
     * the UML metamodel and computing the factory
     * at run time.
     *
     * Certain classes that cannot be tested directly in this way
     * should be calculated.  Event and StateVertex, for example,
     * are marked abstract in the model.  But we need to make sure
     * that the reverse is true, that there are no elements
     * marked abstract in the model that in fact are instantiable
     * by the model subsystem.
     */

    static {
        InitializeModel.initializeDefault();

        /*
         * The following UML 1.4 elements have been removed from UML 2.x, so we
         * don't bother testing them.
         */
        dontTest.add("Primitive");
        dontTest.add("ProgrammingLanguageDataType");
        dontTest.add("UseCaseInstance");
        
        dontTest.add("ActivityGraph");

        dontTest.add("ActionExpression");
        dontTest.add("ArgListsExpression");
        dontTest.add("BooleanExpression");
        dontTest.add("IterationExpression");
        dontTest.add("MappingExpression");
        dontTest.add("ObjectSetExpression");
        dontTest.add("ProcedureExpression");
        dontTest.add("TimeExpression");
        dontTest.add("TypeExpression");
        
        // TODO: We'd like to test this in its new guise as PackageImport, but
        // we don't have a good way to do it currently
        dontTest.add("Permission");

        /*
         * A few of our factories are slightly different than as declared in the
         * UML 1.4 metamodel, so we remap them here.  <metamodel, argouml>
         */
        remap.put("Core:Stereotype", "ExtensionMechanisms:Stereotype");
        remap.put("Core:TaggedValue", "ExtensionMechanisms:TaggedValue");
        remap.put("Core:TagDefinition", "ExtensionMechanisms:TagDefinition");

        /*
         * The UML 2.x package structure is *entirely* different, so we have to
         * remap a bunch of stuff. Names without embedded colons (:) indicate
         * that the entire package is remapped. e.g. Kernel->Core As a matter of
         * fact the only package which did NOT get renamed or moved is UseCases.
         * 
         * TODO: This section is very incomplete. - tfm
         */
        // Specific classes to be remapped
//      remap.put("", "");
        remap.put("Kernel:Expression", "DataTypes:Expression");
        
        // Packages to be remapped
        remap.put("Kernel", "Core");
        remap.put("Interfaces", "Core");
        remap.put("Dependencies", "Core");
        remap.put("Nodes", "Core");
        remap.put("SimpleTime", "Core");
        remap.put("AssociationClasses", "Core");
        remap.put("Communications", "StateMachines");
        remap.put("BehaviorStateMachines", "StateMachines");
        remap.put("ProtocolStateMachines", "StateMachines");
        remap.put("Models", "ModelManagement");
        
        /*
         * For those things which we've already migrated to UML 2.x syntax
         * we need to map them back to their UML 1.4 equivalents during the
         * migration period.
         */
        // TODO: Except this won't work because the names are different which
        // will cause a test in CheckUMLModelHelper to fail
//        remap.put("Core:Permission", "Core:PackageImport");
    }



}



