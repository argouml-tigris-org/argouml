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
abstract public class AbstractButtonAction extends AbstractAction implements ButtonAction {
    
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
    public AbstractButtonAction(String name, Icon icon, boolean modal, int lockMethod) {
        super(name, icon);
        this.modal = modal;
        this.lockMethod = lockMethod;
    }
    
    public void setModal(boolean modal) {
        this.modal = modal;
    }
    
    public boolean isModal(){
        return modal;
    }
    
    public void setLockMethod(int lockMethod) {
        this.lockMethod = lockMethod;
    }
    
    public int getLockMethod() {
        return lockMethod;
    }
}
