// $Id$
// Copyright (c) 1996-2003 The Regents of the University of California. All
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

package org.argouml.uml.ui.foundation.core;

import java.awt.event.ActionEvent;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import org.argouml.model.ModelFacade;
import org.argouml.uml.ui.UMLAction;
import org.argouml.uml.ui.UMLModelElementListModel2;

/**
 * 
 * @author jaap.branderhorst@xs4all.nl	
 * @since Jan 26, 2003
 */
public class UMLClassifierParameterListModel
    extends UMLModelElementListModel2 {

    /**
     * Constructor for UMLClassifierParameterListModel.
     */
    public UMLClassifierParameterListModel() {
        super("parameter");
    }

    /**
     * @see org.argouml.uml.ui.UMLModelElementListModel2#buildModelList()
     */
    protected void buildModelList() {
        if (getTarget() != null) {
            setAllElements(org.argouml.model.ModelFacade
                    .getParameters(getTarget()));
        }
    }

    /**
     * @see org.argouml.uml.ui.UMLModelElementListModel2#isValidElement(Object)
     */
    protected boolean isValidElement(Object/*MBase*/ element) {
        return ModelFacade.getParameters(getTarget()).contains(element);
    }

    /**
     * @see org.argouml.uml.ui.UMLModelElementListModel2#buildPopup(
     * javax.swing.JPopupMenu, int)
     */
    public boolean buildPopup(JPopupMenu popup, int index) {
        JMenuItem moveUp = new JMenuItem(
                new MoveUpAction("menu.popup.moveup", this, index));
        JMenuItem moveDown = new JMenuItem(
                new MoveDownAction("menu.popup.movedown", this, index));
        popup.add(moveUp);
        popup.add(moveDown);
        return true; // i.e. yes, we generated a menu
    }

    class MoveUpAction extends UMLAction {
        private UMLModelElementListModel2 model;
        private int index;
        
        /**
         * The constructor.
         * 
         * @param name the (to be localized) description of the action
         */
        public MoveUpAction(String name, UMLModelElementListModel2 theModel, 
                int theIndex) {
            super(name, false, false);
            model = theModel;
            index = theIndex;
        }
        
        /**
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        public void actionPerformed(ActionEvent e) {
            // TODO: Auto-generated method stub
            super.actionPerformed(e);
        }
        /**
         * @see javax.swing.Action#isEnabled()
         */
        public boolean isEnabled() {
            return index > 0;
        }
    }
    
    /**
     * @author Michiel
     *
     */
    class MoveDownAction extends UMLAction {
        private UMLModelElementListModel2 model;
        private int index;
        
        /**
         * The constructor.
         * 
         * @param name the (to be localized) description of the action
         */
        public MoveDownAction(String name, UMLModelElementListModel2 theModel, 
                int theIndex) {
            super(name, false, false);
            model = theModel;
            index = theIndex;
        }
        
        /**
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        public void actionPerformed(ActionEvent e) {
            // TODO: Auto-generated method stub
            super.actionPerformed(e);
        }
        /**
         * @see javax.swing.Action#isEnabled()
         */
        public boolean isEnabled() {
            return model.getSize() > index + 1;
        }
    }
}
