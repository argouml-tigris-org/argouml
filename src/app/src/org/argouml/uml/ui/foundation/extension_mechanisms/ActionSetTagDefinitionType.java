// $Id$
// Copyright (c) 2006-2007 The Regents of the University of California. All
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

package org.argouml.uml.ui.foundation.extension_mechanisms;

import java.awt.event.ActionEvent;

import javax.swing.Action;

import org.apache.log4j.Logger;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.uml.ui.UMLComboBox2;
import org.tigris.gef.undo.UndoableAction;

/**
 * Action to set the type of a TagDefinition. The tagType attribute of a
 * TagDefinition is a Name of a UML metaclass (ie String).
 */
public class ActionSetTagDefinitionType extends UndoableAction {

    private static final ActionSetTagDefinitionType SINGLETON =
        new ActionSetTagDefinitionType();

    private static final Logger LOG = 
    	Logger.getLogger(ActionSetTagDefinitionType.class);

    /**
     * Constructor for ActionSetTagDefinitionType.
     */
    protected ActionSetTagDefinitionType() {
        super(Translator.localize("Set"), null);
        // Set the tooltip string:
        putValue(Action.SHORT_DESCRIPTION, 
                Translator.localize("Set"));
    }

    /*
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        super.actionPerformed(e);
        Object source = e.getSource();
        LOG.debug("Receiving " + e + "/" + e.getID() + "/"
                + e.getActionCommand());
        String oldType = null;
        String newType = null;
        Object tagDef = null;
        if (source instanceof UMLComboBox2) {
            UMLComboBox2 box = (UMLComboBox2) source;
            Object t = box.getTarget();
            if (Model.getFacade().isATagDefinition(t)) {
                tagDef = t;
                oldType = (String) Model.getFacade().getType(tagDef);
            }
            newType = (String) box.getSelectedItem();
            LOG.debug("Selected item is " + newType);
        }
        if (newType != null && !newType.equals(oldType) && tagDef != null) {
            LOG.debug("New type is " + newType);
            Model.getExtensionMechanismsHelper().setTagType(tagDef, newType);
        }
    }
    
    /**
     * @return Returns the singleton instance.
     */
    public static ActionSetTagDefinitionType getInstance() {
        return SINGLETON;
    }

}
