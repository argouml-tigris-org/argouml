/* $Id$
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

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class PanelData  {
    
    private final Class<?> clazz;
    private final List<ControlData> properties;
    private final String name;
    private final Collection<Class<?>> newChildElements;
    private final Collection<Class<?>> newSiblingElements;
    private final boolean siblingNavigation;
    
    public PanelData(
	    final Class<?> clazz,
	    final String name,
	    final Collection<Class<?>> newChildElements,
	    final Collection<Class<?>> newSiblingElements,
	    final boolean siblingNavigation) {
	
        this.clazz = clazz;
        this.name = name;
        properties = new LinkedList<ControlData>();
        this.newChildElements = newChildElements;
        this.newSiblingElements = newSiblingElements;
        this.siblingNavigation = siblingNavigation;
    }
    
    public boolean isSiblingNavigation() {
        return siblingNavigation;
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

    public Collection<Class<?>> getNewChildElements() {
        return newChildElements;
    }

    public Collection<Class<?>> getNewSiblingElements() {
        return newSiblingElements;
    }
}
