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

// File: FigText.java
// Classes: FigText
// Original Author: ics125 spring 1996
// $Id$

package uci.gef;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.beans.*;
import com.sun.java.swing.*;
import com.sun.java.swing.event.*;

import uci.ui.*;
import uci.util.*;

// needs-more-work: could this be a singleton?

public class FigTextEditor extends JTextArea
implements PropertyChangeListener, DocumentListener, KeyListener {

  FigText _target;
  JPanel drawingPanel;
  JPanel _glass;
  boolean _editing = false;
  
  public static int EXTRA = 2;
  
  public FigTextEditor(FigText ft) {
    _target = ft;
    System.out.println("making FigTextEditor");
    Editor ce = Globals.curEditor();
    if (!(ce.getAwtComponent() instanceof JComponent)) {
      System.out.println("not a JComponent");
      return;
    }
    drawingPanel = (JPanel) ce.getAwtComponent();
    _target.firePropChange("editing", false, true);
    _target.addPropertyChangeListener(this);
    // walk up and add to glass pane
    Component awtComp = drawingPanel;
    while (!(awtComp instanceof JFrame) && awtComp != null) {
      awtComp = awtComp.getParent();
    }
    if (!(awtComp instanceof JFrame)) { System.out.println("no JFrame"); return; }
    _glass = (JPanel) ((JFrame)awtComp).getGlassPane();
    ft.calcBounds();
    Rectangle bbox = ft.getBounds();
    bbox = SwingUtilities.convertRectangle(drawingPanel, bbox, _glass);
    setBounds(bbox.x - EXTRA, bbox.y - EXTRA,
	      bbox.width + EXTRA*2, bbox.height + EXTRA*2 );
    _glass.setVisible(true);
    _glass.setLayout(null);
    _glass.add(this);
    String text = ft.getText();
    if (!text.endsWith("\n")) setText(text + "\n");
    setText(text);
    setFont(ft.getFont());
    //addFocusListener(this);
    addKeyListener(this);
    requestFocus();
    getDocument().addDocumentListener(this);
    ce.setActiveTextEditor(this);
    _editing = true;
  }

  public void propertyChange(PropertyChangeEvent pve) { updateFigText(); }

  //public void focusLost(FocusEvent fe) {
  //  System.out.println("FigTextEditor lostFocus");
  //  if (_editing) endEditing();
  //}

  //public void focusGained(FocusEvent e) {
  //  System.out.println("focusGained");
 // }


  public void endEditing() {
    _editing = false;
    _target.startTrans();
    updateFigText();
    _target.endTrans();
    //hide();
    Container parent = getParent();
    if (parent != null) parent.remove(this);
    _target.removePropertyChangeListener(this);
    _target.firePropChange("editing", true, false);
    drawingPanel.requestFocus();
    //removeFocusListener(this);
    removeKeyListener(this);
    _glass.setVisible(false);
  }
  
  ////////////////////////////////////////////////////////////////
  // event handlers for KeyListener implementaion

  
  public void keyTyped(KeyEvent ke) {
    if (ke.getKeyChar() == KeyEvent.VK_ENTER &&
	 !_target.getMultiLine()) {
      ke.consume();
    }
    //else super.keyTyped(ke);
  }

  public void keyReleased(KeyEvent ke) {
  }

  public void keyPressed(KeyEvent ke) {
    if (ke.getKeyCode() == KeyEvent.VK_ENTER &&
	 !_target.getMultiLine()) {
      ke.consume();
    }
    else if (ke.getKeyCode() == KeyEvent.VK_F2) {
      endEditing();
      ke.consume();
    }
    else if (ke.getKeyCode() == KeyEvent.VK_ESCAPE) {
      endEditing();
      ke.consume();
    }
    //else super.keyPressed(ke);
  }


  ////////////////////////////////////////////////////////////////
  // event handlers for DocumentListener implementaion

  public void insertUpdate(DocumentEvent e) { updateFigText(); }
  
  public void removeUpdate(DocumentEvent e) { updateFigText(); }
  
  public void changedUpdate(DocumentEvent e) { updateFigText(); }


  ////////////////////////////////////////////////////////////////
  // internal utility methods

  protected void updateFigText() {
    if (_target == null) return;
    String text = getText();
    //_target.startTrans();
    _target.setText(text);
    //_target.endTrans();
    _target.calcBounds();
    Rectangle bbox = _target.getBounds();
    bbox = SwingUtilities.convertRectangle(drawingPanel, bbox, _glass);
    setBounds(bbox.x - EXTRA, bbox.y - EXTRA,
	      bbox.width + EXTRA*2, bbox.height + EXTRA*2 );
    setFont(_target.getFont());
  }
  
} /* end class FigTextEditor */
