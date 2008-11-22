// $Id$
// Copyright (c) 1996-2008 The Regents of the University of California. All
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


/**
 * A Fig designed to be the child of some FigNode or FigEdge to display the
 * stereotypes of the model element represented by the parent Fig.
 * <p>
 * TODO: The inheritance hierarchy of this class has been changed, so it may not
 * be binary API compatible even though it implements all methods of the missing
 * class (FigCompartment) with compatible signatures. Double check binary
 * compatibility and restore original implementation for deprecation period if
 * necessary.
 * 
 * @author Bob Tarling
 * @deprecated for 0.27.2 by mvw. Use {@link FigStereotypesGroup}.
 */
@Deprecated
public class FigStereotypesCompartment extends FigStereotypesGroup {

    /**
     * The UID.
     */
    private static final long serialVersionUID = -1696363445893406130L;
    
    
    /**
     * The constructor.
     *
     * @param x x
     * @param y y
     * @param w width
     * @param h height
     * @deprecated for 0.27.2 by mvw.  Use 
     * {@link FigStereotypesGroup#FigStereotypesGroup(int, int, int, int)}.
     */
    @Deprecated
    public FigStereotypesCompartment(int x, int y, int w, int h) {
        super(x, y, w, h);
    }
    
    @Deprecated
    protected void createModelElement() {
    }


}
