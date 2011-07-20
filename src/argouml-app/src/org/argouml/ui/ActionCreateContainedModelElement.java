/* $Id$
 *****************************************************************************
 * Copyright (c) 2009 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    bobtarling
 *****************************************************************************
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

package org.argouml.ui;

import java.awt.event.ActionEvent;

import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.ui.AbstractActionNewModelElement;

/**
 * An action to create a model element to be contained by the 
 * target model element.
 *
 * @author Scott Roberts
 */
public class ActionCreateContainedModelElement
            extends AbstractActionNewModelElement {

    private final Object metaType; 
    private final String property; 

    /**
     * Construct the action.
     * 
     * @param theMetaType the element to be created
     * @param container the container that will own the new element
     */
    public ActionCreateContainedModelElement(
            Object theMetaType, 
            Object container) {
        this(theMetaType, container,
                "button.new-"
                + Model.getMetaTypes().getName(theMetaType).toLowerCase());
    }
    
    
    /**
     * Construct the action.
     * 
     * @param theMetaType the element to be created
     * @param container the container that will own the new element
     * @param menuDescr the description for the menu item label.
     */
    public ActionCreateContainedModelElement(
            Object theMetaType, 
            Object container,
            String menuDescr) {
        super(menuDescr);
        
        metaType = theMetaType;
        property = null;
        
        setTarget(container);
    }

    /**
     * Construct the action.
     * 
     * @param theMetaType the element to be created
     * @param container the container that will own the new element
     * @param property the property name that represents the new element with
     *        the container
     * @param menuDescr the description for the menu item label.
     */
    public ActionCreateContainedModelElement(
            Object theMetaType, 
            Object container,
            String property,
            String menuDescr) {
        super(menuDescr);
        
        metaType = theMetaType;
        this.property = property;
        
        setTarget(container);
    }

    public void actionPerformed(ActionEvent e) {
        // TODO - lets pass in Project as a constructor argument
        Project project = ProjectManager.getManager().getCurrentProject();
        
        Object newElement =
            Model.getUmlFactory().buildNode(
                    metaType,
                    getTarget(),
                    property,
                    project.getDefaults());
            
        TargetManager.getInstance().setTarget(newElement);                
    }
}
