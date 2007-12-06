// $Id$
// Copyright (c) 1996-2007 The Regents of the University of California. All
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.argouml.i18n.Translator;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import org.argouml.uml.ui.AbstractActionAddModelElement2;


/**
 * This action binds Instances to one or more Classifiers,
 * which declare its structure and behaviour.
 * An Object is defined as an instance of a Class, which explains why
 * the type of Classifier is parameter to one of this action's constructors.
 *
 */
public class ActionAddInstanceClassifier extends AbstractActionAddModelElement2 {

    private Object choiceClass = Model.getMetaTypes().getClassifier();

    /**
     * The constructor for ActionAddExtendExtensionPoint.
     */
    public ActionAddInstanceClassifier() {
        super();
    }

    /**
     * Construct Add Classifier action for an instance.  When used with a UML
     * Object (an instance of a UML Class), the argument <code>choice</code>
     * will be UMLClass instead of Classifier to further restrict the 
     * available choices.
     *
     * @param choice the classifier type we are adding
     */
    public ActionAddInstanceClassifier(Object choice) {
        super();
        choiceClass = choice;
    }


    @Override
    protected void doIt(Collection selected) {
        Model.getCommonBehaviorHelper().setClassifiers(getTarget(), selected);
    }


    protected List getChoices() {
        List ret = new ArrayList();
        if (getTarget() != null) {
            Project p = ProjectManager.getManager().getCurrentProject();
            Object model = p.getRoot();
            ret.addAll(Model.getModelManagementHelper()
                    .getAllModelElementsOfKindWithModel(model, choiceClass));
        }
        return ret;
    }


    protected String getDialogTitle() {
        return Translator.localize("dialog.title.add-specifications");
    }


    protected List getSelected() {
        List ret = new ArrayList();
        ret.addAll(Model.getFacade().getClassifiers(getTarget()));
        return ret;
    }
}
