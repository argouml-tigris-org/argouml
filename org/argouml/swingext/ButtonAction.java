/*
 * ButtonAction.java
 *
 * Created on 02 March 2003, 00:23
 */

package org.argouml.swingext;

/**
 *
 * @author ButtonAction
 */
public interface ButtonAction {
    public void setModal(boolean modal);
    public boolean isModal();
    public void setLockMethod(int lockMethod);
    public int getLockMethod();
}
