// $Id$
// Copyright (c) 1996-2007 The Regents of the University of California. All
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
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import org.argouml.i18n.Translator;
import org.argouml.util.osdep.StartBrowser;

/**
 * A window that displays an exception to the user if we can't handle it
 * in any other way.
 * 
 * TODO: This has been partly converted to be a generic error dialog 
 * rather than something specific to exceptions.  This should be renamed
 * when that process is complete.
 */
public class ExceptionDialog extends JDialog implements ActionListener {

    private JButton closeButton;
    private JButton copyButton;
    private JLabel northLabel;
    private JEditorPane textArea;

    /**
     * Construct an exception dialog for the given frame and throwable.
     *
     * @param f the <code>Frame</code> from which the dialog is displayed
     * @param e the exception
     */
    public ExceptionDialog(Frame f, Throwable e) {
        this(f, Translator.localize("dialog.exception.unknown.error"), e);
    }

    /**
     * Construct an exception dialog with the given parameters.
     *
     * @param f the <code>Frame</code> from which the dialog is displayed
     * @param message the message
     * @param e the exception
     */
    public ExceptionDialog(Frame f, String message, Throwable e) {
        this(f, message, e, false);
    }

    /**
     * Construct an exception dialog with the given parameters.
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
        this(f, formatException(message, e, highlightCause));
    }

    public static String formatException(String message, Throwable e,
            boolean highlightCause) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);

        if (highlightCause && e.getCause() != null) {

            // This text is for the developers.
            // It doesn't need to be localized.
            pw.print(message );
            pw.print("<hr>System Info:<p>" + SystemInfoDialog.getInfo());
            pw.print("<p><hr>Error occurred at : " + new Date());
            pw.print("<p>  Cause : ");
            e.getCause().printStackTrace(pw);
            pw.print("-------<p>Full exception : ");
        }
        e.printStackTrace(pw);
        return sw.toString();
    }

    /**
     * Construct an exception dialog with a preformatted error string.
     *
     * @param f   the <code>Frame</code> from which the dialog is displayed
     * @param message
     *            the message
     * @deprecated by tfmorris for 0.24, use 
     * {@link #ExceptionDialog(Frame, String, String, String)}
     */
    @Deprecated
    public ExceptionDialog(Frame f, String message) {
        this(f, Translator.localize("dialog.exception.title"), 
                Translator.localize("dialog.exception.message"), 
                message);
    }
    
    /**
     * Construct an exception dialog with given title, introduction, and detail
     * message.
     * 
     * @param f
     *            the <code>Frame</code> from which the dialog is displayed
     * @param title
     *            string to use as title of dialog box
     * @param intro
     *            introductory text (summary of error)
     * @param message
     *            the message
     */
    public ExceptionDialog(Frame f, String title, String intro, 
            String message) {
        super(f);
        setResizable(true);
        setModal(false);
        setTitle(title);

        Dimension scrSize = Toolkit.getDefaultToolkit().getScreenSize();
        getContentPane().setLayout(new BorderLayout(0, 0));

        // the introducing label
        northLabel =
            new JLabel(intro);
        northLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        getContentPane().add(northLabel, BorderLayout.NORTH);

        // the text box containing the problem messages
        // TODO: This should be hidden by default, but accessible on 
        // via a "details" button or tab to provide more info to the user.
        textArea = new JEditorPane();
        textArea.setContentType("text/html");
        textArea.setEditable(false);
        textArea.addHyperlinkListener(new HyperlinkListener() {
            public void hyperlinkUpdate(HyperlinkEvent hle) {
                linkEvent(hle);
            }
        });

        // These shouldn't really be <br> instead of <p> elements, but
        // the lines all get run together when pasted into a browser window.
        textArea.setText(message.replaceAll("\n", "<p>"));
        textArea.setCaretPosition(0);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(new JScrollPane(textArea));
        centerPanel.setPreferredSize(new Dimension(500, 200));
        getContentPane().add(centerPanel);

        copyButton =
            new JButton(Translator.localize("button.copy-to-clipboard"));
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

        pack();
        Dimension contentPaneSize = getContentPane().getSize();
        setLocation(scrSize.width / 2 - contentPaneSize.width / 2,
                scrSize.height / 2 - contentPaneSize.height / 2);
    }

    /*
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
        disposeDialog();
    }

    /**
     * Copy the textpane's contents to the clipboard.
     *
     * @param e the actionEvent
     */
    private void copyActionPerformed(ActionEvent e) {
        assert e.getSource() == copyButton;
        textArea.setSelectionStart(0);
        textArea.setSelectionEnd(textArea.getText().length());
        textArea.copy();
        textArea.setSelectionEnd(0);
    }

    /**
     * Dispose this dialog.
     */
    private void disposeDialog() {
        setVisible(false);
        dispose();
    }

    /**
     * Handle link activation for our hyperlink.
     *
     * @param e the event
     */
    private void linkEvent(HyperlinkEvent e) {
        if (e.getEventType().equals(HyperlinkEvent.EventType.ACTIVATED)) {
            StartBrowser.openUrl(e.getURL());
        }
    }

     /**
     * The UID.
     */
    private static final long serialVersionUID = -2773182347529547418L;

}
