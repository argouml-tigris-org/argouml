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

package org.argouml.uml.diagram.ui;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.beans.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import javax.swing.text.*;

import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.foundation.data_types.*;
import ru.novosoft.uml.foundation.extension_mechanisms.*;
import ru.novosoft.uml.model_management.*;

import org.tigris.gef.presentation.FigText;

import org.argouml.ui.*;
import org.argouml.ui.targetmanager.TargetEvent;
import org.argouml.uml.ui.*;

public class PropPanelString extends TabSpawnable implements TabModelTarget, PropertyChangeListener, DocumentListener {
    ////////////////////////////////////////////////////////////////
    // instance vars
    FigText _target;
    JLabel _nameLabel = new JLabel("Text: ");
    JTextField _nameField = new JTextField();

    ////////////////////////////////////////////////////////////////
    // constructors

    public PropPanelString() {
	super("String");
	GridBagLayout gb = new GridBagLayout();
	setLayout(gb);
	GridBagConstraints c = new GridBagConstraints();
	c.fill = GridBagConstraints.BOTH;
	c.weightx = 0.0;
	c.ipadx = 3; c.ipady = 3;

	c.gridx = 0;
	c.gridwidth = 1;
	c.gridy = 0;
	gb.setConstraints(_nameLabel, c);
	add(_nameLabel);

	c.weightx = 1.0;
	c.gridx = 1;
	c.gridwidth = GridBagConstraints.REMAINDER;
	c.gridheight = GridBagConstraints.REMAINDER;
	c.gridy = 0;
	gb.setConstraints(_nameField, c);
	add(_nameField);

	_nameField.getDocument().addDocumentListener(this);
	_nameField.setEditable(true);
	// TODO: set font?

    }

    ////////////////////////////////////////////////////////////////
    // accessors

    public void setTarget(Object t) {
	if (t instanceof FigText) {
	    _target = (FigText) t;
	    _target.removePropertyChangeListener(this); // to circumvent to much registred listeners
	    _target.addPropertyChangeListener(this);
	}
   
    }

    public Object getTarget() { return _target; }

    public void refresh() { setTarget(_target); }

    public boolean shouldBeEnabled(Object target) { return false; }


    protected void setTargetName() {
    }

    ////////////////////////////////////////////////////////////////
    // event handling

    public void insertUpdate(DocumentEvent e) {
	if (e.getDocument() == _nameField.getDocument() && _target != null) {
	    _target.setText(_nameField.getText());
	    _target.damage();
	}
    }

    public void removeUpdate(DocumentEvent e) { insertUpdate(e); }

    public void changedUpdate(DocumentEvent e) {
    }

    /**
     * @see java.beans.PropertyChangeListener#propertyChange(PropertyChangeEvent)
     */
    public void propertyChange(PropertyChangeEvent evt) {
	if (evt.getPropertyName().equals("editing") && evt.getNewValue().equals(Boolean.FALSE)) { // ending editing
	    _nameField.setText(_target.getText());
	}
			
    }

    /* (non-Javadoc)
     * @see org.argouml.ui.targetmanager.TargetListener#targetAdded(org.argouml.ui.targetmanager.TargetEvent)
     */
    public void targetAdded(TargetEvent e) {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see org.argouml.ui.targetmanager.TargetListener#targetRemoved(org.argouml.ui.targetmanager.TargetEvent)
     */
    public void targetRemoved(TargetEvent e) {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see org.argouml.ui.targetmanager.TargetListener#targetSet(org.argouml.ui.targetmanager.TargetEvent)
     */
    public void targetSet(TargetEvent e) {
        // TODO Auto-generated method stub

    }

} /* end class PropPanelString */
