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


// File: TablePanelUMLDeploymentDiagram.java
// Classes: TablePanelUMLDeploymentDiagram
// Original Author: 5eichler@informatik.uni-hamburg.de
// $Id$

package org.argouml.uml.diagram.deployment.ui;

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
import ru.novosoft.uml.behavior.common_behavior.*;

import org.tigris.gef.ui.*;

import org.argouml.uml.*;
import org.argouml.ui.*;
import org.argouml.uml.diagram.deployment.*;


public class TablePanelUMLDeploymentDiagram extends TablePanel {

    ////////////////////////////////////////////////////////////////
    // instance variables

    JSortedTable _table2 = new JSortedTable();
    JSortedTable _table3 = new JSortedTable();
    JSortedTable _table4 = new JSortedTable();
    JSortedTable _table5 = new JSortedTable();
    JSortedTable _table6 = new JSortedTable();
    JPanel sp2W, sp3W, sp4W, sp5W, sp6W;
    TableModelComposite _tableModelClass_in_DeplByProps = new TableModelClass_in_DeplByProps();
    TableModelComposite _tableModelInterface_in_DeplByProps = new TableModelInterface_in_DeplByProps();
    TableModelComposite _tableModelObjectByProps = new TableModelObjectByProps();
    TableModelComposite _tableModelComponentByProps = new TableModelComponentByProps();
    TableModelComposite _tableModelCompInstanceByProps = new TableModelCompInstanceByProps();
    JPanel _south = new JPanel();
    JScrollPane _sp2, _sp3, _sp4, _sp5, _sp6;

    ////////////////////////////////////////////////////////////////
    // constructors

    public TablePanelUMLDeploymentDiagram() {
	super("UMLDeploymentDiagram");    

	_south.setLayout(new GridLayout(2, 2, 5, 5));

	_sp2 = new JScrollPane(_table2,
			       JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
			       JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	_sp3 = new JScrollPane(_table3,
			       JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
			       JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	_sp4 = new JScrollPane(_table4,
			       JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
			       JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	_sp5 = new JScrollPane(_table5,
			       JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
			       JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	_sp6 = new JScrollPane(_table6,
			       JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
			       JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

	_sp1.setPreferredSize(new Dimension(300, 300));
	_sp1.setSize(new Dimension(300, 300));

	_sp2.setPreferredSize(new Dimension(300, 100));
	_sp2.setSize(new Dimension(300, 100));

	_sp3.setPreferredSize(new Dimension(300, 100));
	_sp3.setSize(new Dimension(300, 100));

	_sp4.setPreferredSize(new Dimension(300, 100));
	_sp4.setSize(new Dimension(300, 100));

	_sp5.setPreferredSize(new Dimension(300, 100));
	_sp5.setSize(new Dimension(300, 100));

	_sp6.setPreferredSize(new Dimension(300, 100));
	_sp6.setSize(new Dimension(300, 100));

	_content.setPreferredSize(new Dimension(300, 600));
	_content.setSize(new Dimension(300, 600));

	sp2W = new JPanel();
	sp2W.setLayout(new BorderLayout());
	sp2W.add(new JLabel("Classes of selected component:"), BorderLayout.NORTH);
	sp2W.add(_sp2, BorderLayout.CENTER);
	sp3W = new JPanel();
	sp3W.setLayout(new BorderLayout());
	sp3W.add(new JLabel("Interfaces of selected component:"), BorderLayout.NORTH);
	sp3W.add(_sp3, BorderLayout.CENTER);
	sp4W = new JPanel();
	sp4W.setLayout(new BorderLayout());
	sp4W.add(new JLabel("Objects of selected component(-instance):"), BorderLayout.NORTH);
	sp4W.add(_sp4, BorderLayout.CENTER);
	sp5W = new JPanel();
	sp5W.setLayout(new BorderLayout());
	sp5W.add(new JLabel("Components of selected node:"), BorderLayout.NORTH);
	sp5W.add(_sp5, BorderLayout.CENTER);
	sp6W = new JPanel();
	sp6W.setLayout(new BorderLayout());
	sp6W.add(new JLabel("Component-instances of selected node-instance:"), BorderLayout.NORTH);
	sp6W.add(_sp6, BorderLayout.CENTER);

	_south.add(sp2W);
	_south.add(sp3W);
	_south.add(sp4W);
	_south.add(sp5W);
	_south.add(sp6W);

	_content.add(_south, BorderLayout.SOUTH);

	Font labelFont = MetalLookAndFeel.getSubTextFont();
	_table2.setFont(labelFont);
	_table3.setFont(labelFont);
	_table4.setFont(labelFont);
	_table5.setFont(labelFont);
	_table6.setFont(labelFont);
	setEditors(_table2);
	setEditors(_table3);
	setEditors(_table4);
	setEditors(_table5);
	setEditors(_table6);

	_table2.getSelectionModel().addListSelectionListener(this);
	_table3.getSelectionModel().addListSelectionListener(this);
	_table4.getSelectionModel().addListSelectionListener(this);
	_table5.getSelectionModel().addListSelectionListener(this);
	_table6.getSelectionModel().addListSelectionListener(this);
    }

    /////////////////////////////////////////////////////////////////
    // ListSelectionListener implemention

    public void valueChanged(ListSelectionEvent lse) {
	super.valueChanged(lse);
	if (lse.getValueIsAdjusting()) return;
	Object src = lse.getSource();
	_table2.sizeColumnsToFit(-1);
	_table3.sizeColumnsToFit(-1);
	_table4.sizeColumnsToFit(-1);
	_table5.sizeColumnsToFit(-1);
	_table6.sizeColumnsToFit(-1);
	if (src == _table2.getSelectionModel()) {
	    int row = lse.getFirstIndex();
	    if (_tableModelClass_in_DeplByProps != null) {
		Vector rowObjects = _tableModelClass_in_DeplByProps.getRowObjects();
		if (row >= 0 && row < rowObjects.size()) {
		    Object sel = rowObjects.elementAt(row);
		    objectSelected(sel);
		    return;
		}
	    }
	}
	if (src == _table3.getSelectionModel()) {
	    int row = lse.getFirstIndex();
	    if (_tableModelInterface_in_DeplByProps != null) {
		Vector rowObjects = _tableModelInterface_in_DeplByProps.getRowObjects();
		if (row >= 0 && row < rowObjects.size()) {
		    Object sel = rowObjects.elementAt(row);
		    objectSelected(sel);
		    return;
		}
	    }
	}
	if (src == _table4.getSelectionModel()) {
	    int row = lse.getFirstIndex();
	    if (_tableModelObjectByProps != null) {
		Vector rowObjects = _tableModelObjectByProps.getRowObjects();
		if (row >= 0 && row < rowObjects.size()) {
		    Object sel = rowObjects.elementAt(row);
		    objectSelected(sel);
		    return;
		}
	    }
	}
	if (src == _table5.getSelectionModel()) {
	    int row = lse.getFirstIndex();
	    if (_tableModelComponentByProps != null) {
		Vector rowObjects = _tableModelComponentByProps.getRowObjects();
		if (row >= 0 && row < rowObjects.size()) {
		    Object sel = rowObjects.elementAt(row);
		    objectSelected(sel);
		    return;
		}
	    }
	}
	if (src == _table4.getSelectionModel()) {
	    int row = lse.getFirstIndex();
	    if (_tableModelCompInstanceByProps != null) {
		Vector rowObjects = _tableModelCompInstanceByProps.getRowObjects();
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
	if (sel instanceof MComponent) {
	    _tableModelClass_in_DeplByProps.setTarget((MComponent) sel);
	    _tableModelInterface_in_DeplByProps.setTarget((MComponent) sel);
	    _tableModelObjectByProps.setTarget((MComponent) sel);
	}
	else if (sel instanceof MComponentInstance) {
	    _tableModelObjectByProps.setTarget((MComponentInstance) sel);
	}
	else if (sel instanceof MNode) {
	    _tableModelComponentByProps.setTarget((MNode) sel);
	}
	else if (sel instanceof MNodeInstance) {
	    _tableModelCompInstanceByProps.setTarget((MNodeInstance) sel);
	}  
    }



    public void setTablePerspective() {
	super.setTablePerspective();
	if (_tableModel instanceof TableModelComponentByProps) {
	    _south.removeAll();
	    _south.add(sp2W);
	    _south.add(sp3W);
	    _south.add(sp4W);
	    _table2.setModel(_tableModelClass_in_DeplByProps);
	    _table3.setModel(_tableModelInterface_in_DeplByProps);
	    _table4.setModel(_tableModelObjectByProps);
	    _south.setVisible(true);
	}
	else if (_tableModel instanceof TableModelCompInstanceByProps) {
	    _south.removeAll();
	    _south.add(sp4W);
	    _table4.setModel(_tableModelObjectByProps);
	    _south.setVisible(true);
	}
	else if (_tableModel instanceof TableModelNodeByProps) {
	    _south.removeAll();
	    _south.add(sp5W);
	    _table5.setModel(_tableModelComponentByProps);
	    _south.setVisible(true);
	}
	else if (_tableModel instanceof TableModelNodeInstanceByProps) {
	    _south.removeAll();
	    _south.add(sp6W);
	    _table6.setModel(_tableModelCompInstanceByProps);
	    _south.setVisible(true);
	}
	else {
	    _south.setVisible(false);
	}
	validate();
    }

  
    public void initTableModels() {
	_tableModels.addElement(new TableModelNodeByProps());
	_tableModels.addElement(new TableModelNodeInstanceByProps());
	_tableModels.addElement(new TableModelComponentByProps());
	_tableModels.addElement(new TableModelCompInstanceByProps());
	_tableModels.addElement(new TableModelDependency_in_DeplByProps());
	_tableModels.addElement(new TableModelClass_in_DeplByProps());
	_tableModels.addElement(new TableModelInterface_in_DeplByProps());
	_tableModels.addElement(new TableModelAssoc_in_DeplByProps());
	_tableModels.addElement(new TableModelObjectByProps());
	_tableModels.addElement(new TableModelLinkByProps());
    }

} /* end class TablePanelUMLDeploymentDiagram */
