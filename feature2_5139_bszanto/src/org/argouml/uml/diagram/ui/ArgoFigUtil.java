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

package org.argouml.uml.diagram.ui;

import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.uml.diagram.UMLMutableGraphSupport;
import org.tigris.gef.base.Editor;
import org.tigris.gef.base.Globals;
import org.tigris.gef.base.Layer;
import org.tigris.gef.base.LayerPerspective;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.presentation.Fig;

/**
 * Static utility methods for use with ArgoFigs.
 * 
 * @author Tom Morris <tfmorris@gmail.com>
 */
public class ArgoFigUtil {
    
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
                    // TODO: The above doesn't work reliably in a constructor.  We
                    // need a better way of getting default fig settings 
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
                return ((UMLMutableGraphSupport) gm).getProject();
            } else {
                return ProjectManager.getManager().getCurrentProject();
            }
        }
        return null;
    }


}
