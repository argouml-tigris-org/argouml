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
import com.sun.java.swing.tree.*;

import uci.ui.ToolBar;
import uci.util.*;
import uci.argo.kernel.*;
import uci.uml.ui.*;


/** In a future release, each Critic will also provide a wizard to
 * help fix the problem it identifies.  The "Next>" button will
 * advance through the steps of the wizard, and increase the blue
 * progress bar on the ToDoItem "sticky note" icon in ToDo tree pane.
 *
 * @see uci.argo.kernel.CriticCC */

public class WizStep extends JPanel
implements TabToDoTarget, ActionListener {

  ////////////////////////////////////////////////////////////////
  // instance variables

  JPanel  _mainPanel = new JPanel();
  JButton _backButton = new JButton(" < Back ");
  JButton _nextButton = new JButton(" Next > ");
  JButton _finishButton = new JButton(" Finish ");
  JButton _helpButton = new JButton(" Help ");
  JPanel  _buttonPanel = new JPanel();

  Object _target;

  ////////////////////////////////////////////////////////////////
  // constructor

  public WizStep() {
    _backButton.setMnemonic('B');
    _nextButton.setMnemonic('N');
    _finishButton.setMnemonic('F');
    _helpButton.setMnemonic('H');
    _buttonPanel.setLayout(new GridLayout(1, 5));
    _buttonPanel.add(_backButton);
    _buttonPanel.add(_nextButton);
    _buttonPanel.add(new SpacerPanel());
    _buttonPanel.add(_finishButton);
    _buttonPanel.add(new SpacerPanel());
    _buttonPanel.add(_helpButton);

    _backButton.setMargin(new Insets(0, 0, 0, 0));
    _nextButton.setMargin(new Insets(0, 0, 0, 0));
    _finishButton.setMargin(new Insets(0, 0, 0, 0));
    _helpButton.setMargin(new Insets(0, 0, 0, 0));

    JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    southPanel.add(_buttonPanel);

    setLayout(new BorderLayout());
    add(_mainPanel, BorderLayout.CENTER);
    add(southPanel, BorderLayout.SOUTH);

    _backButton.addActionListener(this);
    _nextButton.addActionListener(this);
    _finishButton.addActionListener(this);
    _helpButton.addActionListener(this);
  }

  ////////////////////////////////////////////////////////////////
  // accessors

  public void setTarget(Object item) {
    _target = item;
    if (_target == null) {
      _backButton.setEnabled(false);
      _nextButton.setEnabled(false);
      _finishButton.setEnabled(false);
      _helpButton.setEnabled(false);
    }
    else if (_target instanceof ToDoItem) {
      ToDoItem tdi = (ToDoItem) _target;
      // needs-more-work: these should be set based on the item
      _backButton.setEnabled(false);
      _nextButton.setEnabled(false);
      _finishButton.setEnabled(false);
      _helpButton.setEnabled(false);
    }
    else {
      //_description.setText("needs-more-work");
      return;
    }
  }

  public Object getTarget() { return _target; }

  public void refresh() { setTarget(_target); }

  ////////////////////////////////////////////////////////////////
  // actions

  public void doBack() { }
  public void doNext() { }
  public void doFinsh() { }
  public void doHelp() { }

  ////////////////////////////////////////////////////////////////
  // event handlers

  public void actionPerformed(ActionEvent ae) {
    Object src = ae.getSource();
    if (src == _backButton) doBack();
    else if (src == _nextButton) doNext();
    else if (src == _finishButton) doFinsh();
    else if (src == _helpButton) doHelp();
  }

} /* end class WizStep */
