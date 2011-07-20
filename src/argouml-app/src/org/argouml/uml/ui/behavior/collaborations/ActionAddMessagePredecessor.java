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

// Copyright (c) 1996-2007 The Regents of the University of California. All
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

package org.argouml.uml.ui.behavior.collaborations;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.uml.ui.AbstractActionAddModelElement2;

/**
 * Action to add a predecessor to some message.
 * @since Oct 2, 2002
 * @author jaap.branderhorst@xs4all.nl
 * @stereotype singleton
 */
public class ActionAddMessagePredecessor 
    extends AbstractActionAddModelElement2 {

    private static final ActionAddMessagePredecessor SINGLETON =
	new ActionAddMessagePredecessor();

    /**
     * Constructor for ActionAddMessagePredecessor.
     */
    protected ActionAddMessagePredecessor() {
        super();
    }


    protected List getChoices() {
        if (getTarget() == null) {
            return Collections.EMPTY_LIST;
        }
        List vec = new ArrayList();
        vec.addAll(Model.getCollaborationsHelper()
                .getAllPossiblePredecessors(getTarget()));
        return vec;
    }


    protected List getSelected() {
        if (getTarget() == null) {
	    throw new IllegalStateException(
                "getSelected may not be called with null target");
        }
        List vec = new ArrayList();
        vec.addAll(Model.getFacade().getPredecessors(getTarget()));
        return vec;
    }


    protected String getDialogTitle() {
        return Translator.localize("dialog.add-predecessors");
    }


    protected void doIt(Collection selected) {
	if (getTarget() == null) {
	    throw new IllegalStateException(
	            "doIt may not be called with null target");
	}
	Object message = getTarget();
	Model.getCollaborationsHelper().setPredecessors(message, selected);
    }

    /**
     * @return Returns the SINGLETON.
     */
    public static ActionAddMessagePredecessor getInstance() {
        return SINGLETON;
    }

}
