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

package org.argouml.uml.diagram.static_structure.ui;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ItemEvent;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.DocumentEvent;

import org.argouml.ui.StylePanelFig;

public class StylePanelFigInterface extends StylePanelFig {

  ////////////////////////////////////////////////////////////////
  // constants

  ////////////////////////////////////////////////////////////////
  // instance vars

  protected JCheckBox _operCheckBox = new JCheckBox("Operations");
  protected JLabel _displayLabel = new JLabel("Display: ");

  ////////////////////////////////////////////////////////////////
  // contructors

  public StylePanelFigInterface() {
    super();
    GridBagLayout gb = (GridBagLayout) getLayout();
    GridBagConstraints c = new GridBagConstraints();
    c.fill = GridBagConstraints.BOTH;
    c.ipadx = 0; c.ipady = 0;

    c.gridx = 0;
    c.gridwidth = 1;
    c.gridy = 0;
    c.weightx = 0.0;
    gb.setConstraints(_displayLabel, c);
    add(_displayLabel);

    c.gridx = 1;
    c.gridwidth = 1;
    c.gridy = 0;
    c.weightx = 0.0;
    JPanel pane = new JPanel();
    pane.setLayout(new FlowLayout(FlowLayout.LEFT));
    pane.add(_operCheckBox);
    gb.setConstraints(pane, c);
    add(pane);

    _operCheckBox.setSelected(false);
    _operCheckBox.addItemListener(this);
  }


  ////////////////////////////////////////////////////////////////
  // accessors

  public void refresh() {
    super.refresh();
    org.argouml.uml.diagram.static_structure.ui.FigInterface ti = (org.argouml.uml.diagram.static_structure.ui.FigInterface)_target;
    _operCheckBox.setSelected(ti.isOperationVisible());
  }

  ////////////////////////////////////////////////////////////////
  // event handling

  public void insertUpdate(DocumentEvent e) {
    super.insertUpdate(e);
  }

  public void removeUpdate(DocumentEvent e) { insertUpdate(e); }



  public void itemStateChanged(ItemEvent e) {
    Object src = e.getSource();

    if (src == _operCheckBox) {
      ((org.argouml.uml.diagram.static_structure.ui.FigInterface)_target).setOperationVisible(_operCheckBox.isSelected());
    }
    else super.itemStateChanged(e);
  }


} /* end class StylePanelFigInterface */

