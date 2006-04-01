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

package org.argouml.uml.ui;

import java.awt.event.ActionEvent;

import javax.swing.Action;
import javax.swing.JComboBox;

import org.argouml.ui.LookAndFeelMgr;
import org.argouml.ui.targetmanager.TargetEvent;
import org.argouml.ui.targetmanager.TargetListener;
import org.argouml.ui.targetmanager.TargettableModelView;


/**
 * ComboBox for UML modelelements. <p>
 *
 * This implementation does not use
 * reflection and seperates Model, View and Controller better then does
 * UMLComboBox. The ancient UMLComboBoxModel and UMLComboBox are
 * replaced with this implementation to improve performance.
 */
public class UMLComboBox2
    extends JComboBox implements TargettableModelView, TargetListener {

    /**
     * Constructor for UMLComboBox2.
     * @deprecated As of ArgoUml version unknown (before 0.13.5),
     * replaced by {@link #UMLComboBox2(UMLComboBoxModel2, Action, boolean)}
     * @param arg0 the ComboBoxModel
     */
    protected UMLComboBox2(UMLComboBoxModel2 arg0) {
        super(arg0);
        setFont(LookAndFeelMgr.getInstance().getSmallFont());
        addActionListener(this);
    }

    /**
     * Constructor for UMLComboBox2. Via the given action, the
     * action for this combobox is done.
     * @param arg0 the ComboBoxModel
     * @param action the action
     * @param showIcon true if an icon should be shown in front of the items
     */
    public UMLComboBox2(UMLComboBoxModel2 arg0, Action action,
			boolean showIcon) {
        super(arg0);
        setFont(LookAndFeelMgr.getInstance().getSmallFont());
        addActionListener(action);
        // setDoubleBuffered(true);
        setRenderer(new UMLListCellRenderer2(showIcon));
    }

    /**
     * The constructor.
     *
     * @param arg0 the ComboBoxModel
     * @param action the action
     */
    public UMLComboBox2(UMLComboBoxModel2 arg0, Action action) {
        this(arg0, action, true);
    }

    /**
     * @see java.awt.event.ActionListener#actionPerformed(ActionEvent)
     */
    public void actionPerformed(ActionEvent arg0) {
        int i = getSelectedIndex();
        if (i >= 0) {
            doIt(arg0);
        }
    }

    /**
     * The 'body' of the actionPerformed method. Is only called if there is
     * actually a selection made.
     *
     * @param event the event
     */
    protected void doIt(ActionEvent event) { }

    /**
     * Utility method to get the current target.
     *
     * @return Object
     */
    public Object getTarget() {
        return ((UMLComboBoxModel2) getModel()).getTarget();
    }


    /**
     * @see TargettableModelView#getTargettableModel()
     */
    public TargetListener getTargettableModel() {
        return (TargetListener) getModel();
    }

    /**
     * @see org.argouml.ui.targetmanager.TargetListener#targetAdded(org.argouml.ui.targetmanager.TargetEvent)
     */
    public void targetAdded(TargetEvent e) {
        if (e.getNewTarget() != getTarget()) {
            removeActionListener(this);
        }
    }

    /**
     * @see org.argouml.ui.targetmanager.TargetListener#targetRemoved(org.argouml.ui.targetmanager.TargetEvent)
     */
    public void targetRemoved(TargetEvent e) {
        removeActionListener(this);
    }

    /**
     * @see org.argouml.ui.targetmanager.TargetListener#targetSet(org.argouml.ui.targetmanager.TargetEvent)
     */
    public void targetSet(TargetEvent e) {
        addActionListener(this);
    }
}
