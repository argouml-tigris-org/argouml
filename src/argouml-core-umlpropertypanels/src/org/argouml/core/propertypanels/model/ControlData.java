/* $Id: $
 *******************************************************************************
 * Copyright (c) 2010 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Bob Tarling
 *******************************************************************************
 */

package org.argouml.core.propertypanels.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ControlData {
    
    private String controlType;
    private String name;
    private String label;
    
    private List<Class<?>> types = new ArrayList<Class<?>>();
    private List<CheckBoxData> checkboxes = new ArrayList<CheckBoxData>();
    
    public ControlData(
            final String controlType,
            final String name,
            final String label) {
        this.controlType = controlType;
        this.name = name;
        
        if (label != null && label.length() > 0) {
            this.label = label;
        } else {
            this.label = "label." + name.toLowerCase();
        }
    }
    
    public String getControlType() {
        return controlType;
    }

    public String getName() {
        return name;
    }
    
    public String getLabel() {
    	return label;
    }
    
    public List<CheckBoxData> getCheckboxes() {
        return Collections.unmodifiableList(checkboxes);
    }
    
    public List<Class<?>> getTypes() {
        return Collections.unmodifiableList(types);
    }
    
    public Class<?> getType() {
        return types.get(0);
    }
    
    public void addType(Class<?> type) {
        types.add(type);
    }
    
    public void addCheckbox(CheckBoxData child) {
        checkboxes.add(child);
    }
}
