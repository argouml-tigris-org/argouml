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
    
    // Possible ways in which a user can lock a button
    public static final int NONE = 0;
    public static final int DOUBLE_CLICK = 1;
    
    /**
     * Creates a new instance of AbstractButtonAction
     */
    public AbstractButtonAction(String name, Icon icon) {
        super(name, icon);
    }

    /**
     * Creates a new instance of AbstractButtonAction
     */
    public AbstractButtonAction(String name, Icon icon, boolean modal) {
        super(name, icon);
        this.modal = modal;
    }

    /**
     * Creates a new instance of AbstractButtonAction
     */
    public AbstractButtonAction(String name, Icon icon,
				boolean modal, int lockMethod) {
        super(name, icon);
        this.modal = modal;
        this.lockMethod = lockMethod;
    }
    
    public void setModal(boolean modal) {
        this.modal = modal;
    }
    
    public boolean isModal() {
        return modal;
    }
    
    public void setLockMethod(int lockMethod) {
        this.lockMethod = lockMethod;
    }
    
    public int getLockMethod() {
        return lockMethod;
    }
}
