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
package org.argouml.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.argouml.i18n.Translator;

/**
 * A window that displays an exception to the user if we can't handle it
 * in any other way.
 */
public class ExceptionDialog extends JDialog implements ActionListener {

    private JButton closeButton;
    private JButton copyButton;
    private JLabel northLabel;
    private JEditorPane textArea;

    /**
     * The constructor.
     *
     * @param f the <code>Frame</code> from which the dialog is displayed
     * @param e the exception
     */
    public ExceptionDialog(Frame f, Throwable e) {
        this(f, Translator.localize("dialog.exception.unknown.error"), e);
    }

    /**
     * The constructor.
     *
     * @param f the <code>Frame</code> from which the dialog is displayed
     * @param message the message
     * @param e the exception
     */
    public ExceptionDialog(Frame f, String message, Throwable e) {
        this(f, message, e, false);
    }

    /**
     * The constructor.
     *
     * @param f   the <code>Frame</code> from which the dialog is displayed
     * @param message
     *            the message
     * @param e   the exception
     * @param highlightCause
     *            give priority to Throwable.cause in display. Use this if the
     *            main exception is mostly boilerplate and the really useful
     *            information is in the enclosed cause.
     */
    public ExceptionDialog(Frame f, String message, Throwable e,
            boolean highlightCause) {
        super(f);
        setResizable(true);
        setModal(false);
        setTitle(Translator.localize("dialog.exception.title"));

        Dimension scrSize = Toolkit.getDefaultToolkit().getScreenSize();
        getContentPane().setLayout(new BorderLayout(0, 0));

        // the introducing label
        northLabel = new JLabel(Translator.localize("dialog.exception.message"));
        northLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        getContentPane().add(northLabel, BorderLayout.NORTH);

        // the text box containing the problem messages
        textArea = new JEditorPane();

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        
        if (highlightCause && e.getCause() != null) {
            // Repeat the instructions so they can click on the link
            pw.print(Translator.localize("dialog.exception.link.report"));
            // This text is for the developers.  It doesn't need to be localized.
            pw.print("\n" + message);
            pw.print("\n\nSystem Info:\n" + SystemInfoDialog.getInfo());
            pw.print("\nError occurred at :" + new Date().toGMTString());
            pw.print("\n  Cause : ");
            e.getCause().printStackTrace(pw);
            pw.print("-------\nFull exception : ");
        }
        e.printStackTrace(pw);
        String exception = sw.toString();

        textArea.setText(exception);
        textArea.setCaretPosition(0);
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(new JScrollPane(textArea));
        centerPanel.setPreferredSize(new Dimension(500, 200));
        getContentPane().add(centerPanel);

        copyButton = new JButton(Translator.localize("button.copy-to-clipboard"));
        copyButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                copyActionPerformed(evt);
            }
        });

        closeButton = new JButton(Translator.localize("button.close"));
        closeButton.addActionListener(this);
        JPanel bottomPanel = new JPanel();
        
        bottomPanel.add(copyButton);
        bottomPanel.add(closeButton);
        getContentPane().add(bottomPanel, BorderLayout.SOUTH);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent evt) {
                disposeDialog();
            }
        });

        Dimension contentPaneSize = getContentPane().getPreferredSize();
        setLocation(scrSize.width / 2 - contentPaneSize.width / 2,
                scrSize.height / 2 - contentPaneSize.height / 2);
        pack();
    }

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
        disposeDialog();
    }

    /**
     * Copy the textpane's contents to the clipboard.
     * 
     * @param e the action
     */
    private void copyActionPerformed(ActionEvent e) {
        assert e.getSource() == copyButton;
        String infoText = textArea.getText();
        StringSelection contents = new StringSelection(infoText);
        Clipboard clipboard = getToolkit().getSystemClipboard();
        clipboard.setContents(contents, defaultClipboardOwner);
    } // end copy_actionPerformed()
    
    private void disposeDialog() {
        setVisible(false);
        dispose();
    }

    private static ClipboardOwner defaultClipboardOwner =
        new ClipboardObserver();

    static class ClipboardObserver implements ClipboardOwner {
        public void lostOwnership(Clipboard clipboard, Transferable contents) {
        }
    }
    
    /**
     * The UID.
     */
    private static final long serialVersionUID = -2773182347529547418L;

}
