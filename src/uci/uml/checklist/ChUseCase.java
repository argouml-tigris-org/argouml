// Copyright (c) 1996-98 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation for educational, research and non-profit
// purposes, without fee, and without a written agreement is hereby granted,
// provided that the above copyright notice and this paragraph appear in all
// copies. Permission to incorporate this software into commercial products
// must be negotiated with University of California. This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "as is",
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

package uci.uml.checklist;

import uci.argo.checklist.*;

import uci.uml.Foundation.Core.*;
import uci.uml.Model_Management.*;

/** Defines a checklist to help designers design better associations.
 *  Basically, propmts them to think about various aspects of the
 *  design. Needs-More-Work: the checklist items are categorized, but
 *  those categories are not shown yet in the user interface. */

public class ChUseCase extends UMLChecklist {

  public ChUseCase() {
  setNextCategory("Naming");
  addItem("Does the name '{name}' clearly describe the class?");
  addItem("Is '{name}' a noun or noun phrase?");
  addItem("Could the name '{name}' be misinterpreted to mean something else?");


  setNextCategory("Encoding");
  addItem("Should {name} be its own class or a simple attribute of "+
	  "another class?");
  addItem("Does {name} do exactly one thing and do it well?");
  addItem("Could {name} be broken down into two or more classes?");

  setNextCategory("Value");
  addItem("Do all attributes of {name} start with meaningful values?");
  addItem("Could you write an invariant for this class?");
  addItem("Do all constructors establish the class invariant?");
  addItem("Do all operations maintain the class invariant?");

  setNextCategory("Location");
  addItem("Could {name} be defined in a different location in the class "+
	  "hierarchy?");
  addItem("Have you planned to have subclasses of {name}?");
  addItem("Could {name} be eliminated from the model?");
  addItem("Is there another class in the model that should be revised "+
	  "or eliminated because it serves the same purpose as {name}?");

  setNextCategory("Updates");
  addItem("For what reasons will an instance of {name} be updated?");
  addItem("Is there some other object that must be updated whenever "+
	  "{name} is updated?");

  }

} /* end class ChUseCase */
