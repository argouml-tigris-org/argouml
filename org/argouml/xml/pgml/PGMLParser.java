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

package org.argouml.xml.pgml;
import java.util.*;

public class PGMLParser extends org.tigris.gef.xml.pgml.PGMLParser {

  ////////////////////////////////////////////////////////////////
  // static variables

  public static PGMLParser SINGLETON = new PGMLParser();

  protected HashMap _translateUciToOrg = new HashMap();

  ////////////////////////////////////////////////////////////////
  // constructors

  protected PGMLParser() {
	  _translateUciToOrg.put("uci.uml.visual.UMLClassDiagram",
				 "org.argouml.uml.diagram.static_structure.ui.UMLClassDiagram");
	  _translateUciToOrg.put("uci.uml.visual.UMLUseCaseDiagram",
				 "org.argouml.uml.diagram.use_case.ui.UMLUseCaseDiagram");
	  _translateUciToOrg.put("uci.uml.visual.UMLActivityDiagram",
				 "org.argouml.uml.diagram.activity.ui.UMLActivityDiagram");
	  _translateUciToOrg.put("uci.uml.visual.UMLCollaborationDiagram",
				 "org.argouml.uml.diagram.collaboration.ui.UMLCollaborationDiagram");
	  _translateUciToOrg.put("uci.uml.visual.UMLDeploymentDiagram",
				 "org.argouml.uml.diagram.deployment.ui.UMLDeploymentDiagram");
	  _translateUciToOrg.put("uci.uml.visual.UMLStateDiagram",
				 "org.argouml.uml.diagram.state.ui.UMLStateDiagram");
           _translateUciToOrg.put("uci.uml.visual.UMLSequenceDiagram",
				 "org.argouml.uml.diagram.sequence.ui.UMLSequenceDiagram");
	  _translateUciToOrg.put("uci.uml.visual.FigAssociation",
				 "org.argouml.uml.diagram.ui.FigAssociation");
	  _translateUciToOrg.put("uci.uml.visual.FigRealization",
				 "org.argouml.uml.diagram.ui.FigRealization");
	  _translateUciToOrg.put("uci.uml.visual.FigGeneralization",
				 "org.argouml.uml.diagram.ui.FigGeneralization");
	  _translateUciToOrg.put("uci.uml.visual.FigCompartment",
				 "org.argouml.uml.diagram.ui.FigCompartment");
	  _translateUciToOrg.put("uci.uml.visual.FigDependency",
				 "org.argouml.uml.diagram.ui.FigDependency");
	  _translateUciToOrg.put("uci.uml.visual.FigEdgeModelElement",
				 "org.argouml.uml.diagram.ui.FigEdgeModelElement");
	  _translateUciToOrg.put("uci.uml.visual.FigMessage",
				 "org.argouml.uml.diagram.ui.FigMessage");
	  _translateUciToOrg.put("uci.uml.visual.FigNodeModelElement",
				 "org.argouml.uml.diagram.ui.FigNodeModelElement");
	  _translateUciToOrg.put("uci.uml.visual.FigNodeWithCompartments",
				 "org.argouml.uml.diagram.ui.FigNodeWithCompartments");
	  _translateUciToOrg.put("uci.uml.visual.FigNote",
				 "org.argouml.uml.diagram.ui.FigNote");
	  _translateUciToOrg.put("uci.uml.visual.FigTrace",
				 "org.argouml.uml.diagram.ui.FigTrace");
	  _translateUciToOrg.put("uci.uml.visual.FigClass",
				 "org.argouml.uml.diagram.static_structure.ui.FigClass");
	  _translateUciToOrg.put("uci.uml.visual.FigInterface",
				 "org.argouml.uml.diagram.static_structure.ui.FigInterface");
	  _translateUciToOrg.put("uci.uml.visual.FigInstance",
				 "org.argouml.uml.diagram.static_structure.ui.FigInstance");
	  _translateUciToOrg.put("uci.uml.visual.FigLink",
				 "org.argouml.uml.diagram.static_structure.ui.FigLink");
	  _translateUciToOrg.put("uci.uml.visual.FigPackage",
				 "org.argouml.uml.diagram.static_structure.ui.FigPackage");
	  _translateUciToOrg.put("uci.uml.visual.FigActionState",
				 "org.argouml.uml.diagram.activity.ui.FigActionState");
	  _translateUciToOrg.put("uci.uml.visual.FigAssociationRole",
				 "org.argouml.uml.diagram.collaboration.ui.FigAssociationRole");
	  _translateUciToOrg.put("uci.uml.visual.FigClassifierRole",
				 "org.argouml.uml.diagram.collaboration.ui.FigClassifierRole");
	  _translateUciToOrg.put("uci.uml.visual.FigComponent",
				 "org.argouml.uml.diagram.deployment.ui.FigComponent");
	  _translateUciToOrg.put("uci.uml.visual.FigComponentInstance",
				 "org.argouml.uml.diagram.deployment.ui.FigComponentInstance");
	  _translateUciToOrg.put("uci.uml.visual.FigMNode",
				 "org.argouml.uml.diagram.deployment.ui.FigMNode");
	  _translateUciToOrg.put("uci.uml.visual.FigMNodeInstance",
				 "org.argouml.uml.diagram.deployment.ui.FigMNodeInstance");
	  _translateUciToOrg.put("uci.uml.visual.FigObject",
				 "org.argouml.uml.diagram.deployment.ui.FigObject");
	  _translateUciToOrg.put("uci.uml.visual.FigBranchState",
				 "org.argouml.uml.diagram.state.ui.FigBranchState");
	  _translateUciToOrg.put("uci.uml.visual.FigCompositeState",
				 "org.argouml.uml.diagram.state.ui.FigCompositeState");
	  _translateUciToOrg.put("uci.uml.visual.FigDeepHistoryState",
				 "org.argouml.uml.diagram.state.ui.FigDeepHistoryState");
	  _translateUciToOrg.put("uci.uml.visual.FigFinalState",
				 "org.argouml.uml.diagram.state.ui.FigFinalState");
	  _translateUciToOrg.put("uci.uml.visual.FigForkState",
				 "org.argouml.uml.diagram.state.ui.FigForkState");
	  _translateUciToOrg.put("uci.uml.visual.FigHistoryState",
				 "org.argouml.uml.diagram.state.ui.FigHistoryState");
	  _translateUciToOrg.put("uci.uml.visual.FigInitialState",
				 "org.argouml.uml.diagram.state.ui.FigInitialState");
	  _translateUciToOrg.put("uci.uml.visual.FigJoinState",
				 "org.argouml.uml.diagram.state.ui.FigJoinState");
	  _translateUciToOrg.put("uci.uml.visual.FigShallowHistoryState",
				 "org.argouml.uml.diagram.state.ui.FigShallowHistoryState");
	  _translateUciToOrg.put("uci.uml.visual.FigState",
				 "org.argouml.uml.diagram.state.ui.FigState");
	  _translateUciToOrg.put("uci.uml.visual.FigActionState",
				 "org.argouml.uml.diagram.activity.ui.FigActionState");
	  _translateUciToOrg.put("uci.uml.visual.FigStateVertex",
                                 "org.argouml.uml.diagram.state.ui.FigStateVertex");
	  _translateUciToOrg.put("uci.uml.visual.FigTransition",
				 "org.argouml.uml.diagram.state.ui.FigTransition");
	  _translateUciToOrg.put("uci.uml.visual.FigActor",
				 "org.argouml.uml.diagram.use_case.ui.FigActor");
	  _translateUciToOrg.put("uci.uml.visual.FigUseCase",
				 "org.argouml.uml.diagram.use_case.ui.FigUseCase");
          _translateUciToOrg.put("uci.uml.visual.FigSeqLink",
				 "org.argouml.uml.diagram.sequence.ui.FigSeqLink");
          _translateUciToOrg.put("uci.uml.visual.FigSeqObject",
				 "org.argouml.uml.diagram.sequence.ui.FigSeqObject");
          _translateUciToOrg.put("uci.uml.visual.FigSeqStimulus",
				 "org.argouml.uml.diagram.sequence.ui.FigSeqStimulus");
  }


    protected String translateClassName(String oldName) {
        if ( oldName.startsWith("org.") ) return oldName;

        if ( oldName.startsWith("uci.gef.") ) {
                String className = oldName.substring(oldName.lastIndexOf(".")+1);
                return ("org.tigris.gef.presentation." + className);
        }

        String translated = (String)_translateUciToOrg.get(oldName);
        //System.out.println( "old = " + oldName + " / new = " + translated );
        return translated;
    }

  private String[] _entityPaths = { "/org/argouml/xml/dtd/","/org/tigris/gef/xml/dtd/" };
  protected String[] getEntityPaths() {
    return _entityPaths;
  }
  
  /**
   * Just temporary for debugging...
   */
  public synchronized org.tigris.gef.base.Diagram readDiagram(java.io.InputStream is, boolean closeStream) {
    System.out.println ("org.argouml.xml.pgml.PGMLParser.readDiagram called. _nestedGroups = " + _nestedGroups);
    
    org.tigris.gef.base.Diagram dResult = super.readDiagram (is, closeStream);
    
    System.out.println ("Returning from org.argouml.xml.pgml.PGMLParser.readDiagram call. _nestedGroups = " + _nestedGroups);
    
    return dResult;
  }
} /* end class PGMLParser */

