// $Id$
// Copyright (c) 1996-99 The Regents of the University of California. All
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

package org.argouml.uml.diagram.static_structure.ui;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.beans.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.Document;
import javax.swing.plaf.metal.MetalLookAndFeel;

import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.foundation.data_types.*;

import org.tigris.gef.ui.*;

import org.argouml.ui.*;
import org.argouml.uml.*;
import org.argouml.uml.diagram.static_structure.*;

public class TablePanelUMLClassDiagram extends TablePanel {

    ////////////////////////////////////////////////////////////////
    // instance variables

    JSortedTable _table2 = new JSortedTable();
    JSortedTable _table3 = new JSortedTable();
    TableModelComposite _tableModelOper = new TableModelOper();
    TableModelComposite _tableModelAttr = new TableModelAttr();
    JPanel _south = new JPanel();
    JScrollPane _sp2;
    JScrollPane _sp3;

    ////////////////////////////////////////////////////////////////
    // constructors

    public TablePanelUMLClassDiagram() {
	super("UMLClassDiagram");

	_south.setLayout(new GridLayout(1, 2, 5, 5));

	_sp2 = new JScrollPane(_table2,
			       JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
			       JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	_sp3 = new JScrollPane(_table3,
			       JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
			       JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

	_sp1.setPreferredSize(new Dimension(300, 300));
	_sp1.setSize(new Dimension(300, 300));

	_sp2.setPreferredSize(new Dimension(300, 300));
	_sp2.setSize(new Dimension(300, 300));

	_sp3.setPreferredSize(new Dimension(300, 300));
	_sp3.setSize(new Dimension(300, 300));

	_content.setPreferredSize(new Dimension(300, 600));
	_content.setSize(new Dimension(300, 600));

	JPanel sp2W = new JPanel();
	sp2W.setLayout(new BorderLayout());
	sp2W.add(new JLabel("Operations of selected class:"), BorderLayout.NORTH);
	sp2W.add(_sp2, BorderLayout.CENTER);

	JPanel sp3W = new JPanel();
	sp3W.setLayout(new BorderLayout());
	sp3W.add(new JLabel("Attributes of selected class:"), BorderLayout.NORTH);
	sp3W.add(_sp3, BorderLayout.CENTER);

	_south.add(sp3W);
	_south.add(sp2W);

	_content.add(_south, BorderLayout.SOUTH);

	Font labelFont = MetalLookAndFeel.getSubTextFont();
	_table2.setFont(labelFont);
	_table3.setFont(labelFont);
	setEditors(_table2);
	setEditors(_table3);

	_table2.getSelectionModel().addListSelectionListener(this);
	_table3.getSelectionModel().addListSelectionListener(this);
    }

    /////////////////////////////////////////////////////////////////
    // ListSelectionListener implemention

    public void valueChanged(ListSelectionEvent lse) {
	super.valueChanged(lse);
	if (lse.getValueIsAdjusting()) return;
	Object src = lse.getSource();
	_table2.sizeColumnsToFit(-1);
	_table3.sizeColumnsToFit(-1);
	if (src == _table2.getSelectionModel()) {
	    int row = lse.getFirstIndex();
	    if (_tableModelOper != null) {
		Vector rowObjects = _tableModelOper.getRowObjects();
		if (row >= 0 && row < rowObjects.size()) {
		    Object sel = rowObjects.elementAt(row);
		    objectSelected(sel);
		    return;
		}
	    }
	}
	if (src == _table3.getSelectionModel()) {
	    int row = lse.getFirstIndex();
	    if (_tableModelAttr != null) {
		Vector rowObjects = _tableModelAttr.getRowObjects();
		if (row >= 0 && row < rowObjects.size()) {
		    Object sel = rowObjects.elementAt(row);
		    objectSelected(sel);
		    return;
		}
	    }
	}
    }

    public void objectSelected(Object sel) {
	super.objectSelected(sel);
	if (sel instanceof MClassifier) {
	    _tableModelOper.setTarget((MClassifier) sel);
	    _tableModelAttr.setTarget((MClassifier) sel);
	}
    }



    public void setTablePerspective() {
	super.setTablePerspective();
	if (_tableModel instanceof TableModelClassByProps) {
	    _table2.setModel(_tableModelOper);
	    _table3.setModel(_tableModelAttr);
	    _south.setVisible(true);
	}
	else {
	    _south.setVisible(false);
	}
	validate();
    }

    public void initTableModels() {
	_tableModels.addElement(new TableModelClassByProps());
	_tableModels.addElement(new TableModelAssocByProps());
	//_tableModels.addElement(new TableModelClassByClass());
    }

} /* end class TablePanelUMLClassDiagram */
