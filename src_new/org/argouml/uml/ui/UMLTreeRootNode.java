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
import ru.novosoft.uml.*;
import javax.swing.*;
import javax.swing.tree.*;
import javax.swing.event.*;
import ru.novosoft.uml.foundation.core.*;
import java.util.*;
import ru.novosoft.uml.model_management.*;
import ru.novosoft.uml.behavior.use_cases.*;
import ru.novosoft.uml.foundation.extension_mechanisms.*;

/**
 *  This class is implements a tree model for ownedElements of a MNamespace
 * @deprecated As of ArgoUml version 0.13.5, 
 * This class is not used and probably shouldn't be in the future either.
 *  @author Curt Arnold
 */
abstract public class UMLTreeRootNode implements TreeNode, 
    UMLUserInterfaceComponent  {
    
    protected UMLUserInterfaceContainer _container;
    protected String _property;
    protected UMLTreeModel _model;

    /**
     *   Creates a new tree model
     *   @param container the container (typically a PropPanelClass or PropPanelInterface)
     *                    that provides access to the target classifier.
     *   @param property  a string that specifies the name of an event that should force a refresh
     *                       of the list model.  A null value will cause all events to trigger a refresh.
     *   @param showNone  if true, an element labelled "none" will be shown where there are
     *                        no actual entries in the list.
     */    
    public UMLTreeRootNode(UMLUserInterfaceContainer container,
        String property) {
        _container = container;
        _property = property;
    }
    
    
    public void setModel(UMLTreeModel model) {
        _model = model;
    }
    
    public final UMLTreeModel getModel() {
        return _model;
    }

    public TreeNode getParent() {
        return null;
    }


    public boolean getAllowsChildren() {
        return true;
    }

    public boolean isLeaf() {
        return false;
    }

    
    public void targetReasserted() {
    }
    
    abstract public boolean buildPopup(TreeModel model,JPopupMenu menu,TreePath path);
    
    public final UMLUserInterfaceContainer getContainer() {
        return _container;
    }
    
    public final String getProperty() {
        return _property;
    }
    
    
    
};
