
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


// File: TablePanelUMLSequenceDiagram.java
// Classes: TablePanelUMLSequenceDiagram
// Original Author: 5eichler@informatik.uni-hamburg.de
// $Id$


package org.argouml.uml.diagram.sequence.ui;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.beans.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.plaf.metal.MetalLookAndFeel;

import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.foundation.data_types.*;
import ru.novosoft.uml.behavior.common_behavior.*;

import org.tigris.gef.ui.*;

import org.argouml.ui.*;
import org.argouml.uml.*;
import org.argouml.uml.diagram.sequence.*;


public class TablePanelUMLSequenceDiagram extends TablePanel {

    ////////////////////////////////////////////////////////////////
    // instance variables

    JSortedTable _table2 = new JSortedTable();
    JPanel sp2W;
    TableModelComposite _tableModelSeqStimulusByProps = new TableModelSeqStimulusByProps();
    JPanel _south = new JPanel();
    JScrollPane _sp2;


    ////////////////////////////////////////////////////////////////
    // constructors

    public TablePanelUMLSequenceDiagram() {
	super("UMLDeploymentDiagram");    

	_south.setLayout(new GridLayout(2, 2, 5, 5));

	_sp2 = new JScrollPane(_table2,
			       JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
			       JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

	_sp1.setPreferredSize(new Dimension(300, 300));
	_sp1.setSize(new Dimension(300, 300));

	_sp2.setPreferredSize(new Dimension(300, 100));
	_sp2.setSize(new Dimension(300, 100));

	_content.setPreferredSize(new Dimension(300, 600));
	_content.setSize(new Dimension(300, 600));

	sp2W = new JPanel();
	sp2W.setLayout(new BorderLayout());
	sp2W.add(new JLabel("Stimuli of selected communication-link:"), BorderLayout.NORTH);
	sp2W.add(_sp2, BorderLayout.CENTER);

	_south.add(sp2W);

	_content.add(_south, BorderLayout.SOUTH);

	Font labelFont = MetalLookAndFeel.getSubTextFont();
	_table2.setFont(labelFont);
	setEditors(_table2);

	_table2.getSelectionModel().addListSelectionListener(this);
    }

    /////////////////////////////////////////////////////////////////
    // ListSelectionListener implemention

    public void valueChanged(ListSelectionEvent lse) {
	super.valueChanged(lse);
	if (lse.getValueIsAdjusting()) return;
	Object src = lse.getSource();
	_table2.sizeColumnsToFit(-1);
	if (src == _table2.getSelectionModel()) {
	    int row = lse.getFirstIndex();
	    if (_tableModelSeqStimulusByProps != null) {
		Vector rowObjects = _tableModelSeqStimulusByProps.getRowObjects();
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
	if (org.argouml.model.ModelFacade.isALink(sel)) {
	    _tableModelSeqStimulusByProps.setTarget((MLink) sel);
	}
    }



    public void setTablePerspective() {
	super.setTablePerspective();
	if (_tableModel instanceof TableModelSeqLinkByProps) {
	    _south.removeAll();
	    _south.add(sp2W);
	    _table2.setModel(_tableModelSeqStimulusByProps);
	    _south.setVisible(true);
	}
	else {
	    _south.setVisible(false);
	}
	validate();
    }

    public void initTableModels() {
	_tableModels.addElement(new TableModelSeqObjectByProps());
	_tableModels.addElement(new TableModelSeqLinkByProps());
	_tableModels.addElement(new TableModelSeqStimulusByProps());
	_tableModels.addElement(new TableModelSeqActionByProps());
    }

} /* end class TablePanelUMLSequenceDiagram */