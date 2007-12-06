// $Id$
// Copyright (c) 2003-2006 The Regents of the University of California. All
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
import java.awt.Rectangle;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.text.Document;

import org.tigris.gef.presentation.Fig;
import org.tigris.gef.ui.ColorRenderer;

import org.argouml.i18n.Translator;
import org.argouml.swingext.SpacerPanel;
import org.argouml.uml.diagram.ui.FigEdgeModelElement;
import org.argouml.uml.diagram.ui.FigNodeModelElement;

/**
 * The basic stylepanel which provides the boundaries box,
 * line and fill color information and the stereotype view combo box.
 *
 */
public class StylePanelFig
    extends StylePanel
    implements ItemListener,
        FocusListener, KeyListener {
    private static final String CUSTOM_ITEM =
        Translator.localize("label.stylepane.custom") + "...";

    private JLabel bboxLabel =
        new JLabel(Translator.localize("label.stylepane.bounds") + ": ");

    private JTextField bboxField = new JTextField();

    private JLabel fillLabel =
        new JLabel(Translator.localize("label.stylepane.fill") + ": ");

    private JComboBox fillField = new JComboBox();

    private JLabel lineLabel =
        new JLabel(Translator.localize("label.stylepane.line") + ": ");

    private JComboBox lineField = new JComboBox();

    private JLabel stereoLabel =
        new JLabel(Translator.localize("menu.popup.stereotype-view") + ": ");

    private JComboBox stereoField = new JComboBox();
    
    private SpacerPanel spacer = new SpacerPanel();

    private SpacerPanel spacer2 = new SpacerPanel();

    private SpacerPanel spacer3 = new SpacerPanel();

    /**
     * The constructor of the style panel of a Fig.
     * This constructor does not create any contents of the panel.
     *
     * @param title the title string
     */
    public StylePanelFig(String title) {
        super(title);
    }

    /**
     * The constructor.
     *
     */
    public StylePanelFig() {
        super("Fig Appearance");
        initChoices();

        Document bboxDoc = bboxField.getDocument();
        bboxDoc.addDocumentListener(this);
        bboxField.addKeyListener(this);
        bboxField.addFocusListener(this);
        fillField.addItemListener(this);
        lineField.addItemListener(this);
        stereoField.addItemListener(this);

        fillField.setRenderer(new ColorRenderer());
        lineField.setRenderer(new ColorRenderer());

        bboxLabel.setLabelFor(bboxField);
        add(bboxLabel);
        add(bboxField);

        fillLabel.setLabelFor(fillField);
        add(fillLabel);
        add(fillField);

        lineLabel.setLabelFor(lineField);
        add(lineLabel);
        add(lineField);

        stereoLabel.setLabelFor(stereoField);
        add(stereoLabel);
        add(stereoField);
    }

    /**
     * Fill in the user-choices.
     */
    protected void initChoices() {
        fillField.addItem(Translator.localize("label.stylepane.no-fill"));
        fillField.addItem(Color.black);
        fillField.addItem(Color.white);
        fillField.addItem(Color.gray);
        fillField.addItem(Color.lightGray);
        fillField.addItem(Color.darkGray);
        fillField.addItem(new Color(255, 255, 200));
        fillField.addItem(new Color(255, 200, 255));
        fillField.addItem(new Color(200, 255, 255));
        fillField.addItem(new Color(200, 200, 255));
        fillField.addItem(new Color(200, 255, 200));
        fillField.addItem(new Color(255, 200, 200));
        fillField.addItem(new Color(200, 200, 200));
        fillField.addItem(Color.red);
        fillField.addItem(Color.blue);
        fillField.addItem(Color.cyan);
        fillField.addItem(Color.yellow);
        fillField.addItem(Color.magenta);
        fillField.addItem(Color.green);
        fillField.addItem(Color.orange);
        fillField.addItem(Color.pink);
        fillField.addItem(CUSTOM_ITEM);

        lineField.addItem(Translator.localize("label.stylepane.no-line"));
        lineField.addItem(Color.black);
        lineField.addItem(Color.white);
        lineField.addItem(Color.gray);
        lineField.addItem(Color.lightGray);
        lineField.addItem(Color.darkGray);
        lineField.addItem(new Color(60, 60, 200));
        lineField.addItem(new Color(60, 200, 60));
        lineField.addItem(new Color(200, 60, 60));
        lineField.addItem(Color.red);
        lineField.addItem(Color.blue);
        lineField.addItem(Color.cyan);
        lineField.addItem(Color.yellow);
        lineField.addItem(Color.magenta);
        lineField.addItem(Color.green);
        lineField.addItem(Color.orange);
        lineField.addItem(Color.pink);
        lineField.addItem(CUSTOM_ITEM);
        
        DefaultComboBoxModel model = new DefaultComboBoxModel();
        stereoField.setModel(model);
        
        model.addElement(Translator
                .localize("menu.popup.stereotype-view.textual"));
        model.addElement(Translator
                .localize("menu.popup.stereotype-view.big-icon"));
        model.addElement(Translator
                .localize("menu.popup.stereotype-view.small-icon"));
    }

    /**
     * set whether this Fig has a editable boundingbox. This is done normally in
     * <code>refresh()</code>, e.g. for FigEdgeModelElements where it does
     * not make sense to edit the bounding box.
     *
     * @param value
     *            the boolean value of the bounding box property
     */

    protected void hasEditableBoundingBox(boolean value) {
        bboxField.setEnabled(value);
        bboxLabel.setEnabled(value);
    }

    /**
     * Handle a refresh of the style panel after the fig has moved.<p>
     *
     * <em>Warning</em>. There is a circular trap here. Editing the
     * boundary box will also trigger a refresh, and so we reset the
     * boundary box, which causes funny behaviour (the cursor keeps
     * jumping to the end of the text).
     *
     * The solution is to not reset the boundary box field if the boundaries
     * have not changed.<p>
     */
    public void refresh() {
    	Fig target = getPanelTarget();
    	 //TODO: How about FigAssociationClass?
        if (target instanceof FigEdgeModelElement) {
            hasEditableBoundingBox(false);
        } else {
            hasEditableBoundingBox(true);
        }
        if (target == null) {
            return;
        }

        // The boundary box as held in the target fig, and as listed in
        // the
        // boundary box style field (null if we don't have anything
        // valid)

        Rectangle figBounds = target.getBounds();
        Rectangle styleBounds = parseBBox();

        // Only reset the text if the two are not the same (i.e the fig
        // has
        // moved, rather than we've just edited the text, when
        // setTargetBBox()
        // will have made them the same). Note that styleBounds could
        // be null,
        // so we do the test this way round.

        if (!(figBounds.equals(styleBounds))) {
            bboxField.setText(figBounds.x + "," + figBounds.y + ","
                    + figBounds.width + "," + figBounds.height);
        }

        // Change the fill colour

        if (target.isFilled()) {
            Color c = target.getFillColor();
            fillField.setSelectedItem(c);
            if (c != null && !fillField.getSelectedItem().equals(c)) {
                fillField.insertItemAt(c, fillField.getItemCount() - 1);
                fillField.setSelectedItem(c);
            }
        } else {
            fillField.setSelectedIndex(0);
        }

        // Change the line colour

        if (target.getLineWidth() > 0) {
            Color c = target.getLineColor();
            lineField.setSelectedItem(c);
            if (c != null && !lineField.getSelectedItem().equals(c)) {
                lineField.insertItemAt(c, lineField.getItemCount() - 1);
                lineField.setSelectedItem(c);
            }
        } else {
            lineField.setSelectedIndex(0);
        }

        stereoField.setEnabled(target instanceof FigNodeModelElement);
        stereoLabel.setEnabled(target instanceof FigNodeModelElement);
        
        if (target instanceof FigNodeModelElement) {
            FigNodeModelElement fig = (FigNodeModelElement) target;
            
            switch (fig.getStereotypeView()) {
            case FigNodeModelElement.STEREOTYPE_VIEW_TEXTUAL:
                stereoField.setSelectedIndex(0);                
                break;
            case FigNodeModelElement.STEREOTYPE_VIEW_BIG_ICON:
                stereoField.setSelectedIndex(1);                
                break;
            case FigNodeModelElement.STEREOTYPE_VIEW_SMALL_ICON:
                stereoField.setSelectedIndex(2);                
                break;
            }
        }
    }

    /**
     * Change the bounds of the target fig. Called whenever the bounds box is
     * edited. <p>
     *
     * Format of the bounds is four integers representing x, y, width and height
     * separated by spaces or commas. An empty field is treated as no change and
     * leading and trailing spaces are ignored. <p>
     *
     * <em>Note</em>. There is a note in the old code that more work might be
     * needed, because this could change the graph model. I don't see how that
     * could ever be.
     */
    protected void setTargetBBox() {
    	Fig target = getPanelTarget();
        // Can't do anything if we don't have a fig.
        if (target == null) {
            return;
        }
        // Parse the boundary box text. Null is
        // returned if it is empty or
        // invalid, which causes no change. Otherwise we tell
        // GEF we are making
        // a change, make the change and tell GEF we've
        // finished.
        Rectangle bounds = parseBBox();
        if (bounds == null) {
            return;
        }

        if (!target.getBounds().equals(bounds)) {
            target.setBounds(bounds.x, bounds.y, bounds.width,
                    bounds.height);
            target.endTrans();
        }
    }

    /**
     * Parse the boundary box string and return the rectangle it
     * represents.<p>
     *
     * The syntax are four integers separated by spaces or commas. We
     * ignore leading and trailing blanks.<p>
     *
     * If we have the empty string we return <code>null</code>.<p>
     *
     * If we fail to parse, then we return <code>null</code> and print
     * out a rude message.<p>
     *
     * @return The size of the box, or <code>null</code> if the bounds string
     *         is empty or invalid.
     */
    protected Rectangle parseBBox() {
    	Fig target = getPanelTarget();
        // Get the text in the field, and don't do anything if the
        // field is
        // empty.
        String bboxStr = bboxField.getText().trim();
        if (bboxStr.length() == 0) {
            return null;
        } // Parse the string as if
        // possible
        Rectangle res = new Rectangle();
        java.util.StringTokenizer st =
            new java.util.StringTokenizer(bboxStr, ", ");
        try {
            boolean changed = false;
            if (!st.hasMoreTokens()) {
                return target.getBounds();
            }
            res.x = Integer.parseInt(st.nextToken());
            if (!st.hasMoreTokens()) {
                res.y = target.getBounds().y;
                res.width = target.getBounds().width;
                res.height = target.getBounds().height;
                return res;
            }
            res.y = Integer.parseInt(st.nextToken());
            if (!st.hasMoreTokens()) {
                res.width = target.getBounds().width;
                res.height = target.getBounds().height;
                return res;
            }
            res.width = Integer.parseInt(st.nextToken());
            // TODO: What does the magic number 6000 represent here?
            // Put it in an explanatory literal! - tfm - 20071205
            if ((res.width + res.x) > 6000) {
                res.width = 6000 - res.x;
                changed = true;
            }
            if (!st.hasMoreTokens()) {
                res.width = target.getBounds().width;
                return res;
            }
            res.height = Integer.parseInt(st.nextToken());
            // TODO: What does the magic number 6000 represent here?
            // Put it in an explanatory literal! - tfm - 20071205
            if ((res.height + res.y) > 6000) {
                res.height = 6000 - res.y;
                changed = true;
            }
            if (res.x < 0 || res.y < 0 || res.width < 0 || res.height < 0) {
                // TODO: This exception will be thrown during autoscrolling
                // when the edge of the canvas is reached causing either
                // the width or height to be "adjusted" to a negative value
                throw new IllegalArgumentException();
            }
            if (changed) {
                StringBuffer sb = new StringBuffer();
                sb.append(Integer.toString(res.x));
                sb.append(",");
                sb.append(Integer.toString(res.y));
                sb.append(",");
                sb.append(Integer.toString(res.width));
                sb.append(",");
                sb.append(Integer.toString(res.height));
                bboxField.setText(sb.toString());
            }
        } catch (NumberFormatException ex) {
            bboxField.setBackground(Color.RED);
            return null;
        }
        catch (IllegalArgumentException iae) {
            bboxField.setBackground(Color.RED);
            return null;
        }
        bboxField.setBackground(null);
        return res;
    }

    /**
     * Prompts the user for a new custom color and adds that color to the combo
     * box.
     *
     * @param field the combobox to enter a new color for
     * @param title the title for the dialog box
     * @param targetColor the initial Color set when the color-chooser is shown
     */
    protected void handleCustomColor(JComboBox field, String title,
            Color targetColor) {
        Color newColor =
            JColorChooser.showDialog(ArgoFrame.getInstance(),
                title, targetColor);
        if (newColor != null) {
            field.insertItemAt(newColor, field.getItemCount() - 1);
            field.setSelectedItem(newColor);
        } else if (getPanelTarget() != null) {
            field.setSelectedItem(targetColor);
        }
    }

    /**
     * Change the fill.
     */
    public void setTargetFill() {
    	Fig target = getPanelTarget();
        Object c = fillField.getSelectedItem();
        if (target == null || c == null) {
            return;
        }
        if (c instanceof Color) {
            target.setFillColor((Color) c);
        }
        target.setFilled(c instanceof Color);
        target.endTrans();
    }

    /**
     * Change the line.
     */
    public void setTargetLine() {
    	Fig target = getPanelTarget();
        Object c = lineField.getSelectedItem();
        if (target == null || c == null) {
            return;
        }
        if (c instanceof Color) {
            target.setLineColor((Color) c);
        }
        target.setLineWidth((c instanceof Color) ? 1 : 0);
        target.endTrans();
    }

    /*
     * @see java.awt.event.ItemListener#itemStateChanged(java.awt.event.ItemEvent)
     */
    public void itemStateChanged(ItemEvent e) {
        Object src = e.getSource();
        Fig target = getPanelTarget();
        if (e.getStateChange() == ItemEvent.SELECTED
                && target != null) {
            if (src == fillField) {
                if (e.getItem() == CUSTOM_ITEM) {
                    handleCustomColor(fillField, "Custom Fill Color",
                            target.getFillColor());
                }
                setTargetFill();
            } else if (src == lineField) {
                if (e.getItem() == CUSTOM_ITEM) {
                    handleCustomColor(lineField, "Custom Line Color",
                            target.getLineColor());
                }
                setTargetLine();
            } else if (src == stereoField) {
                if (target instanceof FigNodeModelElement) {
                    Object item = e.getItem();
                    DefaultComboBoxModel model = 
                        (DefaultComboBoxModel) stereoField.getModel();
                    int idx = model.getIndexOf(item);
                    FigNodeModelElement fig = (FigNodeModelElement) target;

                    switch (idx) {
                    case 0:
                        fig.setStereotypeView(
                                FigNodeModelElement.STEREOTYPE_VIEW_TEXTUAL);
                        break;
                    case 1:
                        fig.setStereotypeView(
                                FigNodeModelElement.STEREOTYPE_VIEW_BIG_ICON);
                        break;
                    case 2:
                        fig.setStereotypeView(
                                FigNodeModelElement.STEREOTYPE_VIEW_SMALL_ICON);
                        break;
                    }
                }
            }
        }
    }

    /*
     * @see java.awt.event.FocusListener#focusGained(java.awt.event.FocusEvent)
     */
    public void focusGained(FocusEvent e) {
    }

    /**
     * Makes sure that the fig is updated when the bboxField loses focus.
     *
     * {@inheritDoc}
     */
    public void focusLost(FocusEvent e) {
        if (e.getSource() == bboxField) {
            setTargetBBox();
        }
    }

    /*
     * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
     */
    public void keyPressed(KeyEvent e) {
        // Empty implementation - we only care about keyTyped
    }

    /*
     * @see java.awt.event.KeyListener#keyReleased(java.awt.event.KeyEvent)
     */
    public void keyReleased(KeyEvent e) {
        // Empty implementation - we only care about keyTyped
    }

    /*
     * Tests if enter is pressed in the _bbodField so we need to set the target
     * bounds.
     *
     * @see java.awt.event.KeyListener#keyTyped(java.awt.event.KeyEvent)
     */
    public void keyTyped(KeyEvent e) {
        if (e.getSource().equals(bboxField) && e.getKeyChar() == '\n') {
            setTargetBBox();
        }
    }

    /**
     * @return Returns the _bboxLabel.
     */
    protected JLabel getBBoxLabel() {
        return bboxLabel;
    }

    /**
     * @return Returns the _bboxField.
     */
    protected JTextField getBBoxField() {
        return bboxField;
    }

    /**
     * @return Returns the _fillLabel.
     */
    protected JLabel getFillLabel() {
        return fillLabel;
    }

    /**
     * @return Returns the _fillField.
     */
    protected JComboBox getFillField() {
        return fillField;
    }

    /**
     * @return Returns the _lineLabel.
     */
    protected JLabel getLineLabel() {
        return lineLabel;
    }

    /**
     * @return Returns the _lineField.
     */
    protected JComboBox getLineField() {
        return lineField;
    }

    /**
     * @return Returns the _spacer.
     */
    protected SpacerPanel getSpacer() {
        return spacer;
    }

    /**
     * @return Returns the _spacer2.
     */
    protected SpacerPanel getSpacer2() {
        return spacer2;
    }

    /**
     * @return Returns the _spacer3.
     */
    protected SpacerPanel getSpacer3() {
        return spacer3;
    }

    /**
     * The UID.
     */
    private static final long serialVersionUID = -6232843473753751128L;
}
