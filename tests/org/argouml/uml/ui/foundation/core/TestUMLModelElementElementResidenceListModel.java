// $Id$
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

package org.argouml.uml.ui.foundation.core;

import junit.framework.TestCase;

import org.argouml.application.security.ArgoSecurityManager;
import org.argouml.model.uml.UmlFactory;

import ru.novosoft.uml.MFactoryImpl;
import ru.novosoft.uml.foundation.core.MClassImpl;
import ru.novosoft.uml.foundation.core.MElementResidence;
import ru.novosoft.uml.foundation.core.MElementResidenceImpl;
import ru.novosoft.uml.foundation.core.MModelElement;

/**
 * @since Oct 12, 2002
 * @author jaap.branderhorst@xs4all.nl
 */
public class TestUMLModelElementElementResidenceListModel extends TestCase {

    private MModelElement elem;
    private UMLModelElementElementResidenceListModel list;
    
    /**
     * Constructor for TestUMLModelElementElementResidenceListModel.
     * @param arg0
     */
    public TestUMLModelElementElementResidenceListModel(String arg0) {
        super(arg0);
    }
    
    /**
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        ArgoSecurityManager.getInstance().setAllowExit(true);
        UmlFactory.getFactory().setGuiEnabled(false);
        MFactoryImpl.setEventPolicy(MFactoryImpl.EVENT_POLICY_IMMEDIATE);
        elem = new MClassImpl();     
        list = new UMLModelElementElementResidenceListModel();
        elem.addMElementListener(list);
        list.setTarget(elem);
    }
    
    /**
     * @see junit.framework.TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
        elem.remove();
        elem = null;
        list = null;
    }
    
    public void testElementAdded() {
        MElementResidence res = new MElementResidenceImpl();
        elem.addElementResidence(res);
        assertTrue(list.getSize() == 1);
        assertTrue(list.getElementAt(0) == res);
    }
    
    public void testElementRemoved() {
        MElementResidence res = new MElementResidenceImpl();
        elem.addElementResidence(res);
        assertTrue(list.getSize() == 1);
        assertTrue(list.getElementAt(0) == res);
        elem.removeElementResidence(res);
        assertTrue(list.getSize() == 0);
    }
    
    public void testNoElements() {
        try {
            list.getElementAt(0);
            fail();
        }
        catch (ArrayIndexOutOfBoundsException a) { }
        assertTrue(list.size() == 0);
        assertTrue(elem.getElementResidences().isEmpty());
    }
        


}
