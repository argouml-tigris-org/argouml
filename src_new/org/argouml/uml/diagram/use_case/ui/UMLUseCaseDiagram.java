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

// File: UMLUseCaseDiagram.java
// Classes: UMLUseCaseDiagram
// Original Author: your email here
// $Id$

// 3 Apr 2002: Jeremy Bennett (mail@jeremybennett.com). Extended to support the
// Extend and Include relationships. JavaDoc added for clarity. Default
// constructor made private, since it must never be called directly.

// 11 Apr 2002: Jeremy Bennett (mail@jeremybennett.com). Removed dependency
// relationship and added extension point button

// 3 May 2002: Jeremy Bennett (mail@jeremybennett.com). Replaced dependency
// relationship following review.

package org.argouml.uml.diagram.use_case.ui;

import java.beans.PropertyVetoException;

import javax.swing.Action;
import javax.swing.JToolBar;

import org.apache.log4j.Category;
import org.argouml.kernel.ProjectManager;
import org.argouml.ui.CmdCreateNode;
import org.argouml.uml.diagram.ui.UMLDiagram;
import org.argouml.uml.diagram.use_case.UseCaseDiagramGraphModel;
import org.argouml.uml.ui.ActionAddExtensionPoint;
import org.argouml.uml.ui.ActionAddNote;
import org.tigris.gef.base.CmdSetMode;
import org.tigris.gef.base.LayerPerspective;
import org.tigris.gef.base.LayerPerspectiveMutable;
import org.tigris.gef.base.ModeCreatePolyEdge;
import org.tigris.gef.ui.ToolBar;
import ru.novosoft.uml.behavior.use_cases.MActor;
import ru.novosoft.uml.behavior.use_cases.MExtend;
import ru.novosoft.uml.behavior.use_cases.MInclude;
import ru.novosoft.uml.behavior.use_cases.MUseCase;
import ru.novosoft.uml.foundation.core.MAssociation;
import ru.novosoft.uml.foundation.core.MDependency;
import ru.novosoft.uml.foundation.core.MGeneralization;
import ru.novosoft.uml.foundation.core.MNamespace;

/**
 * <p>The base class of the use case diagram.</p>
 *
 * <p>Defines the toolbar, provides for its initialization and provides
 *   constructors for a top level diagram and one within a defined
 *   namespace.</p>
 */

public class UMLUseCaseDiagram extends UMLDiagram {
    protected static Category cat = Category.getInstance(UMLUseCaseDiagram.class);

    // Actions specific to the use case diagram toolbar

    /**
     * <p>Tool to add an actor node.</p>
     */
    protected static Action _actionActor = new CmdCreateNode(MActor.class, "Actor");

    /**
     * <p>Tool to add a use case node.</p>
     */
    protected static Action _actionUseCase = new CmdCreateNode(MUseCase.class, "UseCase");

    /**
     * <p>Tool to create an association between UML artifacts using a
     *   polyedge.</p>
     */
    protected static Action _actionAssoc = new CmdSetMode(ModeCreatePolyEdge.class, "edgeClass", MAssociation.class, "Association");

    /**
     * <p>Tool to create a generalization between UML artifacts using a
     *   polyedge.</p>
     */
    protected static Action _actionGeneralize = new CmdSetMode(ModeCreatePolyEdge.class, "edgeClass", MGeneralization.class, "Generalization");

    /**
     * <p>Tool to create an extend relationship between UML use cases using a
     *   polyedge.</p>
     */
    protected static Action _actionExtend = new CmdSetMode(ModeCreatePolyEdge.class, "edgeClass", MExtend.class, "Extend");

    /**
     * <p>Tool to create an include relationship between UML use cases using a
     *   polyedge.</p>
     */
    protected static Action _actionInclude = new CmdSetMode(ModeCreatePolyEdge.class, "edgeClass", MInclude.class, "Include");

    /**
     * <p>Tool to create a dependency between UML artifacts using a
     *   polyedge.</p>
     */

    protected static Action _actionDependency = new CmdSetMode(ModeCreatePolyEdge.class, "edgeClass", MDependency.class, "Dependency");
    /**
     * <p>A static counter of the use case index (used in constructing a unique
     *   name for each new diagram.</p>
     */

    protected static int _UseCaseDiagramSerial = 1;

    // constructors

    /**
     * <p>Construct a new use case diagram with no defined namespace.</p>
     *
     * <p>Note we must never call this directly, since defining the namespace
     *   is what makes everything work. However GEF will call it directly when
     *   loading a new diagram, so it must remain public.</p>
     *
     * <p>A unique name is constructed by using the serial index {@link
     *   _UseCaseDiagramSerial}. We allow for the possibility that setting this
     *   may fail, in which case no name is set.</p>
     */

    public UMLUseCaseDiagram() {
        try {
            setName(getNewDiagramName());
        } catch (PropertyVetoException pve) {
        }
    }

    /**
     * <p>Construct a new use case diagram with in a defined namespace.</p>
     *
     * <p>Invokes the generic constructor {@link #UMLUseCaseDiagram()}, then
     *   intialises the namespace (which initializes all the graphics).</p>
     *
     * <p>This is the constructor which should always be used.</p>
     *
     * @param m  the desired namespace for this diagram.
     */
    public UMLUseCaseDiagram(MNamespace m) {
        this();
        setNamespace(m);
    }

    public UMLUseCaseDiagram(String name, MNamespace m) {
        this(m);
        try {
            setName(name);
        } catch (PropertyVetoException v) {
        }
    }

    /**
     * <p> perform a number of important initializations of a <em>Use Case
     *   Diagram</em>.</p>
     *
     * <p>Creates a new graph model for the diagram, settings its namespace to
     *   that supplied.</p>
     *
     * <p>Changed <em>lay</em> from <em>LayerPerspective</em> to
     *   <em>LayerPerspectiveMutable</em>. This class is a child of
     *   <em>LayerPerspective</em> and was implemented to correct some
     *   difficulties in changing the model. <em>lay</em> is used mainly in
     *   <em>LayerManager</em>(GEF) to control the adding, changing and
     *   deleting of items in a layer of the diagram.</p>
     *
     * <p>Set a renderer suitable for the use case diagram.</p>
     *
     * <p><em>Note</em>. This is declared as public. Not clear that other
     *   classes should be allowed to invoke this method.</p>
     *
     * @param m  Namespace to be used for this diagram.
     *
     * @author   psager@tigris.org  Jan 24, 2002
     */

    public void setNamespace(MNamespace m) {
        super.setNamespace(m);

        UseCaseDiagramGraphModel gm = new UseCaseDiagramGraphModel();
        gm.setNamespace(m);
        setGraphModel(gm);

        LayerPerspective lay = new LayerPerspectiveMutable(m.getName(), gm);
        setLayer(lay);

        // The renderer should be a singleton

        UseCaseDiagramRenderer rend = new UseCaseDiagramRenderer();

        lay.setGraphNodeRenderer(rend);
        lay.setGraphEdgeRenderer(rend);
    }

    /**
     * <p>Initialize the toolbar with buttons required for a use case diagram.</p>
     * @param toolBar The toolbar to which to add the buttons.
     */
    protected void initToolBar(JToolBar toolBar) {
        // Use case diagram specific nodes

        toolBar.add(_actionActor);
        toolBar.add(_actionUseCase);

        toolBar.addSeparator();

        // Use case diagram specific edges

        toolBar.add(_actionAssoc);
        toolBar.add(_actionDependency);
        toolBar.add(_actionGeneralize);
        toolBar.add(_actionExtend);
        toolBar.add(_actionInclude);
        toolBar.addSeparator();

        // The extension point button

        toolBar.add(ActionAddExtensionPoint.singleton());
        toolBar.addSeparator();
        toolBar.add(ActionAddNote.SINGLETON);
    }

    protected static String getNewDiagramName() {
        String name = null;
        name = "Usecase Diagram " + _UseCaseDiagramSerial;
        _UseCaseDiagramSerial++;
        if (!ProjectManager.getManager().getCurrentProject().isValidDiagramName(name)) {
            name = getNewDiagramName();
        }
        return name;
    }
} /* end class UMLUseCaseDiagram */
