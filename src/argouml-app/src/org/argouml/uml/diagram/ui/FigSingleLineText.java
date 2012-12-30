/* $Id$
 *****************************************************************************
 * Copyright (c) 2009-2012 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    mvw
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

// Copyright (c) 1996-2009 The Regents of the University of California. All
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

package org.argouml.uml.diagram.ui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.SwingUtilities;

import org.argouml.model.AttributeChangeEvent;
import org.argouml.model.InvalidElementException;
import org.argouml.model.Model;
import org.argouml.model.UmlChangeEvent;
import org.argouml.uml.diagram.DiagramSettings;
import org.tigris.gef.presentation.FigText;

/**
 * A SingleLine FigText to provide consistency across Figs displaying single
 * lines of text.<ul>
 * <li>The display area is transparent
 * <li>Text is center justified
 * <li>There is no line border
 * <li>There is space below the line for a "Clarifier",
 * i.e. a red squiggly line.
 * </ul><p>
 *
 * Some of these have an UML object as owner, others do not.
 *
 * @author Bob Tarling
 */
public class FigSingleLineText extends ArgoFigText  {

    private static final Logger LOG =
        Logger.getLogger(FigSingleLineText.class.getName());

    /**
     * The properties of 'owner' that this is interested in
     */
    private String[] properties;

    private void initialize() {
        setFillColor(FILL_COLOR); // in case someone turns it on
        super.setFilled(false);
        setTabAction(FigText.END_EDITING);
        setReturnAction(FigText.END_EDITING);
        setLineWidth(0);
        setTextColor(TEXT_COLOR);
    }

    public void setFilled(boolean filled) {
        // Do not allow fill to change. We should see through
        // the text to the color of the main FIg background.
    }

    /**
     * Construct text fig
     *
     * @param owner owning UML element
     * @param bounds position and size
     * @param settings rendering settings
     * @param expandOnly true if the Fig should only expand and never contract
     */
    public FigSingleLineText(Object owner, Rectangle bounds,
            DiagramSettings settings, boolean expandOnly) {

        this(owner, bounds, settings, expandOnly, (String[]) null);
    }

    /**
     * Construct text fig
     *
     * @param owner owning UML element
     * @param bounds position and size
     * @param settings rendering settings
     * @param expandOnly true if the Fig should only expand and never contract
     * @param property name of property to listen to
     */
    public FigSingleLineText(Object owner, Rectangle bounds,
            DiagramSettings settings, boolean expandOnly, String property) {

        this(owner, bounds, settings, expandOnly, new String[] {property});
    }

    /**
     * Constructor for text fig without owner.
     * Using this constructor shall mean
     * that this fig will never have an owner.
     *
     * @param bounds position and size
     * @param settings rendering settings
     * @param expandOnly true if the Fig should only expand and never contract
     */
    public FigSingleLineText(Rectangle bounds,
            DiagramSettings settings, boolean expandOnly) {

        this(null, bounds, settings, expandOnly);
    }

    /**
     * Construct text fig
     *
     * @param owner owning UML element
     * @param bounds position and size
     * @param settings rendering settings
     * @param expandOnly true if the Fig should only expand and never contract
     * @param allProperties names of properties to listen to
     */
    public FigSingleLineText(Object owner, Rectangle bounds,
            DiagramSettings settings, boolean expandOnly,
            String[] allProperties) {
        super(owner, bounds, settings, expandOnly);
        initialize();
        this.properties = allProperties;
        addModelListener();
    }

    /**
     * TODO: This function attempts to optimize the more generic
     * code in the parent, which also works correctly in this case.
     * Is this a good idea?
     */
    @Override
    public Dimension getMinimumSize() {
        Dimension d = new Dimension();

        Font font = getFont();

        if (font == null) {
            return d;
        }
        int maxW = 0;
        int maxH = 0;
        if (getFontMetrics() == null) {
            maxH = font.getSize();
        } else {
            maxH = getFontMetrics().getHeight();
            maxW = getFontMetrics().stringWidth(getText());
        }

        /* Now force minimum dimensions for the text: */
        maxW = Math.max(maxW, MIN_TEXT_WIDTH);

        /* Now add the areas around the text to return the complete size: */
        int overallH = maxH + getTopMargin() + getBotMargin()
            + 2 * getLineWidth();
        int overallW = maxW + getLeftMargin() + getRightMargin()
            + 2 * getLineWidth();
        d.width = overallW;
        d.height = overallH;
        return d;
    }

    @Override
    protected boolean isStartEditingKey(KeyEvent ke) {
        if ((ke.getModifiers()
	     & (KeyEvent.META_MASK | KeyEvent.ALT_MASK)) == 0) {
            return super.isStartEditingKey(ke);
        } else {
            return false;
        }
    }

    private void addModelListener() {
        if (properties != null && getOwner() != null) {
            Model.getPump().addModelEventListener(this, getOwner(), properties);
        }
    }

    @Override
    public void removeFromDiagram() {
        if (getOwner() != null && properties != null) {
            Model.getPump().removeModelEventListener(
                    this,
                    getOwner(),
                    properties);
        }
        super.removeFromDiagram();
    }

    @Override
    public void propertyChange(PropertyChangeEvent pce) {
        if ("remove".equals(pce.getPropertyName())
                && (pce.getSource() == getOwner())) {
            deleteFromModel();
        }

        if (pce instanceof UmlChangeEvent) {
            final UmlChangeEvent event = (UmlChangeEvent) pce;
            Runnable doWorkRunnable = new Runnable() {
                public void run() {
                    try {
                        updateLayout(event);
                    } catch (InvalidElementException e) {
                        LOG.log(Level.FINE, "event = {0}", event);
                        LOG.log(Level.FINE, "updateLayout method accessed deleted element ", e);
                    }
                }
            };
            SwingUtilities.invokeLater(doWorkRunnable);
        }
    }

    /**
     * This is a template method called by the ArgoUML framework as the result
     * of a change to a model element. Do not call this method directly
     * yourself.
     * <p>Override this in any subclasses in order to redisplay the Fig
     * due to change of any model element that this Fig is listening to.</p>
     * <p>This method is guaranteed by the framework to be running on the
     * Swing/AWT thread.</p>
     *
     * @param event the UmlChangeEvent that caused the change
     */
    protected void updateLayout(UmlChangeEvent event) {
        assert event != null;
        if (getOwner() == event.getSource()
                && properties != null
                && Arrays.asList(properties).contains(event.getPropertyName())
                && event instanceof AttributeChangeEvent) {
            /* TODO: Why does it fail for changing
             * the name of an associationend?
             *  Why should it pass? */
            //assert Arrays.asList(properties).contains(
            //    event.getPropertyName())
            //  : event.getPropertyName();
            // TODO: Do we really always need to do this or only if
            // notationProvider is null?
            setText();
        }
    }

    /**
     * This function without parameter shall
     * determine the text of the Fig taking values from the owner,
     * and then call {@link #setText(String)}.
     * To be implemented as required by sub classes.
     */
    protected void setText() {
    }

    public void renderingChanged() {
        super.renderingChanged();
        /* This is needed for e.g.
         * guillemet notation change on a class name,
         * see issue 5419. */
        setText();
    }
}
