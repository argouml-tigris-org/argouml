// Copyright (c) 1996-2002 The Regents of the University of California. All
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

import org.argouml.kernel.*;
import org.argouml.ui.*;
import org.argouml.uml.diagram.use_case.ui.*;

import ru.novosoft.uml.behavior.collaborations.MCollaboration;
import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.model_management.*;
import java.awt.event.*;
import java.beans.*;


public class ActionUseCaseDiagram extends UMLChangeAction {

    ////////////////////////////////////////////////////////////////
    // static variables
    
    public static ActionUseCaseDiagram SINGLETON = new ActionUseCaseDiagram(); 


    ////////////////////////////////////////////////////////////////
    // constructors

    public ActionUseCaseDiagram() { super("UseCaseDiagram"); }


    ////////////////////////////////////////////////////////////////
    // main methods

    public void actionPerformed(ActionEvent ae) {
	//_cmdCreateNode.doIt();
	Project p = ProjectBrowser.TheInstance.getProject();
	try {
		MNamespace ns = p.getCurrentNamespace();
		Object target = ProjectBrowser.TheInstance.getDetailsTarget();
		if (target instanceof MPackage && !(target instanceof MCollaboration)) ns = ((MPackage)target);
		if (ns instanceof MCollaboration) ns = p.getModel(); // no collabs allowed
	    ArgoDiagram d  = new UMLUseCaseDiagram(ns);
	    p.addMember(d);
	    ProjectBrowser.TheInstance.getNavPane().addToHistory(d);
	    ProjectBrowser.TheInstance.setTarget(d);
	}
	catch (PropertyVetoException pve) { }
	super.actionPerformed(ae);
    }
	

	/**
	 * @see org.argouml.uml.ui.UMLAction#shouldBeEnabled()
	 */
	public boolean shouldBeEnabled() {
		return ProjectBrowser.TheInstance.getProject() != null &&
			!(ProjectBrowser.TheInstance.getProject().getCurrentNamespace() instanceof MCollaboration);
	}

} /* end class ActionUseCaseDiagram */
