package org.argouml.uml.ui;

import java.awt.event.ActionEvent;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

import org.argouml.kernel.ProjectManager;
import org.argouml.ui.ArgoDiagram;
import org.argouml.ui.ProjectBrowser;
import org.argouml.ui.targetmanager.TargetManager;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.graph.MutableGraphModel;
import org.tigris.gef.presentation.Fig;

import ru.novosoft.uml.foundation.core.MModelElement;

/**
 * The base class for models behind scrollpanes for binary relations like the
 * association pane on PropPanelUsecase
 *
 * @deprecated as of ArgoUml 0.13.5 (10-may-2003),
 *             replaced by {@link org.argouml.uml.ui.foundation.core.PropPanelClassifier#getAssociationEndScroll()},
 *             this class is part of the 'old'(pre 0.13.*) implementation of proppanels
 *             that used reflection a lot.
 */
abstract public class UMLBinaryRelationListModel
    extends UMLModelElementListModel {

    /**
     * Constructor for UMLBinaryConnectionListModel.
     * @param container
     * @param property
     * @param showNone
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
        if (target instanceof MModelElement) {
            MModelElement melement = (MModelElement) target;
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
                    MModelElement othermelement = (MModelElement) it.next();
                    if (!selected.contains(othermelement)) {
                        ProjectBrowser pb = ProjectBrowser.getInstance();
                        ArgoDiagram diagram = ProjectManager.getManager().getCurrentProject().getActiveDiagram();
                        Fig figfrom =
                            diagram.getLayer().presentationFor(melement);
                        Fig figto =
                            diagram.getLayer().presentationFor(othermelement);
                        if (figfrom != null && figto != null) {
                            GraphModel gm = diagram.getGraphModel();
                            if (gm instanceof MutableGraphModel) {
                                connect(
                                    (MutableGraphModel) gm,
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
                    MModelElement othermelement = (MModelElement) it.next();
                    if (!dialog.getSelected().contains(othermelement)) {
                        MModelElement connector =
                            getRelation(melement, othermelement);
                        Object pt = TargetManager.getInstance().getTarget();
                        TargetManager.getInstance().setTarget(connector);
                        ActionEvent event = new ActionEvent(this, 1, "delete");
                        ActionRemoveFromModel.SINGLETON.actionPerformed(event);
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
        if (target instanceof MModelElement) {
            MModelElement melement = (MModelElement) target;
            MModelElement othermelement =
                (MModelElement) getModelElementAt(index);
            MModelElement relation = getRelation(melement, othermelement);
            Object pt = TargetManager.getInstance().getTarget();
            TargetManager.getInstance().setTarget(relation);
            ActionEvent event = new ActionEvent(this, 1, "delete");
            ActionRemoveFromModel.SINGLETON.actionPerformed(event);
            TargetManager.getInstance().setTarget(pt);
            fireIntervalRemoved(this, index, index);
        }
    }

    /**
     * Gets the collection of modelelements a user can select from (left pane
     * in UMLAddDialog)
     * @return Collection
     */
    abstract protected Collection getChoices();

    /**
     * Gets the collection of modelelements that are allready selected before
     * the add method is called
     * @return Collection
     */
    abstract protected Collection getSelected();

    /**
     * Returns the title of the add dialog
     * @return String
     */
    abstract protected String getAddDialogTitle();

    /**
     * Connects two modelelements. The only implementation of this class could be
     * something simple as gm.connect(from, to). This method is only abstract since
     * in some cases gm.connect(to, from) may be necessary.
     * @param from
     * @param to
     */
    abstract protected void connect(
        MutableGraphModel gm,
        MModelElement from,
        MModelElement to);

    /**
     * Builds a relation between two modelelements. A relation is for example
     * an association or a generalization relationship. Only builds the modelelement,
     * not the graphics.
     * @param from
     * @param to
     */
    abstract protected void build(MModelElement from, MModelElement to);

    /**
     * Gets the relation between two modelelements. Implementations should 
     * delegate to utility methods provided in the helpers like CoreHelper
     * @param from
     * @param to
     * @return MModelElement
     */
    abstract protected MModelElement getRelation(
        MModelElement from,
        MModelElement to);

    /**
     * @see org.argouml.uml.ui.UMLModelElementListModel#buildPopup(JPopupMenu, int)
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
    protected MModelElement getModelElementAt(int index) {
        return elementAtUtil(getSelected(), index, MModelElement.class);
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
     * <p>
     * The source of the relation to be layed out. For example, in an association
     * this is one of the classifiers to be connected. Normally users of this class
     * do not have to override this. Only when the target of the container is
     * different then the source of the relation, this must be overriden.
     * </p>
     * @return MModelElement
     */
    protected MModelElement getSource() {
        if (getTarget() instanceof MModelElement) {
            return (MModelElement) getTarget();
        } else
            throw new IllegalStateException("In getSource: target is not a modelelement");
    }

}
