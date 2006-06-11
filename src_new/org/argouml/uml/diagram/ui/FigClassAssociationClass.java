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

import org.argouml.uml.diagram.static_structure.ui.FigClass;

/**
 * Class to display a class in an Association Class
 * It must be used only from a FigAssociationClass
 *
 * @author pepargouml
 */
public class FigClassAssociationClass extends FigClass {

    private static final long serialVersionUID = -4101337246957593739L;
    
    /**
     * The constructor.
     *
     * @param owner the UML object
     * @param x the x of the initial location
     * @param y the y of the initial location
     * @param w the initial width
     * @param h the initial height
     */
    public FigClassAssociationClass(Object owner, int x, int y, int w, int h) {
        super(owner, x, y, w, h);
        enableSizeChecking(true);
    }

    /**
     * The constructor.
     *
     * @param owner the owner UML object
     */
    public FigClassAssociationClass(Object owner) {
        super(null, owner);
    }

} /* end class FigClassAssociationClass */
