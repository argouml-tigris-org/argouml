// $Id$
// Copyright (c) 2003-2006 The Regents of the University of California. All
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

package org.argouml.uml.ui.behavior.activity_graphs;

import java.util.Collection;
import java.util.Vector;

import javax.swing.JComboBox;
import javax.swing.JScrollPane;

import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.uml.ui.AbstractActionAddModelElement;
import org.argouml.uml.ui.UMLModelElementListModel2;
import org.argouml.uml.ui.UMLMutableLinkedList;
import org.argouml.uml.ui.UMLSearchableComboBox;
import org.argouml.uml.ui.behavior.state_machines.AbstractPropPanelState;
import org.argouml.util.ConfigLoader;

/**
 * The properties panel for an ObjectFlowState.
 *
 * @author mkl
 */
public class PropPanelObjectFlowState extends AbstractPropPanelState {

    private JComboBox classifierComboBox;
    protected JScrollPane statesScroll;

    private UMLObjectFlowStateClassifierComboBoxModel classifierComboBoxModel =
        new UMLObjectFlowStateClassifierComboBoxModel();

    /**
     * Constructor
     */
    public PropPanelObjectFlowState() {
        super("ObjectFlowState", lookupIcon("ObjectFlowState"), ConfigLoader
                .getTabPropsOrientation());

        addField(Translator.localize("label.name"),
                getNameTextField());
        addField(Translator.localize("label.stereotype"),
                getStereotypeSelector());
        addField(Translator.localize("label.container"),
                getContainerScroll());

        // field for Classifier(not InState)
        addField(Translator.localize("label.type"),
                getClassifierComboBox());

        // field for States
        AbstractActionAddModelElement action = 
            new ActionAddOFSState();
        UMLMutableLinkedList list = new UMLMutableLinkedList(
                new UMLOFSStateListModel(), action, null, null, true);
        statesScroll = new JScrollPane(list);
        addField(Translator.localize("label.instate"),
                statesScroll);

        addSeperator();

        addField(Translator.localize("label.incoming"),
                getIncomingScroll());
        addField(Translator.localize("label.outgoing"),
                getOutgoingScroll());

    }


    /**
     * @see org.argouml.uml.ui.behavior.state_machines.AbstractPropPanelState#addExtraButtons()
     */
    protected void addExtraButtons() {
        /* We do not want the Internal Transitions button here. */
    }


    /**
     * @return the combo box for the type (Classifier or ClassifierInState)
     */
    protected JComboBox getClassifierComboBox() {
        if (classifierComboBox == null) {
            classifierComboBox = new UMLSearchableComboBox(
                    classifierComboBoxModel,
                    ActionSetObjectFlowStateClassifier.SINGLETON, true);
        }
        return classifierComboBox;

    }
}

class UMLOFSStateListModel extends UMLModelElementListModel2 {
    
    /**
     * Constructor for UMLOFSStateListModel.
     */
    public UMLOFSStateListModel() {
        /* TODO: This needs work... 
         * We also need to listen to addition/removal 
         * of states to/from a ClassifierInState. */
        super("type");
    }
    
    /**
     * @see org.argouml.uml.ui.UMLModelElementListModel2#buildModelList()
     */
    protected void buildModelList() {
        if (getTarget() != null) {
            Object classifier = Model.getFacade().getType(getTarget());
            if (Model.getFacade().isAClassifierInState(classifier)) {
                Collection c = Model.getFacade().getInStates(classifier);
                setAllElements(c);
            }
        }
    }
    
    /**
     * @see org.argouml.uml.ui.UMLModelElementListModel2#isValidElement(java.lang.Object)
     */
    protected boolean isValidElement(Object elem) {
        Object t = getTarget();
        if (Model.getFacade().isAState(elem) 
                && Model.getFacade().isAObjectFlowState(t)) {
            Object type = Model.getFacade().getType(t);
            if (Model.getFacade().isAClassifierInState(type)) {
                Collection c = Model.getFacade().getInStates(type);
                if (c.contains(elem)) return true;
            }
        }
        return false;
    }
}

class ActionAddOFSState extends AbstractActionAddModelElement {
    
    private Object choiceClass = Model.getMetaTypes().getState();
    
    
    public ActionAddOFSState() {
        super();
        this.setMultiSelect(true);
    }

    /**
     * @see org.argouml.uml.ui.AbstractActionAddModelElement#doIt(java.util.Vector)
     */
    protected void doIt(Vector selected) {
        Object t = getTarget();
        if (Model.getFacade().isAObjectFlowState(t)) {
            Object type = Model.getFacade().getType(t);
            if (Model.getFacade().isAClassifierInState(type)) {
                Model.getActivityGraphsHelper().setInStates(type, selected);
            } else if (Model.getFacade().isAClassifier(type) 
                    && (selected != null) 
                    && (selected.size() > 0)) {
                /* So, we found a Classifier that is not a ClassifierInState.
                 * And at least one state has been selected. 
                 * Well, let's correct that: */
                Object cis = Model.getActivityGraphsFactory().buildClassifierInState(type, selected);
                Model.getCoreHelper().setType(t, cis);
            }
        }
    }
    
    /**
     * @see org.argouml.uml.ui.AbstractActionAddModelElement#getChoices()
     */
    protected Vector getChoices() {
        Vector ret = new Vector();
        Object t = getTarget();
        if (Model.getFacade().isAObjectFlowState(t)) {
            Object classifier = Model.getFacade().getType(t);
            if (Model.getFacade().isAClassifierInState(classifier)) {
                classifier = Model.getFacade().getType(classifier);
            }
            if (Model.getFacade().isAClassifier(classifier)) {
                ret.addAll(Model.getModelManagementHelper()
                    .getAllModelElementsOfKindWithModel(classifier, choiceClass));
            }
        }
        return ret;
    }
    
    /**
     * @see org.argouml.uml.ui.AbstractActionAddModelElement#getDialogTitle()
     */
    protected String getDialogTitle() {
        return Translator.localize("dialog.title.add-state");
    }
    
    /**
     * @see org.argouml.uml.ui.AbstractActionAddModelElement#getSelected()
     */
    protected Vector getSelected() {
        Object t = getTarget();
        if (Model.getFacade().isAObjectFlowState(t)) {
            Object type = Model.getFacade().getType(t);
            if (Model.getFacade().isAClassifierInState(type)) {
                return new Vector(Model.getFacade().getInStates(type));
            }
        }
        return new Vector();
    }
}

