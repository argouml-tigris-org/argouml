// $Id$
// Copyright (c) 1996-2003 The Regents of the University of California. All
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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyListener;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.text.Document;

import org.argouml.i18n.Translator;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.uml.diagram.ui.FigNodeModelElement;
import org.tigris.gef.ui.ColorRenderer;

/**
 * Stylepanel which provides base style information for modelelements, e.g.
 * shadow width.
 * 
 */
public class StylePanelFigNodeModelElement extends StylePanelFig implements
        ItemListener, FocusListener, KeyListener {

    private JLabel shadowLabel = new JLabel(Translator
            .localize("label.stylepane.shadow")
            + ": ");

    private JComboBox shadowField = new ShadowComboBox();

    /**
     * The constructor.
     * 
     */
    public StylePanelFigNodeModelElement() {
        super("Fig Appearance");
        initChoices(); //TODO: MVW: Why do this a second time?
        GridBagLayout gb = (GridBagLayout) getLayout();
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.ipadx = 0;
        c.ipady = 0;

        Document bboxDoc = getBBoxField().getDocument();
        bboxDoc.addDocumentListener(this);
        getBBoxField().addKeyListener(this);
        getBBoxField().addFocusListener(this);
        getFillField().addItemListener(this);
        getLineField().addItemListener(this);
        shadowField.addItemListener(this);

        getFillField().setRenderer(new ColorRenderer());
        getLineField().setRenderer(new ColorRenderer());

        c.gridx = 0;
        c.gridwidth = 1;
        c.gridy = 1;
        c.weightx = 0.0;
        gb.setConstraints(getBBoxLabel(), c);
        add(getBBoxLabel());
        c.gridy = 2;
        gb.setConstraints(getFillLabel(), c);
        add(getFillLabel());
        c.gridy = 3;
        gb.setConstraints(getLineLabel(), c);
        add(getLineLabel());
        c.gridy = 4;
        gb.setConstraints(shadowLabel, c);
        add(shadowLabel);

        c.weightx = 1.0;
        c.gridx = 1;
        c.gridy = 1;
        gb.setConstraints(getBBoxField(), c);
        add(getBBoxField());
        c.gridy = 2;
        gb.setConstraints(getFillField(), c);
        add(getFillField());
        c.gridy = 3;
        gb.setConstraints(getLineField(), c);
        add(getLineField());
        c.gridy = 4;
        gb.setConstraints(shadowField, c);
        add(shadowField);

        c.weightx = 0.0;
        c.gridx = 2;
        c.gridy = 1;
        gb.setConstraints(getSpacer(), c);
        add(getSpacer());

        c.gridx = 3;
        c.gridy = 10;
        gb.setConstraints(getSpacer2(), c);
        add(getSpacer2());

        c.weightx = 1.0;
        c.gridx = 4;
        c.gridy = 10;
        gb.setConstraints(getSpacer3(), c);
        add(getSpacer3());
    }

    /**
     * @see org.argouml.ui.TabTarget#refresh()
     */
    public void refresh() {

        // Let the parent do its refresh.

        super.refresh();

        // Change the shadow size if appropriate
        if (getPanelTarget() instanceof FigNodeModelElement) {

            int shadowSize = ((FigNodeModelElement) 
                    getPanelTarget()).getShadowSize();

            if (shadowSize > 0) {
                shadowField.setSelectedIndex(shadowSize);
            } else {
                shadowField.setSelectedIndex(0);
            }
        }
        // lets redraw the box
        setTargetBBox();
    }

    /**
     * Handle changes in the shadowfield.
     */
    public void setTargetShadow() {
        int i = shadowField.getSelectedIndex();
        if (getPanelTarget() == null 
                || !(getPanelTarget() instanceof FigNodeModelElement))
            return;
        FigNodeModelElement nodeTarget = (FigNodeModelElement) getPanelTarget();
        int oldShadowSize = nodeTarget.getShadowSize();
        nodeTarget.setShadowSize(i);
        getPanelTarget().endTrans();
        if (i != oldShadowSize) {
            Project p = ProjectManager.getManager().getCurrentProject();
            if (p != null) {
                p.setNeedsSave(true);
            }
        }
    }

    /**
     * @see java.awt.event.ItemListener#itemStateChanged(java.awt.event.ItemEvent)
     */
    public void itemStateChanged(ItemEvent e) {
        super.itemStateChanged(e);
        Object src = e.getSource();
        if (src == shadowField) setTargetShadow();

    }

} /* end class StylePanelFigNodeModelElement */
