// $Id$
// Copyright (c) 2008 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies. This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason. IN NO EVENT SHALL THE
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

package org.argouml.core.propertypanels.ui;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.argouml.ui.targetmanager.TargetEvent;
import org.argouml.ui.targetmanager.TargetListener;
import org.argouml.uml.ui.UMLExpressionModel2;

/**
 *
 * @author penyaskito
 */
public class UMLInitialValueExpressionModel 
    extends UMLExpressionModel3 {

    public UMLInitialValueExpressionModel() {
        super("initial value");
    }
    
    public UMLInitialValueExpressionModel(String name) {
        super(name);
    }

    /**
     * @return
     * @see org.argouml.uml.ui.UMLExpressionModel2#getExpression()
     */
    @Override
    public Object getExpression() {
        // TODO: Auto-generated method stub
        return null;
    }

    /**
     * @return
     * @see org.argouml.uml.ui.UMLExpressionModel2#newExpression()
     */
    @Override
    public Object newExpression() {
        // TODO: Auto-generated method stub
        return null;
    }

    /**
     * @param expr
     * @see org.argouml.uml.ui.UMLExpressionModel2#setExpression(java.lang.Object)
     */
    @Override
    public void setExpression(Object expr) {
        // TODO: Auto-generated method stub

    }

    public void targetAdded(TargetEvent e) {
        // TODO: Auto-generated method stub
        
    }

    public void targetRemoved(TargetEvent e) {
        // TODO: Auto-generated method stub
        
    }

    public void targetSet(TargetEvent e) {
        // TODO: Auto-generated method stub
        
    }

    public void propertyChange(PropertyChangeEvent evt) {
        // TODO: Auto-generated method stub
        
    }

}
