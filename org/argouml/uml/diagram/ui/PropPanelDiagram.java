// $Id$
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

package org.argouml.uml.diagram.ui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

import org.argouml.application.api.Argo;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.ui.ArgoDiagram;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.ui.PropPanel;
import org.argouml.uml.ui.UMLTextField;
import org.argouml.uml.ui.UMLTextProperty;
import org.argouml.util.ConfigLoader;

import org.tigris.gef.base.Editor;
import org.tigris.gef.base.Globals;

public class PropPanelDiagram extends PropPanel 
implements ChangeListener{

    /**
     * Constructs a proppanel with a given name.
     * @see org.argouml.ui.TabSpawnable#TabSpawnable(String)
     */
    protected PropPanelDiagram(String diagramName) {
        super(diagramName, ConfigLoader.getTabPropsOrientation());
        
        JTextField field =
	    new UMLTextField(this, new UMLTextProperty(ArgoDiagram.class,
						       "name",
						       "getName",
						       "setName"));
        

        addField(Argo.localize("UMLMenu", "label.name"), field);
        
        JSlider zoom = new JSlider(JSlider.HORIZONTAL,
                                      0, 500, 100);
        zoom.setMajorTickSpacing(50);
        zoom.setMinorTickSpacing(5);
        zoom.setPaintTicks(true);
        zoom.setPaintLabels(true);

        zoom.addChangeListener(this);
        addField(Argo.localize("UMLMenu", "label.zoom"), zoom);
    }
    
    /**
     * Default constructor if there is no child of this class that can show the
     * diagram.
     */
    public PropPanelDiagram() {
        this("Diagram");
    }

    public void removeElement() {
        Object target = getTarget();
        if (target instanceof ArgoDiagram) {
            try {
                ArgoDiagram diagram = (ArgoDiagram) target;
                Project project =
		    ProjectManager.getManager().getCurrentProject();
                //
                //  can't easily find owner of diagram
                //    set new target to the model
                //
                Object newTarget = project.getModel();
                project.moveToTrash(diagram);
                TargetManager.getInstance().setTarget(newTarget);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

public void stateChanged(ChangeEvent e) {
    JSlider source = (JSlider)e.getSource();
    //if (!source.getValueIsAdjusting()) {
        double zoomPercentage = (double)source.getValue()/100.0d;
        
        Editor ed = (Editor) Globals.curEditor();
        if (ed == null) return;
        if(zoomPercentage <= 0.0) return;
        
        ed.setScale(zoomPercentage);
        ed.damageAll();
    //}
}

} /* end class PropPanelDiagram */
