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

package org.argouml.language.java.ui;

import java.util.Collection;
import java.util.Iterator;

import javax.swing.table.DefaultTableModel;

import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import org.argouml.ui.ArgoDiagram;
import org.argouml.uml.diagram.ui.UMLDiagram;
import org.argouml.uml.generator.Generator2;

/**
 *
 * @author  thn
 */
public class ModelTableModel extends DefaultTableModel implements Runnable {

    /**
     * Creates a new instance of ModelTableModel.
     */
    public ModelTableModel() {
	super(
	      new Object [][] {},
	      new String [] {"Name", "Type", "Package", "Source path"}
	      );
	run();
	//setColumnIdentifiers(new String[] {"Name", "Type", "Package"});
    }

    /**
     * @see javax.swing.table.TableModel#isCellEditable(int, int)
     */
    public boolean isCellEditable(int rowIndex, int columnIndex) {
	return false;
    }

    ////////////////////////////////////////////////////////////////

    /**
     * @see java.lang.Runnable#run()
     */
    public void run() {
	// The following lines should be substituted
        // by the following 2 commented lines.
	// (This is because getting the project still does not seem to work...)
	ArgoDiagram activeDiagram =
	    ProjectManager.getManager().getCurrentProject().getActiveDiagram();
	if (!(activeDiagram instanceof UMLDiagram)) {
	    return;
	}
	// There was a compile error here - hopefully this corrects it properly
	Object diagramNs = ((UMLDiagram) activeDiagram).getNamespace();
	if (diagramNs == null) {
	    return;
	}
	if (Model.getFacade().isANamespace(diagramNs)) {
	    Object ns = diagramNs;
	    while (Model.getFacade().getNamespace(ns) != null) {
		ns = Model.getFacade().getNamespace(ns);
	    }
	    Collection elems =
		Model.getModelManagementHelper().getAllModelElementsOfKind(
		        ns,
		        Model.getMetaTypes().getClassifier());
	    //Project p = ProjectManager.getManager().getCurrentProject();
	    //Collection elems = ModelManagementHelper.getHelper()
            //       .getAllModelElementsOfKind(MClassifier.class);
	    Iterator iter = elems.iterator();
	    while (iter.hasNext()) {
		Object c = iter.next();
		Object[] rowdata =
		    getCodeRelevantClassifierData(c);
		if (rowdata != null) {
		    addRow(rowdata);
		}
	    }
	}
    }


    private static Object[] getCodeRelevantClassifierData(Object cls) {
        String type = null;
        if (Model.getFacade().isAClass(cls)) {
            type = "Class";
        } else if (Model.getFacade().isAInterface(cls)) {
            type = "Interface";
        }
        String codePath = Generator2.getCodePath(cls);
        Object parentNamespace = Model.getFacade().getNamespace(cls);
        if (codePath == null) {
            codePath = Generator2.getCodePath(parentNamespace);
        }
        String packagePath = Model.getFacade().getName(parentNamespace);
        parentNamespace = Model.getFacade().getNamespace(parentNamespace);
        while (parentNamespace != null) {
            if (codePath == null) {
        	codePath = Generator2.getCodePath(parentNamespace);
            }
            // ommit root package name; it's the model's root
            if (Model.getFacade().getNamespace(parentNamespace) != null) {
        	packagePath =
        	    Model.getFacade().getName(parentNamespace)
        	    + "." + packagePath;
            }
            parentNamespace = Model.getFacade().getNamespace(parentNamespace);
        }
        if (codePath != null && codePath.length() > 0) {
            return new Object [] {
                Model.getFacade().getName(cls),
                type,
                packagePath,
                codePath,
            };
        }
        return null;
    }
}
