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
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.diagram.static_structure.ui.UMLClassDiagram;
import org.argouml.uml.generator.ui.ClassGenerationDialog;

/** Action to trigger code generation for one or more classes.
 *  @stereotype singleton
 */
public class ActionGenerateAll extends UMLAction {

    ////////////////////////////////////////////////////////////////
    // static variables

    public static ActionGenerateAll SINGLETON = new ActionGenerateAll();


    ////////////////////////////////////////////////////////////////
    // constructors

    protected ActionGenerateAll() {
	super("action.generate-all-classes", NO_ICON);
    }


    ////////////////////////////////////////////////////////////////
    // main methods

    public void actionPerformed(ActionEvent ae) {
	ProjectBrowser pb = ProjectBrowser.getInstance();
	ArgoDiagram activeDiagram =
	    ProjectManager.getManager().getCurrentProject().getActiveDiagram();
	if (!(activeDiagram instanceof UMLClassDiagram)) return;

	UMLClassDiagram d = (UMLClassDiagram) activeDiagram;
	Vector classes = new Vector();
	Vector nodes = d.getNodes();
	java.util.Enumeration enum = nodes.elements();
	while (enum.hasMoreElements()) {
	    Object owner = enum.nextElement();
	    if (!ModelFacade.isAClass(owner) && !ModelFacade.isAInterface(owner))
		continue;
	    String name = ModelFacade.getName(owner);
	    if (name == null || name.length() == 0 || Character.isDigit(name.charAt(0)))
		continue;
            classes.addElement(owner);
	}
	 
	if (classes.size() == 0) {
            
            Iterator selectedObjects = 
                TargetManager.getInstance().getTargets().iterator();
       
	    while (selectedObjects.hasNext()) {
		Object selected = selectedObjects.next();
		if (ModelFacade.isAPackage(selected)) {
		    addCollection(ModelManagementHelper.getHelper().getAllModelElementsOfKind(selected, (Class)ModelFacade.CLASS), classes);
		    addCollection(ModelManagementHelper.getHelper().getAllModelElementsOfKind(selected, (Class)ModelFacade.INTERFACE), classes);
		} else if (ModelFacade.isAClass(selected) || ModelFacade.isAInterface(selected)) {
		    if (!classes.contains(selected))
			classes.addElement(selected);
		}
	    }
	}
	ClassGenerationDialog cgd = new ClassGenerationDialog(classes);
	cgd.show();
    }

    public boolean shouldBeEnabled() {
	ProjectBrowser pb = ProjectBrowser.getInstance();
	ArgoDiagram activeDiagram =
	    ProjectManager.getManager().getCurrentProject().getActiveDiagram();;
	return super.shouldBeEnabled()
	    && (activeDiagram instanceof UMLClassDiagram);
    }
    
    /**
     *Adds elements from collection without duplicates
     */
    private void addCollection(Collection c, Vector v) {
        for (Iterator it = c.iterator(); it.hasNext();) {
            Object o = it.next();
            if (!v.contains(o)) v.addElement(o);
        }
    }
} /* end class ActionGenerateAll */