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

// File: FigStateVertex.java
// Classes: FigStateVertex
// Original Author: jrobbins@ics.uci.edu
// $Id$

package org.argouml.uml.diagram.state.ui;

import org.argouml.ui.ProjectBrowser;
import org.argouml.uml.diagram.state.StateDiagramGraphModel;
import org.argouml.uml.diagram.ui.FigNodeModelElement;
import org.argouml.uml.diagram.ui.UMLDiagram;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.presentation.Fig;
import ru.novosoft.uml.behavior.state_machines.MCompositeState;
import ru.novosoft.uml.behavior.state_machines.MStateVertex;

/** Abstract class to with common behavior for nestable nodes in UML
    MState diagrams. */

public abstract class FigStateVertex extends FigNodeModelElement {

  ////////////////////////////////////////////////////////////////
  // constructors

  public FigStateVertex() {  }

  public FigStateVertex(GraphModel gm, Object node) {
    this();
    setOwner(node);
  }

  ////////////////////////////////////////////////////////////////
  // nestable nodes

  public void setEnclosingFig(Fig encloser) {
    super.setEnclosingFig(encloser);
    if (!(getOwner() instanceof MStateVertex)) return;
    MStateVertex sv = (MStateVertex) getOwner();
    MCompositeState m = null;
    if (encloser != null && (encloser.getOwner() instanceof MCompositeState)) {
      m = (MCompositeState) encloser.getOwner();
     }
    else {
      ProjectBrowser pb = ProjectBrowser.TheInstance;
      if (pb.getTarget() instanceof UMLDiagram) {
        try {
	  GraphModel gm = ((UMLDiagram)pb.getTarget()).getGraphModel();
	  StateDiagramGraphModel sdgm =  (StateDiagramGraphModel) gm;
	  m = (MCompositeState) sdgm.getMachine().getTop();
        }
        catch(Exception ex) {
          ex.printStackTrace();
        }
      }
    }	
    if (m!=null) 
	sv.setContainer(m);
  }


} /* end class FigStateVertex */
