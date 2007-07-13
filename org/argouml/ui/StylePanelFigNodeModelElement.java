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

import java.awt.FlowLayout;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyListener;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.argouml.i18n.Translator;
import org.argouml.uml.diagram.PathContainer;
import org.tigris.gef.ui.ColorRenderer;

/**
 * Stylepanel which provides base style information for modelelements, e.g.
 * shadow width.
 *
 */
public class StylePanelFigNodeModelElement
    extends StylePanelFig
    implements ItemListener, FocusListener, KeyListener {

    /**
     * Flag to indicate that a refresh is going on.
     */
    private boolean refreshTransaction;

    private JLabel displayLabel = new JLabel(
            Translator.localize("label.stylepane.display"));

    private JCheckBox pathCheckBox = new JCheckBox(
            Translator.localize("label.stylepane.path"));

    private JPanel displayPane;

    /**
     * The constructor.
     *
     */
    public StylePanelFigNodeModelElement() {
        super();

        getFillField().setRenderer(new ColorRenderer());
        getLineField().setRenderer(new ColorRenderer());

        displayPane = new JPanel();
        displayPane.setLayout(new FlowLayout(FlowLayout.LEFT));
        addToDisplayPane(pathCheckBox);

        displayLabel.setLabelFor(displayPane);
        add(displayPane, 0); // add in front of the others
        add(displayLabel, 0); // add the label in front of the "pane"

        //This instead of the label ???
        //displayPane.setBorder(new TitledBorder(
        //    Translator.localize("Display: ")));

        pathCheckBox.addItemListener(this);
    }

    /**
     * Add a given checkbox to the panel.
     * 
     * @param cb the given checkbox
     */
    public void addToDisplayPane(JCheckBox cb) {
        displayPane.add(cb);
    }

    /*
     * @see org.argouml.ui.TabTarget#refresh()
     */
    public void refresh() {
        refreshTransaction = true;
        // Let the parent do its refresh.
        super.refresh();
        Object target = getPanelTarget();
        // TODO: Why is this code even getting called for a FigGeneralization?
        if (target instanceof PathContainer) {
            PathContainer pc = (PathContainer) getPanelTarget();
            pathCheckBox.setSelected(pc.isPathVisible());
        }
        refreshTransaction = false;

        // lets redraw the box
        setTargetBBox();
    }

    /*
     * @see java.awt.event.ItemListener#itemStateChanged(java.awt.event.ItemEvent)
     */
    public void itemStateChanged(ItemEvent e) {
        if (!refreshTransaction) {
            Object src = e.getSource();
            if (src == pathCheckBox) {
                PathContainer pc = (PathContainer) getPanelTarget();
                pc.setPathVisible(pathCheckBox.isSelected());
            } else {
                super.itemStateChanged(e);
            }
        }
    }

} /* end class StylePanelFigNodeModelElement */
