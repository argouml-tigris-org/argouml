// Copyright (c) 1996-01 The Regents of the University of California. All
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

package org.argouml.language.php.generator.ui;

import org.argouml.ui.*;
import org.argouml.uml.ui.*;
import org.argouml.uml.diagram.static_structure.ui.*;
import org.argouml.uml.generator.ui.*;
import org.argouml.application.api.*;
import ru.novosoft.uml.foundation.core.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class ActionGenerateAllPHP extends UMLAction implements PluggableMenu {

    ////////////////////////////////////////////////////////////////
    // static variables

    public static ActionGenerateAllPHP SINGLETON = new ActionGenerateAllPHP(); 

    private static JMenuItem _menuItem = null;

 
    ////////////////////////////////////////////////////////////////
    // constructors

    protected ActionGenerateAllPHP() {
	super("Generate All Classes as PHP", NO_ICON);
    }


    ////////////////////////////////////////////////////////////////
    // main methods

    public void actionPerformed(ActionEvent ae) {
	ProjectBrowser pb = ProjectBrowser.TheInstance;
	Object target = pb.getTarget();
	if (!(target instanceof UMLClassDiagram)) return;
	UMLClassDiagram d = (UMLClassDiagram) target;
	Vector classes = new Vector();
	Vector nodes = d.getNodes();
	java.util.Enumeration enum = nodes.elements();
	while (enum.hasMoreElements()) {
	    Object owner = enum.nextElement();
	    if (!(owner instanceof MClass) && !(owner instanceof MInterface))
		continue;
	    MClassifier cls = (MClassifier) owner;
	    String name = cls.getName();
	    if (name == null || name.length() == 0) continue;
	    classes.addElement(cls);
	}
	ClassGenerationDialogPHP cgd = new ClassGenerationDialogPHP(classes);
	cgd.show();
    }

    public boolean shouldBeEnabled() {
	ProjectBrowser pb = ProjectBrowser.TheInstance;
	Object target = pb.getTarget();
	return super.shouldBeEnabled() && target instanceof UMLClassDiagram;
    }

    public void setModuleEnabled(boolean v) { }

    public boolean initializeModule() {
        boolean initialized = false;
	// Advertise a little
	Argo.log.info ("+-----------------------------+");
	Argo.log.info ("| PHP plugin testing enabled! |");
	Argo.log.info ("+-----------------------------+");

        return true;
    }

    public Object[] buildContext(JMenuItem a, String b) {
        return new Object[] { a, b };
    }

    public boolean inContext(Object[] o) {
	return (o.length > 1) && (o[0] instanceof JMenu) && "Generate".equals(o[1]);
    }

    public boolean isModuleEnabled() { return true; }

    public Vector getModulePopUpActions(Vector v, Object o) { return null; }

    public boolean shutdownModule() { return true; }

    public String getModuleName() { return "ActionGenerateAllPHP"; }

    public String getModuleDescription() { return "Menu Item for PHP code generation"; }

    public String getModuleAuthor() { return "Andreas Rueckert"; }

    public String getModuleVersion() { return "0.9.5"; }

    public String getModuleKey() { return "module.menu.generation.php"; }

    public JMenuItem getMenuItem(JMenuItem mi, String s) {
        if (_menuItem == null) {
            _menuItem = new JMenuItem(Argo.localize(Argo.MENU_BUNDLE,
                                                    "Generate classes as PHP..."));
            _menuItem.addActionListener(this);
        }
        return _menuItem;
    }                                  

} /* end class ActionGenerateAllPHP */



