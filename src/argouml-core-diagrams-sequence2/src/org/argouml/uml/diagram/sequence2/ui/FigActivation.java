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

package org.argouml.uml.diagram.sequence2.ui;

import org.tigris.gef.presentation.FigGroup;
import org.tigris.gef.presentation.FigRect;

/**
 *
 * @author penyaskito
 */
class FigActivation extends FigGroup {

    
    private FigRect rectFig;
    private FigDestroy destroyFig;
    
    static final int DEFAULT_HEIGHT = 40;
    static final int DEFAULT_WIDTH = 20;
    
    /**
     * @param x
     * @param y
     * @param destroy 
     */
    public FigActivation(int x, int y, boolean destroy) {
        rectFig = new FigRect(x - DEFAULT_WIDTH / 2, y,
                DEFAULT_WIDTH, DEFAULT_HEIGHT);
        addFig(rectFig);
        setDestroy(destroy);
    }
    
    /**     
     * @param isDestroy 
     */
    public void setDestroy (boolean isDestroy) {
        if (isDestroy) {
            if (destroyFig == null) {
                destroyFig = 
                    new FigDestroy(getX() + DEFAULT_WIDTH / 2,
                            getY() + getHeight());
                addFig(destroyFig);                
            }
        }
        else {
            if (destroyFig != null) {
                removeFig(destroyFig);
            }
            destroyFig = null;
        }
    }
    
    /**
     * Checks if ends with a destroy message.
     * @return true if ends with a destroy message, false otherwise
     */
    public boolean isDestroy () {
        return destroyFig != null;
    }

}
