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
import org.argouml.uml.diagram.ui.FigNodeModelElement;
import org.tigris.gef.ui.ColorRenderer;

/**
 * Stylepanel which provides base style information for modelelements, e.g.
 * shadow width.
 * 
 */
public class StylePanelFigNodeModelElement extends StylePanelFig implements
        ItemListener, FocusListener, KeyListener {

    protected JLabel _shadowLabel = new JLabel(Translator
            .localize("label.stylepane.shadow")
            + ": ");

    protected JComboBox _shadowField = new ShadowComboBox();

    public StylePanelFigNodeModelElement() {
        super("Fig Appearance");
        initChoices();
        GridBagLayout gb = (GridBagLayout) getLayout();
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.ipadx = 0;
        c.ipady = 0;

        Document bboxDoc = _bboxField.getDocument();
        bboxDoc.addDocumentListener(this);
        _bboxField.addKeyListener(this);
        _bboxField.addFocusListener(this);
        _fillField.addItemListener(this);
        _lineField.addItemListener(this);
        _shadowField.addItemListener(this);

        _fillField.setRenderer(new ColorRenderer());
        _lineField.setRenderer(new ColorRenderer());

        c.gridx = 0;
        c.gridwidth = 1;
        c.gridy = 1;
        c.weightx = 0.0;
        gb.setConstraints(_bboxLabel, c);
        add(_bboxLabel);
        c.gridy = 2;
        gb.setConstraints(_fillLabel, c);
        add(_fillLabel);
        c.gridy = 3;
        gb.setConstraints(_lineLabel, c);
        add(_lineLabel);
        c.gridy = 4;
        gb.setConstraints(_shadowLabel, c);
        add(_shadowLabel);

        c.weightx = 1.0;
        c.gridx = 1;
        c.gridy = 1;
        gb.setConstraints(_bboxField, c);
        add(_bboxField);
        c.gridy = 2;
        gb.setConstraints(_fillField, c);
        add(_fillField);
        c.gridy = 3;
        gb.setConstraints(_lineField, c);
        add(_lineField);
        c.gridy = 4;
        gb.setConstraints(_shadowField, c);
        add(_shadowField);

        c.weightx = 0.0;
        c.gridx = 2;
        c.gridy = 1;
        gb.setConstraints(_spacer, c);
        add(_spacer);

        c.gridx = 3;
        c.gridy = 10;
        gb.setConstraints(_spacer2, c);
        add(_spacer2);

        c.weightx = 1.0;
        c.gridx = 4;
        c.gridy = 10;
        gb.setConstraints(_spacer3, c);
        add(_spacer3);
    }

    /**
     * @see org.argouml.ui.TabTarget#refresh()
     */
    public void refresh() {

        // Let the parent do its refresh.

        super.refresh();

        // Change the shadow size if appropriate
        if (_target instanceof FigNodeModelElement) {

            int shadowSize = ((FigNodeModelElement) _target).getShadowSize();

            if (shadowSize > 0) {
                _shadowField.setSelectedIndex(shadowSize);
            } else {
                _shadowField.setSelectedIndex(0);
            }
        }
        // lets redraw the box
        setTargetBBox();
    }

    public void setTargetShadow() {
        int i = _shadowField.getSelectedIndex();
        if (_target == null || !(_target instanceof FigNodeModelElement))
                return;
        FigNodeModelElement nodeTarget = (FigNodeModelElement) _target;
        int oldShadowSize = nodeTarget.getShadowSize();
        nodeTarget.setShadowSize(i);
        _target.endTrans();
        if (i != oldShadowSize) {
            markNeedsSave();
        }
    }

    public void itemStateChanged(ItemEvent e) {
        super.itemStateChanged(e);
        Object src = e.getSource();
        if (src == _shadowField) setTargetShadow();

    }

} /* end class StylePanelFigNodeModelElement */
