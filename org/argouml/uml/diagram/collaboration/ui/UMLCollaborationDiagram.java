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

// File: UMLCollaborationDiagram.java
// Classes: UMLCollaborationDiagram
// Original Author: agauthie@ics.uci.edu
// $Id$

package org.argouml.uml.diagram.collaboration.ui;

import java.beans.PropertyVetoException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.Action;
import javax.swing.JToolBar;

import org.apache.log4j.Category;
import org.argouml.kernel.ProjectManager;
import org.argouml.ui.CmdCreateNode;
import org.argouml.swingext.PopupToolBoxButton;
import org.argouml.uml.diagram.collaboration.CollabDiagramGraphModel;
import org.argouml.uml.diagram.ui.FigMessage;
import org.argouml.uml.diagram.ui.UMLDiagram;
import org.argouml.uml.ui.ActionAddMessage;
import org.argouml.uml.diagram.ui.ActionAddAssociationRole;
import org.tigris.gef.base.CmdSetMode;
import org.tigris.gef.base.Layer;
import org.tigris.gef.base.LayerPerspective;
import org.tigris.gef.base.LayerPerspectiveMutable;
import org.tigris.gef.base.ModeCreatePolyEdge;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.ui.ToolBar;
import ru.novosoft.uml.behavior.collaborations.MAssociationRole;
import ru.novosoft.uml.behavior.collaborations.MClassifierRole;
import ru.novosoft.uml.behavior.collaborations.MMessage;
import ru.novosoft.uml.foundation.core.MGeneralization;
import ru.novosoft.uml.foundation.core.MModelElement;
import ru.novosoft.uml.foundation.core.MNamespace;
import ru.novosoft.uml.foundation.data_types.MAggregationKind;

public class UMLCollaborationDiagram extends UMLDiagram {

    /** for logging */
    private final static Category cat = Category.getInstance("org.argouml.uml.diagram.collaboration.ui.UMLCollaborationDiagram");

    ////////////////
    // actions for toolbar

    protected static Action _actionClassifierRole = new CmdCreateNode(MClassifierRole.class, "ClassifierRole");

    protected static Action _actionAssoc = new CmdSetMode(ModeCreatePolyEdge.class, "edgeClass", MAssociationRole.class, "AssociationRole");

    protected static Action _actionGeneralize = new CmdSetMode(ModeCreatePolyEdge.class, "edgeClass", MGeneralization.class, "Generalization");

    protected static Action _actionAssociation = new ActionAddAssociationRole(MAggregationKind.NONE, false, "Association");
    protected static Action _actionAggregation = new ActionAddAssociationRole(MAggregationKind.AGGREGATE, false, "Aggregation");
    protected static Action _actionComposition = new ActionAddAssociationRole(MAggregationKind.COMPOSITE, false, "Composition");
    protected static Action _actionUniAssociation = new ActionAddAssociationRole(MAggregationKind.NONE, true, "UniAssociation");
    protected static Action _actionUniAggregation = new ActionAddAssociationRole(MAggregationKind.AGGREGATE, true, "UniAggregation");
    protected static Action _actionUniComposition = new ActionAddAssociationRole(MAggregationKind.COMPOSITE, true, "UniComposition");

    ////////////////////////////////////////////////////////////////
    // contructors
    protected static int _CollaborationDiagramSerial = 1;

    public UMLCollaborationDiagram() {

        try {
            setName(getNewDiagramName());
        } catch (PropertyVetoException pve) {
        }
    }

    public UMLCollaborationDiagram(MNamespace m) {
        this();
        setNamespace(m);
    }

    public int getNumMessages() {
        Layer lay = getLayer();
        Vector figs = lay.getContents();
        int res = 0;
        int size = figs.size();
        for (int i = 0; i < size; i++) {
            Fig f = (Fig) figs.elementAt(i);
            if (f.getOwner() instanceof MMessage)
                res++;
        }
        return res;
    }

    /** method to perform a number of important initializations of a <I>CollaborationDiagram</I>. 
     * 
     * each diagram type has a similar <I>UMLxxxDiagram</I> class.
     *
     * @param m  MNamespace from the model in NSUML...
     * @modified changed <I>lay</I> from <I>LayerPerspective</I> to <I>LayerPerspectiveMutable</I>. 
     *           This class is a child of <I>LayerPerspective</I> and was implemented 
     *           to correct some difficulties in changing the model. <I>lay</I> is used 
     *           mainly in <I>LayerManager</I>(GEF) to control the adding, changing and 
     *           deleting layers on the diagram...
     *           psager@tigris.org   Jan. 24, 2oo2
     */
    public void setNamespace(MNamespace m) {
        super.setNamespace(m);
        CollabDiagramGraphModel gm = new CollabDiagramGraphModel();
        gm.setNamespace(m);
        setGraphModel(gm);
        LayerPerspective lay = new LayerPerspectiveMutable(m.getName(), gm);
        setLayer(lay);
        CollabDiagramRenderer rend = new CollabDiagramRenderer(); // singleton
        lay.setGraphNodeRenderer(rend);
        lay.setGraphEdgeRenderer(rend);
    }

    /**
     * <p>Initialize the toolbar with buttons required for a use case diagram.</p>
     * @param toolBar The toolbar to which to add the buttons.
     */
    protected void initToolBar(JToolBar toolBar) {
        toolBar.add(_actionClassifierRole);
        toolBar.addSeparator();
        toolBar.add(buildAssociationPopup());
        //toolBar.add(_actionAssoc);
        toolBar.add(ActionAddMessage.SINGLETON);
        toolBar.add(_actionGeneralize);
    }
    
    private PopupToolBoxButton buildAssociationPopup() {
        PopupToolBoxButton toolBox = new PopupToolBoxButton(_actionAssociation, 0, 2);
        toolBox.add(_actionAssociation);
        toolBox.add(_actionUniAssociation);
        toolBox.add(_actionAggregation);
        toolBox.add(_actionUniAggregation);
        toolBox.add(_actionComposition);
        toolBox.add(_actionUniComposition);
        return toolBox;
    }
    


    /**  After loading the diagram it?s necessary to connect
      *  every FigMessage to its FigAssociationRole. 
      *  This is done by adding the FigMessage 
      *  to the PathItems of its FigAssociationRole */
    public void postLoad() {

        super.postLoad();

        Collection messages;
        Iterator msgIterator;
        if (getNamespace() == null) {
            cat.error("Collaboration Diagram does not belong to a namespace");
            return;
        }
        Collection ownedElements = getNamespace().getOwnedElements();
        Iterator oeIterator = ownedElements.iterator();
        Layer lay = getLayer();
        while (oeIterator.hasNext()) {
            MModelElement me = (MModelElement) oeIterator.next();
            if (me instanceof MAssociationRole) {
                messages = ((MAssociationRole) me).getMessages();
                msgIterator = messages.iterator();
                while (msgIterator.hasNext()) {
                    MMessage message = (MMessage) msgIterator.next();
                    FigMessage figMessage = (FigMessage) lay.presentationFor(message);
                    if (figMessage != null) {
                        figMessage.addPathItemToFigAssociationRole(lay);
                    }
                }
            }
        }
    }

    /**
       * Creates a new diagramname.
       * @return String
       */
    protected static String getNewDiagramName() {
        String name = null;
        name = "Collaboration Diagram " + _CollaborationDiagramSerial;
        _CollaborationDiagramSerial++;
        if (!ProjectManager.getManager().getCurrentProject().isValidDiagramName(name)) {
            name = getNewDiagramName();
        }
        return name;
    }
} /* end class UMLCollaborationDiagram */
