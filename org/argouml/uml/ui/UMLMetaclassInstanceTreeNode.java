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
import ru.novosoft.uml.*;
import javax.swing.*;
import javax.swing.tree.*;
import ru.novosoft.uml.foundation.core.*;
import java.util.*;

/**
 *  This class implements a tree node that lists
 *        all elements of a collection that 
 *        are instances of a specified metaclass
 *  @author Curt Arnold
 *
 * @deprecated as of ArgoUml 0.13.5 (10-may-2003),
 *             replaced by nothing?,
 *             this class is part of the 'old'(pre 0.13.*) implementation of proppanels
 *             that used reflection a lot.
 */
public class UMLMetaclassInstanceTreeNode implements TreeNode {
    private TreeNode _parent;
    private List _children;
    private String _label;
    private Collection _collection;
    private Class _include;
    private Class[] _exclude;
    private UMLUserInterfaceContainer _container;
    
    public UMLMetaclassInstanceTreeNode(UMLUserInterfaceContainer container,
        TreeNode parent, String label, Class include, Class[] exclude) {
        _parent = parent;
        _label = label;
        _include = include;
        _exclude = exclude;
        _container = container;
    }

    public TreeNode getChildAt(int childIndex) {
        if (_children == null) update();
        if (childIndex >= 0 && childIndex < _children.size()) {
            return (TreeNode) _children.get(childIndex);
        }
        return null;
    }

    public int getChildCount() {
        if (_children == null) update();
        return _children.size();
    }

    public TreeNode getParent() {
        return _parent;
    }

    public int getIndex(TreeNode node) {
        if (_children == null) update();
        return _children.indexOf(node);
    }

    public boolean getAllowsChildren() {
        return true;
    }

    public boolean isLeaf() {
        return false;
    }

    
    public Enumeration children() {
        if (_children == null) update();
        return new EnumerationAdapter(_children.iterator());
    }
    
    public String toString() {
        return _label;        
    }
    
    public void setCollection(Collection collection) {
        _children = null;
        _collection = collection;
    }
        
    private void update() {
        if (_children == null) {
            _children = new ArrayList();
        }
        else {
            _children.clear();
        }
        if (_collection != null) {
            Iterator iter = _collection.iterator();
            while (iter.hasNext()) {
                Object candidate = iter.next();
                if (_include.isInstance(candidate)) {
                    boolean accept = true;
                    if (_exclude != null) {
                        for (int i = 0; i < _exclude.length && accept; i++) {
                            accept = !_exclude[i].isInstance(candidate);
                        }
                    }
                    if (accept) {
                        _children.add(
                            new UMLModelElementTreeNode(this, _container,
                                (MModelElement) candidate));
                    }
                }
            }
        }
    }
}
