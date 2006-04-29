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

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JTree;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import org.apache.log4j.Logger;
import org.argouml.application.api.Configuration;
import org.argouml.cognitive.ToDoItem;
import org.argouml.cognitive.ToDoList;
import org.argouml.model.InvalidElementException;
import org.argouml.model.Model;
import org.argouml.notation.Notation;
import org.argouml.notation.NotationHelper;
import org.argouml.notation.NotationProvider4;
import org.argouml.notation.NotationProviderFactory2;
import org.argouml.uml.generator.GeneratorDisplay;
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
     *  values are Vector of currently expanded paths.
     * </pre>
     */
    private Hashtable expandedPathsInModel;

    private boolean reexpanding;

    /**
     * This determines if stereotypes are to be shown in the explorer.
     */
    protected boolean showStereotype;

    /**
     * Sets the label renderer, line style angled, enable tooltips,
     * sets row height to 18 pixels.
     */
    public DisplayTextTree() {

        super();

        /* MVW: We should use default font sizes as much as possible.
         * BTW, this impacts only the width, and reduces readibility:*/
//        setFont(LookAndFeelMgr.getInstance().getSmallFont());

        setCellRenderer(new UMLTreeCellRenderer());
        setRootVisible(false);
        setShowsRootHandles(true);

        // This enables tooltips for tree; this one won't be shown:
        setToolTipText("Tree");

        /* The default (16) puts the icons too close together: */
        this.setRowHeight(18);

        expandedPathsInModel = new Hashtable();
        reexpanding = false;
        showStereotype =
    	    Configuration.getBoolean(Notation.KEY_SHOW_STEREOTYPES, false);
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
                // Jeremy Bennett patch
                if (Model.getFacade().isATransition(value)) {
                    name = Model.getFacade().getName(value);
                    NotationProvider4 notationProvider =
                        NotationProviderFactory2.getInstance().getNotationProvider(
                                    NotationProviderFactory2.TYPE_TRANSITION,
                                    NotationHelper.getDefaultNotationContext(),
                                    value);
                    String signature = notationProvider.toString();
                    if (name != null && name.length() > 0) {
                        name += ": " + signature;
                    } else {
                        name = signature;
                    }
                } else if (Model.getFacade().isAExtensionPoint(value)) {
                    NotationProvider4 notationProvider =
                        NotationProviderFactory2.getInstance().getNotationProvider(
                                    NotationProviderFactory2.TYPE_EXTENSION_POINT,
                                    NotationHelper.getDefaultNotationContext(),
                                    value);
                    name = notationProvider.toString();
                } else if (Model.getFacade().isAComment(value)) {
                    /*
                     * From UML 1.4 onwards, the text of the comment
                     * is stored in the "body".
                     */
                    name = (String) Model.getFacade().getBody(value);
                } else if (Model.getFacade().isAElementImport(value)) {
                    // TODO: Localize
                    StringBuffer s = new StringBuffer("Imported ");
                    Object me = Model.getFacade().getImportedElement(value);
                    s.append(Model.getFacade().getUMLClassName(me));
                    s.append(": ");
                    // TODO: Handle the Alias from the ElementImport.
                    s.append(convertValueToText(me, selected, expanded, leaf, row,
                            hasFocus));
                    name = s.toString();
                } else if (Model.getFacade().isATaggedValue(value)) {
                    String tagName = Model.getFacade().getTagOfTag(value);
                    if (tagName == null || tagName.equals("")) {
                        // TODO: Localize
                        tagName = "(unnamed)";
                    }
                    name = ("1-" + tagName);
                } else {
                    name = Model.getFacade().getName(value);
                }

                if (name == null || name.equals("")) {
                    // TODO: Localize
                    name =
                        "(unnamed " + Model.getFacade().getUMLClassName(value)
                            + ")";
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
                    Iterator i = Model.getFacade().getStereotypes(value).iterator();
                    Object stereo;
                    while (i.hasNext()) {
                        stereo = i.next();
                        name +=
                            " " + GeneratorDisplay.getInstance().generate(stereo);
                    }
                    if (name != null && name.length() > 80) {
                        name = name.substring(0, 80) + "...";
                    }
                }
            } catch (InvalidElementException e) {
                // TODO: Localize
                name = "*deleted element*";
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
                // TODO: Localize
                return "*deleted expression*";
            }
        }

        if (Model.getFacade().isAMultiplicity(value)) {
            try {
                // TODO: Localize
                return "Multiplicity: " 
                + Model.getDataTypesHelper().multiplicityToString(value);
            } catch (InvalidElementException e) {
                return "*deleted multiplicity*";
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

    /**
     * Tree Model Expansion notification.<p>
     * 
     * @param path
     *            a Tree node insertion event
     */
    public void fireTreeExpanded(TreePath path) {

        super.fireTreeExpanded(path);

        LOG.debug("fireTreeExpanded");
        if (reexpanding) {
            return;
        }
        if (path == null || expandedPathsInModel == null) {
            return;
        }
        Vector expanded = getExpandedPaths();
        expanded.removeElement(path);
        expanded.addElement(path);
    }

    /**
     * @see javax.swing.JTree#fireTreeCollapsed(javax.swing.tree.TreePath)
     */
    public void fireTreeCollapsed(TreePath path) {

        super.fireTreeCollapsed(path);

        LOG.debug("fireTreeCollapsed");
        if (path == null || expandedPathsInModel == null) {
            return;
        }
        Vector expanded = getExpandedPaths();
        expanded.removeElement(path);
    }

    /**
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
     * @return a Vector containing all expanded paths
     */
    protected Vector getExpandedPaths() {

        LOG.debug("getExpandedPaths");
        TreeModel tm = getModel();
        Vector res = (Vector) expandedPathsInModel.get(tm);
        if (res == null) {
            res = new Vector();
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

        Enumeration pathsEnum = getExpandedPaths().elements();
        while (pathsEnum.hasMoreElements()) {
            TreePath path = (TreePath) pathsEnum.nextElement();
            expandPath(path);
        }
        reexpanding = false;
    }

    /**
     * The UID.
     */
    private static final long serialVersionUID = 949560309817566838L;
}
