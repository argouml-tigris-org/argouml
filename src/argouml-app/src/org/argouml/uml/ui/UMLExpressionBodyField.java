/* $Id$
 *****************************************************************************
 * Copyright (c) 2009-2012 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    mvw
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

// Copyright (c) 1996-2009 The Regents of the University of California. All
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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.argouml.i18n.Translator;
import org.argouml.ui.LookAndFeelMgr;
import org.argouml.ui.targetmanager.TargetListener;
import org.argouml.ui.targetmanager.TargettableModelView;

/**
 * This text field shows the body of a UML expression.
 */
public class UMLExpressionBodyField extends JTextArea
    implements DocumentListener, UMLUserInterfaceComponent,
    PropertyChangeListener, TargettableModelView {

    /**
     * Logger.
     */
    private static final Logger LOG =
        Logger.getLogger(UMLExpressionBodyField.class.getName());

    private UMLExpressionModel2 model;
    private boolean notifyModel;

    /**
     * The constructor.
     *
     * @param expressionModel
     *            Expression model, should be shared between Language and Body
     *            fields
     * @param notify
     *            Set to true to forward events to model. Only one of Language
     *            and Body fields should have this set to true.
     */
    public UMLExpressionBodyField(UMLExpressionModel2 expressionModel,
				  boolean notify) {
        model = expressionModel;
        notifyModel = notify;
        getDocument().addDocumentListener(this);
        setToolTipText(Translator.localize("label.body.tooltip"));
        setFont(LookAndFeelMgr.getInstance().getStandardFont());
        setRows(2); // make it stretch vertically
    }

    /*
     * @see org.argouml.uml.ui.UMLUserInterfaceComponent#targetChanged()
     */
    public void targetChanged() {
        LOG.log(Level.FINE, "UMLExpressionBodyField: targetChanged");
	if (notifyModel) {
	    model.targetChanged();
	}
        update();
    }

    /*
     * @see org.argouml.uml.ui.UMLUserInterfaceComponent#targetReasserted()
     */
    public void targetReasserted() {
    }

    /* TODO: This does not work - no event arrives. */
    public void propertyChange(PropertyChangeEvent event) {
        LOG.log(Level.FINE, "UMLExpressionBodyField: propertySet {0}", event);
        update();
    }

    private void update() {
        String oldText = getText();
        String newText = model.getBody();

        if (oldText == null || newText == null || !oldText.equals(newText)) {
            if (oldText != newText) {
                setText(newText);
            }
        }
    }

    /*
     * @see javax.swing.event.DocumentListener#changedUpdate(javax.swing.event.DocumentEvent)
     */
    public void changedUpdate(final DocumentEvent p1) {
        model.setBody(getText());
    }

    /*
     * @see javax.swing.event.DocumentListener#removeUpdate(javax.swing.event.DocumentEvent)
     */
    public void removeUpdate(final DocumentEvent p1) {
        model.setBody(getText());
    }

    /*
     * @see javax.swing.event.DocumentListener#insertUpdate(javax.swing.event.DocumentEvent)
     */
    public void insertUpdate(final DocumentEvent p1) {
        model.setBody(getText());
    }

    public TargetListener getTargettableModel() {
        return model;
    }
}
