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

package org.argouml.uml.reveng.classfile.ui;

import org.argouml.kernel.*;
import org.argouml.ui.*;
import org.argouml.uml.ui.*;
import org.argouml.util.*;
import org.argouml.uml.reveng.*;
import org.argouml.uml.reveng.classfile.*;
import org.argouml.uml.diagram.static_structure.ui.*;
import org.argouml.application.api.*;
import org.tigris.gef.base.*;
// import ru.novosoft.uml.foundation.core.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;

public class ActionImportFromClassfiles extends UMLAction  { //implements PluggableMenu {

    /////////////////////////////////////////////////////////
    // Constants

    // The file separator of this system.
    public final String _separator = System.getProperty("file.separator");


    ////////////////////////////////////////////////////////////////
    // static variables

    /**
     * This module implements the Singleton pattern, and this is the instance.
     */
    public static ActionImportFromClassfiles SINGLETON = new ActionImportFromClassfiles(); 

    /**
     * The menu item for this module.
     */
    private static JMenuItem _menuItem = null;

    ////////////////////////////////////////////////////////////////
    // constructors

    protected ActionImportFromClassfiles() {
	super("Java classfiles...", NO_ICON);
    }


    ////////////////////////////////////////////////////////////////
    // main methods

    public void actionPerformed(ActionEvent ae) {
	ProjectBrowser pb = ProjectBrowser.TheInstance;
	Object target = pb.getTarget();
	if (!(target instanceof UMLClassDiagram)) return;

        try {
            String directory = Globals.getLastDirectory();
            JFileChooser chooser = new JFileChooser(directory);

            if (chooser == null) chooser = new JFileChooser();

            chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            chooser.setDialogTitle("Import Java classfiles");

	    // Add appropriate file filters for this dialog.
	    ClassFileFilter filter = new ClassFileFilter();
	    chooser.addChoosableFileFilter(filter);
            chooser.setFileFilter(filter);      

            chooser.setAccessory(ClassfileImport.getInstance().getConfigPanel());

            int retval = chooser.showOpenDialog(pb);

            if (retval == 0) {
                File theFile = chooser.getSelectedFile();
                if (theFile != null) {
                    String path = chooser.getSelectedFile().getParent();
                    String filename = chooser.getSelectedFile().getName();
                    filename = path + _separator + filename;
                    Globals.setLastDirectory(path);
                    if (filename != null) {
                        pb.showStatus("Parsing " + path + filename + "...");
                        Project p = ProjectManager.getManager().
                            getCurrentProject();
                        ClassfileImport.getInstance().startImport(p, theFile);
                        p.postLoad();

                        // Check if any diagrams where modified and the project
                        // should be saved before exiting.
                        if(ClassfileImport.getInstance().needsSave()) {
                            p.setNeedsSave(true);
                        }

                        ProjectManager.getManager().setCurrentProject(p);
                        pb.showStatus("Parsed " + filename);
                        return;
                    }
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }               
    }

    /**
     * En- or disable the module.
     *
     * @param flag A flag to indicate if the module is enable.
     */
    public void setModuleEnabled(boolean flag) { }

    public boolean initializeModule() {

	// Advertise a little
	Argo.log.info ("+---------------------------------------+");
	Argo.log.info ("| Java classfile import module enabled! |");
	Argo.log.info ("+---------------------------------------+");

        return true;
    }

    public Object[] buildContext(JMenuItem a, String b) {
        return new Object[] { a, b };
    }

    public boolean inContext(Object[] o) {

	return (o.length > 1) && (o[0] instanceof JMenu) && "File:Import".equals(o[1]);
    }

    /**
     * Check, if the module is enabled.
     *
     * @return true, if the module is enabled,
     *         false otherwise.
     */
    public boolean isModuleEnabled() { return true; }

    /**
     * Get the popup actions of this module.
     *
     * @param v
     * @param o The object that holds the popup actions.
     *
     * @return A vector with the popup actions.
     */
    public Vector getModulePopUpActions(Vector v, Object o) { return null; }

    /**
     * Deactivate the module properly.
     *
     * @return true, if the module was properly deactivated,
     *         false otherwise.
     */
    public boolean shutdownModule() { return true; }

    /**
     * Get the name of this module.
     *
     * @return The name of this module as a String object.
     */
    public String getModuleName() { return "ActionImportFromClassfiles"; }

    /**
     * Get a description of this module.
     *
     * @return A description of this module as a String object.
     */
    public String getModuleDescription() { return "Menu Item for import from Java classfiles"; }

    /**
     * Get the author of this module.
     *
     * @return The author name as a String object.
     */
    public String getModuleAuthor() { return "Andreas Rueckert <a_rueckert@gmx.net>"; }

    /**
     * Get the version of this module.
     *
     * @return The version of this module as a String object.
     */
    public String getModuleVersion() { return "0.0.1"; }

    /**
     * Get this module's key.
     *
     * @return The key of this module as a String object.
     */
    public String getModuleKey() { return "module.menu.file.reveng_classfile"; }

    /**
     * Get the menu item for this module.
     *
     * @return The menu item for this module.
     */
    public JMenuItem getMenuItem(Object[] context) {
        if (_menuItem == null) {
            _menuItem = new JMenuItem(Argo.localize(Argo.MENU_BUNDLE, "Java classfiles..."));
            _menuItem.addActionListener(this);
        }
        return _menuItem;
    }                                  

    public JMenuItem getMenuItem(JMenuItem parentMenuItem, String menuType) {
        return getMenuItem(null);
    }


} /* end class ActionImportFromClassfiles */



