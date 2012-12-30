/* $Id$
 *****************************************************************************
 * Copyright (c) 2009-2012 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    tfmorris
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

// Copyright (c) 2004-2007 The Regents of the University of California. All
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

import java.beans.PropertyVetoException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;

import org.argouml.ui.targetmanager.TargetEvent;
import org.argouml.ui.targetmanager.TargetListener;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.diagram.ArgoDiagram;

/**
 * This is the model for the diagram name text box (JTextField)
 * shown on the property panel of a Diagram. <p>
 *
 * It handles changes by the user in the text-entry field,
 * by updating the name of the diagram.
 * And it handles target changes (i.e. when the user selects another diagram)
 * by updating the name shown in the namefield.
 *
 * @author Michiel
 */
class DiagramNameDocument implements DocumentListener, TargetListener {

    private static final Logger LOG =
        Logger.getLogger(DiagramNameDocument.class.getName());

    private JTextField field;
    private boolean stopEvents = false;

    private Object highlightTag = null;

    /**
     * The constructor.
     * @param theField the input text field
     */
    public DiagramNameDocument(JTextField theField) {
        field = theField;
        TargetManager tm = TargetManager.getInstance();
        tm.addTargetListener(this);
        setTarget(tm.getTarget());
    }

    /**
     * If the currently selected object is a diagram,
     * then update the name-field. <p>
     *
     * MVW: I added the stopEvents mechanism, because otherwise
     * updating the field causes the UML model to be adapted!
     *
     * @param t the currently selected object
     */
    private void setTarget(Object t) {
        if (t instanceof ArgoDiagram) {
            stopEvents = true;
            field.setText(((ArgoDiagram) t).getName());
            stopEvents = false;
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

    /*
     * @see javax.swing.event.DocumentListener#insertUpdate(javax.swing.event.DocumentEvent)
     */
    public void insertUpdate(DocumentEvent e) {
        update(e);
    }

    /*
     * @see javax.swing.event.DocumentListener#removeUpdate(javax.swing.event.DocumentEvent)
     */
    public void removeUpdate(DocumentEvent e) {
        update(e);
    }

    /*
     * @see javax.swing.event.DocumentListener#changedUpdate(javax.swing.event.DocumentEvent)
     */
    public void changedUpdate(DocumentEvent e) {
        update(e);
    }

    /**
     * If a new name has been typed by the user, then
     * let's update the name of the diagram.
     *
     * @param e the documentevent from the Documentlistener interface
     */
    private void update(DocumentEvent e) {
        if (!stopEvents) {
            Object target = TargetManager.getInstance().getTarget();
            if (target instanceof ArgoDiagram) {
                ArgoDiagram d = (ArgoDiagram) target;
                try {
                    int documentLength = e.getDocument().getLength();
                    String newName = e.getDocument().getText(0, documentLength);
                    String oldName = d.getName();
                    /* Prevent triggering too many events by setName(). */
                    if (!oldName.equals(newName)) {
                        d.setName(newName);
                        if (highlightTag != null) {
                            field.getHighlighter()
                                    .removeHighlight(highlightTag);
                            highlightTag = null;
                        }
                    }
                } catch (PropertyVetoException pe) {
                    // Provide feedback to the user that their name was
                    // not accepted
                    try {
                        highlightTag  = field.getHighlighter().addHighlight(0,
                                field.getText().length(),
                                DefaultHighlighter.DefaultPainter);
                    } catch (BadLocationException e1) {
                        LOG.log(Level.FINE, "Nested exception", e1);
                    }
                } catch (BadLocationException ble) {
                    LOG.log(Level.FINE, "update caused: ",ble);
                }
            }
        }
    }

}
