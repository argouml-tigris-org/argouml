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

package uci.uml.checklist;

import uci.argo.checklist.*;

import uci.uml.Foundation.Core.*;
import uci.uml.Model_Management.*;
import uci.uml.Behavioral_Elements.State_Machines.*;

/** Defines a checklist to help designers design better instances.
 *  Basically, propmts them to think about various aspects of the
 *  design. Needs-More-Work: the checklist items are categorized, but
 *  those categories are not shown yet in the user interface. */

  
public class ChInstance extends UMLChecklist {

  public ChInstance() {
  setNextCategory("General");
  addItem("Does this instance {name} clearly describe the instance?");

  setNextCategory("Naming");
  addItem("Does the name '{name}' clearly describe the instance?");
  addItem("Does '{name}' denote a state rather than an activity?");
  addItem("Could the name '{name}' be misinterpreted to mean something else?");

  setNextCategory("Structure");
  addItem("Should {name} be its own state or could it be merged with "+
	  "another state?");
  addItem("Does {name} do exactly one thing and do it well?");
  addItem("Could {name} be broken down into two or more states?");
  addItem("Could you write a characteristic equation for {name}?");
  addItem("Does {name} belong in this state machine or another?");
  addItem("Should {name} be be an initial state?");
  addItem("Is some state in another machine exclusive with {name}?");

  setNextCategory("Actions");
  addItem("What action should be preformed on entry into {name}?");
  addItem("Should some attribute be updated on entry into {name}?");
  addItem("What action should be preformed on exit from {name}?");
  addItem("Should some attribute be updated on exit from {name}?");
  addItem("What action should be preformed while in {name}?");
  addItem("Do state-actions maintain {name} as the current state?");

  setNextCategory("Transitions");
  addItem("Should there be another transition into {name}?");
  addItem("Can all the transitions into {name} be used?");
  addItem("Could some incoming transitions be combined?");
  addItem("Should there be another transition out of {name}?");
  addItem("Can all the transitions out of {name} be used?");
  addItem("Is each outgoing transition exclusive?");
  addItem("Could some outgoing transitions be combined?");
  }

} /* end class ChInstance */
