package uci.uml.ui;

import com.sun.java.swing.*;
import uci.uml.Foundation.Core.Element;
import uci.uml.Foundation.Core.ElementImpl;

public class DisplayTextTree extends JTree {

  public DisplayTextTree() {
    setCellRenderer(new UMLTreeCellRenderer());
  }

  public String convertValueToText(Object value, boolean selected,
				   boolean expanded, boolean leaf, int row,
				   boolean hasFocus) {
    if (value == null) return "(null)";
    if (value instanceof Element) {
      Element e = (Element) value;
      String ocl = "";
      if (e instanceof ElementImpl)
	ocl = ((ElementImpl)e).getOCLTypeStr();
      String name = e.getName().getBody();
      if (name.equals("")) name = "(anon)";
      return ocl + ": " + name;
    }
    return value.toString();
  }

} /* end class DisplayTextTree */
