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

package org.argouml.ui;

import org.argouml.application.api.Argo;
import org.argouml.application.api.PluggableMenu;
import org.argouml.i18n.Translator;
import org.argouml.uml.ui.UMLAction;

import org.workingfrog.i18n.swing.I18NJMenu;
import org.workingfrog.i18n.swing.I18NJMenuItem;
import org.workingfrog.i18n.util.LocaleEvent;

import java.awt.event.ActionEvent;

import java.util.HashMap;
import java.util.Locale;
import java.util.Vector;

import javax.swing.JMenuItem;

/**
 * Allow the user to switch from OS language to one of his choice.
 * @author Jean-Hugues de Raigniac
 * @since  0.11
 */
public class LanguageChooser extends UMLAction implements PluggableMenu {

    /** A singleton as the only instance of this action. */
    private static LanguageChooser singleton = new LanguageChooser();

    /** Entry point for LanguageChooser in Tools' menu. */
    private static I18NJMenu menuItem = null;

    /** Mapper between JMenuItems and Locales. */
    private HashMap item2Locale;

    /**
     * Create a new LanguageChooser instance (is not public, due to 
     * singleton pattern).
     */
    protected LanguageChooser() {
        super("menu.item.languages", false);
    }

    /** 
     * Get the instance.
     *
     * @return The LanguageChooser instance
     */
    public static LanguageChooser getInstance() {
        return singleton;
    }

    public boolean initializeModule() {
        if (((Locale[]) Translator.getLocales()).length < 1) {
            Argo.log.error ("Unable to launch LanguageChooser plugin!");

            return false;
        }

        Argo.log.info ("+---------------------------------+");
        Argo.log.info ("| LanguageChooser plugin enabled! |");
        Argo.log.info ("+---------------------------------+");

        return true;
    }

    public boolean inContext(Object[] o) {
        if (o.length < 2) { return false; }
        if ((o[0] instanceof JMenuItem)
            && ("Tools".equals(o[1]))) {
            return true;
        }
        return false;
    }

    public void setModuleEnabled(boolean v) { /* empty */ }
    public boolean isModuleEnabled() { return true; }
    public Vector getModulePopUpActions(Vector v, Object o) { return null; }
    public boolean shutdownModule() { return true; }

    public String getModuleName() { return "LanguageChooser"; }
    public String getModuleDescription() {
        return "Menu Item for LanguageChooser";
    }
    public String getModuleAuthor() { return "Jean-Hugues de Raigniac"; }
    public String getModuleVersion() { return "0.11.3"; }
    public String getModuleKey() { return "module.menu.tools.languages"; }

    public JMenuItem getMenuItem(JMenuItem mi, String s) {
        return getMenuItem(buildContext(mi, s));
    }

    public Object[] buildContext(JMenuItem a, String b) {
        return new Object[] {
	    a, b 
	};
    }

    /** 
     * Return the JMenuItem controlled by the plugin under the specific
     * context.  One menu plugin may control multiple menu items.
     * @param context array of objects as created by
     * {@link #buildContext(JMenuItem, String) }.
     * @return A JMenuItem object controlled by the plug-in.
     */
    public JMenuItem getMenuItem(Object [] context) {
        if (!inContext(context)) {
            return null;
        }

        if (menuItem == null) {
            menuItem = new I18NJMenu("menu.item.languages");

            Locale[] locales = Translator.getLocales();
            item2Locale = new HashMap(locales.length);

            for (int j = locales.length - 1; j >= 0; j--) {
                String key = "menu.languages-"
                    + locales[j].toString().toLowerCase();
                I18NJMenuItem item = new I18NJMenuItem(key);
                item.setBundle("CoreMenu");
                item2Locale.put(item, locales[j]);
                item.addActionListener(this);
                menuItem.add(item);
            }
        }

        return menuItem;
    }

    /**
     * Fired by a I18NJMenuItem.
     * @param event Usual event of JMenuItem
     */
    public void actionPerformed(ActionEvent event) {
        Locale locale = (Locale) item2Locale.get(event.getSource());

        Translator.setLocale(locale);

        LocaleEvent e = new LocaleEvent(this, locale);
        org.argouml.ui.ProjectBrowser.TheInstance.localeChanged(e);
            
    }
}
