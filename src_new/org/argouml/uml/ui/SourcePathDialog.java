// Copyright (c) 1996-2002 The Regents of the University of California. All
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

package org.argouml.uml.ui;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

//import org.argouml.kernel.Project;
//import org.argouml.kernel.ProjectManager;
import org.argouml.model.uml.modelmanagement.ModelManagementHelper;
import org.argouml.ui.ProjectBrowser;
import org.argouml.uml.generator.Generator;

import ru.novosoft.uml.model_management.*;
import ru.novosoft.uml.foundation.core.*;

public class SourcePathDialog extends JDialog implements ActionListener {

  ////////////////////////////////////////////////////////////////
  // instance variables
  private SrcPathTableModel _srcPathTableModel = new SrcPathTableModel();

  protected JTable _srcPathTable;
  protected JButton _cancelButton;
  protected JButton _okButton;
  protected JScrollPane _srcPathScrollPane;

  ////////////////////////////////////////////////////////////////
  // constructors

  public SourcePathDialog() {
    super(ProjectBrowser.TheInstance, "Generate Code for Project");

    GridBagConstraints gridBagConstraints;

    _cancelButton = new JButton();
    _okButton = new JButton();
    _srcPathScrollPane = new JScrollPane();
    _srcPathTable = new JTable();

    getContentPane().setLayout(new GridBagLayout());

    _cancelButton.setText("Cancel");
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.insets = new Insets(5, 5, 5, 5);
    gridBagConstraints.anchor = GridBagConstraints.EAST;
    getContentPane().add(_cancelButton, gridBagConstraints);

    _okButton.setText("Ok");
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.insets = new Insets(5, 5, 5, 5);
    gridBagConstraints.anchor = GridBagConstraints.EAST;
    getContentPane().add(_okButton, gridBagConstraints);

    _srcPathTable.setModel(_srcPathTableModel);
    _srcPathTable.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
    _srcPathTable.setShowVerticalLines(false);
    _srcPathTable.setIntercellSpacing(new Dimension(0, 1));
    TableColumn elemCol = _srcPathTable.getColumnModel().getColumn(0);
    elemCol.setMinWidth(0);
    elemCol.setMaxWidth(0);
    elemCol = null;
    _srcPathScrollPane.setViewportView(_srcPathTable);

    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
    gridBagConstraints.fill = GridBagConstraints.BOTH;
    gridBagConstraints.insets = new Insets(5, 5, 5, 5);
    gridBagConstraints.weighty = 2.0;
    getContentPane().add(_srcPathScrollPane, gridBagConstraints);

    pack();

    // Center Dialog on Screen -- todo: this should be a support function
    ProjectBrowser pb = ProjectBrowser.TheInstance;
    Rectangle pbBox = pb.getBounds();
    setLocation(pbBox.x + (pbBox.width - this.getWidth())/2,
    		pbBox.y + (pbBox.height - this.getHeight())/2);

    getRootPane().setDefaultButton(_okButton);
    _okButton.addActionListener(this);
    _cancelButton.addActionListener(this);
  }


  ////////////////////////////////////////////////////////////////
  // event handlers

  public void actionPerformed(ActionEvent e) {
    // Cancel Button ------------------------------------------
    if (e.getSource() == _cancelButton) {
      buttonCancelActionPerformed();
    }
    // Ok Button ------------------------------------------
    if (e.getSource() == _okButton) {
      buttonOkActionPerformed();
    }
  }

  public void buttonCancelActionPerformed() {
    setVisible(false);
    dispose();
  }

  public void buttonOkActionPerformed() {
    for (int i = 0; i < _srcPathTableModel.getRowCount(); i++) {
      MModelElement elem = (MModelElement)_srcPathTableModel.getValueAt(i,0);
      String path = (String)_srcPathTableModel.getValueAt(i,3);
      if (elem != null && path != null && !path.equals(elem.getTaggedValue("src_path"))) {
        elem.setTaggedValue("src_path",path);
      }
    }
    buttonCancelActionPerformed();
  }
} /* end class SourcePathDialog */


class SrcPathTableModel extends DefaultTableModel {

  /** Creates a new instance of SrcPathTableModel */
  public SrcPathTableModel() {
    super(
      new Object [][] {},
      new String [] {"", "Name", "Type", "Source path"}
    );
    // The following lines should be substituted by the following 2 commented lines.
    // (This is because getting the project still does not seem to work...)
    ProjectBrowser pb = ProjectBrowser.TheInstance;
    org.argouml.ui.ArgoDiagram activeDiagram = pb.getActiveDiagram();
    if (!(activeDiagram instanceof org.argouml.uml.diagram.ui.UMLDiagram)) return;
    ru.novosoft.uml.foundation.core.MNamespace ns = ((org.argouml.uml.diagram.ui.UMLDiagram)activeDiagram).getNamespace();
    if (ns == null) return;
    while (ns.getNamespace() != null) ns = ns.getNamespace();
    Collection elems = ModelManagementHelper.getHelper().getAllModelElementsOfKind(ns,MModelElement.class);
    //Project p = ProjectManager.getManager().getCurrentProject();
    //Collection elems = ModelManagementHelper.getHelper().getAllModelElementsOfKind(MClassifier.class);
    Iterator iter = elems.iterator();
    while (iter.hasNext()) {
      MModelElement me = (MModelElement)iter.next();
      String path = Generator.getCodePath(me);
      if (path != null) {
        String type = "";
        String name = me.getName();
        if (me instanceof MModel) {
          type = "Model";
        }
        else if (me instanceof MPackage) {
          type = "Package";
          MNamespace parent = me.getNamespace();
          while (parent != null) {
            // ommit root package name; it's the model's root
            if (parent.getNamespace() != null)
              name = parent.getName() + "." + name;
            parent = parent.getNamespace();
          }
        }
        else if (me instanceof MClass) {
          type = "Class";
        }
        else if (me instanceof MInterface) {
          type = "Interface";
        }
        addRow(new Object[] {me, name, type, path});
      }
    }
  }

  public boolean isCellEditable(int rowIndex, int columnIndex) {
    return columnIndex == 3;
  }
}
