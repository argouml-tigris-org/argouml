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

import javax.swing.Action;

import org.argouml.i18n.Translator;
import org.tigris.gef.undo.UndoableAction;

/**
 * Base class for remove actions. Remove actions can remove an element
 * from the model. This can either be a total remove ('erase from
 * model') or just a remove from a list of bases as in the case of
 * classifierrole bases.<p>
 *
 * @author jaap.branderhorst@xs4all.nl
 * @since Jan 25, 2003
 */
public class AbstractActionRemoveElement extends UndoableAction {

    /**
     * The object that owns the object that must be removed (the
     * object that is the target of the projectbrowser in most cases).
     */
    private Object target;

    private Object objectToRemove;

    /**
     * Constructor for AbstractActionRemoveElement.
     */
    protected AbstractActionRemoveElement() {
        this(Translator.localize("Delete From Model"));
    }

    /**
     *  The constructor.
     * @param name the name for this action
     */
    protected AbstractActionRemoveElement(String name) {
        super(Translator.localize(name),
                null);
        // Set the tooltip string:
        putValue(Action.SHORT_DESCRIPTION, 
                Translator.localize(name));
    }

     /**
     * Returns the target.
     *
     * @return MModelElement
     */
    public Object getTarget() {
        return target;
    }

    /**
     * Sets the target.
     *
     * @param theTarget The target to set
     */
    public void setTarget(Object theTarget) {
        target = theTarget;
        setEnabled(isEnabled());
    }

    /**
     * Returns the objectToRemove.
     *
     * @return Object
     */
    public Object getObjectToRemove() {
        return objectToRemove;
    }

    /**
     * Sets the objectToRemove.
     *
     * @param theObjectToRemove The objectToRemove to set
     */
    public void setObjectToRemove(Object theObjectToRemove) {
        objectToRemove = theObjectToRemove;
        setEnabled(isEnabled());
    }

    /*
     * @see javax.swing.Action#isEnabled()
     */
    public boolean isEnabled() {
        return getObjectToRemove() != null && getTarget() != null;
    }

}
