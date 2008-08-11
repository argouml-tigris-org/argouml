// $Id: svn:keywords $
// Copyright (c) 2008 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies. This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason. IN NO EVENT SHALL THE
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

package org.argouml.uml.diagram.static_structure.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.argouml.i18n.Translator;
import org.argouml.model.CoreHelper;
import org.argouml.model.Model;
import org.argouml.model.VisibilityKind;
import org.argouml.uml.diagram.ui.CreateWizard;
import org.argouml.uml.ui.UMLComboBox2;
import org.argouml.uml.ui.UMLComboBoxModel2;
import org.argouml.uml.ui.foundation.core.ActionSetStructuralFeatureType;
import org.argouml.uml.ui.foundation.core.UMLStructuralFeatureTypeComboBoxModel;

/**
 * Wizard for creating a new Class.
 * 
 * @author bszanto
 */
public class ClassCreateWizard implements CreateWizard {

    /** WIDTH of the dialog */
    public final static int WIDTH = 600;
    /** HEIGHT of the dialog */
    public final static int HEIGHT = 450;

    /** The wizard dialog */
    private JDialog dialog = null;

    private JTextField txtName = null;

    /** The new class */
    private Object classNode = null;

    /** Attributes */
    private Vector<Attribute> attributes = new Vector<Attribute>();

    /** Operations */
    private Vector<Operation> operations = new Vector<Operation>();

    /**
     * Constructor
     */
    public ClassCreateWizard() {
        dialog = new JDialog();
        dialog.setTitle(Translator.localize("dialog.title.wizard-class"));
        dialog.setSize(WIDTH, HEIGHT);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        dialog.setLocation((screenSize.width - WIDTH) / 2,
                (screenSize.height - HEIGHT) / 2);
        dialog.setResizable(false);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        
        
        dialog.getContentPane().setLayout(new BorderLayout());

        JPanel pnlName = new JPanel();
        pnlName.add(new JLabel(Translator.localize("label.name")));
        txtName = new JTextField();
        txtName.setPreferredSize(new Dimension(120, 25));
        pnlName.add(txtName);
        dialog.getContentPane().add(pnlName, BorderLayout.NORTH);

        JPanel pnlContent = new JPanel();

        JPanel pnlAtt = new JPanel();
        pnlAtt.setBorder(BorderFactory.createTitledBorder(Translator
                .localize("label.attributes")));
        pnlAtt.setLayout(new BoxLayout(pnlAtt, BoxLayout.PAGE_AXIS));
        JPanel pnlOp = new JPanel();
        pnlOp.setBorder(BorderFactory.createTitledBorder(Translator
                .localize("label.operations")));
        pnlOp.setLayout(new BoxLayout(pnlOp, BoxLayout.PAGE_AXIS));
        for (int i = 0; i < 4; i++) {
            attributes.add(new Attribute());
            pnlAtt.add(attributes.get(i));

            operations.add(new Operation());
            pnlOp.add(operations.get(i));
        }
        pnlContent.add(pnlAtt);
        pnlContent.add(pnlOp);

        dialog.getContentPane().add(pnlContent, BorderLayout.CENTER);

        JPanel pnlButtons = new JPanel();
        JButton btnOk = new JButton(Translator.localize("button.ok"));
        btnOk.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                okPressed();
            }
        });
        JButton btnCancel = new JButton(Translator.localize("button.cancel"));
        btnCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dialog.setVisible(false);
            }
        });
        pnlButtons.add(btnOk);
        pnlButtons.add(btnCancel);
        dialog.getContentPane().add(pnlButtons, BorderLayout.SOUTH);

        dialog.setModal(true);
    }

    public Object display() {
        dialog.setVisible(true);
        // the next line is executed only when the dialog is not visible anymore
        return classNode;
    }
    
    /**
     * When the OK button is pressed the new class is created, and the 
     * attribtutes and operations are added.
     */
    public void okPressed() {
        classNode = Model.getCoreFactory().createClass();
        
        /* setting name */
        Model.getCoreHelper().setName(classNode, txtName.getText());
        
        /* adding attributes */
        Vector<Object> attList = new Vector<Object>();
        Object temp;
        for (int i = 0; i < attributes.size(); i++) {
            temp = attributes.get(i).getAttribute();
            if (temp != null) {
                attList.add(temp);
            }
        }
        Model.getCoreHelper().setAttributes(classNode, attList);
        
        /* adding operations */
        Vector<Object> opList = new Vector<Object>();
        for (int i = 0; i < operations.size(); i++) {
            temp = operations.get(i).getOperation();
            if (temp != null) {
                opList.add(temp);
            }
        }
        Model.getCoreHelper().setOperations(classNode, opList);
        
        dialog.setVisible(false);
    }

}

class Attribute extends JPanel {

    /** Name field */
    private JTextField name;
    /** Visibility field */
    private JComboBox visibility;
    /** Type field */
    private UMLComboBox2 type;
    
    /** The newly created attribue */
    private Object attribute = null;

    /**
     * Constructor.
     */
    public Attribute() {
        super();
        attribute = Model.getCoreFactory().createAttribute();         
        
        String[] vis = new String[] { "public(+)", "package(~)",
                "protected(#)", "private(-)" };
        visibility = new JComboBox(vis);
        visibility.setPreferredSize(
                new Dimension((int) (1.5 * ClassCreateWizard.WIDTH / 10), 25));
        UMLComboBoxModel2 model = new UMLStructuralFeatureTypeComboBoxModel();
        model.setTarget(attribute);
        type = new UMLComboBox2(model,
                ActionSetStructuralFeatureType.getInstance());
        type.setSelectedIndex(0);
        type.setPreferredSize(
                new Dimension(3 * ClassCreateWizard.WIDTH / 10, 25));
        name = new JTextField();
        name.setPreferredSize(
                new Dimension(3 * ClassCreateWizard.WIDTH / 10, 25));

        this.add(visibility);
        this.add(type);
        this.add(name);
    }

    /**
     * @return The newly created attribute, or null if a name was not specified.
     */
    public Object getAttribute() {    
        if (name.getText().length() > 0) {
            CoreHelper help = Model.getCoreHelper();
            VisibilityKind vk = Model.getVisibilityKind();
            
            help.setName(attribute, name.getText());
            
            Object vis = null;
            int index = visibility.getSelectedIndex();
            switch (index) {
            case 1:
                vis = vk.getPublic();
                break;
            case 2:
                vis = vk.getPackage();
                break;
            case 3:
                vis = vk.getProtected();
                break;
            default:
                vis = vk.getPrivate();
            }
            help.setVisibility(attribute, vis);
            
            help.setType(attribute, type.getSelectedItem());
            
            help.setReadOnly(attribute, false);
        } else {
            attribute = null;
        }
        return attribute;
    }
}

class Operation extends JPanel {
    /** Name field */
    private JTextField name;
    /** Visibility field */
    private JComboBox visibility;
    /** In type field */
    private UMLComboBox2 inType;
    /** Return type field */
    private UMLComboBox2 outType;
    /** In parameter */
    private Object inParam = null;
    /** Return parameter */
    private Object returnParam = null;
    
    /** The newly created operation */
    private Object operation = null;
    
    /**
     * Constructor.
     */
    public Operation() {
        super();
        operation = Model.getCoreFactory().createOperation();
        
        String[] vis = new String[] { "public(+)", "package(~)",
                "protected(#)", "private(-)" };
        visibility = new JComboBox(vis);
        visibility.setPreferredSize(
                new Dimension((int) (1.5 * ClassCreateWizard.WIDTH / 10), 25));
        
        UMLComboBoxModel2 model = new UMLStructuralFeatureTypeComboBoxModel();
        inParam = Model.getCoreFactory().createParameter();
        model.setTarget(inParam);
        inType = new UMLComboBox2(model,
                ActionSetStructuralFeatureType.getInstance());
        inType.setSelectedIndex(0);
        inType.setPreferredSize(
                new Dimension((int) (2.7 * ClassCreateWizard.WIDTH / 10), 25));
        
        name = new JTextField();
        name.setPreferredSize(
                new Dimension(2 * ClassCreateWizard.WIDTH / 10, 25));
        
        model = new UMLStructuralFeatureTypeComboBoxModel();
        returnParam = Model.getCoreFactory().createParameter();
        model.setTarget(returnParam);
        outType = new UMLComboBox2(model,
                ActionSetStructuralFeatureType.getInstance());
        outType.setSelectedIndex(0);
        outType.setPreferredSize(
                new Dimension((int) (2.7 * ClassCreateWizard.WIDTH / 10), 25));

        this.add(visibility);
        this.add(inType);
        this.add(name);
        this.add(outType);
    }
    
    /**
     * @return The newly created operation, or null if a name was not specified.
     */
    public Object getOperation() {
        
        if (name.getText().length() > 0) {
            CoreHelper help = Model.getCoreHelper();
            VisibilityKind vk = Model.getVisibilityKind();

            help.setName(operation, name.getText());
            
            Object vis = null;
            int index = visibility.getSelectedIndex();
            switch (index) {
            case 1:
                vis = vk.getPublic();
                break;
            case 2:
                vis = vk.getPackage();
                break;
            case 3:
                vis = vk.getProtected();
                break;
            default:
                vis = vk.getPrivate();
            }
            help.setVisibility(operation, vis);
            
            // the return param
            help.setType(returnParam, outType.getSelectedObjects()[0]);
            help.setKind(returnParam, 
                    Model.getDirectionKind().getReturnParameter());
            help.addParameter(operation, returnParam);
            
            // the in param
            help.setType(inParam, inType.getSelectedObjects()[0]);
            help.setKind(inParam, Model.getDirectionKind().getInParameter());
            help.addParameter(operation, inParam);       
        } else {
            operation = null;
        }
        return operation;
    }
}
