// $Id$
// Copyright (c) 1996-2001 The Regents of the University of California. All
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
import org.argouml.model.ModelFacade;
import org.argouml.model.uml.modelmanagement.ModelManagementHelper;
import org.argouml.ui.ArgoDiagram;
import org.argouml.ui.ProjectBrowser;
import org.argouml.uml.diagram.ui.UMLDiagram;
import org.argouml.uml.generator.Generator;
import org.argouml.uml.generator.ui.ClassGenerationDialog;

import ru.novosoft.uml.foundation.core.MClassifier;
import ru.novosoft.uml.foundation.core.MNamespace;

/** Action to trigger code generation for all classes/interfaces in the
 *  project, which have a source code path set in tagged value 'src_path'
 *  @stereotype singleton
 */
public class ActionGenerateProjectCode extends UMLAction {

    ////////////////////////////////////////////////////////////////
    // static variables

    public static ActionGenerateProjectCode SINGLETON =
	new ActionGenerateProjectCode();


    ////////////////////////////////////////////////////////////////
    // constructors

    protected ActionGenerateProjectCode() {
	super("action.generate-code-for-project", NO_ICON);
    }


    ////////////////////////////////////////////////////////////////
    // main methods

    public void actionPerformed(ActionEvent ae) {
	Vector classes = new Vector();
	// The following lines should be substituted by the following
	// 2 commented lines.  (This is because getting the project
	// still does not seem to work...)
	ProjectBrowser pb = ProjectBrowser.getInstance();
	ArgoDiagram activeDiagram =
	    ProjectManager.getManager().getCurrentProject().getActiveDiagram();
	if (!(activeDiagram instanceof org.argouml.uml.diagram.ui.UMLDiagram))
	    return;
	Object/*MNamespace*/ ns =(MNamespace)
	    ((org.argouml.uml.diagram.ui.UMLDiagram) activeDiagram).getNamespace();
	if (ns == null) return;
	while (ModelFacade.getNamespace(ns) != null) ns = ModelFacade.getNamespace(ns);
	Collection elems =
	    ModelManagementHelper.getHelper().getAllModelElementsOfKind(ns, MClassifier.class);
	//Project p = ProjectManager.getManager().getCurrentProject();
	//Collection elems =
	//ModelManagementHelper.getHelper().getAllModelElementsOfKind(MClassifier.class);
	Iterator iter = elems.iterator();
	while (iter.hasNext()) {
	    Object/*MClassifier*/ cls = (MClassifier) iter.next();
	    if (isCodeRelevantClassifier(cls)) {
		classes.addElement(cls);
	    }
	}
	ClassGenerationDialog cgd = new ClassGenerationDialog(classes, true);
	cgd.show();
    }

    public boolean shouldBeEnabled() {
	ProjectBrowser pb = ProjectBrowser.getInstance();
	ArgoDiagram activeDiagram =
	    ProjectManager.getManager().getCurrentProject().getActiveDiagram();
	return super.shouldBeEnabled() && (activeDiagram instanceof UMLDiagram);
    }

    private boolean isCodeRelevantClassifier(Object/*MClassifier*/ cls) {
	String path = Generator.getCodePath(cls);
	String name = ModelFacade.getName(cls);
	if (name == null || name.length() == 0 || Character.isDigit(name.charAt(0))) {
	    return false;
	}
	if (path != null) {
	    return (path.length() > 0);
	}
	Object/*MNamespace*/ parent = ModelFacade.getNamespace(cls);
	while (parent != null) {
	    path = Generator.getCodePath(parent);
	    if (path != null) {
		return (path.length() > 0);
	    }
	    parent = ModelFacade.getNamespace(parent);
	}
	return false;
    }

} /* end class ActionGenerateProjectCode */