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

import org.apache.log4j.Category;
import java.lang.reflect.*;
import ru.novosoft.uml.*;
import ru.novosoft.uml.foundation.core.*;
import java.awt.event.*;
import java.awt.*;


/**
 *   This class extends JMenuItem to invoke a method upon selection.
 *   The method must have the form of "void method(MModelElement );".
 *
 * @deprecated As of ArgoUml version 0.13.5, 
 * This class is not used and probably shouldn't be in the future either.
 *
 *   @author Curt Arnold
 */
public class UMLTreeMenuItem extends JMenuItem implements ActionListener {
            protected static Category cat = Category.getInstance(UMLTreeMenuItem.class);

    private Object _actionObj;
    private MModelElement _element;
    private Method _action;
    private boolean _requiresElement;
    
    /**
     *   Creates a new menu item.
     *   @param caption Caption for menu item.
     *   @param actionObj object on which method will be invoked.
     *   @param action name of method.
     *   @param index integer value passed to method, typically position in list.
     */
    public UMLTreeMenuItem(String caption, Object actionObj,
        String action,
        boolean requiresElement) {
        super(caption);
        _actionObj = actionObj;
        _requiresElement = requiresElement;

        //
        //  it would be a little more efficient to resolve the
        //     action only when the popup was invoked, however
        //     this will identify bad "actions" more readily
        try {
            _action = _actionObj.getClass().
                    getMethod(action,new Class[] { MModelElement.class });
        }
        catch(Exception e) {
            cat.error("Exception in " + _action + " popup.", e);
            setEnabled(false);
        }
        
        addActionListener(this);
    }

    /**
     *   This method is invoked when the menu item is selected.
     *   @param event
     */
    public void actionPerformed(final java.awt.event.ActionEvent event) {
        try {
             Object[] argValue = { _element };
            _action.invoke(_actionObj,argValue);
        }
        catch(Exception e) {
            cat.error(e.toString() + " in UMLListMenuItem.actionPerformed()", e);
        }
    }
    
    public void setModelElement(MModelElement element) {
        _element = element;
        if(_element == null && _requiresElement) {
            setEnabled(false);
        }
    }
}