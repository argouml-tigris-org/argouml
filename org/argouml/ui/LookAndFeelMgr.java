/*
 * LookAndFeelMgr.java
 *
 * Created on 12 October 2002, 22:25
 */

package org.argouml.ui;

import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.*;

import org.apache.log4j.Category;

import org.argouml.application.api.*;

import org.tigris.gef.util.*;

/**
 *
 * @author Bob Tarling
 */
public class LookAndFeelMgr {
    
    protected static Category cat = 
        Category.getInstance(ProjectBrowser.class);

    public static final LookAndFeelMgr SINGLETON = new LookAndFeelMgr();
    
    private static final int THEME_NOT_SET = -1;
    private static final int THEME_DEFAULT = 0;
    private static final int THEME_NORMAL = 1;
    private static final int THEME_BIG = 2;
    private static final int THEME_HUGE = 3;

    /** Creates a new instance of LookAndFeelMgr */
    private LookAndFeelMgr() {
    }
    
    /**
     * Detecting the theme from the command line.
     */
    public int getThemeFromArg(String arg) {
        if (arg.equalsIgnoreCase("-big")) {
            return THEME_BIG;
        } else if (arg.equalsIgnoreCase("-huge")) {
            return THEME_HUGE;
        }
        return 0;
    }
    public void printThemeArgs() {
        System.err.println("  -big            use big fonts");
        System.err.println("  -huge           use huge fonts");
    }

    private int currentTheme = THEME_NOT_SET;
    
    private int getCurrentTheme() { return currentTheme; }
    
    public void setCurrentTheme(int t) {
        if (t == THEME_DEFAULT) t = Configuration.getInteger(Argo.KEY_SCREEN_THEME, THEME_NORMAL);
        if (currentTheme == t) return;
        
        currentTheme = t;
        switch (t) {
        case THEME_NORMAL:
        default:
            currentTheme = THEME_NORMAL;
            MetalLookAndFeel.setCurrentTheme(new org.argouml.ui.JasonsTheme());
            break;

        case THEME_BIG:
            MetalLookAndFeel.setCurrentTheme(new org.argouml.ui.JasonsBigTheme());
            break;

        case THEME_HUGE:
            MetalLookAndFeel.setCurrentTheme(new org.argouml.ui.JasonsHugeTheme());
            break;
        }
        try {
            UIManager.setLookAndFeel(determineLookAndFeel());
        }
        catch (UnsupportedLookAndFeelException e) {
            cat.error(e);
        }
        catch (ClassNotFoundException e) {
            cat.error(e);
        }
        catch (InstantiationException e) {
            cat.error(e);
        }
        catch (IllegalAccessException e) {
            cat.error(e);
        }
        Configuration.setInteger(Argo.KEY_SCREEN_THEME, currentTheme);
    }
    
    public String determineLookAndFeel() {
        if ("true".equals(System.getProperty("force.nativelaf","false"))) {
            return UIManager.getSystemLookAndFeelClassName();
        }
        else {
            return "javax.swing.plaf.metal.MetalLookAndFeel";
        }
    }
    
    public void setCurrentTheme(String arg) {
        if ("normal".equals(arg))
            setCurrentTheme(THEME_NORMAL);
        else if ("big".equals(arg))
            setCurrentTheme(THEME_BIG);
        else if ("huge".equals(arg))
            setCurrentTheme(THEME_HUGE);
        else {
            cat.error("ProjectBrowser.setCurrentTheme: "
                      + "Incorrect theme: " + arg);
        }
    }
    
    public boolean isCurrentTheme(String arg) {
        if ("normal".equals(arg))
            return getCurrentTheme() == THEME_NORMAL;
        else if ("big".equals(arg))
            return getCurrentTheme() == THEME_BIG;
        else if ("huge".equals(arg))
            return getCurrentTheme() == THEME_HUGE;
        else {
            cat.error("ProjectBrowser.isCurrentTheme: "
                      + "Incorrect theme: " + arg);
            return false;
        }
    }
}
