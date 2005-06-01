// $Id$
// Copyright (c) 1996-2005 The Regents of the University of California. All
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

package org.argouml.model;

import java.beans.PropertyChangeListener;

/**
 * This listens for events on a component and dispatches the events to all its
 * interested child components.
 */
public interface ContainerDispatcher extends PropertyChangeListener {

    /**
     * Calling this method with an array of metaclasses (for example,
     * MClassifier.class) will result in the prop panel propagating
     * any name changes or removals on any object that on the same
     * event queue as the target that is assignable to one of the
     * metaclasses.<p>
     *
     * <em>Note</em>. Despite the name, the old implementation tried
     * to listen for ownedElement and baseClass events as well as name
     * events. We incorporate all these.<p>
     *
     * <em>Note</em> Reworked the implementation to use the new
     * UmlModelEventPump mechanism. In the future proppanels should
     * register directly with UmlModelEventPump IF they are really
     * interested in the events themselves. If components on the
     * proppanels are interested, these components should register
     * themselves.<p>
     *
     * @deprecated by Jaap 3 Nov 2002 (ArgoUml version unknown -
     * earlier than 0.13.5), replaced by
     * {@link org.argouml.model.uml.UmlModelEventPump#addModelEventListener(
     *            Object , Object)}.
     *             since components should register themselves.
     *
     * @param metaclasses  The metaclass array we wish to listen to.
     */
    public void setNameEventListening(Object[] metaclasses);
}