// Copyright (c) 1996-01 The Regents of the University of California. All
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

package org.argouml.uml.ui;

import java.awt.event.ActionEvent;
import java.util.Collection;
import java.util.Iterator;

import org.argouml.ui.targetmanager.TargetManager;
import org.tigris.gef.base.CmdCut;
import org.tigris.gef.presentation.Fig;

/** @stereotype singleton
 */
public class ActionCut extends UMLAction {

    ////////////////////////////////////////////////////////////////
    // static variables

    public static ActionCut SINGLETON = new ActionCut(); 


    ////////////////////////////////////////////////////////////////
    // constructors

    public ActionCut() { super("action.cut"); }


   /**
    * This action should only be enabled if the only selection are figs.
    */

    public boolean shouldBeEnabled() { 
        Collection col = TargetManager.getInstance().getTargets();
        if (col == null) return false;
        Iterator it = col.iterator();
        boolean returnvalue = true;
        while (it.hasNext()) {
            if (!(it.next() instanceof Fig)) {
                 returnvalue = false;
                 break; 
            } 
        }
        return returnvalue;
    }

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
        CmdCut cmd = new CmdCut();
        cmd.doIt();
        super.actionPerformed(e);
    }

} /* end class ActionCut */
