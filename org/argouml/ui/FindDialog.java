// $Id$
// Copyright (c) 1996-2006 The Regents of the University of California. All
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

package org.argouml.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import org.argouml.application.api.AbstractArgoJPanel;
import org.argouml.i18n.Translator;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import org.argouml.swingext.SpacerPanel;
import org.argouml.uml.PredicateFind;
import org.argouml.uml.cognitive.ChildGenFind;
import org.argouml.util.ArgoDialog;
import org.tigris.gef.util.Predicate;
import org.tigris.gef.util.PredicateStringMatch;
import org.tigris.gef.util.PredicateType;


/**
 * Main dialog for Find function.
 *
 * This is one of the few dialog boxes in Argo that is
 * non-modal (so that the user can keep the results on
 * the screen while they work with them).<p>
 *
 * The search is buggy and needs work.
 */
public class FindDialog extends ArgoDialog
    implements ActionListener, MouseListener {

    private static FindDialog instance;
    private static int nextResultNum = 1;

    private static int numFinds;

    /**
     * Insets in pixels.
     */
    private static final int INSET_PX = 3;

    private JButton     search     =
	new JButton(
            Translator.localize("dialog.find.button.find"));
    private JButton     clearTabs  =
	new JButton(
            Translator.localize("dialog.find.button.clear-tabs"));
    private JTabbedPane tabs = new JTabbedPane();

    private JPanel nameLocTab = new JPanel();

    private JPanel modifiedTab = new JPanel();

    private JPanel tagValsTab = new JPanel();

    private JPanel constraintsTab = new JPanel();

    private JComboBox elementName = new JComboBox();

    private JComboBox diagramName = new JComboBox();

    private JComboBox location = new JComboBox();

    private JComboBox type = new JComboBox();

    private JPanel typeDetails = new JPanel();

    private JTabbedPane results = new JTabbedPane();

    private JPanel help = new JPanel();

    private List<TabResults> resultTabs = new ArrayList<TabResults>();

    /**
     * @return the instance of this dialog
     */
    public static FindDialog getInstance() {
        if (instance == null) {
            instance = new FindDialog();
        }
        return instance;
    }

    /**
     * The constructor.
     *
     */
    public FindDialog() {
        super(Translator.localize("dialog.find.title"),
              ArgoDialog.OK_CANCEL_OPTION, false);

        JPanel mainPanel = new JPanel(new BorderLayout());

        initNameLocTab();
        tabs.addTab(Translator.localize("dialog.find.tab.name-and-location"),
                nameLocTab);

        initModifiedTab();
        tabs.addTab(Translator.localize("dialog.find.tab.last-modified"),
                modifiedTab);
        tabs.setEnabledAt(1, false);

        initTagValsTab();
        tabs.addTab(Translator.localize("dialog.find.tab.tagged-values"),
                tagValsTab);
        tabs.setEnabledAt(2, false);

        initConstraintsTab();
        tabs.addTab(Translator.localize("tab.constraints"),
		     constraintsTab);
        tabs.setEnabledAt(3, false);

        tabs.setMinimumSize(new Dimension(300, 250));

        JPanel north = new JPanel();
        north.setLayout(new BorderLayout());
        north.add(tabs, BorderLayout.CENTER);
        mainPanel.add(north, BorderLayout.NORTH);

        initHelpTab();
        results.addTab(Translator.localize("dialog.find.tab.help"), help);
        mainPanel.add(results, BorderLayout.CENTER);

        search.addActionListener(this);
        results.addMouseListener(this);

        clearTabs.addActionListener(this);
        clearTabs.setEnabled(false);

        setContent(mainPanel);

        getOkButton().setEnabled(false);
    }

    /**
     * Initialise the tab "Name and Location".
     */
    private void initNameLocTab() {
        elementName.setEditable(true);
        // TODO: Don't use hardcoded colors here - tfm
        elementName.getEditor()
	    .getEditorComponent().setBackground(Color.white);
        diagramName.setEditable(true);
        diagramName.getEditor()
	    .getEditorComponent().setBackground(Color.white);

        elementName.addItem("*");
        diagramName.addItem("*");

        // TODO: add recent patterns
        GridBagLayout gb = new GridBagLayout();
        nameLocTab.setLayout(gb);

        JLabel elementNameLabel =
	    new JLabel(
                Translator.localize("dialog.find.label.element-name"));
        JLabel diagramNameLabel =
	    new JLabel(
                Translator.localize("dialog.find.label.in-diagram"));
        JLabel typeLabel =
	    new JLabel(
                Translator.localize("dialog.find.label.element-type"));
        JLabel locLabel =
	    new JLabel(
                Translator.localize("dialog.find.label.find-in"));

        location.addItem(
                Translator.localize("dialog.find.comboboxitem.entire-project"));
        initTypes();

        typeDetails.setMinimumSize(new Dimension(200, 100));
        typeDetails.setPreferredSize(new Dimension(200, 100));
        typeDetails.setSize(new Dimension(200, 100));

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.ipadx = 3; c.ipady = 3;
        c.gridwidth = 1;

        c.gridx = 0;     c.gridy = 0;
        c.weightx = 0.0;
        gb.setConstraints(elementNameLabel, c);
        nameLocTab.add(elementNameLabel);

        c.gridx = 1;     c.gridy = 0;
        c.weightx = 1.0;
        gb.setConstraints(elementName, c);
        nameLocTab.add(elementName);

        c.gridx = 0;     c.gridy = 1;
        c.weightx = 0.0;
        gb.setConstraints(diagramNameLabel, c);
        nameLocTab.add(diagramNameLabel);

        c.gridx = 1;     c.gridy = 1;
        c.weightx = 1.0;
        gb.setConstraints(diagramName, c);
        nameLocTab.add(diagramName);

        // open space at gridy = 2;

        c.gridx = 0;     c.gridy = 3;
        c.weightx = 0.0;
        gb.setConstraints(locLabel, c);
        nameLocTab.add(locLabel);

        c.gridx = 1;     c.gridy = 3;
        c.weightx = 1.0;
        gb.setConstraints(location, c);
        nameLocTab.add(location);

        SpacerPanel spacer = new SpacerPanel();
        c.gridx = 2;     c.gridy = 0;
        c.weightx = 0.0;
        gb.setConstraints(spacer, c);
        nameLocTab.add(spacer);

        c.gridx = 3;     c.gridy = 0;
        c.weightx = 0.0;
        gb.setConstraints(typeLabel, c);
        nameLocTab.add(typeLabel);

        c.gridx = 4;     c.gridy = 0;
        c.weightx = 1.0;
        gb.setConstraints(type, c);
        nameLocTab.add(type);

        c.gridx = 3;     c.gridy = 1;
        c.gridwidth = 2; c.gridheight = 5;
        gb.setConstraints(typeDetails, c);
        nameLocTab.add(typeDetails);

        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new GridLayout(1, 2, 5, 5));
        searchPanel.add(clearTabs);
        searchPanel.add(search);
        searchPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        c.gridx = 0;     c.gridy = 4;
        c.weightx = 0.0; c.weighty = 0.0;
        c.gridwidth = 2; c.gridheight = 1;
        gb.setConstraints(searchPanel, c);
        nameLocTab.add(searchPanel);
    }

    /**
     * Initialise the help tab.
     */
    private void initHelpTab() {
        help.setLayout(new BorderLayout());
        JTextArea helpText = new JTextArea();
        helpText.setText(Translator.localize("dialog.find.helptext"));
        helpText.setEditable(false);
        helpText.setMargin(new Insets(INSET_PX, INSET_PX, INSET_PX, INSET_PX));
        help.add(new JScrollPane(helpText), BorderLayout.CENTER);
    }

    /**
     * Init the tab with the tagged values.
     */
    private void initTagValsTab() {
        //TODO: This tab does not work currently.
    }

    /**
     * Init the Last Modified tab.
     */
    private void initModifiedTab() {
        // TODO: This tab does not work currently.
    }

    /**
     * Init the Constraints tab.
     */
    private void initConstraintsTab() {
        // TODO: This tab does not work currently.
    }


    /**
     * Init the modelelement types that we can look for.
     */
    private void initTypes() {
        type.addItem(PredicateMType.create()); // Any type

        type.addItem(PredicateMType.create(
                Model.getMetaTypes().getUMLClass()));
        type.addItem(PredicateMType.create(
                Model.getMetaTypes().getInterface()));
        type.addItem(PredicateMType.create(
                Model.getMetaTypes().getAction()));
        // Not in UML 2.x (or metatypes)
//        type.addItem(PredicateMType.create(
//                Model.getMetaTypes().getActivityGraph()));
        type.addItem(PredicateMType.create(
                Model.getMetaTypes().getActor()));
        type.addItem(PredicateMType.create(
                Model.getMetaTypes().getAssociation()));
        type.addItem(PredicateMType.create(
                Model.getMetaTypes().getAssociationClass()));
        type.addItem(PredicateMType.create(
                Model.getMetaTypes().getAssociationEndRole()));
        type.addItem(PredicateMType.create(
                Model.getMetaTypes().getAssociationRole()));
        type.addItem(PredicateMType.create(
                Model.getMetaTypes().getArtifact()));
        type.addItem(PredicateMType.create(
                Model.getMetaTypes().getAttribute()));
        type.addItem(PredicateMType.create(
                Model.getMetaTypes().getClassifier()));
        type.addItem(PredicateMType.create(
                Model.getMetaTypes().getClassifierRole()));
        type.addItem(PredicateMType.create(
                Model.getMetaTypes().getCollaboration()));
        type.addItem(PredicateMType.create(
                Model.getMetaTypes().getComment()));
        type.addItem(PredicateMType.create(
                Model.getMetaTypes().getComponent()));
        type.addItem(PredicateMType.create(
                Model.getMetaTypes().getCompositeState()));
        type.addItem(PredicateMType.create(
                Model.getMetaTypes().getConstraint()));
        type.addItem(PredicateMType.create(
                Model.getMetaTypes().getDataType()));
        type.addItem(PredicateMType.create(
                Model.getMetaTypes().getDependency()));
        type.addItem(PredicateMType.create(
                Model.getMetaTypes().getElementImport()));
        type.addItem(PredicateMType.create(
                Model.getMetaTypes().getEnumeration()));
        type.addItem(PredicateMType.create(
                Model.getMetaTypes().getEnumerationLiteral()));
        type.addItem(PredicateMType.create(
                Model.getMetaTypes().getException()));
        type.addItem(PredicateMType.create(
                Model.getMetaTypes().getExtend()));
        type.addItem(PredicateMType.create(
                Model.getMetaTypes().getExtensionPoint()));
        type.addItem(PredicateMType.create(
                Model.getMetaTypes().getGuard()));
        type.addItem(PredicateMType.create(
                Model.getMetaTypes().getGeneralization()));
        type.addItem(PredicateMType.create(
                Model.getMetaTypes().getInclude()));
        type.addItem(PredicateMType.create(
                Model.getMetaTypes().getInstance()));
        type.addItem(PredicateMType.create(
                Model.getMetaTypes().getInteraction()));
        type.addItem(PredicateMType.create(
                Model.getMetaTypes().getInterface()));
        type.addItem(PredicateMType.create(
                Model.getMetaTypes().getLink()));
        type.addItem(PredicateMType.create(
                Model.getMetaTypes().getMessage()));
        type.addItem(PredicateMType.create(
                Model.getMetaTypes().getModel()));
        type.addItem(PredicateMType.create(
                Model.getMetaTypes().getNode()));
        type.addItem(PredicateMType.create(
                Model.getMetaTypes().getPackage()));
        type.addItem(PredicateMType.create(
                Model.getMetaTypes().getParameter()));
        type.addItem(PredicateMType.create(
                Model.getMetaTypes().getPartition()));
        type.addItem(PredicateMType.create(
                Model.getMetaTypes().getPseudostate()));
        type.addItem(PredicateMType.create(
                Model.getMetaTypes().getOperation()));
        type.addItem(PredicateMType.create(
                Model.getMetaTypes().getSimpleState()));
        type.addItem(PredicateMType.create(
                Model.getMetaTypes().getSignal()));
        type.addItem(PredicateMType.create(
                Model.getMetaTypes().getState()));
        type.addItem(PredicateMType.create(
                Model.getMetaTypes().getStateMachine()));
        type.addItem(PredicateMType.create(
                Model.getMetaTypes().getStateVertex()));
        type.addItem(PredicateMType.create(
                Model.getMetaTypes().getStereotype()));
        type.addItem(PredicateMType.create(
                Model.getMetaTypes().getTagDefinition()));
        
        // TODO: Doesn't work (perhaps because composite?), so disable for
        // now so user isn't misled - tfm - 20070904
//        type.addItem(PredicateMType.create(
//                Model.getMetaTypes().getTaggedValue()));
        
        // Not in UML 2.x (or Metatypes)
//        type.addItem(PredicateMType.create(
//                Model.getMetaTypes().getTemplateArgument()));
//        type.addItem(PredicateMType.create(
//                Model.getMetaTypes().getTemplateParameter()));
        
        type.addItem(PredicateMType.create(
                Model.getMetaTypes().getTransition()));
        type.addItem(PredicateMType.create(
                Model.getMetaTypes().getUseCase()));
    }

    /*
     * @see org.tigris.swidgets.Dialog#nameButtons()
     */
    @Override
    protected void nameButtons() {
        super.nameButtons();
        nameButton(getOkButton(), "button.go-to-selection");
        nameButton(getCancelButton(), "button.close");
    }

    /*
     * @see java.awt.event.ActionListener#actionPerformed(
     * java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == search) {
            doSearch();
        } else if (e.getSource() == clearTabs) {
            doClearTabs();
        } else if (e.getSource() == getOkButton()) {
            doGoToSelection();
        } else {
            super.actionPerformed(e);
        }
    }


    /**
     * Do the search.
     */
    private void doSearch() {
        numFinds++;
        String eName = "";
        if (elementName.getSelectedItem() != null) {
            eName += elementName.getSelectedItem();
            elementName.removeItem(eName);
            elementName.insertItemAt(eName, 0);
            elementName.setSelectedItem(eName);
        }
        String dName = "";
        if (diagramName.getSelectedItem() != null) {
            dName += diagramName.getSelectedItem();
            diagramName.removeItem(dName);
            diagramName.insertItemAt(dName, 0);
            diagramName.setSelectedItem(dName);
        }
        String name = eName;
        if (dName.length() > 0) {
            Object[] msgArgs = {name, dName };
            name =
		Translator.messageFormat(
                    "dialog.find.comboboxitem.element-in-diagram", msgArgs);
            //name += " in " + dName;
        }
        String typeName = type.getSelectedItem().toString();
        if (!typeName.equals("Any Type")) {
            name += " " + typeName;
        }
        if (name.length() == 0) {
            name =
                Translator.localize("dialog.find.tabname") + (nextResultNum++);
        }
        if (name.length() > 15) {
            // TODO: Localize
            name = name.substring(0, 12) + "...";
        }

        String pName = "";

        Predicate eNamePred = PredicateStringMatch.create(eName);
        Predicate pNamePred = PredicateStringMatch.create(pName);
        Predicate dNamePred = PredicateStringMatch.create(dName);
        Predicate typePred = (Predicate) type.getSelectedItem();
        PredicateFind pred =
            new PredicateFind(eNamePred, pNamePred, dNamePred, typePred);

        ChildGenFind gen = ChildGenFind.getSingleton();
        Object root = ProjectManager.getManager().getCurrentProject();

        TabResults newResults = new TabResults();
        newResults.setTitle(name);
        newResults.setPredicate(pred);
        newResults.setRoot(root);
        newResults.setGenerator(gen);
        resultTabs.add(newResults);
        results.addTab(name, newResults);
        clearTabs.setEnabled(true);
        getOkButton().setEnabled(true);
        results.setSelectedComponent(newResults);
        Object[] msgArgs = {name };
        location.addItem(Translator.messageFormat(
                "dialog.find.comboboxitem.in-tab", msgArgs));
        invalidate();
        results.invalidate();
        validate();
        newResults.run();
        newResults.requestFocus();
        newResults.selectResult(0);
    }

    /**
     * Reset the user interface to its initial state.  Use this if a new project
     * has been loaded and the Find dialog is being displayed with stale results.
     */
    public void reset() {
        doClearTabs();
        doResetFields(true);
    }
    
    /**
     * Clear the tabs.
     * @deprecated for 0.25.4 by tfmorris.  The visibility of this will be reduced
     * in the future.  Use {@link #reset()}.
     */
    @Deprecated
    public void doClearTabs() {
        int numTabs = resultTabs.size();
        for (int i = 0; i < numTabs; i++) {
            results.remove(resultTabs.get(i));
	}
        resultTabs.clear();
        clearTabs.setEnabled(false);
        getOkButton().setEnabled(false);
        doResetFields(false);
    }



    /**
     * Reset the fields.
     * 
     * @param complete if true, reset all 3 fields (element name, diagram name,
     *                and location), otherwise only the last is reset.
     * @deprecated for 0.25.4 by tfmorris. The visibility of this will be
     *             reduced in the future. Use {@link #reset()}.
     */
    @Deprecated
    public void doResetFields(boolean complete) {
        if (complete) {
            elementName.removeAllItems();
            diagramName.removeAllItems();
            elementName.addItem("*");
            diagramName.addItem("*");
        }
        location.removeAllItems();
        location.addItem(
                Translator.localize("dialog.find.comboboxitem.entire-project"));
    }

    /**
     * Reset all 3 fields.
     * 
     * @deprecated for 0.25.4 by tfmorris. The visibility of this will be
     *             reduced in the future. Use {@link #reset()}.
     */
    @Deprecated
    public void doResetFields() {
        doResetFields(true);
    }

    /**
     * Execute the GoTo selection command.
     */
    private void doGoToSelection() {
        if (results.getSelectedComponent() instanceof TabResults) {
            ((TabResults) results.getSelectedComponent()).doDoubleClick();
        }
    }


    ////////////////////////////////////////////////////////////////
    // MouseListener implementation

    /*
     * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
     */
    public void mousePressed(MouseEvent me) { 
        // empty implementation - we only handle mouseClicked
    }

    /*
     * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
     */
    public void mouseReleased(MouseEvent me) { 
        // empty implementation - we only handle mouseClicked
    }

    /*
     * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
     */
    public void mouseEntered(MouseEvent me) { 
        // empty implementation - we only handle mouseClicked
    }

    /*
     * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
     */
    public void mouseExited(MouseEvent me) { 
        // empty implementation - we only handle mouseClicked
    }

    /*
     * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
     */
    public void mouseClicked(MouseEvent me) {
        int tab = results.getSelectedIndex();
        if (tab != -1) {
            Rectangle tabBounds = results.getBoundsAt(tab);
            if (!tabBounds.contains(me.getX(), me.getY())) {
		return;
	    }
            if (tab >= 1 && me.getClickCount() >= 2) {
                myDoubleClick(tab - 1); //help tab is 0
	    }
        }
    }

    /**
     * Double click on tab detaches it in a free floating window.
     *
     * MVW: This is the only place where spawning is still enabled.
     *
     * @param tab the given tab
     */
    private void myDoubleClick(int tab) {
        JPanel t = resultTabs.get(tab);
        if (t instanceof AbstractArgoJPanel) {
            if (((AbstractArgoJPanel) t).spawn() != null) {
                resultTabs.remove(tab);
                location.removeItem("In Tab: "
                                + ((AbstractArgoJPanel) t).getTitle());
            }
        }
    }

    /**
     * The UID.
     */
    private static final long serialVersionUID = 9209251878896557216L;
} /* end class FindDialog */


/**
 * PredicateMType is a small helper class which converts the
 * string representation of the Type from internal form.
 * Now that Type names aren't prefixed with NSUML's "M" the
 * name of the class is somewhat of a misnomer.
 */
class PredicateMType extends PredicateType {
    /**
     * The constructor.
     *
     * @param pats The classes.
     */
    protected PredicateMType(Class[] pats) {
        super(pats, pats.length);
    }

    /**
     * The constructor.
     *
     * @param pats The classes (or <code>null</code> if numPats is 0.
     * @param numPats The count of classes.
     */
    protected PredicateMType(Class[] pats, int numPats) {
        super(pats, numPats);
    }

    /**
     * Create without classes.
     *
     * @return A newly created PredicateType.
     */
    public static PredicateType create() {
        return new PredicateMType(null, 0);
    }

    /**
     * Create with one class.
     *
     * @param c0 First class.
     * @return A newly created PredicateType.
     */
    public static PredicateType create(Object c0) {
        Class[] classes = new Class[1];
        classes[0] = (Class) c0;
        return new PredicateMType(classes);
    }

    /**
     * Create with two classes.
     *
     * @param c0 First class.
     * @param c1 Second class.
     * @return A newly created PredicateType.
     */
    public static PredicateType create(Object c0, Object c1) {
        Class[] classes = new Class[2];
        classes[0] = (Class) c0;
        classes[1] = (Class) c1;
        return new PredicateMType(classes);
    }

    /**
     * Create with three classes.
     *
     * @param c0 First class.
     * @param c1 Second class.
     * @param c2 Third class.
     * @return A newly created PredicateType.
     */
    public static PredicateType create(Object c0, Object c1, Object c2) {
        Class[] classes = new Class[3];
        classes[0] = (Class) c0;
        classes[1] = (Class) c1;
        classes[2] = (Class) c2;
        return new PredicateMType(classes);
    }


    @Override
    public String toString() {
        String result = super.toString();
        // TODO: This shouldn't know the internal form of type names,
        // but I'm not sure what GEF's PredicateType does, so I'm fixing it
        // here - tfm
        if (result.startsWith("Uml")) {
            result = result.substring(3);
        }
        return result;
    }

    /**
     * The UID.
     */
    private static final long serialVersionUID = 901828109709882796L;
} /* end class PredicateMType */
