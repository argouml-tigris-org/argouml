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
import java.awt.event.*;
import java.util.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;

import org.tigris.gef.base.*;
import org.tigris.gef.graph.presentation.*;
import org.tigris.gef.event.*;
import org.tigris.gef.ui.*;

import org.apache.log4j.Category;
import org.argouml.ui.*;
import org.argouml.uml.ui.*;

public class TabDiagram extends TabSpawnable
implements TabModelTarget, GraphSelectionListener, ModeChangeListener {
    protected static Category cat = 
        Category.getInstance(TabDiagram.class);
  ////////////////////////////////////////////////////////////////
  // instance variables
  protected UMLDiagram _target; // the diagram object
  protected JGraph _jgraph;  // the mapen that displays the diagram
  protected ButtonGroup _lineModeBG;
  protected boolean _shouldBeEnabled = true;
  protected ToolBar _toolBar;


  ////////////////////////////////////////////////////////////////
  // constructor

  public TabDiagram() {
    super("Diagram");
    setLayout(new BorderLayout());
    _jgraph = new JGraph();
    _jgraph.setDrawingSize((612-30) * 2, (792-55-20)*2);
    // TODO: should update to size of diagram contents

    Globals.setStatusBar(ProjectBrowser.TheInstance);
    //_toolBar = d.getToolBar();
    //_jgraph.setToolBar(_toolBar); //I wish this had worked...
    //add(_toolBar, BorderLayout.NORTH);
    JPanel p = new JPanel(); 
    p.setLayout(new BorderLayout());
    p.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
    p.add(_jgraph, BorderLayout.CENTER);
    add(p, BorderLayout.CENTER);
    _jgraph.addGraphSelectionListener(this);
    _jgraph.addModeChangeListener(this);
  }

    public Object clone() {
	TabDiagram newPanel = new TabDiagram();
	return newPanel;
    }
    

  ////////////////////////////////////////////////////////////////
  // accessors
  public void setTarget(Object t) {
  	setTarget(t, true);
  }
  
  public void setTarget(Object t, boolean visible) {
    if (t == null) _shouldBeEnabled = false;
    if (t instanceof UMLDiagram) {
      _target = (UMLDiagram) t;
      _shouldBeEnabled = true;
    }
    else {
      _shouldBeEnabled = false;
      return;
    }
    // TODO
    // 2002-07-26
    // Jaap Branderhorst
    // Cool now we assume that's an UMLDiagram
    UMLDiagram d = (UMLDiagram) _target;
    _jgraph.setDiagram(d);
    if (_toolBar != null) {
      setVisible(false);
      remove(_toolBar);
    }
    _toolBar = d.getToolBar();
    cat.debug("setting toolbar in NORTH panel");
    add(_toolBar, BorderLayout.NORTH);
    setVisible(visible);
    //layout();
    //invalidate();
    validate();
  }
  public Object getTarget() { return _target; }

  public void refresh() { setTarget(_target); }

  public boolean shouldBeEnabled() { return _shouldBeEnabled; }

  public JGraph getJGraph() { return _jgraph; }

  public void setVisible(boolean b) {
    super.setVisible(b);
    getJGraph().setVisible(b);
  }

  ////////////////////////////////////////////////////////////////
  // events

  public void selectionChanged(GraphSelectionEvent gse) {
    Vector sels = gse.getSelections();
    ProjectBrowser pb = ProjectBrowser.TheInstance;

    if (sels.size() == 1) pb.setTarget(sels.elementAt(0));
    else pb.setTarget(null);
  }

  public void removeGraphSelectionListener(GraphSelectionListener listener) {
    _jgraph.removeGraphSelectionListener(listener);
  }

  public void modeChange(ModeChangeEvent mce) {
    cat.debug("TabDiagram got mode change event");
    if (!Globals.getSticky() && Globals.mode() instanceof ModeSelect)
      _toolBar.unpressAllButtons();
  }

  public void removeModeChangeListener(ModeChangeListener listener) {
    _jgraph.removeModeChangeListener(listener);
  }

}
