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




package uci.uml.ui.todo;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import com.sun.java.swing.*;
import com.sun.java.swing.event.*;
import com.sun.java.swing.border.*;

import uci.util.*;
import uci.argo.kernel.*;


/** A simple non-modal wizard step that shows instructions and prompts
 *  the user to enter a string. 
 *
 * @see uci.argo.kernel.Critic
 * @see uci.argo.kernel.Wizard
 */

public class WizStepTextField extends WizStep {
  JTextArea _instructions = new JTextArea();
  JLabel _label = new JLabel("Value:");
  JTextField _field = new JTextField(20);

  public WizStepTextField() {
    _mainPanel.setBorder(new EtchedBorder());
    _instructions.setEditable(false);
    _instructions.setBorder(null);
    _instructions.setBackground(_mainPanel.getBackground());
    _mainPanel.setLayout(new BorderLayout());
    _mainPanel.add(_instructions, BorderLayout.NORTH);
    JPanel center = new JPanel();
    center.add(_label);
    center.add(_field);
    _mainPanel.add(center, BorderLayout.CENTER);
  }

  public WizStepTextField(Wizard w, String instr, String lab, String val) {
    this();
    // store wizard?
    _instructions.setText(instr);
    _label.setText(lab);
    _field.setText(val);
  }

  public String getText() { return _field.getText(); }
  
} /* end class WizStepTextField */
