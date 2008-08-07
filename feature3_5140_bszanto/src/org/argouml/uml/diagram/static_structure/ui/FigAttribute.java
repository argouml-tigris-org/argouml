// $Id$
// Copyright (c) 2007 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies. This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason. IN NO EVENT SHALL THE
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

package org.argouml.uml.diagram.static_structure.ui;

import java.awt.Rectangle;

import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import org.argouml.notation.NotationProvider;
import org.tigris.gef.presentation.Fig;

/**
 * Fig with specific knowledge of Attribute display.
 * <p>
 * 
 * This class does not contain any Attribute specific functionality. But since
 * the Feature is an abstract class in the UML metamodel, it seems more logical
 * to follow this structure here, and keep FigFeature abstract, too.
 * 
 * @author Michiel
 */
public class FigAttribute extends FigFeature {

    /**
     * Constructor.
     * @param x x
     * @param y x
     * @param w width
     * @param h height
     * @param aFig the fig
     * @param np the notation provider for the text
     */
    public FigAttribute(int x, int y, int w, int h, Fig aFig,
            NotationProvider np) {
        super(x, y, w, h, aFig, np);
    }

    /**
     * Cyclies the visibility of an attribute when clicking in the beggining 
     * part of the FigAttribute.
     * @param r Hit rectangle.
     * @author bszanto
     */
    public void changeVisibility(Rectangle r) {
        if (super.hit(r)) {            
            // The hit zone is different for the different notations.
            // Java uses words (public, protected, private) while UML 1.4 uses
            // signs (+, -, ~, #), and so on...
            int offset = ProjectManager.getManager().getCurrentProject()
                    .getProjectSettings().getNotationName().getNotationOffset();
            
            
            if (r.x < (_x + offset)) {
                Object atttribute = getOwner();
                Object visibity = Model.getFacade().getVisibility(atttribute);
                
                // visibility is chandes according to prop panel order which is:
                // public, package, protected, private
                if (Model.getVisibilityKind().getPrivate().equals(visibity)) {
                    Model.getCoreHelper().setVisibility(atttribute,
                            Model.getVisibilityKind().getPublic());
                } else if (Model.getVisibilityKind().getPublic().equals(
                        visibity)) {
                    Model.getCoreHelper().setVisibility(atttribute,
                            Model.getVisibilityKind().getPackage());
                } else if (Model.getVisibilityKind().getPackage().equals(
                        visibity)) {
                    Model.getCoreHelper().setVisibility(atttribute,
                            Model.getVisibilityKind().getProtected());
                } else {
                    Model.getCoreHelper().setVisibility(atttribute,
                            Model.getVisibilityKind().getPrivate());
                }
            }
        }
    }
}
