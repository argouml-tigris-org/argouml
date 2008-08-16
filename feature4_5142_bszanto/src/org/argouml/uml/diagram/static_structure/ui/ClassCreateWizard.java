// $Id$
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
import org.argouml.util.ArgoDialog;

/**
 * Wizard for creating a new Class.
 * 
 * @author bszanto
 */
public class ClassCreateWizard implements CreateWizard {

    /** WIDTH of the dialog */
    public static final int WIDTH = 600;
    /** HEIGHT of the dialog */
    public static final int HEIGHT = 450;

    /** The wizard dialog */
    private ArgoDialog dialog = null;

    private JTextField txtName = null;

    /** The new class */
    private Object classNode = null;

    /** Attributes */
    private Vector<Attribute> attributes;

    /** Operations */
    private Vector<Operation> operations;

    /**
     * Constructor
     */
    public ClassCreateWizard() {
    }
    
    public void init() {
        classNode = null;
        attributes = new Vector<Attribute>();
        operations = new Vector<Operation>();
        

        dialog = new ArgoDialog(
                Translator.localize("dialog.title.wizard-class"), true);
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
                .localize("label.operations") 
                + "  visibility [returnType] opName [inType paramName]"));
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
    }

    public Object display() {
        dialog.setVisible(true);
        // the next line is executed only when the dialog is not visible anymore
        return classNode;
    }
    
    /**
     * When the OK button is pressed the new class is created, and the 
     * attributes and operations are added.
     */
    public void okPressed() {
        classNode = Model.getCoreFactory().createClass();
        
        // setting name
        Model.getCoreHelper().setName(classNode, txtName.getText());
        
        // adding attributes
        Vector<Object> attList = new Vector<Object>(); 
        for (Attribute temp : attributes) {
            if (temp.getAttribute() != null) {
                attList.add(temp.getAttribute());
            }
        }
        Model.getCoreHelper().setAttributes(classNode, attList);
        
        // adding operations
        Vector<Object> opList = new Vector<Object>();
        for (Operation temp : operations) {
            if (temp.getOperation() != null) {
                opList.add(temp.getOperation());
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
    
    /** The newly created attribute */
    private Object attribute = null;

    /**
     * Constructor.
     */
    public Attribute() {
        super();
        attribute = Model.getCoreFactory().createAttribute();         
        
        String[] vis = new String[] {"public(+)", "package(~)", "protected(#)", 
                                     "private(-)"};
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
            CoreHelper helper = Model.getCoreHelper();
            
            
            helper.setName(attribute, name.getText());
            
            Object vis = Attribute.getVisibilityKind(
                    visibility.getSelectedIndex());
            helper.setVisibility(attribute, vis);
            
            helper.setType(attribute, type.getSelectedItem());
            
            helper.setReadOnly(attribute, false);
        } else {
            attribute = null;
        }
        return attribute;
    }
    
    /**
     * Determines the VisibilityKind corresponding to the index parameter.
     * If the index is out of the valid range, a default VisibilityKind is 
     * used.
     * 
     * @param index the selected index, with 0 <= index <= 3.
     * @return the VisibilityKind corresponding to the index. The default 
     * VisibilityKind is Private.
     */
    public static Object getVisibilityKind(int index) {
        VisibilityKind vk = Model.getVisibilityKind();
        Object vis = vk.getPrivate();
        switch (index) {
        case 0:
            vis = vk.getPublic();
            break;
        case 1:
            vis = vk.getPackage();
            break;
        case 2:
            vis = vk.getProtected();
            break;
        case 3:
            vis = vk.getPrivate();
        default:
            assert false : "Invalid value of index: " + index + ".";
        }
        return vis;
    }
}

class Operation extends JPanel {
    /** Visibility field */
    private JComboBox visibility;
    /** Return type field */
    private UMLComboBox2 outType;
    /** Name field */
    private JTextField name;
    /** In type field */
    private UMLComboBox2 inType;
    /** Name field */
    private JTextField paramName;
    
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
        
        String[] vis = new String[] {"public(+)", "package(~)", "protected(#)",
                                     "private(-)"};
        visibility = new JComboBox(vis);
        visibility.setPreferredSize(
                new Dimension((int) (1.3 * ClassCreateWizard.WIDTH / 10), 25));

        UMLComboBoxModel2 model = 
            new UMLStructuralFeatureTypeComboBoxModel(true);
        returnParam = Model.getCoreFactory().createParameter();
        model.setTarget(returnParam);
        outType = new UMLComboBox2(model,
                ActionSetStructuralFeatureType.getInstance());
        outType.setSelectedIndex(0);
        outType.setPreferredSize(
                new Dimension((2 * ClassCreateWizard.WIDTH / 10), 25));      

        name = new JTextField();
        name.setPreferredSize(
                new Dimension((int) (1.7 * ClassCreateWizard.WIDTH / 10), 25));
        
        model = new UMLStructuralFeatureTypeComboBoxModel(true);
        inParam = Model.getCoreFactory().createParameter();
        model.setTarget(inParam);
        inType = new UMLComboBox2(model,
                ActionSetStructuralFeatureType.getInstance());
        inType.setSelectedIndex(0);
        inType.setPreferredSize(
                new Dimension((2 * ClassCreateWizard.WIDTH / 10), 25));
        
        paramName = new JTextField();
        paramName.setPreferredSize(
                new Dimension((int) (1.7 * ClassCreateWizard.WIDTH / 10), 25));

        this.add(visibility);
        this.add(outType);
        this.add(name);
        this.add(new JLabel("("));
        this.add(inType);
        this.add(paramName);
        this.add(new JLabel(")"));
    }
    
    /**
     * @return The newly created operation, or null if a name was not specified.
     */
    public Object getOperation() {
        
        if (name.getText().length() > 0) {
            CoreHelper helper = Model.getCoreHelper();

            helper.setName(operation, name.getText());
            
            Object vis = Attribute.getVisibilityKind(
                    visibility.getSelectedIndex());
            helper.setVisibility(operation, vis);
            
            // the return param
            if (outType.getSelectedIndex() != -1 
                    && !outType.getSelectedItem().equals(" ")) {
                helper.setType(returnParam, outType.getSelectedObjects()[0]);
                helper.setKind(returnParam, 
                        Model.getDirectionKind().getReturnParameter());
                helper.addParameter(operation, returnParam);
            }
            
            // the in param
            if (inType.getSelectedIndex() != -1 
                    && !inType.getSelectedItem().equals(" ")
                    && paramName.getText().length() > 0) {
                helper.setType(inParam, inType.getSelectedObjects()[0]);
                helper.setKind(inParam, 
                        Model.getDirectionKind().getInParameter());
                helper.setName(inParam, paramName.getText());
                helper.addParameter(operation, inParam); 
            }
        } else {
            operation = null;
        }
        return operation;
    }
}

