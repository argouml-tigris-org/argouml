/*
 * Created on Nov 29, 2003
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package org.argouml.uml.diagram.sequence;

import org.argouml.uml.diagram.sequence.ui.FigLinkPort;

public class LinkNode extends ActivationNode implements LinkPort {
    private FigLinkPort _figLinkPort;
    private Object _owner;
    private boolean _destroyed;

    public LinkNode(Object owner) {
        this(owner,null);
    }

    public LinkNode(Object owner, FigLinkPort figLinkPort) {
        super();
        _owner = owner;        
        setFigLinkPort(figLinkPort);
    }

    /**
     * @return _figLinkPort
     */
    public FigLinkPort getFigLinkPort() {
        return _figLinkPort;
    }

    /**
     * @param figLinkPort
     */
    public void setFigLinkPort(FigLinkPort figLinkPort) {
        _figLinkPort = figLinkPort;
		if (figLinkPort != null)        
			figLinkPort.setOwner(this);
    }

    public Object getObject() {
        return _owner;
    }

    /**
     * @return _destroyed
     */
    public boolean isDestroyed() {
        return _destroyed;
    }

    /**
     * @param b
     */
    public void setDestroyed(boolean b) {
        _destroyed = b;
    }

}