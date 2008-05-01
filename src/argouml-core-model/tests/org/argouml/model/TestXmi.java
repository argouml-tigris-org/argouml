// $Id$
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

import org.argouml.model.Model;
import org.argouml.model.UmlException;
import org.argouml.model.XmiReader;
import org.argouml.model.XmiWriter;
import org.xml.sax.InputSource;

/**
 * Basic XMI read/write tests which are independent from the rest of the
 * ArgoUML project machinery.
 * 
 * @author Tom Morris <tfmorris@gmail.com>
 */
public class TestXmi extends TestCase {

    private static final String MODEL_NAME = "TestXmi-model";
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        InitializeModel.initializeDefault();
    }

    /**
     * Test the XMI reader.
     * 
     * @throws IOException If we couldn't open the file
     * @throws UmlException if something else goes wrong during the read
     */
    public void testXmiReader() throws UmlException, IOException {
        XmiReader xmiReader = Model.getXmiReader();
        
        String[] ignoredElements = new String[] {"foo"};
        xmiReader.setIgnoredElements(ignoredElements);
        assertEquals("Ignored elements array not correct length", 1, 
                xmiReader.getIgnoredElements().length);
        assertEquals("Ignored elements count not zero", 0, 
                xmiReader.getIgnoredElementCount());
        
        // Write out something for us to read back in
        File xmiFile = File.createTempFile("TestXmiRead", "xmi");
        writeFile(xmiFile);
        
        InputSource inputSource = new InputSource(new FileInputStream(xmiFile));
        Collection elements = xmiReader.parse(inputSource, false);
        
        assertEquals("Wrong number of elements read", 1, elements.size());
        Object model = elements.iterator().next();
        assertEquals("Wrong model name", MODEL_NAME, 
                Model.getFacade().getName(model));
        Model.getUmlFactory().delete(model);
    }
    
    /**
     * Test reading a UML 1.3 file to make sure it gets converted to UML
     * 1.4 properly.
     * 
     * @throws IOException If we couldn't open the file
     * @throws UmlException if something else goes wrong during the read
     */
    public void testUml13Reader() throws UmlException, IOException {

        String filename = "/testmodels/uml13/argo-Alittlebitofeverything.xmi";
        URL url = getClass().getResource(filename);
        assertTrue("Unintended failure: resource to be tested is not found: "
                + filename + ", converted to URL: " + url, url != null);
        String fName = url.getFile();
        
        XmiReader xmiReader = Model.getXmiReader();
        InputSource inputSource = new InputSource(new FileInputStream(fName));
        Collection elements = xmiReader.parse(inputSource, false);
        
        assertEquals("Wrong number of top level elements read", 1, 
                elements.size());
        Object model = elements.iterator().next();
        assertEquals("Wrong model name", "testing", 
                Model.getFacade().getName(model));
        Model.getUmlFactory().delete(model);
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
        OutputStream stream = new FileOutputStream(outputFile);
        XmiWriter xmiWriter = Model.getXmiWriter(model, stream, "1.4");
        xmiWriter.write();
        Model.getUmlFactory().delete(model);
    }
}
