// File: FigClassAssociationClass.java
// Classes: FigClassAssociationClass
// Original Author: pepargouml@yahoo.es

package org.argouml.uml.diagram.ui;

import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.diagram.static_structure.ui.FigClass;
import org.tigris.gef.base.Editor;
import org.tigris.gef.base.Globals;

/**
 * Class to display a class in an Association Class
 * It must be used only from a FigAssociationClass
 */
public class FigClassAssociationClass
        extends FigClass {

    /**
     * The FigAssociationClass that is associated to
     */
    FigAssociationClass mainFig;

    public FigClassAssociationClass() {
        super();
        enableSizeChecking(true);
    }

    public FigClassAssociationClass(FigAssociationClass ownerFig) {
        this();
        mainFig = ownerFig;
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
        Editor ce = Globals.curEditor();
        ce.getSelectionManager().deselect(this);
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

} /* end class FigClassAssociationClass */
