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

import javax.swing.JComboBox;

import org.argouml.model.uml.foundation.core.CoreFactory;
import org.argouml.uml.ui.MockUMLUserInterfaceContainer;
import org.argouml.uml.ui.UMLComboBox2;

import ru.novosoft.uml.MFactoryImpl;
import ru.novosoft.uml.foundation.core.MClass;
import ru.novosoft.uml.foundation.core.MClassImpl;
import ru.novosoft.uml.foundation.core.MFlow;
import ru.novosoft.uml.foundation.core.MFlowImpl;
import ru.novosoft.uml.model_management.MModelImpl;

import junit.framework.TestCase;

/**
 * @since Oct 12, 2002
 * @author jaap.branderhorst@xs4all.nl
 */
public class TestUMLFlowSourceComboBox extends TestCase {
    
    private MFlow elem = null;
    private UMLComboBox2 box = null;

    /**
     * Constructor for TestUMLFlowSourceComboBox.
     * @param arg0
     */
    public TestUMLFlowSourceComboBox(String arg0) {
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
        box = new UMLComboBox2(mockcomp, new UMLFlowSourceComboBoxModel(mockcomp), ActionSetFlowSource.SINGLETON);
        elem.addMElementListener(box);
    }

    /**
     * @see junit.framework.TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
        elem.remove();
        elem = null;
        box = null;
    }
    
    public void testSetSelectedNull() {
        elem.addSource(new MClassImpl());
        box.setSelectedItem(null);
        assert(elem.getSources().isEmpty());
    }
    
    // this test does not work yet since the event mechanisme in argo needs to 
    // be refactored.
    public void testSetSelected() {
        MClass clazz = CoreFactory.getFactory().buildClass(new MModelImpl());
        box.setSelectedItem(clazz);
        assert(elem.getSources().contains(clazz));
    }
        

}
