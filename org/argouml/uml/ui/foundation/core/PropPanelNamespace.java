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

import javax.swing.ImageIcon;
import javax.swing.JScrollPane;

import org.argouml.model.Model;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.ui.ScrollList;
import org.tigris.swidgets.Orientation;

/**
 * The abstract properties panel for a Namespace.
 *
 */
public abstract class PropPanelNamespace extends PropPanelModelElement {

    private JScrollPane ownedElementsScroll;

    private static UMLNamespaceOwnedElementListModel ownedElementListModel =
        new UMLNamespaceOwnedElementListModel();

    /**
     * The constructor.
     *
     * @param panelName the name of the panel to be shown at the top
     * @param icon the icon to be shown next to the name
     * @param orientation the orientation of the panel
     */
    public PropPanelNamespace(String panelName, ImageIcon icon,
            Orientation orientation) {
        super(panelName, icon, orientation);
    }

    /**
     * The constructor.
     *
     * @param title the name of the panel to be shown at the top
     * @param orientation the orientation of the panel
     */
    public PropPanelNamespace(String title, Orientation orientation) {
    	super(title, orientation);
    }


    /**
     * Create a class.
     */
    public void addClass() {
        Object target = getTarget();
        if (Model.getFacade().isANamespace(target)) {
            Object ns = target;
            Object ownedElem = Model.getCoreFactory().buildClass();
            Model.getCoreHelper().addOwnedElement(ns, ownedElem);
            TargetManager.getInstance().setTarget(ownedElem);
        }
    }

    /**
     * Create a new interface.
     */
    public void addInterface() {
        Object target = getTarget();
        if (Model.getFacade().isANamespace(target)) {
            Object ns = target;
            Object ownedElem = Model.getCoreFactory().createInterface();
            Model.getCoreHelper().addOwnedElement(ns, ownedElem);
            TargetManager.getInstance().setTarget(ownedElem);
        }
    }

    /**
     * Create a new package within the namespace.
     */
    public void addPackage() {
        Object target = getTarget();
        if (Model.getFacade().isANamespace(target)) {
            Object ns = target;
            Object ownedElem = Model.getModelManagementFactory()
                .createPackage();
            Model.getCoreHelper().addOwnedElement(ns, ownedElem);
            TargetManager.getInstance().setTarget(ownedElem);
        }
    }



    /**
     * Returns the ownedElementsScroll.
     * @return JScrollPane
     */
    public JScrollPane getOwnedElementsScroll() {
        if (ownedElementsScroll == null) {
            ownedElementsScroll =
                    new ScrollList(ownedElementListModel, true, false);
        }
        return ownedElementsScroll;

    }

} /* end class PropPanelClass */
