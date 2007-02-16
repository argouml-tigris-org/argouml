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

package org.argouml.uml.diagram.ui;

import java.util.Collection;

import org.argouml.model.Model;
import org.argouml.notation.NotationProviderFactory2;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.diagram.static_structure.ui.FigEnumerationLiteral;
import org.argouml.uml.notation.NotationProvider;
import org.tigris.gef.presentation.Fig;

/**
 * The Fig for the compartment of an Enumeration 
 * that shows a list of enumerationliterals.
 * 
 * @author Tom Morris
 */
public class FigEnumLiteralsCompartment extends FigEditableCompartment {
    /**
     * Serial version for initial implementation.
     */
    private static final long serialVersionUID = 829674049363538379L;

    /**
     * The constructor.
     *
     * @param x x
     * @param y y
     * @param w width
     * @param h height
     */
    public FigEnumLiteralsCompartment(int x, int y, int w, int h) {
        super(x, y, w, h);
    }

    /*
     * @see org.argouml.uml.diagram.ui.FigEditableCompartment#getUmlCollection()
     */
    protected Collection getUmlCollection() {
        Object enumeration = getGroup().getOwner();
        return Model.getFacade().getEnumerationLiterals(enumeration);
    }

    /*
     * @see org.argouml.uml.diagram.ui.FigEditableCompartment#getNotationType()
     */
    protected int getNotationType() {
        /* TODO: Make a dedicated notation that supports 
         * parsing "name1;name2" and maybe other notation... 
         * Also needs to support deleting literal by erasing text. */
        return NotationProviderFactory2.TYPE_NAME;
    }

    /**
     * @see org.argouml.uml.diagram.ui.FigEditableCompartment#createModelElement()
     */
    protected void createModelElement() {
        Object enumeration = getGroup().getOwner();
        Object literal = Model.getCoreFactory().buildEnumerationLiteral(
                "",  enumeration);
        TargetManager.getInstance().setTarget(literal);
    }

    /*
     * @see org.argouml.uml.diagram.ui.FigEditableCompartment#createFigText(int, int, int, int, org.tigris.gef.presentation.Fig, org.argouml.uml.notation.NotationProvider)
     */
    protected FigSingleLineText createFigText(int x, int y, int w, int h, 
            Fig aFig, NotationProvider np) {
        return new FigEnumerationLiteral(x, y, w, h, aFig, np);
    }
    
    
}
