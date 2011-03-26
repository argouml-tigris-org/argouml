/* $Id$
 *****************************************************************************
 * Copyright (c) 2009-2010 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Michiel van der Wulp
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

// Copyright (c) 2006-2009 The Regents of the University of California. All
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

package org.argouml.notation.providers;

import java.util.List;

import org.argouml.model.Model;
import org.argouml.notation.NotationProvider;

/**
 * This abstract class forms the basis of all Notation providers
 * for the text shown in the Fig that represents the Message.
 * Subclass this for all languages.
 * 
 * @author Michiel van der Wulp
 */
public abstract class MessageNotation extends NotationProvider {
    
    /**
     * The constructor.
     *
     * @param message the UML element
     */
    public MessageNotation(Object message) {
        if (!Model.getFacade().isAMessage(message)) {
            throw new IllegalArgumentException("This is not an Message.");
        }
    }

    @Override
    public void initialiseListener(Object umlMessage) {
        addElementListener(umlMessage,
                new String[] {"activator", "predecessor", "successor", 
                    "sender", "receiver", "action", "name"});
        Object action = Model.getFacade().getAction(umlMessage);
        if (action != null) {
            addElementListener(action,
                    new String[] {"remove", "recurrence", "script", 
                        "actualArgument", "signal", "operation"});
            List args = Model.getFacade().getActualArguments(action);
            for (Object argument : args) {
                addElementListener(argument,
                        new String[] {"remove", "value"});
            }
            if (Model.getFacade().isACallAction(action)) {
                Object operation = Model.getFacade().getOperation(action);
                if (Model.getFacade().isAOperation(operation)) {
                    addElementListener(operation,
                            new String[] {"name"});
                }
            }
            if (Model.getFacade().isASendAction(action)) {
                Object signal = Model.getFacade().getSignal(action);
                if (Model.getFacade().isASignal(signal)) {
                    addElementListener(signal,
                            new String[] {"name"});
                }
            }
        }
    }
}
