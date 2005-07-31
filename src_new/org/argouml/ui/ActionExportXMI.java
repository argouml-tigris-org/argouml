// $Id$
// Copyright (c) 1996-2005 The Regents of the University of California. All
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

import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;

import org.apache.log4j.Logger;
import org.argouml.application.api.Configuration;
import org.argouml.application.api.PluggableMenu;
import org.argouml.i18n.Translator;
import org.argouml.persistence.PersistenceManager;
import org.argouml.persistence.XmiFilePersister;

/**
 * Exports the xmi of a project to a file choosen by the user.
 * @author jaap.branderhorst@xs4all.nl
 * Jun 7, 2003
 */
public final class ActionExportXMI extends AbstractAction
        implements PluggableMenu {

    /** logger */
    private static final Logger LOG = Logger.getLogger(ActionExportXMI.class);

    private static ActionExportXMI instance = new ActionExportXMI();

    /**
     * Constructor.
     */
    private ActionExportXMI() {
        super(Translator.localize("action.export-project-as-xmi"));
    }

    /**
     * Singleton instance method
     * @return the singleton instance
     */
    public static ActionExportXMI getInstance() {
        return instance;
    }

    /**
     * @see
     * org.argouml.application.api.PluggableMenu#getMenuItem(java.lang.Object[])
     */
    public JMenuItem getMenuItem(Object[] context) {
        if (!inContext(context)) {
            return null;
        }
        // next code does not work with JDK 1.2
        return new JMenuItem(this);
    }

    /**
     * @see PluggableMenu#buildContext(javax.swing.JMenuItem, String)
     */
    public Object[] buildContext(JMenuItem parentMenuItem, String menuType) {
        return new Object[] {
	    parentMenuItem, menuType,
	};
    }

    /**
     * @see org.argouml.application.api.Pluggable#inContext(java.lang.Object[])
     */
    public boolean inContext(Object[] context) {
        if (context.length < 2) {
            return false;
        }
        if ((context[0] instanceof JMenuItem)
            && ("Tools".equals(context[1]))) {
            return true;
        }
        return false;
    }

    /**
     * @see org.argouml.application.api.ArgoModule#initializeModule()
     */
    public boolean initializeModule() {
        LOG.info("+---------------------------------+");
        LOG.info("| Export XMI plugin enabled!      |");
        LOG.info("+---------------------------------+");

        return true;
    }

    /**
     * @see org.argouml.application.api.ArgoModule#shutdownModule()
     */
    public boolean shutdownModule() {
        return true;
    }

    /**
     * @see org.argouml.application.api.ArgoModule#setModuleEnabled(boolean)
     */
    public void setModuleEnabled(boolean tf) {
    }

    /**
     * @see org.argouml.application.api.ArgoModule#isModuleEnabled()
     */
    public boolean isModuleEnabled() {
        return true;
    }

    /**
     * @see org.argouml.application.api.ArgoModule#getModuleName()
     */
    public String getModuleName() {
        return "Export as XMI";
    }

    /**
     * @see org.argouml.application.api.ArgoModule#getModuleDescription()
     */
    public String getModuleDescription() {
        return "A module to export a projectfile as XMI";
    }

    /**
     * @see org.argouml.application.api.ArgoModule#getModuleVersion()
     */
    public String getModuleVersion() {
        return "0.1";
    }

    /**
     * @see org.argouml.application.api.ArgoModule#getModuleAuthor()
     */
    public String getModuleAuthor() {
        return "Jaap Branderhorst";
    }

    /**
     * @see org.argouml.application.api.ArgoModule#getModulePopUpActions(
     * Vector, Object)
     */
    public Vector getModulePopUpActions(Vector popUpActions, Object context) {
        return null;
    }

    /**
     * @see org.argouml.application.api.ArgoModule#getModuleKey()
     */
    public String getModuleKey() {
        return "module.menu.file.export.xmi";
    }

    /**
     * @see
     * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
        // show a chooser dialog for the file name, only xmi is allowed
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle(Translator.localize(
				       "action.export-project-as-xmi"));
        chooser.setApproveButtonText(Translator.localize(
					     "filechooser.export"));
        chooser.setAcceptAllFileFilterUsed(false);
        chooser.setFileFilter(new XmiFilePersister());

        String fn = Configuration.getString(
                PersistenceManager.KEY_EXPORT_XMI_PATH);
        if (fn.length() > 0) {
            chooser.setSelectedFile(new File(fn));
        }
        
        int result = chooser.showSaveDialog(ProjectBrowser.getInstance());
        if (result == JFileChooser.APPROVE_OPTION) {
            File theFile = chooser.getSelectedFile();
            if (theFile != null) {
                String name = theFile.getName();
                Configuration.setString(
                        PersistenceManager.KEY_EXPORT_XMI_PATH,
                        theFile.getPath());
                if (!name.endsWith(".xmi")) {
                    theFile = new File(theFile.getParent(), name + ".xmi");
                }
            }
            ProjectBrowser.getInstance().trySave(false, theFile);
        }
    }
}
