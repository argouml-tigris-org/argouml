// $Id$
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

package org.argouml.uml.diagram.ui;

import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.diagram.static_structure.ui.FigClass;
import org.tigris.gef.base.Editor;
import org.tigris.gef.base.Globals;

/**
 * Class to display a class in an Association Class
 * It must be used only from a FigAssociationClass
 *
 * @author pepargouml
 */
public class FigClassAssociationClass
        extends FigClass {

    /**
     * The FigAssociationClass that is associated to
     */
    private FigAssociationClass mainFig;

    /**
     * The constructor.
     */
    public FigClassAssociationClass(Object owner, int x, int y, int w, int h) {
        super(owner, x, y, w, h);
        enableSizeChecking(true);
    }

    /**
     * The constructor.
     *
     * @param ownerFig the owner fig
     */
    public FigClassAssociationClass(FigAssociationClass ownerFig) {
        super(null, ownerFig.getOwner());
        mainFig = ownerFig;
    }

    /**
     * @return the main fig
     */
    public FigAssociationClass getMainFig() {
        return mainFig;
    }

    /**
     * @param f the main fig
     */
    public void setMainFig(FigAssociationClass f) {
        mainFig = f;
    }

//    /**
//     * It not only damages itself but also its
//     * associated FigAssociationClass
//     */
//    public void damage() {
//        if (mainFig != null) {
//            mainFig.figDamaged();
//        }
//        super.damage();
//    }
//
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
