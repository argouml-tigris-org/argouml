/**
 * Class to display graphics for N-ary association edges (association ends)
 *
 * @author pepargouml@yahoo.es
 */

package org.argouml.uml.diagram.ui;

import org.argouml.application.api.Notation;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import org.argouml.model.ModelFacade;
import org.tigris.gef.base.Layer;
import org.tigris.gef.base.PathConvPercentPlusConst;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigText;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;

/**
 * This class represents an association End Fig on a diagram
 */
public class FigAssociationEnd extends FigEdgeModelElement{

    /**
     * Group for the FigTexts concerning the association end
     */
    private FigTextGroup srcGroup = new FigTextGroup();
    private FigText srcMult, srcRole;
    private FigText srcOrdering;

    public FigAssociationEnd() {
        super();

        srcMult = new FigText(10, 10, 90, 20);
        srcMult.setFont(getLabelFont());
        srcMult.setTextColor(Color.black);
        srcMult.setTextFilled(false);
        srcMult.setFilled(false);
        srcMult.setLineWidth(0);
        srcMult.setMultiLine(false);
        srcMult.setJustification(FigText.JUSTIFY_CENTER);

        srcOrdering = new FigText(10, 10, 90, 20);
        srcOrdering.setFont(getLabelFont());
        srcOrdering.setTextColor(Color.black);
        srcOrdering.setTextFilled(false);
        srcOrdering.setFilled(false);
        srcOrdering.setLineWidth(0);
        srcOrdering.setMultiLine(false);
        srcOrdering.setJustification(FigText.JUSTIFY_CENTER);

        srcRole = new FigText(10, 10, 90, 20);
        srcRole.setFont(getLabelFont());
        srcRole.setTextColor(Color.black);
        srcRole.setTextFilled(false);
        srcRole.setFilled(false);
        srcRole.setLineWidth(0);
        srcRole.setMultiLine(false);
        srcRole.setJustification(FigText.JUSTIFY_CENTER);

        srcGroup.addFig(srcRole);
        srcGroup.addFig(srcOrdering);

        addPathItem(srcMult, new PathConvPercentPlusConst(this, 100, -15, -15));
        addPathItem(srcGroup, new PathConvPercentPlusConst(this, 100, -40, 20));

        setBetweenNearestPoints(true);
        // next line necessary for loading
        setLayer(ProjectManager.getManager().getCurrentProject()
                .getActiveDiagram().getLayer());

    }

    public FigAssociationEnd(Object edge, Layer lay) {
        this();
        setLayer(lay);
        setOwner(edge);
        if (org.argouml.model.ModelFacade.isAAssociationEnd(edge)) {
            Model.getPump()
                    .removeModelEventListener(this, edge);
            Model.getPump()
                    .addModelEventListener(this, edge);
        }
        modelChanged(null);
    }

    /** Returns the name of the OrderingKind.
     * @return "{ordered}", "{sorted}" or "" if null or "unordered"
     */
    private String getOrderingName(Object orderingKind) {
        if (orderingKind == null) return "";
        if (ModelFacade.getName(orderingKind) == null) return "";
        if ("".equals(ModelFacade.getName(orderingKind))) return "";
        if ("unordered".equals(ModelFacade.getName(orderingKind))) return "";

        return "{" + ModelFacade.getName(orderingKind) + "}";
    }

    /**
     * @see org.argouml.uml.diagram.ui.FigEdgeModelElement#textEdited(org.tigris.gef.presentation.FigText)
     */
    protected void textEdited(FigText ft) {
        if (getOwner() == null) return;
        super.textEdited(ft);

        if (ft == srcRole) {
            ModelFacade.setName(getOwner(), srcRole.getText());
        } else if (ft == srcMult) {
            Object multi = Model.getUmlFactory().getDataTypes()
                    .createMultiplicity(srcMult.getText());
            ModelFacade.setMultiplicity(getOwner(), multi);
        }
    }

    private void updateEnd(FigText multiToUpdate, FigText roleToUpdate,
                           FigText orderingToUpdate) {

        Object owner = getOwner();
        if (!ModelFacade.isAAssociationEnd(owner)) {
            throw new IllegalArgumentException();
        }

        Object multi = ModelFacade.getMultiplicity(owner);
        String name = ModelFacade.getName(owner);
        Object order = ModelFacade.getOrdering(owner);
        String visi = "";
        Object stereo = null;
        if (ModelFacade.isNavigable(owner)
                && (ModelFacade.isAClass(ModelFacade.getType(owner))
                || ModelFacade.isAInterface(ModelFacade.getType(owner)))) {
            visi = Notation.generate(this, ModelFacade.getVisibility(owner));
        }
        if (ModelFacade.getStereotypes(owner).size() > 0) {
            stereo = ModelFacade.getStereotypes(owner).iterator().next();
        }

        multiToUpdate.setText(Notation.generate(this, multi));
        orderingToUpdate.setText(getOrderingName(order));
        if (stereo != null) {
            roleToUpdate.setText(Notation.generate(this, stereo)
                    + " " + visi
                    + Notation.generate(this, name));
        } else
            roleToUpdate.setText(visi + Notation.generate(this, name));
    }

    protected void modelChanged(PropertyChangeEvent e) {
        super.modelChanged(e);
        updateEnd(srcMult, srcRole, srcOrdering);
        srcMult.calcBounds();
	    computeRoute();
    }

    /**
     * @see org.tigris.gef.presentation.Fig#removeFromDiagram()
     */
    public void removeFromDiagram() {
        final Object association = ModelFacade.getAssociation(getOwner());
        final Object owner = getOwner();
        final Layer layer = getLayer();
        super.removeFromDiagram();
        SwingUtilities.invokeLater(new Runnable() {
            public void run () {
                Fig associationFig = layer.presentationFor(association);
                if (ModelFacade.getClassifier(owner) != null
                        && associationFig instanceof FigNodeAssociation) {
                    ((FigNodeAssociation) associationFig).removeFromDiagram();
                }
            }
        });
    }

}  /* end class FigAssociationEnd */
