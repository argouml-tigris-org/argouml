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



// File: CrCallWithoutReturn.java
// Classes: CrCallWithoutReturn
// Original Author: 5eichler@informatik.uni-hamburg.de
// $Id$

package org.argouml.uml.cognitive.critics;

import java.util.*;

import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.behavior.common_behavior.*;

import org.argouml.cognitive.*;
import org.argouml.uml.diagram.sequence.ui.FigSeqLink;
import org.argouml.uml.diagram.sequence.ui.FigSeqObject;
import org.argouml.uml.diagram.sequence.ui.UMLSequenceDiagram;

import org.tigris.gef.util.VectorSet;




/**
 * A critic to detect when there are components that
 * are not inside a node
 **/

public class CrCallWithoutReturn extends CrUML {

  public CrCallWithoutReturn() {
    setHeadline("Missing return-actions");
    addSupportedDecision(CrUML.decPATTERNS);
  }

  public boolean predicate2(Object dm, Designer dsgr) {
    if (!(dm instanceof UMLSequenceDiagram)) return NO_PROBLEM;
    UMLSequenceDiagram sd = (UMLSequenceDiagram) dm;
    VectorSet offs = computeOffenders(sd); 
    if (offs == null) return NO_PROBLEM; 
    return PROBLEM_FOUND; 
  }

  public ToDoItem toDoItem(Object dm, Designer dsgr) { 
    UMLSequenceDiagram sd = (UMLSequenceDiagram) dm;
    VectorSet offs = computeOffenders(sd); 
    return new ToDoItem(this, offs, dsgr); 
  } 
 
  public boolean stillValid(ToDoItem i, Designer dsgr) { 
    if (!isActive()) return false; 
    VectorSet offs = i.getOffenders(); 
    UMLSequenceDiagram sd = (UMLSequenceDiagram) offs.firstElement();
       //if (!predicate(dm, dsgr)) return false; 
    VectorSet newOffs = computeOffenders(sd); 
    boolean res = offs.equals(newOffs); 
    return res; 
  } 

  public VectorSet computeOffenders(UMLSequenceDiagram sd) { 

    Vector figs = sd.getLayer().getContents();
    VectorSet offs = null;
    int size = figs.size();
     
    for (int i=0; i<size; i++) {
      if (figs.elementAt(i) instanceof FigSeqLink) {
        FigSeqLink fsl = (FigSeqLink) figs.elementAt(i);
        MLink ml = (MLink) fsl.getOwner();
        boolean found = false;
        if (ml.getStimuli() != null) {
          Collection col = ml.getStimuli();
          Iterator it = col.iterator();
          while (it.hasNext()) {
            MStimulus ms = (MStimulus) it.next();
            if (ms.getDispatchAction() instanceof MCallAction || ms.getDispatchAction() instanceof MSendAction) {
              found = true;
              Vector edges = ((FigSeqObject)fsl.getDestFigNode()).getFigEdges();
              for (int j=0; j<edges.size(); j++) {
                FigSeqLink second = (FigSeqLink) edges.elementAt(j);
	MLink ml2 = (MLink) second.getOwner();
                if (ml2.getStimuli() != null) {
                  Collection col2 = ml2.getStimuli();
                  Iterator it2 = col2.iterator();
	  while (it2.hasNext()) {
 	    MStimulus ms2 = (MStimulus) it2.next();
	    if (ms2.getDispatchAction() instanceof MReturnAction
		&& second.getPortNumber(figs) > fsl.getPortNumber(figs)
		&& ms.getSender() == ms2.getReceiver()) {
 	      found = false;
                    }
 	  }
	}
              }
            }
          }
        }

        if (found) {
          if (offs == null) {
            offs = new VectorSet();
            offs.addElement(sd);
          }
          offs.addElement(fsl);
        }    
      }
    }  

    return offs; 
  } 

} /* end class CrCallWithoutReturn.java */

