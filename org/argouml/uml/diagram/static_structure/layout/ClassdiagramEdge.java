// Copyright (c) 1996-01 The Regents of the University of California. All
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

package org.argouml.uml.diagram.static_structure.layout;

import org.argouml.uml.diagram.layout.*;
import org.argouml.uml.diagram.ui.*;
import org.tigris.gef.presentation.*;

/** This class is an abstract implementation of all edges which are layoutable in the classdiagram.
 *  @author Markus Klink
 *  @since 0.11.1
 */
public abstract class ClassdiagramEdge implements LayoutedEdge {
    
    /** the layout is oriented on a grid. These are our grid spaces.
     */
    static int _vGap;
    static int _hGap;
    
    
    FigEdge currentEdge = null;
    /** the underlying fig of the edge we want to layout */
    FigPoly underlyingFig = null;
    
    /** each fig has a source and a destination port
     */
    Fig destFigNode;
    Fig sourceFigNode;
    
   
    /** Constructor. 
     * @param edge the Edge to layout
     */
    public ClassdiagramEdge(FigEdge edge) {
        currentEdge = edge;
        underlyingFig = new FigPoly();
        underlyingFig.setLineColor(edge.getFig().getLineColor());
       
        destFigNode = edge.getDestFigNode();
        sourceFigNode = edge.getSourceFigNode();
    }
    
    /** abstract method to layout the edge
     */
    public abstract void layout();  
 
    public static void setHGap(int hGap) { _hGap = hGap; }
    public static void setVGap(int vGap) { _vGap = vGap; }
    
    public static int getHGap() { return _hGap; }
    public static int getVGap() { return _vGap; }
    
}
      
