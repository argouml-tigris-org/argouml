// $Id$
// Copyright (c) 1996-2005 The Regents of the University of California. All
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

package org.argouml.uml.diagram.sequence.ui;

import java.util.List;

import org.argouml.uml.diagram.sequence.ActivationNode;
import org.argouml.uml.diagram.sequence.Node;
import org.tigris.gef.presentation.ArrowHeadTriangle;

/**
 * Fig that represents a link on a sequence diagram that has a callaction
 *
 * @author jaap.branderhorst@xs4all.nl
 */
public class FigCallActionLink extends FigLink {

    /**
     * @param owner the owner object
     */
    public FigCallActionLink(Object owner) {
        super(owner);
        setDestArrowHead(new ArrowHeadTriangle());
        setDashed(false);
    }

    /**
     *
     */
    public FigCallActionLink() {
        this(null);
    }

    /**
     * @see org.argouml.uml.diagram.sequence.ui.FigLink#layoutActivations()
     */
    protected void layoutActivations() {
        if (!getSrcFigObject().hasActivations()) {
            getSrcFigObject().makeActivation(
                getSrcFigObject().getObjectNode(),
                (Node) getSrcLinkPort());
            ((ActivationNode) getSrcLinkPort()).setCutOffBottom(false);
            ((ActivationNode) getSrcLinkPort()).setCutOffTop(false);
        } else {
            List activation =
                getSrcFigObject().getActivationNodes((Node) getDestLinkPort());
            if (activation.isEmpty()) {
                List previousActivation =
                    getSrcFigObject().getPreviousActivation(
                        (Node) getSrcLinkPort());
                ((ActivationNode) previousActivation.get(
                        previousActivation.size())).setEnd(false);
                ((ActivationNode) getSrcLinkPort()).setEnd(true);
                ((ActivationNode) getSrcLinkPort()).setCutOffBottom(false);
                ((ActivationNode) getSrcLinkPort()).setCutOffTop(false);
            } else {
            	;
            }
        }
        getDestFigObject().makeActivation(
            (Node) getDestLinkPort(),
            (Node) getDestLinkPort());
        ((ActivationNode) getDestLinkPort()).setCutOffTop(true);
        ((ActivationNode) getDestLinkPort()).setCutOffBottom(false);
    }
}
