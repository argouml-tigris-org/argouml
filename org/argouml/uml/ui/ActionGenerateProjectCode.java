// $Id$
// Copyright (c) 1996-2007 The Regents of the University of California. All
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
import java.util.Vector;

import javax.swing.Action;

import org.argouml.i18n.Translator;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import org.argouml.uml.diagram.ArgoDiagram;
import org.argouml.uml.diagram.ui.UMLDiagram;
import org.argouml.uml.generator.GeneratorManager;
import org.argouml.uml.generator.ui.ClassGenerationDialog;
import org.tigris.gef.undo.UndoableAction;

/**
 * Action to trigger code generation for all classes/interfaces in the
 * project, which have a source code path set in tagged value 'src_path'.
 *
 * @stereotype singleton
 */
public class ActionGenerateProjectCode extends UndoableAction {

    ////////////////////////////////////////////////////////////////
    // constructors

    /**
     *  The constructor.
     */
    public ActionGenerateProjectCode() {
	    super(Translator.localize("action.generate-code-for-project"), 
	            null);
        // Set the tooltip string:
        putValue(Action.SHORT_DESCRIPTION, 
                Translator.localize("action.generate-code-for-project"));
    }


    ////////////////////////////////////////////////////////////////
    // main methods

    /*
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent ae) {
        super.actionPerformed(ae);
        Vector classes = new Vector();
	ArgoDiagram activeDiagram =
	    ProjectManager.getManager().getCurrentProject().getActiveDiagram();
	if (activeDiagram == null) {
	    return;
	}
	Object ns = ((UMLDiagram) activeDiagram).getNamespace();
	if (ns == null) {
	    return;
	}
	while (Model.getFacade().getNamespace(ns) != null) {
	    ns = Model.getFacade().getNamespace(ns);
	}
	Collection elems =
	    Model.getModelManagementHelper()
	    	.getAllModelElementsOfKind(
	    	        ns,
	    	        Model.getMetaTypes().getClassifier());
	//Project p = ProjectManager.getManager().getCurrentProject();
	//Collection elems =
	//ModelManagementHelper.getHelper()
        //    .getAllModelElementsOfKind(MClassifier.class);
	Iterator iter = elems.iterator();
	while (iter.hasNext()) {
	    Object cls = iter.next();
	    if (isCodeRelevantClassifier(cls)) {
		classes.addElement(cls);
	    }
	}
	ClassGenerationDialog cgd = new ClassGenerationDialog(classes, true);
	cgd.setVisible(true);
    }

    /**
     * Check if the diagram is enabled
     * 
     * @return true if enabled
     * @see org.tigris.gef.undo.UndoableAction#isEnabled()
     */
    public boolean isEnabled() {
        ArgoDiagram activeDiagram = ProjectManager.getManager()
				.getCurrentProject().getActiveDiagram();
        return super.isEnabled() && (activeDiagram != null);
    }

    /**
     * @param cls the classifier that is candidate for generation
     * @return true if the candidate is sound
     */
    private boolean isCodeRelevantClassifier(Object cls) {
        if (cls == null) {
            return false;
        }
        if (!Model.getFacade().isAClass(cls)
                && !Model.getFacade().isAInterface(cls)) {
            return false;
        }
        String path = GeneratorManager.getCodePath(cls);
        String name = Model.getFacade().getName(cls);
        if (name == null
            || name.length() == 0
            || Character.isDigit(name.charAt(0))) {
            return false;
        }
        if (path != null) {
            return (path.length() > 0);
        }
        Object parent = Model.getFacade().getNamespace(cls);
        while (parent != null) {
            path = GeneratorManager.getCodePath(parent);
            if (path != null) {
                return (path.length() > 0);
            }
            parent = Model.getFacade().getNamespace(parent);
        }
        return false;
    }


} /* end class ActionGenerateProjectCode */
