// $Id$
// Copyright (c) 2002-2004 The Regents of the University of California. All
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

package org.argouml.ui.targetmanager;

import java.util.EventListener;

/**
 * Interface for listening to events from TargetManager.
 *
 * <p>Notice that while there are three events {@link #targetSet},
 * {@link #targetAdded} and {@link #targetRemoved} these signify that
 * the target set and primary target has changed and the difference
 * between when they are called is subtle.
 *
 * <p>It may often be appropriate to implement as listener so that
 * targetAdded and targetRemoved calls targetSet. Sometimes the subtle
 * difference can be used to implement optimizations in these methods,
 * if desired (needed).
 *
 * @see TargetManager#
 * @author gebruiker
 */
public interface TargetListener extends EventListener {
    
    /**
     * Fired when a total new set of targets is set
     *
     * @param e The TargetEvent, name will be TARGET_SET
     */
    public void targetSet(TargetEvent e);
    
    /**
     * Fired when a target is removed from the list of targets
     *
     * <p>Notice that this is special case of targetSet, that it does not
     * imply that the primary target is the same nor different nor even that
     * there is a primary target.
     *
     * @param e The TargetEvent, name will be TARGET_REMOVED
     */
    public void targetRemoved(TargetEvent e);
    
    /**
     * Fired when a target is added to the list of targets.
     *
     * <p>Notice that this is special case of targetSet, that it does not
     * imply that the primary target is the same nor that it is different
     * nor that there was target previously.
     *
     * @param e The TargetEvent, name will be TARGET_ADDED
     */
    public void targetAdded(TargetEvent e);
}

