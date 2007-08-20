// $Id:SourcePathTableModel.java 10734 2006-06-11 15:43:58Z mvw $
// Copyright (c) 2004-2006 The Regents of the University of California. All
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

import java.io.File;
import java.util.Collection;
import java.util.Iterator;

import javax.swing.table.DefaultTableModel;

import org.apache.log4j.Logger;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;

/**
 * The table model for source path settings. This class contains functionality
 * to load the source path settings from the model.
 */
class SourcePathTableModel extends DefaultTableModel {

    /**
     * Index of the column where model element objects are placed.
     */
    static final int MODEL_ELEMENT_COLUMN = 0;
    /**
     * Index of the column where the model element names are placed.
     */
    static final int NAME_COLUMN = 1;
    /**
     * Index of the column where the types of the model elements are placed.
     */
    static final int TYPE_COLUMN = 2;
    /**
     * Index of the column where the source paths for the model elements are
     * placed.
     */
    static final int SOURCE_PATH_COLUMN = 3;

    /**
     * Logger.
     */
    private static final Logger LOG =
        Logger.getLogger(SourcePathTableModel.class);

    /**
     * Creates a new instance of SourcePathTableModel.
     */
    public SourcePathTableModel(SourcePathController srcPathCtrl) {
        super(new Object[][] {
        }, new String[] {
            " ", Translator.localize("misc.name"),
            Translator.localize("misc.type"),
            Translator.localize("misc.source-path"),
        });
        String strModel = Translator.localize("misc.model");
        String strPackage = Translator.localize("misc.package");
        String strClass = Translator.localize("misc.class");
        String strInterface = Translator.localize("misc.interface");

        Collection elems = srcPathCtrl.getAllModelElementsWithSourcePath();

        Iterator iter = elems.iterator();
        while (iter.hasNext()) {
            Object me = iter.next();
            File path = srcPathCtrl.getSourcePath(me);
            if (path != null) {
                String type = "";
                String name = Model.getFacade().getName(me);
                if (Model.getFacade().isAModel(me)) {
                    type = strModel;
                } else if (Model.getFacade().isAPackage(me)) {
                    type = strPackage;
                    Object parent = Model.getFacade().getNamespace(me);
                    while (parent != null) {
                        // ommit root package name; it's the model's root
                        if (Model.getFacade().getNamespace(parent) != null) {
                            name =
                                Model.getFacade().getName(parent) + "." + name;
                        }
                        parent = Model.getFacade().getNamespace(parent);
                    }
                } else if (Model.getFacade().isAClass(me)) {
                    type = strClass;
                } else if (Model.getFacade().isAInterface(me)) {
                    type = strInterface;
                } else {
                    LOG.warn("Can't assign a type to this model element: "
                            + me);
                }
                addRow(new Object[] {
                    me, name, type, path.toString(),
                });
            } else {
                LOG.warn("Unexpected: the source path for " + me + " is null!");
            }
        }
    }

    /**
     * The only editable cells are the ones in the source path column.
     * @see javax.swing.table.DefaultTableModel#isCellEditable(int, int)
     */
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == SOURCE_PATH_COLUMN;
    }

    /**
     * For the specified row, get the model element (ME).
     * @param rowIndex the row index where the ME data is located
     * @return the ME
     */
    public Object getModelElement(int rowIndex) {
        return getValueAt(rowIndex, MODEL_ELEMENT_COLUMN);
    }

    /**
     * For the specified row, get the model element (ME) name.
     * @param rowIndex the row index where the ME data is located
     * @return the ME name
     */
    public String getMEName(int rowIndex) {
        return (String) getValueAt(rowIndex, NAME_COLUMN);
    }

    /**
     * For the specified row, get the ME type.
     * @param rowIndex the row index where the ME data is located
     * @return the String representation of the ME type
     */
    public String getMEType(int rowIndex) {
        return (String) getValueAt(rowIndex, TYPE_COLUMN);
    }

    /**
     * For the specified row get the, get the source path.
     * @param rowIndex the row index where the ME data is located
     * @return the String representation of the ME source path
     */
    public String getMESourcePath(int rowIndex) {
        return (String) getValueAt(rowIndex, SOURCE_PATH_COLUMN);
    }
}
