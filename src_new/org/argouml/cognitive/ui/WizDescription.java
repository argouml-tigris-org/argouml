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

package org.argouml.cognitive.ui;

import java.awt.*;
import javax.swing.*;
import java.text.MessageFormat;

import ru.novosoft.uml.foundation.core.MModelElement;

import org.argouml.application.api.Argo;
import org.argouml.cognitive.*;
import org.argouml.cognitive.critics.Critic;
import org.argouml.ui.targetmanager.TargetManager;

public class WizDescription extends WizStep {

  ////////////////////////////////////////////////////////////////
  // instance variables

  JTextArea _description = new JTextArea();


  public WizDescription() {
    super();
    Argo.log.info("making WizDescription");

    _description.setLineWrap(true);
    _description.setWrapStyleWord(true);

    _mainPanel.setLayout(new BorderLayout());
    _mainPanel.add(new JScrollPane(_description), BorderLayout.CENTER);
  }

  public void setTarget(Object item) {
    String message = "";
    super.setTarget(item);
    Object target = item;
    if (target == null) {
      _description.setText(Argo.localize("Cognitive", "message.no-item-selected"));
    }
    else if (target instanceof ToDoItem) {
      ToDoItem tdi = (ToDoItem) target;
      _description.setEnabled(true);
      _description.setText(tdi.getDescription());
      _description.setCaretPosition(0);
    }
    else if (target instanceof PriorityNode) {
      message = MessageFormat. 
                format(Argo.localize("Cognitive", "message.branch-priority"),
                       new Object [] { target.toString() });
      _description.setText(message);

      return;
    }
    else if (target instanceof Critic) {
      message = MessageFormat. 
                format(Argo.localize("Cognitive", "message.branch-critic"),
                       new Object [] { target.toString() });
      _description.setText(message);

      return;
    }
    else if (target instanceof MModelElement) {
      message = MessageFormat. 
                format(Argo.localize("Cognitive", "message.branch-model"),
                       new Object [] { target.toString() });
      _description.setText(message);

      return;
    }
    else if (target instanceof Decision) {
      message = MessageFormat. 
                format(Argo.localize("Cognitive", "message.branch-decision"),
                       new Object [] { target.toString() });
      _description.setText(message);

      return;
    }
    else if (target instanceof Goal) {
      message = MessageFormat. 
                format(Argo.localize("Cognitive", "message.branch-goal"),
                       new Object [] { target.toString() });
      _description.setText(message);

      return;
    }
    else if (target instanceof KnowledgeTypeNode) {
      message = MessageFormat. 
                format(Argo.localize("Cognitive", "message.branch-knowledge"),
                       new Object [] { target.toString() });
      _description.setText(message);

      return;
    }
    else {
      _description.setText("");
      return;
    }
  }



} /* end class WizDescription */
