// $Id$
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
package org.argouml.uml.ui.behavior.use_cases;

import java.awt.event.ActionEvent;

import org.argouml.application.api.Argo;
import org.argouml.model.uml.behavioralelements.usecases.UseCasesHelper;
import org.argouml.uml.ui.UMLChangeAction;
import org.argouml.uml.ui.UMLComboBox2;

import ru.novosoft.uml.behavior.use_cases.MExtend;
import ru.novosoft.uml.behavior.use_cases.MUseCase;

/**
 * @since Oct 6, 2002
 * @author jaap.branderhorst@xs4all.nl
 * @stereotype singleton
 */
public class ActionSetExtendExtension extends UMLChangeAction {

    public static final ActionSetExtendExtension SINGLETON =
        new ActionSetExtendExtension();

    /**
     * Constructor for ActionSetExtendBase.
     * @param s
     */
    protected ActionSetExtendExtension() {
        super(Argo.localize("CoreMenu", "Set"), true, NO_ICON);
    }

    /**
    * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
    */
    public void actionPerformed(ActionEvent e) {
        super.actionPerformed(e);
        Object source = e.getSource();
        MUseCase newExtension = null;
        MUseCase oldExtension = null;
        MExtend extend = null;
        if (source instanceof UMLComboBox2) {
            UMLComboBox2 combo = (UMLComboBox2) source;
            newExtension = (MUseCase) combo.getSelectedItem();
            Object o = combo.getTarget();
            if (o instanceof MExtend) {
                extend = (MExtend) o;
                o = combo.getSelectedItem();
                if (o instanceof MUseCase) {
                    newExtension = (MUseCase) o;
                    oldExtension = extend.getExtension();
                    if (newExtension != oldExtension) {
                        extend.setExtension(newExtension);
                    }
                } else {
                    if (o != null && o.equals("")) {
                        extend.setExtension(null);
                    }
                }

            }

        }

    }

}
