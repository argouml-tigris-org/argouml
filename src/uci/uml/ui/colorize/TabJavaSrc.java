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




package uci.uml.ui.colorize;

//import jargo.kernel.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import javax.swing.text.*;
import javax.swing.plaf.metal.*;

import uci.util.*;
import uci.gef.*;
import uci.uml.ui.*;
import uci.uml.generate.*;
import uci.uml.Foundation.Core.ModelElement;


public class TabJavaSrc  extends TabSpawnable
implements TabModelTarget, DocumentListener {

  ////////////////////////////////////////////////////////////////
  // instance variables
  Object _target;
  boolean _parseChanges = true;
  boolean _shouldBeEnabled = false;
  JEditorPane _text = new JEditorPane();
  JavaEditorKit kit = new JavaEditorKit();
  Dimension _size = new Dimension(775, 500);
  
  ////////////////////////////////////////////////////////////////
  // constructor
  public TabJavaSrc() {
    super("Source");

    _text.setEditorKitForContentType("text/java", kit);
    _text.setContentType("text/java");
    _text.setBackground(Color.white);
    _text.setFont(MetalLookAndFeel.getUserTextFont());
    //_text.setFont(new Font("Dialog", 0, 12));
    _text.setEditable(true);

    JavaContext styles = kit.getStylePreferences();
    Style s;
    s = styles.getStyleForScanValue(Token.COMMENT.getScanValue());
    StyleConstants.setForeground(s, new Color(102/2, 153, 153/2));
    s = styles.getStyleForScanValue(Token.STRINGVAL.getScanValue());
    StyleConstants.setForeground(s, new Color(102/2, 102/2, 153));
    Color keyword = new Color(102/2, 102/2, 255);
    for (int code = 70; code <= 130; code++) {
      s = styles.getStyleForScanValue(code);
      if (s != null) {
	StyleConstants.setForeground(s, keyword);
      }
    }
//     JScrollPane scroller = new JScrollPane();
//     JViewport vp = scroller.getViewport();
//     vp.add(_text);
//     vp.setBackingStoreEnabled(true);
//     add(vp, BorderLayout.CENTER);
    _text.setMinimumSize(_size);
    _text.setPreferredSize(_size);
    _text.setSize(_size);
    setLayout(new BorderLayout());
    add(new JScrollPane(_text), BorderLayout.CENTER);
    //add(_text, BorderLayout.CENTER);
    _text.setSize(_size);

  }

  ////////////////////////////////////////////////////////////////
  // accessors

  protected String genText() {
    //System.out.println("TabJavaSrc getting src for " + _target);
    Object modelObject = _target;
    if (_target instanceof FigNode)
      modelObject = ((FigNode)_target).getOwner();
    if (_target instanceof FigEdge)
      modelObject = ((FigEdge)_target).getOwner();
    if (modelObject == null) return null;
    //System.out.println("TabJavaSrc getting src for " + modelObject);
    return GeneratorDisplay.Generate(modelObject);
  }

  protected void parseText(String s) {
    //System.out.println("TabJavaSrc   setting src for "+ _target);
    Object modelObject = _target;
    if (_target instanceof FigNode)
      modelObject = ((FigNode)_target).getOwner();
    if (_target instanceof FigEdge)
      modelObject = ((FigEdge)_target).getOwner();
    if (modelObject == null) return;
    //System.out.println("TabJavaSrc   setting src for " + modelObject);
    //Parser.ParseAndUpdate(modelObject, s);
  }

  public void setTarget(Object t) {
    _target = t;
    _parseChanges = false;
    if (_target == null) {
      _text.setEnabled(false);
      _text.setText("Nothing selected");
      _shouldBeEnabled = false;
    }
    else {
      _text.setEnabled(true);
      String generatedText = genText();
      if (generatedText != null) {
	_text.setText(generatedText);
	_shouldBeEnabled = true;
	_text.setCaretPosition(0);
      }
      else {
	_text.setEnabled(false);
	_text.setText("N/A");
	_shouldBeEnabled = false;
      }
    }
    _parseChanges = true;

    _shouldBeEnabled = false;
    if (t instanceof ModelElement) _shouldBeEnabled = true;
    if (t instanceof Fig) {
      if (((Fig)t).getOwner() instanceof ModelElement)
	_shouldBeEnabled = true;
    }
    _text.setSize(_size);
  }

  public Object getTarget() { return _target; }

  public void refresh() { setTarget(_target); }

  public boolean shouldBeEnabled() { return _shouldBeEnabled; }

  
  ////////////////////////////////////////////////////////////////
  // event handlers
  public void insertUpdate(DocumentEvent e) {
    //needs-more-work: should fire its own event and ProjectBrowser
    //should register a listener
    //System.out.println(getClass().getName() + " insert");
    if (_parseChanges) parseText(_text.getText());
  }

  public void removeUpdate(DocumentEvent e) {
    //needs-more-work: should fire its own event and ProjectBrowser
    //should register a listener
    //System.out.println(getClass().getName() +  " remove");
    if (_parseChanges) parseText(_text.getText());
  }

  public void changedUpdate(DocumentEvent e) {
    //needs-more-work: should fire its own event and ProjectBrowser
    //should register a listener
    //System.out.println(getClass().getName() + " changed");
    if (_parseChanges) parseText(_text.getText());
  }

} /* end class TabJavaSrc */
