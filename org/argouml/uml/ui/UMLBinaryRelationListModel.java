// $Id$
// Copyright (c) 2003-2004 The Regents of the University of California. All
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

import java.awt.event.ActionEvent;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.ModelFacade;
import org.argouml.ui.ArgoDiagram;
import org.argouml.ui.ProjectBrowser;
import org.argouml.ui.targetmanager.TargetManager;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.graph.MutableGraphModel;
import org.tigris.gef.presentation.Fig;

/**
 * The base class for models behind scrollpanes for binary relations like the
 * association pane on PropPanelUsecase
 *
 * @deprecated as of ArgoUml 0.13.5 (10-may-2003), replaced by 
 * {@link org.argouml.uml.ui.foundation.core.PropPanelClassifier#
 * getAssociationEndScroll()},
 *             this class is part of the 'old'(pre 0.13.*) implementation
 *             of proppanels that used reflection a lot.
 */
public abstract class UMLBinaryRelationListModel
    extends UMLModelElementListModel {

    /**
     * Constructor for UMLBinaryConnectionListModel.
     * @param container the ui element that contains this list
     * @param property a string that specifies the name of an event
     * that should force a refresh of the list model.  A null value
     * will cause all events to trigger a refresh.
     * @param showNone true if a "none" has to be shown when there 
     *                 is no name to show
     */
    public UMLBinaryRelationListModel(
        UMLUserInterfaceContainer container,
        String property,
        boolean showNone) {
        super(container, property, showNone);
    }

    /**
     * @see org.argouml.uml.ui.UMLConnectionListModel#add(int)
     */
    public void add(int index) {
        Object target = getSource();
        if (ModelFacade.isAModelElement(target)) {
            Object/*MModelElement*/ melement = target;
            Vector choices = new Vector();
            Vector selected = new Vector();
            choices.addAll(getChoices());
            choices.remove(melement);
            selected.addAll(getSelected());
            UMLAddDialog dialog =
                new UMLAddDialog(
                    choices,
                    selected,
                    getAddDialogTitle(),
                    true,
                    true);
            int returnValue = dialog.showDialog(ProjectBrowser.getInstance());
            if (returnValue == JOptionPane.OK_OPTION) {
                Iterator it = dialog.getSelected().iterator();
                while (it.hasNext()) {
                    Object/*MModelElement*/ othermelement = it.next();
                    if (!selected.contains(othermelement)) {
                        ProjectBrowser pb = ProjectBrowser.getInstance();
			Project currentProject =
			    ProjectManager.getManager().getCurrentProject();
                        ArgoDiagram diagram =
			    currentProject.getActiveDiagram();
                        Fig figfrom =
                            diagram.getLayer().presentationFor(melement);
                        Fig figto =
                            diagram.getLayer().presentationFor(othermelement);
                        if (figfrom != null && figto != null) {
                            GraphModel gm = diagram.getGraphModel();
                            if (gm instanceof MutableGraphModel) {
                                connect((MutableGraphModel) gm,
					melement,
					othermelement);
                            }
                        } else {
                            build(melement, othermelement);
                        }
                    }
                }
                it = selected.iterator();
                while (it.hasNext()) {
                    Object/*MModelElement*/ othermelement = it.next();
                    if (!dialog.getSelected().contains(othermelement)) {
                        Object/*MModelElement*/ connector =
                            getRelation(melement, othermelement);
                        Object pt = TargetManager.getInstance().getTarget();
                        TargetManager.getInstance().setTarget(connector);
                        ActionEvent event = new ActionEvent(this, 1, "delete");
                        new ActionRemoveFromModel().actionPerformed(event);
                        TargetManager.getInstance().setTarget(pt);
                    }
                }
            }
        }
    }

    /**
     * @see org.argouml.uml.ui.UMLModelElementListModel#delete(int)
     */
    public void delete(int index) {
        Object target = getSource();
        if (org.argouml.model.ModelFacade.isAModelElement(target)) {
            Object/*MModelElement*/ melement = target;
            Object/*MModelElement*/ othermelement = getModelElementAt(index);
            Object/*MModelElement*/ relation =
		getRelation(melement, othermelement);
            Object pt = TargetManager.getInstance().getTarget();
            TargetManager.getInstance().setTarget(relation);
            ActionEvent event = new ActionEvent(this, 1, "delete");
            new ActionRemoveFromModel().actionPerformed(event);
            TargetManager.getInstance().setTarget(pt);
            fireIntervalRemoved(this, index, index);
        }
    }

    /**
     * Gets the collection of modelelements a user can select from (left pane
     * in UMLAddDialog)
     * @return Collection
     */
    protected abstract Collection getChoices();

    /**
     * Gets the collection of modelelements that are allready selected before
     * the add method is called
     * @return Collection
     */
    protected abstract Collection getSelected();

    /**
     * Returns the title of the add dialog
     * @return String
     */
    protected abstract String getAddDialogTitle();

    /**
     * Connects two modelelements. The only implementation of this
     * class could be something simple as gm.connect(from, to). This
     * method is only abstract since in some cases gm.connect(to,
     * from) may be necessary.
     *
     * @param gm the graph model
     * @param from the source of the connection
     * @param to the destination of the connection
     */
    protected abstract void connect(
        MutableGraphModel gm,
        Object/*MModelElement*/ from,
        Object/*MModelElement*/ to);

    /**
     * Builds a relation between two modelelements. A relation is for
     * example an association or a generalization relationship. Only
     * builds the modelelement, not the graphics.
     *
     * @param from the source of the relation
     * @param to the destination of the relation
     */
    protected abstract void build(Object/*MModelElement*/ from,
				  Object/*MModelElement*/ to);

    /**
     * Gets the relation between two modelelements. Implementations should
     * delegate to utility methods provided in the helpers like CoreHelper
     * @param from the source of the relation
     * @param to the destination of the relation
     * @return MModelElement
     */
    protected abstract Object getRelation(
        Object from,
        Object to);

    /**
     * @see org.argouml.uml.ui.UMLModelElementListModel#buildPopup(
     *         JPopupMenu, int)
     */
    public boolean buildPopup(JPopupMenu popup, int index) {
        UMLUserInterfaceContainer container = getContainer();
        UMLListMenuItem open =
            new UMLListMenuItem(
                container.localize("Open"),
                this,
                "open",
                index);
        UMLListMenuItem delete =
            new UMLListMenuItem(
                container.localize("Delete"),
                this,
                "delete",
                index);
        if (getModelElementSize() <= 0) {
            open.setEnabled(false);
            delete.setEnabled(false);
        }

        popup.add(open);
        UMLListMenuItem add =
            new UMLListMenuItem(container.localize("Add"), this, "add", index);
        if (getChoices() != null
            && ((getChoices().size() == 1 && getChoices().contains(getSource()))
                || getChoices().isEmpty())
            && getSelected().size() == 0) {
            add.setEnabled(false);
        }
        popup.add(add);
        popup.add(delete);

        return true;
    }

    /**
     * @see org.argouml.uml.ui.UMLModelElementListModel#getModelElementAt(int)
     */
    protected Object getModelElementAt(int index) {
        return elementAtUtil(getSelected(), index,
			     (Class) ModelFacade.MODELELEMENT);
    }

    /**
     * @see org.argouml.uml.ui.UMLModelElementListModel#recalcModelElementSize()
     */
    protected int recalcModelElementSize() {
        Collection collection = getSelected();
        if (collection != null) {
            return collection.size();
        } else
            return 1;

    }

    /**
     * The source of the relation to be layed out. For example, in an
     * association this is one of the classifiers to be
     * connected. Normally users of this class do not have to override
     * this. Only when the target of the container is different then
     * the source of the relation, this must be overriden.<p>
     *
     * @return MModelElement
     */
    protected Object getSource() {
        if (ModelFacade.isAModelElement(getTarget())) {
            return getTarget();
        } else {
            throw new IllegalStateException("In getSource: "
					    + "target is not a modelelement");
        }
    }

}
