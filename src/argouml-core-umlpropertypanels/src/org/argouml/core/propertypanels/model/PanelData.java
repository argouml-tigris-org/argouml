/* $Id: $
 *****************************************************************************
 * Copyright (c) 2009 Contributors - see below
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

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class PanelData  {
    
    private final Class<?> clazz;
    private final List<ControlData> properties;
    private final String name;
    
    public PanelData(Class<?> clazz, String name) {
        this.clazz = clazz;
        this.name = name;
        properties = new LinkedList<ControlData>();
    }
    
    public void addControlData(ControlData record) {
        properties.add(record);
    }
    
    public Class<?> getClazz() {
        return clazz;
    }    
    
    public String getName() {
	return name;
    }
    
    public List<ControlData> getProperties () {
        return Collections.unmodifiableList(properties);
    }
}
