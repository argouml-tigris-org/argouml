// $Id$
// Copyright (c) 1996-2005 The Regents of the University of California. All
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

import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.apache.log4j.Logger;
import org.argouml.cognitive.Poster;
import org.argouml.cognitive.ToDoItem;
import org.argouml.i18n.Translator;
import org.tigris.swidgets.LabelledLayout;

/** The email expert dialog does not work and is in
 * desperate need of some attention.
 *
 * Ideally, this would allow users to directly
 * contact the developers responsible for a piece
 * of code.
 *
 * Enabling this feature would go along way
 * to developing a fully collaborative environment
 * within argo.
 */
public class EmailExpertDialog extends ArgoDialog {
    private static final Logger LOG =
	Logger.getLogger(EmailExpertDialog.class);

    ////////////////////////////////////////////////////////////////
    // instance variables

    /** This field sets the email of the recipient.
     * As yet, the
     * user can not access a list of contributors to a
     * particular argo project.
     */
    private JTextField emailTo;
    private JTextField emailCc;
    /** The subject line should be automatically
     * generated based on the class or the
     * diagram.
     */
    private JTextField emailSubject;
    private JTextArea  emailBody;

    /**
     * The target todo item.
     */
    private ToDoItem target;

    /**
     * The constructor.
     */
    public EmailExpertDialog() {
        super(Translator.localize("dialog.title.send-email-to-expert"),
	      ArgoDialog.OK_CANCEL_OPTION,
	      true);

        getOkButton().setText(Translator.localize("button.send"));
        getOkButton().setMnemonic(
                Translator.localize("button.send.mnemonic").charAt(0));

        emailTo = new JTextField(30);
        emailCc = new JTextField(30);
        emailSubject = new JTextField(30);
        emailBody = new JTextArea(10, 30);

        JLabel toLabel = new JLabel(Translator.localize("label.to"));
        JLabel ccLabel = new JLabel(Translator.localize("label.cc"));
        JLabel subjectLabel = new JLabel(Translator.localize("label.subject"));

        JPanel panel = new JPanel(new LabelledLayout(getLabelGap(),
                getComponentGap()));

        toLabel.setLabelFor(emailTo);
        panel.add(toLabel);
        panel.add(emailTo);

        ccLabel.setLabelFor(emailCc);
        panel.add(ccLabel);
        panel.add(emailCc);

        subjectLabel.setLabelFor(emailSubject);
        panel.add(subjectLabel);
        panel.add(emailSubject);

        JScrollPane bodyScroller = new JScrollPane(emailBody);
        bodyScroller.setPreferredSize(new Dimension(100, 50));
        panel.add(bodyScroller);

        setContent(panel);
    }

    /**
     * @param t the target object
     */
    public void setTarget(Object t) {
	target = (ToDoItem) t;
	Poster p = target.getPoster();
	emailTo.setText(p.getExpertEmail());
	emailSubject.setText(target.getHeadline());
    }

    /**
     * Event handler.
     *
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
	super.actionPerformed(e);
	if (e.getSource() == getOkButton()) {
//	    String to = emailTo.getText();
//	    String cc = emailCc.getText();
//	    String subject = emailSubject.getText();
	    LOG.warn("sending email is not implemented!");
	} else {
	    if (e.getSource() == getCancelButton()) {
	        LOG.debug("cancel");
	    }
	}
    }
} /* end class EmailExpertDialog */
