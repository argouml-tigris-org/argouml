// $Id: WizStep.java 12753 2007-06-04 18:07:56Z mvw $
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

package org.argouml.cognitive.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.cognitive.ToDoItem;
import org.argouml.cognitive.Translator;
import org.argouml.cognitive.critics.Wizard;
import org.argouml.swingext.SpacerPanel;
import org.argouml.ui.ProjectBrowser;
import org.argouml.ui.TabToDoTarget;
import org.argouml.ui.targetmanager.TargetEvent;
import org.argouml.util.osdep.StartBrowser;

/**
 * Each Critic may provide a Wizard to help fix the problem it
 * identifies.  The "Next>" button will advance through the steps of
 * the wizard, and increase the colored progress bar on the ToDoItem
 * "sticky note" icon in ToDo tree pane.
 *
 * @see org.argouml.cognitive.Critic
 * @see org.argouml.cognitive.critics.Wizard
 */

public class WizStep extends JPanel
    implements TabToDoTarget, ActionListener, DocumentListener {
    ////////////////////////////////////////////////////////////////
    // constants
    private static final ImageIcon WIZ_ICON =
	ResourceLoaderWrapper
	    .lookupIconResource("Wiz", "Wiz");

    ////////////////////////////////////////////////////////////////
    // instance variables

    private JPanel  mainPanel = new JPanel();
    private JButton backButton =
        new JButton(Translator.localize("button.back"));
    private JButton nextButton =
        new JButton(Translator.localize("button.next"));
    private JButton finishButton =
        new JButton(Translator.localize("button.finish"));
    private JButton helpButton =
        new JButton(Translator.localize("button.help"));
    private JPanel  buttonPanel = new JPanel();

    /**
     * The current target.
     */
    private Object target;

    /**
     * @return Returns the main Panel.
     */
    protected JPanel getMainPanel() {
        return mainPanel;
    }
    /**
     * @return Returns the WIZ_ICON.
     */
    protected static ImageIcon getWizardIcon() {
        return WIZ_ICON;
    }

    /**
     * @param b the button to set the mnemonic for
     * @param key the mnemonic
     */
    protected static final void setMnemonic(JButton b, String key) {
	String m = Translator.localize(key);
	if (m == null) {
	    return;
        }
	if (m.length() == 1) {
	    b.setMnemonic(m.charAt(0));
	}
    }


    /**
     * The constructor.
     */
    public WizStep() {
	setMnemonic(backButton, "mnemonic.button.back");
	setMnemonic(nextButton, "mnemonic.button.next");
	setMnemonic(finishButton, "mnemonic.button.finish");
	setMnemonic(helpButton, "mnemonic.button.help");
	buttonPanel.setLayout(new GridLayout(1, 5));
	buttonPanel.add(backButton);
	buttonPanel.add(nextButton);
	buttonPanel.add(new SpacerPanel());
	buttonPanel.add(finishButton);
	buttonPanel.add(new SpacerPanel());
	buttonPanel.add(helpButton);

	backButton.setMargin(new Insets(0, 0, 0, 0));
	nextButton.setMargin(new Insets(0, 0, 0, 0));
	finishButton.setMargin(new Insets(0, 0, 0, 0));
	helpButton.setMargin(new Insets(0, 0, 0, 0));

	JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
	southPanel.add(buttonPanel);

	setLayout(new BorderLayout());
	add(mainPanel, BorderLayout.CENTER);
	add(southPanel, BorderLayout.SOUTH);

	backButton.addActionListener(this);
	nextButton.addActionListener(this);
	finishButton.addActionListener(this);
	helpButton.addActionListener(this);
    }

    ////////////////////////////////////////////////////////////////
    // accessors

    /**
     * @param item the target item
     */
    public void setTarget(Object item) {
	target = item;
	enableButtons();
    }

    /**
     * Enable/Disable the buttons.
     */
    public void enableButtons() {
        if (target == null) {
            backButton.setEnabled(false);
            nextButton.setEnabled(false);
            finishButton.setEnabled(false);
            helpButton.setEnabled(false);
        } else if (target instanceof ToDoItem) {
            ToDoItem tdi = (ToDoItem) target;
            Wizard w = getWizard();
            backButton.setEnabled(w != null ? w.canGoBack() : false);
            nextButton.setEnabled(w != null ? w.canGoNext() : false);
            finishButton.setEnabled(w != null ? w.canFinish() : false);

            if (tdi.getMoreInfoURL() == null
                    || "".equals(tdi.getMoreInfoURL())) {
                helpButton.setEnabled(false);
            } else {
                helpButton.setEnabled(true);
            }
        } else {
            return;
        }
    }

    /**
     * Set the target anew.
     *
     * TODO: This method is never used. What is its intention? Remove it?
     */
    public void refresh() { setTarget(target); }

    /**
     * @return the Wizard, or null
     */
    public Wizard getWizard() {
	if (target instanceof ToDoItem) {
	    return ((ToDoItem) target).getWizard();
	}
	return null;
    }

    ////////////////////////////////////////////////////////////////
    // actions

    /**
     * The Back button has been pressed, so we do the "back" action.
     */
    public void doBack() {
	Wizard w = getWizard();
	if (w != null) {
	    w.back();
	    updateTabToDo();
	}
    }
    /**
     * The Next button has been pressed, so we do the "next" action.
     */
    public void doNext() {
	Wizard w = getWizard();
	if (w != null) {
	    w.next();
	    updateTabToDo();
	}
    }

    /**
     * The Finish button has been pressed, so we do the "finish" action.
     */
    public void doFinsh() {
	Wizard w = getWizard();
	if (w != null) {
	    w.finish();
	    updateTabToDo();
	}
    }

    /**
     * Called when the Help button is pressed.
     */
    public void doHelp() {
	if (!(target instanceof ToDoItem)) {
	    return;
        }
	ToDoItem item = (ToDoItem) target;
	String urlString = item.getMoreInfoURL();
	StartBrowser.openUrl(urlString);
    }

    /**
     * Set the target and make visible.
     */
    protected void updateTabToDo() {
	TabToDo ttd =
	    (TabToDo) ProjectBrowser.getInstance().getTab(TabToDo.class);
	JPanel ws = getWizard().getCurrentPanel();
	if (ws instanceof WizStep) {
	    ((WizStep) ws).setTarget(target);
        }
	ttd.showStep(ws);
    }

    ////////////////////////////////////////////////////////////////
    // ActionListener implementation

    /*
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent ae) {
	Object src = ae.getSource();
	if (src == backButton) {
	    doBack();
        } else if (src == nextButton) {
	    doNext();
        } else if (src == finishButton) {
	    doFinsh();
        } else if (src == helpButton) {
	    doHelp();
        }
    }

    ////////////////////////////////////////////////////////////////
    // DocumentListener implementation

    /*
     * @see javax.swing.event.DocumentListener#insertUpdate(javax.swing.event.DocumentEvent)
     */
    public void insertUpdate(DocumentEvent e) {
	enableButtons();
    }

    /*
     * @see javax.swing.event.DocumentListener#removeUpdate(javax.swing.event.DocumentEvent)
     */
    public void removeUpdate(DocumentEvent e) { insertUpdate(e); }

    /*
     * @see javax.swing.event.DocumentListener#changedUpdate(javax.swing.event.DocumentEvent)
     */
    public void changedUpdate(DocumentEvent e) {
	// Apparently, this method is never called.
    }

    ////////////////////////////////////////////////////////////////
    // TargetListener implementation

    /*
     * @see org.argouml.ui.targetmanager.TargetListener#targetAdded(org.argouml.ui.targetmanager.TargetEvent)
     */
    public void targetAdded(TargetEvent e) {
	setTarget(e.getNewTarget());

    }

    /*
     * @see org.argouml.ui.targetmanager.TargetListener#targetRemoved(org.argouml.ui.targetmanager.TargetEvent)
     */
    public void targetRemoved(TargetEvent e) {
	// how to handle empty target lists?
	// probably the wizstep should only show an empty pane in that case
	setTarget(e.getNewTarget());

    }

    /*
     * @see org.argouml.ui.targetmanager.TargetListener#targetSet(org.argouml.ui.targetmanager.TargetEvent)
     */
    public void targetSet(TargetEvent e) {
	setTarget(e.getNewTarget());
    }


    /**
     * The UID.
     */
    private static final long serialVersionUID = 8845081753813440684L;
} /* end class WizStep */
