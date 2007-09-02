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

package org.argouml.ui;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.AbstractAction;
import javax.swing.Icon;

import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;

/**
 * An action to redo from the undo stack.
 * 
 * @author mvw@tigris.org
 */
public class ActionRedo extends AbstractAction
        implements PropertyChangeListener {
    
    private static final long serialVersionUID = 3921952827170089931L;

    /**
     * Construct the redo action with a display name
     * @param name the name to display for this action
     */
    public ActionRedo(String name) {
        super(name);
        final Project p = ProjectManager.getManager().getCurrentProject();
        p.getUndoManager().addPropertyChangeListener(this);
        setEnabled(false);
    }
    
    /**
     * Construct the redo action with a display name and icon.
     * @param name the name to display for this action
     * @param icon the icon to display for this action
     */
    public ActionRedo(String name, Icon icon) {
        super(name, icon);
        final Project p = ProjectManager.getManager().getCurrentProject();
        p.getUndoManager().addPropertyChangeListener(this);
        setEnabled(false);
    }

    public void actionPerformed(ActionEvent e) {
        final Project p = ProjectManager.getManager().getCurrentProject();
        p.getUndoManager().redo();
    }
    
    /**
     * Listens for property change events to determine when redo changes
     * availability
     * @param event the event
     * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
     */
    public void propertyChange(PropertyChangeEvent event) {
        if ("canRedo".equals(event.getPropertyName())) {
            setEnabled ("true".equals(event.getNewValue()));
        }
    }
}
