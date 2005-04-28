// $Id$
// Copyright (c) 1996-2005 The Regents of the University of California. All
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

import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import org.argouml.ui.ArgoDiagram;
import org.argouml.uml.diagram.ui.UMLDiagram;
import org.argouml.uml.generator.Generator2;
import org.argouml.uml.generator.ui.ClassGenerationDialog;

/**
 * Action to trigger code generation for all classes/interfaces in the
 * project, which have a source code path set in tagged value 'src_path'.
 *
 * @stereotype singleton
 */
public class ActionGenerateProjectCode extends UMLAction {

    ////////////////////////////////////////////////////////////////
    // static variables

    /**
     * The singleton.
     */
    private static final ActionGenerateProjectCode SINGLETON =
	new ActionGenerateProjectCode();


    ////////////////////////////////////////////////////////////////
    // constructors

    /**
     *  The constructor.
     */
    protected ActionGenerateProjectCode() {
	super("action.generate-code-for-project", true, NO_ICON);
    }


    ////////////////////////////////////////////////////////////////
    // main methods

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent ae) {
	Vector classes = new Vector();
	ArgoDiagram activeDiagram =
	    ProjectManager.getManager().getCurrentProject().getActiveDiagram();
	if (!(activeDiagram instanceof UMLDiagram)) {
	    return;
	}
	Object/*MNamespace*/ ns = ((UMLDiagram) activeDiagram).getNamespace();
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
	    Object/*MClassifier*/ cls = iter.next();
	    if (isCodeRelevantClassifier(cls)) {
		classes.addElement(cls);
	    }
	}
	ClassGenerationDialog cgd = new ClassGenerationDialog(classes, true);
	cgd.setVisible(true);
    }

    /**
     * @see org.argouml.uml.ui.UMLAction#shouldBeEnabled()
     */
    public boolean shouldBeEnabled() {
	ArgoDiagram activeDiagram =
	    ProjectManager.getManager().getCurrentProject().getActiveDiagram();
	return super.shouldBeEnabled() && (activeDiagram instanceof UMLDiagram);
    }

    private boolean isCodeRelevantClassifier(Object/*MClassifier*/ cls) {
	String path = Generator2.getCodePath(cls);
	String name = Model.getFacade().getName(cls);
	if (name == null
            || name.length() == 0
            || Character.isDigit(name.charAt(0))) {
	    return false;
	}
	if (path != null) {
	    return (path.length() > 0);
	}
	Object/*MNamespace*/ parent = Model.getFacade().getNamespace(cls);
	while (parent != null) {
	    path = Generator2.getCodePath(parent);
	    if (path != null) {
		return (path.length() > 0);
	    }
	    parent = Model.getFacade().getNamespace(parent);
	}
	return false;
    }


    /**
     * @return Returns the SINGLETON.
     */
    public static ActionGenerateProjectCode getInstance() {
        return SINGLETON;
    }

} /* end class ActionGenerateProjectCode */
