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




package uci.uml.ui.props;

//import jargo.kernel.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.beans.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import javax.swing.text.*;
import javax.swing.border.*;

import uci.util.*;
import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.foundation.data_types.*;
import ru.novosoft.uml.model_management.*;
import uci.uml.generate.ParserDisplay;
import uci.uml.ui.*;

public class PropPanelAssociation extends PropPanelTwoEnds
implements ItemListener, ChangeListener {

  ////////////////////////////////////////////////////////////////
  // constants
//   public static final MVisibilityKind VISIBILITIES[] = {
//     MVisibilityKind.PUBLIC, MVisibilityKind.PRIVATE,
//     MVisibilityKind.PROTECTED, MVisibilityKind.PACKAGE };

//   public static final MMultiplicity MULTS[] = {
//     MMultiplicity.M1_1, MMultiplicity.M0_1,
//     MMultiplicity.M1_N, MMultiplicity.M0_N };

  public static final String MULTS[] = {
    MMultiplicity.M1_1.toString(), MMultiplicity.M0_1.toString(),
    MMultiplicity.M1_N.toString(), MMultiplicity.M0_N.toString() };

//   public static final MAggregationKind AGGREGATES[] = {
//     MAggregationKind.NONE, MAggregationKind.AGGREGATE,
//     MAggregationKind.COMPOSITE };
  
  public static final String AGGREGATES[] = {
    MAggregationKind.NONE.getName(), MAggregationKind.AGGREGATE.getName(),
    MAggregationKind.COMPOSITE.getName() };
  
  
  ////////////////////////////////////////////////////////////////
  // instance vars
  JLabel _roleALabel = new JLabel("Role Name: ");
  JTextField _roleAField = new JTextField();
  JLabel _multALabel = new JLabel("Multiplicity: ");
  JComboBox _multAField = new JComboBox(MULTS);
  JLabel _aggALabel = new JLabel("Aggregation: ");
  JComboBox _aggAField = new JComboBox(AGGREGATES);
  JLabel _navALabel = new JLabel("Navigable: ");
  JCheckBox _navAField = new JCheckBox("");
//   JLabel _visALabel = new JLabel("Visibility: ");
//   JComboBox _visAField = new JComboBox(VISIBILITIES);

  JLabel _roleBLabel = new JLabel("Role Name: ");
  JTextField _roleBField = new JTextField();
  JLabel _multBLabel = new JLabel("Multiplicity: ");
  JComboBox _multBField = new JComboBox(MULTS);
  JLabel _aggBLabel = new JLabel("Aggregation: ");
  JComboBox _aggBField = new JComboBox(AGGREGATES);
//   JLabel _visBLabel = new JLabel("Visibility: ");
//   JComboBox _visBField = new JComboBox(VISIBILITIES);
  JLabel _navBLabel = new JLabel("Navigable: ");
  JCheckBox _navBField = new JCheckBox("");

  ////////////////////////////////////////////////////////////////
  // contructors
  public PropPanelAssociation() {
    super("Association Properties");
    GridBagLayout gb = (GridBagLayout) getLayout();
    GridBagConstraints c = new GridBagConstraints();
    c.fill = GridBagConstraints.BOTH;
    c.weightx = 0.0;
    c.ipadx = 0; c.ipady = 0;


    _multAField.setEditable(true);
    _multBField.setEditable(true);
    _multAField.getEditor().getEditorComponent().setBackground(Color.white);
    _multBField.getEditor().getEditorComponent().setBackground(Color.white);


    c.gridx = 0;
    c.gridwidth = 1;
    c.gridy = 4;
    gb.setConstraints(_roleALabel, c);
    add(_roleALabel);
    c.gridy = 5;
    gb.setConstraints(_multALabel, c);
    add(_multALabel);
    c.gridy = 6;
    gb.setConstraints(_aggALabel, c);
    add(_aggALabel);
    c.gridy = 7;
    gb.setConstraints(_navALabel, c);
    add(_navALabel);

    c.gridx = 1;
    c.gridwidth = 1;
    c.gridy = 4;
    c.weightx = 1.0;
    gb.setConstraints(_roleAField, c);
    add(_roleAField);
    c.gridy = 5;
    gb.setConstraints(_multAField, c);
    add(_multAField);
    c.gridy = 6;
    gb.setConstraints(_aggAField, c);
    add(_aggAField);
    c.gridy = 7;
    gb.setConstraints(_navAField, c);
    add(_navAField);

    c.weightx = 0.0;
    c.gridx = 3;
    c.gridwidth = 1;
    c.gridy = 4;
    gb.setConstraints(_roleBLabel, c);
    add(_roleBLabel);
    c.gridy = 5;
    gb.setConstraints(_multBLabel, c);
    add(_multBLabel);
    c.gridy = 6;
    gb.setConstraints(_aggBLabel, c);
    add(_aggBLabel);
    c.gridy = 7;
    gb.setConstraints(_navBLabel, c);
    add(_navBLabel);

    c.gridx = 4;
    c.gridwidth = 1;
    c.gridy = 4;
    c.weightx = 1.0;
    gb.setConstraints(_roleBField, c);
    add(_roleBField);
    c.gridy = 5;
    gb.setConstraints(_multBField, c);
    add(_multBField);
    c.gridy = 6;
    gb.setConstraints(_aggBField, c);
    add(_aggBField);
    c.gridy = 7;
    gb.setConstraints(_navBField, c);
    add(_navBField);


    Component ed = _multAField.getEditor().getEditorComponent();
    Document typeDoc = ((JTextField)ed).getDocument();
    typeDoc.addDocumentListener(this);

    ed = _multBField.getEditor().getEditorComponent();
    typeDoc = ((JTextField)ed).getDocument();
    typeDoc.addDocumentListener(this);

    _roleAField.getDocument().addDocumentListener(this);
    _roleBField.getDocument().addDocumentListener(this);

//     _visAField.addItemListener(this);
//     _visBField.addItemListener(this);
    _multAField.addItemListener(this);
    _multBField.addItemListener(this);

    _aggAField.addItemListener(this);
    _aggBField.addItemListener(this);

    _navAField.addChangeListener(this);
    _navBField.addChangeListener(this);


  }

  ////////////////////////////////////////////////////////////////
  // accessors

  protected void setTargetInternal(Object t) {
    super.setTargetInternal(t);
    MAssociation asc = (MAssociation) t;
    Vector conns = new Vector(asc.getConnections());
    if (conns == null) return; //set fields to blanks?
    if (conns.size() != 2) return; //set fields to blanks?
    MAssociationEnd endA = (MAssociationEnd) conns.elementAt(0);
    MAssociationEnd endB = (MAssociationEnd) conns.elementAt(1);

    String roleAStr = endA.getName();
    if (roleAStr ==null) roleAStr = "";
    String roleBStr = endB.getName();
    if (roleBStr == null) roleBStr = "";
    
//     MVisibilityKind vkA = endA.getVisibility();
//     _visAField.setSelectedItem(vkA);
//     MVisibilityKind vkB = endB.getVisibility();
//     _visBField.setSelectedItem(vkB);

    MMultiplicity mA = endA.getMultiplicity();
    MMultiplicity mB = endB.getMultiplicity();

    MAggregationKind akA = endA.getAggregation();
    MAggregationKind akB = endB.getAggregation();

    boolean navA = endA.isNavigable();
    boolean navB = endB.isNavigable();

    _roleAField.setText(roleAStr);
    _roleBField.setText(roleBStr);
    if (mA != null)
	_multAField.setSelectedItem(mA.toString());

    else
	_multAField.setSelectedItem(null);

    if (mB != null)
	_multBField.setSelectedItem(mB.toString());

    else
	_multBField.setSelectedItem(null);

    if (akA != null)
	_aggAField.setSelectedItem(akA.getName());

    else
	_aggAField.setSelectedItem(MAggregationKind.NONE.getName());

    if (akB != null)
	_aggBField.setSelectedItem(akB.getName());

    else
	_aggBField.setSelectedItem(MAggregationKind.NONE.getName());

    _navAField.getModel().setSelected(navA);

    _navBField.getModel().setSelected(navB);
  }

  public String getSourceLabel() {
    if (!(_target instanceof MAssociation)) return "non Association";
    return "Classifier:";
  }
  public String getSourceValue() {
    if (!(_target instanceof MAssociation)) return "non Association";
    MAssociation a = (MAssociation) _target;
    MAssociationEnd ae = (MAssociationEnd)((Object[])a.getConnections().toArray())[0];
    if (ae == null) return "null ae";
    MClassifier type = ae.getType();
    if (type == null) return "null type";
    return type.getName();
  }
  public String getDestLabel() {
    if (!(_target instanceof MAssociation)) return "non Association";
    return "Classifier:";
  }
  public String getDestValue() {
    if (!(_target instanceof MAssociation)) return "non Association";
    MAssociation a = (MAssociation) _target;
    MAssociationEnd ae = (MAssociationEnd)((Object[])a.getConnections().toArray())[1];
    if (ae == null) return "null ae";
    MClassifier type = ae.getType();
    if (type == null) return "null type";
    return type.getName();
  }
  

  


  public void setTargetVisibility() {
    if (_target == null) return;
    if (_inChange) return;
//     MVisibilityKind vk = (MVisibilityKind) _visField.getSelectedItem();
//     MAttribute attr = (MAttribute) _target;
//     attr.setVisibility(vk);
  }

    public void setMult() {
	if (_target == null) return;
	MMultiplicity mA = null;
	MMultiplicity mB = null;
	Object mAs = _multAField.getSelectedItem();
	Object mBs = _multBField.getSelectedItem();

	MAssociation asc = (MAssociation) _target;
	Vector conns = new Vector(asc.getConnections());
	if (conns == null || conns.size() != 2) return;

	if (mAs != null) {
	    // will be a String if I allow editing, needs-more-work: implement
	    // parsing of multiplicities
	    if (mAs instanceof String)
		mAs = ParserDisplay.SINGLETON.parseMultiplicity((String)mAs);

	    if (mAs instanceof MMultiplicity) mA = (MMultiplicity) mAs;
	    else mA = MMultiplicity.M1_1; // needs-more-work: parse
	    MAssociationEnd endA = (MAssociationEnd) conns.elementAt(0);
	    endA.setMultiplicity(mA);
	}

	if (mBs != null) {
	    if (mBs instanceof String)
		mBs = ParserDisplay.SINGLETON.parseMultiplicity((String)mBs);
	    if (mBs instanceof MMultiplicity) mB = (MMultiplicity) mBs;
	    else mB = MMultiplicity.M1_1; // needs-more-work: parse
	    MAssociationEnd endB = (MAssociationEnd) conns.elementAt(1);
	    endB.setMultiplicity(mB);
	}
    }

  public void setAgg() {
    if (_target == null) return;
    MAggregationKind aggA = MAggregationKind.forName( (String)_aggAField.getSelectedItem());
    MAggregationKind aggB = MAggregationKind.forName( (String)_aggBField.getSelectedItem());
    //if (aggA == null || aggB == null) return;
    MAssociation asc = (MAssociation) _target;
    Vector conns = new Vector(asc.getConnections());
    if (conns == null || conns.size() != 2) return;
    MAssociationEnd endA = (MAssociationEnd) conns.elementAt(0);
    MAssociationEnd endB = (MAssociationEnd) conns.elementAt(1);
      endA.setAggregation(aggA);
      endB.setAggregation(aggB);
  }

  public void setNav() {
    if (_target == null) return;
    boolean navA = _navAField.getModel().isSelected();
    boolean navB = _navBField.getModel().isSelected();
    //System.out.println("navA=" + navA + " navB=" + navB);
    MAssociation asc = (MAssociation) _target;
    Vector conns = new Vector(asc.getConnections());
    if (conns == null || conns.size() != 2) return;
    MAssociationEnd endA = (MAssociationEnd) conns.elementAt(0);
    MAssociationEnd endB = (MAssociationEnd) conns.elementAt(1);
      endA.setNavigable(navA);
      endB.setNavigable(navB);
	
	//very dirty patch: navigation change is not visible until aggregation-kind has been changed!!
	setAgg();

  }
  
  public void setRoleNames() {
    if (_target == null) return;
    String roleAStr = _roleAField.getText();
    String roleBStr = _roleBField.getText();
    if (roleBStr == null || roleBStr == null) return;
    //System.out.println("roleA=" + roleAStr + " roleB=" + roleBStr);
    MAssociation asc = (MAssociation) _target;
    Vector conns = new Vector(asc.getConnections());
    if (conns == null || conns.size() != 2) return;
    MAssociationEnd endA = (MAssociationEnd) conns.elementAt(0);
    MAssociationEnd endB = (MAssociationEnd) conns.elementAt(1);
	endA.setName(roleAStr);
	endB.setName(roleBStr);
  }

  

  
  ////////////////////////////////////////////////////////////////
  // event handlers


  public void insertUpdate(DocumentEvent e) {
    //System.out.println(getClass().getName() + " insert");
//     Component ed = _typeField.getEditor().getEditorComponent();
//     Document typeDoc = ((JTextField)ed).getDocument();

    Document roleADoc = _roleAField.getDocument();
    Document roleBDoc = _roleBField.getDocument();

    if (e.getDocument() == roleADoc) setRoleNames();
    else if (e.getDocument() == roleBDoc) setRoleNames();
//     else if (e.getDocument() == initDoc) setTargetInit();
    super.insertUpdate(e);
  }

  public void removeUpdate(DocumentEvent e) { insertUpdate(e); }

  public void changedUpdate(DocumentEvent e) {
    //System.out.println(getClass().getName() + " changed");
    // Apparently, this method is never called.
  }

  
  
  public void stateChanged(ChangeEvent e) {
    //System.out.println("got stateChanged");
    Object src = e.getSource();
    if (src == _navAField || src == _navBField) setNav();
  }

  public void itemStateChanged(ItemEvent e) {
    Object src = e.getSource();
//     if (src == _visAField) {
//       System.out.println("assoc vis A now is " +
// 			 _visAField.getSelectedItem());
//       //setTargetKeywords();
//     }
//     else if (src == _visBField) {
//       System.out.println("assoc vis B now is " +
// 			 _visBField.getSelectedItem());
//       //setTargetKeywords();
//     }
    if (src == _multAField) {
      //System.out.println("assoc mult A now is " +
      //_multAField.getSelectedItem());
      setMult();
    }
    else if (src == _multBField) {
      //System.out.println("assoc mult B now is " +
      //_multBField.getSelectedItem());
      setMult();
    }
    if (src == _aggAField) {
      //System.out.println("assoc agg A now is " +
      //_aggAField.getSelectedItem());
      setAgg();
    }
    else if (src == _aggBField) {
      //System.out.println("assoc agg B now is " +
      //_aggBField.getSelectedItem());
      setAgg();
    }
    
  }


} /* end class PropPanelAssociation */
