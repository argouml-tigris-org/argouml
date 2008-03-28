// $Id$
// Copyright (c) 2005-2007 The Regents of the University of California. All
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

package org.argouml.model.mdr;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Collection;

import junit.framework.TestCase;

import org.argouml.model.UmlException;
import org.argouml.model.XmiReader;
import org.omg.uml.foundation.core.UmlClass;
import org.omg.uml.modelmanagement.Model;
import org.omg.uml.modelmanagement.UmlPackage;
import org.xml.sax.InputSource;

/**
 * Testing the set up of the MDR.
 */
public class TestMDRModelImplementationCreate extends TestCase {

    /**
     * Constructor for TestMDRModelImplementation.
     * 
     * @param arg0
     *            Test case name.
     */
    public TestMDRModelImplementationCreate(String arg0) {
        super(arg0);
    }

    /**
     * Tests the constructor.
     * 
     * @throws UmlException
     *             if model subsystem initialization fails.
     * @throws FileNotFoundException
     *             If the test XMI file can't be found.
     */
    public void testMDRModelImplementation() throws UmlException,
            FileNotFoundException {
        System.setProperty("argouml.model.implementation",
                "org.argouml.model.mdr.MDRModelImplementation");
        MDRModelImplementation mi = new MDRModelImplementation();
        assertNotNull(mi.getFacade());
        Model m = (Model) mi.getModelManagementFactory().createModel();
        assertNotNull(m);
        UmlPackage p = (UmlPackage) mi.getModelManagementFactory().
                createPackage();
        mi.getCoreHelper().setNamespace(p, m);
        UmlClass c = (UmlClass) mi.getCoreFactory().buildClass(m);
        Model m1 = (Model) mi.getFacade().getModel(c);
        assertNotNull(m1);
        Model m2 = (Model) mi.getFacade().getModel(p);
        assertNotNull(m2);
        assertEquals(m1, m);
        assertEquals(m2, m);
        XmiReader xmiReader = mi.getXmiReader();
        String model = "tests/testmodels/test.xmi"; //!"ALittleBit.xmi";
        File fileModel = new File(model);
        assertTrue(fileModel.exists());
        InputSource source = new InputSource(new FileInputStream(fileModel));
        //Model aLittleBit = (Model) xmiReader.parse(source);
        Collection modelElements = xmiReader.parse(source, false);
        assertNotNull(modelElements);
        assertEquals(1, modelElements.size());
    }

}
