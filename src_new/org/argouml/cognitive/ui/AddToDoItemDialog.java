// $Id$
// Copyright (c) 1996-2004 The Regents of the University of California. All
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

import java.awt.event.ActionEvent;
import javax.swing.*;


import org.argouml.cognitive.Designer;
import org.argouml.cognitive.ToDoItem;
import org.argouml.i18n.Translator;
import org.argouml.kernel.ProjectManager;
import org.argouml.swingext.LabelledLayout;
import org.argouml.ui.ArgoDialog;
import org.argouml.ui.ProjectBrowser;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.cognitive.UMLToDoItem;
import org.argouml.uml.ui.UMLListCellRenderer2;
import org.tigris.gef.util.VectorSet;

/**
 * The dialog to enter a new ToDoItem.
 */
public class AddToDoItemDialog extends ArgoDialog {

    ////////////////////////////////////////////////////////////////
    // constants    
    private static final String PRIORITIES[] = {
        Translator.localize("misc.level.high"),
        Translator.localize("misc.level.medium"),
        Translator.localize("misc.level.low")
    };
    private static final int TEXT_ROWS = 8;
    private static final int TEXT_COLUMNS = 30;

    ////////////////////////////////////////////////////////////////
    // instance variables
    private JTextField headLineTextField;
    private JComboBox  priorityComboBox;
    private JTextField moreinfoTextField;
    private JList offenderList;
    private JTextArea  descriptionTextArea;

    /**
     * Create a new AddToDoItemDialog
     */
    public AddToDoItemDialog() {
        super(ProjectBrowser.getInstance(), 
	      Translator.localize("dialog.title.add-todo-item"), 
	      ArgoDialog.OK_CANCEL_OPTION, true);
        
        headLineTextField = new JTextField(TEXT_COLUMNS);
        priorityComboBox = new JComboBox(PRIORITIES);
        moreinfoTextField = new JTextField(TEXT_COLUMNS);
        descriptionTextArea = new JTextArea(TEXT_ROWS, TEXT_COLUMNS);
        
        DefaultListModel dlm = new DefaultListModel();
        Object[] offObj = 
            TargetManager.getInstance().getModelTargets().toArray();
        for (int i = 0; i < offObj.length; i++) {
            if (offObj[i] != null) {
                dlm.addElement(offObj[i]);
            }
        }
        
        offenderList = new JList(dlm);
        offenderList.setCellRenderer(new UMLListCellRenderer2(true));
        JScrollPane offenderScroll = new JScrollPane(offenderList);
        offenderScroll.setOpaque(true);

        JLabel headlineLabel = 
            new JLabel(Translator.localize("label.headline"));
        JLabel priorityLabel = 
            new JLabel(Translator.localize("label.priority"));
        JLabel moreInfoLabel = 
            new JLabel(Translator.localize("label.more-info-url"));
        JLabel offenderLabel = 
            new JLabel("Offenders:"/*Translator.localize("label.offenders")*/);
        priorityComboBox.setSelectedItem(PRIORITIES[0]);

        JPanel panel = new JPanel(new LabelledLayout(getLabelGap(), 
                getComponentGap()));

        headlineLabel.setLabelFor(headLineTextField);
        panel.add(headlineLabel);
        panel.add(headLineTextField);

        priorityLabel.setLabelFor(priorityComboBox);
        panel.add(priorityLabel);
        panel.add(priorityComboBox);

        moreInfoLabel.setLabelFor(moreinfoTextField);
        panel.add(moreInfoLabel);
        panel.add(moreinfoTextField);
    
        offenderLabel.setLabelFor(offenderScroll);
        panel.add(offenderLabel);
        panel.add(offenderScroll);
        
        descriptionTextArea.setLineWrap(true);  //MVW - Issue 2422
        descriptionTextArea.setWrapStyleWord(true);   //MVW - Issue 2422
        descriptionTextArea.setText(Translator.localize("label.enter-todo-item")
                	    + "\n");
        JScrollPane descriptionScroller = new JScrollPane(descriptionTextArea);
        descriptionScroller.setPreferredSize(
                descriptionTextArea.getPreferredSize());
        panel.add(descriptionScroller);
        
        setContent(panel);
    }
    
    ////////////////////////////////////////////////////////////////
    // event handlers

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
        super.actionPerformed(e);      
        if (e.getSource() == getOkButton()) {
            doAdd();
        }
    }
    
    private void doAdd() {
        Designer designer = Designer.theDesigner();
        String headline = headLineTextField.getText();
        int priority = ToDoItem.HIGH_PRIORITY;
        switch (priorityComboBox.getSelectedIndex()) {
	case 0: 
	    priority = ToDoItem.HIGH_PRIORITY; 
	    break;
	case 1: 
	    priority = ToDoItem.MED_PRIORITY; 
	    break;
	case 2:
	    priority = ToDoItem.LOW_PRIORITY; 
	    break;
        }
        String desc = descriptionTextArea.getText();
        String moreInfoURL = moreinfoTextField.getText();
        ToDoItem item =
	    new UMLToDoItem(designer, headline, priority, desc, moreInfoURL);
        VectorSet newOffenders = new VectorSet();
        for (int i = 0; i < offenderList.getModel().getSize(); i++) {
            newOffenders.addElement(offenderList.getModel().getElementAt(i));
        }  
        item.setOffenders(newOffenders);
        designer.getToDoList().addElement(item); //? inform()
        ProjectManager.getManager().getCurrentProject().setNeedsSave(true);
    }
} /* end class AddToDoItemDialog */

