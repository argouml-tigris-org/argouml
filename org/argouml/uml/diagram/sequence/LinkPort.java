/*
 * Created on Nov 29, 2003
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package org.argouml.uml.diagram.sequence;

import org.argouml.uml.diagram.sequence.ui.FigLinkPort;


public interface LinkPort {
    public FigLinkPort getFigLinkPort();
    public void setFigLinkPort(FigLinkPort figLinkPort);
    public Object getObject();
}