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

import org.apache.log4j.Logger;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.ModelFacade;
import org.argouml.model.uml.UmlFactory;
import org.argouml.model.uml.behavioralelements.collaborations.CollaborationsFactory;
import org.argouml.ui.CmdCreateNode;
import org.argouml.uml.diagram.sequence.SequenceDiagramGraphModel;
import org.argouml.uml.diagram.ui.UMLDiagram;

/**
 * The diagram for sequence diagrams. 
 * Totally rewritten for release 0.16
 * @author jaap.branderhorst@xs4all.nl
 * Aug 3, 2003
 */
public class UMLSequenceDiagram extends UMLDiagram {

    protected static int _SequenceDiagramSerial = 1;

    private Logger _log = Logger.getLogger(this.getClass());

    private Object[] _actions;

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
        this(
            CollaborationsFactory.getFactory().buildCollaboration(
                ProjectManager.getManager().getCurrentProject().getRoot()));
    }

    public UMLSequenceDiagram(Object collaboration) {
        super();
        try {
            setName(getNewDiagramName());
        } catch (PropertyVetoException pve) {}
        // Dirty hack to remove the trash the Diagram constructor leaves
        SequenceDiagramGraphModel gm =
            new SequenceDiagramGraphModel(collaboration);
        SequenceDiagramLayout lay =
            new SequenceDiagramLayout(this.getName(), gm);
        SequenceDiagramRenderer rend = new SequenceDiagramRenderer();
        lay.setGraphEdgeRenderer(rend);
        lay.setGraphNodeRenderer(rend);
        setLayer(lay);
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
        if (!ProjectManager
            .getManager()
            .getCurrentProject()
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
        if (_actions == null) {
            _actions = new Object[1];
            _actions[0] =
                new CmdCreateNode(ModelFacade.OBJECT, false, "Object");
        }
        return _actions;
    }

    /**
     * 
     * @see org.argouml.uml.diagram.ui.UMLDiagram#getNamespace()
     */
    public Object getNamespace() {
        if (getGraphModel() == null
            || !(getGraphModel() instanceof SequenceDiagramGraphModel)) {
            SequenceDiagramGraphModel model =
                new SequenceDiagramGraphModel(
                    CollaborationsFactory.getFactory().buildCollaboration(
                        ProjectManager
                            .getManager()
                            .getCurrentProject()
                            .getRoot()));
            SequenceDiagramLayout lay =
                new SequenceDiagramLayout(this.getName(), model);
            SequenceDiagramRenderer rend = new SequenceDiagramRenderer();
            lay.setGraphEdgeRenderer(rend);
            lay.setGraphNodeRenderer(rend);
            setLayer(lay);
        }
        return ((SequenceDiagramGraphModel)getGraphModel()).getCollaboration();
    }

    /**
     * UMLSequencediagram does not have a namespace. This method throws therefore
     * an UnsupportedOperationException
     * @see org.argouml.uml.diagram.ui.UMLDiagram#setNamespace(java.lang.Object)
     */
    public void setNamespace(Object ns) throws UnsupportedOperationException {
        if (getGraphModel() == null
            || !(getGraphModel() instanceof SequenceDiagramGraphModel)) {
            SequenceDiagramGraphModel model =
                new SequenceDiagramGraphModel(
                    CollaborationsFactory.getFactory().buildCollaboration(
                        ProjectManager
                            .getManager()
                            .getCurrentProject()
                            .getRoot()));
            SequenceDiagramLayout lay =
                new SequenceDiagramLayout(this.getName(), model);
            SequenceDiagramRenderer rend = new SequenceDiagramRenderer();
            lay.setGraphEdgeRenderer(rend);
            lay.setGraphNodeRenderer(rend);
            setLayer(lay);
        }
        ModelFacade.setNamespace(
            ((SequenceDiagramGraphModel)getLayer().getGraphModel())
                .getCollaboration(),
            ns);

    }

    /**
     * Method called by Project.removeDiagram to cleanUp the mess in this diagram 
     * when the diagram is removed.
     */
    public void cleanUp() {
        Object collab =
            ((SequenceDiagramGraphModel)getGraphModel()).getCollaboration();
        UmlFactory.getFactory().delete(collab);
    }

} /* end class UMLSequenceDiagram */
