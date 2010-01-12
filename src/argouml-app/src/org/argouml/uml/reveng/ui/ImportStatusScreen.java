/* $Id$
 *****************************************************************************
 * Copyright (c) 2009 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    tfmorris
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

// Copyright (c) 2008, 2009 The Regents of the University of California. All
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
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import org.argouml.i18n.Translator;
import org.argouml.taskmgmt.ProgressEvent;
import org.argouml.taskmgmt.ProgressMonitor;

/**
 * A window that shows the progress bar and a cancel button. As a convenience to
 * callers which may be executing on a thread other than the Swing event thread,
 * all methods use SwingUtilities.invokeLater() or
 * SwingUtilities.invokeAndWait() to make sure that Swing calls happen on the
 * appropriate thread.
 *<p>
 * TODO: React on the close button as if the Cancel button was pressed.
 */
public class ImportStatusScreen extends JDialog 
    implements ProgressMonitor, WindowListener {
    
    private JButton cancelButton;
    private JLabel progressLabel;
    private JProgressBar progress;
    private JTextArea messageArea;
    private boolean hasMessages = false;
    private boolean canceled = false;

    /**
     * The constructor.
     *
     * @param title
     * @param iconName
     */
    public ImportStatusScreen(Frame frame, String title, String iconName) {
        super(frame, true);
        if (title != null) {
            setTitle(title);
        }
        Dimension scrSize = Toolkit.getDefaultToolkit().getScreenSize();
        getContentPane().setLayout(new BorderLayout(4, 4));
        Container panel = new JPanel(new GridBagLayout());

        // Parsing file x of z.
        progressLabel = new JLabel();
        progressLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.gridheight = 1;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.1;
        
        panel.add(progressLabel, gbc);
        gbc.gridy++;
        
        // progress bar
        progress = new JProgressBar();
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(progress, gbc);
        gbc.gridy++;
        
        panel.add(
                new JLabel(Translator.localize("label.import-messages")), gbc);
        gbc.gridy++;
        
        // Error/warning messageArea
        messageArea = new JTextArea(10, 50);
        gbc.weighty = 0.8;
//        gbc.gridheight = 10;
        gbc.fill = GridBagConstraints.BOTH;
        panel.add(new JScrollPane(messageArea), gbc);
        gbc.gridy++;

        // cancel/close button
        cancelButton = new JButton(Translator.localize("button.cancel"));

        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.SOUTH;
        gbc.weighty = 0.1;
        gbc.gridheight = GridBagConstraints.REMAINDER;
        panel.add(cancelButton, gbc);
        gbc.gridy++;
        
        cancelButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (isComplete()) {
                    close();
                }
                canceled = true;
            }

        });
        
        getContentPane().add(panel);
        pack();
        Dimension contentPaneSize = getContentPane().getPreferredSize();
        setLocation(scrSize.width / 2 - contentPaneSize.width / 2,
                scrSize.height / 2 - contentPaneSize.height / 2);
        setResizable(true);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(this);
    }

    public void setMaximumProgress(final int i) {
        SwingUtilities.invokeLater(new Runnable () {
            public void run() {
                progress.setMaximum(i);
                setVisible(true);
            }
        });
    }

    public void updateProgress(final int i) {
        SwingUtilities.invokeLater(new Runnable () {
            public void run() {
                progress.setValue(i);
                if (isComplete()) {
                    if (hasMessages) {
                        cancelButton.setText(
                                Translator.localize("button.close"));
                    } else {
                        close();
                    }
                }
            }
        });
    }
    
    private boolean isComplete() {
        return progress.getValue() == progress.getMaximum();
    }

    /**
     * The UID.
     */
    private static final long serialVersionUID = -1336242911879462274L;

    /*
     * @see org.argouml.application.api.ProgressMonitor#close()
     */
    public void close() {
        SwingUtilities.invokeLater(new Runnable () {
            public void run() {
                setVisible(false);
                dispose();
            }
        });
    }

    /*
     * @see org.argouml.application.api.ProgressMonitor#isCanceled()
     */
    public boolean isCanceled() {
        return canceled;
    }

    /*
     * @see org.argouml.application.api.ProgressMonitor#notifyMessage(java.lang.String, java.lang.String, java.lang.String)
     */
    public void notifyMessage(final String title, final String introduction,
            final String message) {
        hasMessages = true;
        // TODO: Add filename ?
        messageArea.setText(messageArea.getText() + title + "\n" + introduction
                + "\n" + message + "\n\n");
        messageArea.setCaretPosition(messageArea.getText().length());
    }

    /*
     * @see org.argouml.application.api.ProgressMonitor#notifyNullAction()
     */
    public void notifyNullAction() {
        String msg = Translator.localize("label.import.empty");
        notifyMessage(msg, msg, msg);
    }

    /*
     * @see org.argouml.application.api.ProgressMonitor#updateMainTask(java.lang.String)
     */
    public void updateMainTask(final String name) {
        SwingUtilities.invokeLater(new Runnable () {
            public void run() {
                setTitle(name);
            }
        });
    }

    /*
     * @see org.argouml.application.api.ProgressMonitor#updateSubTask(java.lang.String)
     */
    public void updateSubTask(final String action) {
        SwingUtilities.invokeLater(new Runnable () {
            public void run() {
                progressLabel.setText(action);
            }
        });
    }

    /*
     * @see org.argouml.persistence.ProgressListener#progress(org.argouml.persistence.ProgressEvent)
     */
    public void progress(ProgressEvent event) throws InterruptedException {
        // ignored
    }

    public void windowClosing(WindowEvent e) {
        // User closing the progress window is interpreted as cancel request
        canceled = true;
        close();
    }
    
    public void windowActivated(WindowEvent e) { }
    public void windowClosed(WindowEvent e) { }
    public void windowDeactivated(WindowEvent e) { }
    public void windowDeiconified(WindowEvent e) { }
    public void windowIconified(WindowEvent e) { }
    public void windowOpened(WindowEvent e) { }

}
