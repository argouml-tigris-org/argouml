
package uci.uml.ui;

import uci.argo.kernel.*;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import com.sun.java.swing.*;
import com.sun.java.swing.event.*;
import com.sun.java.swing.tree.*;
// import com.sun.java.swing.text.*;
// import com.sun.java.swing.border.*;

import uci.util.*;
import uci.ui.*;
import uci.gef.*;
import uci.argo.kernel.*;
import uci.uml.Foundation.Core.*;
import uci.uml.Model_Management.*;


public class DesignIssuesFrame extends JDialog
implements ActionListener {

  ////////////////////////////////////////////////////////////////
  // instance variables
  protected JLabel _diLabel = new JLabel("Design Issues");
  protected JPanel  _mainPanel = new JComboBox(PRIORITIES);
  protected JButton _okButton = new JButton("OK");

  ////////////////////////////////////////////////////////////////
  // constructors

  public DesignIssuesFrame(JFrame parent) {
    super("Design Issues");
    Container content = getContentPane();
    content.setLayout(new BorderLayout());
    initMainPanel();
    content.add(_diLabel, BorderLayout.NORTH);
    content.add(_mainPanel, BorderLayout.CENTER);
    content.add(_okButton, BorderLayout.SOUTH);
    _okButton.addActionListener(this);
  }
  
  ////////////////////////////////////////////////////////////////
  // event handlers
  
  public void actionPerformed(ActionEvent e) {
    if (e.getSource() == _okButton) {
      hide();
      dispose();
    }
  }

} /* end class DesignIssuesFrame */
