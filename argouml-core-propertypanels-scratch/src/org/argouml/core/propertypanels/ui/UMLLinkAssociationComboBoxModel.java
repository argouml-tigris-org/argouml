// $Id$
// Copyright (c) 2008 The Regents of the University of California. All
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

package org.argouml.core.propertypanels.ui;

import java.beans.PropertyChangeEvent;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import org.argouml.model.Model;
import org.argouml.uml.ui.UMLComboBoxModel2;

/**
 * The model for the combobox to show the Association of the Link.
 *
 * @author Michiel
 */
class UMLLinkAssociationComboBoxModel extends UMLComboBoxModel2 {

    /**
     * Constructor for UMLModelElementNamespaceComboBoxModel.
     */
    public UMLLinkAssociationComboBoxModel() {
        super("assocation", true);
    }

    /*
     * @see org.argouml.uml.ui.UMLComboBoxModel2#isValidElement(Object)
     */
    protected boolean isValidElement(Object o) {
        return Model.getFacade().isAAssociation(o);
    }

    /**
     * To simplify implementation, we list all associations
     * found with any of the Classifiers
     * represented by the linked Instances. <p>
     *
     * TODO: Make a foolproof algorithm that only allows selecting associations
     * that create a correct model. Also take into account n-ary associations
     * and associationclasses. This algo best goes in the model subsystem, e.g.
     * in a method getAllPossibleAssociationsForALink().
     *
     * @see org.argouml.uml.ui.UMLComboBoxModel2#buildModelList()
     */
    protected void buildModelList() {
        Collection linkEnds;
        Collection associations = new HashSet();
        Object t = getTarget();
        if (Model.getFacade().isALink(t)) {
            linkEnds = Model.getFacade().getConnections(t);
            Iterator ile = linkEnds.iterator();
            while (ile.hasNext()) {
                Object instance = Model.getFacade().getInstance(ile.next());
                Collection c = Model.getFacade().getClassifiers(instance);
                Iterator ic = c.iterator();
                while (ic.hasNext()) {
                    Object classifier = ic.next();
                    Collection ae =
                        Model.getFacade().getAssociationEnds(classifier);
                    Iterator iae = ae.iterator();
                    while (iae.hasNext()) {
                        Object associationEnd = iae.next();
                        Object association =
                            Model.getFacade().getAssociation(associationEnd);
                        associations.add(association);
                    }
                }
            }
        }
        setElements(associations);
    }

    /*
     * @see org.argouml.uml.ui.UMLComboBoxModel2#getSelectedModelElement()
     */
    protected Object getSelectedModelElement() {
        if (Model.getFacade().isALink(getTarget())) {
            return Model.getFacade().getAssociation(getTarget());
        }
        return null;
    }

    /*
    * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
    */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        /*
         * Rebuild the list from scratch to be sure it's correct.
         */
        Object t = getTarget();
        if (t != null
                && evt.getSource() == t
                && evt.getNewValue() != null) {
            buildModelList();
            /* In some cases (se issue 3780) the list remains the same, but
             * the selected item differs. Without the next step,
             * the combo would not be refreshed.
             */
            setSelectedItem(getSelectedModelElement());
        }
    }

    /**
     * The UID.
     */
    private static final long serialVersionUID = 3232437122889409351L;
}
