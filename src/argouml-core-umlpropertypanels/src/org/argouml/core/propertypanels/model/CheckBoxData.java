/* $Id: $
 *****************************************************************************
 * Copyright (c) 2010 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Bob Tarling
 *****************************************************************************
 */

package org.argouml.core.propertypanels.model;

public class CheckBoxData {
    
    private Class<?> type;
    private String name;
    
    public CheckBoxData(
            final Class<?> type,
            final String name) {
        this.type = type;
        this.name = name;
    }
    
    public Class<?> getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getLabel() {
        return name;
    }
}
