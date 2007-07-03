// $Id: PropPanelCallEvent.java 11516 2006-11-25 04:30:15Z tfmorris $
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

package org.argouml.uml.ui.behavior.state_machines;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.ui.UMLComboBox2;
import org.argouml.uml.ui.UMLComboBoxModel2;
import org.argouml.uml.ui.UMLComboBoxNavigator;
import org.argouml.uml.ui.UMLSearchableComboBox;
import org.argouml.uml.ui.foundation.core.ActionNewParameter;
import org.argouml.util.ConfigLoader;

/**
 * The properties panel of a CallEvent.
 *
 * @author oliver.heyden@gentleware.de
 */
public class PropPanelCallEvent extends PropPanelEvent {

    /**
     * The constructor.
     *
     */
    public PropPanelCallEvent() {
        super("Call event", lookupIcon("CallEvent"),
              ConfigLoader.getTabPropsOrientation());
    }

    /*
     * @see org.argouml.uml.ui.behavior.state_machines.PropPanelEvent#initialize()
     */
    public void initialize() {
        super.initialize();

        UMLSearchableComboBox operationComboBox =
            new UMLCallEventOperationComboBox2(
                new UMLCallEventOperationComboBoxModel());
        addField(Translator.localize("label.operations"),
                new UMLComboBoxNavigator(
                        this,
                        Translator.localize("label.operation.navigate.tooltip"),
                        operationComboBox));

        addAction(new ActionNewParameter());
        addAction(getDeleteAction());
    }

} /* end class PropPanelCallEvent */

class UMLCallEventOperationComboBox2 extends UMLSearchableComboBox {
    /**
     * The constructor.
     *
     * @param arg0 the model
     */
    public UMLCallEventOperationComboBox2(UMLComboBoxModel2 arg0) {
        super(arg0, null); // no external action; we do it ourselves
        setEditable(false);
    }

    /*
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
        super.actionPerformed(e);
        Object source = e.getSource();
        if (source instanceof UMLComboBox2) {
            Object selected = ((UMLComboBox2) source).getSelectedItem();
            Object target = ((UMLComboBox2) source).getTarget();
            if (Model.getFacade().isACallEvent(target) 
                && Model.getFacade().isAOperation(selected)) {
                if (Model.getFacade().getOperation(target) != selected) {
                    Model.getCommonBehaviorHelper()
                        .setOperation(target, selected);
                }
            }
        }
    }
}

class UMLCallEventOperationComboBoxModel extends UMLComboBoxModel2 {
    /**
     * The constructor.
     */
    public UMLCallEventOperationComboBoxModel() {
        super("operation", true);
    }

    /*
     * @see org.argouml.uml.ui.UMLComboBoxModel2#buildModelList()
     */
    protected void buildModelList() {
        Object target = TargetManager.getInstance().getModelTarget();
        Collection ops = new ArrayList();
        if (Model.getFacade().isACallEvent(target)) {
            Object ns = Model.getFacade().getNamespace(target);
            if (Model.getFacade().isANamespace(ns)) {
                Collection c =
                    Model.getModelManagementHelper().getAllModelElementsOfKind(
                            ns,
                            Model.getMetaTypes().getClassifier());
                Iterator i = c.iterator();
                while (i.hasNext()) {
                    ops.addAll(Model.getFacade().getOperations(i.next()));
                }
                c = Model.getModelManagementHelper().getAllImportedElements(ns);
                i = c.iterator();
                while (i.hasNext()) {
                    Object obj = i.next();
                    if (Model.getFacade().isAClassifier(obj)) {
                        ops.addAll(Model.getFacade().getOperations(obj));
                    }
                }
            }
        }
        setElements(ops);
    }

    /*
     * @see org.argouml.uml.ui.UMLComboBoxModel2#getSelectedModelElement()
     */
    protected Object getSelectedModelElement() {
        Object target = TargetManager.getInstance().getModelTarget();
        if (Model.getFacade().isACallEvent(target)) {
            return Model.getFacade().getOperation(target);
        }
        return null;
    }

    /*
     * @see org.argouml.uml.ui.UMLComboBoxModel2#isValidElement(java.lang.Object)
     */
    protected boolean isValidElement(Object element) {
        Object target = TargetManager.getInstance().getModelTarget();
        if (Model.getFacade().isACallEvent(target)) {
            return element == Model.getFacade().getOperation(target);
        }
        return false;
    }
}

