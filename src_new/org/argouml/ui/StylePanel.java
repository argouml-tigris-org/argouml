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

package org.argouml.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.util.Collection;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.log4j.Category;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.ModelFacade;
import org.argouml.ui.targetmanager.TargetEvent;
import org.tigris.gef.presentation.Fig;

public class StylePanel
    extends TabSpawnable
    implements
        TabFigTarget,
        ItemListener,
        DocumentListener,
        ListSelectionListener,
        ActionListener {
    protected static Category cat = Category.getInstance(StylePanel.class);
    ////////////////////////////////////////////////////////////////
    // instance vars
    protected Fig _target;

    /**
     * This method must be overriden by implementors if they don't
     * want to refresh the whole stylepanel every time a property
     * change events is fired.
     * @since 8 june 2003, 0.13.6
     * @see org.argouml.ui.TabTarget#refresh(java.beans.PropertyChangeEvent)
     */
    public void refresh(PropertyChangeEvent e) {
        refresh();

    }

    ////////////////////////////////////////////////////////////////
    // constructors

    public StylePanel(String title) {
        super(title);
        GridBagLayout gb = new GridBagLayout();
        setLayout(gb);
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 0.0;
        c.weighty = 0.0;
        c.ipadx = 3;
        c.ipady = 3;
    }

    ////////////////////////////////////////////////////////////////
    // accessors
    
    /**     
     * @deprecated settarget will change visibility in the near future (from version 0.14). 
     * Implementation will change since the way a Fig is selected is very dangerous (can easily 
     * select the wrong fig)
     */
    public void setTarget(Object t) {
        if (!(t instanceof Fig)) {
            if (ModelFacade.isABase(t)) {
                Project p = ProjectManager.getManager().getCurrentProject();
                Collection col = p.findFigsForMember(t);
                if (col == null || col.isEmpty()) {
                    return;
                } else {
                    t = col.iterator().next();
                    if (!(t instanceof Fig)) return;
                }
            } else {
                return;
            }

        }
        _target = (Fig) t;
        refresh();
    }

    /**
     * @deprecated will be replaced by targetmanager.gettarget
     * @see org.argouml.ui.TabTarget#getTarget()
     */
    public Object getTarget() {
        return _target;
    }

    public void refresh() {
        //_tableModel.setTarget(_target);
        //_table.setModel(_tableModel);
    }

    /**
     * style panels ony apply when a Fig is selected.
     */
    public boolean shouldBeEnabled(Object target) {
        ArgoDiagram diagram =
            ProjectManager.getManager().getCurrentProject().getActiveDiagram();
        target =
            (target instanceof Fig) ? target : diagram.getContainingFig(target);
        return (target instanceof Fig);
    }

    ////////////////////////////////////////////////////////////////
    // actions

    ////////////////////////////////////////////////////////////////
    // document event handling

    public void insertUpdate(DocumentEvent e) {
        cat.debug(getClass().getName() + " insert");
    }

    public void removeUpdate(DocumentEvent e) {
        insertUpdate(e);
    }

    public void changedUpdate(DocumentEvent e) {
    }

    ////////////////////////////////////////////////////////////////
    // combobox event handling

    public void itemStateChanged(ItemEvent e) {
        Object src = e.getSource();
    }

    /////////////////////////////////////////////////////////////////
    // ListSelectionListener implemention

    public void valueChanged(ListSelectionEvent lse) {
    }

    /////////////////////////////////////////////////////////////////
    // ActionListener implementation

    public void actionPerformed(ActionEvent ae) {
        Object src = ae.getSource();
        //if (src == _config) doConfig();
    }

    /**
     * @see
     * org.argouml.ui.targetmanager.TargetListener#targetAdded(org.argouml.ui.targetmanager.TargetEvent)
     */
    public void targetAdded(TargetEvent e) {
    }

    /**
     * @see
     * org.argouml.ui.targetmanager.TargetListener#targetRemoved(org.argouml.ui.targetmanager.TargetEvent)
     */
    public void targetRemoved(TargetEvent e) {
        setTarget(e.getNewTargets()[0]);

    }

    /**
     * Postcondition: the panel should be refreshed (refresh should be called) if the panel should be enabled.
     * @see
     * org.argouml.ui.targetmanager.TargetListener#targetSet(org.argouml.ui.targetmanager.TargetEvent)
     */
    public void targetSet(TargetEvent e) {
        setTarget(e.getNewTargets()[0]);
    }

} /* end class StylePanel */
