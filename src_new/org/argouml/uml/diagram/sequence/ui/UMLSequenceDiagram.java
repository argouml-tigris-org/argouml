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

import javax.swing.Action;

import org.apache.log4j.Category;
import org.argouml.kernel.ProjectManager;
import org.argouml.ui.CmdCreateNode;
import org.argouml.uml.diagram.sequence.SequenceDiagramGraphModel;
import org.argouml.uml.diagram.ui.UMLDiagram;
import org.tigris.gef.base.LayerPerspective;
import org.tigris.gef.base.LayerPerspectiveMutable;

import ru.novosoft.uml.behavior.common_behavior.MCallAction;
import ru.novosoft.uml.behavior.common_behavior.MCreateAction;
import ru.novosoft.uml.behavior.common_behavior.MDestroyAction;
import ru.novosoft.uml.behavior.common_behavior.MObject;
import ru.novosoft.uml.behavior.common_behavior.MReturnAction;
import ru.novosoft.uml.behavior.common_behavior.MSendAction;
import ru.novosoft.uml.foundation.core.MNamespace;

public class UMLSequenceDiagram extends UMLDiagram {
    protected static Category cat =
        Category.getInstance(UMLSequenceDiagram.class);

    ////////////////
    // actions for toolbar

    protected static Action _actionObject =
        new CmdCreateNode(MObject.class, "Object");

    protected static Action _actionLinkWithStimulusCall =
        new ActionAddLink(MCallAction.class, "StimulusCall");

    protected static Action _actionLinkWithStimulusCreate =
        new ActionAddLink(MCreateAction.class, "StimulusCreate");

    protected static Action _actionLinkWithStimulusDestroy =
        new ActionAddLink(MDestroyAction.class, "StimulusDestroy");

    protected static Action _actionLinkWithStimulusSend =
        new ActionAddLink(MSendAction.class, "StimulusSend");

    protected static Action _actionLinkWithStimulusReturn =
        new ActionAddLink(MReturnAction.class, "StimulusReturn");

    ////////////////////////////////////////////////////////////////
    // contructors
    protected static int _SequenceDiagramSerial = 1;

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

} /* end class UMLSequenceDiagram */
