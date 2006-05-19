// $Id$
// Copyright (c) 1996-2006 The Regents of the University of California. All
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

package org.argouml.uml.diagram.ui;

import java.awt.event.ActionEvent;

import org.argouml.model.Model;
import org.argouml.notation.NotationHelper;
import org.argouml.uml.ui.UMLAction;

/**
 * Action to add a sterotype to a model element.
 * @author Bob Tarling
 */
class ActionAddStereotype extends UMLAction {
    private Object modelElement;
    private Object stereotype;

    /**
     * Constructor.
     *
     * @param me The model element.
     * @param st The stereotype.
     */
    public ActionAddStereotype(Object me, Object st) {
        super(NotationHelper.getLeftGuillemot()
                + Model.getFacade().getName(st)
                + NotationHelper.getRightGuillemot(), NO_ICON);
        modelElement = me;
        stereotype = st;
    }

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent ae) {
        if (Model.getFacade().getStereotypes(modelElement)
                .contains(stereotype)) {
            Model.getCoreHelper().removeStereotype(modelElement, stereotype);
        } else {
            Object stereo =
                Model.getModelManagementHelper()
                    .getCorrespondingElement(stereotype,
                        Model.getFacade().getModel(modelElement), true);
            Model.getCoreHelper().addStereotype(modelElement, stereo);
        }
    }

    /**
     * @see javax.swing.Action#getValue(java.lang.String)
     */
    public Object getValue(String key) {
        if ("SELECTED".equals(key)) {
            if (Model.getFacade().getStereotypes(modelElement).contains(
                    stereotype)) {
                return Boolean.TRUE;
            } else {
                return Boolean.FALSE;
            }
        }
        return super.getValue(key);
    }
}
