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

// $Id$
package org.argouml.uml.ui.foundation.core;

import java.awt.event.ActionEvent;

import org.argouml.application.api.Argo;
import org.argouml.model.ModelFacade;
import org.argouml.uml.ui.UMLChangeAction;
import org.argouml.uml.ui.UMLCheckBox2;

//import ru.novosoft.uml.foundation.data_types.MOrderingKind;

/**
 * 
 * @author jaap.branderhorst@xs4all.nl	
 * @since Jan 4, 2003
 */
public class ActionSetAssociationEndOrdering extends UMLChangeAction {

    public static final ActionSetAssociationEndOrdering SINGLETON = new ActionSetAssociationEndOrdering();

    /**
     * Constructor for ActionSetElementOwnershipSpecification.
     * @param s
     */
    protected ActionSetAssociationEndOrdering() {
        super(Argo.localize("CoreMenu", "Set"), true, NO_ICON);
    }
    
    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
        super.actionPerformed(e);
        if (e.getSource() instanceof UMLCheckBox2) {
            UMLCheckBox2 source = (UMLCheckBox2) e.getSource();
            Object target = source.getTarget();
            if (org.argouml.model.ModelFacade.isAAssociationEnd(target)) {
                Object m = /*(MAssociationEnd)*/ target;
                if (source.isSelected()) {
                    ModelFacade.setOrdering(m, ModelFacade.ORDERED_ORDERINGKIND); 
                } else {
                    ModelFacade.setOrdering(m, ModelFacade.UNORDERED_ORDERINGKIND); 
                }
            }
        }
    }

}