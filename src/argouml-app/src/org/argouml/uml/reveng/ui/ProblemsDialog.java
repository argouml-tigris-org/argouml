// $Id$
// Copyright (c) 2008 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies. This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason. IN NO EVENT SHALL THE
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

package org.argouml.uml.reveng.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.argouml.i18n.Translator;

/**
 * A window that shows the problems occurred during import.
 */
class ProblemsDialog extends JDialog implements ActionListener {

    private Frame parentFrame;
    private JButton abortButton;
    private JButton continueButton;
    private JLabel northLabel;
    private boolean aborted = false;

    /**
     * The constructor.
     */
    ProblemsDialog(Frame frame, String errors) {
        super(frame);
        setResizable(true);
        setModal(true);
        setTitle(Translator.localize("dialog.title.import-problems"));

        Dimension scrSize = Toolkit.getDefaultToolkit().getScreenSize();
        getContentPane().setLayout(new BorderLayout(0, 0));

        // the introducing label
        northLabel =
            new JLabel(Translator.localize("label.import-problems"));
        getContentPane().add(northLabel, BorderLayout.NORTH);

        // the text box containing the problem messages
        JEditorPane textArea = new JEditorPane();
        textArea.setText(errors);
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(new JScrollPane(textArea));
        centerPanel.setPreferredSize(new Dimension(600, 200));
        getContentPane().add(centerPanel);

        // continue and abort buttons
        continueButton = new JButton(Translator.localize("button.continue"));
        abortButton = new JButton(Translator.localize("button.abort"));
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(continueButton);
        bottomPanel.add(abortButton);
        getContentPane().add(bottomPanel, BorderLayout.SOUTH);
        continueButton.requestFocusInWindow();

        // listeners
        continueButton.addActionListener(this);
        abortButton.addActionListener(this);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent evt) {
                disposeDialog();
            }
        });

        pack();
        Dimension contentPaneSize = getContentPane().getSize();
        setLocation(scrSize.width / 2 - contentPaneSize.width / 2,
                scrSize.height / 2 - contentPaneSize.height / 2);
    }

    /*
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(abortButton)) {
            aborted = true;
        }
        disposeDialog();
    }

    /**
     * Returns whether the Abort button was pressed.
     * @return aborted
     */
    public boolean isAborted() {
        return aborted;
    }

    private void disposeDialog() {
        setVisible(false);
        dispose();
    }

    /**
     * The UID.
     */
    private static final long serialVersionUID = -9221358976863603143L;
}
