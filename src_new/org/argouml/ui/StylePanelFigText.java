// $Id$
// Copyright (c) 1996-2005 The Regents of the University of California. All
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
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
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
	new Integer(8), new Integer(9),
	new Integer(10), new Integer(12), new Integer(16), new Integer(18),
	new Integer(24), new Integer(36), new Integer(48), new Integer(72),
	new Integer(96),
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
     *
     */
    public StylePanelFigText() {
        super();
        GridBagLayout gb = (GridBagLayout) getLayout();
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.ipadx = 0;
        c.ipady = 0;

        fontField.addItemListener(this);
        sizeField.addItemListener(this);
        styleField.addItemListener(this);
        justField.addItemListener(this);
        textColorField.addItemListener(this);
      
        textColorField.setRenderer(new ColorRenderer());
      
        c.weightx = 0.0;
        c.gridx = 0;
        c.gridwidth = 1;
        c.gridy = 5;
        gb.setConstraints(textColorLabel, c);
        add(textColorLabel);
       
        c.gridx = 3;
        c.gridwidth = 1;
        c.gridy = 1;
        gb.setConstraints(fontLabel, c);
        add(fontLabel);
        c.gridy = 2;
        gb.setConstraints(sizeLabel, c);
        add(sizeLabel);
        c.gridy = 3;
        gb.setConstraints(styleLabel, c);
        add(styleLabel);
        // row 4 left blank for some reason...
        c.gridy = 5;
        gb.setConstraints(justLabel, c);
        add(justLabel);

        c.weightx = 1.0;
        c.gridx = 1;
        c.gridy = 5;
        gb.setConstraints(textColorField, c);
        add(textColorField);
        c.gridy = 6;
        c.gridx = 4;
        c.gridy = 1;
        gb.setConstraints(fontField, c);
        add(fontField);
        c.gridy = 2;
        gb.setConstraints(sizeField, c);
        add(sizeField);
        c.gridy = 3;
        gb.setConstraints(styleField, c);
        add(styleField);
        c.gridy = 5;
        gb.setConstraints(justField, c);
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
     * refresh the text element with all selected values.
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
        sizeField.setSelectedItem(new Integer(size));
        if (ft.getBold()) styleName = STYLES[1];
        if (ft.getItalic()) styleName = STYLES[2];
        if (ft.getBold() && ft.getItalic()) styleName = STYLES[3];
        styleField.setSelectedItem(styleName);

        String justName = JUSTIFIES[0];
        int justCode = ft.getJustification();
        if (justCode >= 0 && justCode <= JUSTIFIES.length)
                justName = JUSTIFIES[justCode];
        justField.setSelectedItem(justName);

        Color c = ft.getTextColor();
        textColorField.setSelectedItem(c);
        if (c != null && !textColorField.getSelectedItem().equals(c)) {
            textColorField.insertItemAt(c, textColorField.getItemCount() - 1);
            textColorField.setSelectedItem(c);
        }

        c = ft.getFillColor();
        getFillField().setSelectedItem(c);
        if (c != null && !getFillField().getSelectedItem().equals(c)) {
            getFillField().insertItemAt(c, getFillField().getItemCount() - 1);
            getFillField().setSelectedItem(c);
        }
    }

    /**
     * set the font of the text element to the selected value.
     *
     */
    protected void setTargetFont() {
        if (getPanelTarget() == null) return;
        String fontStr = (String) fontField.getSelectedItem();
        if (fontStr.length() == 0) return;
        ((FigText) getPanelTarget()).setFontFamily(fontStr);
        getPanelTarget().endTrans();
    }

    /**
     * change font size of the text element according to the selected value.
     *
     */
    protected void setTargetSize() {
        if (getPanelTarget() == null) return;
        Integer size = (Integer) sizeField.getSelectedItem();
        ((FigText) getPanelTarget()).setFontSize(size.intValue());
        getPanelTarget().endTrans();
    }

    /**
     * change style of the text element (bold/italic) according to the selected
     * value.
     *
     */
    protected void setTargetStyle() {
        if (getPanelTarget() == null) return;
        String styleStr = (String) styleField.getSelectedItem();
        if (styleStr == null) return;
        boolean bold = (styleStr.indexOf("Bold") != -1);
        boolean italic = (styleStr.indexOf("Italic") != -1);
        ((FigText) getPanelTarget()).setBold(bold);
        ((FigText) getPanelTarget()).setItalic(italic);
        getPanelTarget().endTrans();
    }

    /**
     * change the justification of the text according to the selected value.
     *
     */
    protected void setTargetJustification() {
        if (getPanelTarget() == null) return;
        String justStr = (String) justField.getSelectedItem();
        if (justStr == null) return;
        ((FigText) getPanelTarget()).setJustificationByName(justStr);
        getPanelTarget().endTrans();
    }



    /**
     * change the color of the text element according to the selected value.
     *
     */
    protected void setTargetTextColor() {
        if (getPanelTarget() == null) return;
        Object c = textColorField.getSelectedItem();
        if (c instanceof Color)
            ((FigText) getPanelTarget()).setTextColor((Color) c);
        getPanelTarget().endTrans();
    }

    /**
     * @see java.awt.event.ItemListener#itemStateChanged(java.awt.event.ItemEvent)
     */
    public void itemStateChanged(ItemEvent e) {
        Object src = e.getSource();
        if (src == fontField)
            setTargetFont();
        else if (src == sizeField)
            setTargetSize();
        else if (src == styleField)
            setTargetStyle();
        else if (src == justField)
            setTargetJustification();
        else if (src == textColorField)
            setTargetTextColor();
        else
            super.itemStateChanged(e);
    }

} /* end class StylePanelFigText */
