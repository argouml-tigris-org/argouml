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



// File: UMLUseCaseDiagram.java
// Classes: UMLUseCaseDiagram
// Original Author: your email here
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
import uci.uml.Behavioral_Elements.Use_Cases.*;


public class UMLUseCaseDiagram extends UMLDiagram {

  ////////////////
  // actions for toolbar


  protected static Action _actionActor = 
  new CmdCreateNode(Actor.class, "Actor");

  protected static Action _actionUseCase = 
  new CmdCreateNode(UseCase.class, "UseCase");

  protected static Action _actionAssoc =
  new CmdSetMode(ModeCreatePolyEdge.class,
		 "edgeClass", Association.class,
		 "Association");

  protected static Action _actionGeneralize =
  new CmdSetMode(ModeCreatePolyEdge.class,
		 "edgeClass", Generalization.class,
		 "Generalization");


  ////////////////////////////////////////////////////////////////
  // contructors
  protected static int _UseCaseDiagramSerial = 1;


  public UMLUseCaseDiagram() {
    try { setName("use case diagram " + _UseCaseDiagramSerial++); }
    catch (PropertyVetoException pve) { }
  }

  public UMLUseCaseDiagram(Namespace m) {
    this();
    setNamespace(m);
  }

  public void setNamespace(Namespace m) {
    super.setNamespace(m);
    UseCaseDiagramGraphModel gm = new UseCaseDiagramGraphModel();
    gm.setNamespace(m);
    setGraphModel(gm);
    LayerPerspective lay = new LayerPerspective(m.getName().getBody(), gm);
    setLayer(lay);
    UseCaseDiagramRenderer rend = new UseCaseDiagramRenderer(); // singleton
    lay.setGraphNodeRenderer(rend);
    lay.setGraphEdgeRenderer(rend);
  }


  /** initialize the toolbar for this diagram type */
  protected void initToolBar() {
    //System.out.println("making usecase toolbar");
    _toolBar = new ToolBar();
    _toolBar.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
//     _toolBar.add(Actions.Cut);
//     _toolBar.add(Actions.Copy);
//     _toolBar.add(Actions.Paste);
//     _toolBar.addSeparator();

    _toolBar.add(_actionSelect);
    _toolBar.add(_actionBroom);
    _toolBar.addSeparator();

    _toolBar.add(_actionActor);
    _toolBar.add(_actionUseCase);
    _toolBar.add(_actionAssoc);
    _toolBar.add(_actionGeneralize);
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

} /* end class UMLUseCaseDiagram */
