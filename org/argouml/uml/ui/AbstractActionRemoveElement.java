// $Id$
// Copyright (c) 1996-2002 The Regents of the University of California. All
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

// $Id$
package org.argouml.uml.ui;

import org.argouml.application.api.Argo;

/**
 * Base class for remove actions. Remove actions can remove an element from the 
 * model. This can either be a total remove ('erase from model') or just a
 * remove from a list of bases as in the case of classifierrole bases.
 * @author jaap.branderhorst@xs4all.nl	
 * @since Jan 25, 2003
 */
public class AbstractActionRemoveElement extends UMLChangeAction {
    
    /**
     * The object that owns the object that must be removed (the object that is 
     * the target of the projectbrowser in most cases).
     */
    private Object _target;
    
    private Object _objectToRemove;
    
    /**
     * Constructor for AbstractActionRemoveElement.
     * @param s
     */
    protected AbstractActionRemoveElement() {
        this(Argo.localize("CoreMenu", "Delete From Model"));
    }
    
    protected AbstractActionRemoveElement(String name) {
        super(name, true, NO_ICON);
    }

     /**
     * Returns the target.
     * @return MModelElement
     */
    public Object getTarget() {
        return _target;
    }

    /**
     * Sets the target.
     * @param target The target to set
     */
    public void setTarget(Object target) {
        _target = target;
    }
    
    /**
     * Returns the objectToRemove.
     * @return Object
     */
    public Object getObjectToRemove() {
        return _objectToRemove;
    }

    /**
     * Sets the objectToRemove.
     * @param objectToRemove The objectToRemove to set
     */
    public void setObjectToRemove(Object objectToRemove) {
        _objectToRemove = objectToRemove;
    }
    
    

    /**
     * @see javax.swing.Action#isEnabled()
     */
    public boolean isEnabled() {
        return getObjectToRemove() != null;
    }

}
