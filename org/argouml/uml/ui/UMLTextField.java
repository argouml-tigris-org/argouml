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
import java.text.*;
import javax.swing.event.*;
import javax.swing.*;
import java.lang.reflect.*;
import ru.novosoft.uml.*;
import ru.novosoft.uml.foundation.core.*; //--pjs-- added for event.

public class UMLTextField extends JTextField implements DocumentListener, UMLUserInterfaceComponent {

    private UMLUserInterfaceContainer _container;
    private UMLTextProperty _property;
    
    /** Creates new BooleanChangeListener */
    public UMLTextField(UMLUserInterfaceContainer container,UMLTextProperty property) {
        _container = container;
        _property = property;
        getDocument().addDocumentListener(this);
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
        if(_property.isAffected(event)) {
            //
            //   check the possibility that this is a promiscuous event
            Object eventSource = event.getSource();
            Object target = _container.getTarget();
            //
            //    if event source is unknown or 
            //       the event source is the container's target
            //          then update the field
            if(eventSource == null || eventSource == target) {
                update();
            }
        }
    }
    
/** update() updates both the Collection (by setText()) and the drawing (using 
 *  the if statements and code blocks). The code forces FigClass to update the
 *  drawing as information is typed into the text boxes in the property panes.
 *  @author modified by psager@tigris.org Aug. 27, 2001 */    
    private void update() {
        String oldText = getText();
        String newText = _property.getProperty(_container);
        if(oldText == null || newText == null || !oldText.equals(newText)) {
            if(oldText != newText) {
                setText(newText);
            }
        }

        Object target = _container.getTarget();
        
        if (target instanceof MClassifier){
            MClassifier classifier = (MClassifier) target;
            classifier.setFeatures(classifier.getFeatures());
        }
        else if (target instanceof MOperation){
            MClassifier classifier = (MClassifier) ((MOperation)target).getOwner();
            classifier.setFeatures(classifier.getFeatures());
        }
        else if (target instanceof MAttribute){
            MClassifier classifier = (MClassifier) ((MAttribute)target).getOwner();
            classifier.setFeatures(classifier.getFeatures());
        }
        else if (target instanceof MParameter){
            MBehavioralFeature feature = ((MParameter) target).getBehavioralFeature();
            MClassifier classifier = (MClassifier) feature.getOwner();
            classifier.setFeatures(classifier.getFeatures());
        }  
    }

    
    public void changedUpdate(final DocumentEvent p1) {
        _property.setProperty(_container,getText());
    }
    public void removeUpdate(final DocumentEvent p1) {
        _property.setProperty(_container,getText());
    }
    public void insertUpdate(final DocumentEvent p1) {
        _property.setProperty(_container,getText());
    }
    
} //...end of class UMLTextField...