// $Id$
// Copyright (c) 1996-2001 The Regents of the University of California. All
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

import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.apache.log4j.Logger;
import org.argouml.ui.targetmanager.TargetEvent;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.ui.TabModelTarget;
import org.tigris.toolbar.ToolBar;

/** A tab that contains textual information.
 */
public class TabText
    extends TabSpawnable
    implements TabModelTarget, DocumentListener {
    ////////////////////////////////////////////////////////////////
    // instance variables
    private Object target;
    private JTextArea textArea = new JTextArea();
    private boolean parseChanges = true;
    private boolean enabled = false;
    
    /** 
     * The optional toolbar. Contains null if no toolbar was requested.
     */
    private JToolBar toolbar = null;
    
    private static final Logger LOG = Logger.getLogger(TabText.class);

    ////////////////////////////////////////////////////////////////
    // constructor

    /** 
     * Create a text tab without a toolbar.
     * 
     * @param title the title of the tab
     */
    public TabText(String title) {
        this(title, false);
    }

    /** Create a text tab and optionally request a toolbar.
     *  @since ARGO0.9.4
     * 
     * @param title the title
     * @param withToolbar true if a toolbar is needed
     */
    public TabText(String title, boolean withToolbar) {
        super(title);
        setLayout(new BorderLayout());
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        textArea.setTabSize(4);
        add(new JScrollPane(textArea), BorderLayout.CENTER);
        textArea.getDocument().addDocumentListener(this);

        // If a toolbar was requested, create an empty one.
        if (withToolbar) {
            toolbar = new ToolBar();
            toolbar.putClientProperty("JToolBar.isRollover",  Boolean.TRUE);
            toolbar.setOrientation(JToolBar.HORIZONTAL);
            add(toolbar, BorderLayout.NORTH);
        }
    }

    ////////////////////////////////////////////////////////////////
    // accessors

    /**
     * @see org.argouml.ui.TabTarget#setTarget(java.lang.Object)
     */
    public void setTarget(Object t) {
        parseChanges = false;
        target = t;
        if (t == null) {
            textArea.setEnabled(false);
            textArea.setText("Nothing selected");
            enabled = false;
        } else {
            textArea.setEnabled(true);
            String generatedText = genText(t);
            if (generatedText != null) {
                textArea.setText(generatedText);
                enabled = true;
                textArea.setCaretPosition(0);
            } else {
                textArea.setEnabled(false);
                textArea.setText("N/A");
                enabled = false;
            }
        }
        parseChanges = true;
    }

    /**
     * Returns the target of this tab.
     *
     * @see org.argouml.ui.TabTarget#getTarget()
     */
    public Object getTarget() {
        return target;
    }

    /**
     * @see org.argouml.ui.TabTarget#refresh()
     */
    public void refresh() {
        Object t = TargetManager.getInstance().getTarget();
        setTarget(t);
    }

    /**
     * the target must not be null.
     *
     * @see org.argouml.ui.TabTarget#shouldBeEnabled(java.lang.Object)
     */
    public boolean shouldBeEnabled(Object t) {
        return (t != null);
    }

    /**
     * @param t the object to be "generated" = make a string of it
     * @return the generated text
     */
    protected String genText(Object t) {
        return t == null ? "Nothing selected" : t.toString();
    }

    /**
     * @param s the string to parse
     */
    protected void parseText(String s) {
        if (s == null)
            s = "(null)";
        LOG.debug("parsing text:" + s); // THN
    }

    ////////////////////////////////////////////////////////////////
    // event handlers
    
    /**
     * @see javax.swing.event.DocumentListener#insertUpdate(javax.swing.event.DocumentEvent)
     */
    public void insertUpdate(DocumentEvent e) {
        if (parseChanges)
            parseText(textArea.getText());
    }

    /**
     * @see javax.swing.event.DocumentListener#removeUpdate(javax.swing.event.DocumentEvent)
     */
    public void removeUpdate(DocumentEvent e) {
        if (parseChanges)
            parseText(textArea.getText());
    }

    /**
     * @see javax.swing.event.DocumentListener#changedUpdate(javax.swing.event.DocumentEvent)
     */
    public void changedUpdate(DocumentEvent e) {
        if (parseChanges)
            parseText(textArea.getText());
    }

    /**
     * @see org.argouml.ui.targetmanager.TargetListener#targetAdded(org.argouml.ui.targetmanager.TargetEvent)
     */
    public void targetAdded(TargetEvent e) {
        setTarget(e.getNewTarget());

    }

    /**
     * @see org.argouml.ui.targetmanager.TargetListener#targetRemoved(org.argouml.ui.targetmanager.TargetEvent)
     */
    public void targetRemoved(TargetEvent e) {
        // how to handle empty target lists?
        // probably the TabText should only show an empty pane in that case
        setTarget(e.getNewTarget());

    }

    /**
     * @see org.argouml.ui.targetmanager.TargetListener#targetSet(org.argouml.ui.targetmanager.TargetEvent)
     */
    public void targetSet(TargetEvent e) {
        setTarget(e.getNewTarget());

    }

    /**
     * @return Returns the _toolbar.
     */
    protected JToolBar getToolbar() {
        return toolbar;
    }

    /**
     * @param s The _shouldBeEnabled to set.
     */
    protected void setShouldBeEnabled(boolean s) {
        this.enabled = s;
    }

    /**
     * @return Returns the _shouldBeEnabled.
     */
    protected boolean shouldBeEnabled() {
        return enabled;
    }

} /* end class TabText */
