package uci.uml.ui;

import java.awt.*;
import com.sun.java.swing.*;
import com.sun.java.swing.event.*;
import com.sun.java.swing.tree.*;
import com.sun.java.swing.text.*;
import com.sun.java.swing.border.*;

import uci.util.*;
import uci.uml.Foundation.Core.Element;
import uci.uml.Foundation.Data_Types.*;
import uci.uml.Model_Management.*;



class ModelElementRenderer extends JLabel implements ListCellRenderer {
  // This is the only method defined by ListCellRenderer.  We just
  // reconfigure the Jlabel each time we're called.

  public Component getListCellRendererComponent(JList list,
						Object value,
						int index,   
						boolean isSelected,
						boolean cellHasFocus) {
    if (value instanceof Element)
      setText(((Element)value).getName().getBody());
    else
      setText(value.toString());
      
    //setIcon((s.length > 10) ? longIcon : shortIcon);
    return this;
  }
} /* end class ModelElementListRenderer */
