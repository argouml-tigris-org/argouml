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
import javax.swing.event.*;
import javax.swing.*;
import java.lang.reflect.*;
import java.awt.event.*;
import java.awt.*;
import ru.novosoft.uml.MElementEvent;

/**
 * @deprecated as of ArgoUml 0.13.5 (10-may-2003),
 *             replaced by {@link org.argouml.uml.ui.UMLComboBox2},
 *             this class is part of the 'old'(pre 0.13.*) implementation of proppanels
 *             that used reflection a lot.
 */
public class UMLComboBox
    extends JComboBox
    implements UMLUserInterfaceComponent 
{

    private UMLComboBoxModel _model;

    public UMLComboBox(UMLComboBoxModel model) {
        super(model);
        _model = model;
	addActionListener(_model);
    }

    public void setModel(ComboBoxModel newModel) {
        ComboBoxModel oldModel = getModel();
        if (oldModel != null) {
            if (oldModel instanceof ActionListener) {
                removeActionListener((ActionListener) oldModel);
            }
        }
        if (newModel instanceof ActionListener) {
            addActionListener((ActionListener) newModel);
        }
        super.setModel(newModel);
    }


    public void targetChanged() {
        _model.targetChanged();
        // updateUI();
    }

    public void targetReasserted() {
    }

    public void roleAdded(final MElementEvent event) {
        _model.roleAdded(event);
        // updateUI();
    }

    public void recovered(final MElementEvent event) {
        _model.recovered(event);
        // updateUI();
    }

    public void roleRemoved(final MElementEvent event) {
        _model.roleRemoved(event);
        // updateUI();
    }

    public void listRoleItemSet(final MElementEvent event) {
        _model.listRoleItemSet(event);
        // updateUI();
    }

    public void removed(final MElementEvent event) {
        _model.removed(event);
        // updateUI();
    }
    public void propertySet(final MElementEvent event) {
        _model.propertySet(event);
        // updateUI();
    }


}
