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

package org.argouml.uml.ui.foundation.core;

import java.beans.PropertyChangeEvent;
import java.util.Collection;

import org.apache.log4j.Logger;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import org.argouml.uml.ui.UMLComboBoxModel2;

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
    protected void buildModelList() {
        Object model =
            ProjectManager.getManager().getCurrentProject().getRoot();
        Object target = getTarget();
        Collection c = 
            Model.getCoreHelper().getAllPossibleNamespaces(target, model);

        /* These next lines for the case that the current namespace
         * is not a valid one... Which ofcourse should not happen,
         * but it does - see the project attached to issue 3772.
         */
        /* TODO: Enhance the isValidNamespace function so
         * that this never happens.
         */
        if (target != null) {
            Object namespace = Model.getFacade().getNamespace(target);
            if (!c.contains(namespace)) {
                c.add(namespace);
                LOG.warn("The current namespace is not a valid one!");
            }
        }
        setElements(c);
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
    public void propertyChange(PropertyChangeEvent evt) {
        /*
         * Rebuild the list from scratch to be sure it's correct.
         */
        Object t = getTarget();
        if (t != null
                && evt.getSource() == t
                && evt.getNewValue() != null) {
//            setTarget(t); // this fixes issue 3780, but causes issue 3832.
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
    private static final long serialVersionUID = -775116993155949065L;
}
