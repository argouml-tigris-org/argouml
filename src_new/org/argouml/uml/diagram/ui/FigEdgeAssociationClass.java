// File: FigEdgeAssociationClass.java
// Classes: FigEdgeAssociationClass
// Original Author: pepargouml@yahoo.es

package org.argouml.uml.diagram.ui;

import org.argouml.kernel.DelayedVChangeListener;
import org.argouml.ui.targetmanager.TargetManager;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigNode;
import org.tigris.gef.presentation.FigPoly;
import ru.novosoft.uml.MElementEvent;

import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeListener;
import java.beans.VetoableChangeListener;


/**
 * Class to display a connection linking the class and the
 * association in a Association Class
 * It must be used only from a FigAssociationClass
 */
public class FigEdgeAssociationClass
        extends FigEdgeModelElement
        implements VetoableChangeListener,
        DelayedVChangeListener,
        MouseListener,
        KeyListener,
        PropertyChangeListener {

    /**
     * The FigAssociationClass that is associated to
     */
    FigAssociationClass mainFig;
    ////////////////////////////////////////////////////////////////
    // constructors

    public FigEdgeAssociationClass() {
        setBetweenNearestPoints(true);
        ( (FigPoly) _fig).setRectilinear(false);
        setDashed(true);
    }

    public FigEdgeAssociationClass(Fig fromFig, Fig toFig,
                                   FigAssociationClass ownerFig) {
        this();
        if (toFig == null || fromFig == null) {
            throw new IllegalStateException("No destfig or sourcefig while "
                    + "creating FigEdgeAssociationClass");
        }
        mainFig = ownerFig;
        setDestFigNode( (FigNode) toFig);
        setDestPortFig(toFig);
        setSourcePortFig(fromFig);
        computeRoute();
    }

    ////////////////////////////////////////////////////////////////
    // accessors

    public void setFig(Fig f) {
        super.setFig(f);
        _fig.setDashed(true);
    }

    protected boolean canEdit(Fig f) {
        return false;
    }

    protected void modelChanged(MElementEvent e) {
    }

    public FigAssociationClass getMainFig() {
        return mainFig;
    }

    public void setMainFig(FigAssociationClass f) {
        mainFig = f;
    }

    /**
     * It not only damages itself but also its
     * associated FigAssociationClass
     */
    public void damage() {
        if (mainFig != null) {
            mainFig.figDamaged();
        }
        super.damage();
    }

    /**
     * It not only removes itself but also its
     * associated FigAssociationClass
     */
    public void removeFromDiagram() {
        super.removeFromDiagram();
        if (mainFig != null)
            mainFig.removeFromDiagram();
    }

    /**
     * It is used to remove itself without removing its
     * associated FigAssociationClass.
     */
    public void removeThisFromDiagram() {
        super.removeFromDiagram();
        TargetManager.getInstance().removeHistoryElement(this);
    }
} /* end class FigEdgeAssociationClass */
