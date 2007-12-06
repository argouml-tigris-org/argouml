// $Id$
// Copyright (c) 1996-2007 The Regents of the University of California. All
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

package org.argouml.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.log4j.Logger;
import org.argouml.application.api.AbstractArgoJPanel;
import org.argouml.i18n.Translator;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import org.argouml.ui.targetmanager.TargetEvent;
import org.argouml.uml.diagram.ArgoDiagram;
import org.tigris.gef.presentation.Fig;
import org.tigris.swidgets.LabelledLayout;

/**
 * The Presentation panel - formerly called style panel.
 *
 */
public class StylePanel
    extends AbstractArgoJPanel
    implements TabFigTarget,
                ItemListener, DocumentListener, ListSelectionListener,
                ActionListener {
    /**
     * Logger.
     */
    private static final Logger LOG = Logger.getLogger(StylePanel.class);

    private Fig panelTarget;

    /**
     * The constructor.
     *
     * @param tag The localization tag for the panel title.
     */
    public StylePanel(String tag) {
	super(Translator.localize(tag));
        setLayout(new LabelledLayout());
    }

    /**
     * Add a separator.
     */
    protected final void addSeperator() {
        add(LabelledLayout.getSeperator());
    }

    /**
     * This method must be overridden by implementors if they don't want to
     * refresh the whole style panel every time a property change events is
     * fired.  The default behavior is to always call {@link #refresh()}.
     * @see #refresh()
     */
    public void refresh(PropertyChangeEvent e) {
	refresh();
    }

    /*
     * @see org.argouml.ui.TabTarget#setTarget(java.lang.Object)
     */
    public void setTarget(Object t) {
	if (!(t instanceof Fig)) {
	    if (Model.getFacade().isAUMLElement(t)) {
		Project p =
                    ProjectManager.getManager()
                        .getCurrentProject();
                ArgoDiagram diagram = p.getActiveDiagram();
                if (diagram != null) {
                    t = diagram.presentationFor(t);
                }
		if (!(t instanceof Fig)) {
		    return;
		}
	    } else {
		return;
	    }

	}
	panelTarget = (Fig) t;
	refresh();
    }

    /*
     * @see org.argouml.ui.TabTarget#getTarget()
     */
    public Object getTarget() {
	return panelTarget;
    }

    /*
     * @see org.argouml.ui.TabTarget#refresh()
     */
    public void refresh() {
	//_tableModel.setTarget(_target);
	//_table.setModel(_tableModel);
    }

    /*
     * Style panels only apply when a Fig is selected.
     *
     * @see org.argouml.ui.TabTarget#shouldBeEnabled(java.lang.Object)
     */
    public boolean shouldBeEnabled(Object target) {
	ArgoDiagram diagram =
            ProjectManager.getManager()
                .getCurrentProject().getActiveDiagram();
	target =
            (target instanceof Fig) ? target : diagram.getContainingFig(target);
	return (target instanceof Fig);
    }

    /*
     * @see javax.swing.event.DocumentListener#insertUpdate(javax.swing.event.DocumentEvent)
     */
    public void insertUpdate(DocumentEvent e) {
	LOG.debug(getClass().getName() + " insert");
    }

    /*
     * @see javax.swing.event.DocumentListener#removeUpdate(javax.swing.event.DocumentEvent)
     */
    public void removeUpdate(DocumentEvent e) {
	insertUpdate(e);
    }

    /*
     * @see javax.swing.event.DocumentListener#changedUpdate(javax.swing.event.DocumentEvent)
     */
    public void changedUpdate(DocumentEvent e) {
    }

    /*
     * @see java.awt.event.ItemListener#itemStateChanged(java.awt.event.ItemEvent)
     */
    public void itemStateChanged(ItemEvent e) {
    }

    /*
     * @see javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event.ListSelectionEvent)
     */
    public void valueChanged(ListSelectionEvent lse) {
    }

    /*
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent ae) {
	// Object src = ae.getSource();
	//if (src == _config) doConfig();
    }

    /*
     * @see org.argouml.ui.targetmanager.TargetListener#targetAdded(
     *      TargetEvent)
     */
    public void targetAdded(TargetEvent e) {
        setTarget(e.getNewTarget());
    }

    /*
     * @see org.argouml.ui.targetmanager.TargetListener#targetRemoved(
     *      TargetEvent)
     */
    public void targetRemoved(TargetEvent e) {
	setTarget(e.getNewTarget());

    }

    /*
     * @see org.argouml.ui.targetmanager.TargetListener#targetSet(TargetEvent)
     */
    public void targetSet(TargetEvent e) {
	setTarget(e.getNewTarget());

    }

    /**
     * @return Returns the _target.
     */
    protected Fig getPanelTarget() {
        return panelTarget;
    }

    /**
     * The UID.
     */
    private static final long serialVersionUID = 2183676111107689482L;
} /* end class StylePanel */
