/*
 * Created on Nov 29, 2003
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package org.argouml.uml.diagram.sequence;

import org.argouml.uml.diagram.sequence.ui.FigLinkPort;


public class ObjectNode extends ActivationNode implements LinkPort {
    private FigLinkPort _figLinkPort;
    private Object _owner;

    public ObjectNode(Object owner,FigLinkPort figLinkPort) {
        super();
        this._figLinkPort = figLinkPort;
        if (figLinkPort != null)
        	figLinkPort.setOwner(this);     
        setStart(true);       
    }

    public ObjectNode(Object owner) {
        this(owner, null);
    }

    /** 
     * @see org.argouml.uml.diagram.sequence.ui.FigObject.LinkPort#getFigLinkPort()
     */
    public FigLinkPort getFigLinkPort() {
        return _figLinkPort;
    }

    /** 
     * @see org.argouml.uml.diagram.sequence.ui.FigObject.LinkPort#setFigLinkPort(org.argouml.uml.diagram.sequence.ui.FigLinkPort)
     */
    public void setFigLinkPort(FigLinkPort figLinkPort) {
        _figLinkPort = figLinkPort;
    }   
    
    public Object getObject() {
    	return _owner;
    }

}