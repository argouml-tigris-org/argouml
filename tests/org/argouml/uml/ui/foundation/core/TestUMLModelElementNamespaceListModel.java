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

import org.argouml.model.uml.UmlFactory;
import org.argouml.model.uml.behavioralelements.collaborations.CollaborationsFactory;
import org.argouml.model.uml.foundation.core.CoreFactory;
import org.argouml.uml.ui.MockUMLUserInterfaceContainer;
import org.argouml.uml.ui.behavior.collaborations.UMLMessageSenderListModel;
import ru.novosoft.uml.MFactoryImpl;
import ru.novosoft.uml.behavior.collaborations.MClassifierRole;
import ru.novosoft.uml.foundation.core.MModelElement;
import ru.novosoft.uml.foundation.core.MNamespace;

/**
 * @since Oct 30, 2002
 * @author jaap.branderhorst@xs4all.nl
 */
public class TestUMLModelElementNamespaceListModel extends TestCase {

    private int oldEventPolicy;
    private UMLModelElementNamespaceListModel model;
    private MModelElement elem;
    
    /**
     * Constructor for TestUMLModelElementNamespaceListModel.
     */
    public TestUMLModelElementNamespaceListModel(String arg0) {
        super(arg0);
    }
   
    /**
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        elem = CoreFactory.getFactory().createClass();
        oldEventPolicy = MFactoryImpl.getEventPolicy();
        MFactoryImpl.setEventPolicy(MFactoryImpl.EVENT_POLICY_IMMEDIATE);
        MockUMLUserInterfaceContainer cont = new MockUMLUserInterfaceContainer();
        cont.setTarget(elem);
        model = new UMLModelElementNamespaceListModel(cont);
        elem.addMElementListener(model);
    }
    
    /**
     * @see junit.framework.TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
        UmlFactory.getFactory().delete(elem);
        MFactoryImpl.setEventPolicy(oldEventPolicy);
        model = null;
    }
    
    public void testSetNamespace() {
        MNamespace ns = CoreFactory.getFactory().createNamespace();
        elem.setNamespace(ns);
        assertEquals(1, model.getSize());
        assertEquals(ns, model.getElementAt(0));
    }
    
    public void testRemoveNamespace() {
        MNamespace ns = CoreFactory.getFactory().createNamespace();
        elem.setNamespace(ns);
        elem.setNamespace(null);
        assertEquals(0, model.getSize());
        assertTrue(model.isEmpty());
    } 
}
