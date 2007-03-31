// $Id$
// Copyright (c) 2007 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies. This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason. IN NO EVENT SHALL THE
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

package org.argouml.uml.ui.model_management;

import javax.swing.JList;
import javax.swing.JScrollPane;

import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.uml.ui.ActionNavigateContainerElement;
import org.argouml.uml.ui.UMLLinkedList;
import org.argouml.uml.ui.UMLModelElementListModel2;
import org.argouml.uml.ui.foundation.core.PropPanelModelElement;
import org.argouml.util.ConfigLoader;

/**
 * The properties panel for an ElementImport.
 *
 * @author Michiel
 */
public class PropPanelElementImport extends PropPanelModelElement {

    /**
     * Constructor.
     */
    public PropPanelElementImport() {
        super("ElementImport", ConfigLoader.getTabPropsOrientation());

        // This is not a ModelElement, hence none of the following:
//     addField(Translator.localize("label.name"),
//     getNameTextField());
//     add(getNamespaceVisibilityPanel());
//     addSeparator();

        JList lst1 = new UMLLinkedList(
                new ElementImportImportedElementListModel());
        lst1.setVisibleRowCount(1);
        addField(Translator.localize("label.imported-element"),
                new JScrollPane(lst1));

        JList lst2 = new UMLLinkedList(
                new ElementImportPackageListModel());
        lst2.setVisibleRowCount(1);
        addField(Translator.localize("label.package"),
                new JScrollPane(lst2));

        addAction(new ActionNavigateContainerElement());
        addAction(getDeleteAction());
    }

}

/**
 * The list model for the importedElement of a ElementImport.
 *
 * @author mvw
 */
class ElementImportImportedElementListModel extends UMLModelElementListModel2 {

    /**
     * Constructor.
     */
    public ElementImportImportedElementListModel() {
        super("importedElement");
    }

    protected void buildModelList() {
        if (getTarget() != null) {
            removeAllElements();
            addElement(Model.getFacade().getImportedElement(getTarget()));
        }
    }

    protected boolean isValidElement(Object element) {
        return Model.getFacade().isAElementImport(getTarget());
    }
}

/**
 * The list model for the package of a ElementImport.
 *
 * @author mvw
 */
class ElementImportPackageListModel extends UMLModelElementListModel2 {

    /**
     * Constructor.
     */
    public ElementImportPackageListModel() {
        super("package");
    }

    protected void buildModelList() {
        if (getTarget() != null) {
            removeAllElements();
            addElement(Model.getFacade().getPackage(getTarget()));
        }
    }

    protected boolean isValidElement(Object element) {
        return Model.getFacade().isAElementImport(getTarget());
    }
}