// $Id$
/*******************************************************************************
 * Copyright (c) 2011-2012 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Bob Tarling
 *******************************************************************************/

package org.argouml.model.mdr;

import org.argouml.model.MessageSort;
import org.omg.uml.behavioralelements.commonbehavior.CallAction;
import org.omg.uml.behavioralelements.commonbehavior.CreateAction;
import org.omg.uml.behavioralelements.commonbehavior.DestroyAction;
import org.omg.uml.behavioralelements.commonbehavior.ReturnAction;
import org.omg.uml.behavioralelements.commonbehavior.SendAction;

/**
 * The implementation of the OrderingKindEUMLImpl.java for EUML2.
 */
class MessageSortMDRImpl implements MessageSort {

    public Object getASynchCall() {
        return CallAction.class;
    }

    public Object getCreateMessage() {
        return CreateAction.class;
    }

    public Object getDeleteMessage() {
        return DestroyAction.class;
    }

    public Object getReply() {
        return ReturnAction.class;
    }

    public Object getSynchCall() {
        return CallAction.class;
    }
    
    public Object getASynchSignal() {
        return SendAction.class;
    }
}
