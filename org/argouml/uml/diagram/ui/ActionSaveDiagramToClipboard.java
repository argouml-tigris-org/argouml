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

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import org.tigris.gef.base.CmdSaveGIF;
import org.tigris.gef.base.Editor;
import org.tigris.gef.base.Globals;

/**
 * This class copies a diagram to the system clipboard, this functionality
 * will only work with Java1.4, but it will compile with 1.3. It can be put into
 * GEF as it is rather generic.
 *
 * @see <a href="http://java.sun.com/docs/books/tutorial/uiswing/misc/dnd.html">
 * Swing Drag and Drop
 * </a>
 * @author  alexb
 * @since argoUML version 0.15.2, Created on 19 October 2003, 08:36
 */
public class ActionSaveDiagramToClipboard
    extends AbstractAction
    implements ClipboardOwner {
    
    /** get diagram image and put in system clipboard. */
    public void actionPerformed(ActionEvent actionEvent) {
        
        Image diagramGifImage = getImage();
        
        if (diagramGifImage == null) {
            return;
        }
        
        // copy the gif image to the clipboard
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(new ImageSelection(diagramGifImage), this);
    }
    
    /** get image from gef */
    private Image getImage() {
        
        Editor ce = Globals.curEditor();
        Rectangle drawingArea =
	    ce.getLayerManager().getActiveLayer().calcDrawingArea();
        
        // avoid GEF calcDrawingArea bug when nothing in a diagram.
        if (drawingArea.x < 0
	    || drawingArea.y < 0
	    || drawingArea.width < 0
	    || drawingArea.height < 0) {
            return null;
        }
        
        boolean isGridHidden = ce.getGridHidden();
        ce.setGridHidden( true ); // hide grid, otherwise can't see anything
        Image diagramGifImage =
	    ce.createImage(drawingArea.width, drawingArea.height);
        Graphics g = diagramGifImage.getGraphics();
        
	// background color.
        g.setColor( new Color(CmdSaveGIF.TRANSPARENT_BG_COLOR) );
        g.fillRect( 0, 0, drawingArea.width, drawingArea.height );
        g.translate( -drawingArea.x, -drawingArea.y );
        ce.print( g );
        ce.setGridHidden( isGridHidden);
        
        return diagramGifImage;
    }
    
    /** do nothing */
    public void lostOwnership(Clipboard clipboard, Transferable transferable) {
    }
}

/**
 * Encapsulates an awt Image for Data Transfer to/from the clipboard.
 */
class ImageSelection implements Transferable {
    
    public static DataFlavor imageFlavor;
    private DataFlavor [] supportedFlavors = {imageFlavor};
    
    // the diagram image data
    private Image diagramImage;
    
    public ImageSelection(Image newDiagramImage) {
        
        diagramImage = newDiagramImage;
        // hack in order to be able to compile in java1.3
        imageFlavor =
	    new DataFlavor("image/x-java-image; class=java.awt.Image",
			   "Image");
    }
    
    public synchronized DataFlavor [] getTransferDataFlavors() {
        
        return (supportedFlavors);
    }
    
    public boolean isDataFlavorSupported(DataFlavor parFlavor) {
        
        // hack in order to be able to compile in java1.3
        return (parFlavor.getMimeType().
                    equals(imageFlavor.getMimeType())
		&& parFlavor.getHumanPresentableName()
                       .equals(imageFlavor.getHumanPresentableName()));
        
    }
    
    public synchronized Object getTransferData(DataFlavor parFlavor)
	throws UnsupportedFlavorException {
        
        if (isDataFlavorSupported(parFlavor)) {
            return (diagramImage);
        }
        else {
            throw new UnsupportedFlavorException(imageFlavor);
        }
        
    }
}
