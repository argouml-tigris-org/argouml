// $Id$
// Copyright (c) 1996-99 The Regents of the University of California. All
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

// File: UMLSequenceDiagram.java
// Classes: UMLSequenceDiagram
// Original Author: 5eichler@informatik.uni-hamburg.de
// $Id$

package org.argouml.uml.diagram.sequence.ui;

import java.beans.PropertyVetoException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.ModelFacade;
import org.argouml.model.uml.UmlFactory;
import org.argouml.model.uml.behavioralelements.collaborations.CollaborationsFactory;
import org.argouml.uml.diagram.sequence.SequenceDiagramGraphModel;
import org.argouml.uml.diagram.ui.UMLDiagram;
import org.tigris.gef.base.LayerPerspective;
import org.tigris.gef.base.LayerPerspectiveMutable;

import ru.novosoft.uml.foundation.core.MNamespace;

/**
 * The diagram for sequence diagrams. 
 * Totally rewritten for release 0.16
 * @author jaap.branderhorst@xs4all.nl
 * Aug 3, 2003
 */
public class UMLSequenceDiagram extends UMLDiagram {
    protected static int _SequenceDiagramSerial = 1;
    
    private Logger _log = Logger.getLogger(this.getClass());
    
    /**
     * The interaction that's shown on this sequence diagram.
     * By default, the interaction is owned by a dummy collaboration that
     * can be retrieved by calling getCollaboration. See 
     * the constructor.
     */
    private Object _interaction;
    
    /**
     * Flag to indicate if this sequence diagram was derived from some collaboration
     * or not.
     */
    private boolean _isDerivedFromCollaboration = false;

    /**
     * Constructs a new sequence diagram with a default name and NO namespace.
     * namespaces are used to determine the 'owner' of the diagram for diagrams
     * but that's plain misuse.
     */
    public UMLSequenceDiagram() {
        try {
            setName(getNewDiagramName());
        } catch (PropertyVetoException pve) {
        }
        SequenceDiagramGraphModel gm = new SequenceDiagramGraphModel();
        setGraphModel(gm);
        LayerPerspective lay = new LayerPerspectiveMutable(this.getName(), gm);
        setLayer(lay);
        SequenceDiagramRenderer rend = new SequenceDiagramRenderer();
        lay.setGraphEdgeRenderer(rend);
        lay.setGraphNodeRenderer(rend);        
        Object rootModel = ProjectManager.getManager().getCurrentProject().getRoot();
        Object collaboration = CollaborationsFactory.getFactory().buildCollaboration(rootModel);
        _interaction = CollaborationsFactory.getFactory().buildInteraction(collaboration);
    }
    
    /**
     * Returns the owner of this diagram. In the case of sequencediagrams it's 
     * allways the root model. 
     *
     * @see org.argouml.uml.diagram.ui.UMLDiagram#getOwner()
     */
    public Object getOwner() {
        // TODO in the future (when there are multiple models) this should be changeable
        return ProjectManager.getManager().getCurrentProject().getRoot();
    }

    /**
     * Creates a new diagramname.
     * @return String
     */
    protected static String getNewDiagramName() {
        String name = null;
        name = "Sequence Diagram " + _SequenceDiagramSerial;
        _SequenceDiagramSerial++;
        if (!ProjectManager.getManager().getCurrentProject()
                .isValidDiagramName(name)) {
            name = getNewDiagramName();
        }
        return name;
    }
    
    
    /**
     * <p>Must return an array of actions via which the model can be manipulated. To
     * use the 'nested actions' feature (like the different association types on
     * UMLClassDiagram) these nested actions must be in an array of their own.</p>
     * <p>In case of the sequence diagram this method must return the following 
     * actions</p>
     * <ul>
     * <li>Action to create an object
     * <li>Action to add a procedural link
     * <li>Action to add a create link
     * <li>Action to add a asynchronous link
     * <li>Action to add a synchronous link
     * <li>Action to add a return link
     * </ul>
     * @see org.argouml.uml.diagram.ui.UMLDiagram#getUmlActions()
     */
    protected Object[] getUmlActions() {
       return new Object[0];
    }
    
    

    /**
     * UMLSequencediagram does not have a namespace. This method throws therefore
     * an UnsupportedOperationException
     * @see org.argouml.uml.diagram.ui.UMLDiagram#getNamespace()
     */
    public MNamespace getNamespace() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Sequence diagram does not have a namespace");
    }

    /**
     * UMLSequencediagram does not have a namespace. This method throws therefore
     * an UnsupportedOperationException
     * @see org.argouml.uml.diagram.ui.UMLDiagram#setNamespace(java.lang.Object)
     */
    public void setNamespace(Object ns) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Sequence diagram does not have a namespace");
    }
    
    /**
     * Method called by Project.removeDiagram to cleanUp the mess in this diagram 
     * when the diagram is removed.
     */
    public void cleanUp() {        
       Iterator it = getObjects().iterator();
       while (it.hasNext()) {
           UmlFactory.getFactory().delete(it.next()); 
       }
       if (!_isDerivedFromCollaboration) {
           UmlFactory.getFactory().delete(getCollaboration());
       }
    }
    
    /**
     * Utility method to retreive the collaboration. 
     * @return the collaboration that owns the interaction shown on this diagram
     */
    public Object getCollaboration() {
        return ModelFacade.getContext(_interaction);
    }
    
    /**
     * Utility method to retrieve all objects (the modelelement Object, not all figs)
     * on this sequence diagram.
     * @return
     */
    public Collection getObjects() {
        Iterator it = getGraphModel().getNodes().iterator();
        List objects = new ArrayList();
        while (it.hasNext()) {
            Object o = it.next();
            if (ModelFacade.isAObject(o)) {
                objects.add(o);
            }
        }
        return objects;
    }
    

} /* end class UMLSequenceDiagram */
