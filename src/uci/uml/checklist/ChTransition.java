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

/** Defines a checklist to help designers design better transitions.
 *  Basically, propmts them to think about various aspects of the
 *  design. Needs-More-Work: the checklist items are categorized, but
 *  those categories are not shown yet in the user interface. */

  
public class ChTransition extends UMLChecklist {

  public ChTransition() {
  setNextCategory("Structure");
  addItem("Should this transition start at a different source?");
  addItem("Should this transition end at a different destination?");
  addItem("Should there be another transition \"like\" this one?");
  addItem("Is another transition unneeded because of this one?");

  setNextCategory("Trigger");
  addItem("Does this transition need a trigger?");
  addItem("Does the trigger happen too often?");
  addItem("Does the trigger happen too rarely?");

  setNextCategory("Guard");
  addItem("Could this transition be taken too often?");
  addItem("Is this transition's condition too restrictive?");
  addItem("Could it be broken down into two or more transitions?");

  setNextCategory("Actions");
  addItem("Should this transition have an action?");
  addItem("Should this transition's action be an exit action?");
  addItem("Should this transition's action be an entry action?");
  addItem("Is the precondition of the action always met?");
  addItem("Is the action's postcondition consistent with the destination?");


  }

} /* end class ChTransition */
