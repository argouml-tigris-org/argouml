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



// File: CrStimulusWithWrongPosition.java
// Classes: CrStimulusWithWrongPosition
// Original Author: 5eichler@informatik.uni-hamburg.de
// $Id$

package org.argouml.uml.cognitive.critics;

import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import org.argouml.cognitive.Designer;
import org.argouml.cognitive.ToDoItem;
import org.argouml.model.ModelFacade;
import org.argouml.uml.diagram.sequence.ui.FigSeqLink;
import org.argouml.uml.diagram.sequence.ui.FigSeqObject;
import org.argouml.uml.diagram.sequence.ui.UMLSequenceDiagram;
import org.tigris.gef.presentation.FigActivation;
import org.tigris.gef.util.VectorSet;

public class CrStimulusWithWrongPosition extends CrUML {
    
    public CrStimulusWithWrongPosition() {
	setHeadline("Wrong position of these stimuli");
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
	    
	for (int i = 0; i < size; i++) {
	    if (figs.elementAt(i) instanceof FigSeqLink) {
		FigSeqLink fsl = (FigSeqLink) figs.elementAt(i);
		Object ml = /*(MLink)*/ fsl.getOwner();
		boolean found = false;
		if (ModelFacade.getStimuli(ml) != null) {
		    Collection col = ModelFacade.getStimuli(ml);
		    Iterator it = col.iterator();
		    while (it.hasNext()) {
			Object ms = it.next();
			if (ModelFacade.getDispatchAction(ms) != null) {
			    Object ma = ModelFacade.getDispatchAction(ms);
			    if (ModelFacade.isAReturnAction(ma)) continue;
			    else {
				FigSeqObject fso =
				    (FigSeqObject) fsl.getSourceFigNode();
				if ( fso != null) {
				    Vector edges = fso.getFigEdges();
				    if (edges != null && edges.size() > 0) {
					int portNumber =
					    fsl.getPortNumber(edges);
					int low = 10000;		
					for (int j = 0; j < edges.size(); j++)
					{
					    FigSeqLink edge =
						(FigSeqLink) edges.elementAt(j);
					    int edge_number =
						edge.getPortNumber(edges);
					    if (edge_number < low)
						low = edge_number;
					}
					if (low >= portNumber) {
					    Vector act = fso._activations;
					    if ( act != null && act.size() > 0)
					    {
						FigActivation fa =
						    (FigActivation)
						    act.elementAt(0);
						if (!fa.isFromTheBeg())
						    found = true;
					    }
					}
					else {
					    Vector act = fso._activations;
					    if (act != null && act.size() > 0)
					    {
						for (int k = 0;
						     k < act.size();
						     k++)
						{
						    FigActivation fa =
							(FigActivation)
							act.elementAt(k);
						    int from =
							fa.getFromPosition();
						    if (from ==  portNumber)
							found = true;  
						}
					    }
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
									    
} /* end class CrStimulusWithWrongPosition.java */
