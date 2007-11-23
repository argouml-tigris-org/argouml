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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.argouml.i18n.Translator;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import org.argouml.uml.ui.AbstractActionAddModelElement2;

/**
 *
 * @author jaap.branderhorst@xs4all.nl
 * @since Jan 4, 2003
 */
public class ActionAddAssociationSpecification extends
        AbstractActionAddModelElement2 {

    private static final ActionAddAssociationSpecification SINGLETON =
        new ActionAddAssociationSpecification();
    /**
     * Constructor for ActionAddExtendExtensionPoint.
     */
    protected ActionAddAssociationSpecification() {
        super();
    }

    /*
     * @see org.argouml.uml.ui.AbstractActionAddModelElement#doIt(java.util.Vector)
     */
    protected void doIt(Collection selected) {
        Model.getCoreHelper().setSpecifications(getTarget(), selected);
    }

    /*
     * @see org.argouml.uml.ui.AbstractActionAddModelElement#getChoices()
     */
    protected List getChoices() {
        List ret = new ArrayList();
        if (getTarget() != null) {
            Project p = ProjectManager.getManager().getCurrentProject();
            Object model = p.getRoot();
            ret.addAll(Model.getModelManagementHelper()
                .getAllModelElementsOfKindWithModel(model,
                        Model.getMetaTypes().getClassifier()));
        }
        return ret;
    }

    /*
     * @see org.argouml.uml.ui.AbstractActionAddModelElement#getDialogTitle()
     */
    protected String getDialogTitle() {
        return Translator.localize("dialog.title.add-specifications");
    }

    /*
     * @see org.argouml.uml.ui.AbstractActionAddModelElement#getSelected()
     */
    protected List getSelected() {
        List ret = new ArrayList();
        ret.addAll(Model.getFacade().getSpecifications(getTarget()));
        return ret;
    }

    /**
     * @return Returns the sINGLETON.
     */
    public static ActionAddAssociationSpecification getInstance() {
        return SINGLETON;
    }
}
