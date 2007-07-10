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

package org.argouml.uml;

import java.util.Vector;

import javax.swing.table.AbstractTableModel;

import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.uml.diagram.ui.UMLDiagram;
import org.tigris.gef.base.Diagram;

/**
 * TMResults (Table Model Results) implements a default table model
 * which is used by Find and Goto Operations in order to display search
 * results. It defines a default table model with columns and can
 * resolve found objects to strings.
 */
public class TMResults extends AbstractTableModel {

    private Vector rowObjects;
    private Vector diagrams;
    private boolean showInDiagramColumn;

    /**
     * The constructor.
     *
     */
    public TMResults() {
        showInDiagramColumn = true;
    }

    /**
     * The constructor.
     *
     * @param showTheInDiagramColumn true if the "In Diagram" column
     *                               should be shown
     */
    public TMResults(boolean showTheInDiagramColumn) {
        showInDiagramColumn = showTheInDiagramColumn;
    }

    ////////////////
    // accessors

    /**
     * @param results the row objects
     * @param theDiagrams the diagrams
     */
    public void setTarget(Vector results, Vector theDiagrams) {
        rowObjects = results;
        diagrams = theDiagrams;
        fireTableStructureChanged();
    }

    ////////////////
    // TableModel implementation

    /*
     * @see javax.swing.table.TableModel#getColumnCount()
     */
    public int getColumnCount() {
        return showInDiagramColumn ? 4 : 3;
    }

    /*
     * @see javax.swing.table.TableModel#getRowCount()
     */
    public int getRowCount() {
        if (rowObjects == null) {
            return 0;
        }
        return rowObjects.size();
    }

    /*
     * @see javax.swing.table.TableModel#getColumnName(int)
     */
    public String getColumnName(int c) {
        if (c == 0) {
            return Translator.localize("dialog.find.column-name.type");
        }
        if (c == 1) {
            return Translator.localize("dialog.find.column-name.name");
        }
        if (c == 2) {
            return Translator.localize(showInDiagramColumn
                    ? "dialog.find.column-name.in-diagram"
                    : "dialog.find.column-name.description");
        }
        if (c == 3) {
            return Translator.localize("dialog.find.column-name.description");
        }
        return "XXX";
    }

    /*
     * @see javax.swing.table.TableModel#getColumnClass(int)
     */
    public Class getColumnClass(int c) {
        return String.class;
    }

    /*
     * @see javax.swing.table.TableModel#isCellEditable(int, int)
     */
    public boolean isCellEditable(int row, int col) {
        return false;
    }

    /*
     * @see javax.swing.table.TableModel#getValueAt(int, int)
     */
    public Object getValueAt(int row, int col) {
        if (row < 0 || row >= rowObjects.size()) {
            return "bad row!";
        }
        if (col < 0 || col >= (showInDiagramColumn ? 4 : 3)) {
            return "bad col!";
        }
        Object rowObj = rowObjects.elementAt(row);
        if (rowObj instanceof Diagram) {
            Diagram d = (Diagram) rowObj;
            switch (col) {
	    case 0 : // the name of this type of diagram
		if (d instanceof UMLDiagram) {
                    return ((UMLDiagram) d).getLabelName();
                }
		return null;
	    case 1 : // the name of this instance of diagram
		return d.getName();
	    case 2 : // "N/A" or "x nodes and x edges"
		return showInDiagramColumn
		    ? Translator.localize("dialog.find.not-applicable")
                    : countNodesAndEdges(d);
	    case 3 : // "x nodes and x edges"
		return countNodesAndEdges(d);
	    default:
            }
        }
        if (Model.getFacade().isAModelElement(rowObj)) {
            Diagram d = null;
            if (diagrams != null) {
                d = (Diagram) diagrams.elementAt(row);
            }
            switch (col) {
	    case 0 : // the name of this type of ModelElement
		return Model.getFacade().getUMLClassName(rowObj);
	    case 1 : // the name of this instance of ModelElement
		return Model.getFacade().getName(rowObj);
	    case 2 : // the name of the parent diagram instance
		return (d == null)
		    ? Translator.localize("dialog.find.not-applicable")
                    : d.getName();
	    case 3 : // TODO: implement this - show some documentation?
		return "docs";
	    default:
            }
        }
        switch (col) {
	case 0 : // the name of this type of Object
            if (rowObj == null) {
                return "";
            }
	    String clsName = rowObj.getClass().getName();
	    int lastDot = clsName.lastIndexOf(".");
	    return clsName.substring(lastDot + 1);
	case 1 :
	    return "";
	case 2 :
	    return "??";
	case 3 :
	    return "docs";
	default:
        }
        return "unknown!";
    }

    /**
     * @param d the diagram to count the nodes and edges of
     * @return a string which says it all
     */
    private Object countNodesAndEdges(Diagram d) {
        int numNodes = d.getNodes().size();
        int numEdges = d.getEdges().size();
        Object[] msgArgs = {new Integer(numNodes),
                            new Integer(numEdges),
	};
        return Translator.messageFormat("dialog.nodes-and-edges", msgArgs);
    }

    /*
     * @see javax.swing.table.TableModel#setValueAt(java.lang.Object, int, int)
     */
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
    }

    /**
     * The UID.
     */
    private static final long serialVersionUID = -1444599676429024575L;
} /* end class TMResults */
