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

package org.argouml.uml.ui.model_management;

import java.awt.event.ActionEvent;

import javax.swing.Action;
import javax.swing.JList;
import javax.swing.JScrollPane;

import org.argouml.i18n.Translator;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.ui.AbstractActionNewModelElement;
import org.argouml.uml.ui.UMLLinkedList;
import org.argouml.uml.ui.foundation.core.UMLClassifierFeatureListModel;
import org.argouml.util.ConfigLoader;

/**
 * A property panel for UML subsystems.
 */
public class PropPanelSubsystem extends PropPanelPackage {

    private JScrollPane featureScroll;

    private static UMLClassifierFeatureListModel featureListModel =
        new UMLClassifierFeatureListModel();

    /**
     * The constructor.
     *
     */
    public PropPanelSubsystem() {
        super("Subsystem", lookupIcon("Subsystem"),
                ConfigLoader.getTabPropsOrientation());

        addField(Translator.localize("label.available-features"),
                getFeatureScroll());

        addAction(new ActionNewOperation());
    }

    /**
     * Add a new operation to this classifier.
     *
     * @author mvw@tigris.org
     */
    private static class ActionNewOperation
        extends AbstractActionNewModelElement {

        /**
         * The key for the action name.
         */
        private static final String ACTION_KEY = "button.new-operation";

        /**
         * The constructor.
         */
        public ActionNewOperation() {
            super(ACTION_KEY);
            putValue(Action.NAME, Translator.localize(ACTION_KEY));
        }

        /**
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        public void actionPerformed(ActionEvent e) {
            Object target = TargetManager.getInstance().getModelTarget();
            if (Model.getFacade().isAClassifier(target)) {
                Project p = ProjectManager.getManager().getCurrentProject();
                Object model = p.getModel();
                Object voidType = p.findType("void");
                Object newOper =
                    Model.getCoreFactory()
                        .buildOperation(target, model, voidType);
                TargetManager.getInstance().setTarget(newOper);
                super.actionPerformed(e);
            }
        }

        /**
         * The UID.
         */
        private static final long serialVersionUID = -5149342278246959597L;
    }

    /**
     * Returns the featureScroll.
     *
     * @return JScrollPane
     */
    public JScrollPane getFeatureScroll() {
        if (featureScroll == null) {
            JList list = new UMLLinkedList(featureListModel);
            featureScroll = new JScrollPane(list);
        }
        return featureScroll;
    }

    /**
     * The UID.
     */
    private static final long serialVersionUID = -8616239241648089917L;
} /* end class PropPanelSubsystem */
