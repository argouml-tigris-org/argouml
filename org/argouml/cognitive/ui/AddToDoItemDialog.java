// $Id$
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

import java.awt.event.ActionEvent;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.cognitive.Designer;
import org.argouml.cognitive.ToDoItem;
import org.argouml.i18n.Translator;
import org.argouml.kernel.ProjectManager;
import org.argouml.swingext.LabelledLayout;
import org.argouml.ui.ArgoDialog;
import org.argouml.ui.ProjectBrowser;

import org.tigris.gef.util.VectorSet;

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
    private JTextField _headline;
    private JComboBox  _priority;
    private JTextField _moreinfo;
    private JList _offenderList;
    private JTextArea  _description;

    /**
     * Create a new AddToDoItemDialog
     */
    public AddToDoItemDialog() {
        super(ProjectBrowser.getInstance(), 
	      Translator.localize("dialog.title.add-todo-item"), 
	      ArgoDialog.OK_CANCEL_OPTION, true);
        
        _headline = new JTextField(TEXT_COLUMNS);
        _priority = new JComboBox(PRIORITIES);
        _moreinfo = new JTextField(TEXT_COLUMNS);
        _description = new JTextArea(TEXT_ROWS, TEXT_COLUMNS);
        _offenderList = new JList(TargetManager.
            getInstance().getModelTargets().toArray());

        JLabel headlineLabel = new JLabel(Translator.localize("label.headline"));
        JLabel priorityLabel = new JLabel(Translator.localize("label.priority"));
        JLabel moreInfoLabel = new JLabel(Translator.localize(BUNDLE, "label.more-info-url"));
        JLabel offenderLabel = new JLabel("Offenders:"/*Translator.localize("label.offenders")*/);
        _priority.setSelectedItem(PRIORITIES[0]);

        JPanel panel = new JPanel(new LabelledLayout(labelGap, componentGap));

        headlineLabel.setLabelFor(_headline);
        panel.add(headlineLabel);
        panel.add(_headline);

        priorityLabel.setLabelFor(_priority);
        panel.add(priorityLabel);
        panel.add(_priority);

        moreInfoLabel.setLabelFor(_moreinfo);
        panel.add(moreInfoLabel);
        panel.add(_moreinfo);
    
        offenderLabel.setLabelFor(_offenderList);
        panel.add(offenderLabel);
        panel.add(_offenderList);
        
        _description.setText(Translator.localize("label.enter-todo-item") + "\n");
        JScrollPane descriptionScroller = new JScrollPane(_description);
        descriptionScroller.setPreferredSize(_description.getPreferredSize());
        panel.add(descriptionScroller);
        
        setContent(panel);
    }
    
    ////////////////////////////////////////////////////////////////
    // event handlers
    public void actionPerformed(ActionEvent e) {
        super.actionPerformed(e);      
        if (e.getSource() == getOkButton()) {
            doAdd();
        }
    }
    
    private void doAdd() {
        Designer designer = Designer.TheDesigner;
        String headline = _headline.getText();
        int priority = ToDoItem.HIGH_PRIORITY;
        switch (_priority.getSelectedIndex()) {
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
        String desc = _description.getText();
        String moreInfoURL = _moreinfo.getText();
        ToDoItem item =
	    new ToDoItem(designer, headline, priority, desc, moreInfoURL);
        VectorSet newOffenders = new VectorSet();
        for (int i = 0; i < _offenderList.getModel().getSize(); i++) {
            newOffenders.addElement(_offenderList.getModel().getElementAt(i));
        }  
        item.setOffenders(newOffenders);
        designer.getToDoList().addElement(item); //? inform()
        ProjectManager.getManager().getCurrentProject().setNeedsSave(true);
    }
} /* end class AddToDoItemDialog */
