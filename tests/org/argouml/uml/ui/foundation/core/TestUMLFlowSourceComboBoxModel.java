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

import javax.swing.DefaultComboBoxModel;

import org.argouml.uml.ui.MockUMLUserInterfaceContainer;
import org.argouml.uml.ui.UMLComboBoxModel2;

import ru.novosoft.uml.MFactoryImpl;
import ru.novosoft.uml.foundation.core.MClassImpl;
import ru.novosoft.uml.foundation.core.MFlow;
import ru.novosoft.uml.foundation.core.MFlowImpl;
import ru.novosoft.uml.foundation.core.MModelElement;

import junit.framework.TestCase;

/**
 * @since Oct 12, 2002
 * @author jaap.branderhorst@xs4all.nl
 */
public class TestUMLFlowSourceComboBoxModel extends TestCase {
    
    private MFlow elem = null;
    private UMLComboBoxModel2 model = null;
    
    /**
     * Constructor for TestUMLFlowSourceComboBoxModel.
     * @param arg0
     */
    public TestUMLFlowSourceComboBoxModel(String arg0) {
        super(arg0);
    }
    
    /**
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        MFactoryImpl.setEventPolicy(MFactoryImpl.EVENT_POLICY_IMMEDIATE);
        elem = new MFlowImpl();
        MockUMLUserInterfaceContainer mockcomp = new MockUMLUserInterfaceContainer();
        mockcomp.setTarget(elem);
        model = new UMLFlowSourceComboBoxModel(mockcomp);
        elem.addMElementListener(model);
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
        MModelElement m = new MClassImpl();
        elem.addSource(m);
        assert(model.getSize() == 1);
        assert(model.getElementAt(0) == m);
    }
    
    public void testElementRemoved() {
        MModelElement m = new MClassImpl();
        elem.addSource(m);
        assert(model.getSize() == 1);
        assertEquals(model.getElementAt(0), m);
        assertEquals(model.getSelectedItem(), m);
        elem.removeSource(m);
        assert(model.getSize() == 0);
        assertNull(model.getSelectedItem());
    }
    
    public void testNoElements() {
        assertNull(model.getElementAt(0));
        assert(model.getSize() == 0);
        assert(elem.getSources().isEmpty());
    }
    
    public void testSelectNull() {
        assertNull(model.getSelectedItem());
    }
    
    public void testSelectWithMultiList() {
        MModelElement[] m = new MModelElement[10];
        for (int i = 0; i < m.length; i++) {
            m[i] = new MClassImpl();
            model.addElement(m[i]);
        }
        model.setSelectedItem(m[5]); // 'userclick'
        assertEquals(model.getSelectedItem(), m[5]);
    }

}
