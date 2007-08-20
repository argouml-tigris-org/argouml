// $Id:ActionGenerateAll.java 12583 2007-05-09 20:11:45Z mvw $
// Copyright (c) 1996-2006 The Regents of the University of California. All
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
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.swing.Action;

import org.argouml.i18n.Translator;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.diagram.ArgoDiagram;
import org.argouml.uml.diagram.static_structure.ui.UMLClassDiagram;
import org.argouml.uml.generator.ui.ClassGenerationDialog;
import org.tigris.gef.undo.UndoableAction;

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

    ////////////////////////////////////////////////////////////////
    // constructors

    /**
     * Constructor.
     */
    public ActionGenerateAll() {
        super(Translator.localize("action.generate-all-classes"), null);
        // Set the tooltip string:
        putValue(Action.SHORT_DESCRIPTION, 
                Translator.localize("action.generate-all-classes"));
    }


    ////////////////////////////////////////////////////////////////
    // main methods

    /*
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent ae) {
    	super.actionPerformed(ae);
	ArgoDiagram activeDiagram =
	    ProjectManager.getManager().getCurrentProject().getActiveDiagram();
	if (!(activeDiagram instanceof UMLClassDiagram)) {
	    return;
	}

	UMLClassDiagram d = (UMLClassDiagram) activeDiagram;
	Vector classes = new Vector();
	List nodes = d.getNodes();
	Iterator elems = nodes.iterator();
	while (elems.hasNext()) {
	    Object owner = elems.next();
	    if (!Model.getFacade().isAClass(owner)
		&& !Model.getFacade().isAInterface(owner)) {

		continue;

	    }
	    String name = Model.getFacade().getName(owner);
	    if (name == null
		|| name.length() == 0
		|| Character.isDigit(name.charAt(0))) {

		continue;

	    }
            classes.addElement(owner);
	}

	if (classes.size() == 0) {

            Iterator selectedObjects =
                TargetManager.getInstance().getTargets().iterator();

	    while (selectedObjects.hasNext()) {
		Object selected = selectedObjects.next();
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
			   || Model.getFacade().isAInterface(selected)) {
		    if (!classes.contains(selected)) {
		        classes.addElement(selected);
		    }
		}
	    }
	}
	ClassGenerationDialog cgd = new ClassGenerationDialog(classes);
	cgd.setVisible(true);
    }

    /**
     * @return true if the action is enabled and the diagram is a class diagram
     * @see org.tigris.gef.undo.UndoableAction#isEnabled()
     */
    public boolean isEnabled() {
	ArgoDiagram activeDiagram =
	    ProjectManager.getManager().getCurrentProject().getActiveDiagram();
	return super.isEnabled()
	    && (activeDiagram instanceof UMLClassDiagram);
    }

    /**
     * Adds elements from collection without duplicates.
     */
    private void addCollection(Collection c, Vector v) {
        for (Iterator it = c.iterator(); it.hasNext();) {
            Object o = it.next();
            if (!v.contains(o)) {
                v.addElement(o);
            }
        }
    }
} /* end class ActionGenerateAll */
