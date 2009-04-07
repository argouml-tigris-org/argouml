// $Id: UMLStateDeferrableEventListModel.java 15914 2008-10-13 17:10:00Z bobtarling $
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

package org.argouml.core.propertypanels.ui;

import javax.swing.JPopupMenu;

import org.argouml.model.Model;
import org.argouml.uml.ui.behavior.state_machines.ActionNewEvent;

/**
 * @since Dec 14, 2002
 * @author jaap.branderhorst@xs4all.nl
 */
class UMLStateDeferrableEventListModel
    extends UMLModelElementListModel {

    /**
     * Constructor for UMLStateDeferrableEventListModel.
     */
    public UMLStateDeferrableEventListModel() {
        super("deferrableEvent");
    }

    /*
     * @see org.argouml.uml.ui.UMLModelElementListModel2#buildModelList()
     */
    protected void buildModelList() {
        setAllElements(Model.getFacade().getDeferrableEvents(getTarget()));
    }

    /*
     * @see org.argouml.uml.ui.UMLModelElementListModel2#isValidElement(Object)
     */
    protected boolean isValidElement(Object element) {
        return Model.getFacade().getDeferrableEvents(getTarget())
        	.contains(element);
    }

    @Override
    public boolean buildPopup(JPopupMenu popup, int index) {
        final PopupMenuNewEvent menu =
            new PopupMenuNewEvent(ActionNewEvent.Roles.DEFERRABLE_EVENT, getTarget());
        
        menu.buildMenu(popup,
                ActionNewEvent.Roles.DEFERRABLE_EVENT, getTarget());;
        
        return true;
    }
}
