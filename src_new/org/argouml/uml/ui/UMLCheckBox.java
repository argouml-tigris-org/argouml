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

package org.argouml.uml.ui;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyVetoException;

import javax.swing.JCheckBox;

import org.argouml.ui.ProjectBrowser;

import ru.novosoft.uml.MElementEvent;

/**
 * @deprecated as of ArgoUml 0.13.5 (10-may-2003),
 *             replaced by {@link org.argouml.uml.ui.UMLCheckBox2},
 *             this class is part of the 'old'(pre 0.13.*) implementation of proppanels
 *             that used reflection a lot.
 */
public class UMLCheckBox extends JCheckBox implements ItemListener, UMLUserInterfaceComponent {

    private UMLUserInterfaceContainer _container;
    private UMLBooleanProperty _property;
    
    /** Creates new BooleanChangeListener */
    public UMLCheckBox(String label, UMLUserInterfaceContainer container, UMLBooleanProperty property) {
        super(label);
        _container = container;
        _property = property;
        addItemListener(this);
        update();
    }

    public void itemStateChanged(final ItemEvent event) {
    	try {
	    _property.setProperty(_container.getTarget(), event.getStateChange() == ItemEvent.SELECTED);
    	}
    	catch (PropertyVetoException ve) {
	    ProjectBrowser.getInstance().getStatusBar().showStatus(ve.getMessage());
    	}
    	update();
        
    }

    public void targetChanged() {
        update();
    }

    public void targetReasserted() {
    }
    
    public void roleAdded(final MElementEvent p1) {
    }
    public void recovered(final MElementEvent p1) {
    }
    public void roleRemoved(final MElementEvent p1) {
    }
    public void listRoleItemSet(final MElementEvent p1) {
    }
    public void removed(final MElementEvent p1) {
    }
    public void propertySet(final MElementEvent event) {
        if (_property.isAffected(event))
            update();
    }
    
    private void update() {
        boolean oldState = isSelected();
        boolean newState = _property.getProperty(_container.getTarget());
        if (newState && oldState != newState) {
            setSelected(newState);
        }
        // clear out the residual garbage.
        if (!newState && oldState) {
            setSelected(false);
        }
    }
}
