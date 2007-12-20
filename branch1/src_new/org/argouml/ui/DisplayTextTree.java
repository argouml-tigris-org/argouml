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

package org.argouml.ui;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;


import javax.swing.JTree;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import org.apache.log4j.Logger;
import org.argouml.cognitive.ToDoItem;
import org.argouml.cognitive.ToDoList;
import org.argouml.i18n.Translator;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.kernel.ProjectSettings;
import org.argouml.model.InvalidElementException;
import org.argouml.model.Model;
import org.argouml.notation.NotationProvider;
import org.argouml.notation.NotationProviderFactory2;
import org.argouml.uml.ui.UMLTreeCellRenderer;
import org.tigris.gef.base.Diagram;

/**
 * This is the JTree that is the GUI component view of the UML model
 * navigation (the explorer) and the todo list.
 */
public class DisplayTextTree extends JTree {
    /**
     * Logger.
     */
    private static final Logger LOG = Logger.getLogger(DisplayTextTree.class);

    /**
     * A Map helping the tree maintain a consistent expanded paths state.
     *
     * <pre>
     *  keys are the current TreeModel of this Tree
     *  values are Lists of currently expanded paths.
     * </pre>
     */
    private Hashtable<TreeModel, List<TreePath>> expandedPathsInModel;

    private boolean reexpanding;

    /**
     * This determines if stereotypes are to be shown in the explorer.
     */
    private boolean showStereotype;

    /**
     * Sets the label renderer, line style angled, enable tooltips,
     * sets row height to 18 pixels.
     */
    public DisplayTextTree() {

        super();

        /* MVW: We should use default font sizes as much as possible.
         * BTW, this impacts only the width, and reduces readibility:
         */
//        setFont(LookAndFeelMgr.getInstance().getSmallFont());

        setCellRenderer(new UMLTreeCellRenderer());
        setRootVisible(false);
        setShowsRootHandles(true);

        // This enables tooltips for tree; this one won't be shown:
        setToolTipText("Tree");

        /* The default (16) puts the icons too close together: */
        setRowHeight(18);

        expandedPathsInModel = new Hashtable<TreeModel, List<TreePath>>();
        reexpanding = false;

        Project p = ProjectManager.getManager().getCurrentProject();
        ProjectSettings ps = p.getProjectSettings();
        showStereotype = ps.getShowStereotypesValue();
    }

    // ------------ methods that override JTree methods ---------

    /**
     * Override the default JTree implementation to display the appropriate text
     * for any object that will be displayed in the todo list. <p>
     *
     * This is used for the Todo list as well as the Explorer list.
     *
     * @param value
     *            the given object
     * @param selected
     *            ignored
     * @param expanded
     *            ignored
     * @param leaf
     *            ignored
     * @param row
     *            ignored
     * @param hasFocus
     *            ignored
     *
     * @return the value converted to text.
     *
     * @see javax.swing.JTree#convertValueToText(java.lang.Object, boolean,
     *      boolean, boolean, int, boolean)
     */
    public String convertValueToText(Object value, boolean selected,
            boolean expanded, boolean leaf, int row, boolean hasFocus) {

        if (value instanceof ToDoItem) {
            return ((ToDoItem) value).getHeadline();
        }
        if (value instanceof ToDoList) {
            // TODO: Localize
            return "ToDoList";
        }

        if (Model.getFacade().isAModelElement(value)) {
            String name = null;
            try {
                if (Model.getFacade().isATransition(value)) {
                    name = formatTransitionLabel(value);
                } else if (Model.getFacade().isAExtensionPoint(value)) {
                    NotationProvider notationProvider =
                        NotationProviderFactory2.getInstance()
                            .getNotationProvider(
                                NotationProviderFactory2.TYPE_EXTENSION_POINT,
                                value);
                    name = notationProvider.toString(value, null);
                } else if (Model.getFacade().isAComment(value)) {
                    name = (String) Model.getFacade().getBody(value);
                } else if (Model.getFacade().isATaggedValue(value)) {
                    name = formatTaggedValueLabel(value);
                } else {
                    name = getModelElementDisplayName(value);
                }

                /*
                 * If the name is too long or multi-line (e.g. for comments)
                 * then we reduce to the first line or 80 chars.
                 */
                // TODO: Localize
                if (name != null
                        && name.indexOf("\n") < 80
                        && name.indexOf("\n") > -1) {
                    name = name.substring(0, name.indexOf("\n")) + "...";
                } else if (name != null && name.length() > 80) {
                    name = name.substring(0, 80) + "...";
                }

                // Look for stereotype
                if (showStereotype) {
                    Collection<Object> stereos =
                        Model.getFacade().getStereotypes(value);
                    name += " " + generateStereotype(stereos);
                    if (name != null && name.length() > 80) {
                        name = name.substring(0, 80) + "...";
                    }
                }
            } catch (InvalidElementException e) {
                name = Translator.localize("misc.name.deleted");
            }

            return name;
        }

        if (Model.getFacade().isAExpression(value)) {
            try {
                String name = Model.getFacade().getUMLClassName(value);
                String language = Model.getDataTypesHelper().getLanguage(value);
                String body = Model.getDataTypesHelper().getBody(value);
                if (language != null && language.length() > 0) {
                    name += " (" + language + ")";
                }
                if (body != null && body.length() > 0) {
                    name += ": " + body;
                }
                return name;
            } catch (InvalidElementException e) {
                return Translator.localize("misc.name.deleted");
            }
        }
        
        if (Model.getFacade().isAElementImport(value)) {
            try {
                Object me = Model.getFacade().getImportedElement(value);
                String typeName = Model.getFacade().getUMLClassName(me);
                String elemName = convertValueToText(me, selected, 
                        expanded, leaf, row,
                        hasFocus);
                String alias = Model.getFacade().getAlias(value);
                if (alias != null && alias.length() > 0) {
                    Object[] args = {typeName, elemName, alias};
                    return Translator.localize(
                            "misc.name.element-import.alias", args);
                } else {
                    Object[] args = {typeName, elemName};
                    return Translator.localize(
                            "misc.name.element-import", args);
                }
            } catch (InvalidElementException e) {
                return Translator.localize("misc.name.deleted");
            }
        }

        if (Model.getFacade().isAMultiplicity(value)) {
            try {
                // TODO: Localize
                return "Multiplicity: "
                    + Model.getDataTypesHelper().multiplicityToString(value);
            } catch (InvalidElementException e) {
                return Translator.localize("misc.name.deleted");
            }
        }
        
        if (Model.getFacade().isAElementResidence(value)) {
            try {
                // TODO: Add the container and resident names
                return "ElementResidence";
            } catch (InvalidElementException e) {
                return Translator.localize("misc.name.deleted");
            }            
        }

        if (value instanceof Diagram) {
            return ((Diagram) value).getName();
        }

        if (value != null) {
            return value.toString();
        }
        return "-";
    }

    private String formatTaggedValueLabel(Object value) {
        String name;
        String tagName = Model.getFacade().getTag(value);
        if (tagName == null || tagName.equals("")) {
            name = MessageFormat.format(
                    Translator.localize("misc.unnamed"),
                    new Object[] {
                        Model.getFacade().getUMLClassName(value)
                    });
        }
        Collection referenceValues = 
            Model.getFacade().getReferenceValue(value);
        Collection dataValues = 
            Model.getFacade().getDataValue(value);
        Iterator i;
        if (referenceValues.size() > 0) {
            i = referenceValues.iterator();
        } else {
            i = dataValues.iterator();
        }
        String theValue = "";
        if (i.hasNext()) {
            theValue = i.next().toString();
        }
        if (i.hasNext()) {
            theValue += " , ...";
        }
        name = (tagName + " = " + theValue);
        return name;
    }

    private String formatTransitionLabel(Object value) {
        String name;
        name = Model.getFacade().getName(value);
        NotationProvider notationProvider =
            NotationProviderFactory2.getInstance()
                .getNotationProvider(
                        NotationProviderFactory2.TYPE_TRANSITION,
                        value);
        String signature = notationProvider.toString(value, null);
        if (name != null && name.length() > 0) {
            name += ": " + signature;
        } else {
            name = signature;
        }
        return name;
    }

    /**
     * @param st a collection of stereotypes
     * @return a string representing the given stereotype(s)
     */
    public static String generateStereotype(Collection<Object> st) {
        if (st == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder(10);
        boolean first = true;
        for (Object o : st) {
            if (!first) {
                sb.append(',');
            }
            if (o != null) {
                sb.append(Model.getFacade().getName(o));
                first = false;
            }
        }
        if (!first) {
            Project project =
                ProjectManager.getManager().getCurrentProject();
            ProjectSettings ps = project.getProjectSettings();
            return ps.getLeftGuillemot()
                + sb.toString()
                + ps.getRightGuillemot();
        }
        return "";
    }

    public static final String getModelElementDisplayName(Object modelElement) {
        String name = Model.getFacade().getName(modelElement);
        if (name == null || name.equals("")) {
            name = MessageFormat.format(
        	    Translator.localize("misc.unnamed"),
                    new Object[] {
        		Model.getFacade().getUMLClassName(modelElement)
                    }
    	    );
        }
        return name;
    }

    /**
     * Tree Model Expansion notification.<p>
     *
     * @param path
     *            a Tree node insertion event
     */
    public void fireTreeExpanded(TreePath path) {

        super.fireTreeExpanded(path);

        LOG.debug("fireTreeExpanded");
        if (reexpanding || path == null) {
            return;
        }
        List<TreePath> expanded = getExpandedPaths();
        expanded.remove(path);
        expanded.add(path);
    }

    /*
     * @see javax.swing.JTree#fireTreeCollapsed(javax.swing.tree.TreePath)
     */
    public void fireTreeCollapsed(TreePath path) {

        super.fireTreeCollapsed(path);

        LOG.debug("fireTreeCollapsed");
        if (path == null || expandedPathsInModel == null) {
            return;
        }
        List<TreePath> expanded = getExpandedPaths();
        expanded.remove(path);
    }

    /*
     * @see javax.swing.JTree#setModel(javax.swing.tree.TreeModel)
     */
    public void setModel(TreeModel newModel) {

        LOG.debug("setModel");
        Object r = newModel.getRoot();
        if (r != null) {
            super.setModel(newModel);
        }
        reexpand();
    }

    // ------------- other methods ------------------

    /**
     * Called in reexpand().
     *
     * @return a List containing all expanded paths
     */
    protected List<TreePath> getExpandedPaths() {

        LOG.debug("getExpandedPaths");
        TreeModel tm = getModel();
        List<TreePath> res = expandedPathsInModel.get(tm);
        if (res == null) {
            res = new ArrayList<TreePath>();
            expandedPathsInModel.put(tm, res);
        }
        return res;
    }

    /**
     * We re-expand the ones that were open before to maintain the same viewable
     * tree.
     *
     * called by doForceUpdate(), setModel()
     */
    private void reexpand() {

        LOG.debug("reexpand");
        if (expandedPathsInModel == null) {
            return;
        }

        reexpanding = true;

        for (TreePath path : getExpandedPaths()) {
            expandPath(path);
        }
        reexpanding = false;
    }

    /**
     * @param show true if stereotypes have to be shown
     */
    protected void setShowStereotype(boolean show) {
        this.showStereotype = show;
    }

    /**
     * The UID.
     */
    private static final long serialVersionUID = 949560309817566838L;
}
