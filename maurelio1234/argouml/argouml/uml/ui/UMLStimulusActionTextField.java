// $Id: UMLStimulusActionTextField.java 11516 2006-11-25 04:30:15Z tfmorris $
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

package org.argouml.uml.ui;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * TODO: this class should be moved to package
 * org.argouml.uml.ui.behavior.common_behavior
 */
public class UMLStimulusActionTextField extends JTextField
    implements DocumentListener, UMLUserInterfaceComponent, 
    PropertyChangeListener {

    private UMLUserInterfaceContainer theContainer;
    private UMLStimulusActionTextProperty theProperty;

    /**
     * Creates new BooleanChangeListener.
     *
     * @param container the container of UML user interface components
     * @param property the property
     */
    public UMLStimulusActionTextField(UMLUserInterfaceContainer container,
            UMLStimulusActionTextProperty property) {
        theContainer = container;
        theProperty = property;
        getDocument().addDocumentListener(this);
        update();
    }

    /*
     * @see org.argouml.uml.ui.UMLUserInterfaceComponent#targetChanged()
     */
    public void targetChanged() {
        theProperty.targetChanged();
        update();
    }

    /*
     * @see org.argouml.uml.ui.UMLUserInterfaceComponent#targetReasserted()
     */
    public void targetReasserted() {
    }

    public void propertyChange(PropertyChangeEvent event) {
        if (theProperty.isAffected(event)) {
            //
            //   check the possibility that this is a promiscuous event
            Object eventSource = event.getSource();
            Object target = theContainer.getTarget();
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

        String newText = theProperty.getProperty(theContainer);

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

        theProperty.setProperty(theContainer, getText());
    }

    /*
     * @see javax.swing.event.DocumentListener#removeUpdate(javax.swing.event.DocumentEvent)
     */
    public void removeUpdate(final DocumentEvent p1) {

        theProperty.setProperty(theContainer, getText());
    }

    /*
     * @see javax.swing.event.DocumentListener#insertUpdate(javax.swing.event.DocumentEvent)
     */
    public void insertUpdate(final DocumentEvent p1) {


        theProperty.setProperty(theContainer, getText());

    }





}
