// $Id$
// Copyright (c) 2006-2007 The Regents of the University of California. All
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JComboBox;
import javax.swing.JScrollPane;

import org.argouml.i18n.Translator;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.AttributeChangeEvent;
import org.argouml.model.InvalidElementException;
import org.argouml.model.Model;
import org.argouml.uml.ui.AbstractActionAddModelElement;
import org.argouml.uml.ui.AbstractActionAddModelElement2;
import org.argouml.uml.ui.AbstractActionRemoveElement;
import org.argouml.uml.ui.ActionNavigateNamespace;
import org.argouml.uml.ui.UMLComboBox2;
import org.argouml.uml.ui.UMLComboBoxModel2;
import org.argouml.uml.ui.UMLComboBoxNavigator;
import org.argouml.uml.ui.UMLModelElementListModel2;
import org.argouml.uml.ui.UMLMutableLinkedList;
import org.argouml.uml.ui.UMLSearchableComboBox;
import org.argouml.uml.ui.foundation.core.PropPanelClassifier;
import org.tigris.gef.undo.UndoableAction;

/**
 * The properties panel for a ClassifierInState.
 * 
 * @author Michiel
 */
public class PropPanelClassifierInState extends PropPanelClassifier {

    /**
     * The serial version.
     */
    private static final long serialVersionUID = 609338855898756817L;
    
    private JComboBox typeComboBox;
    private JScrollPane statesScroll;

    private UMLClassifierInStateTypeComboBoxModel typeComboBoxModel =
        new UMLClassifierInStateTypeComboBoxModel();
    
    /**
     * Construct a property panel for a ClassifierInState.
     */
    public PropPanelClassifierInState() {
        super("label.classifier-in-state", lookupIcon("ClassifierInState"));

        addField(Translator.localize("label.name"),
                getNameTextField());
        addField(Translator.localize("label.namespace"),
                getNamespaceSelector());
        
        addSeparator();
        
        addField(Translator.localize("label.type"),
                new UMLComboBoxNavigator(
                        Translator.localize("label.class.navigate.tooltip"),
                getClassifierInStateTypeSelector()));
        
        // field for States
        AbstractActionAddModelElement2 actionAdd = 
            new ActionAddCISState();
        AbstractActionRemoveElement actionRemove = 
            new ActionRemoveCISState();
        UMLMutableLinkedList list =
                new UMLMutableLinkedList(
                        new UMLCISStateListModel(), actionAdd, null,
                        actionRemove, true);
        statesScroll = new JScrollPane(list);
        addField(Translator.localize("label.instate"),
                statesScroll);

        addAction(new ActionNavigateNamespace());
        addAction(getDeleteAction());
    }
    
    protected JComboBox getClassifierInStateTypeSelector() {
        if (typeComboBox == null) {
            typeComboBox = new UMLSearchableComboBox(
                    typeComboBoxModel,
                    new ActionSetClassifierInStateType(), true);
        }
        return typeComboBox;

    }

}

class ActionSetClassifierInStateType extends UndoableAction {

    /**
     * The serial version.
     */
    private static final long serialVersionUID = -7537482435346517599L;

    /**
     * Construct an action to set the type of a ClassifierInState.
     */
    ActionSetClassifierInStateType() {
        super();
    }

    /*
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource(); // the source UI element of the event
        Object oldClassifier = null;
        Object newClassifier = null;
        Object cis = null;
        if (source instanceof UMLComboBox2) {
            UMLComboBox2 box = (UMLComboBox2) source;
            Object obj = box.getTarget();
            if (Model.getFacade().isAClassifierInState(obj)) {
                try {
                    oldClassifier = Model.getFacade().getType(obj);
                } catch (InvalidElementException e1) {
                    /* No problem - this ClassifierInState was just erased. */
                    return;
                }
                cis = obj;
            }
            Object cl = box.getSelectedItem();
            if (Model.getFacade().isAClassifier(cl)) {
                newClassifier = cl;
            }
        }
        if (newClassifier != oldClassifier
                && cis != null
                && newClassifier != null) {
            Model.getCoreHelper().setType(cis, newClassifier);
            super.actionPerformed(e);
        }
    }

}

class UMLClassifierInStateTypeComboBoxModel extends UMLComboBoxModel2 {

    /**
     * The serial version.
     */
    private static final long serialVersionUID = 1705685511742198305L;

    /**
     * Construct a combobox model for a ClassifierInState's type.
     */
    public UMLClassifierInStateTypeComboBoxModel() {
        super("type", false);
    }
    
    /*
     * @see org.argouml.uml.ui.UMLComboBoxModel2#isValidElement(Object)
     */
    protected boolean isValidElement(Object o) {
        return Model.getFacade().isAClassifier(o)
                && !Model.getFacade().isAClassifierInState(o);
    }
    
    /**
     * Get all Classifiers that are not ClassifierInState.
     * 
     * @see org.argouml.uml.ui.UMLComboBoxModel2#buildModelList()
     */
    protected void buildModelList() {
        Object model =
            ProjectManager.getManager().getCurrentProject().getModel();
        Collection c = 
            new ArrayList(Model.getCoreHelper().getAllClassifiers(model));
        Collection newList = new ArrayList();
        Iterator i = c.iterator();
        while (i.hasNext()) {
            Object classifier = i.next();
            if (!Model.getFacade().isAClassifierInState(classifier)) {
                newList.add(classifier);
            }
        }
        // get the current type - normally we won't need this, but who knows?
        if (getTarget() != null) {
            Object type = Model.getFacade().getType(getTarget());
            if (Model.getFacade().isAClassifierInState(type)) {
                // get the Classifier
                type = Model.getFacade().getType(type);
            }
            if (type != null)
                if (!newList.contains(type)) newList.add(type);
        }
        setElements(newList);
    }
    
    /*
     * @see org.argouml.uml.ui.UMLComboBoxModel2#getSelectedModelElement()
     */
    protected Object getSelectedModelElement() {
        if (getTarget() != null) {
            Object type = Model.getFacade().getType(getTarget());
            return type; // a Classifier that is not a ClassifierInState
        }
        return null;
    }
    
    /**
     * The function in the parent removes items from the list 
     * when deselected. We do not need that here. 
     * 
     * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
     */
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt instanceof AttributeChangeEvent) {
            if (evt.getPropertyName().equals("type")) {
                if (evt.getSource() == getTarget()
                        && (getChangedElement(evt) != null)) {
                    Object elem = getChangedElement(evt);
                    setSelectedItem(elem);
                }
            }
        }
    }
}

class ActionAddCISState extends AbstractActionAddModelElement {
    
    /**
     * The serial version.
     */
    private static final long serialVersionUID = -3892619042821099432L;
    private Object choiceClass = Model.getMetaTypes().getState();
    
    
    /**
     * Construct an action to add a new ClassifierInState.
     */
    public ActionAddCISState() {
        super();
        setMultiSelect(true);
    }

    /*
     * @see org.argouml.uml.ui.AbstractActionAddModelElement#doIt(java.util.Vector)
     */
    protected void doIt(Vector selected) {
        Object cis = getTarget();
        if (Model.getFacade().isAClassifierInState(cis)) {
            Model.getActivityGraphsHelper().setInStates(cis, selected);
        }
    }
    
    /*
     * @see org.argouml.uml.ui.AbstractActionAddModelElement#getChoices()
     */
    protected Vector getChoices() {
        Vector ret = new Vector();
        Object cis = getTarget();
        Object classifier = Model.getFacade().getType(cis);
        if (Model.getFacade().isAClassifier(classifier)) {
            ret.addAll(Model.getModelManagementHelper()
                    .getAllModelElementsOfKindWithModel(classifier,
                            choiceClass));
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
        Object cis = getTarget();
        if (Model.getFacade().isAClassifierInState(cis)) {
            return new Vector(Model.getFacade().getInStates(cis));
        }
        return new Vector();
    }
}

class ActionRemoveCISState extends AbstractActionRemoveElement {

    /**
     * The serial version.
     */
    private static final long serialVersionUID = -1431919084967610562L;

    /**
     * Construct an action to remove a ClassifierInState.
     */
    public ActionRemoveCISState() {
        super(Translator.localize("menu.popup.remove"));
    }

    /*
     * @see org.tigris.gef.undo.UndoableAction#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
        super.actionPerformed(e);
        Object state = getObjectToRemove(); 
        if (state != null) {
            Object cis = getTarget();
            if (Model.getFacade().isAClassifierInState(cis)) {
                Collection states = new ArrayList(
                        Model.getFacade().getInStates(cis));
                states.remove(state);
                Model.getActivityGraphsHelper().setInStates(cis, states);
            }
            
        }
    }
    
}

class UMLCISStateListModel extends UMLModelElementListModel2 {
    
    /**
     * The serial version.
     */
    private static final long serialVersionUID = -8786823179344335113L;

    /**
     * Construct a list model for ClassifierInState elements.
     */
    public UMLCISStateListModel() {
        super("inState");
    }
    
    /*
     * @see org.argouml.uml.ui.UMLModelElementListModel2#buildModelList()
     */
    protected void buildModelList() {
        Object cis = getTarget();
        if (Model.getFacade().isAClassifierInState(cis)) {
            Collection c = Model.getFacade().getInStates(cis);
            setAllElements(c);
        }
    }
    
    /*
     * @see org.argouml.uml.ui.UMLModelElementListModel2#isValidElement(java.lang.Object)
     */
    protected boolean isValidElement(Object elem) {
        Object cis = getTarget();
        if (Model.getFacade().isAClassifierInState(cis)) {
            Collection c = Model.getFacade().getInStates(cis);
            if (c.contains(elem)) return true;
        }
        return false;
    }
}
