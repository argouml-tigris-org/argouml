// $Id$
// Copyright (c) 2003-2004 The Regents of the University of California. All
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

package org.argouml.uml.diagram.sequence;

import org.argouml.uml.diagram.sequence.ui.FigLinkPort;


/**
 * An Object Node for the sequence diagram.
 *
 */
public class ObjectNode extends ActivationNode implements LinkPort {
    private FigLinkPort figLinkPort;
    private Object ownerObject;

    /**
     * The constructor.
     * 
     * @param owner the owner object
     * @param flp the figlinkport
     */
    public ObjectNode(Object owner, FigLinkPort flp) {
        super();
        this.figLinkPort = flp;
        if (flp != null)
        	flp.setOwner(this);     
        setStart(true);       
    }

    /**
     * The constructor.
     * 
     * @param owner the owner object
     */
    public ObjectNode(Object owner) {
        this(owner, null);
    }

    /** 
     * @see LinkPort#getFigLinkPort()
     */
    public FigLinkPort getFigLinkPort() {
        return figLinkPort;
    }

    /** 
     * @see LinkPort#setFigLinkPort(FigLinkPort)
     */
    public void setFigLinkPort(FigLinkPort flp) {
        figLinkPort = flp;
    }   
    
    /**
     * @see org.argouml.uml.diagram.sequence.LinkPort#getObject()
     */
    public Object getObject() {
    	return ownerObject;
    }
    
    /**
     * @param object the owner object
     */
    public void setObject(Object object) {
    	ownerObject = object;
    }

}
