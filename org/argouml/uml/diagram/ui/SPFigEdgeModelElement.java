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

package org.argouml.uml.diagram.ui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;

import org.apache.log4j.Category;
import org.argouml.ui.SpacerPanel;
import org.argouml.ui.StylePanel;
import org.tigris.gef.ui.ColorRenderer;

public class SPFigEdgeModelElement extends StylePanel
    implements ItemListener, DocumentListener 
{
    protected static Category cat = 
        Category.getInstance(SPFigEdgeModelElement.class);

    ////////////////////////////////////////////////////////////////
    // constants

    ////////////////////////////////////////////////////////////////
    // instance vars
    JLabel _bboxLabel = new JLabel("Bounds: ");
    JTextField _bboxField = new JTextField();
    JLabel _lineLabel = new JLabel("Line: ");
    JComboBox _lineField = new JComboBox();
    //JLabel _dashedLabel = new JLabel("Dashed: ");
    //JComboBox _dashedField = new JComboBox(Fig.DASHED_CHOICES);
    SpacerPanel _spacer = new SpacerPanel();
    SpacerPanel _spacer2 = new SpacerPanel();
    SpacerPanel _spacer3 = new SpacerPanel();

    ////////////////////////////////////////////////////////////////
    // contructors
    public SPFigEdgeModelElement() {
	super("Edge Appearance");
	initChoices();
	GridBagLayout gb = (GridBagLayout) getLayout();
	GridBagConstraints c = new GridBagConstraints();
	c.fill = GridBagConstraints.BOTH;
	c.ipadx = 0; c.ipady = 0;

	Document bboxDoc = _bboxField.getDocument();
	bboxDoc.addDocumentListener(this);
	_lineField.addItemListener(this);
	//_dashedField.addItemListener(this);

	_lineField.setRenderer(new ColorRenderer());
	//_dashedField.setRenderer(DashRenderer.SINGLETON);


	c.gridx = 0;
	c.gridwidth = 1;
	c.gridy = 1;
	c.weightx = 0.0;
	gb.setConstraints(_bboxLabel, c);
	add(_bboxLabel);
	c.gridy = 2;
	gb.setConstraints(_lineLabel, c);
	add(_lineLabel);
	//c.gridy = 3;
	//gb.setConstraints(_dashedLabel, c);
	//add(_dashedLabel);


	c.weightx = 1.0;
	c.gridx = 1;
	//c.gridwidth = GridBagConstraints.REMAINDER;
	c.gridy = 1;
	gb.setConstraints(_bboxField, c);
	add(_bboxField);
	c.gridy = 2;
	gb.setConstraints(_lineField, c);
	add(_lineField);
	//c.gridy = 3;
	//gb.setConstraints(_dashedField, c);
	//add(_dashedField);

	c.weightx = 0.0;
	c.gridx = 2;
	c.gridy = 1;
	gb.setConstraints(_spacer, c);
	add(_spacer);

	c.gridx = 3;
	c.gridy = 10;
	gb.setConstraints(_spacer2, c);
	add(_spacer2);

	c.weightx = 1.0;
	c.gridx = 4;
	c.gridy = 10;
	gb.setConstraints(_spacer3, c);
	add(_spacer3);
    }

    protected void initChoices() {
	_lineField.addItem("No Line");
	_lineField.addItem(Color.black);
	_lineField.addItem(Color.white);
	_lineField.addItem(Color.gray);
	_lineField.addItem(Color.lightGray);
	_lineField.addItem(Color.darkGray);
	_lineField.addItem(new Color(60, 60, 200));
	_lineField.addItem(new Color(60, 200, 60));
	_lineField.addItem(new Color(200, 60, 60));
	_lineField.addItem(Color.red);
	_lineField.addItem(Color.blue);
	_lineField.addItem(Color.cyan);
	_lineField.addItem(Color.yellow);
	_lineField.addItem(Color.magenta);
	_lineField.addItem(Color.green);
	_lineField.addItem(Color.orange);
	_lineField.addItem(Color.pink);
	_lineField.addItem("Custom...");
    }

    ////////////////////////////////////////////////////////////////
    // accessors

    public void refresh() {
	super.refresh();

	String bboxStr = _target.getX() + ", " + _target.getY() + ", " +
	    _target.getWidth() + ", " + _target.getHeight();
	_bboxField.setText(bboxStr);

	if (_target.getLineWidth() > 0)
	    _lineField.setSelectedItem(_target.getLineColor());
	else _lineField.setSelectedItem("No Line");

	//_dashedField.setSelectedItem(_target.getDashedString());
    }


    public void setTargetBBox() {
	if (_target == null) return;
	String bboxStr = _bboxField.getText();
	if (bboxStr.length() == 0) return;
	_target.startTrans();
	java.util.StringTokenizer st =
	    new java.util.StringTokenizer(bboxStr, ", ");
	try {
	    int x = Integer.parseInt(st.nextToken());
	    int y = Integer.parseInt(st.nextToken());
	    int w = Integer.parseInt(st.nextToken());
	    int h = Integer.parseInt(st.nextToken());
	    _target.setBounds(x, y, w, h);
	}
	catch (Exception ex) {
	    cat.error("could not parse bounds string", ex);
	}
	_target.endTrans();
    }

    public void setTargetLine() {
	Object c =  _lineField.getSelectedItem();
	if (_target == null || c == null) return;
	_target.startTrans();
	if (c instanceof Color) _target.setLineColor((Color) c);
	_target.setLineWidth((c instanceof Color) ? 1 : 0);
	_target.endTrans();
    }


    ////////////////////////////////////////////////////////////////
    // event handling

    public void insertUpdate(DocumentEvent e) {
	cat.debug(getClass().getName() + " insertUpdate");
	Document bboxDoc = _bboxField.getDocument();
	if (e.getDocument() == bboxDoc) setTargetBBox();
	super.insertUpdate(e);
    }

    public void removeUpdate(DocumentEvent e) { insertUpdate(e); }

    public void changedUpdate(DocumentEvent e) {
    }


    public void itemStateChanged(ItemEvent e) {
	Object src = e.getSource();
	if (src == _lineField) setTargetLine();
	//else if (src == _dashedField) setTargetDashed();
	else super.itemStateChanged(e);
    }



} /* end class SPFigEdgeModelElement */























