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

import org.apache.log4j.Category;
import java.lang.reflect.*;
import ru.novosoft.uml.*;

public class UMLExpressionBodyField extends JTextArea implements DocumentListener, UMLUserInterfaceComponent {
    protected static Category cat = 
        Category.getInstance(UMLExpressionBodyField.class);

    private UMLExpressionModel _model;
    private boolean _notifyModel;
    
    public UMLExpressionBodyField(UMLExpressionModel model,boolean notifyModel) {
        _model = model;
        _notifyModel = notifyModel;
        getDocument().addDocumentListener(this);       
    }

    public void targetChanged() {
	cat.debug("UMLExpressionBodyField: targetChanged");
	if(_notifyModel) _model.targetChanged();
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
       	cat.debug("UMLExpressionBodyField: propertySet"+event);
    }
    
    private void update() {
        String oldText = getText();
        String newText = _model.getBody();
	cat.debug("UMLExpressionBodyField: update: "+oldText+" "+newText);

	if(oldText == null || newText == null || !oldText.equals(newText)) {
            if(oldText != newText) {
		cat.debug("setNewText!!");
                setText(newText);
            }
        }
    }
    public void changedUpdate(final DocumentEvent p1) {
        _model.setBody(getText());
    }
    public void removeUpdate(final DocumentEvent p1) {
        _model.setBody(getText());
    }
    public void insertUpdate(final DocumentEvent p1) {
        _model.setBody(getText());
    }
}
