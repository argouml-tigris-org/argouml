/* $Id$
 *****************************************************************************
 * Copyright (c) 2009 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    mvw
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

// Copyright (c) 2007-2009 The Regents of the University of California. All
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

import java.awt.Color;
import java.awt.Dimension;

import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.uml.diagram.UMLMutableGraphSupport;
import org.tigris.gef.base.Editor;
import org.tigris.gef.base.Globals;
import org.tigris.gef.base.Layer;
import org.tigris.gef.base.LayerPerspective;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigCircle;
import org.tigris.gef.presentation.FigEdge;
import org.tigris.gef.presentation.FigRect;

/**
 * Static utility methods for use with ArgoFigs.
 * 
 * @author Tom Morris <tfmorris@gmail.com>
 */
public class ArgoFigUtil {
    
    /**
     * Find the Project that contains a given figure.  Because we don't have
     * a single reliable way to do this, we try a bunch of different approaches.
     * 
     * @param fig the Fig to return the project of
     * @return the project containing the given fig
     */
    public static Project getProject(ArgoFig fig) {
        if (fig instanceof Fig) {
            Fig f = (Fig) fig;
            LayerPerspective layer = (LayerPerspective) f.getLayer();
            if (layer == null) {
                /* TODO: Without this, we fail to draw e.g. a Class.
                 * But is this a good solution? 
                 * Why is the Layer not set in the constructor? */
                Editor editor = Globals.curEditor();
                if (editor == null) {
                    // TODO: The above doesn't work reliably in a constructor.
                    // We need a better way of getting default fig settings 
                    // for the owning project rather than using the 
                    // project manager singleton. - tfm
                    return ProjectManager.getManager().getCurrentProject();
                }
                Layer lay = editor.getLayerManager().getActiveLayer();
                if (lay instanceof LayerPerspective) {
                    layer = (LayerPerspective) lay;
                }
            }
            if (layer == null) {
                return ProjectManager.getManager().getCurrentProject();
            }
            GraphModel gm = layer.getGraphModel();
            if (gm instanceof UMLMutableGraphSupport) {
                Project project = ((UMLMutableGraphSupport) gm).getProject();
                if (project != null) {
                    return project;
                }
            }
            return ProjectManager.getManager().getCurrentProject();
        }
        return null;
    }


    /**
     * Add pretty little markers for debugging purposes. We use three markers so
     * you can see the anchor, the computed target position, and how collision
     * detection affects a largish box.
     */
    static void markPosition(FigEdge fe, 
            int pct, int delta, int angle, int offset,
            Color color) {
        // set this to true on to enable debugging figs
        if (false) {
            Fig f;
            f = new FigCircle(0, 0, 5, 5, color, Color.red);
            // anchor position
            fe.addPathItem(f, new PathItemPlacement(fe, f, pct, delta, angle, 
                    0));
            f = new FigRect(0, 0, 100, 20, color, Color.red);
            f.setFilled(false);
            fe.addPathItem(f, new PathItemPlacement(fe, f, pct, delta, angle,
                    offset));
            f = new FigCircle(0, 0, 5, 5, color, Color.blue);
            fe.addPathItem(f, new PathItemPlacement(fe, f, pct, delta, angle,
                    offset));
        }
    }

    /**
     * This utility adds the size of a child component to an overall size. The
     * width is maximized with child's width and the child's height is added to
     * the overall height. If the child figure is not visible or not yet
     * created, it's size is not added.
     * 
     * @param size current dimensions - modified with the result
     * @param child child figure
     * @return new Dimension with child size added
     */
    public static Dimension addChildDimensions(Dimension size, Fig child) {
        if (child != null && child.isVisible()) {
            Dimension childSize = child.getMinimumSize();
            size.width = Math.max(size.width, childSize.width);
            size.height += childSize.height;
        }
        return size;
    }


    /**
     * This utility adds the width of a child component to an overall size. The
     * width is maximized with child's width and the child's height is ignored.
     * If the child figure is not visible, it's size is not added.
     * 
     * @param size current dimensions - modified with the result
     * @param child child figure
     * @return new Dimension with child width added
     */
    public static Dimension addChildWidth(Dimension size, Fig child) {
        if (child.isVisible()) {
            Dimension childSize = child.getMinimumSize();
            size.width = Math.max(size.width, childSize.width);
        }
        return size;
    }
}
