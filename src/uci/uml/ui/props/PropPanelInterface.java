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




package uci.uml.ui.props;

//import jargo.kernel.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.beans.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import javax.swing.text.*;
import javax.swing.border.*;

import uci.util.*;
import uci.uml.Foundation.Core.*;
import uci.uml.Foundation.Data_Types.*;
import uci.uml.Model_Management.*;
import uci.uml.ui.*;

public class PropPanelInterface extends PropPanel
implements DocumentListener, ItemListener {

  ////////////////////////////////////////////////////////////////
  // constants
  public static final VisibilityKind VISIBILITIES[] = {
    VisibilityKind.PUBLIC, VisibilityKind.PACKAGE };

  
  ////////////////////////////////////////////////////////////////
  // instance vars
  JLabel _visLabel = new JLabel("Visibility: ");
  JComboBox _visField = new JComboBox(VISIBILITIES);
  JLabel _extendsLabel = new JLabel("Extends: ");
  JComboBox _extendsField = new JComboBox();

  ////////////////////////////////////////////////////////////////
  // contructors
  public PropPanelInterface() {
    super("Interface Properties");
    GridBagLayout gb = (GridBagLayout) getLayout();
    GridBagConstraints c = new GridBagConstraints();
    c.fill = GridBagConstraints.BOTH;
    c.weightx = 0.0;
    c.ipadx = 0; c.ipady = 0;


    _extendsField.setEditable(true);

    c.gridx = 0;
    c.gridwidth = 1;
    c.gridy = 1;
    gb.setConstraints(_visLabel, c);
    add(_visLabel);
    c.gridy = 2;
    gb.setConstraints(_extendsLabel, c);
    add(_extendsLabel);


    
    c.weightx = 1.0;
    c.gridx = 1;
    //c.gridwidth = GridBagConstraints.REMAINDER;
    c.gridy = 1;
    gb.setConstraints(_visField, c);
    add(_visField);
    c.gridy = 2;
    gb.setConstraints(_extendsField, c);
    add(_extendsField);

    Component ed = _extendsField.getEditor().getEditorComponent();
    Document extendsDoc = ((JTextField)ed).getDocument();
    extendsDoc.addDocumentListener(this);
    _visField.addItemListener(this);
    _extendsField.addItemListener(this);
    
  }

  ////////////////////////////////////////////////////////////////
  // accessors

  protected void setTargetInternal(Object t) {
    super.setTargetInternal(t);
    Interface inf = (Interface) t;

    VisibilityKind vk = inf.getElementOwnership().getVisibility();
    _visField.setSelectedItem(vk);

    updateExtendsChoices();
  }




  public void setTargetExtends() {
    if (_target == null) return;
    if (_inChange) return;
    Object base = _extendsField.getSelectedItem();
    System.out.println("base = " + base);
    // needs-more-work: this could involve changes to the graph model
  }

  public void setTargetVisibility() {
    if (_target == null) return;
    if (_inChange) return;
    VisibilityKind vk = (VisibilityKind) _visField.getSelectedItem();
    Interface inf = (Interface) _target;
    inf.getElementOwnership().setVisibility(vk);
  }


  ////////////////////////////////////////////////////////////////
  // utility functions

  public void updateExtendsChoices() {
    // needs-more-work: build a list of existing (non-final) classes
  }

  
  ////////////////////////////////////////////////////////////////
  // event handling

  public void insertUpdate(DocumentEvent e) {
    //System.out.println(getClass().getName() + " insert");
    Component ed = _extendsField.getEditor().getEditorComponent();
    Document extendsDoc = ((JTextField)ed).getDocument();
    if (e.getDocument() == extendsDoc) setTargetExtends();
    super.insertUpdate(e);
  }

  public void removeUpdate(DocumentEvent e) { insertUpdate(e); }

  public void changedUpdate(DocumentEvent e) {
    System.out.println(getClass().getName() + " changed");
    // Apparently, this method is never called.
  }


  public void itemStateChanged(ItemEvent e) {
    Object src = e.getSource();
    if (src == _visField) {
      System.out.println("class VisibilityKind now is " +
			 _visField.getSelectedItem());
      setTargetVisibility();
    }
    else if (src == _extendsField) {
      System.out.println("class extends now is " +
			 _extendsField.getSelectedItem());
      setTargetExtends();
    }
    
  }

  
} /* end class PropPanelInterface */
