// $Id$
// Copyright (c) 2003-2004 The Regents of the University of California. All
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

import org.argouml.i18n.Translator;
import org.tigris.gef.base.ModeBroom;
import org.tigris.gef.base.ModeSelect;

/**
 * Extends GEF CmdSetMode to add additional metadata such as tooltips.
 * 
 * @author Jeremy Jones
 */
public class CmdSetMode extends org.tigris.gef.base.CmdSetMode {

    private static final String ACTION_PREFIX_KEY = "action.new";

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
     * @param modeClass the mode
     */
    public CmdSetMode(Class modeClass) {
        super(modeClass);
    }

    public CmdSetMode(Class modeClass, String name) {
        super(modeClass, name);
        putToolTip(name);
    }

    public CmdSetMode(Class modeClass, String name, String tooltip) {
        super(modeClass, name);
        putToolTip(tooltip);
    }

    public CmdSetMode(Class modeClass, boolean sticky) {
        super(modeClass, sticky);
    }

    public CmdSetMode(Class modeClass, Hashtable modeArgs) {
        super(modeClass, modeArgs);
    }
    
    public CmdSetMode(Class modeClass, Hashtable modeArgs, String name) {
    	super(modeClass, name);
    	_modeArgs = modeArgs;
    }

    public CmdSetMode(Class modeClass, String arg, Object value) {
        super(modeClass, arg, value);
    }

    public CmdSetMode(Class modeClass, String arg, Object value, String name) {
        super(modeClass, arg, value, name);
        putToolTip(name);
    }

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
     */
    private void putToolTip(String name) {
        Class desiredModeClass = (Class) getArg("desiredModeClass");
        if (ModeSelect.class.isAssignableFrom(desiredModeClass)
            || ModeBroom.class.isAssignableFrom(desiredModeClass)) {
            putValue(Action.SHORT_DESCRIPTION, Translator.localize(name));
        }
        else {
            putValue(Action.SHORT_DESCRIPTION, 
		     Translator.localize(ACTION_PREFIX_KEY) + " " 
		     + Translator.localize(name));
        }
    }
}
