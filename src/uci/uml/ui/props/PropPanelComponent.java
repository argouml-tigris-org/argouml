
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
import javax.swing.border.*;
import javax.swing.plaf.metal.MetalLookAndFeel;

import uci.util.*;
import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.foundation.data_types.*;
import ru.novosoft.uml.model_management.*;
import uci.uml.ui.*;


public class PropPanelComponent extends PropPanel 
implements ItemListener, DocumentListener {

  ////////////////////////////////////////////////////////////////
  // constants
  // needs-more-work 

  ////////////////////////////////////////////////////////////////
  // instance vars
  public JLabel _deploymentLocationLabel = new JLabel("Deployment-Location:");
  public static JTextField _deploymentLocationField = new JTextField();

  // declare and initialize all widgets

  ////////////////////////////////////////////////////////////////
  // contructors
  public PropPanelComponent() {
    super("Component Properties");
    _deploymentLocationField.setEditable(false);
    GridBagLayout gb = (GridBagLayout) getLayout();
    GridBagConstraints c = new GridBagConstraints();
    c.fill = GridBagConstraints.BOTH;
    c.weightx = 0.0;
    c.ipadx = 0; c.ipady = 0;


    c.gridx = 0;
    c.gridwidth = 1;
    c.gridy = 1;
    c.weightx = 0.0;
    gb.setConstraints(_deploymentLocationLabel, c);
    add(_deploymentLocationLabel);
        
    c.weightx = 1.0;
    c.gridx = 1;
    c.gridy = 1;
    _deploymentLocationField.setMinimumSize(new Dimension(120, 20));
    gb.setConstraints(_deploymentLocationField, c);
    add(_deploymentLocationField);
    _deploymentLocationField.getDocument().addDocumentListener(this);
    _deploymentLocationField.setFont(_stereoField.getFont());

  }

  ////////////////////////////////////////////////////////////////
  // accessors

  /** Set the values to be shown in all widgets based on model */
  protected void setTargetInternal(Object t) {
    super.setTargetInternal(t);
    MComponent co = (MComponent) t;
    Collection nodes = co.getDeploymentLocations();  
    MNode node = null;
    if ((co.getDeploymentLocations() != null) && (nodes.size()>0)) {
      Iterator it = nodes.iterator();
      while (it.hasNext()) {
        node = (MNode) it.next();
      }
      _deploymentLocationField.setText(node.getName());
    }
    else {
      _deploymentLocationField.setText(null);
    }

    validate();
  }
  
 

  ////////////////////////////////////////////////////////////////
  // event handlers


  /** The user typed some text */
  public void insertUpdate(DocumentEvent e) {
    //System.out.println(getClass().getName() + " insert");
    // check if it was one of my text fields
    super.insertUpdate(e);
 }

  public void removeUpdate(DocumentEvent e) { insertUpdate(e); }

  public void changedUpdate(DocumentEvent e) {
    System.out.println(getClass().getName() + " changed");
    // Apparently, this method is never called.
  }

  /** The user modified one of the widgets */
  public void itemStateChanged(ItemEvent e) {
    Object src = e.getSource();
    // check for each widget, and update the model with new value
  }

  static final long serialVersionUID = 4536645723645617622L;
  
} /* end class PropPanelComponent */



