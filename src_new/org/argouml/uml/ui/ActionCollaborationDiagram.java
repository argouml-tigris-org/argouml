// $Id$
// Copyright (c) 1996-2002 The Regents of the University of California. All
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
import org.argouml.model.ModelFacade;
import org.argouml.model.uml.UmlFactory;
import org.argouml.model.uml.behavioralelements.collaborations.CollaborationsHelper;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.diagram.collaboration.ui.UMLCollaborationDiagram;
import org.argouml.uml.diagram.ui.UMLDiagram;

//import ru.novosoft.uml.behavior.collaborations.MCollaboration;
//import ru.novosoft.uml.foundation.core.MClassifier;
//import ru.novosoft.uml.foundation.core.MNamespace;
//import ru.novosoft.uml.model_management.MModel;

/** Action to trigger creation of new collaboration diagram.
 *  @stereotype singleton
 */
public class ActionCollaborationDiagram extends ActionAddDiagram {

    public static ActionCollaborationDiagram SINGLETON =
        new ActionCollaborationDiagram();

    private ActionCollaborationDiagram() {
        super("action.collaboration-diagram");
    }

    /**
     * @see org.argouml.uml.ui.ActionAddDiagram#createDiagram(MNamespace,Object)
     */
    public UMLDiagram createDiagram(Object handle) {
        if (!ModelFacade.isANamespace(handle)) {
            cat.error("No namespace as argument");
            cat.error(handle);
            throw new IllegalArgumentException(
                "The argument " + handle + "is not a namespace.");
        }
        Object/*MNamespace*/ namespace = handle;
        Object target = TargetManager.getInstance().getTarget();
        Object collaboration = null;
        if (ModelFacade.isAOperation(target)) {
            collaboration =
                UmlFactory.getFactory().getCollaborations().buildCollaboration(namespace);
            ModelFacade.setRepresentedOperation(collaboration, target);
        } else if (ModelFacade.isAClassifier(target)) {
            collaboration =
                UmlFactory.getFactory().getCollaborations().buildCollaboration(target);
            ModelFacade.setRepresentedClassifier(collaboration, target);
        } else if (ModelFacade.isAModel(target)) {
            collaboration =
                UmlFactory.getFactory().getCollaborations().buildCollaboration(target);
        } else if (ModelFacade.isAInteraction(target)) {
            collaboration = ModelFacade.getContext(target);
        } else if (target instanceof UMLCollaborationDiagram) {
            Object owner = ((UMLCollaborationDiagram) target).getOwner();
            if (ModelFacade.isACollaboration(owner)) {
                //preventing backward compat problems
                collaboration = owner;
            }
        } else if (ModelFacade.isACollaboration(target)) {
            collaboration = target;
        } else {
            collaboration =
                UmlFactory.getFactory().getCollaborations().buildCollaboration(
                    namespace);
        }
        UMLCollaborationDiagram d = new UMLCollaborationDiagram(collaboration);
        return d;
    }

    /**
     * @see
     * org.argouml.model.uml.behavioralelements.collaborations.CollaborationsHelper#isAddingCollaborationAllowed(Object)
     * @see org.argouml.uml.ui.ActionAddDiagram#isValidNamespace(MNamespace)
     */
    public boolean isValidNamespace(Object handle) {
        if (!ModelFacade.isANamespace(handle)) {
            cat.error("No namespace as argument");
            cat.error(handle);
            throw new IllegalArgumentException(
                "The argument " + handle + "is not a namespace.");
        }
        Object/*MNamespace*/ ns = handle;
        return CollaborationsHelper.getHelper().isAddingCollaborationAllowed(ns);
    }

    /**
     * Just calls isValidNamespace(...) on the nav pane target.
     * @see org.argouml.uml.ui.UMLAction#shouldBeEnabled()
     */
    public boolean shouldBeEnabled() {

        Object target = TargetManager.getInstance().getTarget();
        if (org.argouml.model.ModelFacade.isANamespace(target))
            return isValidNamespace(target);
        else
            return false;
    }

} /* end class ActionCollaborationDiagram */