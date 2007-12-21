// $Id$
// Copyright (c) 1996-2006 The Regents of the University of California. All
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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.argouml.application.api.AbstractArgoJPanel;
import org.argouml.i18n.Translator;
import org.argouml.ui.TabModelTarget;
import org.argouml.ui.targetmanager.TargetEvent;

import org.tigris.gef.presentation.FigText;

/**
 * The properties panel for a simple text / string.
 *
 */
public class PropPanelString
    extends AbstractArgoJPanel
    implements TabModelTarget, PropertyChangeListener, DocumentListener {

    private FigText target;
    private JLabel nameLabel = new JLabel(Translator.localize("label.text"));
    private JTextField nameField = new JTextField();

    /**
     * The constructor.
     *
     */
    public PropPanelString() {
	super(Translator.localize("tab.string"));
	GridBagLayout gb = new GridBagLayout();
	setLayout(gb);
	GridBagConstraints c = new GridBagConstraints();
	c.fill = GridBagConstraints.BOTH;
	c.weightx = 0.0;
	c.ipadx = 3; c.ipady = 3;

	c.gridx = 0;
	c.gridwidth = 1;
	c.gridy = 0;
	gb.setConstraints(nameLabel, c);
	add(nameLabel);

	c.weightx = 1.0;
	c.gridx = 1;
	c.gridwidth = GridBagConstraints.REMAINDER;
	c.gridheight = GridBagConstraints.REMAINDER;
	c.gridy = 0;
	gb.setConstraints(nameField, c);
	add(nameField);

	nameField.getDocument().addDocumentListener(this);
	nameField.setEditable(true);
	// TODO: set font?

    }


    /*
     * @see org.argouml.ui.TabTarget#setTarget(java.lang.Object)
     */
    public void setTarget(Object t) {
        if (target != null) {
            target.removePropertyChangeListener(this);
        }
	if (t instanceof FigText) {
	    target = (FigText) t;
	    // to circumvent too many registered listeners
	    target.removePropertyChangeListener(this);
	    if (isVisible()) {
	        target.addPropertyChangeListener(this);
	    }
	}

    }

    /*
     * @see org.argouml.ui.TabTarget#getTarget()
     */
    public Object getTarget() {
        return target;
    }

    /*
     * @see org.argouml.ui.TabTarget#refresh()
     */
    public void refresh() {
        setTarget(target);
    }

    /*
     * @see org.argouml.ui.TabTarget#shouldBeEnabled(java.lang.Object)
     */
    public boolean shouldBeEnabled(Object theTarget) { return false; }


    /**
     * Set the target name.
     */
    protected void setTargetName() {
    }

    ////////////////////////////////////////////////////////////////
    // event handling

    /*
     * @see javax.swing.event.DocumentListener#insertUpdate(javax.swing.event.DocumentEvent)
     */
    public void insertUpdate(DocumentEvent e) {
	if (e.getDocument() == nameField.getDocument() && target != null) {
	    target.setText(nameField.getText());
	    target.damage();
	}
    }

    /*
     * @see javax.swing.event.DocumentListener#removeUpdate(javax.swing.event.DocumentEvent)
     */
    public void removeUpdate(DocumentEvent e) {
        insertUpdate(e);
    }

    /*
     * @see javax.swing.event.DocumentListener#changedUpdate(javax.swing.event.DocumentEvent)
     */
    public void changedUpdate(DocumentEvent e) {
    }

    /*
     * @see java.beans.PropertyChangeListener#propertyChange(PropertyChangeEvent)
     */
    public void propertyChange(PropertyChangeEvent evt) {
	if (evt.getPropertyName().equals("editing")
	    && evt.getNewValue().equals(Boolean.FALSE)) {
	    // ending editing
	    nameField.setText(target.getText());
	}

    }

    /*
     * @see org.argouml.ui.targetmanager.TargetListener#targetAdded(org.argouml.ui.targetmanager.TargetEvent)
     */
    public void targetAdded(TargetEvent e) {
        setTarget(e.getNewTarget());
    }

    /*
     * @see org.argouml.ui.targetmanager.TargetListener#targetRemoved(org.argouml.ui.targetmanager.TargetEvent)
     */
    public void targetRemoved(TargetEvent e) {
        setTarget(e.getNewTarget());
    }

    /*
     * @see org.argouml.ui.targetmanager.TargetListener#targetSet(org.argouml.ui.targetmanager.TargetEvent)
     */
    public void targetSet(TargetEvent e) {
        setTarget(e.getNewTarget());
    }

}
