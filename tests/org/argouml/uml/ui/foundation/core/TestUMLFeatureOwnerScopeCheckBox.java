// $Id$
// Copyright (c) 1996-2005 The Regents of the University of California. All
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

import org.argouml.model.Model;

import ru.novosoft.uml.foundation.data_types.MScopeKind;

/**
 * @since Nov 6, 2002
 * @author jaap.branderhorst@xs4all.nl
 */
public class TestUMLFeatureOwnerScopeCheckBox extends TestCase {

    private UMLFeatureOwnerScopeCheckBox box = null;
    private Object elem = null;

    /**
     * Constructor for TestUMLFeatureOwnerScopeCheckBox.
     * @param arg0 is the name of the test case.
     */
    public TestUMLFeatureOwnerScopeCheckBox(String arg0) {
        super(arg0);
    }

    /**
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        elem = Model.getCoreFactory().createAttribute();

	box = new UMLFeatureOwnerScopeCheckBox();
        box.setTarget(elem);
    }

    /**
     * @see junit.framework.TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
        Model.getUmlFactory().delete(elem);
        elem = null;
        box = null;
    }

    /**
     * Tests the marking/clicking of the checkbox. Simulates the behaviour when
     * the users selects the checkbox. Tests if the ownerscope of the element
     * is really changed
     */
    public void testDoClick() {
        Object spec = Model.getFacade().getOwnerScope(elem);
	if (box == null) {
	    return; // Inconclusive
	}
        box.doClick();
        assertEquals(
                MScopeKind.CLASSIFIER,
                Model.getFacade().getOwnerScope(elem));
    }

    /**
     * Tests wether a change in the NSUML modelelement is reflected in the
     * checkbox.
     */
    public void testPropertySet() {
	if (box == null) {
	    return; // Inconclusive
	}
        boolean selected = box.isSelected();
        if (selected) {
            Model.getCoreHelper().setOwnerScope(elem, MScopeKind.INSTANCE);
        } else {
            Model.getCoreHelper().setOwnerScope(elem, MScopeKind.CLASSIFIER);
        }
        assertEquals(!selected, box.isSelected());
    }


}
