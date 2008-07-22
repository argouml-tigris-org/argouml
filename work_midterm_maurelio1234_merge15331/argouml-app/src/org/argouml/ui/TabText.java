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
import java.awt.Font;
import java.util.Collections;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.apache.log4j.Logger;
import org.argouml.application.api.AbstractArgoJPanel;
import org.argouml.swingext.UpArrowIcon;
import org.argouml.ui.targetmanager.TargetEvent;
import org.argouml.ui.targetmanager.TargetManager;
import org.tigris.toolbar.ToolBarFactory;

/**
 * A tab that contains textual information.
 */
public class TabText
    extends AbstractArgoJPanel
    implements TabModelTarget, DocumentListener {

    private Object target;
    private JTextArea textArea = new JTextArea();
    private boolean parseChanges = true;
    private boolean enabled;

    /**
     * The optional toolbar. Contains <code>null</code> if no toolbar
     * was requested.
     */
    private JToolBar toolbar;

    /**
     * Logger.
     */
    private static final Logger LOG = Logger.getLogger(TabText.class);

    /**
     * Create a text tab without a toolbar.
     *
     * @param title the title of the tab
     */
    public TabText(String title) {
        this(title, false);
    }

    /**
     * Create a text tab and optionally request a toolbar.
     * @since ARGO0.9.4
     *
     * @param title the title
     * @param withToolbar true if a toolbar is needed
     */
    public TabText(String title, boolean withToolbar) {
        super(title);
        setIcon(new UpArrowIcon());
        setLayout(new BorderLayout());
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        textArea.setTabSize(4);
        add(new JScrollPane(textArea), BorderLayout.CENTER);
        textArea.getDocument().addDocumentListener(this);

        // If a toolbar was requested, create an empty one.
        if (withToolbar) {
            toolbar = (new ToolBarFactory(Collections.EMPTY_LIST))
                .createToolBar();
            toolbar.setOrientation(SwingConstants.HORIZONTAL);
            toolbar.setFloatable(false);
            toolbar.setName(getTitle());
            add(toolbar, BorderLayout.NORTH);
        }
    }


    private void doGenerateText() {
        parseChanges = false;
        if (getTarget() == null) {
            textArea.setEnabled(false);
            // TODO: Localize
            textArea.setText("Nothing selected");
            enabled = false;
        } else {
            textArea.setEnabled(true);
	    if (isVisible()) {
		String generatedText = genText(getTarget());
		if (generatedText != null) {
		    textArea.setText(generatedText);
		    enabled = true;
		    textArea.setCaretPosition(0);
		} else {
		    textArea.setEnabled(false);
		    // TODO: Localize
		    textArea.setText("N/A");
		    enabled = false;
		}
	    }
        }
        parseChanges = true;
    }

    /*
     * @see org.argouml.ui.TabTarget#setTarget(java.lang.Object)
     */
    public void setTarget(Object t) {
        target = t;
        if (isVisible()) {
            doGenerateText();
        }
    }

    /*
     * Returns the target of this tab.
     *
     * @see org.argouml.ui.TabTarget#getTarget()
     */
    public Object getTarget() {
        return target;
    }

    /*
     * Refresh the text of the tab.
     *
     * @see org.argouml.ui.TabTarget#refresh()
     */
    public void refresh() {
        Object t = TargetManager.getInstance().getTarget();
        setTarget(t);
    }

    /**
     * This tab pane is enabled if there is a target,
     * i.e. the target must not be null.
     *
     * {@inheritDoc}
     */
    public boolean shouldBeEnabled(Object t) {
        return (t != null);
    }

    /**
     * The target has changed, so let's generate some text to be shown.
     *
     * @param t the object to be "generated" = make a string of it
     * @return the generated text
     */
    protected String genText(Object t) {
        return t == null ? "Nothing selected" : t.toString();
    }

    /**
     * The user has edited the text in the textfield, so let's parse it now,
     * and update the model.
     *
     * @param s the string to parse
     */
    protected void parseText(String s) {
        if (s == null) {
            s = "(null)";
        }
        LOG.debug("parsing text:" + s);
    }

    /*
     * @see javax.swing.event.DocumentListener#insertUpdate(javax.swing.event.DocumentEvent)
     */
    public void insertUpdate(DocumentEvent e) {
        if (parseChanges) {
            parseText(textArea.getText());
        }
    }

    /*
     * @see javax.swing.event.DocumentListener#removeUpdate(javax.swing.event.DocumentEvent)
     */
    public void removeUpdate(DocumentEvent e) {
        if (parseChanges) {
            parseText(textArea.getText());
        }
    }

    /*
     * @see javax.swing.event.DocumentListener#changedUpdate(javax.swing.event.DocumentEvent)
     */
    public void changedUpdate(DocumentEvent e) {
        if (parseChanges) {
            parseText(textArea.getText());
        }
    }

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
        // probably the TabText should only show an empty pane in that case
        setTarget(e.getNewTarget());

    }

    /*
     * @see org.argouml.ui.targetmanager.TargetListener#targetSet(org.argouml.ui.targetmanager.TargetEvent)
     */
    public void targetSet(TargetEvent e) {
        setTarget(e.getNewTarget());

    }

    /**
     * @return Returns the toolbar.
     */
    protected JToolBar getToolbar() {
        return toolbar;
    }

    /**
     * @param s true if we are enabled
     */
    protected void setShouldBeEnabled(boolean s) {
        this.enabled = s;
    }

    /**
     * @return returns true if enabled
     */
    protected boolean shouldBeEnabled() {
        return enabled;
    }

    /**
     * Sets if text area can be edited.
     * @param editable if true, text area is editable (default), else not
     */
    public void setEditable(boolean editable) {
        textArea.setEditable(editable);
    }

    /**
     * Generates the text whenever this panel becomes visible.
     * {@inheritDoc}
     */
    @Override
    public void setVisible(boolean visible) {
	super.setVisible(visible);
	if (visible) {
	    doGenerateText();
	}
    }

    /**
     * The UID.
     */
    private static final long serialVersionUID = -1484647093166393888L;
} /* end class TabText */
