

// $Id$
// Copyright (c) 1996-2001 The Regents of the University of California. All
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

package org.argouml.uml.ui;

import org.argouml.kernel.ProjectManager;
import org.argouml.model.ModelFacade;
import org.argouml.model.uml.UmlFactory;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.diagram.sequence.ui.UMLSequenceDiagram;
import org.argouml.uml.diagram.ui.UMLDiagram;

import ru.novosoft.uml.behavior.collaborations.MCollaboration;
import ru.novosoft.uml.behavior.collaborations.MInteraction;
import ru.novosoft.uml.foundation.core.MClassifier;
import ru.novosoft.uml.foundation.core.MNamespace;
import ru.novosoft.uml.foundation.core.MOperation;
import ru.novosoft.uml.model_management.MModel;
import ru.novosoft.uml.model_management.MPackage;

/** Action to add a new sequence diagram.
 * @stereotype singleton
 */
public class ActionSequenceDiagram extends ActionAddDiagram {

    ////////////////////////////////////////////////////////////////
    // static variables

    public static ActionSequenceDiagram SINGLETON = new ActionSequenceDiagram();

    ////////////////////////////////////////////////////////////////
    // constructors

    private ActionSequenceDiagram() {
        super("action.sequence-diagram");
    }

    /**
     * @see
     * org.argouml.uml.ui.ActionAddDiagram#createDiagram(MNamespace,
     * Object)
     */
    public UMLDiagram createDiagram(Object handle) {
        if (!ModelFacade.isANamespace(handle)) {
            cat.error("No namespace as argument");
            cat.error(handle);
            throw new IllegalArgumentException(
                "The argument " + handle + "is not a namespace.");
        }
        MNamespace ns = (MNamespace) handle;
        MCollaboration c = null;
        Object target = TargetManager.getInstance().getModelTarget();
        if (org.argouml.model.ModelFacade.isAOperation(target)) {
            c =
                UmlFactory.getFactory().getCollaborations().buildCollaboration(
                    ns);
            c.setRepresentedOperation((MOperation) target);
        } else if (org.argouml.model.ModelFacade.isAClassifier(target)) {
            c =
                UmlFactory.getFactory().getCollaborations().buildCollaboration(
                    ns);
            c.setRepresentedClassifier((MClassifier) target);
        } else if (org.argouml.model.ModelFacade.isAModel(target)) {
            c =
                UmlFactory.getFactory().getCollaborations().buildCollaboration(
                    (MModel) target);
        } else if (org.argouml.model.ModelFacade.isAInteraction(target)) {
            c = ((MInteraction) target).getContext();
        } else if (target instanceof UMLSequenceDiagram) {
            Object o = ((UMLSequenceDiagram) target).getOwner();
            if (org.argouml.model.ModelFacade.isACollaboration(o)) {
                //preventing backward compat problems
                c = (MCollaboration) o;
            }
        } else if (org.argouml.model.ModelFacade.isACollaboration(target)) {
            c = (MCollaboration) target;
        } else {
            c =
                UmlFactory.getFactory().getCollaborations().buildCollaboration(
                    ns);
        }
        UMLSequenceDiagram d = new UMLSequenceDiagram(c);
        return d;
    }

    /**
     * @see org.argouml.uml.ui.ActionAddDiagram#isValidNamespace(MNamespace)
     */
    public boolean isValidNamespace(Object handle) {
        if (!ModelFacade.isANamespace(handle)) {
            cat.error("No namespace as argument");
            cat.error(handle);
            throw new IllegalArgumentException(
                "The argument " + handle + "is not a namespace.");
        }
        MNamespace ns = (MNamespace) handle;
        return (
            org.argouml.model.ModelFacade.isACollaboration(ns)
                || org.argouml.model.ModelFacade.isAClassifier(ns)
                || ns
                    == ProjectManager.getManager().getCurrentProject().getModel() ||
                    org.argouml.model.ModelFacade.isAPackage(ns));
    }
    
    /**
     * patch to disable SD's in 0.14.
     * @see org.argouml.uml.ui.UMLAction#shouldBeEnabled()
     */
    public boolean shouldBeEnabled() {

        return false;
    }
} /* end class ActionSequenceDiagram */