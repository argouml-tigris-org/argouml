// $Id$
// Copyright (c) 1996-2006 The Regents of the University of California. All
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

package org.argouml.uml.ui.foundation.core;

import java.awt.event.ActionEvent;

import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JList;
import javax.swing.JScrollPane;

import org.argouml.i18n.Translator;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.ui.AbstractActionNewModelElement;
import org.argouml.uml.ui.ActionDeleteSingleModelElement;
import org.argouml.uml.ui.ActionNavigateContainerElement;
import org.argouml.uml.ui.UMLLinkedList;
import org.argouml.uml.ui.foundation.extension_mechanisms.ActionNewStereotype;
import org.argouml.util.ConfigLoader;
import org.tigris.swidgets.Orientation;

/**
 * The properties panel for a Datatype.
 */
public class PropPanelDataType extends PropPanelClassifier {

    private JScrollPane operationScroll;

    private static UMLClassOperationListModel operationListModel =
        new UMLClassOperationListModel();

    /**
     * Construct a property panel for UML DataType elements.
     *
     * @param title
     * @param icon
     * @param orientation
     */
    public PropPanelDataType(String title, ImageIcon icon,
            Orientation orientation) {
        super(title, icon, orientation);

        addField(Translator.localize("label.name"),
                getNameTextField());
        addField(Translator.localize("label.namespace"),
                getNamespaceSelector());
        add(getModifiersPanel());
        add(getNamespaceVisibilityPanel());

        addSeparator();

        addField(Translator.localize("label.client-dependencies"),
                getClientDependencyScroll());
        addField(Translator.localize("label.supplier-dependencies"),
                getSupplierDependencyScroll());
        addField(Translator.localize("label.generalizations"),
                getGeneralizationScroll());
        addField(Translator.localize("label.specializations"),
                getSpecializationScroll());

        addSeparator();

        addField(Translator.localize("label.operations"),
                getOperationScroll());

        addAction(new ActionNavigateContainerElement());
        addAction(new ActionAddDataType());
        addEnumerationButtons();
        addAction(new ActionAddQueryOperation());
        addAction(new ActionNewStereotype());
        addAction(new ActionDeleteSingleModelElement());
    }

    /**
     * Override this to add more buttons.
     */
    protected void addEnumerationButtons() {
        addAction(new ActionAddEnumeration());
    }

    /**
     * The constructor.
     */
    public PropPanelDataType() {
        this("DataType", lookupIcon("DataType"),
                ConfigLoader.getTabPropsOrientation());
    }

    private static class ActionAddQueryOperation
        extends AbstractActionNewModelElement {

        /**
         * The constructor.
         */
        public ActionAddQueryOperation() {
            super("button.new-operation");
            putValue(Action.NAME, Translator.localize("button.new-operation"));
        }

        /**
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        public void actionPerformed(ActionEvent e) {
            Object target = TargetManager.getInstance().getModelTarget();
            if (Model.getFacade().isAClassifier(target)) {
                Object model =
                    ProjectManager.getManager()
                    	.getCurrentProject().getModel();
                Object voidType =
                    ProjectManager.getManager()
                    	.getCurrentProject().findType("void");
                Object newOper =
                    Model.getCoreFactory()
                    	.buildOperation(target, model, voidType);
                // due to Well Defined rule [2.5.3.12/1]
                Model.getCoreHelper().setQuery(newOper, true);
                TargetManager.getInstance().setTarget(newOper);
                super.actionPerformed(e);
            }
        }

        /**
         * The UID.
         */
        private static final long serialVersionUID = -3393730108010236394L;
    }

    /**
     * Returns the operationScroll.
     *
     * @return JScrollPane
     */
    public JScrollPane getOperationScroll() {
        if (operationScroll == null) {
            JList list = new UMLLinkedList(operationListModel);
            operationScroll = new JScrollPane(list);
        }
        return operationScroll;
    }

    /**
     * The UID.
     */
    private static final long serialVersionUID = -8752986130386737802L;
}
