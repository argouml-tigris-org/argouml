// Copyright (c) 1996-98 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation for educational, research and non-profit
// purposes, without fee, and without a written agreement is hereby granted,
// provided that the above copyright notice and this paragraph appear in all
// copies. Permission to incorporate this software into commercial products
// must be negotiated with University of California. This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "as is",
// without any accompanying services from The Regents. The Regents do not
// warrant that the operation of the program will be uninterrupted or
// error-free. The end-user understands that the program was developed for
// research purposes and is advised not to rely exclusively on the program for
// any reason. IN NO EVENT SHALL THE UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY
// PARTY FOR DIRECT, INDIRECT, SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES,
// INCLUDING LOST PROFITS, ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS
// DOCUMENTATION, EVEN IF THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE
// POSSIBILITY OF SUCH DAMAGE. THE UNIVERSITY OF CALIFORNIA SPECIFICALLY
// DISCLAIMS ANY WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
// WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE
// SOFTWARE PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF
// CALIFORNIA HAS NO OBLIGATIONS TO PROVIDE MAINTENANCE, SUPPORT, UPDATES,
// ENHANCEMENTS, OR MODIFICATIONS.




package uci.uml.ui;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import com.sun.java.swing.*;
import com.sun.java.swing.event.*;

import uci.util.*;
import uci.argo.kernel.*;
import uci.uml.visual.*;
import uci.uml.Foundation.Core.*;
import uci.uml.generate.*;

public class ClassGenerationDiaglog extends JFrame implements ActionListener {

  ////////////////////////////////////////////////////////////////
  // constants
  public static final String PRIORITIES[] = { "High", "Medium", "Low" };
  public static final int WIDTH = 300;
  public static final int HEIGHT = 350;

  ////////////////////////////////////////////////////////////////
  // instance variables

  // JTable of classes
  protected JTextField _dir = new JTextField();
  protected JButton _generateButton = new JButton("Generate");
  protected JButton _cancelButton = new JButton("Cancel");
  protected UMLClassDiagram _diagram = null;

  ////////////////////////////////////////////////////////////////
  // constructors

  public ClassGenerationDiaglog(UMLClassDiagram d) {
    super("Generate Classes");
    _diagram = d;
    JLabel promptLabel = new JLabel("Generate Classes:");
    JLabel dirLabel = new JLabel("Output Directory:");


    setSize(new Dimension(WIDTH, HEIGHT));
    getContentPane().setLayout(new BorderLayout());
    JPanel top = new JPanel();
    GridBagLayout gb = new GridBagLayout();
    top.setLayout(gb);
    GridBagConstraints c = new GridBagConstraints();
    c.fill = GridBagConstraints.BOTH;
    c.weightx = 0.0;
    c.ipadx = 3; c.ipady = 3;


    c.gridx = 0;
    c.gridwidth = 1;
    c.gridy = 0;
    gb.setConstraints(promptLabel, c);
    top.add(promptLabel);
    c.gridy = 1;
    gb.setConstraints(dirLabel, c);
    top.add(dirLabel);

    c.weightx = 1.0;
    c.gridx = 0;
    c.gridwidth = GridBagConstraints.REMAINDER;
    c.gridy = 2;
    gb.setConstraints(_dir, c);
    top.add(_dir);

    JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
    JPanel buttonInner = new JPanel(new GridLayout(1, 2));
    buttonInner.add(_generateButton);
    buttonInner.add(_cancelButton);
    buttonPanel.add(buttonInner);

    ProjectBrowser pb = ProjectBrowser.TheInstance;
    Project p = pb.getProject();
    _dir.setText(p.getGenerationPrefs().outputDir);

    Rectangle pbBox = pb.getBounds();
    setLocation(pbBox.x + (pbBox.width - WIDTH)/2,
		pbBox.y + (pbBox.height - HEIGHT)/2);
    getContentPane().add(top, BorderLayout.NORTH);
    getContentPane().add(buttonPanel, BorderLayout.SOUTH);

    getRootPane().setDefaultButton(_generateButton);
    _generateButton.addActionListener(this);
    _cancelButton.addActionListener(this);
  }


  ////////////////////////////////////////////////////////////////
  // event handlers
  public void actionPerformed(ActionEvent e) {
    if (e.getSource() == _generateButton) {
      String path = _dir.getText().trim();
      Vector nodes = _diagram.getGraphModel().getNodes();
      int size = nodes.size();
      for (int i = 0; i <size; i++) {
	Object node = nodes.elementAt(i);
	if (node instanceof Classifier)
	  GeneratorJava.GenerateFile((Classifier) node, path);
      }
      hide();
      dispose();
    }
    if (e.getSource() == _cancelButton) {
      //System.out.println("cancel");
      hide();
      dispose();
    }
  }

} /* end class ClassGenerationDiaglog */
