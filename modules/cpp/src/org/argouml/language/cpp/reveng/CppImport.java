// $Id$
// Copyright (c) 2005 The Regents of the University of California. All
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

package org.argouml.language.cpp.reveng;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

import org.apache.log4j.Logger;
import org.argouml.application.api.Configuration;
import org.argouml.application.api.ConfigurationKey;
import org.argouml.kernel.Project;
import org.argouml.uml.reveng.DiagramInterface;
import org.argouml.uml.reveng.FileImportSupport;
import org.argouml.uml.reveng.Import;
import org.argouml.util.SuffixFilter;

/**
 * Implementation of the reverse engineering interface of ArgoUML,
 * <code>PluggableImport</code>, for the C++ module.
 * 
 * FIXME i18n support?!
 * 
 * TODO: when the module is ready for prime time, remove the warning.
 * 
 * @author Luis Sergio Oliveira (euluis)
 * @since 0.19.2
 */
public class CppImport extends FileImportSupport {

    /** logger */
    private static final Logger LOG = Logger.getLogger(CppImport.class);

    /**
     * Flag for warning the user about the limitations of the C++ module. The
     * default value is true, which means that if there isn't the respective
     * property, the user will be warned.
     */
    private boolean userWarning = Configuration.getBoolean(KEY_USER_WARNING,
        true);

    /**
     * Configuration key for the user warning.
     */
    private static final ConfigurationKey KEY_USER_WARNING = Configuration
            .makeKey("cpp", "reveng", "user", "warning");

    /**
     * @see org.argouml.application.api.PluggableImport#parseFile(
     *      org.argouml.kernel.Project, java.lang.Object,
     *      org.argouml.uml.reveng.DiagramInterface,
     *      org.argouml.uml.reveng.Import)
     */
    public void parseFile(Project p, Object o, DiagramInterface diagram,
            Import theImport) throws Exception {
        LOG.warn("Not fully implemented yet!");

        if (o instanceof File) {
            File f = (File) o;
            FileInputStream in = new FileInputStream(f);
            try {
                Modeler modeler = new ModelerImpl();
                CPPLexer lexer = new CPPLexer(in);
                CPPParser parser = new CPPParser(lexer);
                parser.translation_unit(modeler);
            } finally {
                in.close();
            }
        } else
            LOG.error("o isn't a File!");
    }

    /**
     * This method is overriden cause it is a nice place to show a warning to
     * the user about the immaturity of the reveng. TODO: this should be changed
     * to show options that will be used by the module in reveng.
     * 
     * @see org.argouml.application.api.PluggableImport#getConfigPanel()
     */
    public JComponent getConfigPanel() {
        JComponent cfgPanel = super.getConfigPanel();
        if (userWarning) {
            warnUser(cfgPanel);
        }
        return cfgPanel;
    }

    /**
     * Show a dialog box to the user, warning that the C++ reveng is still very
     * limited. The list of obvious limitations must be shown. The user is given
     * the option of, by default, not seeing the warning again.
     * 
     * TODO: i18n, or not to-do?... This warning is temporary, and it will
     * change often in the future - hopefully removing limitations - so, would
     * the effort of i18n pay off? I don't think so.
     * 
     * @param parentComponent
     */
    private void warnUser(JComponent parentComponent) {
        final String lineSepAndListIndent = System
                .getProperty("line.separator")
            + "    * ";
        String warnMsg = "The C++ reverse engineering module is pre-alpha "
            + "stage."
            + System.getProperty("line.separator")
            + "Its known limits are: "
            + lineSepAndListIndent
            + "preprocessed files only, i.e., works on full translation units;"
            + lineSepAndListIndent
            + "very few C++ constructs are supported, e.g., enums, unions, "
            + "templates, etc, aren't;"
            + lineSepAndListIndent
            + "no support for non-member variables and functions;"
            + lineSepAndListIndent
            + "no integration with the C++ generator => RTE won't work!;"
            + lineSepAndListIndent + "no operator overload support;"
            + lineSepAndListIndent
            + "very immature, certainly this list needs to grow!";
        JOptionPane warnDlg = new JOptionPane(warnMsg,
            JOptionPane.WARNING_MESSAGE);
        JCheckBox warnAgainButton = new JCheckBox("Don't warn me again", true);
        JButton okButton = new JButton("OK");
        warnDlg.setOptions(new Object[] {warnAgainButton, okButton });
        final JDialog dlg = warnDlg.createDialog(parentComponent,
            "C++ reveng module limits");
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dlg.dispose();
            }

        });
        dlg.show();
        userWarning = warnAgainButton.isSelected();
        Configuration.setBoolean(KEY_USER_WARNING, userWarning);
        LOG.debug("userWarning = " + userWarning);
        // Even if the user didn't turn off the warning, we won't show it to
        // him again in this ArgoUML run.
        userWarning = false;
    }

    /**
     * The suffix filters for C++ files. Header sufixes are left out, since the
     * module should deal with files that originate translation units.
     */
    private static final SuffixFilter[] CPP_SUFFIX_FILTERS = {
        new SuffixFilter("cxx", "C++ source files"),
        new SuffixFilter("c++", "C++ source files"),
        new SuffixFilter("C++", "C++ source files"),
        new SuffixFilter("CPP", "C++ source files"),
        new SuffixFilter("cpp", "C++ source files"), };

    /**
     * TODO: I would like that an option to have all suffix applied to exist.
     * Maybe this has to be fixed within ArgoUML...
     * 
     * @see org.argouml.uml.reveng.FileImportSupport#getSuffixFilters()
     */
    public SuffixFilter[] getSuffixFilters() {
        return CPP_SUFFIX_FILTERS;
    }

    /**
     * @see org.argouml.application.api.ArgoModule#getModuleName()
     */
    public String getModuleName() {
        return "C++";
    }

    /**
     * @see org.argouml.application.api.ArgoModule#getModuleDescription()
     */
    public String getModuleDescription() {
        return "C++ reverse engineering support";
    }

    /**
     * @see org.argouml.application.api.ArgoModule#getModuleKey()
     */
    public String getModuleKey() {
        return "module.language.cpp.reveng";
    }

    /**
     * @see org.argouml.application.api.ArgoModule#getModuleAuthor()
     */
    public String getModuleAuthor() {
        return "Luis Sergio Oliveira (euluis)";
    }

    /**
     * @see org.argouml.application.api.ArgoModule#getModuleVersion()
     */
    public String getModuleVersion() {
        return "0.00";
    }

}