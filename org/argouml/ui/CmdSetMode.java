package org.argouml.ui;

import java.util.Hashtable;
import java.util.Properties;

import javax.swing.Action;
import javax.swing.ImageIcon;

import org.argouml.i18n.Translator;

/**
 * Extends GEF CmdSetMode to add additional metadata such as tooltips.
 * 
 * @author Jeremy Jones
**/
public class CmdSetMode extends org.tigris.gef.base.CmdSetMode {

    public CmdSetMode(Properties args) {
        super(args);
    }

    public CmdSetMode(Class modeClass) {
        super(modeClass);
    }

    public CmdSetMode(Class modeClass, String name) {
        super(modeClass, name);
        putToolTip(name);
    }

    public CmdSetMode(Class modeClass, boolean sticky) {
        super(modeClass, sticky);
    }

    public CmdSetMode(Class modeClass, Hashtable modeArgs) {
        super(modeClass, modeArgs);
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
        putValue(Action.SHORT_DESCRIPTION, Translator.localize(name));
    }
}
