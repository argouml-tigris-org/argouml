// $Id$
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

package org.argouml.uml.ui.foundation.core;

import junit.framework.TestCase;

import org.argouml.application.security.ArgoSecurityManager;
import org.argouml.model.uml.UmlFactory;
import org.argouml.model.uml.foundation.core.CoreFactory;
import org.argouml.model.uml.modelmanagement.ModelManagementFactory;
import org.argouml.uml.ui.MockUMLUserInterfaceContainer;
import javax.swing.text.BadLocationException;

import ru.novosoft.uml.MFactoryImpl;
import ru.novosoft.uml.foundation.core.MModelElement;
import ru.novosoft.uml.model_management.MModel;

/**
 * @since Oct 26, 2002
 * @author jaap.branderhorst@xs4all.nl
 */
public class TestUMLModelElementNameDocument extends TestCase {
    
    private MModelElement elem = null;
    private int oldEventPolicy;
    private UMLModelElementNameDocument model;
    private MModel ns;

    /**
     * Constructor for TestUMLModelElementNameDocument.
     * @param arg0
     */
    public TestUMLModelElementNameDocument(String arg0) {
        super(arg0);
    }

    /**
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        ArgoSecurityManager.getInstance().setAllowExit(true);
        UmlFactory.getFactory().setGuiEnabled(false);
        ns = ModelManagementFactory.getFactory().createModel();
        elem = CoreFactory.getFactory().buildClass(ns);
        oldEventPolicy = MFactoryImpl.getEventPolicy();
        MFactoryImpl.setEventPolicy(MFactoryImpl.EVENT_POLICY_IMMEDIATE);
        MockUMLUserInterfaceContainer cont = new MockUMLUserInterfaceContainer();
        //cont.setTarget(elem);
        model = new UMLModelElementNameDocument();
        model.setTarget(elem);
    }

    /**
     * @see junit.framework.TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
        UmlFactory.getFactory().delete(ns);
        UmlFactory.getFactory().delete(elem);
        elem = null;
        ns = null;
        MFactoryImpl.setEventPolicy(oldEventPolicy);
        model = null;
    }
    
    public void testSetName()
	throws BadLocationException
    {
        elem.setName("test");
	assertEquals("test", model.getText(0, model.getLength()));
    }
    
    public void testRemoveName()
	throws BadLocationException
    {
        elem.setName("test");
        elem.setName(null);
	assertEquals("", model.getText(0, model.getLength()));
    }
    
    public void testInsertString()
	throws BadLocationException
    {
        elem.setName("");
	model.insertString(0, "test", null);
        assertEquals("test", elem.getName());
    }
    
    public void testRemoveString() 
	throws BadLocationException
    {
	model.insertString(0, "test", null);
	model.remove(0, model.getLength());
        assertEquals("", elem.getName());
    }
    
    public void testAppendString()
	throws BadLocationException
    {
        elem.setName("test");
	model.insertString(model.getLength(), "test", null);
        assertEquals("testtest", elem.getName());
    }
    
    public void testInsertStringHalfway()
	throws BadLocationException
    {
        elem.setName("test");
	model.insertString(1, "test", null);
        assertEquals("ttestest", elem.getName());
    }
    
    public void testRemoveStringHalfway()
	throws BadLocationException
    {
        elem.setName("test");
	model.remove(1, model.getLength()-2);
        assertEquals("tt", elem.getName());
    }
}
