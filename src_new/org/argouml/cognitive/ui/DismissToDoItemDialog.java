// $Id$
// Copyright (c) 1996-2004 The Regents of the University of California. All
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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.apache.log4j.Logger;
import org.argouml.cognitive.Designer;
import org.argouml.cognitive.ToDoItem;
import org.argouml.cognitive.ToDoList;
import org.argouml.cognitive.UnresolvableException;
import org.argouml.i18n.Translator;
import org.argouml.kernel.ProjectManager;
import org.argouml.ui.ArgoDialog;
import org.argouml.ui.ProjectBrowser;

public class DismissToDoItemDialog extends ArgoDialog {

    private static final Logger cat =
        Logger.getLogger(DismissToDoItemDialog.class);

    ////////////////////////////////////////////////////////////////
    // instance variables

    private JRadioButton    _badGoalButton;
    private JRadioButton    _badDecButton;
    private JRadioButton    _explainButton;
    private ButtonGroup     _actionGroup;
    private JTextArea       _explanation;
    private ToDoItem        _target;

    ////////////////////////////////////////////////////////////////
    // constructors

    public DismissToDoItemDialog() {
        super(
            ProjectBrowser.getInstance(),
            Translator.localize("dialog.title.dismiss-todo-item"),
            ArgoDialog.OK_CANCEL_OPTION,
            true);

        JLabel instrLabel =
            new JLabel(Translator.localize("label.remove-item"));
            
        _badGoalButton = new JRadioButton(Translator.localize(
            "button.not-relevant-to-my-goals"));            
        _badDecButton = new JRadioButton(Translator.localize(
            "button.not-of-concern-at-moment"));
        _explainButton = new JRadioButton(Translator.localize(
            "button.reason-given-below"));
        
        _badGoalButton.setMnemonic(
            Translator.localize(
                "button.not-relevant-to-my-goals.mnemonic")
	        .charAt(0));
        _badDecButton.setMnemonic(
            Translator.localize(
                "button.not-of-concern-at-moment.mnemonic")
	        .charAt(0));
        _explainButton.setMnemonic(
            Translator.localize("button.reason-given-below.mnemonic").charAt(
                0));
                
        JPanel content = new JPanel();

        GridBagLayout gb = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();

        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.gridwidth = 2;

        content.setLayout(gb);

        _explanation = new JTextArea(6, 40);
        _explanation.setLineWrap(true);
        _explanation.setWrapStyleWord(true);
        JScrollPane explain = new JScrollPane(_explanation);

        c.gridx = 0;
        c.gridy = 0;

        gb.setConstraints(instrLabel, c);
        content.add(instrLabel);

        c.gridy = 1;
        c.insets = new Insets(5, 0, 0, 0);        

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
        
        setContent(content);

        getOkButton().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (_badGoalButton.getModel().isSelected()) {
                    badGoal(e);
                }
                else if (_badDecButton.getModel().isSelected()) {
                    badDec(e);
                }
                else if (_explainButton.getModel().isSelected()) {
                    explain(e);
                }
                else {
                    cat.warn("DissmissToDoItemDialog: Unknown action: " + e);
                }
            }
        });

        _actionGroup = new ButtonGroup();
        _actionGroup.add(_badGoalButton);
        _actionGroup.add(_badDecButton);
        _actionGroup.add(_explainButton);
        _actionGroup.setSelected(_explainButton.getModel(), true);

        _explanation.setText(
            Translator.localize("label.enter-rationale-here"));
            
        _badGoalButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                _explanation.setEnabled(false);
            }
        });                
        _badDecButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                _explanation.setEnabled(false);
            }
        });                
        _explainButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                _explanation.setEnabled(true);
                _explanation.requestFocus();
                _explanation.selectAll();
            }
        });                
    }

    public void setTarget(Object t) {
        _target = (ToDoItem) t;
    }

    /** Prepare for typing in rationale field when window is opened. */
    public void setVisible(boolean b) {
        super.setVisible(b);
        if (b) {
            _explanation.requestFocus();
            _explanation.selectAll();
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
            list.explicitlyResolve(_target, _explanation.getText());
            ProjectManager.getManager().getCurrentProject().setNeedsSave(true);
        }
        catch (UnresolvableException ure) {
            cat.error("Resolve failed (ure): " + ure);
            // TODO: Should be internationalized
            JOptionPane.showMessageDialog(
		    this,
		    ure.getMessage(),
		    Translator.localize("optionpane.dismiss-failed"),
		    JOptionPane.ERROR_MESSAGE);
        }
    }
} /* end class DismissToDoItemDialog */
