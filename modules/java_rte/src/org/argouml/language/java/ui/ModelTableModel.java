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

package org.argouml.language.java.ui;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;

import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.foundation.extension_mechanisms.MTaggedValue;

import org.argouml.kernel.*;
import org.argouml.uml.generator.Generator;
import org.argouml.model.uml.modelmanagement.ModelManagementHelper;

/**
 *
 * @author  thn
 */
public class ModelTableModel extends DefaultTableModel implements Runnable {

    Object _root = null;
    JSplitPane _mainPane;
    Vector _results = new Vector();

    /** Creates a new instance of ModelTableModel */
    public ModelTableModel() {
	super(
	      new Object [][] {},
	      new String [] {"Name", "Type", "Package", "Source path"}
	      );
	run();
	//setColumnIdentifiers(new String[] {"Name", "Type", "Package"});
    }

    public boolean isCellEditable(int rowIndex, int columnIndex) {
	return false;
    }

    ////////////////////////////////////////////////////////////////

    public void run() {
	// The following lines should be substituted by the following 2 commented lines.
	// (This is because getting the project still does not seem to work...)
	org.argouml.ui.ProjectBrowser pb = org.argouml.ui.ProjectBrowser.TheInstance;
	org.argouml.ui.ArgoDiagram activeDiagram = pb.getActiveDiagram();
	if (!(activeDiagram instanceof org.argouml.uml.diagram.ui.UMLDiagram)) return;
	ru.novosoft.uml.foundation.core.MNamespace ns = ((org.argouml.uml.diagram.ui.UMLDiagram) activeDiagram).getNamespace();
	if (ns == null) return;
	while (ns.getNamespace() != null) ns = ns.getNamespace();
	Collection elems = ModelManagementHelper.getHelper().getAllModelElementsOfKind(ns, MClassifier.class);
	//Project p = ProjectManager.getManager().getCurrentProject();
	//Collection elems = ModelManagementHelper.getHelper().getAllModelElementsOfKind(MClassifier.class);
	Iterator iter = elems.iterator();
	while (iter.hasNext()) {
	    Object c = iter.next();
	    Object[] rowdata = getCodeRelevantClassifierData((MClassifier) c);
	    if (rowdata != null) {
		addRow(rowdata);
	    }
	}
    }


    private static Object[] getCodeRelevantClassifierData(MClassifier cls) {
	String type = null;
	if (cls instanceof MClass) {
	    type = "Class";
	}
	else if (cls instanceof MInterface) {
	    type = "Interface";
	}
	String codePath = Generator.getCodePath(cls);
	MNamespace parent = cls.getNamespace();
	if (codePath == null) {
	    codePath = Generator.getCodePath(parent);
	}
	String packagePath = parent.getName();
	parent = parent.getNamespace();
	while (parent != null) {
	    if (codePath == null) {
		codePath = Generator.getCodePath(parent);
	    }
	    // ommit root package name; it's the model's root
	    if (parent.getNamespace() != null) {
		packagePath = parent.getName() + "." + packagePath;
	    }
	    parent = parent.getNamespace();
	}
	if (codePath != null && codePath.length() > 0) {
	    return new Object [] {cls.getName(), type, packagePath, codePath};
	} else {
	    return null;
	}
    }
}
