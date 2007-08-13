// $Id:MockUMLUserInterfaceContainer.java 10736 2006-06-11 17:30:38Z mvw $
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

package org.argouml.uml.ui;

import java.util.Iterator;

import org.argouml.uml.Profile;

/**
 * @since Oct 12, 2002
 * @author jaap.branderhorst@xs4all.nl
 */
public class MockUMLUserInterfaceContainer
    implements UMLUserInterfaceContainer {

    private Object target;

    /**
     * Constructor for MockUMLUserInterfaceContainer.
     */
    public MockUMLUserInterfaceContainer() {
        super();
    }

    /**
     * @see org.argouml.uml.ui.UMLUserInterfaceContainer#getTarget()
     */
    public Object getTarget() {
        return target;
    }

    /**
     * @see org.argouml.uml.ui.UMLUserInterfaceContainer#getModelElement()
     */
    public Object getModelElement() {
        return null;
    }

    /**
     * @see org.argouml.uml.ui.UMLUserInterfaceContainer#getProfile()
     */
    public Profile getProfile() {
        return null;
    }

    /**
     * @see org.argouml.uml.ui.UMLUserInterfaceContainer#formatElement(java.lang.Object)
     */
    public String formatElement(/*ModelElement*/Object element) {
        return null;
    }

    /**
     * @see org.argouml.uml.ui.UMLUserInterfaceContainer#formatCollection(java.util.Iterator)
     */
    public String formatCollection(Iterator iter) {
        return null;
    }

    /**
     * @see org.argouml.uml.ui.UMLUserInterfaceContainer#formatNamespace(java.lang.Object)
     */
    public String formatNamespace(/*Namespace*/Object ns) {
        return null;
    }

    /**
     * @see org.argouml.ui.NavigationListener#navigateTo(java.lang.Object)
     */
    public void navigateTo(Object element) {
    }

    /**
     * @see org.argouml.ui.NavigationListener#open(java.lang.Object)
     */
    public void open(Object element) {
    }

    /**
     * @see org.argouml.ui.NavigationListener#navigateBack(boolean)
     */
    public boolean navigateBack(boolean attempt) {
        return false;
    }

    /**
     * @see org.argouml.ui.NavigationListener#navigateForward(boolean)
     */
    public boolean navigateForward(boolean attempt) {
        return false;
    }

    /**
     * @see org.argouml.ui.NavigationListener#isNavigateBackEnabled()
     */
    public boolean isNavigateBackEnabled() {
        return false;
    }

    /**
     * @see org.argouml.ui.NavigationListener#isNavigateForwardEnabled()
     */
    public boolean isNavigateForwardEnabled() {
        return false;
    }

    /**
     * @param t the target
     */
    public void setTarget(Object t) {
        target = t;
    }

}
