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

import org.apache.log4j.Category;
import org.argouml.swingext.Toolbar;
import org.argouml.ui.targetmanager.TargetEvent;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.ui.TabModelTarget;

/** A tab that contains textual information.
 */
public class TabText
    extends TabSpawnable
    implements TabModelTarget, DocumentListener {
    ////////////////////////////////////////////////////////////////
    // instance variables
    protected Object _target;
    protected JTextArea _text = new JTextArea();
    protected boolean _parseChanges = true;
    protected boolean _shouldBeEnabled = false;
    /** The optional toolbar.
     *  Contains null if no toolbar was requested.
     */
    protected Toolbar _toolbar = null;
    protected Category cat = Category.getInstance(TabText.class);

    ////////////////////////////////////////////////////////////////
    // constructor

    /** Create a text tab without a toolbar.
     */
    public TabText(String title) {
        this(title, false);
    }

    /** Create a text tab and optionally request a toolbar.
     *  @since ARGO0.9.4
     */
    public TabText(String title, boolean withToolbar) {
        super(title);
        setLayout(new BorderLayout());
        _text.setFont(new Font("Monospaced", Font.PLAIN, 12));
        _text.setTabSize(4);
        add(new JScrollPane(_text), BorderLayout.CENTER);
        _text.getDocument().addDocumentListener(this);

        // If a toolbar was requested, create an empty one.
        if (withToolbar) {
            _toolbar = new Toolbar();
            _toolbar.setOrientation(JToolBar.HORIZONTAL);
            add(_toolbar, BorderLayout.NORTH);
        }
    }

    ////////////////////////////////////////////////////////////////
    // accessors

    public void setTarget(Object t) {
        _parseChanges = false;
        if (t == null) {
            _text.setEnabled(false);
            _text.setText("Nothing selected");
            _shouldBeEnabled = false;
        } else {
            _text.setEnabled(true);
            String generatedText = genText(t);
            if (generatedText != null) {
                _text.setText(generatedText);
                _shouldBeEnabled = true;
                _text.setCaretPosition(0);
            } else {
                _text.setEnabled(false);
                _text.setText("N/A");
                _shouldBeEnabled = false;
            }
        }
        _parseChanges = true;
    }

    /**
     * Returns the target of this tab.
     * @deprecated Use TargetManager.getInstance().getTarget() now
     */
    public Object getTarget() {
        return TargetManager.getInstance().getTarget();
    }

    public void refresh() {
        Object target = TargetManager.getInstance().getTarget();
        setTarget(target);
    }

    /**
     * the target must not be null.
     */
    public boolean shouldBeEnabled(Object target) {
        return (target != null);
    }

    protected String genText(Object target) {
        return target == null ? "Nothing selected" : target.toString();
    }

    protected void parseText(String s) {
        if (s == null)
            s = "(null)";
        cat.debug("parsing text:" + s); // THN
    }

    ////////////////////////////////////////////////////////////////
    // event handlers
    public void insertUpdate(DocumentEvent e) {
        if (_parseChanges)
            parseText(_text.getText());
    }

    public void removeUpdate(DocumentEvent e) {
        if (_parseChanges)
            parseText(_text.getText());
    }

    public void changedUpdate(DocumentEvent e) {
        if (_parseChanges)
            parseText(_text.getText());
    }

    /**
      * @see org.argouml.ui.targetmanager.TargetListener#targetAdded(org.argouml.ui.targetmanager.TargetEvent)
      */
    public void targetAdded(TargetEvent e) {
        // we can neglect this, the TabText allways selects the first target
        // in a set of targets. The first target can only be 
        // changed in a targetRemoved or a TargetSet event

    }

    /**
     * @see org.argouml.ui.targetmanager.TargetListener#targetRemoved(org.argouml.ui.targetmanager.TargetEvent)
     */
    public void targetRemoved(TargetEvent e) {
        // how to handle empty target lists?
        // probably the TabText should only show an empty pane in that case
        setTarget(e.getNewTargets()[0]);

    }

    /**
     * @see org.argouml.ui.targetmanager.TargetListener#targetSet(org.argouml.ui.targetmanager.TargetEvent)
     */
    public void targetSet(TargetEvent e) {
        setTarget(e.getNewTargets()[0]);

    }

} /* end class TabText */
