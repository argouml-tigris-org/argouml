/* $Id$
 *****************************************************************************
 * Copyright (c) 2009 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    euluis
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

// Copyright (c) 2008 The Regents of the University of California. All
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

package org.argouml.swingext;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.net.URL;

import javax.swing.Action;
import javax.swing.ButtonModel;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.metal.MetalButtonUI;

/**
 * A button with the looks of an hyperlink.
 * 
 * Based on 
 * <a href="http://www.java2s.com/Code/Java/Swing-Components/LinkButton.htm">
 * JLinkButton</a>.
 * 
 * @since 0.25.6
 */
public class JLinkButton extends JButton {

    /**
     * Link behavior - underline always.
     */
    public static final int ALWAYS_UNDERLINE = 0;

    /**
     * Link behavior - underline only when hovering the mouse over the button.
     */
    public static final int HOVER_UNDERLINE = 1;

    /**
     * Link behavior - don't underline.
     */
    public static final int NEVER_UNDERLINE = 2;

    /**
     * Link behavior - use the system default behavior for hyper-links.
     */
    public static final int SYSTEM_DEFAULT = 3;

    private int linkBehavior;

    private Color linkColor;

    private Color colorPressed;

    private Color visitedLinkColor;

    private Color disabledLinkColor;

    private URL buttonURL;

    private Action defaultAction;

    private boolean isLinkVisited;

    /**
     * No arguments constructor.
     */
    public JLinkButton() {
        this(null, null, null);
    }

    /**
     * Constructor.
     * @param action the Action to perform upon the button being hit.
     */
    public JLinkButton(Action action) {
        this();
        setAction(action);
    }

    /**
     * @param text the button label.
     * @param icon the Icon to be shown in the button.
     * @param url the URL that will be linked from the button.
     */
    public JLinkButton(String text, Icon icon, URL url) {
        super(text, icon);
        linkBehavior = SYSTEM_DEFAULT;
        linkColor = Color.blue;
        colorPressed = Color.red;
        visitedLinkColor = new Color(128, 0, 128);
        if (text == null && url != null) {
            setText(url.toExternalForm());
        }
        setLinkURL(url);
        setCursor(Cursor.getPredefinedCursor(12));
        setBorderPainted(false);
        setContentAreaFilled(false);
        setRolloverEnabled(true);
        addActionListener(defaultAction);
    }

    @Override
    public void updateUI() {
        setUI(BasicLinkButtonUI.createUI(this));
    }

    @Override
    public String getUIClassID() {
        return "LinkButtonUI";
    }

    protected void setupToolTipText() {
        String tip = null;
        if (buttonURL != null) {
            tip = buttonURL.toExternalForm();
        }
        setToolTipText(tip);
    }

    int getLinkBehavior() {
        return linkBehavior;
    }

    Color getLinkColor() {
        return linkColor;
    }

    Color getActiveLinkColor() {
        return colorPressed;
    }

    Color getDisabledLinkColor() {
        return disabledLinkColor;
    }

    Color getVisitedLinkColor() {
        return visitedLinkColor;
    }

    URL getLinkURL() {
        return buttonURL;
    }

    void setLinkURL(URL url) {
        URL urlOld = buttonURL;
        buttonURL = url;
        setupToolTipText();
        firePropertyChange("linkURL", urlOld, url);
        revalidate();
        repaint();
    }

    boolean isLinkVisited() {
        return isLinkVisited;
    }

    protected String paramString() {
        String str;
        if (linkBehavior == ALWAYS_UNDERLINE) {
            str = "ALWAYS_UNDERLINE";
        } else if (linkBehavior == HOVER_UNDERLINE) {
            str = "HOVER_UNDERLINE";
        } else if (linkBehavior == NEVER_UNDERLINE) {
            str = "NEVER_UNDERLINE";
        } else {
            str = "SYSTEM_DEFAULT";
        }
        String colorStr = linkColor == null ? "" : linkColor.toString();
        String colorPressStr = colorPressed == null ? "" : colorPressed
                .toString();
        String disabledLinkColorStr = disabledLinkColor == null ? ""
                : disabledLinkColor.toString();
        String visitedLinkColorStr = visitedLinkColor == null ? ""
                : visitedLinkColor.toString();
        String buttonURLStr = buttonURL == null ? "" : buttonURL.toString();
        String isLinkVisitedStr = isLinkVisited ? "true" : "false";
        return super.paramString() + ",linkBehavior=" + str + ",linkURL="
                + buttonURLStr + ",linkColor=" + colorStr + ",activeLinkColor="
                + colorPressStr + ",disabledLinkColor=" + disabledLinkColorStr
                + ",visitedLinkColor=" + visitedLinkColorStr
                + ",linkvisitedString=" + isLinkVisitedStr;
    }
}

class BasicLinkButtonUI extends MetalButtonUI {
    private static final BasicLinkButtonUI UI = new BasicLinkButtonUI();

    BasicLinkButtonUI() {
    }

    public static ComponentUI createUI(JComponent jcomponent) {
        return UI;
    }

    protected void paintText(Graphics g, JComponent com, Rectangle rect,
            String s) {
        JLinkButton bn = (JLinkButton) com;
        ButtonModel bnModel = bn.getModel();
        bn.getForeground();
        if (bnModel.isEnabled()) {
            if (bnModel.isPressed()) {
                bn.setForeground(bn.getActiveLinkColor());
            } else if (bn.isLinkVisited()) {
                bn.setForeground(bn.getVisitedLinkColor());
            } else {
                bn.setForeground(bn.getLinkColor());
            }
        } else {
            if (bn.getDisabledLinkColor() != null) {
                bn.setForeground(bn.getDisabledLinkColor());
            }
        }
        super.paintText(g, com, rect, s);
        int behaviour = bn.getLinkBehavior();
        boolean drawLine = false;
        if (behaviour == JLinkButton.HOVER_UNDERLINE) {
            if (bnModel.isRollover()) {
                drawLine = true;
            }
        } else if (behaviour == JLinkButton.ALWAYS_UNDERLINE
                || behaviour == JLinkButton.SYSTEM_DEFAULT) {
            drawLine = true;
        }
        if (!drawLine) {
            return;
        }
        FontMetrics fm = g.getFontMetrics();
        int x = rect.x + getTextShiftOffset();
        int y = (rect.y + fm.getAscent() + fm.getDescent() 
                + getTextShiftOffset()) - 1;
        if (bnModel.isEnabled()) {
            g.setColor(bn.getForeground());
            g.drawLine(x, y, (x + rect.width) - 1, y);
        } else {
            g.setColor(bn.getBackground().brighter());
            g.drawLine(x, y, (x + rect.width) - 1, y);
        }
    }
}
