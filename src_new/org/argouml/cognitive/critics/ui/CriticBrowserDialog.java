// $Id$
// Copyright (c) 1996-2003 The Regents of the University of California. All
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

package org.argouml.cognitive.critics.ui;

import java.beans.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import javax.swing.text.*;

import org.argouml.kernel.*;
import org.argouml.ui.ArgoDialog;
import org.argouml.ui.ProjectBrowser;
import org.argouml.cognitive.*;
import org.argouml.cognitive.critics.*;

import org.apache.log4j.Category;
import org.argouml.application.api.*;

/** Dialog box to list all critics and allow editing of some of their
 *  properties.  TODO: knowledge type, supported goals,
 *  supported decisions, critic network, localize labels. */
public class CriticBrowserDialog extends ArgoDialog
    implements ActionListener, 
	       ListSelectionListener, 
	       ItemListener, 
	       DocumentListener 
{
    
    protected static Category cat =
	Category.getInstance(CriticBrowserDialog.class);
    
    public static int _numCriticBrowser = 0;

    ////////////////////////////////////////////////////////////////
    // constants
    private static final String BUNDLE = "Cognitive";

    private static final String DESC_WIDTH_TEXT = 
	"This is Sample Text for determining Column Width";
  
    private static final int NUM_COLUMNS = 25;

    static final String high = Argo.localize(BUNDLE, "level.high");
    static final String medium = Argo.localize(BUNDLE, "level.medium");
    static final String low = Argo.localize(BUNDLE, "level.low");

    public static final String PRIORITIES[] = {
	high, medium, low 
    };
    public static final String USE_CLAR[] = {
	"Always", "If Only One", "Never" 
    };


    ////////////////////////////////////////////////////////////////
    // instance variables

    protected JLabel _criticsLabel   = new JLabel("Critics");
    protected JLabel _clsNameLabel   = new JLabel("Critic Class: ");
    protected JLabel _headlineLabel  = new JLabel("Headline: ");
    protected JLabel _priorityLabel  = new JLabel("Priority: ");
    protected JLabel _moreInfoLabel  = new JLabel("More Info: ");
    protected JLabel _descLabel      = new JLabel("Description: ");
    protected JLabel _clarifierLabel = new JLabel("Use Clarifier: ");

    TableModelCritics _tableModel  = new TableModelCritics();
    protected JTable _table        = new JTable();
    protected JTextField _className = new JTextField("", NUM_COLUMNS);
    protected JTextField _headline = new JTextField("", NUM_COLUMNS);
    protected JComboBox _priority  = new JComboBox(PRIORITIES);
    protected JTextField _moreInfo = new JTextField("", NUM_COLUMNS);
    protected JTextArea _desc      = new JTextArea("", 6, NUM_COLUMNS);
    protected JComboBox _useClar   = new JComboBox(USE_CLAR);

    protected JButton _wakeButton    = new JButton("Wake");
    protected JButton _configButton  = new JButton("Configure");
    protected JButton _networkButton = new JButton("Edit Network");
    protected JButton _goButton      = new JButton("Go");

    protected Critic _target;

    ////////////////////////////////////////////////////////////////
    // constructors

    public CriticBrowserDialog() {
	super(ProjectBrowser.getInstance(), "Critics", true);

	JPanel mainContent = new JPanel();
	mainContent.setLayout(new BorderLayout(10, 10));

	// Critics Table
    
	JPanel tablePanel = new JPanel(new BorderLayout(5, 5));

	_tableModel.setTarget(Agency.getCritics());
	_table.setModel(_tableModel);
	_table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	_table.setShowVerticalLines(false);
	_table.getSelectionModel().addListSelectionListener(this);
	_table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);

	TableColumn checkCol = _table.getColumnModel().getColumn(0);
	TableColumn descCol = _table.getColumnModel().getColumn(1);
	TableColumn actCol = _table.getColumnModel().getColumn(2);
	checkCol.setMinWidth(35);
	checkCol.setMaxWidth(35);
	checkCol.setWidth(30);
	int descWidth =
	    _table.getFontMetrics(_table.getFont())
	    .stringWidth(DESC_WIDTH_TEXT);
	descCol.setMinWidth(descWidth);
	descCol.setWidth(descWidth);
	actCol.setMinWidth(50);
	actCol.setMaxWidth(50);
	actCol.setWidth(50);

	tablePanel.add(_criticsLabel, BorderLayout.NORTH);
	JScrollPane tableSP = new JScrollPane(_table);
	tablePanel.add(tableSP, BorderLayout.CENTER);

	// Set tableSP's preferred height to 0 so that details height
	// is used in pack()
	tableSP.setPreferredSize(new Dimension(checkCol.getWidth()
					       + descCol.getWidth()
					       + actCol.getWidth() + 20,
					       0));
	
	mainContent.add(tablePanel, BorderLayout.CENTER);

	// Critic Details panel

	JPanel detailsPanel = new JPanel(new GridBagLayout());
	
	GridBagConstraints labelConstraints = new GridBagConstraints();
	labelConstraints.anchor = GridBagConstraints.EAST;
	labelConstraints.gridy = 0;
	labelConstraints.gridx = 0;
	labelConstraints.gridwidth = 1;
	labelConstraints.gridheight = 1;
	labelConstraints.insets = new Insets(0, 10, 5, 4);

	GridBagConstraints fieldConstraints = new GridBagConstraints();
	fieldConstraints.anchor = GridBagConstraints.WEST;
	fieldConstraints.fill = GridBagConstraints.HORIZONTAL;
	fieldConstraints.gridy = 0;
	fieldConstraints.gridx = 1;
	fieldConstraints.gridwidth = 3;
	fieldConstraints.gridheight = 1;
	fieldConstraints.weightx = 1.0;
	fieldConstraints.insets = new Insets(0, 4, 5, 10);
	
	_className.setEditable(false);
	_className.setBorder(null);
	labelConstraints.gridy = 0;
	fieldConstraints.gridy = 0;
	detailsPanel.add(_clsNameLabel, labelConstraints);
	detailsPanel.add(_className, fieldConstraints);
	
	labelConstraints.gridy = 1;
	fieldConstraints.gridy = 1;
	detailsPanel.add(_headlineLabel, labelConstraints);
	detailsPanel.add(_headline, fieldConstraints);

	labelConstraints.gridy = 2;
	fieldConstraints.gridy = 2;
	detailsPanel.add(_priorityLabel, labelConstraints);
	detailsPanel.add(_priority, fieldConstraints);

	labelConstraints.gridy = 3;
	fieldConstraints.gridy = 3;
	detailsPanel.add(_moreInfoLabel, labelConstraints);
	JPanel moreInfoPanel =
	    new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
	moreInfoPanel.add(_moreInfo);
	moreInfoPanel.add(new JLabel(" ")); // spacing
	moreInfoPanel.add(_goButton);
	detailsPanel.add(moreInfoPanel, fieldConstraints);

	labelConstraints.gridy = 4;
	fieldConstraints.gridy = 4;
	labelConstraints.anchor = GridBagConstraints.NORTHEAST;
	detailsPanel.add(_descLabel, labelConstraints);
	detailsPanel.add(new JScrollPane(_desc), fieldConstraints);

	labelConstraints.anchor = GridBagConstraints.EAST;
	labelConstraints.gridy = 5;
	fieldConstraints.gridy = 5;
	detailsPanel.add(_clarifierLabel, labelConstraints);
	detailsPanel.add(_useClar, fieldConstraints);

	labelConstraints.gridy = 6;
	fieldConstraints.gridy = 6;
	JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
	buttonPanel.add(_wakeButton);
	buttonPanel.add(_configButton);
	buttonPanel.add(_networkButton);
	detailsPanel.add(new JLabel(""), labelConstraints);
	detailsPanel.add(buttonPanel, fieldConstraints);

	JPanel detailsContainer =
	    new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
	detailsContainer.setBorder(BorderFactory
				   .createTitledBorder("Critic Details"));
	detailsContainer.add(detailsPanel);
	mainContent.add(detailsContainer, BorderLayout.EAST);	
    
	_goButton.addActionListener(this);
	_networkButton.addActionListener(this);
	_wakeButton.addActionListener(this);
	_configButton.addActionListener(this);
	_headline.getDocument().addDocumentListener(this);
	_moreInfo.getDocument().addDocumentListener(this);
	_desc.getDocument().addDocumentListener(this);
	_priority.addItemListener(this);
	_useClar.addItemListener(this);

	_goButton.setEnabled(false);
	_wakeButton.setEnabled(false);
	_networkButton.setEnabled(false);
	_configButton.setEnabled(false);

	_desc.setLineWrap(true);
	_desc.setWrapStyleWord(true);

	setResizable(true);
    
	setContent(mainContent);
    
	_numCriticBrowser++;
    }

    public void setTarget(Object t) {
	_target = (Critic) t;
	_goButton.setEnabled(false);
	_networkButton.setEnabled(false);
	_wakeButton.setEnabled(_target != null &&
			       _target.snoozeOrder().getSnoozed());
	_configButton.setEnabled(false);
	_className.setText(_target.getClass().getName());
	_headline.setText(_target.getHeadline());

	int p = _target.getPriority();
	if (p == ToDoItem.HIGH_PRIORITY)
	    _priority.setSelectedItem(high);
	else if (p == ToDoItem.MED_PRIORITY)
	    _priority.setSelectedItem(medium);
	else
	    _priority.setSelectedItem(low);
	_priority.repaint();

	_moreInfo.setText(_target.getMoreInfoURL());
	_desc.setText(_target.getDescriptionTemplate());
	_desc.setCaretPosition(0);
	_useClar.setSelectedItem("Always");
	_useClar.repaint();
    }

    public void setTargetHeadline() {
	if (_target == null) return;
	String h = _headline.getText();
	_target.setHeadline(h);
    }

    public void setTargetPriority() {
	if (_target == null) return;
	String p = (String) _priority.getSelectedItem();
	if (p == null) return;
	if (p.equals(PRIORITIES[0]))
	    _target.setPriority(ToDoItem.HIGH_PRIORITY);
	if (p.equals(PRIORITIES[1]))
	    _target.setPriority(ToDoItem.MED_PRIORITY);
	if (p.equals(PRIORITIES[2])
	    _target.setPriority(ToDoItem.LOW_PRIORITY);
    }

    public void setTargetMoreInfo() {
	if (_target == null) return;
	String mi = _moreInfo.getText();
	_target.setMoreInfoURL(mi);
    }

    public void setTargetDesc() {
	if (_target == null) return;
	String d = _desc.getText();
	_target.setDescription(d);
    }

    public void setTargetUseClarifiers() {
	cat.debug("setting clarifier usage rule");
    }

    ////////////////////////////////////////////////////////////////
    // event handlers


    public void actionPerformed(ActionEvent e) {
	super.actionPerformed(e);
	if (e.getSource() == _goButton) {
	    cat.debug("TODO go!");
	    return;
	}
	if (e.getSource() == _networkButton) {
	    cat.debug("TODO network!");
	    return;
	}
	if (e.getSource() == _configButton) {
	    cat.debug("TODO config!");
	    return;
	}
	if (e.getSource() == _wakeButton) {
	    _target.unsnooze();
	    return;
	}
	cat.debug("unknown src in CriticBrowserDialog: " + e.getSource());
    }

    public void valueChanged(ListSelectionEvent lse) {
	if (lse.getValueIsAdjusting()) return;
	Object src = lse.getSource();
	if (src != _table.getSelectionModel()) {
	    cat.debug("src = " + src);
	    return;
	}
	cat.debug("got valueChanged from " + src);
	int row = _table.getSelectedRow();
	Vector critics = Agency.getCritics();
	setTarget(critics.elementAt(row));
    }

    public void insertUpdate(DocumentEvent e) {
	cat.debug(getClass().getName() + " insert");
	Document hDoc = _headline.getDocument();
	Document miDoc = _moreInfo.getDocument();
	Document dDoc = _desc.getDocument();
	if (e.getDocument() == hDoc) setTargetHeadline();
	if (e.getDocument() == miDoc) setTargetMoreInfo();
	if (e.getDocument() == dDoc) setTargetDesc();
    }

    public void removeUpdate(DocumentEvent e) { insertUpdate(e); }

    public void changedUpdate(DocumentEvent e) {
	cat.debug(getClass().getName() + " changed");
	// Apparently, this method is never called.
    }

    public void itemStateChanged(ItemEvent e) {
	Object src = e.getSource();
	if (src == _priority) {
	    setTargetPriority();
	}
	else if (src == _useClar) {
	    setTargetUseClarifiers();
	}
	else cat.debug("unknown itemStateChanged src: " + src);
    }

} /* end class CriticBrowserDialog */




class TableModelCritics extends AbstractTableModel
    implements VetoableChangeListener, DelayedVChangeListener 
{
    protected static Category cat =
	Category.getInstance(TableModelCritics.class);
    ////////////////
    // instance varables
    Vector _target;

    ////////////////
    // constructor
    public TableModelCritics() { }

    ////////////////
    // accessors
    public void setTarget(Vector critics) {
	_target = critics;
	//fireTableStructureChanged();
    }

    ////////////////
    // TableModel implemetation
    public int getColumnCount() { return 3; }

    public String  getColumnName(int c) {
	if (c == 0) return "Active";
	if (c == 1) return "Headline";
	if (c == 2) return "Snoozed";
	return "XXX";
    }

    public Class getColumnClass(int c) {
	if (c == 0) return Boolean.class;
	if (c == 1) return String.class;
	if (c == 2) return String.class;
	return String.class;
    }

    public boolean isCellEditable(int row, int col) {
	return col == 0;
    }

    public int getRowCount() {
	if (_target == null) return 0;
	return _target.size();
    }

    public Object getValueAt(int row, int col) {
	Critic cr = (Critic) _target.elementAt(row);
	if (col == 0) return cr.isEnabled() ? Boolean.TRUE : Boolean.FALSE;
	if (col == 1) return cr.getHeadline();
	if (col == 2) return cr.isActive() ? "no" : "yes";
	return "CR-" + row * 2 + col; // for debugging
    }

    public void setValueAt(Object aValue, int rowIndex, int columnIndex)  {
	cat.debug("setting table value " + rowIndex + ", " + columnIndex);
	if (columnIndex != 0) return;
	if (!(aValue instanceof Boolean)) return;
	Boolean enable = (Boolean) aValue;
	Critic cr = (Critic) _target.elementAt(rowIndex);
	cr.setEnabled(enable.booleanValue());
	fireTableRowsUpdated(rowIndex, rowIndex); //TODO
    }

    ////////////////
    // event handlers

    public void vetoableChange(PropertyChangeEvent pce) {
	DelayedChangeNotify delayedNotify = new DelayedChangeNotify(this, pce);
	SwingUtilities.invokeLater(delayedNotify);
    }

    public void delayedVetoableChange(PropertyChangeEvent pce) {
	fireTableStructureChanged();
    }


} /* end class TableModelCritics */
