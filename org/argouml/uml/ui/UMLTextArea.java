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

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.argouml.ui.ProjectBrowser;

import ru.novosoft.uml.MElementEvent;

/**
 * @deprecated as of ArgoUml 0.13.5 (10-may-2003),
 *             replaced by {@link org.argouml.uml.ui.UMLTextArea2},
 *             this class is part of the 'old'(pre 0.13.*) implementation of proppanels
 *             that used reflection a lot.
 */
public class UMLTextArea
    extends JTextArea
    implements DocumentListener, UMLUserInterfaceComponent, FocusListener {

    private UMLUserInterfaceContainer _container;

    private UMLTextProperty _property;
    private int counter = 0;

    public static final int MAX_KEY_STROKES = 10;

    /**
     * Creates a new UMLTextArea
     * @param container
     * @param property
     */
    public UMLTextArea(
        UMLUserInterfaceContainer container,
        UMLTextProperty property) {

        _container = container;

        _property = property;

        getDocument().addDocumentListener(this);
        addFocusListener(this);
        update();
    }

    public void targetChanged() {
        _property.targetChanged();
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
        if (_property.isAffected(event)) {
            //
            //   check the possibility that this is a promiscuous event
            Object eventSource = event.getSource();
            Object target = _container.getTarget();
            //
            //    if event source is unknown or 
            //       the event source is the container's target
            //          then update the field
            if (eventSource == null || eventSource == target) {
                update();
            }
        }
    }

    private void update() {
        String oldText = getText();
        String newText = _property.getProperty(_container);
        if (oldText == null || newText == null || !oldText.equals(newText)) {
            setText(newText);
        }
    }

    public void changedUpdate(final DocumentEvent p1) {
        handleDocEvent();
    }

    public void removeUpdate(final DocumentEvent p1) {
        handleDocEvent();
    }

    public void insertUpdate(final DocumentEvent p1) {
        handleDocEvent();
    }

    protected void handleDocEvent() {
        counter++; // to prevent continuously setting the property
        if (counter >= MAX_KEY_STROKES) {
            changeProperty();
            counter = 0;
        }
    }

    protected void changeProperty() {
        try {
            _property.setProperty(_container, getText());
        }
        catch (Exception ex) {
            String message = ex.getMessage();
            // cant show the messagebox in this container
            JOptionPane.showMessageDialog(ProjectBrowser.getInstance(),
					  message,
					  "error",
					  JOptionPane.ERROR_MESSAGE);
        }
        update();
    }

    /**
     * @see java.awt.event.FocusListener#focusGained(FocusEvent)
     */
    public void focusGained(FocusEvent arg0) {
    	counter = 0;
    }
    /**
     * @see java.awt.event.FocusListener#focusLost(FocusEvent)
     */
    public void focusLost(FocusEvent arg0) {
    	changeProperty();
    }
}
