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

// Copyright (c) 2006 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies. This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason. IN NO EVENT SHALL THE
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

import java.util.Iterator;
import java.util.List;

import javax.swing.Action;

import org.argouml.application.helpers.ResourceLoaderWrapper;

/**
 * An action to navigate to the next in the list, 
 * i.e. first we go up, then down again to the next in the list.
 * 
 * @author Michiel
 */
public abstract class ActionNavigateUpNextDown extends AbstractActionNavigate {

    /**
     * The constructor.
     */
    public ActionNavigateUpNextDown() {
        super("button.go-up-next-down", true);
        putValue(Action.SMALL_ICON,
                ResourceLoaderWrapper.lookupIconResource("NavigateUpNext"));
    }

    /*
     * @see org.argouml.uml.ui.AbstractActionNavigate#navigateTo(java.lang.Object)
     */
    protected Object navigateTo(Object source) {
        Object up = getParent(source);
        List family = getFamily(up);
        assert family.contains(source);
        Iterator it = family.iterator();
        while (it.hasNext()) {
            Object child = it.next();
            if (child == source) {
                if (it.hasNext()) {
                    return it.next();
                } else {
                    return null;
                }
            }
        }
        return null;
    }
    
    /**
     * Get the list of elements that we are navigating through.
     * 
     * @param parent the parent element that owns all elements in the list
     * @return the list
     */
    public abstract List getFamily(Object parent);

    /**
     * Get the parent of the list of elements that we are navigating through.
     * 
     * @param child the childelement of which we seek the previous element
     * @return the parent element
     */
    public abstract Object getParent(Object child);

}
