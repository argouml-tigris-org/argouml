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

import java.util.*;
import java.awt.*;
import java.beans.*;
import javax.swing.*;

import ru.novosoft.uml.model_management.*;
import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.behavior.collaborations.*;
import ru.novosoft.uml.behavior.common_behavior.*;

import org.tigris.gef.base.Layer;
import org.tigris.gef.base.LayerPerspective;
import org.tigris.gef.presentation.*;
import org.tigris.gef.ui.*;

import org.argouml.ui.*;
import org.argouml.uml.diagram.ui.*;
import org.argouml.uml.diagram.sequence.*;



public class UMLSequenceDiagram extends UMLDiagram {

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


  public UMLSequenceDiagram() {
  	
    try { setName(getNewDiagramName()); }
    catch (PropertyVetoException pve) { }
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
    for (int i=0; i < size; i++) {
      Fig f = (Fig) figs.elementAt(i);
      if (f.getOwner() instanceof MStimulus) res++;
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
        System.out.println("SEVERE WARNING: Sequence diagram was created " +
                           "without a valid namesspace. " + 
                           "Setting namespace to empty.");
        lay = new SequenceDiagramLayout("", gm);
    }
    else
        lay = new SequenceDiagramLayout(m.getName(), gm);
    setLayer(lay);
    SequenceDiagramRenderer rend = new SequenceDiagramRenderer(); // singleton
    lay.setGraphNodeRenderer(rend);
    lay.setGraphEdgeRenderer(rend);
  }

  /** initialize the toolbar for this diagram type */
  protected void initToolBar() {
    _toolBar = new ToolBar();
    _toolBar.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
//     _toolBar.add(Actions.Cut);
//     _toolBar.add(Actions.Copy);
//     _toolBar.add(Actions.Paste);
//     _toolBar.addSeparator();

    _toolBar.add(_actionSelect);
    _toolBar.add(_actionBroom);
    _toolBar.addSeparator();

    _toolBar.add(_actionObject);
    _toolBar.addSeparator();

    _toolBar.add(_actionLinkWithStimulusCall);
    _toolBar.add(_actionLinkWithStimulusCreate);
    _toolBar.add(_actionLinkWithStimulusDestroy);
    _toolBar.add(_actionLinkWithStimulusSend);
    _toolBar.add(_actionLinkWithStimulusReturn);

    // other actions
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

    _toolBar.add(_diagramName);
  }

  /** every stimulus has to become a path item of its link
    * to have a graphical connections between stimulus and link */
  public void postLoad() {

    super.postLoad();


    Collection stimuli;
    Iterator stimuliIterator;
    Iterator oeIterator=null;
    Collection ownedElements=null;
    if ( getNamespace() != null) ownedElements = getNamespace().getOwnedElements();
    if (ownedElements != null) oeIterator = ownedElements.iterator();
    Layer lay = getLayer();
    if (oeIterator != null && lay != null) {
      Vector contents = new Vector();
      boolean objFound=false;
      FigSeqObject figSeqObj=null;
      FigSeqLink figLink=null;
      FigSeqStimulus figStim=null;

      Vector createdObjs = new Vector();
      Vector createLinks = new Vector();
      FigSeqObject dest=null;

      while(oeIterator.hasNext()) {
        MModelElement me = (MModelElement)oeIterator.next();
     

        if (me instanceof MLink) {
          stimuli = ((MLink) me).getStimuli();
          stimuliIterator= stimuli.iterator();
          while(stimuliIterator.hasNext()) {
            MStimulus stimulus = (MStimulus)stimuliIterator.next();
            FigSeqStimulus figStimulus = (FigSeqStimulus) lay.presentationFor(stimulus);
            if ( figStimulus != null ) {
              figStimulus.addPathItemToLink(lay);
            }
          }
        }
      }
    }
    
  
   
  }

   protected static String getNewDiagramName() {
  	String name = null;
  	Object[] args = {name};
  	do {
        name = "sequence diagram " + _SequenceDiagramSerial;
        _SequenceDiagramSerial++;
        args[0] = name;
    }
    while (TheInstance.vetoCheck("name", args));
    return name;
  }



} /* end class UMLSequenceDiagram */
