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



// File: FigTextEditor.java
// Classes: FigTextEditor
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.gef;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.beans.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;
import javax.swing.border.*;

import uci.ui.*;
import uci.util.*;

// needs-more-work: could this be a singleton?

public class FigTextEditor extends JTextPane
implements PropertyChangeListener, DocumentListener, KeyListener {

  FigText _target;
  JPanel drawingPanel;
  JPanel _glass;
  boolean _editing = false;

  public static int EXTRA = 2;


  /** Needs-more-work: does not open if I use tab to select the
   *  FigText. */
  public FigTextEditor(FigText ft, InputEvent ie) {
    _target = ft;
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
//     setDocument(new DefaultStyledDocument());
    setText(text);
    //addFocusListener(this);
    addKeyListener(this);
    requestFocus();
    getDocument().addDocumentListener(this);
    ce.setActiveTextEditor(this);
    _editing = true;
    setSelectionStart(0);
    setSelectionEnd(getDocument().getLength());
    MutableAttributeSet attr = new SimpleAttributeSet();
    if (ft.getJustification() == FigText.JUSTIFY_CENTER)
      StyleConstants.setAlignment(attr, StyleConstants.ALIGN_CENTER);
    if (ft.getJustification() == FigText.JUSTIFY_RIGHT)
      StyleConstants.setAlignment(attr, StyleConstants.ALIGN_RIGHT);
    Font font = ft.getFont();
    StyleConstants.setFontFamily(attr, font.getFamily());
    StyleConstants.setFontSize(attr, font.getSize());
    setParagraphAttributes(attr, true);
    if (ie instanceof KeyEvent) {
      setSelectionStart(getDocument().getLength());
      setSelectionEnd(getDocument().getLength());
    }
    setBorder(LineBorder.createGrayLineBorder());
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
    // Avoid recursion resulting from call to Editor.setActiveTextEditor
    // at the end of this method.
    if (_editing == false) return;

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
    Editor ce = Globals.curEditor();
    ce.setActiveTextEditor(null);
  }

  ////////////////////////////////////////////////////////////////
  // event handlers for KeyListener implementaion


  public void keyTyped(KeyEvent ke) {
    if (ke.getKeyChar() == KeyEvent.VK_ENTER &&
	 !_target.getMultiLine()) {
      ke.consume();
    }
    if (ke.getKeyCode() == KeyEvent.VK_TAB) {
      if (!_target.getAllowsTab()) {
	endEditing();
	ke.consume();
      }
    }
    //else super.keyTyped(ke);
  }

  public void keyReleased(KeyEvent ke) { }

  public void keyPressed(KeyEvent ke) {
    if (ke.getKeyCode() == KeyEvent.VK_ENTER) {
      if (!_target.getMultiLine()) {
	endEditing();
	ke.consume();
      }
    }
    if (ke.getKeyCode() == KeyEvent.VK_TAB) {
      if (!_target.getAllowsTab()) {
	endEditing();
	ke.consume();
      }
    }
    else if (ke.getKeyCode() == KeyEvent.VK_F2) {
      endEditing();
      ke.consume();
    }
    else if (ke.getKeyCode() == KeyEvent.VK_ESCAPE) {
      // needs-more-work: should revert to orig text, or simply don't commit
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

  static final long serialVersionUID = 5253658760578153001L;

} /* end class FigTextEditor */
