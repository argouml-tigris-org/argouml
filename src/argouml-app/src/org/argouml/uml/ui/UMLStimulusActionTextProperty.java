/* $Id$
 *****************************************************************************
 * Copyright (c) 2009 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    tfmorris
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

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

import java.beans.PropertyChangeEvent;

import org.argouml.model.Model;

/**
 * TODO: this class should be moved to package
 * org.argouml.uml.ui.behavior.common_behavior.
 */
public class UMLStimulusActionTextProperty  {


    private String thePropertyName;

    /**
     * The constructor.
     *
     * @param propertyName the name of the property
     */
    public UMLStimulusActionTextProperty(String propertyName) {
        thePropertyName = propertyName;
    }


    /**
     * @param container the container of UML user interface components
     * @param newValue the new value of the property
     */
    public void setProperty(UMLUserInterfaceContainer container,
            String newValue) {
	Object stimulus = container.getTarget();
	if (stimulus != null) {

	    String oldValue = getProperty(container);
	    //
	    //  if one or the other is null or they are not equal
	    if (newValue == null
                || oldValue == null
                || !newValue.equals(oldValue)) {
		//
		//  as long as they aren't both null
		//   (or a really rare identical string pointer)
		if (newValue != oldValue) {
		    // Object[] args = { newValue };
		    Object action =
		        Model.getFacade().getDispatchAction(stimulus);
		    Model.getCoreHelper().setName(action, newValue);
		    // to rupdate the diagram set the stimulus name again
                    // TODO: Explain that this really works also in the
                    // MDR case. Linus is a sceptic.
		    String dummyStr = Model.getFacade().getName(stimulus);
		    Model.getCoreHelper().setName(stimulus, dummyStr);
		}
	    }
	}
    }

    /**
     * @param container the container of UML user interface components
     * @return the property
     */
    public String getProperty(UMLUserInterfaceContainer container) {
        String value = null;
	Object stimulus = container.getTarget();
	if (stimulus != null) {
	    Object action = Model.getFacade().getDispatchAction(stimulus);
	    if (action != null) {
                value = Model.getFacade().getName(action);
            }
	}
        return value;
    }

    boolean isAffected(PropertyChangeEvent event) {
        String sourceName = event.getPropertyName();
        return (thePropertyName == null
                || sourceName == null
                || sourceName.equals(thePropertyName));
    }

    /**
     * Called when the target has changed.
     */
    void targetChanged() {
    }
}

