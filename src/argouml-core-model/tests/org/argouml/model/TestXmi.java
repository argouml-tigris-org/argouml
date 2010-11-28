/* $Id$
 *******************************************************************************
 * Copyright (c) 2009 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    tfmorris
 *******************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

// Copyright (c) 2008 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies. This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason. IN NO EVENT SHALL THE
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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.Collection;

import junit.framework.TestCase;

import org.xml.sax.InputSource;

/**
 * Basic XMI read/write tests which are independent from the rest of the
 * ArgoUML project machinery.
 * 
 * @author Tom Morris <tfmorris@gmail.com>
 */
public class TestXmi extends TestCase {

    private static final String MODEL_NAME = "TestXmi-model";
    private XmiReader xmiReader;
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        InitializeModel.initializeDefault();
        xmiReader = Model.getXmiReader();
    }

    /**
     * Test the set and get of IgnoredElements.
     */
    public void testIgnoredElements() {
        assertEquals("Ignored elements array not correct length", 0, 
                xmiReader.getIgnoredElements().length);

        xmiReader.setIgnoredElements(new String[] {"foo"});
        assertEquals("Ignored elements array not correct length", 1, 
                xmiReader.getIgnoredElements().length);

        xmiReader.setIgnoredElements(new String[] {});
        assertEquals("Ignored elements array not correct length", 0, 
                xmiReader.getIgnoredElements().length);

        xmiReader.setIgnoredElements(new String[] {"foo", "bar"});
        assertEquals("Ignored elements array not correct length", 2, 
                xmiReader.getIgnoredElements().length);

        xmiReader.setIgnoredElements(null);
        assertEquals("Ignored elements array not correct length", 0, 
                xmiReader.getIgnoredElements().length);
    }
    
    
    /**
     * Test the XMI reader.
     * 
     * @throws IOException If we couldn't open the file
     * @throws UmlException if something else goes wrong during the read
     */
    public void testXmiReader() throws UmlException, IOException {

        assertEquals("Wrong tag", "XMI", xmiReader.getTagName());
        String[] ignoredElements = new String[] {"foo"};
        xmiReader.setIgnoredElements(ignoredElements);

        assertEquals("Ignored elements count not zero", 0, 
                xmiReader.getIgnoredElementCount());
        
        // Write out something for us to read back in
        File xmiFile = File.createTempFile("TestXmiRead", "xmi");
        writeFile(xmiFile);
        
        // Now read it and check the results.
        InputSource inputSource = new InputSource(new FileInputStream(xmiFile));
        Collection elements = xmiReader.parse(inputSource, false);
        assertEquals("Wrong number of top level elements", 1, elements.size());
        Object model = elements.iterator().next();
        assertEquals("Wrong model name", MODEL_NAME, 
                Model.getFacade().getName(model));
        assertTrue("Didn't find ArgoUML in the header", 
                xmiReader.getHeader().contains("ArgoUML"));
        assertEquals("Wrong number of total elements in ID map", 2, 
                xmiReader.getXMIUUIDToObjectMap().size());
        Model.getUmlFactory().delete(model);
        
        // Create an empty file and try to read it
        File emptyFile = File.createTempFile("TestXmiRead", "xmi");
        try {
            elements =
                    xmiReader.parse(new InputSource(new FileInputStream(
                            emptyFile)), false);
            fail("Reading empty file failed to throw an exception");
        } catch (UmlException e) {
            assertTrue("Unexpected exception type", e instanceof XmiException);
            XmiException xe = (XmiException) e;

            // Depends on implementation: 
            if (xe.getLineNumber() == -1) {
                // The netbeans SAXParser sets line number
                // and column number to -1.
                assertEquals("Unexpected line number", -1, xe.getLineNumber());
                assertEquals("Unexpected column number", 
                             -1, xe.getColumnNumber());
            } else {
                // The xerces SAXParser sets line number
                // and column number to 1.
                assertEquals("Unexpected line number", 1, xe.getLineNumber());
                assertEquals("Unexpected column number", 
                             1, xe.getColumnNumber());                
            }
        }
    }
    
    /**
     * Test reading a UML 1.3 file to make sure it gets converted to UML
     * 1.4 properly.
     * 
     * @throws IOException If we couldn't open the file
     * @throws UmlException if something else goes wrong during the read
     */
    public void testUml13Reader() throws UmlException, IOException {
        readAndCheckXmi("/testmodels/uml13/argo-Alittlebitofeverything.xmi",
                "testing", 214);
    }
    
    private void readAndCheckXmi(String xmiFile, String packageName,
            int numElements) throws UmlException, FileNotFoundException {
        Collection elements = readXmi(xmiFile);
        checkModel(packageName, numElements, elements);
        Model.getUmlHelper().deleteCollection(elements);
    }
    
    private Collection readXmi(String filename) throws UmlException,
            FileNotFoundException {
        URL url = getClass().getResource(filename);
        assertTrue("Unintended failure: resource to be tested is not found: "
                + filename + ", converted to URL: " + url, url != null);
        String fName = url.getFile();

        InputSource inputSource = new InputSource(new FileInputStream(fName));
        return xmiReader.parse(inputSource, false);
    }

    private void checkModel(String packageName, int numElements,
            Collection elements) {
        // Not true in general, but should be true for our test models
        assertEquals("Wrong number of top level elements read", 1, elements
                .size());
        Object pkg = elements.iterator().next();
        assertEquals("Wrong model name", packageName, Model.getFacade()
                .getName(pkg));
        assertEquals("Wrong number of total elements in ID map", numElements,
                xmiReader.getXMIUUIDToObjectMap().size());
    }
    
    public void notestUml13Large() {
        // TODO: Get file from bug report
        // http://argouml.tigris.org/issues/show_bug.cgi?id=4188
        // that fails with no more DTM ID message
    }
    
    /**
     * The simplest possible XMI writer test.
     * 
     * @throws IOException If we couldn't open the file
     * @throws UmlException if something else goes wrong during the read
     */
    public void testXmiWriter() throws IOException, UmlException {
        File xmiFile = File.createTempFile("TestXmi", "xmi");
        writeFile(xmiFile);
    }

    private void writeFile(File outputFile) throws IOException,
        FileNotFoundException, UmlException {
        
        Object model = Model.getModelManagementFactory().createModel();
        Model.getCoreHelper().setName(model, MODEL_NAME);
        Model.getCoreFactory().buildClass("TestXmiClass", model);
        OutputStream stream = new FileOutputStream(outputFile);
        XmiWriter xmiWriter = Model.getXmiWriter(model, stream, "1.4");
        xmiWriter.write();
        Model.getUmlFactory().delete(model);
    }
    
    public void notestXmiVisio() {
        // TODO: 
    }
    
    /**
     * Test a UML 1.3/XMI 1.1 file to make sure that it gets converted properly.
     * <p>
     * TODO: Our UML 1.3 converter currently only handles XMI 1.0. 
     * @throws FileNotFoundException indicates a test configuration problem
     * @throws UmlException if it fails to open the file
     */
    public void notestXmiRoseUml13() throws FileNotFoundException, 
            UmlException {
        // http://argouml.tigris.org/issues/show_bug.cgi?id=4919
        readAndCheckXmi("/testmodels/uml13/issue4919-Rose-uml13-xmi11.xmi",
                "AAA_5.1.1_K2", 7);
        // This throws 
        // org.argouml.model.XmiException: Unknown element in XMI file : Model
        // because the UML 1.3->1.4 converter can only handle XMI 1.0, not 1.1
    }

    public void notestXmiRoseUml14() {
        // TODO:
    }

    /**
     * Test weird hybrid Poseidon file to make sure that it throws a reasonable
     * exception when we try to load it.
     * 
     * @throws FileNotFoundException indicates a test configuration problem
     */
    public void notestPoseidonHybrid() throws FileNotFoundException {
        // http://argouml.tigris.org/issues/show_bug.cgi?id=4440
        try {
            readAndCheckXmi("/testmodels/uml14/issue4440-Poseidon-hybrid.xmi",
                    "model 2", 99);
        } catch (UmlException e) {
            if (!(e instanceof XmiException) 
                    || !e.getMessage().contains("Unknown element")) {
                fail("Expected exception not thrown");
            }
        }
    }
    
 
}
