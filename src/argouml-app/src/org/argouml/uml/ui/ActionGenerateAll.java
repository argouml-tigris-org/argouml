/* $Id$
 *****************************************************************************
 * Copyright (c) 2009-2012 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Bob Tarling
 *    Thomas Neustupny
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

// Copyright (c) 1996-2008 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies.  This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason.  IN NO EVENT SHALL THE
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

package org.argouml.uml.ui;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.Action;

import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.ui.UndoableAction;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.diagram.ArgoDiagram;
import org.argouml.uml.diagram.DiagramUtils;
import org.argouml.uml.diagram.static_structure.ui.UMLClassDiagram;
import org.argouml.uml.generator.ui.ClassGenerationDialog;

/**
 * Action to trigger code generation for one or more classes.
 * <p>
 * In fact, only all named classes and interfaces
 * on the active diagram are generated.
 * Or, if this delivers an empty collection, all selected classes, interfaces
 * and the contents of selected packages are generated
 * (independent if they are named or not). <p>
 * TODO: Implement a more logical behaviour.
 */
public class ActionGenerateAll extends UndoableAction {


    /**
     * Constructor.
     */
    public ActionGenerateAll() {
        super(Translator.localize("action.generate-code"), null);
        // Set the tooltip string:
        putValue(Action.SHORT_DESCRIPTION, 
                Translator.localize("action.generate-code"));
    }


    /*
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent ae) {
    	super.actionPerformed(ae);
	ArgoDiagram activeDiagram = DiagramUtils.getActiveDiagram();
	if (!(activeDiagram instanceof UMLClassDiagram)) {
	    return;
	}

	UMLClassDiagram d = (UMLClassDiagram) activeDiagram;
	List classes = new ArrayList();
	List nodes = d.getNodes();
	for (Object owner : nodes) {
	    if (!Model.getFacade().isAClass(owner)
                    && !Model.getFacade().isAInterface(owner)
                    && !Model.getFacade().isAEnumeration(owner)) {
                continue;
            }
	    String name = Model.getFacade().getName(owner);
	    if (name == null
		|| name.length() == 0
		|| Character.isDigit(name.charAt(0))) {

		continue;

	    }
            classes.add(owner);
	}

	if (classes.size() == 0) {

            Collection selectedObjects =
                TargetManager.getInstance().getTargets();
            for (Object selected : selectedObjects) {
		if (Model.getFacade().isAPackage(selected)) {
		    addCollection(Model.getModelManagementHelper()
				  .getAllModelElementsOfKind(
                                      selected,
		                      Model.getMetaTypes().getUMLClass()),
				  classes);
		    addCollection(Model.getModelManagementHelper()
				  .getAllModelElementsOfKind(
                                      selected,
			              Model.getMetaTypes().getInterface()),
				  classes);
		} else if (Model.getFacade().isAClass(selected)
			   || Model.getFacade().isAInterface(selected)
                           || Model.getFacade().isAEnumeration(selected)) {
		    if (!classes.contains(selected)) {
		        classes.add(selected);
		    }
		}
	    }
	}
	ClassGenerationDialog cgd = new ClassGenerationDialog(classes);
	cgd.setVisible(true);
    }

    /**
     * @return true if the action is enabled and the active diagram is a class
     *         diagram
     * @see org.tigris.gef.undo.UndoableAction#isEnabled()
     */
    @Override
    public boolean isEnabled() {
        // TODO: this seems to be called at startup only so no check so far
        return true;
	//ArgoDiagram activeDiagram = DiagramUtils.getActiveDiagram();
	//return (activeDiagram instanceof UMLClassDiagram);
    }

    /**
     * Adds elements from collection without duplicates.
     */
    private void addCollection(Collection c, Collection v) {
        for (Object o : c) {
            if (!v.contains(o)) {
                v.add(o);
            }
        }
    }
}
