// Copyright (c) 1996-99 The Regents of the University of California. All
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

import java.awt.*;
import javax.swing.*;

import org.tigris.gef.base.*;
import org.tigris.gef.util.Util;

import org.argouml.application.api.*;
import org.argouml.ui.*;
import org.argouml.uml.ui.*;
import org.argouml.kernel.*;

public class PropPanelDiagram extends PropPanel  {

    public PropPanelDiagram() {
	super("Diagram",null, 2);

	addCaption(Argo.localize("UMLMenu", "label.name"),1,0,0);
	addField(new UMLTextField(this,
				  new UMLTextProperty(ArgoDiagram.class,"name","getName","setName")),1,0,0);
	//
	//   filler to take up rest of panel
	addCaption(new JPanel(),1,0,1);
	new PropPanelButton(this,buttonPanel,_navBackIcon, Argo.localize("UMLMenu", "button.go-back"),"navigateBackAction","isNavigateBackEnabled");
	new PropPanelButton(this,buttonPanel,_navForwardIcon, Argo.localize("UMLMenu", "button.go-forward"),"navigateForwardAction","isNavigateForwardEnabled");
    }

    public void removeElement() {
        Object target = getTarget();
        if(target instanceof ArgoDiagram) {
          try {
            ArgoDiagram diagram = (ArgoDiagram) target;
            Project project = ProjectManager.getManager().getCurrentProject();
            //
            //  can't easily find owner of diagram
            //    set new target to the model
            //
            Object newTarget = project.getModel();
            project.moveToTrash(diagram);
            navigateTo(newTarget);
          }
          catch(Exception e) {
            e.printStackTrace();
          }
        }
    }

    protected boolean isAcceptibleBaseMetaClass(String baseClass) {
	return false;
    }


} /* end class PropPanelDiagram */
