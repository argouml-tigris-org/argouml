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
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.argouml.ui.targetmanager.TargetManager;

import ru.novosoft.uml.MElementEvent;
import ru.novosoft.uml.foundation.core.MModelElement;

/**
 * 
 * @deprecated As of ArgoUml version 0.13.5, 
 * This class is not used and probably shouldn't be in the future either.
 */
public class UMLTree extends JTree implements UMLUserInterfaceComponent, 
      MouseListener, TreeSelectionListener {

    private UMLTreeModel _model;
    private boolean _navigate;
    private UMLUserInterfaceContainer _container;
    
    public UMLTree(UMLUserInterfaceContainer container,
		   UMLTreeModel model, boolean navigate) {
        super(model);
        _model = model;
        _navigate = navigate;
        _container = container;
        setRootVisible(false);
        setEditable(false);
        getSelectionModel()
	    .setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        if (navigate) {
            addMouseListener(this);
            addTreeSelectionListener(this);
        }
    }
    
    public void setModel(TreeModel model) {
        super.setModel(model);
        if (model instanceof UMLTreeModel) {
            _model = (UMLTreeModel) model;
        }
        else {
            _model = null;
            setEnabled(false);
        }
    }

    public void targetChanged() {
        if (_model != null) _model.targetChanged();
    }

    public void targetReasserted() {
    }
    
    public void roleAdded(final MElementEvent event) {
        if (_model != null) _model.roleAdded(event);
    }
    
    public void recovered(final MElementEvent event) {
        if (_model != null) _model.recovered(event);
    }
    
    public void roleRemoved(final MElementEvent event) {
        if (_model != null) _model.roleRemoved(event);
    }
    
    public void listRoleItemSet(final MElementEvent event) {
        if (_model != null) _model.listRoleItemSet(event);
    }
    
    public void removed(final MElementEvent event) {
        if (_model != null) _model.removed(event);
    }
    public void propertySet(final MElementEvent event) {
        if (_model != null) _model.propertySet(event);
    }
   
    public void mouseReleased(final MouseEvent event) {
        if (event.isPopupTrigger()) {
            showPopup(event);
        }
    }
    
    public void mouseEntered(final MouseEvent event) {
        if (event.isPopupTrigger()) {
            showPopup(event);
        }
    }

    public void mouseClicked(final MouseEvent event) {
        if (event.isPopupTrigger()) {
            showPopup(event);
        }            
    }
    
    public void mousePressed(final MouseEvent event) {
        if (event.isPopupTrigger()) {
            showPopup(event);
        }
    }

    public void mouseExited(final MouseEvent event) {
        if (event.isPopupTrigger()) {
            showPopup(event);
        }
    }
    
    private final void showPopup(MouseEvent event) {
        Point point = event.getPoint();
        TreePath path = getPathForLocation(event.getX(), event.getY());
        if (path != null) {
            JPopupMenu popup = new JPopupMenu();
            if (_model != null && _model.buildPopup(popup, path)) {
                popup.show(this, point.x, point.y);
            }
        }
    }
    
    public void valueChanged(TreeSelectionEvent e)    {
        TreePath selection = e.getNewLeadSelectionPath();
        if (selection != null) {
            Object last = selection.getLastPathComponent();
            if (last instanceof UMLModelElementTreeNode) {
                MModelElement element = ((UMLModelElementTreeNode) last).getModelElement();
                if (element != null) {
                    TargetManager.getInstance().setTarget(element);
                }
            }
        }
    }
    
}
