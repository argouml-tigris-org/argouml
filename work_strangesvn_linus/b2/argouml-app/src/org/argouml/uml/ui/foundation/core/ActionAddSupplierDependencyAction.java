// $Id$
// Copyright (c) 2007 The Regents of the University of California. All
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

package org.argouml.uml.ui.foundation.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.argouml.i18n.Translator;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import org.argouml.uml.ui.AbstractActionAddModelElement2;

/**
 * An Action to add client dependencies to some modelelement.
 *
 * @author Michiel
 */
public class ActionAddSupplierDependencyAction extends
        AbstractActionAddModelElement2 {

    /**
     * The constructor.
     */
    public ActionAddSupplierDependencyAction() {
        super();
        setMultiSelect(true);
    }

    /*
     * Constraint: This code only deals with 1 supplier per dependency!
     * TODO: Do we need more?
     * 
     * @see org.argouml.uml.ui.AbstractActionAddModelElement#doIt(java.util.List)
     */
    protected void doIt(Collection selected) {
        Set oldSet = new HashSet(getSelected());
        for (Object supplier : oldSet) {
            if (oldSet.contains(supplier)) {
                oldSet.remove(supplier); //to be able to remove dep's later
            } else {
                Model.getCoreFactory().buildDependency(supplier, getTarget());
            }
        }

        Collection toBeDeleted = new ArrayList();
        Collection c =  Model.getFacade().getSupplierDependencies(getTarget());
        for (Object dependency : c) {
            if (oldSet.containsAll(
                    Model.getFacade().getClients(dependency))) {
                toBeDeleted.add(dependency);
            }
        }
        ProjectManager.getManager().getCurrentProject()
                .moveToTrash(toBeDeleted);
    }

    /*
     * @see org.argouml.uml.ui.AbstractActionAddModelElement#getChoices()
     */
    protected List getChoices() {
        List ret = new ArrayList();
        Object model =
            ProjectManager.getManager().getCurrentProject().getModel();
        if (getTarget() != null) {
            ret.addAll(Model.getModelManagementHelper()
                    .getAllModelElementsOfKind(model, 
                            "org.omg.uml.foundation.core.ModelElement"));
            ret.remove(getTarget());
        }
        return ret;
    }

    /*
     * @see org.argouml.uml.ui.AbstractActionAddModelElement#getDialogTitle()
     */
    protected String getDialogTitle() {
        return Translator.localize("dialog.title.add-supplier-dependency");
    }

    /*
     * @see org.argouml.uml.ui.AbstractActionAddModelElement#getSelected()
     */
    protected List getSelected() {
        List v = new ArrayList();
        Collection c =  Model.getFacade().getSupplierDependencies(getTarget());
        for (Object supplierDependency : c) {
            v.addAll(Model.getFacade().getClients(supplierDependency));
        }
        return v;
    }

}
