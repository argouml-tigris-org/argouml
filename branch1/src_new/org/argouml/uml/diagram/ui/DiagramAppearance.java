// $Id$
// Copyright (c) 2007 The Regents of the University of California. All
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
package org.argouml.uml.diagram.ui;

import java.awt.Font;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.apache.log4j.Logger;
import org.argouml.configuration.Configuration;
import org.argouml.configuration.ConfigurationKey;
import org.argouml.ui.LookAndFeelMgr;

/**
 * Provides centralized methods dealing with diagram appearance.
 *
 * @stereotype singleton
 * @author Aleksandar
 */
public final class DiagramAppearance implements PropertyChangeListener {

    /**
     * Define a static log4j category variable for ArgoUML diagram appearance.
     */
    private static final Logger LOG = Logger.getLogger(DiagramAppearance.class);

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
     * The instance.
     */
    private static final DiagramAppearance SINGLETON = new DiagramAppearance();

    /**
     * The constructor.
     */
    private DiagramAppearance() {
        Configuration.addListener(DiagramAppearance.KEY_FONT_NAME, this);
        Configuration.addListener(DiagramAppearance.KEY_FONT_SIZE, this);
//        Configuration.addListener(DiagramAppearance.KEY_FONT_BOLD, this);
//        Configuration.addListener(DiagramAppearance.KEY_FONT_ITALLIC, this);
    }

    /**
     * @return the singleton
     */
    public static DiagramAppearance getInstance() {
        return SINGLETON;
    }

    /*
     * Called after the notation default property gets changed.
     *
     * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
     */
    public void propertyChange(PropertyChangeEvent pce) {
        LOG.info("Diagram appearance change:" + pce.getOldValue() + " to "
                + pce.getNewValue());
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
            Font f = LookAndFeelMgr.getInstance().getStandardFont();
            fontName = f.getName();

            Configuration.setString(DiagramAppearance.KEY_FONT_NAME, f
                    .getName());
            Configuration.setInteger(DiagramAppearance.KEY_FONT_SIZE, f
                    .getSize());
        }

        return fontName;
    }
}
