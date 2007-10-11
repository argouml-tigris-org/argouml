// $Id$
// Copyright (c) 1996-2006 The Regents of the University of California. All
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

import java.awt.Color;
import java.awt.event.ItemEvent;

import javax.swing.JComboBox;
import javax.swing.JLabel;

import org.tigris.gef.presentation.FigText;
import org.tigris.gef.ui.ColorRenderer;

/**
 * StylePanel class which provides additional parameters for changing Text
 * elements provided by GEF.
 *  TODO: i18n
 */
public class StylePanelFigText extends StylePanelFig {

    private static final String[] FONT_NAMES = {
	"dialog", "serif", "sanserif",
	"monospaced",
    };

    private static final Integer[] COMMON_SIZES = {
	Integer.valueOf(8), Integer.valueOf(9),
	Integer.valueOf(10), Integer.valueOf(12), Integer.valueOf(16), Integer.valueOf(18),
	Integer.valueOf(24), Integer.valueOf(36), Integer.valueOf(48), Integer.valueOf(72),
	Integer.valueOf(96),
    };

    private static final String[] STYLES = {
	"Plain", "Bold", "Italic",
	"Bold-Italic",
    };

    private static final String[] JUSTIFIES = {
	"Left", "Right", "Center",
    };

    private JLabel fontLabel = new JLabel("Font: ");

    private JComboBox fontField = new JComboBox(FONT_NAMES);

    private JLabel sizeLabel = new JLabel("Size: ");

    private JComboBox sizeField = new JComboBox(COMMON_SIZES);

    private JLabel styleLabel = new JLabel("Style: ");

    private JComboBox styleField = new JComboBox(STYLES);

    private JLabel justLabel = new JLabel("Justify: ");

    private JComboBox justField = new JComboBox(JUSTIFIES);

    private JLabel textColorLabel = new JLabel("Text Color: ");

    private JComboBox textColorField = new JComboBox();

    /**
     * Construct default style panel for text elements.
     */
    public StylePanelFigText() {
        super();

        fontField.addItemListener(this);
        sizeField.addItemListener(this);
        styleField.addItemListener(this);
        justField.addItemListener(this);
        textColorField.addItemListener(this);

        textColorField.setRenderer(new ColorRenderer());

        textColorLabel.setLabelFor(textColorField);
        add(textColorLabel);
        add(textColorField);

        addSeperator();

        fontLabel.setLabelFor(fontField);
        add(fontLabel);
        add(fontField);

        sizeLabel.setLabelFor(sizeField);
        add(sizeLabel);
        add(sizeField);

        styleLabel.setLabelFor(styleField);
        add(styleLabel);
        add(styleField);

        justLabel.setLabelFor(justField);
        add(justLabel);
        add(justField);

        initChoices2();
    }

    /**
     * Second part of the default style panel construction.
     */
    protected void initChoices2() {
        textColorField.addItem(Color.black);
        textColorField.addItem(Color.white);
        textColorField.addItem(Color.gray);
        textColorField.addItem(Color.lightGray);
        textColorField.addItem(Color.darkGray);
        textColorField.addItem(Color.red);
        textColorField.addItem(Color.blue);
        textColorField.addItem(Color.green);
        textColorField.addItem(Color.orange);
        textColorField.addItem(Color.pink);
        textColorField.addItem("Custom...");

    }

    /**
     * Refresh the style panel when the Fig has been altered.
     *
     * @see org.argouml.ui.TabTarget#refresh()
     */
    public void refresh() {
        super.refresh();
        FigText ft = (FigText) getPanelTarget();
        String fontName = ft.getFontFamily();
        int size = ft.getFontSize();
        String styleName = STYLES[0];

        fontField.setSelectedItem(fontName);
        sizeField.setSelectedItem(Integer.valueOf(size));
        if (ft.getBold()) {
            styleName = STYLES[1];
        }
        if (ft.getItalic()) {
            styleName = STYLES[2];
        }
        if (ft.getBold() && ft.getItalic()) {
            styleName = STYLES[3];
        }
        styleField.setSelectedItem(styleName);

        String justName = JUSTIFIES[0];
        int justCode = ft.getJustification();
        if (justCode >= 0 && justCode <= JUSTIFIES.length) {
            justName = JUSTIFIES[justCode];
        }
        justField.setSelectedItem(justName);

        Color c = ft.getTextColor();
        textColorField.setSelectedItem(c);
        if (c != null && !textColorField.getSelectedItem().equals(c)) {
            textColorField.insertItemAt(c, textColorField.getItemCount() - 1);
            textColorField.setSelectedItem(c);
        }

        if (ft.getFilled()) {
            Color fc = ft.getFillColor();
            getFillField().setSelectedItem(fc);
            if (fc != null && !getFillField().getSelectedItem().equals(fc)) {
                getFillField().insertItemAt(fc,
                                            getFillField().getItemCount() - 1);
                getFillField().setSelectedItem(fc);
            }
        } else {
            getFillField().setSelectedIndex(0);
        }
    }

    /**
     * Set the font of the text element to the selected value.
     */
    protected void setTargetFont() {
        if (getPanelTarget() == null) {
            return;
        }
        String fontStr = (String) fontField.getSelectedItem();
        if (fontStr.length() == 0) {
            return;
        }
        ((FigText) getPanelTarget()).setFontFamily(fontStr);
        getPanelTarget().endTrans();
    }

    /**
     * Change font size of the text element according to the selected value.
     */
    protected void setTargetSize() {
        if (getPanelTarget() == null) {
            return;
        }
        Integer size = (Integer) sizeField.getSelectedItem();
        ((FigText) getPanelTarget()).setFontSize(size.intValue());
        getPanelTarget().endTrans();
    }

    /**
     * Change style of the text element (bold/italic) according to the selected
     * value.
     */
    protected void setTargetStyle() {
        if (getPanelTarget() == null) {
            return;
        }
        String styleStr = (String) styleField.getSelectedItem();
        if (styleStr == null) {
            return;
        }
        boolean bold = (styleStr.indexOf("Bold") != -1);
        boolean italic = (styleStr.indexOf("Italic") != -1);
        ((FigText) getPanelTarget()).setBold(bold);
        ((FigText) getPanelTarget()).setItalic(italic);
        getPanelTarget().endTrans();
    }

    /**
     * Change the justification of the text according to the selected value.
     */
    protected void setTargetJustification() {
        if (getPanelTarget() == null) {
            return;
        }
        String justStr = (String) justField.getSelectedItem();
        if (justStr == null) {
            return;
        }
        ((FigText) getPanelTarget()).setJustificationByName(justStr);
        getPanelTarget().endTrans();
    }



    /**
     * Change the color of the text element according to the selected value.
     */
    protected void setTargetTextColor() {
        if (getPanelTarget() == null) {
            return;
        }
        Object c = textColorField.getSelectedItem();
        if (c instanceof Color) {
            ((FigText) getPanelTarget()).setTextColor((Color) c);
        }
        getPanelTarget().endTrans();
    }

    /*
     * @see java.awt.event.ItemListener#itemStateChanged(java.awt.event.ItemEvent)
     */
    public void itemStateChanged(ItemEvent e) {
        Object src = e.getSource();
        if (src == fontField) {
            setTargetFont();
        } else if (src == sizeField) {
            setTargetSize();
        } else if (src == styleField) {
            setTargetStyle();
        } else if (src == justField) {
            setTargetJustification();
        } else if (src == textColorField) {
            setTargetTextColor();
        } else {
            super.itemStateChanged(e);
        }
    }


    /**
     * The UID.
     */
    private static final long serialVersionUID = 2019248527481196634L;
} /* end class StylePanelFigText */
