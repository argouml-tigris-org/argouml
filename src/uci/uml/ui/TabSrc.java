// Copyright (c) 1996-98 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation for educational, research and non-profit
// purposes, without fee, and without a written agreement is hereby granted,
// provided that the above copyright notice and this paragraph appear in all
// copies. Permission to incorporate this software into commercial products
// must be negotiated with University of California. This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "as is",
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
import com.sun.java.swing.event.*;
import com.sun.java.swing.tree.*;
import com.sun.java.swing.text.*;

import uci.util.*;
import uci.gef.*;
import uci.uml.generate.*;
import uci.uml.Foundation.Core.ModelElement;


public class TabSrc extends TabText {
  ////////////////////////////////////////////////////////////////
  // constructor
  public TabSrc() {
    setTitle("Source");
    System.out.println("making TabSrc");
  }

  ////////////////////////////////////////////////////////////////
  // accessors

  protected String genText() {
    //System.out.println("TabSrc getting src for " + _target);
    Object modelObject = _target;
    if (_target instanceof FigNode)
      modelObject = ((FigNode)_target).getOwner();
    if (_target instanceof FigEdge)
      modelObject = ((FigEdge)_target).getOwner();
    if (modelObject == null) return null;
    //System.out.println("TabSrc getting src for " + modelObject);
    return GeneratorDisplay.Generate(modelObject);
  }

  protected void parseText(String s) {
    //System.out.println("TabSrc   setting src for "+ _target);
    Object modelObject = _target;
    if (_target instanceof FigNode)
      modelObject = ((FigNode)_target).getOwner();
    if (_target instanceof FigEdge)
      modelObject = ((FigEdge)_target).getOwner();
    if (modelObject == null) return;
    //System.out.println("TabSrc   setting src for " + modelObject);
    //Parser.ParseAndUpdate(modelObject, s);
  }

  public void setTarget(Object t) {
    super.setTarget(t);

    _shouldBeEnabled = false;
    if (t instanceof ModelElement) _shouldBeEnabled = true;
    if (t instanceof Fig) {
      if (((Fig)t).getOwner() instanceof ModelElement)
	_shouldBeEnabled = true;
    }
  }

  public void refresh() { setTarget(_target); }

} /* end class TabSrc */
