// $Id$
// Copyright (c) 2003 The Regents of the University of California. All
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

/*
 * AbstractButtonAction.java
 *
 * Created on 02 March 2003, 00:26
 */

package org.argouml.swingext;

import javax.swing.AbstractAction;
import javax.swing.Icon;

/**
 *
 * @author Bob Tarling
 */
public abstract class AbstractButtonAction
    extends AbstractAction
    implements ButtonAction
{
    
    private boolean modal;
    private int lockMethod = NONE;
    
    /**
     * Possible ways in which a user can lock a button: <code>NONE</code>
     */
    public static final int NONE = 0;
    
    /**
     * Possible ways in which a user can lock a button:
     *  <code>DOUBLE_CLICK</code>
     */
    public static final int DOUBLE_CLICK = 1;
    
    /**
     * Creates a new instance of AbstractButtonAction
     *
     * @param name the name of the action
     * @param icon the icon for the action
     */
    public AbstractButtonAction(String name, Icon icon) {
        super(name, icon);
    }

    /**
     * Creates a new instance of AbstractButtonAction
     *
     * @param name the name of the action
     * @param icon the icon of the action
     * @param isModal modal = the user is obliged to answer this action 
     *              before doing anything else
     */
    public AbstractButtonAction(String name, Icon icon, boolean isModal) {
        super(name, icon);
        this.modal = isModal;
    }

    /**
     * Creates a new instance of AbstractButtonAction
     *
     * @param name the name of the action
     * @param icon the icon of the action
     * @param isModal modal = the user is obliged to answer this action 
     *              before doing anything else
     * @param theLockMethod purpose: action buttons can remain depressed 
     *                      so that they can be used multiple times
     */
    public AbstractButtonAction(String name, Icon icon,
				boolean isModal, int theLockMethod) {
        super(name, icon);
        this.modal = isModal;
        this.lockMethod = theLockMethod;
    }
    
    /**
     * @see org.argouml.swingext.ButtonAction#setModal(boolean)
     */
    public void setModal(boolean isModal) {
        this.modal = isModal;
    }
    
    /**
     * @see org.argouml.swingext.ButtonAction#isModal()
     */
    public boolean isModal() {
        return modal;
    }
    
    /**
     * @see org.argouml.swingext.ButtonAction#setLockMethod(int)
     */
    public void setLockMethod(int theLockMethod) {
        this.lockMethod = theLockMethod;
    }
    
    /**
     * @see org.argouml.swingext.ButtonAction#getLockMethod()
     */
    public int getLockMethod() {
        return lockMethod;
    }
}
