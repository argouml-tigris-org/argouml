// Copyright (c) 1996-2005 The Regents of the University of California. All
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

package org.argouml.uml.reveng.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.AbstractTableModel;

import org.apache.log4j.Logger;
import org.argouml.i18n.Translator;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import org.argouml.ui.ArgoDialog;
import org.argouml.ui.ProjectBrowser;
import org.argouml.ui.explorer.ExplorerEventAdaptor;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.diagram.DiagramFactory;
import org.argouml.uml.diagram.sequence.ui.FigClassifierRole;
import org.argouml.uml.diagram.sequence.ui.UMLSequenceDiagram;
import org.argouml.uml.diagram.ui.UMLDiagram;
import org.argouml.uml.reveng.java.JavaLexer;
import org.argouml.uml.reveng.java.JavaRecognizer;
import org.argouml.uml.reveng.java.Modeller;
import org.argouml.uml.ui.ActionBaseDelete;
import org.tigris.gef.base.Diagram;
import org.tigris.gef.presentation.Fig;

/**
 * The dialog that starts the reverse engineering of operations.
 */
public class RESequenceDiagramDialog extends ArgoDialog implements ActionListener {

    /**
     * Constructor.
     *
     * @param operation The operation that should be reverse engineered.
     */
    public RESequenceDiagramDialog(Object operation) {
        super(
            ProjectBrowser.getInstance(),
            Translator.localize("dialog.title.generate-classes"),
            ArgoDialog.OK_CANCEL_OPTION,
            true);
        
        _operation = operation;
        _model = ProjectManager.getManager().getCurrentProject().getModel();
        try {
            _modeller = new Modeller(_model, null, null, true, true, null);
            parseBody();
        } catch (Exception ex) {}

        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Manually", getManuallyTab());
        tabs.addTab("Automatic", getAutomaticallyTab());
        contentPanel.add(tabs, BorderLayout.CENTER);
        setContent(contentPanel);

        Object classifier = Model.getFacade().getOwner(_operation);
        Object owner = Model.getFacade().getNamespace(classifier);
        _collaboration = Model.getCollaborationsFactory().buildCollaboration(owner, classifier);
        _diagram = 
            (UMLDiagram)DiagramFactory.getInstance().createDiagram(
                UMLSequenceDiagram.class, 
                _collaboration,
                null);
        ProjectManager.getManager().getCurrentProject().addMember(_diagram);
        TargetManager.getInstance().setTarget(_diagram);
        Object node = Model.getCollaborationsFactory().buildClassifierRole(_collaboration);
        _diagram.add(new FigClassifierRole(node));
        ExplorerEventAdaptor.getInstance().modelElementChanged(owner);
    }

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
        super.actionPerformed(e);

        // Generate Button --------------------------------------
        if (e.getSource() == getCancelButton()) {
            // remove SD and clean up everything
            Project p = ProjectManager.getManager().getCurrentProject();
            Object newTarget = null;
            if (ActionBaseDelete.sureRemove(_diagram)) {
                // remove from the model
                newTarget = getNewTarget(_diagram);
                p.moveToTrash(_diagram);
                p.moveToTrash(_collaboration);
            }
            if (newTarget != null) {
                TargetManager.getInstance().setTarget(newTarget);
            }
        }
    }

    /**
     * Gets the object that should be target after the given target is
     * deleted from the model.
     *
     * @param target the target to delete
     * @return The object.
     */
    private Object getNewTarget(Object target) {
        Project p = ProjectManager.getManager().getCurrentProject();
        Object newTarget = null;
        if (target instanceof Fig) {
            target = ((Fig) target).getOwner();
        }
        if (Model.getFacade().isABase(target)) {
            newTarget = Model.getFacade().getModelElementContainer(target);
        } else if (target instanceof Diagram) {
            Diagram firstDiagram = (Diagram) p.getDiagrams().get(0);
            if (target != firstDiagram) {
                newTarget = firstDiagram;
            } else {
                if (p.getDiagrams().size() > 1) {
                    newTarget = p.getDiagrams().get(1);
                } else {
                    newTarget = p.getRoot();
                }
            }
        } else {
            newTarget = p.getRoot();
        }
        return newTarget;
    }

    /**
     * Gets the panel of the automatically tab.
     * @return the constructed panel
     */
    private JPanel getAutomaticallyTab() {
        JPanel top = new JPanel();
        top.setLayout(new GridBagLayout());

        GridBagConstraints labelConstraints = new GridBagConstraints();
        labelConstraints.anchor = GridBagConstraints.WEST;
        labelConstraints.gridy = 0;
        labelConstraints.gridx = 0;
        labelConstraints.insets = new Insets(10, 4, 2, 4);

        GridBagConstraints fieldConstraints = new GridBagConstraints();
        fieldConstraints.anchor = GridBagConstraints.WEST;
        fieldConstraints.fill = GridBagConstraints.NONE;
        fieldConstraints.gridy = 0;
        fieldConstraints.gridx = 1;
        fieldConstraints.weightx = 1.0;
        fieldConstraints.insets = new Insets(10, 4, 2, 4);

        JPanel depthPanel = new JPanel(new FlowLayout());
        JRadioButton unlimited = new JRadioButton("unlimited");
        JRadioButton limited = new JRadioButton("limit to", true);
        ButtonGroup group = new ButtonGroup();
        group.add(unlimited);
        group.add(limited);
        depthPanel.add(limited);
        depthPanel.add(new JSpinner(new SpinnerNumberModel(1, 0, 999, 1)));
        depthPanel.add(unlimited);

        labelConstraints.gridy = 0;
        fieldConstraints.gridy = 0;
        top.add(new JLabel("Depth:"), labelConstraints);
        top.add(depthPanel, fieldConstraints);

        labelConstraints.gridy = 1;
        fieldConstraints.gridy = 1;
        top.add(new JLabel("Assumption table:"), labelConstraints);
        top.add(new JButton("Update"), fieldConstraints);

        ArrayList assumptions = new ArrayList();
        assumptions.add("calls.hasMoreElements()");
        assumptions.add("methods != null && !methods.isEmpty()");
        JTable table = new JTable(new SelectionTableModel(assumptions.toArray(), "Conditions", "Assume true"));
        table.setShowVerticalLines(true);

        fieldConstraints.gridx = 0;
        fieldConstraints.gridy = 2;
        fieldConstraints.gridwidth = 2;
        fieldConstraints.anchor = GridBagConstraints.CENTER;
        fieldConstraints.fill = GridBagConstraints.BOTH;
        fieldConstraints.weighty = 1.0;
        top.add(new JScrollPane(table), fieldConstraints);

        return top;
    }

    /**
     * Gets the panel of the manually tab.
     * @return the constructed panel
     */
    private JPanel getManuallyTab() {
        JPanel top = new JPanel();
        top.setLayout(new GridBagLayout());

        GridBagConstraints labelConstraints = new GridBagConstraints();
        labelConstraints.anchor = GridBagConstraints.WEST;
        labelConstraints.gridy = 0;
        labelConstraints.gridx = 0;
        labelConstraints.insets = new Insets(10, 4, 2, 4);

        GridBagConstraints fieldConstraints = new GridBagConstraints();
        fieldConstraints.anchor = GridBagConstraints.WEST;
        fieldConstraints.fill = GridBagConstraints.NONE;
        fieldConstraints.gridy = 0;
        fieldConstraints.gridx = 1;
        fieldConstraints.weightx = 1.0;
        fieldConstraints.insets = new Insets(10, 4, 2, 4);

        top.add(new JLabel("Method call table:"), labelConstraints);
        top.add(new JButton("Process"), fieldConstraints);

        JTable table = new JTable(new SelectionTableModel(_calls.toArray(), "Method calls", "Enable"));
        table.setShowVerticalLines(true);

        fieldConstraints.gridx = 0;
        fieldConstraints.gridy = 1;
        fieldConstraints.gridwidth = 2;
        fieldConstraints.anchor = GridBagConstraints.CENTER;
        fieldConstraints.fill = GridBagConstraints.BOTH;
        fieldConstraints.weighty = 1.0;
        top.add(new JScrollPane(table), fieldConstraints);

        return top;
    }

    /**
     * Gets the (first) body of an operation.
     */
    private static String getBody(Object operation) {
        String body = null;
        Collection methods = Model.getFacade().getMethods(operation);
        if (methods != null && !methods.isEmpty()) {
            Object expression = Model.getFacade().getBody(methods.iterator().next());
            body = (String)Model.getFacade().getBody(expression);
        }
        if (body == null) {
            body = "";
        }
        return body;
    }

    /**
     * parses a body of an operation.
     */
    private void parseBody() {
        JavaLexer lexer = null;
        JavaRecognizer parser = null;
        _calls.clear();
        _types.clear();
        _modeller.clearMethodCalls();
        _modeller.clearLocalVariableDeclarations();
        try {
            lexer = new JavaLexer(new StringReader('{' + getBody(_operation) + '}'));
            lexer.setTokenObjectClass("org.argouml.uml.reveng.java.ArgoToken");
            parser = new JavaRecognizer(lexer);
            parser.setModeller(_modeller);
            parser.setParserMode(JavaRecognizer.MODE_REVENG_SEQUENCE);
        } catch (Exception ex) {}
        if (_modeller != null && parser != null) {
            try {
                parser.compoundStatement();
            } catch (Exception ex) {
                LOG.debug("Parsing method body failed:", ex);
            }
            Collection methodCalls = _modeller.getMethodCalls();
            if (methodCalls != null) {
                _calls.addAll(methodCalls);
                Iterator iter = methodCalls.iterator();
                while (iter.hasNext()) {
                    determineCallee((String)iter.next());
                }
            }
        }
    }
    
    private void determineCallee(String call) {
        StringBuffer sb = new StringBuffer(call);
    }

    /**
     * Logger.
     */
    private static final Logger LOG =
        Logger.getLogger(RESequenceDiagramDialog.class);

    private static Object _model = null;
    private static Modeller _modeller = null;

    private Object _operation = null;
    private ArrayList _calls = new ArrayList();
    private ArrayList _conditions = new ArrayList();
    private Hashtable _types = new Hashtable();
    private Object _collaboration = null;
    private UMLDiagram _diagram = null;
    
    /**
     * Table model for the assumption table.
     */
    class SelectionTableModel extends AbstractTableModel {
        /**
         * Constructor.
         */
        public SelectionTableModel(Object[] arr, String colName1, String colName2) {
            elements = new Object[arr.length][2];
            for (int i = 0; i < elements.length; i++) {
            elements[i][0] = arr[i];
            elements[i][1] = new Boolean(true);
            }
            columnName1 = colName1;
            columnName2 = colName2;
        }
    
        /**
         * @see javax.swing.table.TableModel#getColumnCount()
         */
        public int getColumnCount() {
            return 2;
        }
    
        /**
         * @see javax.swing.table.TableModel#getColumnName(int)
         */
        public String getColumnName(int col) {
            if (col == 0) {
                return columnName1;
            } else if (col == 1) {
                return columnName2;
            }
            return null;
        }
    
        /**
         * @see javax.swing.table.TableModel#getRowCount()
         */
        public int getRowCount() {
            return elements.length;
        }
    
        /**
         * @see javax.swing.table.TableModel#getValueAt(int, int)
         */
        public Object getValueAt(int row, int col) {
            if (row < elements.length) {
                return elements[row][col];
            } else {
                throw new IllegalArgumentException("Too many columns");
            }
        }
    
        /**
         * @see javax.swing.table.TableModel#setValueAt(
         *         java.lang.Object, int, int)
         */
        public void setValueAt(Object ob, int row, int col) {
            elements[row][col] = ob;
        }
    
        /**
         * @see javax.swing.table.TableModel#getColumnClass(int)
         */
        public Class getColumnClass(int col) {
            if (col == 0) {
                return String.class;
            } else if (col == 1) {
                return Boolean.class;
            }
            return null;
        }
    
        /**
         * @see javax.swing.table.TableModel#isCellEditable(int, int)
         */
        public boolean isCellEditable(int row, int col) {
            return col >= 1 && row < elements.length;
        }
        
        private Object[][] elements;
        private String columnName1, columnName2;
    } /* end class SelectionTableModel */

} /* end class RESequenceDiagramDialog */
