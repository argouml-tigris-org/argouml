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

import java.awt.BorderLayout;
import java.text.MessageFormat;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.apache.log4j.Logger;
import org.argouml.cognitive.Decision;
import org.argouml.cognitive.Goal;
import org.argouml.cognitive.ToDoItem;
import org.argouml.cognitive.Translator;
import org.argouml.cognitive.critics.Critic;
import org.argouml.model.Model;


/**
 * This class represents the first step of the wizard.
 * It contains the description of the
 * wizard in case the selected target is a todo item.
 * An appropriate message is shown in case nothing is selected, or
 * in case the user selected one of the branches (folders) in the
 * tree in the todo panel.
 */
public class WizDescription extends WizStep {
    /**
     * Logger.
     */
    private static final Logger LOG = Logger.getLogger(WizDescription.class);

    ////////////////////////////////////////////////////////////////
    // instance variables

    private JTextArea description = new JTextArea();


    /**
     * The constructor.
     *
     */
    public WizDescription() {
	super();
	LOG.info("making WizDescription");

	description.setLineWrap(true);
	description.setWrapStyleWord(true);

	getMainPanel().setLayout(new BorderLayout());
	getMainPanel().add(new JScrollPane(description), BorderLayout.CENTER);
    }

    /**
     * @see org.argouml.cognitive.ui.WizStep#setTarget(java.lang.Object)
     */
    public void setTarget(Object item) {
	String message = "";
	super.setTarget(item);
	Object target = item;
	if (target == null) {
	    description.setEditable(false);
	    description.setText(
                Translator.localize("message.item.no-item-selected"));
	} else if (target instanceof ToDoItem) {
	    ToDoItem tdi = (ToDoItem) target;
	    description.setEditable(false);
	    description.setEnabled(true);
	    description.setText(tdi.getDescription());
	    description.setCaretPosition(0);
	} else if (target instanceof PriorityNode) {
	    message =
                MessageFormat.format(
                        Translator.localize("message.item.branch-priority"),
                        new Object [] {
                            target.toString(),
                        });
	    description.setEditable(false);
	    description.setText(message);

	    return;
	} else if (target instanceof Critic) {
	    message =
                MessageFormat.format(
                        Translator.localize("message.item.branch-critic"),
                        new Object [] {
                            target.toString(),
                        });
	    description.setEditable(false);
	    description.setText(message);

	    return;
	} else if (Model.getFacade().isAModelElement(target)) {
	    message =
                MessageFormat.format(
                        Translator.localize("message.item.branch-model"),
                        new Object [] {
                            Model.getFacade().toString(target),
                        });
	    description.setEditable(false);
	    description.setText(message);

	    return;
	} else if (target instanceof Decision) {
	    message =
                MessageFormat.format(
                        Translator.localize("message.item.branch-decision"),
                        new Object [] {
                            Model.getFacade().toString(target),
                        });
	    description.setText(message);

	    return;
	} else if (target instanceof Goal) {
	    message =
                MessageFormat.format(
                        Translator.localize("message.item.branch-goal"),
                        new Object [] {
                            Model.getFacade().toString(target),
                        });
	    description.setText(message);

	    return;
	} else if (target instanceof KnowledgeTypeNode) {
	    message =
                MessageFormat.format(
                        Translator.localize("message.item.branch-knowledge"),
                        new Object [] {
                            Model.getFacade().toString(target),
                        });
	    description.setText(message);

	    return;
	} else {
	    description.setText("");
	    return;
	}
    }

    /**
     * The UID.
     */
    private static final long serialVersionUID = 2545592446694112088L;
} /* end class WizDescription */
