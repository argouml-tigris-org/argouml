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

// File: GoUseCaseToExtensionPoint.java
// Classes: GoUseCaseToExtensionPoint
// Original Author: mail@jeremybennett.com
// $Id$

// 16 Apr 2002: Jeremy Bennett (mail@jeremybennett.com). Created to support
// display of extension points in the navigator pane.


package org.argouml.uml.diagram.use_case.ui;

import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;

import ru.novosoft.uml.model_management.*;
import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.behavior.use_cases.*;

import org.apache.log4j.Category;
import org.argouml.application.api.Argo;
import org.argouml.model.uml.UmlHelper;
import org.argouml.ui.*;
import org.argouml.uml.MMUtil;


/**
 * <p>Provides a rule to display extension points in the navigation pane.</p>
 *
 * @author  16 Apr 2002. Jeremy Bennett (mail@jeremybennett.com).
 */

public class GoUseCaseToExtensionPoint extends AbstractGoRule {
    protected static Category cat = Category.getInstance(GoUseCaseToExtensionPoint.class);


    /**
     * <p>Give a name to this rule.</p>
     *
     * @return  The name of the rule ("<code>Use Case->Extension
     *          Point</code>"). 
     */

    public String getRuleName() {
        return Argo.localize ("Tree", "Use Case->Extension Point");
    }
  

    /**
     * Get the object at the root of the {@link JTree}.
     *
     * <p>
     * <em>Warning</em>. This should never be invoked, since an extension
     * point cannot be the root of the tree.
     *
     * @return  The object at the root of the tree
     */
    public Object getRoot() {
        throw
	    new UnsupportedOperationException("getRoot should not be called");
    }


    /**
     * <p>Set the object at the root of the {@link JTree}.<p>
     *
     * <p><em>Warning</em>. This should never be invoked, since an extension
     *   point cannot be the root of the tree. We print a rude message on the
     *   console.</p>
     *
     * @param r  The object to become the root of the tree
     */

    public void setRoot(Object r) {
        cat.error(getClass().toString() +
                           ": setRoot() should never be called");
    }


    /**
     * <p>Get an indexed child object from a parent in the tree.<p>
     *
     * <p>This should only be invoked for use cases. It gets the extension
     *   points and returns the child at the requested index.</p>
     *
     * <p><em>Warning</em>. There is no test for the index being in bounds (the
     *   calling method should have tested for this.</p>
     *
     * @param parent  The parent object in the tree. Should be an instance of
     *                {@link MUseCase}.
     *
     * @param index   The index of the child required.
     *
     * @return        The object ({@link MExtensionPoint}) at the given index.
     */

    public Object getChild(Object parent, int index) {

        // Give up (with a rude message) if we are not a use case

        if (!(parent instanceof MUseCase)) {
            throw new UnsupportedOperationException
		("getChild should not be called");
        }

        // Get the extension points, place in a vector and return the one asked
        // for.

        MUseCase   useCase = (MUseCase) parent;
        Collection eps     = UmlHelper.getHelper().getUseCases().getExtensionPoints(useCase);
        Vector     v       = new Vector(eps);

        return v.elementAt(index);
    }


    /**
     * <p>Count how many child extension points in a parent use case.<p>
     *
     * <p>This only does the count for use cases. Anything else returns 0. It
     *   gets the extension points and counts how many.</p>
     *
     * @param parent  The parent object in the tree. Should be an instance of
     *                {@link MUseCase} if a count is to be given.
     *
     * @return        The count of the number of child extension points, or
     *                <code>0</code> if there are none or the parent is not a
     *                use case.
     */
  
    public int getChildCount(Object parent) {

        // Zero (quietly) if parent is not a use case

        if (!(parent instanceof MUseCase)) {
            return 0;
        }

        MUseCase   useCase = (MUseCase) parent;
        Collection eps     = UmlHelper.getHelper().getUseCases().getExtensionPoints(useCase);

        return (eps == null) ? 0 : eps.size();
    }
  

    /**
     * <p>Find the index of a child extension point in a use case parent.<p>
     *
     * <p>This only does the index for extension points in use cases. Anything
     *   else returns -1. It gets the extension points and looks for the *
     *   child.</p>
     *
     * @param parent  The parent object in the tree. Should be an instance of
     *                {@link MUseCase} if an index is to be found.
     *
     * @param child   The child object in the tree. Should be an instance of
     *                {@link MExtensionPoint} if an index is to be found.
     *
     * @return        The index of the child extension point, or
     *                <code>-1</code> if is not one or the parent is not a
     *                use case.  */
  
    public int getIndexOfChild(Object parent, Object child) {

        // Give up if not a use case

        if (!(parent instanceof MUseCase)) {
            return -1;
        }

        MUseCase   useCase = (MUseCase) parent;
        Collection eps     = UmlHelper.getHelper().getUseCases().getExtensionPoints(useCase);
        Vector     v       = new Vector(eps);

        if (v.contains(child)) {
            return v.indexOf(child);
        }

        return -1;
    }

    public Collection getChildren(Object parent) { 
      throw
          new UnsupportedOperationException("getChildren should not be called");
    }



    /**
     * <p>Test if the given object is a leaf from a use case.</p>
     *
     * @param node  The node to test.
     *
     * @return      <code>false</code> if node is a use case and has
     *              children, <code>true</code> otherwise.
     */

    public boolean isLeaf(Object node) {
        return !((node instanceof MClassifier) && (getChildCount(node) > 0));
    }


    /**
     * <p>Notification that the value associated with the given tree path has
     *   changed.</p>
     *
     * <p>Provided for compliance with the {@link TreeModel} interface. Null
     *   implementation here.</p>
     *
     * @param path      The path whose value has changed.
     *
     * @param newValue  The new value associated with that path
     */

    public void valueForPathChanged(TreePath path, Object newValue) { }


    /**
     * <p>Request to add a new listener for the tree model.</p>
     *
     * <p>Provided for compliance with the {@link TreeModel} interface. Null
     *   implementation here (we should not be listening directly to this
     *   model).</p>
     *
     * @param l  The listener to add.
     */

    public void addTreeModelListener(TreeModelListener l) { }


    /**
     * <p>Request to remove an existing listener for the tree model.</p>
     *
     * <p>Provided for compliance with the {@link TreeModel} interface. Null
     *   implementation here (we should not be listening directly to this
     *   model).</p>
     *
     * @param l  The listener to add.
     */

    public void removeTreeModelListener(TreeModelListener l) { }

}  /* End of class GoUseCaseToExtensionPoint */
