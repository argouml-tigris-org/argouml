// $Id$
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

import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.apache.log4j.Logger;
import org.argouml.cognitive.Designer;
import org.argouml.cognitive.ToDoItem;
import org.argouml.cognitive.ToDoList;
import org.argouml.cognitive.UnresolvableException;
import org.argouml.ui.ProjectBrowser;

public class DismissToDoItemDialog extends JDialog {
    protected static Logger cat =
	Logger.getLogger(DismissToDoItemDialog.class);

    ////////////////////////////////////////////////////////////////
    // constants
    String BAD_GOAL_LABEL = "It is not relevant to my goals";
    String BAD_DECISION_LABEL = "It is not of concern at the momemt";
    String EXPLAIN_LABEL = "Reason given below";

    ////////////////////////////////////////////////////////////////
    // instance variables
    protected JRadioButton _badGoalButton = new JRadioButton(BAD_GOAL_LABEL);
    protected JRadioButton _badDecButton = new JRadioButton(BAD_DECISION_LABEL);
    protected JRadioButton _explainButton = new JRadioButton(EXPLAIN_LABEL);
    protected ButtonGroup _actionGroup = new ButtonGroup();
    protected JButton _okButton = new JButton("Ok");
    protected JButton _cancelButton = new JButton("Cancel");
    protected JTextArea _explaination = new JTextArea();
    protected ToDoItem _target;

    ////////////////////////////////////////////////////////////////
    // constructors
  
    public DismissToDoItemDialog() {
	super(ProjectBrowser.getInstance(), "Dismiss To Do Item");
	JLabel instrLabel = new JLabel("This item should be removed because");

	setLocation(300, 200);
	setSize(new Dimension(300, 250));
	Container content = getContentPane();
	GridBagLayout gb = new GridBagLayout();
	GridBagConstraints c = new GridBagConstraints();
	c.fill = GridBagConstraints.BOTH;
	c.weightx = 1.0;
	c.ipadx = 3; c.ipady = 3;
	c.gridwidth = 2;

	content.setLayout(gb);

	JScrollPane explain = new JScrollPane(_explaination);
	//set size?

	c.gridx = 0;
	c.gridy = 0;
	gb.setConstraints(instrLabel, c);
	content.add(instrLabel);
	c.gridy = 1;
	gb.setConstraints(_badGoalButton, c);
	content.add(_badGoalButton);
	c.gridy = 2;
	gb.setConstraints(_badDecButton, c);
	content.add(_badDecButton);
	c.gridy = 3;
	gb.setConstraints(_explainButton, c);
	content.add(_explainButton);
	c.gridy = 4;
	c.weighty = 1.0;
	gb.setConstraints(explain, c);
	content.add(explain);
	//c.fill = GridBagConstraints.NONE;
	c.gridwidth = 1;
	c.gridy = 5;
	c.gridx = 0;
	c.weighty = 0.;
	gb.setConstraints(_okButton, c);
	content.add(_okButton);
	c.gridx = 1;
	c.gridwidth = 1;
	gb.setConstraints(_cancelButton, c);
	content.add(_cancelButton);

	_okButton.addActionListener(new ActionListener() 
	    {
		public void actionPerformed(ActionEvent e) {
		    if (_badGoalButton.getModel().isSelected()) {
			badGoal(e);
		    } else if (_badDecButton.getModel().isSelected()) {
			badDec(e);
		    } else if (_explainButton.getModel().isSelected()) {
			explain(e);
		    } else {
			cat.warn("DissmissToDoItemDialog: Unknown action: "
				 + e);
		    }
		    setVisible(false);
		    dispose();
		}
	    });
	_cancelButton.addActionListener(new ActionListener() 
	    {
		public void actionPerformed(ActionEvent e) {
		    setVisible(false);
		    dispose();
		}
	    });

	_actionGroup.add(_badGoalButton);
	_actionGroup.add(_badDecButton);
	_actionGroup.add(_explainButton);
	_actionGroup.setSelected(_explainButton.getModel(), true);

	_explaination.setText("<Enter Rationale Here>");
    }

    public void setTarget(Object t) { _target = (ToDoItem) t; }

    /** Prepare for typing in rationale field when window is opened. */
    public void setVisible(boolean b) {
	super.setVisible(b);
	if (b) {
	    _explaination.requestFocus();
	    _explaination.selectAll();
	}
    }

    ////////////////////////////////////////////////////////////////
    // event handlers

    private void badGoal(ActionEvent e) {
	//cat.debug("bad goal");
	GoalsDialog d = new GoalsDialog(ProjectBrowser.getInstance());
	d.setVisible(true);
    }

    private void badDec(ActionEvent e) {
	//cat.debug("bad decision");
	DesignIssuesDialog d =
	    new DesignIssuesDialog(ProjectBrowser.getInstance());
	d.setVisible(true);
    }

    private void explain(ActionEvent e) {
	//cat.debug("I can explain!");
	//TODO: make a new history item
	ToDoList list = Designer.TheDesigner.getToDoList();
	try {
	    list.explicitlyResolve(_target, _explaination.getText());
	} catch (UnresolvableException ure) {
	    cat.error("Resolve failed (ure): " + ure);
	    // TODO: Should be internationalized
	    JOptionPane.showMessageDialog(this, ure.getMessage(),
					  "Dismiss failed",
					  JOptionPane.ERROR_MESSAGE);
	}
    }
} /* end class DismissToDoItemDialog */

