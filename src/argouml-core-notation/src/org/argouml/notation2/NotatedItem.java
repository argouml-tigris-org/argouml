/* $Id$
 *******************************************************************************
 * Copyright (c) 2011 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Bob Tarling
 *******************************************************************************
 */

package org.argouml.notation2;

public interface NotatedItem {

    void notationTextChanged(NotationTextEvent event);
    Object getMetaType();
    Object getOwner();
    NotationType getNotationType();
    NotationLanguage getNotationLanguage();
}
