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
import com.sun.java.util.collections.*;
import java.beans.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import javax.swing.text.*;
//import javax.swing.border.*;

import uci.util.*;
import ru.novosoft.uml.*;
import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.foundation.data_types.*;
import ru.novosoft.uml.foundation.extension_mechanisms.*;
import ru.novosoft.uml.model_management.*;
import uci.uml.ui.*;

/** This is an abstract superclass for panels that go in the
 *  "Properties" tab in the Argo/UML user interface.
 *
 * @see TabProps */

public class PropPanel extends TabSpawnable
implements TabModelTarget, DocumentListener, MElementListener{
  ////////////////////////////////////////////////////////////////
  // instance vars
  Object _target;
  boolean _inChange = false;
  JLabel _nameLabel = new JLabel("Name: ");
  JTextField _nameField = new JTextField();
  JLabel _stereoLabel = new JLabel("Stereotype: ");
  JComboBox _stereoField = new JComboBox();
  JLabel _namespaceLabel = new JLabel("Namespace: ");
  JComboBox _namespaceField = new JComboBox();

  ////////////////////////////////////////////////////////////////
  // constructors

  public PropPanel(String title) {
    super(title);
    _stereoField.setEditable(true);
    _stereoField.getEditor().getEditorComponent().setBackground(Color.white);
    _namespaceField.setEditable(true);
    _namespaceField.getEditor().getEditorComponent().setBackground(Color.white);

    GridBagLayout gb = new GridBagLayout();
    setLayout(gb);
    GridBagConstraints c = new GridBagConstraints();
    c.fill = GridBagConstraints.BOTH;
    c.weightx = 0.0;
    c.weighty = 0.0;
    c.ipadx = 3; c.ipady = 3;

    c.gridx = 0;
    c.gridwidth = 1;
    c.gridy = 0;
    gb.setConstraints(_nameLabel, c);
    add(_nameLabel);
    c.gridy = 9;
    gb.setConstraints(_stereoLabel, c);
    add(_stereoLabel);
    c.gridy = 10;
    gb.setConstraints(_namespaceLabel, c);
    add(_namespaceLabel);


    _nameField.setMinimumSize(new Dimension(120, 20));
    c.weightx = 1.0;
    c.gridx = 1;
    //c.gridwidth = GridBagConstraints.REMAINDER;
    c.gridy = 0;
    gb.setConstraints(_nameField, c);
    add(_nameField);
    c.gridy = 9;
    gb.setConstraints(_stereoField, c);
    add(_stereoField);
    c.gridy = 10;
    gb.setConstraints(_namespaceField, c);
    add(_namespaceField);

    _nameField.getDocument().addDocumentListener(this);
    _nameField.setFont(_stereoField.getFont());

    Component ed = _stereoField.getEditor().getEditorComponent();
    Document stereoDoc = ((JTextField)ed).getDocument();
    stereoDoc.addDocumentListener(this);

    ed = _namespaceField.getEditor().getEditorComponent();
    Document nsDoc = ((JTextField)ed).getDocument();
    nsDoc.addDocumentListener(this);
  }

  protected SetTargetRunner setTargetRunner = new SetTargetRunner();

  class SetTargetRunner implements Runnable {
    public boolean scheduled = false;
    public void run() {
      setTarget(_target);
      scheduled = false;
    }
  }

  protected void scheduleSetTarget() {
    if (!setTargetRunner.scheduled) {
      SwingUtilities.invokeLater(setTargetRunner);
    }
  }

  ////////////////////////////////////////////////////////////////
  // accessors

  public void setTarget(Object t) {
    try {
      _inChange = true;
      setTargetInternal(t);
    }
    finally {
      _inChange = false;
    }
  }

  protected void setTargetInternal(Object t) {
    if (! t.equals(_target)) {
     if ( _target instanceof MBase )
       ((MBase)t).removeMElementListener(this);
    _target = t;
     if ( _target instanceof MBase )
       ((MBase)t).addMElementListener(this);
    }
    MModelElement me = (MModelElement) _target;
    String n = me.getName();
    // this is a small hack to get the text to update for an empty string
    if ((n == null) || n.equals("")) _nameField.setText(" ");
    _nameField.setText(n);
    _nameField.setCaretPosition(0);
    MStereotype stereo = me.getStereotype();
    JTextField ed = (JTextField) _stereoField.getEditor().getEditorComponent();
    if (stereo != null) {
      String stereoName = stereo.getName();
      // needs-more-work: select from list
      _stereoField.setSelectedItem(stereo);
      ed.setText(stereoName);
    }
    else {
      _stereoField.setSelectedItem(null);
      ed.setText("");
    }
    MNamespace ns = me.getNamespace();
    ed = (JTextField) _namespaceField.getEditor().getEditorComponent();
    if (ns != null) {
      String namespaceName = ns.getName();
      while (ns.getNamespace() != null) {
	namespaceName = ns.getNamespace().getName() +
	  "." + namespaceName;
	ns = ns.getNamespace();
      }
      
      // needs-more-work: select from list
      _namespaceField.setSelectedItem(ns);
      ed.setText(namespaceName);
    }
    else {
      _namespaceField.setSelectedItem(null);
      ed.setText("");
    }
  }

  public Object getTarget() { return _target; }

  public void refresh() { setTarget(_target); }

  public boolean shouldBeEnabled() { return _target instanceof MModelElement; }


  protected void setTargetName() {
    if (_target == null) return;
    if (_inChange) return;
	String newName = _nameField.getText();
	((MModelElement)_target).setName(newName);
  }

  protected void setTargetStereotype() {
    if (_target == null) return;
    if (_inChange) return;
	MModelElement me = (MModelElement) _target;
	// needs-more-work: find predefined stereotype
	Component ed = _stereoField.getEditor().getEditorComponent();
	String stereoName = ((JTextField)ed).getText();
	MNamespace m = ProjectBrowser.TheInstance.getProject().getCurrentNamespace();
	MStereotype s = null;
   	Object eo = m.lookup(stereoName);
	if (eo != null && eo instanceof MStereotype)
		s = (MStereotype) eo;
	else {
		s = new MStereotypeImpl();
		s.setName(stereoName);
		m.addOwnedElement(s);
	}
	me.setStereotype(s);
  }
	
  ////////////////////////////////////////////////////////////////
  // DocumentListener implementation

  public void insertUpdate(DocumentEvent e) {
    //System.out.println(getClass().getName() + " insert");
    if (e.getDocument() == _nameField.getDocument()) setTargetName();
    Component ed = _stereoField.getEditor().getEditorComponent();
    Document stereoDoc = ((JTextField)ed).getDocument();
    if (e.getDocument() == stereoDoc) setTargetStereotype();
  }

  public void removeUpdate(DocumentEvent e) { insertUpdate(e); }

  public void changedUpdate(DocumentEvent e) {
    //System.out.println(getClass().getName() + " changed");
    // Apparently, this method is never called.
  }

	// MElementListener implementation

	public void propertySet(MElementEvent mee) {
           if (( mee.getName().equals("name"))
           &&  ( mee.getType() == MElementEvent.ATTRIBUTE_SET )
           &&  ( !_nameField.hasFocus())) { 
	     scheduleSetTarget();	
           }
           
	}
	public void listRoleItemSet(MElementEvent mee) {
	}
	public void recovered(MElementEvent mee) {
	}
	public void removed(MElementEvent mee) {
	}
	public void roleAdded(MElementEvent mee) {
	}
	public void roleRemoved(MElementEvent mee) {
	}


} /* end class PropPanel */
