// $Id: ActionSetMode.java 12827 2007-06-13 18:56:14Z mvw $
// Copyright (c) 2003-2007 The Regents of the University of California. All
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

import java.util.Hashtable;
import java.util.Properties;

import javax.swing.Action;
import javax.swing.ImageIcon;

import org.apache.log4j.Logger;
import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.i18n.Translator;
import org.tigris.gef.base.SetModeAction;

/**
 * Extends GEF ActionSetMode to add additional metadata such as tooltips.
 *
 * @author Jeremy Jones
 */
public class ActionSetMode extends SetModeAction {
    
    private static final Logger LOG = Logger.getLogger(ActionSetMode.class);

    /**
     * The constructor.
     *
     * @param args arguments
     */
    public ActionSetMode(Properties args) {
        super(args);
    }

    /**
     * The constructor.
     *
     * @param modeClass the next global editor mode
     */
    public ActionSetMode(Class modeClass) {
        super(modeClass);
    }

    /**
     * The constructor.
     *
     * @param modeClass the next global editor mode
     * @param name the name of the icon
     */
    public ActionSetMode(Class modeClass, String name) {
        super(modeClass);
        putToolTip(name);
        putIcon(name);
    }

    /**
     * The constructor.
     *
     * @param modeClass the next global editor mode
     * @param name the icon name
     * @param tooltipkey The key for the tooltip text.
     */
    public ActionSetMode(Class modeClass, String name, String tooltipkey) {
        super(modeClass, name);
        putToolTip(tooltipkey);
        putIcon(name);
    }

    /**
     * The constructor.
     *
     * @param modeClass the next global editor mode
     * @param sticky the global sticky mode boolean allows the user
     *               to place several nodes rapidly (in succession)
     */
    public ActionSetMode(Class modeClass, boolean sticky) {
        super(modeClass, sticky);
    }

    /**
     * The constructor.
     *
     * @param modeClass the next global editor mode
     * @param modeArgs arguments for the new mode
     */
    public ActionSetMode(Class modeClass, Hashtable modeArgs) {
        super(modeClass, modeArgs);
    }

    /**
     * The constructor.
     * TODO: The "name" parameter is used for the icon and for the tooltip.
     * This makes i18n of the tooltip impossible.
     *
     * @param modeClass the next global editor mode
     * @param modeArgs arguments for the new mode
     * @param name the name of the command that is the tooltip text.
     */
    public ActionSetMode(Class modeClass, Hashtable modeArgs, String name) {
    	super(modeClass);
        this.modeArgs = modeArgs;
        putToolTip(name);
        putIcon(name);
    }

    /**
     * The constructor.
     *
     * @param modeClass the next global editor mode
     * @param arg the name of a new argument for the new mode
     * @param value the value of a new argument for the new mode
     */
    public ActionSetMode(Class modeClass, String arg, Object value) {
        super(modeClass, arg, value);
    }

    /**
     * The constructor.
     * TODO: The "name" parameter is used for the icon and for the tooltip.
     * This makes i18n of the tooltip impossible.
     *
     * @param modeClass the next global editor mode
     * @param arg the name of a new argument for the new mode
     * @param value the value of a new argument for the new mode
     * @param name the name of the command that is the tooltip text.
     */
    public ActionSetMode(Class modeClass, String arg, Object value, 
            String name) {
        super(modeClass, arg, value);
        putToolTip(name);
        putIcon(name);
    }

    /**
     * The constructor.
     *
     * @param modeClass the next global editor mode
     * @param arg the name of a new argument for the new mode
     * @param value the value of a new argument for the new mode
     * @param name the name of the command that is the tooltip text.
     * @param icon the SMALL_ICON for the action
     */
    public ActionSetMode(
        Class modeClass,
        String arg,
        Object value,
        String name,
        ImageIcon icon) {
        super(modeClass, arg, value, name, icon);
        putToolTip(name);
    }

    /**
     * Adds tooltip text to the Action.
     *
     * @param key The key to be localized to become the tooltip.
     */
    private void putToolTip(String key) {
        putValue(Action.SHORT_DESCRIPTION, Translator.localize(key));
    }
    
    private void putIcon(String key) {
        ImageIcon icon = ResourceLoaderWrapper.lookupIcon(key);
        if (icon != null) {
            putValue(Action.SMALL_ICON, icon);
        } else {
            LOG.debug("Failed to find icon for key " + key);
        }
    }
}
