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

// File: UMLClassDiagram.java
// Classes: UMLClassDiagram
// Original Author: jrobbins@ics.uci.edy
// $Id$

package org.argouml.uml.diagram.static_structure.ui;

import java.util.*;
import java.awt.*;
import java.beans.*;
import javax.swing.*;

import ru.novosoft.uml.model_management.*;
import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.behavior.common_behavior.*;

import org.tigris.gef.base.CmdSetMode;
import org.tigris.gef.base.LayerPerspective;
import org.tigris.gef.base.LayerPerspectiveMutable;
import org.tigris.gef.base.ModeCreatePolyEdge;
import org.tigris.gef.ui.*;

import org.argouml.ui.*;
import org.argouml.uml.ui.*;
import org.argouml.uml.diagram.ui.*;
import org.argouml.uml.diagram.static_structure.*;

public class UMLClassDiagram extends UMLDiagram {

  ////////////////
  // actions for toolbar
  // needs-more-work: should these be static?



	protected static Action _actionClass =
		new CmdCreateNode(MClass.class, "Class");

	protected static Action _actionObject =
		new CmdCreateNode(MInstance.class, "Instance");

	protected static Action _actionInterface =
		new CmdCreateNode(MInterface.class, "Interface");

	protected static Action _actionDepend =
		new CmdSetMode(ModeCreatePolyEdge.class,
					   "edgeClass", MDependency.class,
					   "Dependency");
    
    /* in work: uncomment to test - mkl
    protected static Action _actionPermission =
        new CmdSetMode(ModeCreatePolyEdge.class,
                       "edgeClass", MPermission.class,
                       "Permission");
    */

	protected static Action _actionAssoc =
		new CmdSetMode(ModeCreatePolyEdge.class,
					   "edgeClass", MAssociation.class,
					   "Association");

	protected static Action _actionLink =
		new CmdSetMode(ModeCreatePolyEdge.class,
					   "edgeClass", MLink.class,
					   "Link");

	protected static Action _actionGeneralize =
		new CmdSetMode(ModeCreatePolyEdge.class,
					   "edgeClass", MGeneralization.class,
					   "Generalization");

  
	protected static Action _actionRealize =
		new CmdSetMode(ModeCreatePolyEdge.class,
					   "edgeClass", MAbstraction.class,
					   "Realization");
   
	protected static Action _actionPackage =
		new CmdCreateNode(MPackage.class, "Package");


  ////////////////////////////////////////////////////////////////
  // contructors
  protected static int _ClassDiagramSerial = 1;

  protected static String getNewDiagramName() {
  	String name = null;
  	Object[] args = {name};
  	do {
        name = "class diagram " + _ClassDiagramSerial;
        _ClassDiagramSerial++;
        args[0] = name;
    }
    while (TheInstance.vetoCheck("name", args));
    return name;
  }

  public UMLClassDiagram() {
  	super();
    try { setName(getNewDiagramName()); }
    catch (PropertyVetoException pve) { }
  }

  public UMLClassDiagram(MNamespace m) {
    super(getNewDiagramName(), m);
  }

  public void setNamespace(MNamespace m) {
    super.setNamespace(m);
    ClassDiagramGraphModel gm = new ClassDiagramGraphModel();
    gm.setNamespace(m);
    setGraphModel(gm);
    LayerPerspective lay = new LayerPerspectiveMutable(m.getName(), gm);
    setLayer(lay);
    ClassDiagramRenderer rend = new ClassDiagramRenderer(); // singleton
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

    _toolBar.add(_actionPackage);
    _toolBar.add(_actionClass);
    _toolBar.add(_actionAssoc);
    _toolBar.add(_actionDepend);
    _toolBar.add(_actionPermission);
    _toolBar.add(_actionGeneralize);
    _toolBar.addSeparator();

//     _toolBar.add(_actionObject);
//     _toolBar.add(_actionLink);
//     _toolBar.addSeparator();

    _toolBar.add(_actionInterface);
	_toolBar.add(_actionRealize);
    _toolBar.addSeparator();

    _toolBar.add(ActionAddAttribute.SINGLETON);
    _toolBar.add(ActionAddOperation.SINGLETON);
    // needs-more-work: remove attribute and operation?
    _toolBar.addSeparator();

    _toolBar.add(ActionAddNote.SINGLETON);
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

} /* end class UMLClassDiagram */
