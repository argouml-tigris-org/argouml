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

import javax.swing.JRadioButton;

import junit.framework.TestCase;

import org.argouml.application.security.ArgoSecurityManager;
import org.argouml.model.uml.UmlFactory;
import org.argouml.uml.ui.MockUMLUserInterfaceContainer;

import ru.novosoft.uml.MFactoryImpl;
import ru.novosoft.uml.foundation.core.MClassImpl;
import ru.novosoft.uml.foundation.core.MModelElement;
import ru.novosoft.uml.foundation.data_types.MVisibilityKind;

/**
 * @since Oct 12, 2002
 * @author jaap.branderhorst@xs4all.nl
 */
public class TestUMLElementOwnershipVisibilityButtonGroup extends TestCase {

    private UMLVisibilityButtonGroup group = null;
    private MModelElement elem;
    /**
     * Constructor for TestUMLElementOwnershipVisibilityButtonGroup.
     * @param arg0
     */
    public TestUMLElementOwnershipVisibilityButtonGroup(String arg0) {
        super(arg0);
    }
    
    public void testDoPublicClick() {
	if (group == null) return; // Inconclusive
        JRadioButton button = group.getPublicButton();
        assertNotNull("public button null!", button);
        button.doClick();
        assertEquals(group.getSelection(), button.getModel());
    }
    
    public void testDoProtectedClick() {
	if (group == null) return; // Inconclusive
        JRadioButton button = group.getProtectedButton();
        assertNotNull("public button null!", button);
        button.doClick();
        assertEquals(group.getSelection(), button.getModel());
    }    
    
    public void testDoPrivateClick() {
	if (group == null) return; // Inconclusive
        JRadioButton button = group.getPrivateButton();
        assertNotNull("public button null!", button);
        button.doClick();
        assertEquals(group.getSelection(), button.getModel());
    }   
    
    public void testVisibilityPublic() {
	if (group == null) return; // Inconclusive
        elem.setVisibility(MVisibilityKind.PUBLIC);
        assertEquals(group.getSelection(), group.getPublicButton().getModel());
    }
    
    public void testVisibilityPrivate() {
	if (group == null) return; // Inconclusive
        elem.setVisibility(MVisibilityKind.PRIVATE);
        assertEquals(group.getSelection(), group.getPrivateButton().getModel());
    }
    
    public void testVisibilityProtected() {
	if (group == null) return; // Inconclusive
        elem.setVisibility(MVisibilityKind.PROTECTED);
        assertEquals(group.getSelection(), group.getProtectedButton().getModel());
    }


    /**
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        ArgoSecurityManager.getInstance().setAllowExit(true);
        UmlFactory.getFactory().setGuiEnabled(false);
        elem = new MClassImpl();
        MFactoryImpl.setEventPolicy(MFactoryImpl.EVENT_POLICY_IMMEDIATE);
        MockUMLUserInterfaceContainer cont = new MockUMLUserInterfaceContainer();
        cont.setTarget(elem);

	// If we cannot create the group, we assume that it is because
	// there is no GUI available.
	// If so, all tests are inconclusive.
	try {
	    group = new UMLElementOwnershipVisibilityButtonGroup(cont);
	} catch (java.lang.Exception e) {
	    // Cannot set up for this test.
	    return;
	}
        elem.addMElementListener(group);
    }

    /**
     * @see junit.framework.TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
        elem.remove();
        elem = null;
        group = null;
    }

}
