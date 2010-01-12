/* $Id$
 *****************************************************************************
 * Copyright (c) 2009 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    tfmorris
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

// Copyright (c) 1996-2009 The Regents of the University of California. All
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

package org.argouml.uml.diagram;

import java.util.EnumMap;
import java.util.Map;

import org.argouml.kernel.ProjectManager;
import org.argouml.model.ActivityDiagram;
import org.argouml.model.ClassDiagram;
import org.argouml.model.CollaborationDiagram;
import org.argouml.model.DeploymentDiagram;
import org.argouml.model.DiDiagram;
import org.argouml.model.Model;
import org.argouml.model.StateDiagram;
import org.argouml.model.UseCaseDiagram;
import org.argouml.uml.diagram.activity.ui.UMLActivityDiagram;
import org.argouml.uml.diagram.collaboration.ui.UMLCollaborationDiagram;
import org.argouml.uml.diagram.deployment.ui.UMLDeploymentDiagram;
import org.argouml.uml.diagram.state.ui.UMLStateDiagram;
import org.argouml.uml.diagram.static_structure.ui.UMLClassDiagram;
import org.argouml.uml.diagram.use_case.ui.UMLUseCaseDiagram;

/**
* Provide a factory method to create different UML diagrams.
* 
* @author Bob Tarling
*/
public final class DiagramFactory {

    /**
     * Map from our public enum to our internal implementation classes.
     * This allows use to hide the implementation classes from users of
     * the factory.
     * NOTE: This needs to be initialized before the constructor is called
     * to initialize the singleton.
     */
    private static Map<DiagramType, Class> diagramClasses = 
        new EnumMap<DiagramType, Class>(DiagramType.class);
    
    /**
     * The singleton instance.
     */
    private static DiagramFactory diagramFactory = new DiagramFactory();

    /**
     * Enumeration containing all the different types of UML diagrams.
     */
    public enum DiagramType {
        Class, UseCase, State, Deployment, Collaboration, Activity, Sequence
    }

    private Map<DiagramType, DiagramFactoryInterface2> factories =
        new EnumMap<DiagramType, DiagramFactoryInterface2>(DiagramType.class);

    private DiagramFactory() {
        super();
        // TODO: Use our extension registration mechanism for our internal
        // classes as well, so everything is treated the same
        diagramClasses.put(DiagramType.Class, UMLClassDiagram.class);
        diagramClasses.put(DiagramType.UseCase, UMLUseCaseDiagram.class);
        diagramClasses.put(DiagramType.State, UMLStateDiagram.class);
        diagramClasses.put(DiagramType.Deployment, UMLDeploymentDiagram.class);
        diagramClasses.put(DiagramType.Collaboration, 
                UMLCollaborationDiagram.class);
        diagramClasses.put(DiagramType.Activity, UMLActivityDiagram.class);
    }

    /**
     * @return the singleton
     */
    public static DiagramFactory getInstance() {
        return diagramFactory;
    }

    
    /**
     * Factory method to create a new default instance of an ArgoDiagram.
     * @param namespace The namespace that (in)directly 
     *                        owns the elements on the diagram
     * @return the newly instantiated class diagram
     */
    public ArgoDiagram createDefaultDiagram(Object namespace) {
        return createDiagram(DiagramType.Class, namespace, null);
    }

    /**
     * Factory method to create a new instance of an ArgoDiagram.
     *
     * @param type The class of rendering diagram to create
     * @param namespace The namespace that (in)directly 
     *                        owns the elements on the diagram
     * @param machine The StateMachine for the diagram
     *                         (only: statemachine - activitygraph)
     * @return the newly instantiated class diagram
     * @deprecated for 0.27.3 by tfmorris.  Use 
     * {@link #create(DiagramType, Object, DiagramSettings)}.  The 'owner'
     * argument should be the 'machine' for a state diagram or activity diagram
     * (which can figure out the correct namespace from that) and the 
     * 'namespace' for all others.
     */
    @Deprecated
    public ArgoDiagram createDiagram(final DiagramType type,
            final Object namespace, final Object machine) {
        
        DiagramSettings settings = ProjectManager.getManager()
                .getCurrentProject().getProjectSettings()
                .getDefaultDiagramSettings();
        
        return createInternal(type, namespace, machine, settings);
    }


    /**
     * Factory method to create a new instance of an ArgoDiagram.
     * 
     * @param type The class of rendering diagram to create
     * @param owner the owning UML element. For most diagrams this is a
     *            namespace, but for the state diagram it is the state machine
     *            and for the activity diagram it is the context.
     * @param settings default rendering settings for the diagram
     * @return the newly instantiated class diagram
     */
    public ArgoDiagram create(
            final DiagramType type,
            final Object owner,
            final DiagramSettings settings) {
        
        return  createInternal(type, owner, null, settings);
    }


    /*
     * Create a diagram.  This 4-arg version is only for internal use.  The
     * 'namespace' argument is deprecated and not used in the new APIs.
     */
    private ArgoDiagram createInternal(final DiagramType type,
            final Object namespace, final Object machine,
            DiagramSettings settings) {
        final ArgoDiagram diagram;

        if (settings == null) {
            throw new IllegalArgumentException(
                    "DiagramSettings may not be null");
        }
        
        Object factory = factories.get(type);
        if (factory != null) {
            Object owner;
            if (machine != null) {
                owner = machine;
            } else {
                owner = namespace;
            }
            if (factory instanceof DiagramFactoryInterface2) {
                diagram = ((DiagramFactoryInterface2) factory).createDiagram(
                        owner, (String) null, settings);
            } else if (factory instanceof DiagramFactoryInterface) {
                diagram = ((DiagramFactoryInterface) factory).createDiagram(
                        namespace, machine);
                diagram.setDiagramSettings(settings);
            } else {
                // This shouldn't be possible, but just in case
                throw new IllegalStateException(
                        "Unknown factory type registered");
            }
        } else {
            if ((type == DiagramType.State || type == DiagramType.Activity)
                    && machine == null) {
                diagram = createDiagram(diagramClasses.get(type), null,
                        namespace);
            } else {
                diagram = createDiagram(diagramClasses.get(type), namespace,
                        machine);
            }
            diagram.setDiagramSettings(settings);
        }

        return diagram;
    }
    
    /**
     * Factory method to create a new instance of an ArgoDiagram.
     *
     * @param type The class of rendering diagram to create
     * @param namespace The namespace that (in)directly 
     *                        owns the elements on the diagram
     * @param machine The StateMachine for the diagram
     *                         (only: statemachine - activitygraph)
     * @return the newly instantiated class diagram
     * @deprecated for 0.25.4 by tfmorris.  Use 
     * {@link #create(DiagramType, Object, DiagramSettings)}.  The 'owner'
     * argument should be the 'machine' for a state diagram or activity diagram
     * (which can figure out the correct namespace from that) and the 
     * 'namespace' for all others.
     */
    @Deprecated
    private ArgoDiagram createDiagram(Class type, Object namespace,
            Object machine) {

        ArgoDiagram diagram = null;
        Class diType = null;

        // TODO: Convert all to use standard factory registration
        if (type == UMLClassDiagram.class) {
            diagram = new UMLClassDiagram(namespace);
            diType = ClassDiagram.class;
        } else if (type == UMLUseCaseDiagram.class) {
            diagram = new UMLUseCaseDiagram(namespace);
            diType = UseCaseDiagram.class;
        } else if (type == UMLStateDiagram.class) {
            diagram = new UMLStateDiagram(namespace, machine);
            diType = StateDiagram.class;
        } else if (type == UMLDeploymentDiagram.class) {
            diagram = new UMLDeploymentDiagram(namespace);
            diType = DeploymentDiagram.class;
        } else if (type == UMLCollaborationDiagram.class) {
            diagram = new UMLCollaborationDiagram(namespace);
            diType = CollaborationDiagram.class;
        } else if (type == UMLActivityDiagram.class) {
            diagram = new UMLActivityDiagram(namespace, machine);
            diType = ActivityDiagram.class;
        }

        if (diagram == null) {
            throw new IllegalArgumentException ("Unknown diagram type");
        }
        
        if (Model.getDiagramInterchangeModel() != null) {
            // TODO: This is never executed as Ludos DI work was never
            // finished.
            diagram.getGraphModel().addGraphEventListener(
                 GraphChangeAdapter.getInstance());
            /*
             * The diagram are always owned by the model
             * in this first implementation.
             */
            DiDiagram dd = GraphChangeAdapter.getInstance()
                .createDiagram(diType, namespace);
            ((UMLMutableGraphSupport) diagram.getGraphModel()).setDiDiagram(dd);
        }

        return diagram;
    }

    /**
     * Factory method to remove a diagram.
     *
     * @param diagram the diagram
     * @return the diagram that was removed
     */
    public ArgoDiagram removeDiagram(ArgoDiagram diagram) {

        DiDiagram dd =
            ((UMLMutableGraphSupport) diagram.getGraphModel()).getDiDiagram();
        if (dd != null) {
            GraphChangeAdapter.getInstance().removeDiagram(dd);
        }
        return diagram;
    }

    
    /**
     * Register a specific factory class to create diagram instances for a
     * specific diagram type
     * 
     * @param type the diagram type
     * @param factory the factory instance
     */
    public void registerDiagramFactory(
            final DiagramType type,
            final DiagramFactoryInterface2 factory) {
        // TODO: This uses a "last one wins" algorithm for registration
        // We should warn if a factory is being overwritten.
        factories.put(type, factory);
    }
}
