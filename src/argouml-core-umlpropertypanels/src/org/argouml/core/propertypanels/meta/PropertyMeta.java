/* $Id$
 *******************************************************************************
 * Copyright (c) 2010 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Christian López Espínola 
 *    Bob Tarling
 *******************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */
// Copyright (c) 2008 The Regents of the University of California. All
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

package org.argouml.core.propertypanels.meta;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Entry of the XML that defines the property panels
 *
 * @author penyaskito
 */
public class PropertyMeta {
    
    private String controlType;
    private String type;
    private String name;
    private String label;
    
    private List<CheckBoxMeta> checkboxes = new LinkedList<CheckBoxMeta>();
    
    // TODO: this is a tree node, we must refine the tree structure
    
    public PropertyMeta(
            final String controlType,
            final String name,
            final String type,
            final String label) {
        this.controlType = controlType;
        this.name = name;
        this.type = type;
        
        if (label != null) {
            this.label = label;
        } else if (name != null) {
            // TODO: Getting the labels is more complex than this.
            // Options: 
            // 1) Make the logic here (plurals, camel-to-hyphens...)
            // 2) Change the labels in i18n files for every language.
            this.label = "label." + name.toLowerCase();
        }
    }
    
    public String getControlType() {
        return controlType;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }
    
    public String getLabel() {
    	return label;
    }
    
    public List<CheckBoxMeta> getCheckboxes() {
        return Collections.unmodifiableList(checkboxes);
    }
    
    public void addCheckbox(CheckBoxMeta child) {
        checkboxes.add(child);
    }
}
