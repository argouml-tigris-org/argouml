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



// File: UMLCollaborationDiagram.java
// Classes: UMLCollaborationDiagram
// Original Author: agauthie@ics.uci.edu
// $Id$


package uci.uml.visual;

import java.util.*;
import java.awt.*;
import java.beans.*;
import javax.swing.*;

import uci.gef.*;
import uci.graph.*;
import uci.ui.*;
import uci.uml.ui.*;
import uci.uml.Model_Management.*;
import uci.uml.Foundation.Core.*;
import uci.uml.Behavioral_Elements.Collaborations.*;


public class UMLCollaborationDiagram extends UMLDiagram {

  ////////////////
  // actions for toolbar


  protected static Action _actionClassifierRole =
  new CmdCreateNode(ClassifierRole.class, "ClassifierRole");

  protected static Action _actionAssoc =
  new CmdSetMode(ModeCreatePolyEdge.class,
		 "edgeClass", AssociationRole.class,
		 "AssociationRole");


  ////////////////////////////////////////////////////////////////
  // contructors
  protected static int _CollaborationDiagramSerial = 1;


  public UMLCollaborationDiagram() {
    try { setName("collaboration diagram " + _CollaborationDiagramSerial++); }
    catch (PropertyVetoException pve) { }
  }

  public UMLCollaborationDiagram(Namespace m) {
    this();
    setNamespace(m);
  }

  public int getNumMessages() {
    Layer lay = getLayer();
    Vector figs = lay.getContents();
    int res = 0;
    int size = figs.size();
    for (int i=0; i < size; i++) {
      Fig f = (Fig) figs.elementAt(i);
      if (f.getOwner() instanceof Message) res++;
    }
    return res;
  }

  public void setNamespace(Namespace m) {
    super.setNamespace(m);
    CollabDiagramGraphModel gm = new CollabDiagramGraphModel();
    gm.setNamespace(m);
    setGraphModel(gm);
    LayerPerspective lay = new LayerPerspective(m.getName().getBody(), gm);
    setLayer(lay);
    CollabDiagramRenderer rend = new CollabDiagramRenderer(); // singleton
    lay.setGraphNodeRenderer(rend);
    lay.setGraphEdgeRenderer(rend);
  }

  /** initialize the toolbar for this diagram type */
  protected void initToolBar() {
    _toolBar = new ToolBar();
    _toolBar.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
//     _toolBar.add(Actions.Cut);
//     _toolBar.add(Actions.Copy);
//     _toolBar.add(Actions.Paste);
//     _toolBar.addSeparator();

    _toolBar.add(_actionSelect);
    _toolBar.add(_actionBroom);
    _toolBar.addSeparator();

    _toolBar.add(_actionClassifierRole);
    _toolBar.addSeparator();
    _toolBar.add(_actionAssoc);
    _toolBar.add(Actions.AddMessage);
    // other actions
    _toolBar.addSeparator();

    _toolBar.add(_actionRectangle);
    _toolBar.add(_actionRRectangle);
    _toolBar.add(_actionCircle);
    _toolBar.add(_actionLine);
    _toolBar.add(_actionText);
    _toolBar.add(_actionPoly);
    _toolBar.add(_actionSpline);
    _toolBar.add(_actionInk);
    _toolBar.addSeparator();

    _toolBar.add(_diagramName);
  }

} /* end class UMLCollaborationDiagram */
