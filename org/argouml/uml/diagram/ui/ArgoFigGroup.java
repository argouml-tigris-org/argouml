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

import java.util.List;

import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.uml.diagram.UMLMutableGraphSupport;
import org.tigris.gef.base.Editor;
import org.tigris.gef.base.Globals;
import org.tigris.gef.base.Layer;
import org.tigris.gef.base.LayerPerspective;
import org.tigris.gef.presentation.FigGroup;

/**
 * A fig which contains other figs.  ArgoUMLs version of GEF's FigGroup.
 * 
 * @author Tom Morris <tfmorris@gmail.com>
 */
public abstract class ArgoFigGroup extends FigGroup implements ArgoFig {

    public ArgoFigGroup() {
        super();
    }

    public ArgoFigGroup(List arg0) {
        super(arg0);
    }

    /*
     * This optional method is not implemented.  It will throw an
     * {@link UnsupportedOperationException} if used.  Figs are 
     * added to a GraphModel which is, in turn, owned by a project.<p>
     * 
     * This method is identical to the one in FigNodeModelElement.
     */
    public void setProject(Project project) {
        throw new UnsupportedOperationException();
    }
    
    /**
     * This method is identical to the one in FigNodeModelElement.
     * 
     * @return the project 
     * @see org.argouml.uml.diagram.ui.ArgoFig#getProject()
     */
    public Project getProject() {
        LayerPerspective layer = (LayerPerspective) getLayer();
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
            return null;
        }
        UMLMutableGraphSupport gm = 
            (UMLMutableGraphSupport) layer.getGraphModel();
        return gm.getProject();
    }
}
