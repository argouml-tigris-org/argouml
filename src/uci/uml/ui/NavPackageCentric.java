// Copyright (c) 1996-98 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation for educational, research and non-profit
// purposes, without fee, and without a written agreement is hereby granted,
// provided that the above copyright notice and this paragraph appear in all
// copies. Permission to incorporate this software into commercial products may
// be obtained by contacting the University of California. David F. Redmiles
// Department of Information and Computer Science (ICS) University of
// California Irvine, California 92697-3425 Phone: 714-824-3823. This software
// program and documentation are copyrighted by The Regents of the University
// of California. The software program and documentation are supplied "as is",
// without any accompanying services from The Regents. The Regents do not
// warrant that the operation of the program will be uninterrupted or
// error-free. The end-user understands that the program was developed for
// research purposes and is advised not to rely exclusively on the program for
// any reason. IN NO EVENT SHALL THE UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY
// PARTY FOR DIRECT, INDIRECT, SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES,
// INCLUDING LOST PROFITS, ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS
// DOCUMENTATION, EVEN IF THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE
// POSSIBILITY OF SUCH DAMAGE. THE UNIVERSITY OF CALIFORNIA SPECIFICALLY
// DISCLAIMS ANY WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
// WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE
// SOFTWARE PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF
// CALIFORNIA HAS NO OBLIGATIONS TO PROVIDE MAINTENANCE, SUPPORT, UPDATES,
// ENHANCEMENTS, OR MODIFICATIONS.


package uci.uml.ui;

import java.util.*;
import uci.uml.Model_Management.*;
import uci.uml.Foundation.Core.*;


/** Project -> (Diagram or (Model -> Element -> Feature)) */
// needs-more-work: what about pseudo nodes for types of elements and features?
// needs-more-work: nested classes: a class should be treated as a namespace


public class NavPackageCentric extends NavPerspective {
  public NavPackageCentric() {
    addSubTreeModel(new GoProjectModel());
    addSubTreeModel(new GoModelToDiagram());
    addSubTreeModel(new GoModelToElements());
    addSubTreeModel(new GoClassifierToBeh());
    addSubTreeModel(new GoClassifierToStr());
  }

  public String toString() { return "Package-centric"; }

//   public Object getChild(Object parent, int index) {
//     //System.out.println("NavM_DE_F getChild");
//     if (parent instanceof Project) {
//       Vector diagrams = ((Project)parent).getDiagrams();
//       Vector models = ((Project)parent).getModels();
//       if (index < diagrams.size()) return diagrams.elementAt(index);
//       else return models.elementAt(index - diagrams.size());
//     }
//     else if (parent instanceof Package) {
//       //pseudo nodes for types of elements?
//       Vector refs = ((Package)parent).getOwnedElement();
//       ElementOwnership eo = (ElementOwnership) refs.elementAt(index);
//       return eo.getModelElement();
//     }
//     else if (parent instanceof Classifier) {
//       Classifier c = (Classifier) parent;
//       Vector beh = c.getBehavioralFeature();
//       Vector str = c.getStructuralFeature();
//       int behSize = (beh == null ? 0 : beh.size());
//       int strSize = (str == null ? 0 : str.size());
//       if (index < behSize) return beh.elementAt(index);
//       else return str.elementAt(index - behSize);
//     }
//     //states
//     //use cases
//     else return super.getChild(parent, index);
//   }
  
//   public int getChildCount(Object parent) {
//     //System.out.println("NavM_DE_F getChildCount");
//     if (parent instanceof Project) {
//       Project p = (Project) parent;
//       return p.getDiagrams().size() + p.getModels().size();
//     }
//     else if (parent instanceof Package) {
//       Vector refs = ((Package)parent).getOwnedElement();
//       return (refs == null ? 0 : refs.size());
//     }
//     else if (parent instanceof Classifier) {
//       Classifier c = (Classifier) parent;
//       Vector beh = c.getBehavioralFeature();
//       Vector str = c.getStructuralFeature();
//       int behSize = (beh == null ? 0 : beh.size());
//       int strSize = (str == null ? 0 : str.size());
//       return behSize + strSize;
//     }
//     //states
//     //use cases
//     else return super.getChildCount(parent);
//   }
  
//   public int getIndexOfChild(Object parent, Object child) {
//     if (parent instanceof Project) {
//       Project p = (Project) parent;
//       Vector diagrams = p.getDiagrams();
//       Vector models = p.getModels();
//       if (diagrams.contains(child)) return diagrams.indexOf(child);
//       else return diagrams.size() + models.indexOf(child);
//     }
//     else if (parent instanceof Package) {
//       Vector refs = ((Package)parent).getOwnedElement();
//       return refs.indexOf(child);
//     }
//     else if (parent instanceof Classifier) {
//       Classifier c = (Classifier) parent;
//       Vector beh = c.getBehavioralFeature();
//       Vector str = c.getStructuralFeature();
//       int behSize = (beh == null ? 0 : beh.size());
//       int strSize = (str == null ? 0 : str.size());
//       if (beh.contains(child)) return beh.indexOf(child);
//       else return behSize + str.indexOf(child);
//     }
//     //states
//     //use cases
//     else return super.getIndexOfChild(parent, child);
//   }
  
//   public boolean isLeaf(Object node) {
//     //if (node == null) System.out.println("NavM_DE_F isLeaf null");
//     //else System.out.println("NavM_DE_F isLeaf" + node.toString());
//     if (node instanceof Project) return false;
//     else if (node instanceof Package) return false;
//     else if (node instanceof Classifier) return false;
//     // states
//     // use cases
//     else return super.isLeaf(node);
//     //attributes ok?
//   }

  
} /* end class NavPackageCentric */
