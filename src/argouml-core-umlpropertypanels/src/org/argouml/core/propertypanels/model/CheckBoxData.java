/* $Id$
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
    private String propertyName;
    
    public CheckBoxData(
            final Class<?> type,
            final String propertyName) {
        this.type = type;
        this.propertyName = propertyName;
    }
    
    public Class<?> getType() {
        return type;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public String getLabel() {
        return propertyName;
    }
}
