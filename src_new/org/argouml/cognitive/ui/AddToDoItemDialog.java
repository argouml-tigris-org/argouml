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

package org.argouml.cognitive.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import org.argouml.application.api.Argo;
import org.argouml.cognitive.Designer;
import org.argouml.cognitive.ToDoItem;
import org.argouml.swingext.LabelledLayout;
import org.argouml.ui.ProjectBrowser;
import org.tigris.gef.util.VectorSet;

public class AddToDoItemDialog extends JDialog implements ActionListener {

    ////////////////////////////////////////////////////////////////
    // constants
    private static final String BUNDLE = "Cognitive";

    private static final String PRIORITIES[] = {
        Argo.localize(BUNDLE, "level.high"),
        Argo.localize(BUNDLE, "level.medium"),
        Argo.localize(BUNDLE, "level.low")
    };

    ////////////////////////////////////////////////////////////////
    // instance variables
    private JTextField _headline = new JTextField(30);
    private JComboBox  _priority = new JComboBox(PRIORITIES);
    private JTextField _moreinfo = new JTextField(30);
    private JTextArea  _description = new JTextArea(10, 30);
    private JButton _addButton = new JButton(Argo.localize(BUNDLE, "button.add"));
    private JButton _cancelButton = new JButton(Argo.localize(BUNDLE, "button.cancel"));

  ////////////////////////////////////////////////////////////////
  // constructors

    public AddToDoItemDialog() {
        super(ProjectBrowser.getInstance(), Argo.localize(BUNDLE, "dialog.title.add-todo-item"), true);
        JLabel headlineLabel = new JLabel(Argo.localize(BUNDLE, "label.headline"));
        JLabel priorityLabel = new JLabel(Argo.localize(BUNDLE, "label.priority"));
        JLabel moreInfoLabel = new JLabel(Argo.localize(BUNDLE, "label.more-info-url"));
    
        _priority.setSelectedItem(PRIORITIES[0]);
    
        JPanel panel = new JPanel(new LabelledLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        getContentPane().add(panel);

        headlineLabel.setLabelFor(_headline);
        headlineLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        panel.add(headlineLabel);
        panel.add(_headline);

        priorityLabel.setLabelFor(_priority);
        priorityLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        panel.add(priorityLabel);
        panel.add(_priority);

        moreInfoLabel.setLabelFor(_moreinfo);
        moreInfoLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        panel.add(moreInfoLabel);
        panel.add(_moreinfo);
    
        _description.setText(Argo.localize(BUNDLE, "label.enter-todo-item") + "\n");
        JPanel descriptionPanel = new JPanel(new BorderLayout());
        JScrollPane descriptionScroller = new JScrollPane(_description);
        descriptionScroller.setPreferredSize(_description.getPreferredSize());
        panel.add(descriptionScroller);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.add(_addButton);
        buttonPanel.add(_cancelButton);
        panel.add(buttonPanel);

        getRootPane().setDefaultButton(_addButton);
        _addButton.addActionListener(this);
        _cancelButton.addActionListener(this);

        pack();
        
        centerOnParent();
    }

    private void centerOnParent() {
        Dimension size = getSize();
        Dimension p = getParent().getSize();
        int x = (getParent().getX() - size.width) + (int) ((size.width + p.width) / 2d);
        int y = (getParent().getY() - size.height) + (int) ((size.height + p.height) / 2d);
        setLocation(x, y);
    }
    
  ////////////////////////////////////////////////////////////////
  // event handlers
  public void actionPerformed(ActionEvent e) {
    if (e.getSource() == _addButton) {
      Designer dsgr = Designer.TheDesigner;
      String head = _headline.getText();
      int pri = ToDoItem.HIGH_PRIORITY;
      switch (_priority.getSelectedIndex()) {
      case 0: pri = ToDoItem.HIGH_PRIORITY; break;
      case 1: pri = ToDoItem.MED_PRIORITY; break;
      case 2: pri = ToDoItem.LOW_PRIORITY; break;
      }
      String desc = _description.getText();
      String more = _moreinfo.getText();
      VectorSet offs = new VectorSet();  //? null
      ToDoItem item = new ToDoItem(dsgr, head, pri, desc, more, offs);
      dsgr.getToDoList().addElement(item); //? inform()
      setVisible(false);
      dispose();
    }
    if (e.getSource() == _cancelButton) {
      hide();
      dispose();
    }
  }

} /* end class AddToDoItemDialog */
