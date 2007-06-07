// $Id: eclipse-argo-codetemplates.xml 11347 2006-10-26 22:37:44Z linus $
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

package org.argouml.uml.diagram.ui;

import java.awt.Image;

import org.tigris.gef.presentation.FigImage;
import org.tigris.gef.presentation.FigNode;
import org.tigris.gef.presentation.FigText;

public class FigProfileIcon extends FigNode {

    private FigImage image = null;
    
    private FigText  label = null;

    private static final int GAP = 2;
    public FigProfileIcon(Image icon, String str) {
	image = new FigImage(0, 0, icon);
	label = new FigSingleLineText(0, image.getHeight()+GAP, 0, 0, true);	
	label.setText(str);
	label.calcBounds();
	
	addFig(image);
	addFig(label);

	image.setResizable(false);
	image.setLocked(true);
    }
        
    protected void setBoundsImpl(int x, int y, int w, int h) {	
	int width = Math.max(image.getWidth(), label.getWidth());
	
	image.setLocation(x + (width - image.getWidth())/2, y);
	label.setLocation(x + (width - label.getWidth())/2, y + image.getHeight()+GAP);
	
        calcBounds();
        updateEdges();
    }

    public FigText getLabelFig() {
        return label;
    }
    
    public String getLabel() {
        return label.getText();
    }

    public void setLabel(String label) {
        this.label.setText(label);
	this.label.calcBounds();
	this.calcBounds();
    }
    
    
}
