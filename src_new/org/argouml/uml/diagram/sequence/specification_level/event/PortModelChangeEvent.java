/*
 * PortModelChangeEvent.java
 *
 * Created on 5. Februar 2003, 15:07
 */

package org.argouml.uml.diagram.sequence.specification_level.event;

import org.argouml.application.events.ArgoEvent;
import org.argouml.application.events.ArgoEventTypes;

import org.argouml.uml.diagram.sequence.specification_level.PortModel;

/**
 *
 * @author  Administrator
 */
public class PortModelChangeEvent extends ArgoEvent implements ArgoEventTypes {

    public static final int ANY_CHANGE    = 0;  // Should re-render
    public static final int LINE_INSERTED = 1;  // Currently not used
    public static final int LINE_DELETED  = 2;  // Currently not used

    /** Creates a new instance of PortModelChangeEvent */
    public PortModelChangeEvent(PortModel src, int type) {
        super(ANY_EVENT, src);
    }

}
