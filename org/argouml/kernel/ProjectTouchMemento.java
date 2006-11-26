// $Id: eclipse-argo-codetemplates.xml 10612 2006-05-25 12:58:04Z linus $
// Copyright (c) 2006 The Regents of the University of California. All
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

package org.argouml.kernel;

import org.tigris.gef.undo.Memento;

/**
 * This Memento is intended to be used by Actions that 
 * touch the Project, i.e. they enable the Save Flag when executed.
 * So, this memento takes care of resetting the save flag 
 * when the action is undone. <p>
 * 
 * @constraint 
 * This Memento only works good if you call the redo method 
 * when executing the Action the first time.
 * I.e. the redo method initializes a member that is needed for the undo.
 *
 * @author Michiel
 */
public class ProjectTouchMemento extends Memento {

    /**
     * the previous save flag state
     */
    private boolean isSaveEnabled;
    private boolean initialized = false;

    /**
     * @see org.tigris.gef.undo.Memento#redo()
     */
    public void redo() {
        initialized = true;
        isSaveEnabled = ProjectManager.getManager().isSaveActionEnabled();
        ProjectManager.getManager().setSaveEnabled(true);
    }

    /**
     * @see org.tigris.gef.undo.Memento#undo()
     */
    public void undo() {
        assert initialized;
        ProjectManager.getManager().setSaveEnabled(isSaveEnabled);
    }

}
