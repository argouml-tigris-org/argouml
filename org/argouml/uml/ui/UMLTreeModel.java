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
import javax.swing.JPopupMenu;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import org.argouml.ui.targetmanager.TargetManager;

import ru.novosoft.uml.MElementEvent;
import ru.novosoft.uml.foundation.core.MModelElement;

/**
 *  This class is an abstract superclass for classes that provide a tree model
 *  of UML model elements.
 *
 * @deprecated As of ArgoUml version 0.13.5, 
 * This class is not used and probably shouldn't be in the future either.
 *
 *  @author Curt Arnold
 */
public class UMLTreeModel extends DefaultTreeModel 
        implements UMLUserInterfaceComponent  {

    /**
     *   The container that provides the "target" model element.
     */
    private UMLUserInterfaceContainer _container;
    
    private UMLTreeRootNode _rootComponent;

    /**
     *   Creates a new tree model
     *   @param container the container (typically a PropPanelClass or PropPanelInterface)
     *                    that provides access to the target classifier.
     *   @param property  a string that specifies the name of an event that should force a refresh
     *                       of the list model.  A null value will cause all events to trigger a refresh.
     *   @param showNone  if true, an element labelled "none" will be shown where there are
     *                        no actual entries in the list.
     */    
    public UMLTreeModel(UMLUserInterfaceContainer container, UMLTreeRootNode root) {
        super(root, true);
        _rootComponent = root;
        _container = container;
    }
    
        
    
    /**
     *  This method returns the current "target" of the container.
     */
    final Object getTarget() {
        return _container.getTarget();
    }
    
    
    /**
     *   This method returns a rendering (typically a String) of the model element for the list.
     *   Default implementation defers to the current Profile of the container, but this
     *   method may be overriden.
     *
     *  @param @element model element
     *  @return rendering of the ModelElement
     */
    public Object formatElement(MModelElement element) {
        return _container.formatElement(element);
    }

    public void targetChanged() {
        _rootComponent.targetChanged();
    }
    
    public void targetReasserted() {        
    }
    
    public void fireTreeStructureChanged() {
        fireTreeStructureChanged(this, new Object[] {
	    getRoot() 
	}, null, null);
    }
    
    
    public void propertySet(MElementEvent mee) {
        _rootComponent.propertySet(mee);
    }

    public void listRoleItemSet(MElementEvent mee) {
        _rootComponent.listRoleItemSet(mee);
    }
    
    public void recovered(MElementEvent mee) {
        _rootComponent.recovered(mee);
    }
    
    public void removed(MElementEvent mee) {
        _rootComponent.removed(mee);
    }
    
    public void roleAdded(MElementEvent mee) {
        _rootComponent.roleAdded(mee);
    }
    
    
    public void roleRemoved(MElementEvent mee) {
        _rootComponent.roleRemoved(mee);
    }
    
    /**
     *  This method is called by context menu actions that
     *  desire to change to currently displayed object.
     *
     *  @param modelElement model element to display
     */
    public void navigateTo(MModelElement modelElement) {
        TargetManager.getInstance().setTarget(modelElement);
    }
    
    
    public boolean buildPopup(JPopupMenu popup, TreePath path) {
        return _rootComponent.buildPopup(this, popup, path);
    }
    
    
}


