package uci.uml.ui;

//import jargo.kernel.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.beans.*;
import com.sun.java.swing.*;
import com.sun.java.swing.event.*;
import com.sun.java.swing.tree.*;
import com.sun.java.swing.text.*;
import com.sun.java.swing.border.*;

import uci.util.*;
import uci.uml.Foundation.Core.*;
import uci.uml.Foundation.Data_Types.*;
import uci.uml.Model_Management.*;

public class PropPanelAssoc extends PropPanel
implements DocumentListener, ItemListener, ChangeListener {

  ////////////////////////////////////////////////////////////////
  // constants
  public static final VisibilityKind VISIBILITIES[] = {
    VisibilityKind.PUBLIC, VisibilityKind.PRIVATE,
    VisibilityKind.PROTECTED, VisibilityKind.PACKAGE };

  public static final Multiplicity MULTS[] = {
    Multiplicity.ONE, Multiplicity.ONE_OR_ZERO,
    Multiplicity.ONE_OR_MORE, Multiplicity.ZERO_OR_MORE };

  
  ////////////////////////////////////////////////////////////////
  // instance vars
  JLabel _roleALabel = new JLabel("Role Name: ");
  JTextField _roleAField = new JTextField();
  JLabel _visALabel = new JLabel("Visibility: ");
  JComboBox _visAField = new JComboBox(VISIBILITIES);
  JLabel _multALabel = new JLabel("Multiplicity: ");
  JComboBox _multAField = new JComboBox(MULTS);
  JLabel _navALabel = new JLabel("Navigable: ");
  JCheckBox _navAField = new JCheckBox(" ");

  JLabel _roleBLabel = new JLabel("Role Name: ");
  JTextField _roleBField = new JTextField();
  JLabel _visBLabel = new JLabel("Visibility: ");
  JComboBox _visBField = new JComboBox(VISIBILITIES);
  JLabel _multBLabel = new JLabel("Multiplicity: ");
  JComboBox _multBField = new JComboBox(MULTS);
  JLabel _navBLabel = new JLabel("Navigable: ");
  JCheckBox _navBField = new JCheckBox(" ");

  SpacerPanel _spacer = new SpacerPanel();

  ////////////////////////////////////////////////////////////////
  // contructors
  public PropPanelAssoc() {
    super("Association Properties");
    GridBagLayout gb = (GridBagLayout) getLayout();
    GridBagConstraints c = new GridBagConstraints();
    c.fill = GridBagConstraints.BOTH;
    c.weightx = 1.0;
    c.ipadx = 0; c.ipady = 0;


    _multAField.setEditable(true);
    _multBField.setEditable(true);
    _multAField.getEditor().getEditorComponent().setBackground(Color.white);
    _multBField.getEditor().getEditorComponent().setBackground(Color.white);


    c.gridx = 0;
    c.gridwidth = 1;
    c.gridy = 5;
    gb.setConstraints(_roleALabel, c);
    add(_roleALabel);
    c.gridy = 6;
    gb.setConstraints(_visALabel, c);
    add(_visALabel);
    c.gridy = 7;
    gb.setConstraints(_multALabel, c);
    add(_multALabel);
    c.gridy = 8;
    gb.setConstraints(_navALabel, c);
    add(_navALabel);

    c.gridx = 1;
    c.gridwidth = 1;
    c.gridy = 5;
    gb.setConstraints(_roleAField, c);
    add(_roleAField);
    c.gridy = 6;
    gb.setConstraints(_visAField, c);
    add(_visAField);
    c.gridy = 7;
    gb.setConstraints(_multAField, c);
    add(_multAField);
    c.gridy = 8;
    gb.setConstraints(_navAField, c);
    add(_navAField);

    

    c.weightx = 0.0;
    c.gridx = 2;
    c.gridy = 0;
    gb.setConstraints(_spacer, c);
    add(_spacer);
    

    c.weightx = 1.0;
    c.gridx = 3;
    c.gridwidth = 1;
    c.gridy = 5;
    gb.setConstraints(_roleBLabel, c);
    add(_roleBLabel);
    c.gridy = 6;
    gb.setConstraints(_visBLabel, c);
    add(_visBLabel);
    c.gridy = 7;
    gb.setConstraints(_multBLabel, c);
    add(_multBLabel);
    c.gridy = 8;
    gb.setConstraints(_navBLabel, c);
    add(_navBLabel);

    c.gridx = 4;
    c.gridwidth = 1;
    c.gridy = 5;
    gb.setConstraints(_roleBField, c);
    add(_roleBField);
    c.gridy = 6;
    gb.setConstraints(_visBField, c);
    add(_visBField);
    c.gridy = 7;
    gb.setConstraints(_multBField, c);
    add(_multBField);
    c.gridy = 8;
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

    _visAField.addItemListener(this);
    _visBField.addItemListener(this);
    _multAField.addItemListener(this);
    _multBField.addItemListener(this);

    _navAField.addChangeListener(this);
    _navAField.addChangeListener(this);


  }

  ////////////////////////////////////////////////////////////////
  // accessors

  public void setTarget(Object t) {
    super.setTarget(t);
    Association asc = (Association) t;

    // VisibilityKind vk = attr.getVisibility();
//     _visField.setSelectedItem(vk);

//     ScopeKind sk = attr.getOwnerScope();
//     ChangeableKind ck = attr.getChangeable();
    
//     if (sk == ScopeKind.CLASSIFIER && ck == ChangeableKind.FROZEN)
//       _keywordsField.setSelectedItem("static final");
//     else if (sk == ScopeKind.CLASSIFIER)
//       _keywordsField.setSelectedItem("static");
//     else if (ck == ChangeableKind.FROZEN)
//       _keywordsField.setSelectedItem("final");
//     else
//       _keywordsField.setSelectedItem("None");

//     Classifier type = attr.getType();
//     _typeField.setSelectedItem(type);
    
//     Expression expr = attr.getInitialValue();
//     if (expr == null) _initText.setText("");
//     else _initText.setText(expr.getBody().getBody());

  }



  public void setTargetVisibility() {
    if (_target == null) return;
//     VisibilityKind vk = (VisibilityKind) _visField.getSelectedItem();
//     uci.uml.Foundation.Core.Attribute attr =
//       (uci.uml.Foundation.Core.Attribute) _target;
//     attr.setVisibility(vk);
  }




  
  ////////////////////////////////////////////////////////////////
  // event handlers


  public void insertUpdate(DocumentEvent e) {
    System.out.println(getClass().getName() + " insert");
//     Component ed = _typeField.getEditor().getEditorComponent();
//     Document typeDoc = ((JTextField)ed).getDocument();

//     Document initDoc = _initText.getDocument();

//     if (e.getDocument() == typeDoc) setTargetType();
//     else if (e.getDocument() == initDoc) setTargetInit();
    super.insertUpdate(e);
  }

  public void removeUpdate(DocumentEvent e) { insertUpdate(e); }

  public void changedUpdate(DocumentEvent e) {
    System.out.println(getClass().getName() + " changed");
    // Apparently, this method is never called.
  }

  
  
  public void stateChanged(ChangeEvent e) {
    System.out.println("got stateChanged");
  }

  public void itemStateChanged(ItemEvent e) {
    Object src = e.getSource();
    if (src == _visAField) {
      System.out.println("assoc vis A now is " +
			 _visAField.getSelectedItem());
      //setTargetKeywords();
    }
    else if (src == _visBField) {
      System.out.println("assoc vis B now is " +
			 _visBField.getSelectedItem());
      //setTargetKeywords();
    }
    else if (src == _multAField) {
      System.out.println("assoc mult A now is " +
			 _multAField.getSelectedItem());
      //setTargetKeywords();
    }
    else if (src == _multBField) {
      System.out.println("assoc mult B now is " +
			 _multBField.getSelectedItem());
      //setTargetKeywords();
    }
    
  }


} /* end class PropPanelAssoc */
