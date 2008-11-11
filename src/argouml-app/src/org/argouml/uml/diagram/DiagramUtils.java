// $Id$
// Copyright (c) 2008 The Regents of the University of California. All
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

package org.argouml.uml.diagram;

import org.apache.log4j.Logger;
import org.tigris.gef.base.Editor;
import org.tigris.gef.base.Globals;
import org.tigris.gef.base.Layer;
import org.tigris.gef.base.LayerManager;
import org.tigris.gef.base.LayerPerspective;

/**
 * A utility class for diagrams (ArgoDiagrams).  It provides convenience 
 * methods which encapsulate and isolate access to GEF.
 * 
 * @author Tom Morris <tfmorris@gmail.com>
 */
public class DiagramUtils {

    private static final Logger LOG = Logger.getLogger(DiagramUtils.class);
    
    /**
     * @return the diagram containing the mouse (or which most recently
     *         contained the mouse).
     */
    public static ArgoDiagram getActiveDiagram() {
        LayerPerspective layer = getActiveLayer();
        if (layer != null) {
            return (ArgoDiagram) layer.getDiagram();
        }
        LOG.debug("No active diagram");
        return null;
    }
    
    /**
     * @return the active diagram layer. If the currently active layer is not a
     *         LayerPerspective, it can't be associated with a Diagram, so
     *         return null.
     */
    private static LayerPerspective getActiveLayer() {
        Editor editor = Globals.curEditor();
        if (editor != null) {
            LayerManager manager = editor.getLayerManager();
            if (manager != null) {
                Layer layer = manager.getActiveLayer();
                if (layer instanceof LayerPerspective) {
                    return (LayerPerspective) layer;
                }
            }
        }
        return null;
    }
}
