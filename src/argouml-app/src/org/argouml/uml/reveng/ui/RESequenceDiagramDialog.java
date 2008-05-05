// $Id$
// Copyright (c) 1996-2007 The Regents of the University of California. All
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
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerNumberModel;

import org.apache.log4j.Logger;
import org.argouml.i18n.Translator;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import org.argouml.ui.CheckboxTableModel;
import org.argouml.ui.explorer.ExplorerEventAdaptor;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.diagram.ArgoDiagram;
import org.argouml.uml.diagram.DiagramFactory;
import org.argouml.uml.diagram.DiagramFactory.DiagramType;
import org.argouml.uml.diagram.sequence.MessageNode;
import org.argouml.uml.diagram.sequence.SequenceDiagramGraphModel;
import org.argouml.uml.diagram.sequence.ui.FigClassifierRole;
import org.argouml.uml.diagram.sequence.ui.FigMessage;
import org.argouml.uml.diagram.sequence.ui.SequenceDiagramLayer;
import org.argouml.uml.reveng.ImportSettings;
import org.argouml.uml.reveng.java.JavaLexer;
import org.argouml.uml.reveng.java.JavaRecognizer;
import org.argouml.uml.reveng.java.Modeller;
import org.argouml.uml.ui.ActionDeleteModelElements;
import org.argouml.util.ArgoDialog;
import org.tigris.gef.base.Diagram;
import org.tigris.gef.base.Editor;
import org.tigris.gef.base.Globals;
import org.tigris.gef.base.Mode;
import org.tigris.gef.presentation.Fig;

/**
 * The dialog that starts the reverse engineering of operations.<p>
 *
 * TODO: subsequent parsing of further operation bodies <p>
 * TODO: suppressing multiple creation of already created messages<p>
 * TODO: processing of non-constructor-calls to other classifiers<p>
 * TODO: i18n<p>
 */
public class RESequenceDiagramDialog
    extends ArgoDialog
    implements ActionListener, ItemListener {
    /**
     * Logger.
     */
    private static final Logger LOG =
        Logger.getLogger(RESequenceDiagramDialog.class);

    private static final int X_OFFSET = 10;

    private Object model;
    private Modeller modeller;
    private Object classifier;
    private Object operation;
    private FigClassifierRole classifierRole;
    private List calls = new ArrayList();
    private List calldata = new ArrayList();
    private Hashtable types = new Hashtable();
    private Object collaboration;
    private ArgoDiagram diagram;
    private SequenceDiagramGraphModel graphModel;
    private CheckboxTableModel callTable;
    private JComboBox modeChoice;
    private JPanel changingPanel;
    private JPanel manuPanel;
    private JPanel autoPanel;
    private int maxXPos = -X_OFFSET;
    private int maxPort;
    private int portCnt;
    private int anonCnt;
    private boolean isNewSequenceDiagram;


    /**
     * Constructor.
     *
     * @param oper The operation that should be reverse engineered.
     */
    public RESequenceDiagramDialog(Object oper) {
        this(oper, null);
    }

    /**
     * Constructor.
     *
     * @param oper The operation that should be reverse engineered.
     * @param figMessage the message figure where the result will be drawn to
     */
    public RESequenceDiagramDialog(Object oper, FigMessage figMessage) {
        super(
                "NOT FUNCTIONAL!!! "
                + Translator.localize(
                        "dialog.title.reverse-engineer-sequence-diagram")
                + (oper != null
                        ? (' ' + Model.getFacade().getName(oper) + "()")
                                : ""),
                ArgoDialog.OK_CANCEL_OPTION,
                true);
        setResizable(false);

        operation = oper;
        model = ProjectManager.getManager().getCurrentProject().getModel();
        try {
            modeller = new Modeller(model, new DummySettings(), null);
        } catch (Exception ex) { 
            // TODO: Why are these being ignored? - tfm
            LOG.warn("Exception ignored", ex);
        }

        classifier = Model.getFacade().getOwner(operation);
        if (figMessage != null) {
            isNewSequenceDiagram = false;
            graphModel =
                (SequenceDiagramGraphModel) Globals.curEditor().getGraphModel();
            collaboration = graphModel.getCollaboration();
            // maybe there is an easier way to find
            // the current diagram than this:
            Project p = ProjectManager.getManager().getCurrentProject();
            Iterator<ArgoDiagram> iter = p.getDiagramList().iterator();
            while (iter.hasNext()) {
                diagram = iter.next();
                if (graphModel == diagram.getGraphModel()) {
                    break;
                }
            }
            classifierRole = getClassifierRole(classifier, "obj");
            portCnt =
                SequenceDiagramLayer.getNodeIndex(
                    figMessage.getDestMessageNode().getFigMessagePort().getY());
            Iterator<Fig> it = diagram.getFigIterator();
            while (it.hasNext()) {
                Fig f = it.next();
                if (f instanceof FigClassifierRole) {
                    int x = f.getX();
                    if (maxXPos < x) {
                        maxXPos = x;
                    }
                    if (Model.getFacade().getName(f.getOwner())
                            .startsWith("anon")) {
                        anonCnt++;
                    }
                } else if (f instanceof FigMessage) {
                    int port =
                        SequenceDiagramLayer.getNodeIndex(
                            ((FigMessage) f).getDestMessageNode()
                                .getFigMessagePort().getY());
                    if (maxPort < port) {
                        maxPort = port;
                    }
                }
            }
        } else {
            isNewSequenceDiagram = true;
            buildSequenceDiagram(classifier);
            classifierRole = getClassifierRole(classifier, "obj");
            maxXPos = classifierRole.getX();
        }
        parseBody();

        JPanel contentPanel = getContentPanel();
        setContent(contentPanel);
    }

    /*
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
        super.actionPerformed(e);

        if (e.getSource() == getOkButton()) {
            for (int i = 0; i < callTable.getRowCount(); i++) {
                if (Boolean.TRUE.equals(callTable.getValueAt(i, 1))) {
                    buildAction((String) callTable.getValueAt(i, 0));
                }
            }
        } else if (e.getSource() == getCancelButton()
                && isNewSequenceDiagram) {
            // remove SD and clean up everything
            Project p = ProjectManager.getManager().getCurrentProject();
            Object newTarget = null;
            if (ActionDeleteModelElements.sureRemove(diagram)) {
                // remove from the model
                newTarget = getNewTarget(diagram);
                p.moveToTrash(diagram);
                p.moveToTrash(collaboration);
            }
            if (newTarget != null) {
                TargetManager.getInstance().setTarget(newTarget);
            }
        }
    }

    /*
     * @see java.awt.event.ItemListener#itemStateChanged(java.awt.event.ItemEvent)
     */
    public void itemStateChanged(ItemEvent e) {
        if (modeChoice.getSelectedIndex() != 1) {
            changingPanel.remove(autoPanel);
            changingPanel.add(manuPanel, BorderLayout.CENTER);
            pack();
        } else {
            changingPanel.remove(manuPanel);
            changingPanel.add(autoPanel, BorderLayout.CENTER);
            pack();
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
        if (Model.getFacade().isAModelElement(target)
                && Model.getFacade().getNamespace(target) != null) {
            newTarget = Model.getFacade().getNamespace(target);
        } else if (target instanceof Diagram) {
            Diagram firstDiagram = (Diagram) p.getDiagramList().get(0);
            if (target != firstDiagram) {
                newTarget = firstDiagram;
            } else {
                if (p.getDiagramList().size() > 1) {
                    newTarget = p.getDiagramList().get(1);
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
     * Gets the content panel, containing all the gui.
     * @return the constructed panel
     */
    private JPanel getContentPanel() {
        JPanel content = new JPanel();
        content.setLayout(new GridBagLayout());

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.WEST;
        constraints.fill =
            GridBagConstraints.HORIZONTAL;
        constraints.gridx = 0;
        constraints.gridy = 0;

        constraints.insets = new Insets(2, 2, 2, 2);
        content.add(new JLabel("Mode:"), constraints);

        constraints.gridy = 1;
        modeChoice = new JComboBox();
        modeChoice.addItem("Manually select calls of this operation");
        modeChoice.addItem("Traverse calls automatically with a chosen depth");
        modeChoice.addItemListener(this);
        content.add(modeChoice, constraints);

        manuPanel = getManuallyTab();
        autoPanel = getAutomaticallyTab();

        constraints.gridy = 2;
        changingPanel = new JPanel(new BorderLayout(0, 0));
        changingPanel.add(manuPanel, BorderLayout.CENTER);
        content.add(changingPanel, constraints);

        return content;
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
        labelConstraints.insets = new Insets(10, 2, 2, 2);

        GridBagConstraints fieldConstraints = new GridBagConstraints();
        fieldConstraints.anchor = GridBagConstraints.WEST;
        fieldConstraints.fill = GridBagConstraints.NONE;
        fieldConstraints.gridy = 0;
        fieldConstraints.gridx = 1;
        fieldConstraints.weightx = 1.0;
        fieldConstraints.insets = new Insets(4, 2, 2, 2);

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

        List<String> assumptions = new ArrayList<String>();
        assumptions.add("calls.hasMoreElements()");
        assumptions.add("methods != null && !methods.isEmpty()");
        Object[] data = null;
        JTable table =
            new JTable(new CheckboxTableModel(
                    assumptions.toArray(),
                    data,
                    "Conditions",
                    "Assume true"));
        table.setShowVerticalLines(true);

        fieldConstraints.gridx = 0;
        fieldConstraints.gridy = 2;
        fieldConstraints.gridwidth = 2;
        fieldConstraints.anchor = GridBagConstraints.CENTER;
        fieldConstraints.fill = GridBagConstraints.BOTH;
        fieldConstraints.weighty = 1.0;
        top.add(new JScrollPane(table), fieldConstraints);

        fieldConstraints.insets = new Insets(0, 2, 0, 2);
        JCheckBox checkbox1 = new JCheckBox("also process create calls", true);
        fieldConstraints.gridy = 3;
        top.add(checkbox1, fieldConstraints);
        JCheckBox checkbox2 = new JCheckBox("also process local calls", true);
        fieldConstraints.gridy = 4;
        top.add(checkbox2, fieldConstraints);
        JCheckBox checkbox3 =
            new JCheckBox("also process calls inside package", true);
        fieldConstraints.gridy = 5;
        top.add(checkbox3, fieldConstraints);

        return top;
    }

    /**
     * Gets the panel of the manually tab.
     *
     * @return the constructed panel
     */
    private JPanel getManuallyTab() {
        JPanel top = new JPanel();
        top.setLayout(new GridBagLayout());

        GridBagConstraints labelConstraints = new GridBagConstraints();
        labelConstraints.anchor = GridBagConstraints.WEST;
        labelConstraints.gridx = 0;
        labelConstraints.gridy = 0;
        labelConstraints.insets = new Insets(10, 2, 2, 2);

        GridBagConstraints fieldConstraints = new GridBagConstraints();
        fieldConstraints.anchor = GridBagConstraints.WEST;
        fieldConstraints.fill = GridBagConstraints.NONE;
        fieldConstraints.gridx = 1;
        fieldConstraints.gridy = 0;
        fieldConstraints.weightx = 1.0;
        fieldConstraints.insets = new Insets(4, 2, 2, 2);

        top.add(new JLabel("Method call table:"), labelConstraints);

        callTable =
            new CheckboxTableModel(
                    calls.toArray(),
                    calldata.toArray(),
                    "Method calls",
                    "Enable");
        JTable table = new JTable(callTable);
        table.setShowVerticalLines(true);

        fieldConstraints.gridx = 0;
        fieldConstraints.gridy = 1;
        fieldConstraints.gridwidth = 2;
        fieldConstraints.anchor = GridBagConstraints.CENTER;
        fieldConstraints.fill = GridBagConstraints.BOTH;
        fieldConstraints.weighty = 1.0;
        top.add(new JScrollPane(table), fieldConstraints);

        fieldConstraints.insets = new Insets(0, 2, 0, 2);
        JCheckBox checkbox1 = new JCheckBox("(un)check create calls", true);
        fieldConstraints.gridy = 2;
        top.add(checkbox1, fieldConstraints);
        JCheckBox checkbox2 = new JCheckBox("(un)check local calls", true);
        fieldConstraints.gridy = 3;
        top.add(checkbox2, fieldConstraints);
        JCheckBox checkbox3 = new JCheckBox("(un)check package calls", true);
        fieldConstraints.gridy = 4;
        top.add(checkbox3, fieldConstraints);
        JCheckBox checkbox4 = new JCheckBox("(un)check far calls", true);
        fieldConstraints.gridy = 5;
        top.add(checkbox4, fieldConstraints);

        return top;
    }

    /**
     * Builds the sequence diagram for a classifier.
     */
    private void buildSequenceDiagram(Object theClassifier) {
        Project p = ProjectManager.getManager().getCurrentProject();

        collaboration =
            Model.getCollaborationsFactory().buildCollaboration(
                Model.getFacade().getNamespace(theClassifier),
                theClassifier);
        diagram =
            DiagramFactory.getInstance().createDiagram(
                DiagramType.Sequence,
                collaboration,
                null);
        graphModel = (SequenceDiagramGraphModel) diagram.getGraphModel();
        p.addMember(diagram);
        TargetManager.getInstance().setTarget(diagram);
    }

    /**
     * Gets or builds the figure of the classifier role for a given classifier
     * and object name.
     */
    private FigClassifierRole getClassifierRole(
            Object theClassifier, String objName) {
        FigClassifierRole crFig = null;
        // first check if the fig of the classifier role already exists
        Collection coll = diagram.getLayer().getContents();
        Iterator iter = coll != null ? coll.iterator() : null;
        while (iter != null && iter.hasNext()) {
            Object fig = iter.next();
            if (fig instanceof FigClassifierRole) {
                Object elem = ((FigClassifierRole) fig).getOwner();
                Collection bases = Model.getFacade().getBases(elem);
                if (Model.getFacade().isAClassifierRole(elem)
                     && Model.getFacade().getName(elem).equals(objName)
                     && bases != null && bases.contains(theClassifier)) {
                    // yes found, so this will be returned
                    crFig = (FigClassifierRole) fig;
                    break;
                }
            }
        }
        if (crFig == null) {
            // classifier role does not exists, so create a new one
            Object node =
                Model.getCollaborationsFactory()
                    .buildClassifierRole(collaboration);
            if (objName != null) {
                Model.getCoreHelper().setName(node, objName);
            } else {
                Model.getCoreHelper().setName(node, "anon" + (++anonCnt));
            }
            coll = new ArrayList();
            coll.add(theClassifier);
            Model.getCollaborationsHelper().setBases(node, coll);
            crFig = new FigClassifierRole(node);

            // location must be set for correct automatic layouting (how funny)
            // otherwise, the new classifier role is not the rightmost
            maxXPos += X_OFFSET;
            crFig.setLocation(maxXPos, 0);

            diagram.add(crFig);
            graphModel.addNode(node);
            // TODO: Send event instead of calling event adapter directly
            ExplorerEventAdaptor.getInstance().modelElementChanged(
                Model.getFacade().getNamespace(classifier));
        }
        return crFig;
    }

    /**
     * Parses a body of the actual operation.
     */
    private void parseBody() {
        JavaLexer lexer = null;
        JavaRecognizer parser = null;
        calls.clear();
        types.clear();
        modeller.clearMethodCalls();
        modeller.clearLocalVariableDeclarations();
        try {
            lexer =
                new JavaLexer(
                        new StringReader('{' + getBody(operation) + '}'));
            lexer.setTokenObjectClass("org.argouml.uml.reveng.java.ArgoToken");
            parser = new JavaRecognizer(lexer);
            parser.setModeller(modeller);
            parser.setParserMode(JavaRecognizer.MODE_REVENG_SEQUENCE);
        } catch (Exception ex) { 
            // TODO: Why are these being ignored?
            LOG.warn("Exception ignored", ex);
        }
        if (modeller != null && parser != null) {
            try {
                parser.compoundStatement();
            } catch (Exception ex) {
                LOG.debug("Parsing method body failed:", ex);
            }
            Collection methodCalls = modeller.getMethodCalls();
            if (methodCalls != null) {
                calls.addAll(methodCalls);
                if (modeller.getLocalVariableDeclarations() != null) {
                    types.putAll(modeller.getLocalVariableDeclarations());
                }
            }
        }
    }

    /**
     * Gets the (first) body of an operation.
     */
    private static String getBody(Object operation) {
        String body = null;
        Collection methods = Model.getFacade().getMethods(operation);
        if (methods != null && !methods.isEmpty()) {
            Object expression =
                Model.getFacade().getBody(methods.iterator().next());
            body = (String) Model.getFacade().getBody(expression);
        }
        if (body == null) {
            body = "";
        }
        return body;
    }

    /**
     * Builds the complete action and its target
     * classifier role (if not existing).
     */
    private Object buildAction(String call) {
        Object action = null;
        StringBuffer sb = new StringBuffer(call);
        int findpos = sb.lastIndexOf(".");
        int createPos = sb.indexOf("new ");
        boolean isCreate =
            createPos != -1
            && (createPos == 0 || sb.charAt(createPos - 1) == '=');
        if (!isCreate && findpos == -1) {
            // call of a method of the class
            action =
                buildEdge(call, classifierRole, classifierRole,
                        Model.getMetaTypes().getCallAction());
        } else if (!isCreate
                && findpos <= 5
                && (call.startsWith("super.") || call.startsWith("this."))) {
            // also call of a method of the class,
            // but prefixed with "super." or "this."
            action =
                buildEdge(call, classifierRole, classifierRole,
                        Model.getMetaTypes().getCallAction());
        } else {
            String type = null;
            if (isCreate) {
                // creator (constructor) call
                type = sb.substring(createPos + 4);
                String objName =
                    createPos >= 2 ? sb.substring(0, createPos - 1) : null;
                Object cls = getClassifierFromModel(type, objName);
                FigClassifierRole endFig = getClassifierRole(cls, objName);
                action =
                    buildEdge(
                            sb.substring(createPos),
                            classifierRole,
                            endFig,
                            Model.getMetaTypes().getCreateAction());
            } else {
                String teststring = call.substring(0, findpos);
                type = (String) types.get(teststring);
                if (type != null) {
                    Object cls = getClassifierFromModel(type, teststring);
                    FigClassifierRole endFig =
                        getClassifierRole(cls, teststring);
                    action =
                        buildEdge(call, classifierRole, endFig,
                                Model.getMetaTypes().getCallAction());
                }
            }

            // if (type != null) {
                // call of a method of a local object
                // or call of a static method of a classifier
            // } else {
                // unknown type
            // }
        }
        return action;
    }

    /**
     * Builds the edge figure for an action.
     */
    private FigMessage buildEdge(String call,
            FigClassifierRole startFig,
            FigClassifierRole endFig,
            Object callType) {
        FigMessage figEdge = null;
        SequenceDiagramLayer lay = (SequenceDiagramLayer) diagram.getLayer();
        int n = startFig == endFig ? 2 : 1;
        if (portCnt < maxPort) {
            lay.expandDiagram(portCnt + 1, n);
        }
        MessageNode startPort = startFig.getNode(portCnt + 1);
        MessageNode foundPort = endFig.getNode(portCnt + n);
        portCnt += n;
        maxPort += n;
        Fig startPortFig = startFig.getPortFig(startPort);
        Fig destPortFig = endFig.getPortFig(foundPort);
        Object edgeType = Model.getMetaTypes().getMessage();
        Editor ce = Globals.curEditor();
        Hashtable args = new Hashtable();
        args.put("action", callType);
        Mode mode = ce.getModeManager().top();
        mode.setArgs(args);
        Object newEdge = graphModel.connect(startPort, foundPort, edgeType);
        if (null != newEdge) {
            Model.getCoreHelper().setName(newEdge, call);
            figEdge = (FigMessage) lay.presentationFor(newEdge);
            figEdge.setSourcePortFig(startPortFig);
            figEdge.setSourceFigNode(startFig);
            figEdge.setDestPortFig(destPortFig);
            figEdge.setDestFigNode(endFig);
            endFig.updateEdges();
            if (startFig != endFig) {
                startFig.updateEdges();
            }
        }
        return figEdge;
    }

    /**
     * Gets or builds a classifier role from a type. The type is a classifier
     * name, either fully qualified (with whole package path) or not.
     * Also ensures that there is an association to the actual classifier.
     */
    private Object getClassifierFromModel(String type, String objName) {
        Object theClassifier = null;
        int pos = type.lastIndexOf(".");
        if (pos != -1) {
            // full package path given, so let's get it from the model
            Object namespace = model;
            pos = 0;
            StringTokenizer st = new StringTokenizer(type, ".");
            while (st.hasMoreTokens()) {
                String s = st.nextToken();
                pos += s.length();
                Object element = Model.getFacade().lookupIn(namespace, s);
                if (element == null) {
                    // package/classifier is missing, so create one
                    if (st.hasMoreTokens()) {
                        // must be a package
                        element =
                            Model.getModelManagementFactory()
                                .buildPackage(s);
                    } else {
                        // must be a classifier, let's assume a class
                        element = Model.getCoreFactory().buildClass(s);
                    }
                    Model.getCoreHelper().setNamespace(element, namespace);
                    Model.getCoreHelper().addOwnedElement(namespace, element);
                }
                namespace = element;
                pos++;
            }
            theClassifier = namespace;
        } else {
            // classifier without package information given
            // first, let's look in the namespace of the actual classifier
            Object namespace = Model.getFacade().getNamespace(classifier);
            theClassifier = Model.getFacade().lookupIn(namespace, type);
            if (!Model.getFacade().isAClassifier(theClassifier)) {
                theClassifier = null;
                // let's search for it in the imports (component dependencies)
                Collection sdeps =
                    Model.getFacade().getSupplierDependencies(classifier);
                Iterator iter1 = sdeps != null ? sdeps.iterator() : null;
                while (theClassifier == null
                        && iter1 != null
                        && iter1.hasNext()) {
                    Object dep = iter1.next();
                    if (Model.getFacade().isADependency(dep)) {
                        Collection clients = Model.getFacade().getClients(dep);
                        Iterator iter2 =
                            clients != null ? clients.iterator() : null;
                        while (theClassifier == null
                                && iter2 != null
                                && iter2.hasNext()) {
                            Object comp = iter2.next();
                            if (Model.getFacade().isAComponent(comp)) {
                                theClassifier = permissionLookup(comp, type);
                            }
                        }
                    }
                }
            }
        }
        if (theClassifier == null) {
            // not found any matching classifier, so create one, and put it
            // into the namespace of the actual classifier
            theClassifier = Model.getCoreFactory().buildClass(type);
            Object namespace = Model.getFacade().getNamespace(classifier);
            Model.getCoreHelper().setNamespace(theClassifier, namespace);
            Model.getCoreHelper().addOwnedElement(namespace, theClassifier);
        }
        ensureDirectedAssociation(classifier, theClassifier);
        return theClassifier;
    }

    /**
     * Checks if there is a directed association between two classifiers, and
     * creates one if necessary.
     */
    private void ensureDirectedAssociation(Object fromCls, Object toCls) {
        String fromName = Model.getFacade().getName(fromCls);
        String toName = Model.getFacade().getName(toCls);
        Object assocEnd = null;
        for (Iterator i =
                Model.getFacade().getAssociationEnds(toCls).iterator();
             i.hasNext();) {
            Object ae = i.next();
            Object assoc = Model.getFacade().getAssociation(ae);
            if (Model.getFacade().getConnections(assoc).size() == 2
                    && Model.getFacade().getType(
                            Model.getFacade().getNextEnd(ae)) == fromCls
                    && Model.getFacade().getName(ae) == null
                    && Model.getFacade().isNavigable(ae)) {
                assocEnd = ae;
            }
        }
        if (assocEnd == null) {
            String assocName = fromName + " -> " + toName;
            Modeller.buildDirectedAssociation(assocName, fromCls, toCls);
        }
    }

    /**
     * Get the classifier with the given name from a permission
     * (null if not found).
     */
    private Object permissionLookup(Object comp, String clsName) {
        Object theClassifier = null;
        // TODO: This could use the new CoreHelper.getPackageImports()
        Collection cdeps = Model.getFacade().getClientDependencies(comp);
        Iterator iter1 = cdeps != null ? cdeps.iterator() : null;
        while (theClassifier == null && iter1 != null && iter1.hasNext()) {
            Object perm = iter1.next();
            if (Model.getFacade().isAPermission(perm)) {
                Collection suppliers = Model.getFacade().getSuppliers(perm);
                Iterator iter2 =
                    suppliers != null ? suppliers.iterator() : null;
                while (theClassifier == null
                        && iter2 != null
                        && iter2.hasNext()) {
                    Object elem = iter2.next();
                    // TODO: I'm not sure what this is trying to do, but it
                    // probably isn't what it thinks it is.  The supplier to
                    // an import is going to be a Package, which is not a
                    // Classifier.  Perhaps this intends to process the 
                    // ownedElements of the Package. - tfm - 20070803
                    if (Model.getFacade().isAClassifier(elem)
                         && clsName.equals(Model.getFacade().getName(elem))) {
                        theClassifier = elem;
                    }
                }
            }
        }
        return theClassifier;
    }

    /**
     * Fixed import settings class for our use.
     */
    private class DummySettings implements ImportSettings {

        public int getImportLevel() {
            return ImportSettings.DETAIL_CLASSIFIER;
        }

        public String getInputSourceEncoding() {
            return null;
        }

        public boolean isAttributeSelected() {
            return true;
        }

        public boolean isCreateDiagramsSelected() {
            return false;
        }

        public boolean isDatatypeSelected() {
            return true;
        }

        public boolean isMinimizeFigsSelected() {
            return false;
        }
        
    }
    
    /**
     * The UID.
     */
    private static final long serialVersionUID = -8595714827064181907L;
}
