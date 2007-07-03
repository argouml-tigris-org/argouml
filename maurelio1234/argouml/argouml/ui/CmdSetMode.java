// $Id: CmdSetMode.java 12546 2007-05-05 16:54:40Z linus $
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

package org.argouml.ui;

import java.util.Hashtable;
import java.util.Properties;

import javax.swing.Action;
import javax.swing.ImageIcon;

import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.i18n.Translator;

/**
 * Extends GEF CmdSetMode to add additional metadata such as tooltips.
 *
 * @author Jeremy Jones
 * @deprecated for 0.25.3 by tfmorris - use {@link ActionSetMode}
 */

@SuppressWarnings("deprecation")
public class CmdSetMode extends org.tigris.gef.base.CmdSetMode {

    /**
     * The constructor.
     *
     * @param args arguments
     */
    public CmdSetMode(Properties args) {
        super(args);
    }

    /**
     * The constructor.
     *
     * @param modeClass the next global editor mode
     */
    public CmdSetMode(Class modeClass) {
        super(modeClass);
    }

    /**
     * The constructor.
     *
     * @param modeClass the next global editor mode
     * @param name the name of the command that is the tooltip text.
     */
    public CmdSetMode(Class modeClass, String name) {
        super(modeClass, ResourceLoaderWrapper.getImageBinding(name));
        putToolTip(name);
    }

    /**
     * The constructor.
     *
     * @param modeClass the next global editor mode
     * @param name the to be localized name of the command
     * @param tooltipkey The key for the tooltip text.
     */
    public CmdSetMode(Class modeClass, String name, String tooltipkey) {
        super(modeClass, name);
        putToolTip(tooltipkey);
    }

    /**
     * The constructor.
     *
     * @param modeClass the next global editor mode
     * @param sticky the global sticky mode boolean allows the user
     *               to place several nodes rapidly (in succession)
     */
    public CmdSetMode(Class modeClass, boolean sticky) {
        super(modeClass, sticky);
    }

    /**
     * The constructor.
     *
     * @param modeClass the next global editor mode
     * @param modeArgs arguments for the new mode
     */
    public CmdSetMode(Class modeClass, Hashtable modeArgs) {
        super(modeClass, modeArgs);
    }

    /**
     * The constructor.
     *
     * @param modeClass the next global editor mode
     * @param modeArgs arguments for the new mode
     * @param name the name of the command that is the tooltip text.
     */
    public CmdSetMode(Class modeClass, Hashtable modeArgs, String name) {
        super(modeClass, ResourceLoaderWrapper.getImageBinding(name));
        _modeArgs = modeArgs;
        putToolTip(name);
    }

    /**
     * The constructor.
     *
     * @param modeClass the next global editor mode
     * @param arg the name of a new argument for the new mode
     * @param value the value of a new argument for the new mode
     */
    public CmdSetMode(Class modeClass, String arg, Object value) {
        super(modeClass, arg, value);
    }

    /**
     * The constructor.
     *
     * @param modeClass the next global editor mode
     * @param arg the name of a new argument for the new mode
     * @param value the value of a new argument for the new mode
     * @param name the name of the command that is the tooltip text.
     */
    public CmdSetMode(Class modeClass, String arg, Object value, String name) {
        super(modeClass, arg, value,
                ResourceLoaderWrapper.getImageBinding(name));
        putToolTip(name);
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
    public CmdSetMode(
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
}
