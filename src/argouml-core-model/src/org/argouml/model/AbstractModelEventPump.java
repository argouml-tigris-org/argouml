/* $Id$
 *******************************************************************************
 * Copyright (c) 2009 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    linus
 *******************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

// Copyright (c) 2004-2007 The Regents of the University of California. All
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
 * Abstract class that implements the convenience methods of the
 * {@link ModelEventPump} interface.
 *
 * @author Linus Tolke
 */
public abstract class AbstractModelEventPump implements ModelEventPump {

    /*
     * @see org.argouml.model.ModelEventPump#addModelEventListener(
     *          java.beans.PropertyChangeListener, java.lang.Object,
     *          java.lang.String[])
     */
    public abstract void addModelEventListener(PropertyChangeListener listener,
            				       Object modelelement,
            				       String[] eventNames);

    /*
     * @see org.argouml.model.ModelEventPump#addModelEventListener(
     *          java.beans.PropertyChangeListener, java.lang.Object,
     *          java.lang.String)
     */
    public void addModelEventListener(PropertyChangeListener listener,
            Object modelelement, String eventName) {
        addModelEventListener(listener,
                	      modelelement,
                	      new String[] {eventName });
    }

    /*
     * @see org.argouml.model.ModelEventPump#addModelEventListener(
     *          java.beans.PropertyChangeListener, java.lang.Object,
     *          java.lang.String)
     */
    public void addModelEventListener(UmlChangeListener listener,
            Object modelelement, String eventName) {
        addModelEventListener(listener,
                              modelelement,
                              new String[] {eventName });
    }

    /*
     * @see org.argouml.model.ModelEventPump#addModelEventListener(
     *          java.beans.PropertyChangeListener, java.lang.Object)
     */
    public abstract void addModelEventListener(PropertyChangeListener listener,
            				       Object modelelement);

    /*
     * @see org.argouml.model.ModelEventPump#removeModelEventListener(
     *          java.beans.PropertyChangeListener, java.lang.Object,
     *          java.lang.String[])
     */
    public abstract void removeModelEventListener(
            PropertyChangeListener listener,
            Object modelelement, String[] eventNames);

    /*
     * @see org.argouml.model.ModelEventPump#removeModelEventListener(
     *          java.beans.PropertyChangeListener, java.lang.Object,
     *          java.lang.String)
     */
    public void removeModelEventListener(PropertyChangeListener listener,
            				 Object modelelement,
            				 String eventName) {
        removeModelEventListener(listener,
                		 modelelement,
                		 new String[] {eventName, });
    }

    /*
     * @see org.argouml.model.ModelEventPump#removeModelEventListener(
     *          java.beans.PropertyChangeListener, java.lang.Object,
     *          java.lang.String)
     */
    public void removeModelEventListener(UmlChangeListener listener,
                                         Object modelelement,
                                         String eventName) {
        removeModelEventListener(listener,
                                 modelelement,
                                 new String[] {eventName, });
    }

    /*
     * @see org.argouml.model.ModelEventPump#removeModelEventListener(
     * 		java.beans.PropertyChangeListener, java.lang.Object)
     */
    public abstract void removeModelEventListener(
            PropertyChangeListener listener,
            Object modelelement);

    /*
     * @see org.argouml.model.ModelEventPump#addClassModelEventListener(
     * 		java.beans.PropertyChangeListener, java.lang.Object,
     * 		java.lang.String[])
     */
    public abstract void addClassModelEventListener(
            PropertyChangeListener listener,
            Object modelClass,
            String[] eventNames);

    /*
     * @see org.argouml.model.ModelEventPump#addClassModelEventListener(
     * 		java.beans.PropertyChangeListener, java.lang.Object,
     * 		java.lang.String)
     */
    public void addClassModelEventListener(PropertyChangeListener listener,
            				   Object modelClass,
            				   String eventName) {
        addClassModelEventListener(listener,
                		   modelClass,
                		   new String[] {eventName, });
    }

    /*
     * @see org.argouml.model.ModelEventPump#removeClassModelEventListener(
     * 		java.beans.PropertyChangeListener, java.lang.Object,
     * 		java.lang.String[])
     */
    public abstract void removeClassModelEventListener(
            PropertyChangeListener listener,
            Object modelClass,
            String[] eventNames);

    /*
     * @see org.argouml.model.ModelEventPump#removeClassModelEventListener(
     * 		java.beans.PropertyChangeListener, java.lang.Object,
     * 		java.lang.String)
     */
    public void removeClassModelEventListener(PropertyChangeListener listener,
            				      Object modelClass,
            				      String eventName) {
        removeClassModelEventListener(listener,
                		      modelClass,
                		      new String[] {eventName, });

    }

}
