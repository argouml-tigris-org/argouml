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




package uci.uml.ui.props;

//import jargo.kernel.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.beans.*;
import com.sun.java.swing.*;
import com.sun.java.swing.event.*;
import com.sun.java.swing.tree.*;
import com.sun.java.swing.text.*;
//import com.sun.java.swing.border.*;

import uci.util.*;
import uci.gef.Diagram;
import uci.uml.Foundation.Core.*;
import uci.uml.Foundation.Data_Types.*;
import uci.uml.Foundation.Extension_Mechanisms.*;
import uci.uml.Model_Management.*;
import uci.uml.ui.*;
import uci.uml.visual.*;

public class PropPanelUMLStateDiagram extends PropPanelDiagram {
  ////////////////////////////////////////////////////////////////
  // instance vars
  JLabel _contextLabel = new JLabel("Context: ");
  JComboBox _contextField = new JComboBox();

  ////////////////////////////////////////////////////////////////
  // constructors

  public PropPanelUMLStateDiagram() {
    super();
    GridBagLayout gb = (GridBagLayout) getLayout();
    GridBagConstraints c = new GridBagConstraints();
    c.fill = GridBagConstraints.BOTH;
    c.weightx = 0.0;
    c.ipadx = 3; c.ipady = 3;

    c.gridx = 0;
    c.gridwidth = 1;
    c.gridy = 1;
    gb.setConstraints(_contextLabel, c);
    add(_contextLabel);

    c.weightx = 1.0;
    c.gridx = 1;
    gb.setConstraints(_contextField, c);
    add(_contextField);

    _contextField.setEditable(true);
    _contextField.getEditor().getEditorComponent().setBackground(Color.white);
    Component ed = _contextField.getEditor().getEditorComponent();
    Document contextDoc = ((JTextField)ed).getDocument();
    contextDoc.addDocumentListener(this);
    // needs-more-work: set font?
  }

  ////////////////////////////////////////////////////////////////
  // accessors

  public void setTarget(Object t) {
    super.setTarget(t);
    if (!(_target instanceof UMLStateDiagram)) return;
    UMLStateDiagram d = (UMLStateDiagram) _target;
    StateDiagramGraphModel sdgm = (StateDiagramGraphModel) d.getGraphModel();
    _contextField.setSelectedItem(null);
    if (sdgm != null && sdgm.getMachine() != null)
      _contextField.setSelectedItem(sdgm.getMachine().getContext());
  }

  public Object getTarget() { return _target; }

  public void refresh() { setTarget(_target); }

  public boolean shouldBeEnabled() { return _target instanceof Diagram; }


  protected void setTargetName() {
    if (!(_target instanceof Diagram)) return;
    try {
      ((Diagram)_target).setName(_nameField.getText());
    }
    catch (PropertyVetoException pve) {
      System.out.println("Could not set diagram name");
    }
  }

  ////////////////////////////////////////////////////////////////
  // event handling

  public void insertUpdate(DocumentEvent e) {
    //System.out.println(getClass().getName() + " insert");
    if (e.getDocument() == _nameField.getDocument()) setTargetName();
  }

  public void removeUpdate(DocumentEvent e) { insertUpdate(e); }

  public void changedUpdate(DocumentEvent e) {
    System.out.println(getClass().getName() + " changed");
    // Apparently, this method is never called.
  }
  
} /* end class PropPanelDiagram */
