// $Id$
// Copyright (c) 2003-2005 The Regents of the University of California. All
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

package org.argouml.uml.ui.foundation.extension_mechanisms;

import java.awt.event.ActionEvent;

import javax.swing.SwingUtilities;

import org.apache.log4j.Logger;
import org.argouml.model.Model;
import org.argouml.uml.ui.UMLAction;
import org.argouml.uml.ui.UMLComboBox2;

/**
 *
 * @author mkl
 *
 */
public class ActionSetTagDefinitionOwner extends UMLAction {

    private Logger LOG = Logger.getLogger(ActionSetTagDefinitionOwner.class);
  
    /**
     * The Singleton.
     */
    public static final ActionSetTagDefinitionOwner SINGLETON =
            new ActionSetTagDefinitionOwner();
    
    /**
     * Constructor.
     */
    public ActionSetTagDefinitionOwner() {
        super("Set", HAS_ICON);
    }

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
        super.actionPerformed(e);
        Object source = e.getSource();
        LOG.info("Receiving " + e + "/" + e.getID() + "/"
                + e.getActionCommand());
        if (source instanceof UMLComboBox2
                && e.getModifiers() == ActionEvent.MOUSE_EVENT_MASK) {
            UMLComboBox2 combo = (UMLComboBox2) source;
            final Object o = combo.getSelectedItem();
            final Object tagDefinition = combo.getTarget();
            LOG.info("Set owner to " + o);
            if (Model.getFacade().isAStereotype(o)
                    && Model.getFacade().isATagDefinition(tagDefinition)) {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        Model.getCoreHelper().setOwner(tagDefinition, o);
                    }
                });
            }
        }
    }
}
