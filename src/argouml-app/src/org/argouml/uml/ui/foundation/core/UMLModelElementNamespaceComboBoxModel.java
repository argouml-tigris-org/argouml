// $Id$
// Copyright (c) 1996-2008 The Regents of the University of California. All
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

package org.argouml.uml.ui.foundation.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import org.argouml.model.UmlChangeEvent;
import org.argouml.uml.ui.UMLComboBoxModel2;
import org.argouml.uml.util.PathComparator;

/**
 * A model for a namespace combo box.
 *
 * @since Oct 10, 2002
 * @author jaap.branderhorst@xs4all.nl, alexb
 */
public class UMLModelElementNamespaceComboBoxModel extends UMLComboBoxModel2 {

    /**
     * Logger.
     */
    private static final Logger LOG =
        Logger.getLogger(UMLModelElementNamespaceComboBoxModel.class);

    /**
     * The UID.
     */
    private static final long serialVersionUID = -775116993155949065L;
    
    /**
     * Constructor for UMLModelElementNamespaceComboBoxModel.
     */
    public UMLModelElementNamespaceComboBoxModel() {
        super("namespace", true);
        Model.getPump().addClassModelEventListener(this,
                Model.getMetaTypes().getNamespace(), "ownedElement");
    }

    /*
     * @see org.argouml.uml.ui.UMLComboBoxModel2#isValidElement(Object)
     */
    protected boolean isValidElement(Object o) {
        return Model.getFacade().isANamespace(o)
                && Model.getCoreHelper().isValidNamespace(getTarget(), o);
    }

    /*
     * @see org.argouml.uml.ui.UMLComboBoxModel2#buildModelList()
     */
    @Override
    protected void buildMinimalModelList() {
        Object target = getTarget();
        Collection c = new ArrayList(1);

        if (target != null) {
            Object namespace = Model.getFacade().getNamespace(target);
            if (namespace != null && !c.contains(namespace)) {
                c.add(namespace);
            }
        }
        setElements(c);
    }

    /*
     * @see org.argouml.uml.ui.UMLComboBoxModel2#buildModelList()
     */
    protected void buildModelList() {
        Set<Object> elements = new TreeSet<Object>(new PathComparator());
        
        Object model =
            ProjectManager.getManager().getCurrentProject().getRoot();
        Object target = getTarget();
        elements.addAll(
            Model.getCoreHelper().getAllPossibleNamespaces(target, model));

        /* These next lines for the case that the current namespace
         * is not a valid one... Which of course should not happen,
         * but it does - see the project attached to issue 3772.
         */
        /* TODO: Enhance the isValidNamespace function so
         * that this never happens.
         */
        if (target != null) {
            Object namespace = Model.getFacade().getNamespace(target);
            if (namespace != null && !elements.contains(namespace)) {
                elements.add(namespace);
                LOG.warn("The current namespace is not a valid one!");
            }
        }

        // Our comparator will throw an InvalidElementException if the old
        // list contains deleted elements (eg after a new project is loaded)
        // so remove all the old contents first
        removeAllElements();
        addAll(elements);
    }

    /*
     * @see org.argouml.uml.ui.UMLComboBoxModel2#getSelectedModelElement()
     */
    protected Object getSelectedModelElement() {
        if (getTarget() != null) {
            return Model.getFacade().getNamespace(getTarget());
        }
        return null;
    }

    /*
    * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
    */
    @Override
    public void modelChanged(UmlChangeEvent evt) {
        /*
         * Rebuild the list from scratch to be sure it's correct.
         */
        Object t = getTarget();
        if (t != null
                && evt.getSource() == t
                && evt.getNewValue() != null) {
            /* In some cases (see issue 3780) the list remains the same, but
             * the selected item differs. Without the next step,
             * the combo would not be refreshed.
             */
            buildMinimalModelList();
            setSelectedItem(getSelectedModelElement());
        }
    }
    
    @Override
    protected boolean isLazy() {
        return true;
    }
}
