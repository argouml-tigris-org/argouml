// $Id$
// Copyright (c) 1996-2002 The Regents of the University of California. All
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

/*
 * LookAndFeelMgr.java
 *
 * Created on 12 October 2002, 22:25
 */

package org.argouml.ui;

import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.metal.MetalTheme;

import org.apache.log4j.Category;
import org.argouml.application.api.Argo;
import org.argouml.application.api.Configuration;
//import org.argouml.swingext.MetalThemes;

/**
 * Controls the look and feel and theme of ArgoUML. LookAndFeelMgr has a
 * "Look and Feel" property which represents the Swing Pluggable Look-and-feel.
 * It also has a "Theme" property which represents a MetalTheme when the
 * Metal look-and-feel is selected.
 *
 * @author Bob Tarling
 * @author Jeremy Jones
 */
public class LookAndFeelMgr {
    public static final LookAndFeelMgr	SINGLETON = new LookAndFeelMgr();

    private static Category                     cat =
	Category.getInstance(LookAndFeelMgr.class);

    // The Metal look and feel class name
    private static final String                 METAL_LAF =
	"javax.swing.plaf.metal.MetalLookAndFeel";

    // Display name and configuration key for default look and feel and theme
    private static final String			DEFAULT_KEY = "Default";

    // JasonsThemes
    private static final MetalTheme		DEFAULT_THEME =
	new JasonsTheme();
    private static final MetalTheme		BIG_THEME =
	new JasonsBigTheme();
    private static final MetalTheme		HUGE_THEME =
	new JasonsHugeTheme();

    // The list of supported MetalThemes
    private static final MetalTheme[] THEMES = {
        DEFAULT_THEME,
        BIG_THEME,
        HUGE_THEME,
        new DefaultMetalTheme()
        //new MetalThemes.Green(),
        //new MetalThemes.BlueGreen(),
        //new MetalThemes.Khaki(),
        //new MetalThemes.Presentation()
    };

    /**
     * The class name of Swing's default look and feel (will be used if
     * the LookAndFeel property is null).
     **/
    private String				_defaultLafClass;

    /** Creates a new instance of LookAndFeelMgr. */
    private LookAndFeelMgr() {
        LookAndFeel laf = UIManager.getLookAndFeel();
        if (laf != null) {
            _defaultLafClass = laf.getClass().getName();
        }
        else {
            _defaultLafClass = null;
        }
    }

    /**
     * Sets the appearance of the UI using the current values of
     * the LookAndFeel and Theme properties.
     **/
    public void initializeLookAndFeel() {
        setLookAndFeel(getCurrentLookAndFeel());
        setTheme(getMetalTheme(getCurrentTheme()));
    }

    /**
     * Detecting the theme from the command line.
     */
    public String getThemeFromArg(String arg) {
        if (arg.equalsIgnoreCase("-big")) {
            return BIG_THEME.getClass().getName();
        }
        else if (arg.equalsIgnoreCase("-huge")) {
            return HUGE_THEME.getClass().getName();
        }
        return null;
    }

    /**
     * Outputs command-line arguments supported by this class.
     **/
    public void printThemeArgs() {
        System.err.println("  -big            use big fonts");
        System.err.println("  -huge           use huge fonts");
    }

    /**
     * Returns the display names of the available look and feel choices.
     * 
     * @return	look and feel display names
     **/
    public String[] getAvailableLookAndFeelNames() {
        UIManager.LookAndFeelInfo[] lafs = UIManager.getInstalledLookAndFeels();

        String[] names = new String[lafs.length + 1];
        names[0] = DEFAULT_KEY;
        for (int i = 0; i < lafs.length; ++i) {
            names[i + 1] = lafs[i].getName();
        }

        return names;
    }

    /**
     * Returns the display names of the available theme choices.
     * 
     * @return	theme display names
     **/
    public String[] getAvailableThemeNames()
    {
        String[] names = new String[LookAndFeelMgr.THEMES.length];
        for (int i = 0; i < THEMES.length; ++i) {
            names[i] = THEMES[i].getName();
        }

        return names;
    }

    /**
     * Returns the Look and Feel class name identifier for the specified
     * display name, or null if no such Look and Feel is found.
     * 
     * @param	name	display name of desired look and feel
     * @return			class name for desired look and feel
     **/
    public String getLookAndFeelFromName(String name) {
        if (name == null || DEFAULT_KEY.equals(name)) {
            return null;
        }

        String className = null;

        UIManager.LookAndFeelInfo[] lafs = UIManager.getInstalledLookAndFeels();
        for (int i = 0; i < lafs.length; ++i) {
            if (lafs[i].getName().equals(name)) {
                className = lafs[i].getClassName();
            }
        }

        return className;
    }

    /**
     * Returns the theme class name identifier for the specified
     * display name, or null if no such theme is found.
     * 
     * @param	name	display name of desired theme
     * @return			class name for desired theme
     **/
    public String getThemeFromName(String name) {
        if (name == null) {
            return null;
        }

        String className = null;

        for (int i = 0; i < THEMES.length; ++i) {
            if (THEMES[i].getName().equals(name)) {
                className = THEMES[i].getClass().getName();
            }
        }

        return className;
    }

    /**
     * Returns true if the specified look and feel class
     * supports setting different themes.
     * 
     * @param	lafClass	look and feel class name
     * @return				true if supports themes
     **/
    public boolean isThemeCompatibleLookAndFeel(String lafClass)
    {
        return (lafClass == null || lafClass.equals(METAL_LAF));
    }

    /**
     * Returns the string identifier for the current look and feel.
     * This is the class name of the LookAndFeel class for the look and feel.
     * Returns null if no look and feel has been specified, in which case
     * Swing's default look and feel should be used.
     * 
     * @return	current look and feel class name
     **/
    public String getCurrentLookAndFeel()
    {
        String value =
	    Configuration.getString(Argo.KEY_LOOK_AND_FEEL_CLASS, null);
        if (DEFAULT_KEY.equals(value)) {
            value = null;
        }
        return value;
    }

    /**
     * Returns the display name of the current look and feel.
     * 
     * @return	look and feel display name
     **/
    public String getCurrentLookAndFeelName() {
        String currentLookAndFeel = getCurrentLookAndFeel();

        if (currentLookAndFeel == null) {
            return DEFAULT_KEY;
        } else {
            String name = null;

            UIManager.LookAndFeelInfo[] lafs =
		UIManager.getInstalledLookAndFeels();
            for (int i = 0; i < lafs.length; ++i) {
                if (lafs[i].getClassName().equals(currentLookAndFeel)) {
		    name = lafs[i].getName();
                }
            }

            return name;
        }
    }

    /**
     * Sets the current look and feel, storing the new value in
     * Configuration.  If argument is null, indicates that Swing's default
     * look and feel should be used.
     * 
     * @param	lafClass	class name of desired look and feel
     **/
    public void setCurrentLookAndFeel(String lafClass) {
        String currentLookAndFeel = getCurrentLookAndFeel();

        if ((lafClass == null && currentLookAndFeel == null)
            || (lafClass != null && lafClass.equals(currentLookAndFeel))) {
            return;
        }

        //setLookAndFeel(lafClass); Disabled until UI supports dynamic switching

        String lafValue = lafClass;
        if (lafValue == null) {
            lafValue = DEFAULT_KEY;
        }
        Configuration.setString(Argo.KEY_LOOK_AND_FEEL_CLASS, lafValue);
    }

    /**
     * Returns the string identifier for the current theme.
     * This is the class name of the MetalTheme class for the theme.
     * This method should never return null.
     * 
     * @return	current theme class name
     **/
    public String getCurrentTheme() {
	String value = Configuration.getString(Argo.KEY_THEME_CLASS, null);
	if (DEFAULT_KEY.equals(value)) {
	    value = null;
	}
	return value;
    }

    /**
     * Returns the display name of the current theme.
     * 
     * @return	theme display name
     **/
    public String getCurrentThemeName() {
        String currentTheme = getCurrentTheme();

        if (currentTheme == null) {
            return DEFAULT_THEME.getName();
        } else {
            String name = null;

            for (int i = 0; i < THEMES.length; ++i) {
                if (THEMES[i].getClass().getName().equals(currentTheme)) {
                    name = THEMES[i].getName();
                }
            }
            return name;
        }
    }

    /**
     * Sets the current theme, storing the new value in
     * Configuration. Argument should not be null, and argument class
     * should be an instance of MetalTheme.
     * 
     * @param	themeClass	class name of desired theme
     **/
    public void setCurrentTheme(String themeClass) {
        MetalTheme theme = getMetalTheme(themeClass);

        if (theme.getClass().getName().equals(getCurrentTheme())) {
            return;
        }

        //setTheme(theme); Disabled until UI supports dynamic switching

        String themeValue = themeClass;
        if (themeValue == null) {
            themeValue = DEFAULT_KEY;
        }
        Configuration.setString(Argo.KEY_THEME_CLASS, themeValue);
    }

    /**
     * Sets the look and feel in the GUI by calling UIManager.setLookAndFeel().
     * 
     * @param	lafClass	class name of look and feel
     **/
    private void setLookAndFeel(String lafClass) {
        try {
            if (lafClass == null && _defaultLafClass != null) {
                // Set to the default LAF
                UIManager.setLookAndFeel(_defaultLafClass);
            } else {
                // Set a custom LAF
                UIManager.setLookAndFeel(lafClass);
            }
        } catch (UnsupportedLookAndFeelException e) {
            cat.error(e);
        } catch (ClassNotFoundException e) {
            cat.error(e);
        } catch (InstantiationException e) {
            cat.error(e);
        } catch (IllegalAccessException e) {
            cat.error(e);
        }
    }

    /**
     * Sets the metal theme in the GUI by calling
     * MetalLookAndFeel.setCurrentTheme().
     * 
     * @param	theme	new MetalTheme to set
     **/
    private void setTheme(MetalTheme theme) {
        String currentLookAndFeel = getCurrentLookAndFeel();

        // If LAF is Metal (either set explicitly, or as the default)
        if ((currentLookAndFeel != null && currentLookAndFeel.equals(METAL_LAF))
	    || (currentLookAndFeel == null && _defaultLafClass.equals(METAL_LAF))) {
            try {
                MetalLookAndFeel.setCurrentTheme(theme);
                UIManager.setLookAndFeel(METAL_LAF);
            } catch (UnsupportedLookAndFeelException e) {
                cat.error(e);
            } catch (ClassNotFoundException e) {
                cat.error(e);
            } catch (InstantiationException e) {
                cat.error(e);
            } catch (IllegalAccessException e) {
                cat.error(e);
            }
        }
    }

    /**
     * Returns the MetalTheme for the specified class name.
     * Returns the default theme if a corresponding MetalTheme class
     * can not be found.
     * 
     * @param	themeClass	MetalTheme class name
     * @return				MetalTheme object for class name
     **/
    private MetalTheme getMetalTheme(String themeClass) {
        MetalTheme theme = null;

        for (int i = 0; i < THEMES.length; ++i) {
            if (THEMES[i].getClass().getName().equals(themeClass)) {
                theme = THEMES[i];
            }
        }

        if (theme == null) {
            theme = DEFAULT_THEME;
        }

        return theme;
    }
}
