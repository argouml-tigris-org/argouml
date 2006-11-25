// $Id$
// Copyright (c) 1996-2006 The Regents of the University of California. All
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

package org.argouml.uml.ui.foundation.core;

import org.argouml.model.Model;
import org.argouml.uml.ui.AbstractUMLModelElementListModel2Test;

/**
 * @since Oct 30, 2002
 * @author jaap.branderhorst@xs4all.nl
 */
public class TestUMLModelElementSupplierDependencyListModel
    extends AbstractUMLModelElementListModel2Test {

    /**
     * The number of elements used in the tests.
     */
    private static final int NO_OF_ELEMENTS = 10;

    /**
     * The object that the dependency is going from. A class.
     */
    private Object from;

    /**
     * The object that the dependency is going to. A class.
     */
    private Object to;

    /**
     * The namespace of the elements.
     */
    private Object namespace;

    /**
     * Constructor for TestUMLModelElementSupplierDependencyListModel.
     *
     * @param arg0 is the name of the test case.
     */
    public TestUMLModelElementSupplierDependencyListModel(String arg0) {
        super(arg0);
    }

    /*
     * @see junit.framework.TestCase#setUp()
     */
    public void setUp() throws Exception {
        super.setUp();

        namespace = Model.getModelManagementFactory().createPackage();
        from = Model.getCoreFactory().buildClass("from", namespace);
        to = Model.getCoreFactory().buildClass("to", namespace);
    }

    /*
     * @see junit.framework.TestCase#tearDown()
     */
    public void tearDown() throws Exception {
        Model.getUmlFactory().delete(from);
        Model.getUmlFactory().delete(to);
        Model.getUmlFactory().delete(namespace);

        super.tearDown();
    }

    /*
     * @see org.argouml.uml.ui.AbstractUMLModelElementListModel2Test#buildElement()
     */
    protected void buildElement() {
        setElem(Model.getCoreFactory().createClass());
    }

    /*
     * @see org.argouml.uml.ui.AbstractUMLModelElementListModel2Test#buildModel()
     */
    protected void buildModel() {
        setModel(new UMLModelElementSupplierDependencyListModel());
    }

    /*
     * @see org.argouml.uml.ui.AbstractUMLModelElementListModel2Test#fillModel()
     */
    protected Object[] fillModel() {
        Object[] ext = new Object[NO_OF_ELEMENTS];
        for (int i = 0; i < NO_OF_ELEMENTS; i++) {
            ext[i] = Model.getCoreFactory().buildDependency(from, to);
            Model.getCoreHelper().addSupplierDependency(getElem(), ext[i]);
        }
        return ext;
    }

    /*
     * @see org.argouml.uml.ui.AbstractUMLModelElementListModel2Test#removeHalfModel(Object[])
     */
    protected void removeHalfModel(Object[] elements) {
        for (int i = 0; i < NO_OF_ELEMENTS / 2; i++) {
            Model.getCoreHelper().removeSupplierDependency(
                    getElem(),
                    elements[i]);
        }
    }

}
