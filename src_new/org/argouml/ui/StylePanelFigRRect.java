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

package org.argouml.ui;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.beans.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import javax.swing.text.*;
import javax.swing.border.*;
import javax.swing.plaf.metal.MetalLookAndFeel;

import org.tigris.gef.presentation.*;

public class StylePanelFigRRect extends StylePanelFig {

  ////////////////////////////////////////////////////////////////
  // instance vars
  JLabel _roundingLabel = new JLabel("Rounding: ");
  JTextField _roundingField = new JTextField();

  
  ////////////////////////////////////////////////////////////////
  // contructors
  public StylePanelFigRRect() {
    super();
    GridBagLayout gb = (GridBagLayout) getLayout();    
    GridBagConstraints c = new GridBagConstraints();
    c.fill = GridBagConstraints.BOTH;
    c.ipadx = 0; c.ipady = 0;

    Document roundingDoc = _roundingField.getDocument();
    roundingDoc.addDocumentListener(this);
    
    c.weightx = 0.0;
    c.gridx = 3;
    c.gridy = 1;
    gb.setConstraints(_roundingLabel, c);
    add(_roundingLabel);

    c.weightx = 1.0;
    c.gridx = 4;
    c.gridy = 1;
    gb.setConstraints(_roundingField, c);
    add(_roundingField);
  }

  
  ////////////////////////////////////////////////////////////////
  // accessors

  public void refresh() {
    super.refresh();
    String roundingStr = ((FigRRect)_target).getCornerRadius() + "";
    _roundingField.setText(roundingStr);
  }


  public void setTargetRounding() {
    if (_target == null) return;
    String roundingStr = _roundingField.getText();
    if (roundingStr.length() == 0) return;
    int r = Integer.parseInt(roundingStr);
    _target.startTrans();
    ((FigRRect)_target).setCornerRadius(r);
    _target.endTrans();
  }

  
  ////////////////////////////////////////////////////////////////
  // event handling

  public void insertUpdate(DocumentEvent e) {
    //System.out.println(getClass().getName() + " insert");
    Document roundingDoc = _roundingField.getDocument();
    if (e.getDocument() == roundingDoc) setTargetRounding();
    super.insertUpdate(e);
  }


  
} /* end class StylePanelFigRRect */
