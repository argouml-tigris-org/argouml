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

// $header$
package org.argouml.uml.ui;

/**
 * Classes implementing this interface are interested in changes of the
 * target. Target changes occur when the user or argouml itself (programmatically)
 * select another modelelement. 
 *<p>
 * This listener is introduced to remove the very close dependency between
 * property panel and GUI elements. More specifically to support the need to
 * implement GUI elements as singletons which is not possible with the implementation
 * that uses UMLUserInterfaceComponent as the interface.
 * </p>
 * <p>
 * The methods are called at the moment via UMLChangeDispatch. In the future
 * an eventpump will come into place that does not call the components on a 
 * property panel but that will call the interested instances (GUI elements) 
 * directly.
 * </p>
 * @since Nov 8, 2002
 * @author jaap.branderhorst@xs4all.nl
 */
public interface TargetChangedListener {
    
    /**
     * This method is called when a new target is selected, either by the
     * program or by the user.
     * @param newTarget
     */
    public void targetChanged(Object newTarget);
    
    /**
     * This method is called when the navigation history is updated.
     * @param newTarget
     */
    public void targetReasserted(Object newTarget);

}
