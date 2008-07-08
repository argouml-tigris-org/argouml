// $Id: eclipse-argo-codetemplates.xml 11347 2006-10-26 22:37:44Z linus $
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

package org.argouml.application.events;

import org.argouml.uml.diagram.ArgoDiagram;

/**
 * The Project Event is used to notify interested parties when diagrams have
 * been added to a Project.
 * 
 * @author Brian Hudson
 */
public class ArgoProjectEvent extends ArgoEvent {
    private ArgoDiagram diagram;

    /**
     * Creates a new ArgoProjectEvent
     * 
     * @param et Event type
     * @param src Event source
     * @param diagram The diagram that was added/removed
     */
    public ArgoProjectEvent(int et, Object src, ArgoDiagram diagram) {
        super(et, src);

        this.diagram = diagram;
    }

    @Override
    public int getEventStartRange() {
        return ArgoEventTypes.ANY_PROJECT_EVENT;
    }

    /**
     * Returns the diagram associated with this event
     * 
     * @return The diagram associated with this event
     */
    public ArgoDiagram getDiagram() {
        return diagram;
    }
}
