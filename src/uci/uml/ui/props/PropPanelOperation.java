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
import javax.swing.table.*;
import javax.swing.tree.*;
import javax.swing.text.*;
import javax.swing.border.*;

import uci.util.*;
import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.foundation.data_types.*;
import ru.novosoft.uml.model_management.*;
import uci.uml.ui.*;
import uci.uml.util.MMUtil;

public class PropPanelOperation extends PropPanel
implements ItemListener {

  ////////////////////////////////////////////////////////////////
  // constants
  public static final String VISIBILITIES[] = {
    MVisibilityKind.PUBLIC.getName(), MVisibilityKind.PRIVATE.getName(),
    MVisibilityKind.PROTECTED.getName() };

	//PACKAGE in nsuml???
  public static final String ATTRKEYWORDS[] = {
    "None", "static", "final", "static final", "synchronized",
    "static synchronized", "final synchronized", "static final synchronized"
  };

  
  ////////////////////////////////////////////////////////////////
  // instance vars
  JLabel _visLabel = new JLabel("Visibility: ");
  JComboBox _visField = new JComboBox(VISIBILITIES);
  JLabel _keywordsLabel = new JLabel("Keywords: ");
  JComboBox _keywordsField = new JComboBox(ATTRKEYWORDS);
  JLabel _typeLabel = new JLabel("Return Type: ");
  JComboBox _typeField = new JComboBox();
  JLabel _argsLabel = new JLabel("Arguments: ");
  JTable _argsTable = new JTable();
  SpacerPanel _spacer = new SpacerPanel();

  ////////////////////////////////////////////////////////////////
  // contructors
  public PropPanelOperation() {
    super("Operation Properties");
    GridBagLayout gb = (GridBagLayout) getLayout();
    GridBagConstraints c = new GridBagConstraints();
    c.fill = GridBagConstraints.BOTH;
    c.weightx = 0.0;
    c.ipadx = 0; c.ipady = 0;


    //_visField.getEditor().getEditorComponent().setBackground(Color.white);
    //_keywordsField.getEditor().getEditorComponent().setBackground(Color.white);
    _typeField.setEditable(true);
    _typeField.getEditor().getEditorComponent().setBackground(Color.white);    

    c.gridx = 0;
    c.gridwidth = 1;
    c.gridy = 1;
    gb.setConstraints(_visLabel, c);
    add(_visLabel);
    c.gridy = 2;
    gb.setConstraints(_keywordsLabel, c);
    add(_keywordsLabel);
    c.gridy = 3;
    gb.setConstraints(_typeLabel, c);
    add(_typeLabel);

    
    c.weightx = 1.0;
    c.gridx = 1;
    //c.gridwidth = GridBagConstraints.REMAINDER;
    c.gridy = 1;
    gb.setConstraints(_visField, c);
    add(_visField);
    c.gridy = 2;
    gb.setConstraints(_keywordsField, c);
    add(_keywordsField);
    c.gridy = 3;
    gb.setConstraints(_typeField, c);
    add(_typeField);

    c.weightx = 0.0;
    c.gridx = 2;
    c.gridy = 0;
    gb.setConstraints(_spacer, c);
    add(_spacer);
    

    c.weightx = 1.0;
    c.gridwidth = 3;
    c.gridx = 3;
    //c.gridwidth = GridBagConstraints.REMAINDER;
    c.gridy = 0;
    gb.setConstraints(_argsLabel, c);
    add(_argsLabel);
    c.gridy = 1;
    c.gridheight = GridBagConstraints.REMAINDER;
    gb.setConstraints(_argsTable, c);
    add(_argsTable);

    _visField.addItemListener(this);
    _keywordsField.addItemListener(this);
    _typeField.addItemListener(this);

  }


  
  ////////////////////////////////////////////////////////////////
  // accessors

  protected void setTargetInternal(Object t) {
    super.setTargetInternal(t);
    MOperation oper = (MOperation) _target;

    Vector offeredTypes = getOfferedTypes();
    if (offeredTypes != null)
      _typeField.setModel(new DefaultComboBoxModel(Converter.convert(offeredTypes)));

    MVisibilityKind vk = oper.getVisibility();
	if (vk != null)
		_visField.setSelectedItem(vk.getName());

    MScopeKind sk = oper.getOwnerScope();
    MCallConcurrencyKind ck = oper.getConcurrency();
    // needs-more-work: concurrency
    // needs-more-work: final methods?
    if (MScopeKind.CLASSIFIER.equals(sk)) {
      if (MCallConcurrencyKind.GUARDED.equals(ck))
	_keywordsField.setSelectedItem("static synchronized");
      else
	_keywordsField.setSelectedItem("static");
    }
    else {
      if (MCallConcurrencyKind.GUARDED.equals(ck))
	_keywordsField.setSelectedItem("synchronized");
      else
	_keywordsField.setSelectedItem("None");
    }

	Vector parameters = new Vector(oper.getParameters());

	MParameter returnParameter = MMUtil.SINGLETON.getReturnParameter(oper);
	if (returnParameter != null && returnParameter.getType() != null && returnParameter.getType().getName() != null)
		_typeField.setSelectedItem(returnParameter.getType().getName());

	DefaultTableModel _argsModel = new DefaultTableModel(0,2);
	parameters.remove(returnParameter);

	for (int i = 0; i<parameters.size();i++) {
		MParameter p = (MParameter)parameters.elementAt(i);
		String[] row = {p.getName(), p.getType().getName()};
		_argsModel.addRow(row);
	}
	_argsTable.setModel(_argsModel);
  }



  public void setTargetVisibility() {
    if (_target == null) return;
    if (_inChange) return;
    MVisibilityKind vk = MVisibilityKind.forName((String)_visField.getSelectedItem());
    MOperation oper = (MOperation) _target;
	oper.setVisibility(vk);
  }

  // needs-more-work: how to model abstract methods?
  // needs-more-work: how to model final methods?
  
  public void setTargetKeywords() {
    if (_target == null) return;
    if (_inChange) return;
    String keys = (String) _keywordsField.getSelectedItem();
    if (keys == null) {
      //System.out.println("keywords are null");
      return;
    }
    MOperation oper = (MOperation) _target;
	if (keys.equals("None")) {
		oper.setOwnerScope(MScopeKind.INSTANCE);
		oper.setConcurrency(MCallConcurrencyKind.CONCURRENT);
	}
	else if (keys.equals("static")) {
		oper.setOwnerScope(MScopeKind.CLASSIFIER);
		oper.setConcurrency(MCallConcurrencyKind.CONCURRENT);
	}
	else if (keys.equals("final")) {
		oper.setOwnerScope(MScopeKind.INSTANCE);
		oper.setConcurrency(MCallConcurrencyKind.CONCURRENT);
	}
	else if (keys.equals("static final")) {
		oper.setOwnerScope(MScopeKind.INSTANCE);
		oper.setConcurrency(MCallConcurrencyKind.CONCURRENT);
	}
	else if (keys.equals("synchronized")) {
		oper.setOwnerScope(MScopeKind.INSTANCE);
		oper.setConcurrency(MCallConcurrencyKind.GUARDED);
	}
	else if (keys.equals("static synchronized")) {
		oper.setOwnerScope(MScopeKind.CLASSIFIER);
		oper.setConcurrency(MCallConcurrencyKind.GUARDED);
	}
	else if (keys.equals("final synchronized")) {
		oper.setOwnerScope(MScopeKind.INSTANCE);
		oper.setConcurrency(MCallConcurrencyKind.GUARDED);
	}
	else if (keys.equals("static final synchronized")) {
		oper.setOwnerScope(MScopeKind.CLASSIFIER);
		oper.setConcurrency(MCallConcurrencyKind.GUARDED);
	}
  }

  public void setTargetType() {
    if (_target == null) return;
    if (_inChange) return;
    MOperation op = (MOperation) _target;
    Object rtObj = _typeField.getSelectedItem();
    MClassifier rt = null;
    if (rtObj instanceof String) {
      String rtStr = (String) _typeField.getSelectedItem();
      ProjectBrowser pb = ProjectBrowser.TheInstance;
      Project p = pb.getProject();
      rt = p.findType(rtStr);
    }
    else if (rtObj instanceof MClassifier) {
      rt = (MClassifier) rtObj;
    }
    else {
      // watch out for null rtObj
//       System.out.println("selected return type was a " + rtObj.getClass());
//       System.out.println("selected return type is " + rtObj);
      // needs-more-work: selected predefined class
    }
    if (rt != null) {
	//System.out.println("Props setting return type: " + rt);
		MParameter p = new MParameterImpl();
		p.setType(rt);
		MMUtil.SINGLETON.setReturnParameter(op,p);
    }
  }


  public static Vector getOfferedTypes() {
    // needs-more-work: should update when project changes
    Project p = ProjectBrowser.TheInstance.getProject();
    Vector types = p.getDefinedTypesVector();
    Vector res = new Vector();
    for (int i = 0; i<types.size();i++) {
	res.add(((MClassifier)types.get(i)).getName());
    }
    return res;
  }

  ////////////////////////////////////////////////////////////////
  // event handlers


  public void insertUpdate(DocumentEvent e) {
    //System.out.println(getClass().getName() + " insert");
    Component ed = _typeField.getEditor().getEditorComponent();
    Document typeDoc = ((JTextField)ed).getDocument();

    if (e.getDocument() == typeDoc) setTargetType();
    super.insertUpdate(e);
  }

  public void removeUpdate(DocumentEvent e) { insertUpdate(e); }

  public void changedUpdate(DocumentEvent e) {
    System.out.println(getClass().getName() + " changed");
    // Apparently, this method is never called.
  }
  
  public void itemStateChanged(ItemEvent e) {
	  if (e.getStateChange() == ItemEvent.SELECTED) {
		  Object src = e.getSource();
		  if (src == _keywordsField) {
			  //       System.out.println("attr keywords now is " +
			  // 			 _keywordsField.getSelectedItem());
			  setTargetKeywords();
		  }
		  else if (src == _visField) {
			  //       System.out.println("attr MVisibilityKind now is " +
			  // 			 _visField.getSelectedItem());
			  setTargetVisibility();
		  }
		  else if (src == _typeField) {
			  //       System.out.println("attr type now is " +
			  // 			 _typeField.getSelectedItem());
			  setTargetType();
		  }
	  }
  }

  
} /* end class PropPanelOperation */
