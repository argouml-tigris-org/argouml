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

package org.argouml.uml.ui.behavior.activity_graphs;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.argouml.kernel.ProjectManager;
import org.argouml.model.AttributeChangeEvent;
import org.argouml.model.Model;
import org.argouml.uml.ui.UMLComboBoxModel2;

/**
 * A model for the classifier of an ObjectFlowState.
 * This combo should never show the ClassifierInState! 
 * Instead it should show its "type".
 *
 * @since Oct 10, 2002
 * @author jaap.branderhorst@xs4all.nl, alexb
 */
public class UMLObjectFlowStateClassifierComboBoxModel
    extends UMLComboBoxModel2 {

    /**
     * Constructor.
     * TODO: MVW: I do not understand this! Is it correct?
     */
    public UMLObjectFlowStateClassifierComboBoxModel() {
        super("type", false);
        // TODO: Do we need something more here?
//        Model.getPump().addClassModelEventListener(this,
//                Model.getMetaTypes().getClassifier(), "type");
    }

    /**
     * @see org.argouml.uml.ui.UMLComboBoxModel2#isValidElement(Object)
     */
    protected boolean isValidElement(Object o) {
        return Model.getFacade().isAClassifier(o) /* 
            && !Model.getFacade().isAClassifierInState(o)*/;
    }

    /**
     * Get all Classifiers that are not ClassifierInState.
     * 
     * @see org.argouml.uml.ui.UMLComboBoxModel2#buildModelList()
     */
    protected void buildModelList() {
        Object model =
            ProjectManager.getManager().getCurrentProject().getModel();
        Collection c = new ArrayList(Model.getCoreHelper().getAllClassifiers(model));
        Collection newList = new ArrayList();
        Iterator i = c.iterator();
        while (i.hasNext()) {
            Object classifier = i.next();
//            if (!Model.getFacade().isAClassifierInState(classifier)) {
                newList.add(classifier);
//            }
        }
        // get the current type - normally we won't need this, but who knows?
        if (getTarget() != null) {
            Object type = Model.getFacade().getType(getTarget());
//            if (Model.getFacade().isAClassifierInState(type)) {
//                // get the Classifier
//                type = Model.getFacade().getType(type);
//            }
            if (type != null)
                if (!newList.contains(type)) newList.add(type);
        }
        setElements(newList);
    }

    /**
     * @see org.argouml.uml.ui.UMLComboBoxModel2#getSelectedModelElement()
     */
    protected Object getSelectedModelElement() {
        if (getTarget() != null) {
            Object type = Model.getFacade().getType(getTarget());
//            if (Model.getFacade().isAClassifierInState(type)) {
//                // get the Classifier
//                type = Model.getFacade().getType(type);
//            }
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
                    if (Model.getFacade().isAClassifierInState(elem)) {
                        // get the Classifier
                        elem = Model.getFacade().getType(elem);
                    }
                    setSelectedItem(elem);
                }
            }
        }
    }
}
