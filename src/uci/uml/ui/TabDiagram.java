// Copyright (c) 1996-98 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation for educational, research and non-profit
// purposes, without fee, and without a written agreement is hereby granted,
// provided that the above copyright notice and this paragraph appear in all
// copies. Permission to incorporate this software into commercial products may
// be obtained by contacting the University of California. David F. Redmiles
// Department of Information and Computer Science (ICS) University of
// California Irvine, California 92697-3425 Phone: 714-824-3823. This software
// program and documentation are copyrighted by The Regents of the University
// of California. The software program and documentation are supplied "as is",
// without any accompanying services from The Regents. The Regents do not
// warrant that the operation of the program will be uninterrupted or
// error-free. The end-user understands that the program was developed for
// research purposes and is advised not to rely exclusively on the program for
// any reason. IN NO EVENT SHALL THE UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY
// PARTY FOR DIRECT, INDIRECT, SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES,
// INCLUDING LOST PROFITS, ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS
// DOCUMENTATION, EVEN IF THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE
// POSSIBILITY OF SUCH DAMAGE. THE UNIVERSITY OF CALIFORNIA SPECIFICALLY
// DISCLAIMS ANY WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
// WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE
// SOFTWARE PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF
// CALIFORNIA HAS NO OBLIGATIONS TO PROVIDE MAINTENANCE, SUPPORT, UPDATES,
// ENHANCEMENTS, OR MODIFICATIONS.


package uci.uml.ui;

//import jargo.kernel.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

import com.sun.java.swing.*;
import com.sun.java.swing.border.*;
import com.sun.java.swing.event.*;

import uci.ui.*;
import uci.util.*;
import uci.gef.*;
import uci.gef.event.*;
import uci.uml.visual.*;


public class TabDiagram extends TabSpawnable
implements TabModelTarget, uci.gef.event.GraphSelectionListener {
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
    _jgraph.setDrawingSize(1000, 1000);
    // needs-more-work: should update to size of diagram contents
    
    uci.gef.Globals.setStatusBar(ProjectBrowser.TheInstance);
    //_toolBar = d.getToolBar();
    //_jgraph.setToolBar(_toolBar); //I wish this had worked...
    //add(_toolBar, BorderLayout.NORTH);
    JPanel p = new JPanel();
    p.setLayout(new BorderLayout());
    p.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
    p.add(_jgraph, BorderLayout.CENTER);
    add(p, BorderLayout.CENTER);
    _jgraph.addGraphSelectionListener(this);
  }

    

  public Object clone() {
    try {
      TabDiagram newPanel = new TabDiagram();
      return newPanel;
    }
    catch (Exception ex) {
      System.out.println("exception in TabDiagram clone()");
    }
    return null;
  }
  

  ////////////////////////////////////////////////////////////////
  // accessors
  public void setTarget(Object t) {
    if (t instanceof UMLDiagram) {
      _target = (UMLDiagram) t;
      _shouldBeEnabled = true;
    }
    else {
      _shouldBeEnabled = false;
      return;
    }
    // needs-more-work
    UMLDiagram d = (UMLDiagram) _target;
    _jgraph.setDiagram(d);
    if (_toolBar != null) {
      setVisible(false);
      remove(_toolBar);
    }
    _toolBar = d.getToolBar();
    //System.out.println("setting toolbar in NORTH panel");
    add(_toolBar, BorderLayout.NORTH);
    setVisible(true);
    //layout();
    //invalidate();
    validate();
  }
  public Object getTarget() { return _target; }

  public boolean shouldBeEnabled() { return _shouldBeEnabled; }

  public JGraph getJGraph() { return _jgraph; }

  public void setVisible(boolean b) {
    super.setVisible(b);
    getJGraph().setVisible(b);
  }
  
  ////////////////////////////////////////////////////////////////
  // events

  public void selectionChanged(GraphSelectionEvent gse) {
    //System.out.println("TabDiagram got editor selection event");
    Vector sels = gse.getSelections();
    ProjectBrowser pb = ProjectBrowser.TheInstance;
    if (sels.size() == 1) pb.setDetalsTarget(sels.elementAt(0));
    else pb.setDetalsTarget(null);
  }
  
  public void removeGraphSelectionListener(GraphSelectionListener listener) {
    _jgraph.removeGraphSelectionListener(listener);
  }


  ////////////////////////////////////////////////////////////////
  // utility methods

  protected static ImageIcon loadIconResource(String imgName, String desc) {
    ImageIcon res = null;
    try {
      java.net.URL imgURL = TabDiagram.class.getResource(imgName);
      return new ImageIcon(imgURL, desc);
    }
    catch (Exception ex) { return new ImageIcon(desc); }
  }


  
  protected static String imageName(String name) {
    return "/uci/Images/" + stripJunk(name) + ".gif";
  }

  protected static String stripJunk(String s) {
    String res = "";
    int len = s.length();
    for (int i = 0; i < len; i++) {
      char c = s.charAt(i);
      if (Character.isJavaLetterOrDigit(c)) res += c;
    }
    return res;
  }

}
