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

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JComboBox;
import javax.swing.JScrollPane;

import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.uml.ui.AbstractActionAddModelElement;
import org.argouml.uml.ui.AbstractActionNewModelElement;
import org.argouml.uml.ui.AbstractActionRemoveElement;
import org.argouml.uml.ui.UMLComboBoxNavigator;
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
public class PropPanelObjectFlowState extends AbstractPropPanelState
    implements PropertyChangeListener {

    private JComboBox classifierComboBox;
    private JScrollPane statesScroll;

    private ActionNewClassifierInState actionNewCIS;

    private UMLObjectFlowStateClassifierComboBoxModel classifierComboBoxModel =
        new UMLObjectFlowStateClassifierComboBoxModel();

    /**
     * Construct a property panel for ObjectFlowState elements.
     */
    public PropPanelObjectFlowState() {
        super("ObjectFlowState", lookupIcon("ObjectFlowState"), ConfigLoader
                .getTabPropsOrientation());

        addField(Translator.localize("label.name"),
                getNameTextField());
        addField(Translator.localize("label.container"),
                getContainerScroll());

        addField(Translator.localize("label.synch-state"),
                new UMLActionSynchCheckBox());
        
        // field for Type (Classifier)
        addField(Translator.localize("label.type"),
            new UMLComboBoxNavigator(
                this,
                Translator.localize("label.classifierinstate.navigate.tooltip"),
                getClassifierComboBox()));

        // field for States
        UMLMutableLinkedList list =
            new UMLMutableLinkedList(
                new UMLOFSStateListModel(),
                new ActionAddOFSState(),
                null,
                new ActionRemoveOFSState(),
                true);
        statesScroll = new JScrollPane(list);
        addField(Translator.localize("label.instate"),
                statesScroll);

        addSeparator();

        addField(Translator.localize("label.incoming"),
                getIncomingScroll());
        addField(Translator.localize("label.outgoing"),
                getOutgoingScroll());
        
        // field for Parameters
        addField(Translator.localize("label.parameters"),
                new JScrollPane(
                        new UMLMutableLinkedList(
                                new UMLObjectFlowStateParameterListModel(),
                                new ActionAddOFSParameter(),
                                new ActionNewOFSParameter(),
                                new ActionRemoveOFSParameter(),
                                true)));
    }


    /*
     * @see org.argouml.uml.ui.behavior.state_machines.AbstractPropPanelState#addExtraButtons()
     */
    protected void addExtraButtons() {
        /* We do not want the Internal Transitions button here. */

        actionNewCIS = new ActionNewClassifierInState();
        actionNewCIS.putValue(Action.SHORT_DESCRIPTION,
                Translator.localize("button.new-classifierinstate"));
        Icon icon = ResourceLoaderWrapper.lookupIcon("ClassifierInState");
        actionNewCIS.putValue(Action.SMALL_ICON, icon);
        addAction(actionNewCIS);
    }

    /*
     * @see org.argouml.uml.ui.PropPanel#setTarget(java.lang.Object)
     */
    public void setTarget(Object t) {
        Object oldTarget = getTarget();
        super.setTarget(t);
        actionNewCIS.setEnabled(actionNewCIS.isEnabled());
        if (Model.getFacade().isAObjectFlowState(oldTarget)) {
            Model.getPump().removeModelEventListener(this, oldTarget, "type");
        }
        if (Model.getFacade().isAObjectFlowState(t)) {
            Model.getPump().addModelEventListener(this, t, "type");
        }
    }

    /*
     * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
     */
    public void propertyChange(PropertyChangeEvent evt) {
        actionNewCIS.setEnabled(actionNewCIS.isEnabled());
    }

    /**
     * @return the combo box for the type (Classifier or ClassifierInState)
     */
    protected JComboBox getClassifierComboBox() {
        if (classifierComboBox == null) {
            classifierComboBox =
                new UMLSearchableComboBox(
                    classifierComboBoxModel,
                    new ActionSetObjectFlowStateClassifier(), true);
        }
        return classifierComboBox;

    }


    /**
     * Utility function to remove the top states
     * from a given collection of states.
     *
     * @param ret a collection of states
     */
    static void removeTopStateFrom(Collection ret) {
        Iterator i = ret.iterator();
        Collection tops = new ArrayList();
        while (i.hasNext()) {
            Object state = i.next();
            if (Model.getFacade().isACompositeState(state)
                    && Model.getFacade().isTop(state)) {
                tops.add(state);
            }
        }
        ret.removeAll(tops);
    }

    private static Object getType(Object target) {
        Object type = Model.getFacade().getType(target);
        if (Model.getFacade().isAClassifierInState(type)) {
            type = Model.getFacade().getType(type);
        }
        return type;
    }
    
    static class UMLOFSStateListModel extends UMLModelElementListModel2 {

        /**
         * Constructor for UMLOFSStateListModel.
         */
        public UMLOFSStateListModel() {
            /* TODO: This needs work...
             * We also need to listen to addition/removal
             * of states to/from a ClassifierInState.
             */
            super("type");
        }

        /*
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

        /*
         * @see org.argouml.uml.ui.UMLModelElementListModel2#isValidElement(java.lang.Object)
         */
        protected boolean isValidElement(Object elem) {
            Object t = getTarget();
            if (Model.getFacade().isAState(elem)
                    && Model.getFacade().isAObjectFlowState(t)) {
                Object type = Model.getFacade().getType(t);
                if (Model.getFacade().isAClassifierInState(type)) {
                    Collection c = Model.getFacade().getInStates(type);
                    if (c.contains(elem)) {
                        return true;
                    }
                }
            }
            return false;
        }

        /**
         * The UID.
         */
        private static final long serialVersionUID = -7742772495832660119L;
    }

    static class ActionAddOFSState extends AbstractActionAddModelElement {
        private Object choiceClass = Model.getMetaTypes().getState();


        /**
         * The constructor.
         */
        public ActionAddOFSState() {
            super();
            setMultiSelect(true);
        }

        /*
         * @see org.argouml.uml.ui.AbstractActionAddModelElement#doIt(
         *         java.util.Vector)
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
                    /* So, we found a Classifier
                     * that is not a ClassifierInState.
                     * And at least one state has been selected.
                     * Well, let's correct that:
                     */
                    Object cis =
                        Model.getActivityGraphsFactory()
                            .buildClassifierInState(type, selected);
                    Model.getCoreHelper().setType(t, cis);
                }
            }
        }

        /*
         * @see org.argouml.uml.ui.AbstractActionAddModelElement#getChoices()
         */
        protected Vector getChoices() {
            Vector ret = new Vector();
            Object t = getTarget();
            if (Model.getFacade().isAObjectFlowState(t)) {
                Object classifier = getType(t);
                if (Model.getFacade().isAClassifier(classifier)) {
                    ret.addAll(Model.getModelManagementHelper()
                            .getAllModelElementsOfKindWithModel(classifier,
                                    choiceClass));
                }
                removeTopStateFrom(ret);
            }
            return ret;
        }

        /*
         * @see org.argouml.uml.ui.AbstractActionAddModelElement#getDialogTitle()
         */
        protected String getDialogTitle() {
            return Translator.localize("dialog.title.add-state");
        }

        /*
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

        /**
         * The UID.
         */
        private static final long serialVersionUID = 7266495601719117169L;
    }

    static class ActionRemoveOFSState extends AbstractActionRemoveElement {

        /**
         * Constructor.
         */
        public ActionRemoveOFSState() {
            super(Translator.localize("menu.popup.remove"));
        }

        /*
         * @see org.tigris.gef.undo.UndoableAction#actionPerformed(java.awt.event.ActionEvent)
         */
        public void actionPerformed(ActionEvent e) {
            super.actionPerformed(e);
            Object state = getObjectToRemove();
            if (state != null) {
                Object t = getTarget();
                if (Model.getFacade().isAObjectFlowState(t)) {
                    Object type = Model.getFacade().getType(t);
                    if (Model.getFacade().isAClassifierInState(type)) {
                        Collection states =
                            new ArrayList(
                                Model.getFacade().getInStates(type));
                        states.remove(state);
                        Model.getActivityGraphsHelper()
                                .setInStates(type, states);
                    }
                }
            }
        }

        /**
         * The UID.
         */
        private static final long serialVersionUID = -5113809512624883836L;
    }

    /**
     * This is the model for the list of parameters for an ObjectFlowState.<p>
     *
     * @author Tom Morris
     * @since 6 May 2007
     */
    static class UMLObjectFlowStateParameterListModel
        extends UMLModelElementListModel2 {

        /**
         * Constructor for UMLObjectFlowStateParameterListModel.
         */
        public UMLObjectFlowStateParameterListModel() {
            super("parameter");
        }

        /*
         * @see org.argouml.uml.ui.UMLModelElementListModel2#buildModelList()
         */
        protected void buildModelList() {
            if (getTarget() != null) {
                setAllElements(Model.getFacade().getParameters(getTarget()));
            }
        }

        /*
         * @see org.argouml.uml.ui.UMLModelElementListModel2#isValidElement(Object)
         */
        protected boolean isValidElement(Object element) {
            return Model.getFacade().getParameters(getTarget()).contains(
                    element);
        }
        
    }
    
    
    static class ActionAddOFSParameter extends AbstractActionAddModelElement {
        private Object choiceClass = Model.getMetaTypes().getParameter();

        /**
         * The constructor.
         */
        public ActionAddOFSParameter() {
            super();
            setMultiSelect(true);
        }

        /*
         * @see org.argouml.uml.ui.AbstractActionAddModelElement#doIt(
         *         java.util.Vector)
         */
        protected void doIt(Vector selected) {
            Object t = getTarget();
            if (Model.getFacade().isAObjectFlowState(t)) {
                Model.getActivityGraphsHelper().setParameters(t, selected);
            }
        }

        /*
         * @see org.argouml.uml.ui.AbstractActionAddModelElement#getChoices()
         */
        protected Vector getChoices() {
            Vector ret = new Vector();
            Object t = getTarget();
            if (Model.getFacade().isAObjectFlowState(t)) {
                Object classifier = getType(t);
                if (Model.getFacade().isAClassifier(classifier)) {
                    ret.addAll(Model.getModelManagementHelper()
                            .getAllModelElementsOfKindWithModel(classifier,
                                    choiceClass));
                }

                // TODO: We may want to restrict the list to parameters which 
                // conform to the following WFR:
//              parameter.type = ofstype
//              or (parameter.kind = #in
//              and ofstype.allSupertypes->includes(type))
//              or ((parameter.kind = #out or parameter.kind = #return)
//              and type.allSupertypes->includes(ofstype))
//              or (parameter.kind = #inout
//              and ( ofstype.allSupertypes->includes(type)
//              or type.allSupertypes->includes(ofstype))))

            }
            return ret;
        }

        /*
         * @see org.argouml.uml.ui.AbstractActionAddModelElement#getDialogTitle()
         */
        protected String getDialogTitle() {
            return Translator.localize("dialog.title.add-state");
        }

        /*
         * @see org.argouml.uml.ui.AbstractActionAddModelElement#getSelected()
         */
        protected Vector getSelected() {
            Object t = getTarget();
            if (Model.getFacade().isAObjectFlowState(t)) {
                    return new Vector(Model.getFacade().getParameters(t));
            }
            return new Vector();
        }
        
    }
    
    
    static class ActionNewOFSParameter extends AbstractActionNewModelElement {
        ActionNewOFSParameter() {
            super();
        }

        public void actionPerformed(ActionEvent e) {
            Object target = getTarget();
            if (Model.getFacade().isAObjectFlowState(target)) {
                Object type = getType(target);
                Object parameter = Model.getCoreFactory().createParameter();
                Model.getCoreHelper().setType(parameter, type);
                Model.getActivityGraphsHelper().addParameter(target, parameter);
            }
        }
    }

    
    static class ActionRemoveOFSParameter extends AbstractActionRemoveElement {

        /**
         * Constructor.
         */
        public ActionRemoveOFSParameter() {
            super(Translator.localize("menu.popup.remove"));
        }

        /*
         * @see org.tigris.gef.undo.UndoableAction#actionPerformed(java.awt.event.ActionEvent)
         */
        public void actionPerformed(ActionEvent e) {
            super.actionPerformed(e);
            Object param = getObjectToRemove();
            if (param != null) {
                Object t = getTarget();
                if (Model.getFacade().isAObjectFlowState(t)) {
                    Model.getActivityGraphsHelper().removeParameter(t, param);
                }
            }
        }
    }

    
}
