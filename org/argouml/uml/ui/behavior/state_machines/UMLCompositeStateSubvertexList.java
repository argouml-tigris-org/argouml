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

package org.argouml.uml.ui.behavior.state_machines;

import javax.swing.JMenu;
import javax.swing.JPopupMenu;

import org.argouml.i18n.Translator;
import org.argouml.uml.ui.ActionRemoveModelElement;
import org.argouml.uml.ui.UMLModelElementListModel2;
import org.argouml.uml.ui.UMLMutableLinkedList;

/**
 * @since Dec 14, 2002
 * @author jaap.branderhorst@xs4all.nl
 */
public class UMLCompositeStateSubvertexList extends UMLMutableLinkedList {
    
    private class PopupMenu extends JPopupMenu {
        
        /**
         * Constructor for PopupMenu.
         */
        public PopupMenu() {
            super();
            
            JMenu newMenu = new JMenu();
            newMenu.setText(Translator.localize("action.new"));
            ActionNewPseudoState.SINGLETON.setTarget(getTarget());
            newMenu.add(ActionNewPseudoState.SINGLETON);
            newMenu.add(ActionNewSynchState.SINGLETON);
            ActionNewSynchState.SINGLETON.setTarget(getTarget());
            newMenu.add(ActionNewStubState.SINGLETON);
            ActionNewStubState.SINGLETON.setTarget(getTarget());
            newMenu.add(ActionNewCompositeState.SINGLETON);
            ActionNewCompositeState.SINGLETON.setTarget(getTarget());
            newMenu.add(ActionNewSimpleState.SINGLETON);
            ActionNewSimpleState.SINGLETON.setTarget(getTarget());
            newMenu.add(ActionNewFinalState.SINGLETON);
            ActionNewFinalState.SINGLETON.setTarget(getTarget());
            newMenu.add(ActionNewSubmachineState.SINGLETON);
            ActionNewSubmachineState.SINGLETON.setTarget(getTarget());
            add(newMenu);
            
            addSeparator();
            
            ActionRemoveModelElement.SINGLETON.setTarget(getSelectedValue());
            add(ActionRemoveModelElement.SINGLETON);
        }
        

    }
    
    /**
     * Constructor for UMLCompositeStateSubvertexList.
     * @param container
     * @param dataModel
     * @param popup
     */
    public UMLCompositeStateSubvertexList(
        UMLModelElementListModel2 dataModel) {
        super(dataModel, null);
    }

    /**
     * @see org.argouml.uml.ui.UMLMutableLinkedList#getPopupMenu()
     */
    public JPopupMenu getPopupMenu() {
        return new PopupMenu();
    }

}
