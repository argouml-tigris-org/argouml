// $Id$
// Copyright (c) 1996-2004 The Regents of the University of California. All
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
 * A specialist for displaying the model element name and stereotype.
 * @author Bob Tarling
 */
public class FigNameCompartment extends FigCompartment {
    
    private FigStereotype stereotype;
    private FigName name;
    
    /**
     * The constructor.
     * 
     * @param x horizontal distance to the left
     * @param y vertical distance from the top 
     * @param w width
     * @param h heigth
     * @param expandOnly true if the fig can grow if the text changes, 
     *                   but not shrink
     */
    public FigNameCompartment(int x, int y, int w, int h, boolean expandOnly) {
        stereotype = new FigStereotype(x, y, w, h / 2, expandOnly);
        name = new FigName(x, y + h / 2, w, h / 2, expandOnly);
        addFig(stereotype);
        addFig(name);
    }
    
    /**
     * @param s the stereotype name string
     */
    public void setStereotype(String s) {
        this.stereotype.setText(s);
    }
    
    /**
     * @return the stereotype name string
     */
    public String getStereotype() {
        return stereotype.getText();
    }
    
    /**
     * @param n the name string
     */
    public void setName(String n) {
        this.name.setText(n);
    }
    
    /**
     * @return the name string
     */
    public String getName() {
        return name.getText();
    }
    
    /**
     * @see java.lang.Object#clone()
     */
    public Object clone() {
        FigNameCompartment clone = (FigNameCompartment) super.clone();
        clone.stereotype = (FigStereotype) clone.getFigAt(0);
        clone.name = (FigName) clone.getFigAt(1);
        return clone;
    }
}
