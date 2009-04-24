// $Id$
// Copyright (c) 2009 Tom Morris and other contributors.
// New BSD License

package org.argouml.uml.diagram;

/**
 * Interface to be implemented by UML Sequence Diagram implementations
 * 
 * @author Tom Morris
 */
public interface SequenceDiagram extends ArgoDiagram {

    /**
     * @return the collaboration from the associated graph model
     */
    public Object getCollaboration();
}
