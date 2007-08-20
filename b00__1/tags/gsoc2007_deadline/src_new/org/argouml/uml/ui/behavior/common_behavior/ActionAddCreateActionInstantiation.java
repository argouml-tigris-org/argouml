// $Id:ActionAddCreateActionInstantiation.java 11450 2006-11-12 07:27:42Z tfmorris $
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

package org.argouml.uml.ui.behavior.common_behavior;

import java.util.Vector;

import org.argouml.i18n.Translator;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import org.argouml.uml.ui.AbstractActionAddModelElement;


/**
 * This action binds Instances to one  Classifiers,
 * which declare its structure and behaviour.
 */
public class ActionAddCreateActionInstantiation
    extends AbstractActionAddModelElement {

    private Object choiceClass = Model.getMetaTypes().getClassifier();


    /**
     * Constructor.
     */
    public ActionAddCreateActionInstantiation() {
        super();
        setMultiSelect(false);
    }


    /*
     * @see org.argouml.uml.ui.AbstractActionAddModelElement#doIt(java.util.Vector)
     */
    protected void doIt(Vector selected) {
        if (selected != null && selected.size() >= 1) {
            Model.getCommonBehaviorHelper().setInstantiation(getTarget(),
                    selected.get(0));
        } else {
            Model.getCommonBehaviorHelper().setInstantiation(getTarget(),
                    null);
        }
    }

    /*
     * @see org.argouml.uml.ui.AbstractActionAddModelElement#getChoices()
     */
    protected Vector getChoices() {
        Vector ret = new Vector();
        if (getTarget() != null) {
            Project p = ProjectManager.getManager().getCurrentProject();
            Object model = p.getRoot();
            ret.addAll(Model.getModelManagementHelper()
                    .getAllModelElementsOfKindWithModel(model, choiceClass));
        }
        return ret;
    }

    /*
     * @see org.argouml.uml.ui.AbstractActionAddModelElement#getDialogTitle()
     */
    protected String getDialogTitle() {
        return Translator.localize("dialog.title.add-instantiation");
    }

    /*
     * @see org.argouml.uml.ui.AbstractActionAddModelElement#getSelected()
     */
    protected Vector getSelected() {
        Vector ret = new Vector();
        Object instantiation =
            Model.getCommonBehaviorHelper().getInstantiation(getTarget());
        if (instantiation != null) {
            ret.add(instantiation);
        }
        return ret;
    }

    /**
     * The UID.
     */
    private static final long serialVersionUID = -7108663482184056359L;
}
