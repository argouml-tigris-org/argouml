// $Id$
// Copyright (c) 1996-2006 The Regents of the University of California. All
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
import org.argouml.cognitive.Translator;
import org.argouml.cognitive.UnresolvableException;
import org.argouml.ui.ArgoDialog;
import org.tigris.swidgets.Dialog;

/**
 * The dialog to dismiss todo items.
 *
 */
public class DismissToDoItemDialog extends ArgoDialog {

    private static final Logger LOG =
        Logger.getLogger(DismissToDoItemDialog.class);

    ////////////////////////////////////////////////////////////////
    // instance variables

    private JRadioButton    badGoalButton;
    private JRadioButton    badDecButton;
    private JRadioButton    explainButton;
    private ButtonGroup     actionGroup;
    private JTextArea       explanation;
    private ToDoItem        target;

    ////////////////////////////////////////////////////////////////
    // constructors

    /**
     * The constructor.
     */
    public DismissToDoItemDialog() {
        super(
            Translator.localize("dialog.title.dismiss-todo-item"),
            Dialog.OK_CANCEL_OPTION,
            true);

        JLabel instrLabel =
            new JLabel(Translator.localize("label.remove-item"));

        badGoalButton = new JRadioButton(Translator.localize(
            "button.not-relevant-to-my-goals"));
        badDecButton = new JRadioButton(Translator.localize(
            "button.not-of-concern-at-moment"));
        explainButton = new JRadioButton(Translator.localize(
            "button.reason-given-below"));

        badGoalButton.setMnemonic(
            Translator.localize(
                "button.not-relevant-to-my-goals.mnemonic")
	        .charAt(0));
        badDecButton.setMnemonic(
            Translator.localize(
                "button.not-of-concern-at-moment.mnemonic")
	        .charAt(0));
        explainButton.setMnemonic(
            Translator.localize("button.reason-given-below.mnemonic").charAt(
                0));

        JPanel content = new JPanel();

        GridBagLayout gb = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();

        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.gridwidth = 2;

        content.setLayout(gb);

        explanation = new JTextArea(6, 40);
        explanation.setLineWrap(true);
        explanation.setWrapStyleWord(true);
        JScrollPane explain = new JScrollPane(explanation);

        c.gridx = 0;
        c.gridy = 0;

        gb.setConstraints(instrLabel, c);
        content.add(instrLabel);

        c.gridy = 1;
        c.insets = new Insets(5, 0, 0, 0);

        gb.setConstraints(badGoalButton, c);
        content.add(badGoalButton);

        c.gridy = 2;

        gb.setConstraints(badDecButton, c);
        content.add(badDecButton);

        c.gridy = 3;

        gb.setConstraints(explainButton, c);
        content.add(explainButton);

        c.gridy = 4;
        c.weighty = 1.0;

        gb.setConstraints(explain, c);
        content.add(explain);

        setContent(content);

        getOkButton().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (badGoalButton.getModel().isSelected()) {
                    badGoal(e);
                }
                else if (badDecButton.getModel().isSelected()) {
                    badDec(e);
                }
                else if (explainButton.getModel().isSelected()) {
                    explain(e);
                }
                else {
                    LOG.warn("DissmissToDoItemDialog: Unknown action: " + e);
                }
            }
        });

        actionGroup = new ButtonGroup();
        actionGroup.add(badGoalButton);
        actionGroup.add(badDecButton);
        actionGroup.add(explainButton);
        actionGroup.setSelected(explainButton.getModel(), true);

        explanation.setText(
            Translator.localize("label.enter-rationale-here"));

        badGoalButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                explanation.setEnabled(false);
            }
        });
        badDecButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                explanation.setEnabled(false);
            }
        });
        explainButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                explanation.setEnabled(true);
                explanation.requestFocus();
                explanation.selectAll();
            }
        });
    }

    /**
     * @param t the new target object (ToDoItem)
     */
    public void setTarget(Object t) {
        target = (ToDoItem) t;
    }

    /**
     * Prepare for typing in rationale field when window is opened.
     *
     * @see java.awt.Component#setVisible(boolean)
     */
    public void setVisible(boolean b) {
        super.setVisible(b);
        if (b) {
            explanation.requestFocus();
            explanation.selectAll();
        }
    }

    ////////////////////////////////////////////////////////////////
    // event handlers

    private void badGoal(ActionEvent e) {
        //cat.debug("bad goal");
        GoalsDialog d = new GoalsDialog();
        d.setVisible(true);
    }

    private void badDec(ActionEvent e) {
        //cat.debug("bad decision");
        DesignIssuesDialog d = new DesignIssuesDialog();
        d.setVisible(true);
    }

    private void explain(ActionEvent e) {
        //TODO: make a new history item
        ToDoList list = Designer.theDesigner().getToDoList();
        try {
            list.explicitlyResolve(target, explanation.getText());
            Designer.firePropertyChange(
                    Designer.MODEL_TODOITEM_DISMISSED, null, null);
        }
        catch (UnresolvableException ure) {
            LOG.error("Resolve failed (ure): ", ure);
            // TODO: Should be internationalized
            JOptionPane.showMessageDialog(
		    this,
		    ure.getMessage(),
		    Translator.localize("optionpane.dismiss-failed"),
		    JOptionPane.ERROR_MESSAGE);
        }
    }
} /* end class DismissToDoItemDialog */
