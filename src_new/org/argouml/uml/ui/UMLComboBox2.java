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

import java.awt.event.ActionEvent;

import javax.swing.JComboBox;

import org.argouml.ui.targetmanager.TargetListener;
import org.argouml.ui.targetmanager.TargettableModelView;

/**
 * ComboBox for UML modelelements. This implementation does not use 
 * reflection and seperates Model, View and Controller better then does
 * UMLComboBox. In the future UMLComboBoxModel and UMLComboBox will be
 * replaced with this implementation to improve performance.
 */
public class UMLComboBox2
    extends JComboBox implements TargettableModelView {       
    
    /**
     * Constructor for UMLMessageActivatorComboBox.
     * @deprecated As of ArgoUml version unknown (before 0.13.5),
     *             replaced by {@link #UMLComboBox2(UMLComboBoxModel2 , UMLAction, boolean )}
     * @param arg0
     */
    protected UMLComboBox2(UMLComboBoxModel2 arg0) {
        super(arg0);
        addActionListener(this);

    }
    
    /**
     * Constructor for UMLMessageActivatorComboBox. Via the given action, the 
     * action for this combobox is done.
     * @param container
     * @param arg0
     * @param action
     */
    public UMLComboBox2(UMLComboBoxModel2 arg0, UMLAction action, boolean showIcon) {
        super(arg0);
        addActionListener(action);
        // setDoubleBuffered(true);
        setRenderer(new UMLListCellRenderer2(showIcon));      
    }
           
    public UMLComboBox2(UMLComboBoxModel2 arg0, UMLAction action) {
        this(arg0, action, false);
    }

    /**
     * @see java.awt.event.ActionListener#actionPerformed(ActionEvent)
     */
    public void actionPerformed(ActionEvent arg0) {        
        int i = getSelectedIndex();
        if ( i >= 0) {
            doIt(arg0);
        }
    }
    
    /**
     * The 'body' of the actionPerformed method. Is only called if there is
     * actually a selection made.
     * @param event
     */
    protected void doIt(ActionEvent event) { }
    
    /**
     * Utility method to get the current target
     * @return Object
     */
    public Object getTarget() {
        return ((UMLComboBoxModel2) getModel()).getTarget();
    }
    
    
    /**
     * @see org.argouml.ui.targetmanager.TargettableModelView#getTargettableModel()
     */
    public TargetListener getTargettableModel() {
        return (TargetListener) getModel();
    }

}
