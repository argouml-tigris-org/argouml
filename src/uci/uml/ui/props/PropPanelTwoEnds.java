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
//import javax.swing.border.*;

import uci.util.*;
import uci.uml.Foundation.Core.*;
import uci.uml.Foundation.Data_Types.*;
import uci.uml.Foundation.Extension_Mechanisms.*;
import uci.uml.Model_Management.*;
import uci.uml.ui.*;


public abstract class PropPanelTwoEnds extends PropPanel {

  ////////////////////////////////////////////////////////////////
  // intance variables


  JLabel _srcLabel = new JLabel("Src: ");
  JTextField _srcField = new JTextField("src");
  JLabel _dstLabel = new JLabel("Dst: ");
  JTextField _dstField = new JTextField("dst");
  SpacerPanel _spacer = new SpacerPanel();

  ////////////////////////////////////////////////////////////////
  // constructors

  public PropPanelTwoEnds(String panelNane) {
    super(panelNane);
    _srcField.setEditable(false);
    _dstField.setEditable(false);
    
    GridBagLayout gb = (GridBagLayout) getLayout();
    GridBagConstraints c = new GridBagConstraints();
    c.fill = GridBagConstraints.BOTH;
    c.weightx = 0.0;
    c.weighty = 0.0;
    c.ipadx = 3; c.ipady = 3;
    c.gridwidth = 1;
    c.gridy = 8;

    c.gridx = 0;
    c.weightx = 0.0;
    gb.setConstraints(_srcLabel, c);
    add(_srcLabel);

    c.gridx = 1;
    c.weightx = 1.0;
    gb.setConstraints(_srcField, c);
    add(_srcField);

    c.gridx = 3;
    c.weightx = 0.0;
    gb.setConstraints(_dstLabel, c);
    add(_dstLabel);

    c.gridx = 4;
    c.weightx = 1.0;
    gb.setConstraints(_dstField, c);
    add(_dstField);

    c.weightx = 0.0;
    c.gridx = 2;
    c.gridy = 0;
    gb.setConstraints(_spacer, c);
    add(_spacer);
  }

  protected void setTargetInternal(Object t) {
    super.setTargetInternal(t);
    _srcLabel.setText(getSourceLabel());
    _srcField.setText(getSourceValue());
    _dstLabel.setText(getDestLabel());
    _dstField.setText(getDestValue());
  }

  public abstract String getSourceLabel();
  public abstract String getSourceValue();
  public abstract String getDestLabel();
  public abstract String getDestValue();

} /* end class PropPanelTwoEnds */
