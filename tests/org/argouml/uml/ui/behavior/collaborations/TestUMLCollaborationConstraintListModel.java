// Copyright (c) 1996-99 The Regents of the University of California. All
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

// $header$
package org.argouml.uml.ui.behavior.collaborations;

import org.argouml.model.uml.behavioralelements.collaborations.CollaborationsFactory;
import org.argouml.model.uml.foundation.core.CoreFactory;
import org.argouml.uml.ui.AbstractUMLModelElementListModel2Test;
import org.argouml.uml.ui.UMLUserInterfaceContainer;
import ru.novosoft.uml.MBase;
import ru.novosoft.uml.behavior.collaborations.MCollaboration;
import ru.novosoft.uml.foundation.core.MConstraint;
import ru.novosoft.uml.foundation.core.MModelElement;


/**
 * @since Oct 27, 2002
 * @author jaap.branderhorst@xs4all.nl
 */
public class TestUMLCollaborationConstraintListModel
    extends AbstractUMLModelElementListModel2Test {

    /**
     * Constructor for TestUMLCollaborationConstraintListModel.
     * @param arg0
     */
    public TestUMLCollaborationConstraintListModel(String arg0) {
        super(arg0);
    }

    /**
     * @see org.argouml.uml.ui.AbstractUMLModelElementListModel2Test#buildElement()
     */
    protected void buildElement() {
        elem = CollaborationsFactory.getFactory().createCollaboration();
    }

    /**
     * @see org.argouml.uml.ui.AbstractUMLModelElementListModel2Test#buildModel(org.argouml.uml.ui.UMLUserInterfaceContainer)
     */
    protected void buildModel(UMLUserInterfaceContainer cont) {
        model = new UMLCollaborationConstraintListModel(cont);
    }

    /**
     * @see org.argouml.uml.ui.AbstractUMLModelElementListModel2Test#fillModel()
     */
    protected MBase[] fillModel() {
        MConstraint[] constraints = new MConstraint[10];
        for (int i = 0; i < 10; i++) {
            constraints[i] = CoreFactory.getFactory().createConstraint();
            ((MCollaboration)elem).addConstrainingElement(constraints[i]);
        }
        return constraints;
    }

    /**
     * @see org.argouml.uml.ui.AbstractUMLModelElementListModel2Test#removeHalfModel(ru.novosoft.uml.MBase)
     */
    protected void removeHalfModel(MBase[] elements) {
        for (int i = 0 ; i < 5; i++) {
            ((MCollaboration)elem).removeConstrainingElement((MModelElement)elements[i]);
        }
    }

}
