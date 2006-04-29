// $Id$
// Copyright (c) 2004-2006 The Regents of the University of California. All
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
 * Adapts events triggered by the underlying model into PropertyChangeEvents
 * for the application.<p>
 *
 * @author Bob Tarling
 * @deprecated for 0.21.2 by tfmorris - use ModelEventPump interface
 */
public interface EventAdapter {

    /**
     * @param pcl the property change listener to be added
     * @deprecated for 0.21.2 by tfmorris.
     *         Use {@link ModelEventPump#addClassModelEventListener(
     *         PropertyChangeListener, Object, String[])}.
     * @see ModelEventPump#addClassModelEventListener(
     *         PropertyChangeListener, Object, String[])
     */
    void addPropertyChangeListener(PropertyChangeListener pcl);

    /**
     * @param pcl the property change listener to be removed
     * @deprecated for 0.21.2 by tfmorris.
     *         Use {@link ModelEventPump#removeClassModelEventListener(
     *         PropertyChangeListener, Object, String[])}.
     * @see ModelEventPump#removeClassModelEventListener(
     *         PropertyChangeListener, Object, String[])
     */
    void removePropertyChangeListener(PropertyChangeListener pcl);
}
