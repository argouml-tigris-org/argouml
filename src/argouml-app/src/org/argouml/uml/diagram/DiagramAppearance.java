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

// Copyright (c) 2007-2009 The Regents of the University of California. All
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
package org.argouml.uml.diagram;

import java.awt.Font;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.UIManager;

import org.argouml.application.events.ArgoDiagramAppearanceEvent;
import org.argouml.application.events.ArgoEventPump;
import org.argouml.application.events.ArgoEventTypes;
import org.argouml.configuration.Configuration;
import org.argouml.configuration.ConfigurationKey;

/**
 * Provides centralized methods dealing with diagram appearance.
 * <p>
 * These settings do not apply to the appearance of the ArgoUML application! <p>
 *
 * In the MVC pattern, this is part of the Model.
 *
 * @stereotype singleton
 * @author Aleksandar
 */
public final class DiagramAppearance implements PropertyChangeListener {

    /**
     * Define a static log4j category variable for ArgoUML diagram appearance.
     */
    private static final Logger LOG =
        Logger.getLogger(DiagramAppearance.class.getName());

    /**
     * The configuration key for the font name.
     */
    public static final ConfigurationKey KEY_FONT_NAME = Configuration.makeKey(
            "diagramappearance", "fontname");

    /**
     * The configuration key for the font size.
     */
    public static final ConfigurationKey KEY_FONT_SIZE = Configuration.makeKey(
            "diagramappearance", "fontsize");

    /**
     * The configuration key that indicates whether to show bold names.
     */
    public static final ConfigurationKey KEY_SHOW_BOLD_NAMES =
        Configuration.makeKey("notation", "show", "bold", "names");

    /**
     * Default value for the shadow size of classes, interfaces etc.
     */
    public static final ConfigurationKey KEY_DEFAULT_SHADOW_WIDTH =
        Configuration.makeKey("notation", "default", "shadow-width");

    /**
     * Indicates if the user wants to see the arrows when both
     * association ends in an association are navigable.
     */
    public static final ConfigurationKey KEY_HIDE_BIDIRECTIONAL_ARROWS =
        Configuration.makeKey("notation", "hide", "bidirectional-arrows");

    /**
     * The instance.
     */
    private static final DiagramAppearance SINGLETON = new DiagramAppearance();

    /**
     * Used for FigNodeModelElement#setStereotypeView().
     * Represents the default view for
     * stereotypes applied to this node.
     *
     * @see org.argouml.uml.diagram.ui.ActionStereotypeViewTextual
     */
    public static final int STEREOTYPE_VIEW_TEXTUAL = 0;

    /**
     * Used for FigNodeModelElement#setStereotypeView().
     * Represents the view for stereotypes where the
     * default representation is replaced by a provided
     * icon.
     *
     * @see org.argouml.uml.diagram.ui.ActionStereotypeViewBigIcon
     */
    public static final int STEREOTYPE_VIEW_BIG_ICON = 1;

    /**
     * Used for FigNodeModelElement#setStereotypeView().
     * Represents the view for stereotypes where the
     * default view is adorned with a small version of the
     * provided icon.
     *
     * @see org.argouml.uml.diagram.ui.ActionStereotypeViewSmallIcon
     */
    public static final int STEREOTYPE_VIEW_SMALL_ICON = 2;

    /**
     * The constructor.
     * TODO: Why does this method not handle all settings
     * (KEY_DEFAULT_SHADOW_WIDTH is missing)?
     */
    private DiagramAppearance() {
        Configuration.addListener(DiagramAppearance.KEY_FONT_NAME, this);
        Configuration.addListener(DiagramAppearance.KEY_FONT_SIZE, this);
        Configuration.addListener(KEY_SHOW_BOLD_NAMES, this);
        Configuration.addListener(KEY_HIDE_BIDIRECTIONAL_ARROWS, this);
    }

    /**
     * @return the singleton
     */
    public static DiagramAppearance getInstance() {
        return SINGLETON;
    }

    /*
     * Called after the diagram font gets changed. <p>
     *
     * TODO: Do we need to do anything here?
     *
     * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
     */
    public void propertyChange(PropertyChangeEvent pce) {
        LOG.log(Level.INFO,
                "Diagram appearance change:{0} to {1}",
                new Object[]{pce.getOldValue(), pce.getNewValue()});

        ArgoEventPump.fireEvent(
                new ArgoDiagramAppearanceEvent(ArgoEventTypes.DIAGRAM_FONT_CHANGED, pce));
    }

    /**
     * Gets font name. If it doesn't exist in configuration it creates new
     * entries in configuration for appearance.
     *
     * TODO: Why create in a getter?
     *
     * @return the name of the configured font
     */
    public String getConfiguredFontName() {
        String fontName = Configuration
                .getString(DiagramAppearance.KEY_FONT_NAME);
        if (fontName.equals("")) {
            Font f = getStandardFont();
            fontName = f.getName();

            Configuration.setString(DiagramAppearance.KEY_FONT_NAME, f
                    .getName());
            Configuration.setInteger(DiagramAppearance.KEY_FONT_SIZE, f
                    .getSize());
        }

        return fontName;
    }

    /**
     * This is the same function as
     * LookAndFeelMgr.getInstance().getStandardFont();
     * but used for a totally different puropose: here it determines
     * a default font when none is set. In the LookAndFeelMgr it
     * determines the looks of the UI.
     *
     * @return the standard textfield font
     */
    private Font getStandardFont() {
        Font font = UIManager.getDefaults().getFont("TextField.font");
        if (font == null) {
            font = (new javax.swing.JTextField()).getFont();
        }
        return font;
    }
}
