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

import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.*;

import org.apache.log4j.Category;

import org.argouml.application.api.*;

import org.tigris.gef.util.*;

/**
 * Control look and feel and theme of ArgoUML.
 * @todo currently we still rely heavily on the Metal Theme and on
 *       jasons theme classes to get the theme right.
 *
 * @author Bob Tarling
 */
public class LookAndFeelMgr {
    
    protected static Category cat = 
        Category.getInstance(LookAndFeelMgr.class);

    public static final LookAndFeelMgr SINGLETON = new LookAndFeelMgr();
    
    private static final int THEME_NOT_SET = -1;
    private static final int THEME_DEFAULT = 0;
    private static final int THEME_NORMAL = 1;
    private static final int THEME_BIG = 2;
    private static final int THEME_HUGE = 3;

    /** Creates a new instance of LookAndFeelMgr. */
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
    
    /** set the theme to use according to the configuration information. */
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
            cat.error("LookAndFeelMgr.setCurrentTheme: "
                      + "Incorrect theme: " + arg);
        }
    }

    /** check whether the currently set theme equals the argument.
     *  @param arg one of normal, big or huge.
     */
    public boolean isCurrentTheme(String arg) {
        if ("normal".equals(arg))
            return getCurrentTheme() == THEME_NORMAL;
        else if ("big".equals(arg))
            return getCurrentTheme() == THEME_BIG;
        else if ("huge".equals(arg))
            return getCurrentTheme() == THEME_HUGE;
        else {
            cat.error("LookAndFeelMgr.isCurrentTheme: "
                      + "Incorrect theme: " + arg);
            return false;
        }
    }
}
