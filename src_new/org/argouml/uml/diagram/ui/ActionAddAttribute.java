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

package org.argouml.uml.diagram.ui;

import java.awt.event.ActionEvent;
import java.util.Collection;

import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import org.argouml.ui.ProjectBrowser;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.ui.UMLAction;

/**
 * Action to add an attribute to a classifier.<p>
 *
 * @stereotype singleton
 */
public class ActionAddAttribute extends UMLAction {

    private static ActionAddAttribute singleton = new ActionAddAttribute();

    /**
     * The constructor for this class.
     */
    public ActionAddAttribute() {
        super("button.new-attribute", true, HAS_ICON);
    }

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent ae) {
	Object target = TargetManager.getInstance().getModelTarget();
	Object/*MClassifier*/ cls = null;

	if (Model.getFacade().isAClassifier(target)
            || Model.getFacade().isAAssociationEnd(target)) {
	    cls = target;
	} else if (Model.getFacade().isAFeature(target)
		 && Model.getFacade().isAClass(Model.getFacade().getOwner(target))) {
	    cls = Model.getFacade().getOwner(target);
	} else {
	    return;
	}

	Collection propertyChangeListeners =
	    ProjectManager.getManager()
	    	.getCurrentProject().findFigsForMember(cls);
	Object intType =
	    ProjectManager.getManager()
	    	.getCurrentProject().findType("int");
	Object model =
	    ProjectManager.getManager()
	    	.getCurrentProject().getModel();
	Object attr =
	    Model.getCoreFactory().buildAttribute(cls, model, intType,
	            propertyChangeListeners);
	TargetManager.getInstance().setTarget(attr);
	super.actionPerformed(ae);
    }

    /**
     * @see org.argouml.uml.ui.UMLAction#shouldBeEnabled()
     */
    public boolean shouldBeEnabled() {
	ProjectBrowser pb = ProjectBrowser.getInstance();
	Object target =  TargetManager.getInstance().getModelTarget();
	/*
	if (target instanceof MInterface) {
		return Notation.getDefaultNotation().getName().equals("Java");
	}
	*/
	return super.shouldBeEnabled()
	       && (Model.getFacade().isAClass(target)
		   || (Model.getFacade().isAFeature(target)
		       && Model.getFacade().isAClass(Model.getFacade().getOwner(target)))
           || Model.getFacade().isAAssociationEnd(target));
    }
    /**
     * @return Returns the singleton.
     * @deprecated singleton use will be removed in 0.18.0.
     * Use the constructor instead.
     */
    public static ActionAddAttribute getSingleton() {
        return singleton;
    }
} /* end class ActionAddAttribute */
