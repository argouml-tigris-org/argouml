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

package org.argouml.uml.ui;

import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.JFileChooser;

import org.argouml.i18n.Translator;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.ModelFacade;
import org.argouml.ui.NavigatorPane;
import org.argouml.ui.ProjectBrowser;
import org.argouml.util.osdep.OsUtil;



/** Action to choose and set source path for model elements
 * @stereotype singleton
 */
public class ActionSetSourcePath extends UMLAction {

    ////////////////////////////////////////////////////////////////
    // static variables

    public static ActionSetSourcePath SINGLETON = new ActionSetSourcePath();

    public static final String separator = "/";
    //System.getProperty("file.separator");

    ////////////////////////////////////////////////////////////////
    // constructors

    protected ActionSetSourcePath() {
	super("action.set-source-path", NO_ICON);
    }


    ////////////////////////////////////////////////////////////////
    // main methods

    public void actionPerformed(ActionEvent e) {
	File f = getNewDirectory();
	if (f != null) {
	    Object obj = NavigatorPane.getInstance().getSelectedObject();
	    if (ModelFacade.isAModelElement(obj)) {
		ModelFacade.setTaggedValue(obj, "src_path",f.getPath());
	    }
	}
    }

    protected File getNewDirectory() {
	Project p = ProjectManager.getManager().getCurrentProject();
	Object obj = NavigatorPane.getInstance().getSelectedObject();
	String name = null;
	String type = null;
	String path = null;
	if (ModelFacade.isAModelElement(obj)) {
	    name = ModelFacade.getName(obj);
	    path = (String)ModelFacade.getTaggedValue(obj, "src_path");
	    if (ModelFacade.isAPackage(obj))
		type = "Package";
	    else if (ModelFacade.isAClass(obj))
		type = "Class";
	    if (ModelFacade.isAInterface(obj))
		type = "Interface";
	} else {
	    return null;
	}

	JFileChooser chooser = null;
	File f = null;
	if (path != null) {
	    f = new File(path);
	}
	if ((f != null) && (f.getPath().length() > 0)) {
	    chooser  = OsUtil.getFileChooser(f.getPath());
	}
	if (chooser == null) {
	    chooser  = OsUtil.getFileChooser();
	}
	if (f != null) {
	    chooser.setSelectedFile(f);
	}

	String sChooserTitle =
	    Translator.localize("CoreMenu", "action.set-source-path");
	if (type != null)
	    sChooserTitle += ' ' + type;
	if (name != null)
	    sChooserTitle += ' ' + name;
	chooser.setDialogTitle(sChooserTitle);
	chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

	int retval = chooser.showDialog(ProjectBrowser.getInstance(), "OK");
	if (retval == JFileChooser.APPROVE_OPTION) {
	    return chooser.getSelectedFile();
	} else {
	    return null;
	}
    }

    public boolean shouldBeEnabled() {
	return true;
    }
} /* end class ActionSetSourcePath */