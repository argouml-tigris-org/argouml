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

import java.awt.*;
import java.util.*;
import javax.swing.*;
import javax.swing.tree.*;

import ru.novosoft.uml.*;
import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.model_management.*;
import ru.novosoft.uml.behavior.use_cases.*;
import ru.novosoft.uml.foundation.extension_mechanisms.*;

import org.argouml.application.api.*;


/**
 *  This class is implements a tree model for ownedElements of a MNamespace
 *
 *  @author Curt Arnold
 *
 * @deprecated as of ArgoUml 0.13.5 (10-may-2003),
 *             replaced by nothing,
 *             this class is part of the 'old'(pre 0.13.*) implementation of proppanels
 *             that used reflection a lot.
 */
public class UMLOwnedElementRootNode extends UMLTreeRootNode  {
    
    private ArrayList _nodeList = new ArrayList(10);
    private ArrayList _nonEmptyNodes = new ArrayList(10);
    private ArrayList _popupMenu = new ArrayList(10);

    /**
     *   Creates a new tree model
     *   @param container the container (typically a PropPanelClass or PropPanelInterface)
     *                    that provides access to the target classifier.
     *   @param property  a string that specifies the name of an event that should force a refresh
     *                       of the list model.  A null value will cause all events to trigger a refresh.
     *   @param showNone  if true, an element labelled "none" will be shown where there are
     *                        no actual entries in the list.
     */    
    public UMLOwnedElementRootNode(UMLUserInterfaceContainer container,
        String property, boolean forClass) {
        super(container, property);
        JMenu add = new JMenu("Add");
        _popupMenu.add(add);
        if (!forClass) {
            _nodeList.add(new UMLMetaclassInstanceTreeNode(container, this, 
                "Packages", MPackage.class, null));
            add.add(new UMLTreeMenuItem("Package", container, "addPackage", false));
        }
        _nodeList.add(new UMLMetaclassInstanceTreeNode(container, this,
            "Classes", MClass.class, null));
        add.add(new UMLTreeMenuItem(Argo.localize("UMLMenu", "misc.class"), container, "addClass", false));
        if (!forClass) {
            _nodeList.add(new UMLMetaclassInstanceTreeNode(container, this,
                "Interfaces", MInterface.class, null));
            add.add(new UMLTreeMenuItem("Interface", container, "addInterface", false));
        }
        _nodeList.add(new UMLMetaclassInstanceTreeNode(container, this,
            "Datatypes", MDataType.class, null));
        add.add(new UMLTreeMenuItem("DataType", container, "addDataType", false));
        if (!forClass) {
            _nodeList.add(new UMLMetaclassInstanceTreeNode(container, this,
                "Actors", MActor.class, null));
            add.add(new UMLTreeMenuItem("Actor", container, "addActor", false));
            _nodeList.add(new UMLMetaclassInstanceTreeNode(container, this,
                "Use Cases", MUseCase.class, null));
            add.add(new UMLTreeMenuItem("Use Case", container, "addUseCase", false));
            /*
            _nodeList.add(new UMLMetaclassInstanceTreeNode(container,this,
                "Associations",MAssociation.class,null));
            add.add(new UMLTreeMenuItem("Association",container,"addAssociation",false));
            _nodeList.add(new UMLMetaclassInstanceTreeNode(container,this, 
                "Generalizations",MGeneralization.class,null));
            add.add(new UMLTreeMenuItem("Generalization",container,"addGeneralization",false));
            */
//            _nodeList.add(new UMLMetaclassInstanceTreeNode(container,this,
//                "Abstractions",MAbstraction.class,null));
//            add.add(new UMLTreeMenuItem("Abstraction",container,"addAbstraction",false));
            _nodeList.add(new UMLMetaclassInstanceTreeNode(container, this,
                "Stereotypes", MStereotype.class, null));
            add.add(new UMLTreeMenuItem("Stereotype", container, "addStereotype", false));
        }
        _popupMenu.add(new UMLTreeMenuItem("Delete", container, "deleteElement", true));
        _popupMenu.add(new UMLTreeMenuItem("Go", container, "navigateElement", true));
        _popupMenu.add(new UMLTreeMenuItem("Open", container, "openElement", true));
    }
    
    
    public TreeNode getChildAt(int childIndex) {
        return (TreeNode) _nonEmptyNodes.get(childIndex);
    }

    public int getChildCount() {
        return _nonEmptyNodes.size();
    }

    public int getIndex(TreeNode node) {
        return _nonEmptyNodes.indexOf(node);
    }

    
    public Enumeration children() {
        return new EnumerationAdapter(_nonEmptyNodes.iterator());
    }
    
    public void propertySet(MElementEvent mee) {
        if (checkEvent(mee)) {
            update();
        }
    }

    public void listRoleItemSet(MElementEvent mee) {
        if (checkEvent(mee)) {
            update();
        }
    }
    
    public void recovered(MElementEvent mee) {
        if (checkEvent(mee)) {
            update();
        }
    }
    
    public void removed(MElementEvent mee) {
        if (checkEvent(mee)) {
            update();
        }
    }
    
    public void roleAdded(MElementEvent mee) {
        if (checkEvent(mee)) {
            update();
        }
    }
    
    public void roleRemoved(MElementEvent mee) {
        if (checkEvent(mee)) {
            update();
        }
    }
    
    private boolean checkEvent(MElementEvent mee) {
        boolean isAffected = false;
        String eventName = mee.getName();
        if (eventName != null && eventName.equals("ownedElement")) {
            Object source = mee.getSource();
            Object target = getContainer().getTarget();
            if (source == target) {
                isAffected = true;
            }
        }
        return isAffected;
    }
    
    public void targetChanged() {
        update();
    }
    
    private void update() {
        _nonEmptyNodes.clear();
        Object target = getContainer().getTarget();
        if (target instanceof MNamespace) {
            Collection ownedElements = ((MNamespace) target).getOwnedElements();            
            Iterator iter = _nodeList.iterator();
            while (iter.hasNext()) {
                UMLMetaclassInstanceTreeNode metaNode = 
                    (UMLMetaclassInstanceTreeNode) iter.next();
                metaNode.setCollection(ownedElements);
                //
                //   only put nodes that have some entries 
                //      in display
                if (metaNode.getChildCount() > 0) {
                    _nonEmptyNodes.add(metaNode);
                }
            }
        }
        getModel().fireTreeStructureChanged();
    
    }
        
    public void targetReasserted() {
    }
    
    
    public boolean buildPopup(TreeModel model, JPopupMenu menu, TreePath path) {
        MModelElement element = null;
        
        Object last = path.getLastPathComponent();
        if (last instanceof UMLModelElementTreeNode) {
            element = ((UMLModelElementTreeNode) last).getModelElement();
        }
        
        Object container = getContainer();
        Object item = null;
        Object child = null;
        Iterator iter = _popupMenu.iterator();
        while (iter.hasNext()) {
            item = iter.next();
            if (item instanceof UMLTreeMenuItem) {
                ((UMLTreeMenuItem) item).setModelElement(element);
            } 
            else {
                if (item instanceof Container) {
                    Container cont = (Container) item;
                    int count = cont.getComponentCount();
                    for (int i = 0; i < count; i++) {
                        child = cont.getComponent(i);
                        if (child instanceof UMLTreeMenuItem) {
                            ((UMLTreeMenuItem) child).setModelElement(element);
                        }
                    }
                }
            }
            menu.add((Component) item);
        }
        return true;
    }                    
    
}
