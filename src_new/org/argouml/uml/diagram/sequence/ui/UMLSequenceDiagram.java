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
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.Action;

import org.apache.log4j.Category;
import org.argouml.kernel.ProjectManager;
import org.argouml.ui.CmdCreateNode;
import org.argouml.uml.diagram.sequence.specification_level.ui.ActionAddMessage;
import org.argouml.uml.diagram.sequence.specification_level.ui.ActionNestedCall;
import org.argouml.uml.diagram.sequence.specification_level.ui.FigSeqMessage;
import org.argouml.uml.diagram.sequence.specification_level.ui.SequenceDiagramLayout;
import org.argouml.uml.diagram.sequence.specification_level.ui.SequenceDiagramRenderer;
import org.argouml.uml.diagram.sequence.specification_level.MessageSupervisor;
import org.argouml.uml.diagram.sequence.specification_level.OldLayoutConverter;
import org.argouml.uml.diagram.sequence.specification_level.OldToSpecConverter;
import org.argouml.uml.diagram.sequence.specification_level.SequenceDiagramGraphModel;
import org.argouml.uml.diagram.ui.UMLDiagram;
import org.argouml.uml.ui.ActionAddNote;

import org.tigris.gef.base.Layer;
import org.tigris.gef.base.LayerPerspective;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigPoly;
import org.tigris.gef.ui.ToolBar;

import ru.novosoft.uml.behavior.collaborations.MMessage;
import ru.novosoft.uml.behavior.collaborations.MClassifierRole;
import ru.novosoft.uml.behavior.common_behavior.MCallAction;
import ru.novosoft.uml.behavior.common_behavior.MCreateAction;
import ru.novosoft.uml.behavior.common_behavior.MDestroyAction;
import ru.novosoft.uml.behavior.common_behavior.MLink;
import ru.novosoft.uml.behavior.common_behavior.MObject;
import ru.novosoft.uml.behavior.common_behavior.MReturnAction;
import ru.novosoft.uml.behavior.common_behavior.MSendAction;
import ru.novosoft.uml.behavior.common_behavior.MStimulus;
import ru.novosoft.uml.foundation.core.MModelElement;
import ru.novosoft.uml.foundation.core.MNamespace;

public class UMLSequenceDiagram extends UMLDiagram {

    protected static Category cat = Category.getInstance(UMLSequenceDiagram.class);

    ////////////////
    // actions for toolbar

    protected static Action _actionObject         = new CmdCreateNode   (MClassifierRole.class, "Object");
    protected Action _actionMessageCall           = new ActionAddMessage(this, MCallAction.class,    "Call");
    protected Action _actionMessageCreate         = new ActionAddMessage(this, MCreateAction.class,  "Create");
    protected Action _actionMessageDestroy        = new ActionAddMessage(this, MDestroyAction.class, "Destroy");
    protected Action _actionMessageSend           = new ActionAddMessage(this, MSendAction.class,    "Send");
    protected Action _actionMessageReturn         = new ActionAddMessage(this, MReturnAction.class,  "Return");
    protected static Action _actionNestedCall     = new ActionNestedCall(MCallAction.class);

    ////////////////////////////////////////////////////////////////
    // contructors
    protected static int _SequenceDiagramSerial = 1;

    public UMLSequenceDiagram() {

        try {
            setName(getNewDiagramName());
        } catch (PropertyVetoException pve) {
        }
    }

    public UMLSequenceDiagram(MNamespace m) {
        this();
        setNamespace(m);
    }

    public int getNumStimuluss() {
        Layer lay = getLayer();
        Vector figs = lay.getContents();
        int res = 0;
        int size = figs.size();
        for (int i = 0; i < size; i++) {
            Fig f = (Fig) figs.elementAt(i);
            if (f.getOwner() instanceof MStimulus)
                res++;
        }
        return res;
    }

    public void setNamespace(MNamespace m) {
        super.setNamespace(m);
        SequenceDiagramGraphModel gm = new SequenceDiagramGraphModel();
        gm.setNamespace(m);
        setGraphModel(gm);

        LayerPerspective lay;
        if (m == null) {
            cat.error("SEVERE WARNING: Sequence diagram was created " + "without a valid namesspace. " + "Setting namespace to empty.");
            lay = new SequenceDiagramLayout("", gm);
        } else
            lay = new SequenceDiagramLayout(m.getName(), gm);
        setLayer(lay);
        SequenceDiagramRenderer rend = new SequenceDiagramRenderer(); // singleton
        lay.setGraphNodeRenderer(rend);
        lay.setGraphEdgeRenderer(rend);
    }

    /** initialize the toolbar for this diagram type */
    public void initToolBar() {
        _toolBar = new ToolBar();
        _toolBar.putClientProperty("JToolBar.isRollover", Boolean.TRUE);
        //_toolBar.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));

        _toolBar.add(_actionSelect);
        _toolBar.add(_actionBroom);
        _toolBar.addSeparator();

        _toolBar.add(_actionObject);
        _toolBar.addSeparator();

        _toolBar.add(_actionMessageCall);
        _toolBar.add(_actionMessageCreate);
        _toolBar.add(_actionMessageDestroy);
        _toolBar.add(_actionMessageSend);
        _toolBar.add(_actionMessageReturn);
        _toolBar.add(_actionNestedCall);

        // other actions
        _toolBar.addSeparator();
        _toolBar.add(ActionAddNote.SINGLETON);
        _toolBar.addSeparator();

        _toolBar.add(_actionRectangle);
        _toolBar.add(_actionRRectangle);
        _toolBar.add(_actionCircle);
        _toolBar.add(_actionLine);
        _toolBar.add(_actionText);
        _toolBar.add(_actionPoly);
        _toolBar.add(_actionSpline);
        _toolBar.add(_actionInk);
        _toolBar.addSeparator();

        _toolBar.add(_diagramName.getJComponent());
    }

    public void postLoad() {
        super.postLoad();
        if(completeOldStimulusData()) {
            computeOldLayoutData();
            MessageSupervisor.initialize((SequenceDiagramLayout)getLayer());
            OldToSpecConverter.convertDiagramContents(this);
        } else {
            MessageSupervisor.initialize((SequenceDiagramLayout)getLayer());
            MessageSupervisor.createContainerFromModel((SequenceDiagramLayout)getLayer());
        }
        ((SequenceDiagramLayout)getLayer()).placeAllFigures();
        Collection c = ((SequenceDiagramLayout)getLayer()).getContents();
        Iterator i = c.iterator();
        Fig f = null;
        for(; i.hasNext(); f.damage()) {
            f = (Fig)i.next();
        }
    }

    public void preSave() {
        for(Iterator itr = ((SequenceDiagramLayout)getLayer()).getContents().iterator(); itr.hasNext();) {
            Object obj = itr.next();
            if(obj instanceof FigSeqMessage) {
                Iterator i = ((FigSeqMessage)obj).getPredecessors().iterator();
                Vector vmm = new Vector(2, 2);
                for(; i.hasNext(); vmm.add((MMessage)((FigSeqMessage)i.next()).getOwner())) {
                }
                ((MMessage)((FigSeqMessage)obj).getOwner()).setPredecessors(vmm);
            }
        }
    }

    /**
     * Creates a new diagramname.
     * @return String
     */
    protected static String getNewDiagramName() {
        String name = null;
        name = "Sequence Diagram " + _SequenceDiagramSerial;
        _SequenceDiagramSerial++;
        if (!ProjectManager.getManager().getCurrentProject().isValidDiagramName(name)) {
            name = getNewDiagramName();
        }
        return name;
    }

    /** every stimulus has to become a path item of its link
      * to have a graphical connections between stimulus and link */
    private boolean completeOldStimulusData() {
        boolean r = false;
        Iterator oeIterator = null;
        Collection ownedElements = null;
        if(getNamespace() != null) {
            ownedElements = getNamespace().getOwnedElements();
        }
        if(ownedElements != null) {
            oeIterator = ownedElements.iterator();
        }
        Layer lay = getLayer();
        if(oeIterator != null && lay != null) {
            while(oeIterator.hasNext()) {
                MModelElement me = (MModelElement)oeIterator.next();
                if(!r && ((me instanceof MLink) || (me instanceof MObject) || (me instanceof MStimulus))) {
                    r = true;
                }
                if(me instanceof MLink) {
                    Collection stimuli = ((MLink)me).getStimuli();
                    for(Iterator stimuliIterator = stimuli.iterator(); stimuliIterator.hasNext();) {
                        MStimulus stimulus = (MStimulus)stimuliIterator.next();
                        FigSeqStimulus figStimulus = (FigSeqStimulus)lay.presentationFor(stimulus);
                        if(figStimulus != null) {
                            figStimulus.addPathItemToLink(lay);
                        }
                    }

                }
            }
        }
        return r;
    }

    private void computeOldLayoutData() {
        OldLayoutConverter.computeOldLayoutData(getLayer());
    }

    /**
     * This method is for debugging only; should be removed in later version
     */
    private void debugLinkData() {
        Iterator i = getLayer().getContents().iterator();
        Object o = null;
        while(i.hasNext()) {
            o = i.next();
            if(o instanceof FigSeqLink) {
                FigSeqLink lnk = (FigSeqLink)o;
                int y = lnk.getFirstPoint().y;
                System.out.println("y = " + y);
                System.out.println("FirstPoint = " + lnk.getFirstPoint());
                System.out.println("LastPoint  = " + lnk.getLastPoint());
                System.out.println("Points     = " + lnk.getPoints());
                System.out.println("Type       = " + lnk.getClass().getName());
                System.out.println("PathItemFigs.size = " + lnk.getPathItemFigs().size());
                for(int pi = 0; pi < lnk.getPathItemFigs().size(); pi++) {
                    System.out.println("PathItemFigs(" + pi + ")   = " + lnk.getPathItemFigs().get(pi));
                }
                System.out.println("FigType    = " + lnk.getFig().getClass().getName());
                System.out.println("Polygon    = " + ((FigPoly)lnk.getFig()).getPolygon().npoints);
                System.out.println("Points[0]  = " + ((FigPoly)lnk.getFig()).getPolygon().ypoints[0]);
            }
        }
    }
} /* end class UMLSequenceDiagram */
