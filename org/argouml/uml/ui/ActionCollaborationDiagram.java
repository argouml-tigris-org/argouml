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
import org.argouml.model.uml.UmlFactory;

import org.argouml.application.api.Argo;
import org.argouml.kernel.*;
import org.argouml.model.uml.UmlFactory;
import org.argouml.uml.*;
import org.argouml.uml.diagram.collaboration.ui.*;
import org.argouml.uml.diagram.ui.UMLDiagram;
import org.argouml.ui.*;
import org.argouml.i18n.Translator;

import ru.novosoft.uml.*;
import ru.novosoft.uml.behavior.collaborations.*;
import ru.novosoft.uml.foundation.core.MClassifier;
import ru.novosoft.uml.foundation.core.MNamespace;
import ru.novosoft.uml.foundation.core.MOperation;
import ru.novosoft.uml.model_management.MModel;

import java.awt.event.*;
import java.beans.*;

/** Action to trigger creation of new collaboration diagram.
 *  @stereotype singleton
 */
public class ActionCollaborationDiagram extends ActionAddDiagram {

    public static ActionCollaborationDiagram SINGLETON = new ActionCollaborationDiagram();

    private ActionCollaborationDiagram() {
        super("CollaborationDiagram");
    }

    /**
     * @see org.argouml.uml.ui.ActionAddDiagram#createDiagram(MNamespace,Object)
     */
    public ArgoDiagram createDiagram(MNamespace ns, Object target) {
        MCollaboration c = null;
        if (target instanceof MOperation) {
            c = UmlFactory.getFactory().getCollaborations().buildCollaboration(ns);
            c.setRepresentedOperation((MOperation)target);
        } else
        if (target instanceof MClassifier) {
            c = UmlFactory.getFactory().getCollaborations().buildCollaboration((MClassifier)target);
            c.setRepresentedClassifier((MClassifier)target);
        } else
        if (target instanceof MModel) {
            c = UmlFactory.getFactory().getCollaborations().buildCollaboration((MModel)target);
        } else
        if (target instanceof MInteraction) {
            c = ((MInteraction)target).getContext();
        } else
        if (target instanceof UMLCollaborationDiagram) {
            Object o = ((UMLCollaborationDiagram)target).getOwner();
            if (o instanceof MCollaboration) { //preventing backward compat problems
                c = (MCollaboration)o;
            }
        } else
        if (target instanceof MCollaboration) {
            c = (MCollaboration)target;
        } else {
            c =  UmlFactory.getFactory().getCollaborations().buildCollaboration(ns);
        }
        UMLCollaborationDiagram d  = new UMLCollaborationDiagram(c);
        return d;
    }

    /**
     * @see org.argouml.uml.ui.ActionAddDiagram#isValidNamespace(MNamespace)
     */
    public boolean isValidNamespace(MNamespace ns) {
        if (ns instanceof MCollaboration) return true;
        return false;
    }

} /* end class ActionCollaborationDiagram */
