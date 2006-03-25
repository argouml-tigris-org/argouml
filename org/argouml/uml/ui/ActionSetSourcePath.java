// $Id$
// Copyright (c) 1996-2006 The Regents of the University of California. All
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
import org.argouml.model.Model;
import org.argouml.ui.ProjectBrowser;
import org.argouml.ui.targetmanager.TargetManager;


/**
 * Action to choose and set source path for model elements.
 */
public class ActionSetSourcePath extends UMLAction {

    ////////////////////////////////////////////////////////////////
    // constructors

    /**
     * The constructor.
     */
    public ActionSetSourcePath() {
	super("action.set-source-path", false, NO_ICON);
    }

    ////////////////////////////////////////////////////////////////
    // main methods

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
	File f = getNewDirectory();
	if (f != null) {
	    Object obj = TargetManager.getInstance().getTarget();
	    if (Model.getFacade().isAModelElement(obj)) {
		Model.getCoreHelper().setTaggedValue(obj, "src_path",
		        f.getPath());
	    }
	}
    }

    /**
     * @return the new source path directory
     */
    protected File getNewDirectory() {
	Object obj = TargetManager.getInstance().getTarget();
	String name = null;
	String type = null;
	String path = null;
	if (Model.getFacade().isAModelElement(obj)) {
	    name = Model.getFacade().getName(obj);
            Object tv = Model.getFacade().getTaggedValue(obj, "src_path");
            if (tv != null) {
                path = Model.getFacade().getValueOfTag(tv);
            }
	    if (Model.getFacade().isAPackage(obj)) {
                type = "Package";
            } else if (Model.getFacade().isAClass(obj)) {
                type = "Class";
            }
	    if (Model.getFacade().isAInterface(obj)) {
                type = "Interface";
            }
	} else {
	    return null;
	}

	JFileChooser chooser = null;
	File f = null;
	if (path != null) {
	    f = new File(path);
	}
	if ((f != null) && (f.getPath().length() > 0)) {
	    chooser = new JFileChooser(f.getPath());
	}
	if (chooser == null) {
	    chooser = new JFileChooser();
	}
	if (f != null) {
	    chooser.setSelectedFile(f);
	}

	String sChooserTitle =
	    Translator.localize("action.set-source-path");
	if (type != null) {
            sChooserTitle += ' ' + type;
        }
	if (name != null) {
            sChooserTitle += ' ' + name;
        }
	chooser.setDialogTitle(sChooserTitle);
	chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

	int retval =
            chooser.showDialog(ProjectBrowser.getInstance(),
                    Translator.localize("dialog.button.ok"));
	if (retval == JFileChooser.APPROVE_OPTION) {
	    return chooser.getSelectedFile();
	} else {
	    return null;
	}
    }

    /**
     * The UID.
     */
    private static final long serialVersionUID = -6455209886706784094L;
} /* end class ActionSetSourcePath */
