// $Id$
// Copyright (c) 2006 The Regents of the University of California. All
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

package org.argouml.uml.ui.foundation.extension_mechanisms;

import java.awt.AWTEvent;
import java.awt.event.ActionEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;

import org.argouml.i18n.Translator;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import org.argouml.uml.ui.ActionNavigateContainerElement;
import org.argouml.uml.ui.UMLComboBox2;
import org.argouml.uml.ui.UMLComboBoxModel2;
import org.argouml.uml.ui.UMLComboBoxNavigator;
import org.argouml.uml.ui.foundation.core.PropPanelModelElement;
import org.argouml.util.ConfigLoader;
import org.tigris.gef.undo.UndoableAction;

/**
 * The properties panel for a TaggedValue. <p>
 * 
 * TODO: Complete this panel - it needs to show fields for: 
 * list of dataValue, 
 * 1 modelElement, 
 * 1 type and 
 * the list of referenceValue.
 * See issue 2906.
 * And buttons for navigate up, new taggedValue, delete.
 * 
 * @author michiel
 */
public class PropPanelTaggedValue extends PropPanelModelElement {
    
    private JComponent modelElementSelector;
    
    /**
     * The constructor.
     */
    public PropPanelTaggedValue() {
        super("TaggedValue", ConfigLoader.getTabPropsOrientation());

        addField(Translator.localize("label.name"),
                getNameTextField());
        addField(Translator.localize("label.modelelement"),
                getModelElementSelector());

        addAction(new ActionNavigateContainerElement());
        addAction(new ActionNewTagDefinition());
        addAction(getDeleteAction());
    }

    /**
     * Returns the modelelement selecter. 
     * This is a component which allows the
     * user to select a single item as the modelelement.
     *
     * @return the modelelement selecter
     */
    protected JComponent getModelElementSelector() {
        if (modelElementSelector == null) {
            modelElementSelector = new Box(BoxLayout.X_AXIS);
            modelElementSelector.add(new UMLComboBoxNavigator(this,
                    Translator.localize("label.modelelement.navigate.tooltip"),
                    new UMLComboBox2(
                            new UMLTaggedValueModelElementComboBoxModel(),
                            new ActionSetTaggedValueModelElement())
            ));
        }
        return modelElementSelector;
    }
    
    class ActionSetTaggedValueModelElement extends UndoableAction {

        /**
         * Construct this action.
         */
        public ActionSetTaggedValueModelElement() {
            super();
        }

        /**
         * @see org.tigris.gef.undo.UndoableAction#actionPerformed(java.awt.event.ActionEvent)
         */
        public void actionPerformed(ActionEvent e) {
            super.actionPerformed(e);
            Object source = e.getSource();
            if (source instanceof UMLComboBox2
                    && e.getModifiers() == AWTEvent.MOUSE_EVENT_MASK) {
                UMLComboBox2 combo = (UMLComboBox2) source;
                Object o = combo.getSelectedItem();
                final Object taggedValue = combo.getTarget();
                if (Model.getFacade().isAModelElement(o)
                        && Model.getFacade().isATaggedValue(taggedValue)) {
                    Object oldME = Model.getFacade().getModelElement(taggedValue);
                    Model.getExtensionMechanismsHelper()
                        .removeTaggedValue(oldME, taggedValue);
                    Model.getExtensionMechanismsHelper()
                        .addTaggedValue(o, taggedValue);
                }
            }
        }
        
    }
    
    class UMLTaggedValueModelElementComboBoxModel 
        extends UMLComboBoxModel2 {

        /**
         * Constructor for UMLModelElementStereotypeComboBoxModel.
         */
        public UMLTaggedValueModelElementComboBoxModel() {
            super("modelelement", true); // ??
//            Model.getPump().addClassModelEventListener(
//                this,
//                Model.getMetaTypes().getNamespace(),
//                "ownedElement");
        }

        /**
         * @see org.argouml.uml.ui.UMLComboBoxModel2#buildModelList()
         */
        protected void buildModelList() {
            Object elem = getTarget();
            Project p = ProjectManager.getManager().getCurrentProject();
            Object model = p.getRoot();
            setElements(Model.getModelManagementHelper()
                .getAllModelElementsOfKindWithModel(model, 
                        Model.getMetaTypes().getModelElement()));
        }

        /**
         * @see org.argouml.uml.ui.UMLComboBoxModel2#getSelectedModelElement()
         */
        protected Object getSelectedModelElement() {
            Object me = null;
            if (getTarget() != null 
                    && Model.getFacade().isATaggedValue(getTarget())) {
                me = Model.getFacade().getModelElement(getTarget());
            }
            return me;
        }

        /**
         * @see org.argouml.uml.ui.UMLComboBoxModel2#isValidElement(java.lang.Object)
         */
        protected boolean isValidElement(Object element) {
            return Model.getFacade().isAModelElement(element);
        }
        
    }
}
