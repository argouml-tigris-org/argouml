// $Id$
// Copyright (c) 1996-2003 The Regents of the University of California. All
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

// File: DiagramInfo.java
// Classes: DiagramInfo
// Original Author: jrobbins@ics.uci.edy

package org.argouml.uml.diagram.ui;

import java.awt.BorderLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;

import org.argouml.ui.LookAndFeelMgr;
import org.argouml.uml.diagram.activity.ui.UMLActivityDiagram;
import org.argouml.uml.diagram.collaboration.ui.UMLCollaborationDiagram;
import org.argouml.uml.diagram.deployment.ui.UMLDeploymentDiagram;
import org.argouml.uml.diagram.sequence.ui.UMLSequenceDiagram;
import org.argouml.uml.diagram.state.ui.UMLStateDiagram;
import org.argouml.uml.diagram.static_structure.ui.UMLClassDiagram;
import org.argouml.uml.diagram.use_case.ui.UMLUseCaseDiagram;
import org.tigris.gef.base.Diagram;

public class DiagramInfo extends JComponent {

    ////////////////////////////////////////////////////////////////
    // instance variables

    private Diagram diagram = null;
    private JLabel name = null;

    ////////////////////////////////////////////////////////////////
    // constructor

    /**
     * The constructor.
     * 
     * @param d the diagram
     */
    public DiagramInfo(Diagram d) {
	diagram = d;
	//setBorder(new EtchedBorder());
    }

    /**
     * @return the diagram info
     */
    public JComponent getJComponent() {
	name = new JLabel("");
	setLayout(new BorderLayout());
	name.setFont(LookAndFeelMgr.getInstance().getSmallFont());
	add(name, BorderLayout.CENTER);
	updateName();
	return this;
    }

    ////////////////////////////////////////////////////////////////
    // updates
    /**
     * the name of the diagram
     */
    public void updateName() {
	if (name == null)
	    return;

	String type = "Diagram";
	if (diagram instanceof UMLClassDiagram)
	    type = "Class Diagram";
	if (diagram instanceof UMLStateDiagram)
	    type = "Statechart Diagram";
	if (diagram instanceof UMLUseCaseDiagram)
	    type = "Use Case Diagram";
	if (diagram instanceof UMLActivityDiagram)
	    type = "Activity Diagram";
	if (diagram instanceof UMLCollaborationDiagram)
	    type = "Collaboration Diagram";
	if (diagram instanceof UMLSequenceDiagram)
	    type = "Sequence Diagram";
	if (diagram instanceof UMLDeploymentDiagram)
	    type = "Deployment Diagram";

	name.setText(type + ": " + diagram.getName());
    }

} /* end class DiagramInfo */
