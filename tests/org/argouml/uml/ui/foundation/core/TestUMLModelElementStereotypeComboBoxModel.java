// Copyright (c) 1996-99 The Regents of the University of California. All
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

// $header$
package org.argouml.uml.ui.foundation.core;

import junit.framework.TestCase;

import ru.novosoft.uml.MFactoryImpl;
import ru.novosoft.uml.foundation.core.MClassImpl;
import ru.novosoft.uml.foundation.core.MModelElement;
import ru.novosoft.uml.foundation.extension_mechanisms.MStereotype;
import ru.novosoft.uml.foundation.extension_mechanisms.MStereotypeImpl;

/**
 * @since Oct 13, 2002
 * @author jaap.branderhorst@xs4all.nl
 */
public class TestUMLModelElementStereotypeComboBoxModel extends TestCase {

    private MModelElement elem;
    private UMLModelElementStereotypeComboBoxModel model;
    
    /**
     * Constructor for TestUMLModelElementStereotypeComboBoxModel.
     * @param arg0
     */
    public TestUMLModelElementStereotypeComboBoxModel(String arg0) {
        super(arg0);
    }
    
     /**
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        MFactoryImpl.setEventPolicy(MFactoryImpl.EVENT_POLICY_IMMEDIATE);
        elem = new MClassImpl();
        model = new UMLModelElementStereotypeComboBoxModel();
        model.targetChanged(elem);
    }
    
    /**
     * @see junit.framework.TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
        elem.remove();
        elem = null;
        model = null;
    }
    
    public void testElementAdded() {
        int oldSize = model.getSize();
        MStereotype m = new MStereotypeImpl();
        m.setBaseClass("Class");
        elem.setStereotype(m);
        // assertEquals(model.getSize()-1, oldSize);
        assertEquals(m, model.getSelectedItem());
    }
    
    public void testElementRemoved() {
        MStereotype m = new MStereotypeImpl();
        m.setBaseClass("Class");
        elem.setStereotype(m);
        assertEquals(m, model.getSelectedItem());
        m.remove();
        m = null;
        assertNull(elem.getStereotype());
        assertNull(model.getSelectedItem());
    }
    
    public void testNoElements() {
        assertEquals(model.getElementAt(0), null);
        assertEquals(model.getSize(), 0);
        assertNull(model.getSelectedItem());
    }
    
    public void testSetStereotypeNull() {
        MStereotype m = new MStereotypeImpl();
        m.setBaseClass("Class");
        elem.setStereotype(m);
        assertEquals(m, model.getSelectedItem());
        elem.setStereotype(null);
        assertNull(model.getSelectedItem());
    }

}
