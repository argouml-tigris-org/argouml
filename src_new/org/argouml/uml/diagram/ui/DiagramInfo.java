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
import javax.swing.plaf.metal.MetalLookAndFeel;
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

    protected Diagram _diagram = null;
    private JLabel _name = null;

    ////////////////////////////////////////////////////////////////
    // constructor

    public DiagramInfo(Diagram d) {
	_diagram = d;
	//setBorder(new EtchedBorder());
    }

    public JComponent getJComponent() {
	_name = new JLabel("");
	setLayout(new BorderLayout());
	_name.setFont(MetalLookAndFeel.getSubTextFont());
	add(_name, BorderLayout.CENTER);
	updateName();
	return this;
    }

    ////////////////////////////////////////////////////////////////
    // updates
    public void updateName() {
	if (_name == null)
	    return;

	String type = "Diagram";
	if (_diagram instanceof UMLClassDiagram)
	    type = "Class Diagram";
	if (_diagram instanceof UMLStateDiagram)
	    type = "State Diagram";
	if (_diagram instanceof UMLUseCaseDiagram)
	    type = "Use Case Diagram";
	if (_diagram instanceof UMLActivityDiagram)
	    type = "Activity Diagram";
	if (_diagram instanceof UMLCollaborationDiagram)
	    type = "Collaboration Diagram";
	if (_diagram instanceof UMLSequenceDiagram)
	    type = "Sequence Diagram";
	if (_diagram instanceof UMLDeploymentDiagram)
	    type = "Deployment Diagram";
    
	_name.setText(type + ": " + _diagram.getName());
    }

} /* end class DiagramInfo */
