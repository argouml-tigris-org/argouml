// $Id$
// Copyright (c) 1996-2004 The Regents of the University of California. All
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

package org.argouml.uml.ui.foundation.core;

import java.awt.event.ActionEvent;
import java.util.Collection;

import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.ui.AbstractActionNewModelElement;

/**
 * Action to add a parameter to the selected target.
 * 
 * @since Juli 11, 2004
 * @author jaap.branderhorst@xs4all.nl
 */
public class ActionNewParameter extends AbstractActionNewModelElement {

    private static final ActionNewParameter SINGLETON = 
        new ActionNewParameter();

    /**
     * The constructor.
     */
    public ActionNewParameter() {
        super("button.new-parameter");
    }

    /**
     * On event, a parameter is build and added to the target.
     * 
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
        super.actionPerformed(e);
        //Object target =  getTarget(); // it is not set anywhere, pity...
        Object target =  TargetManager.getInstance().getModelTarget();
        Object model = ProjectManager.getManager().getCurrentProject().getModel();
        Object voidType = ProjectManager.getManager().getCurrentProject().findType("void");
        Collection propertyChangeListeners = ProjectManager.getManager().getCurrentProject().findFigsForMember(target);
        Object param = Model.getCoreFactory().buildParameter(target, model, voidType, propertyChangeListeners);
        TargetManager.getInstance().setTarget(param);
    }

    /**
     * @return Returns the SINGLETON.
     * @deprecated singleton use will be removed in 0.18.0. 
     * Use the constructor instead.
     */
    public static ActionNewParameter getInstance() {
        return SINGLETON;
    }
}