// Copyright (c) 1996-2001 The Regents of the University of California. All
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

package org.argouml.uml.cognitive.checklist;

import org.argouml.cognitive.checklist.*;

import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.model_management.*;
import ru.novosoft.uml.behavior.state_machines.*;

/** Defines a checklist to help designers design better instances.
 *  Basically, propmts them to think about various aspects of the
 *  design. TODO: the checklist items are categorized, but
 *  those categories are not shown yet in the user interface. 
 *
 *  @deprecated As of ArgoUml version 0.9.1, Now created by loading strings from 
 *     UMLCognitiveResourceBundle into a generic UMLChecklist instance
 *           
 *  @see org.argouml.uml.cognitive.UMLCognitiveResourceBundle
 *  @see UMLChecklist
 * 
 */

  
public class ChInstance extends UMLChecklist {

  public ChInstance() {
  setNextCategory("General");
  addItem("Does this instance <ocl>self</ocl> clearly describe the instance?");

  setNextCategory("Naming");
  addItem("Does the name '<ocl>self</ocl>' clearly describe the instance?");
  addItem("Does '<ocl>self</ocl>' denote a state rather than an activity?");
  addItem("Could the name '<ocl>self</ocl>' be misinterpreted to mean something else?");

  setNextCategory("Structure");
  addItem("Should <ocl>self</ocl> be its own state or could it be merged with "+
	  "another state?");
  addItem("Does <ocl>self</ocl> do exactly one thing and do it well?");
  addItem("Could <ocl>self</ocl> be broken down into two or more states?");
  addItem("Could you write a characteristic equation for <ocl>self</ocl>?");
  addItem("Does <ocl>self</ocl> belong in this state machine or another?");
  addItem("Should <ocl>self</ocl> be be an initial state?");
  addItem("Is some state in another machine exclusive with <ocl>self</ocl>?");

  setNextCategory("Actions");
  addItem("What action should be preformed on entry into <ocl>self</ocl>?");
  addItem("Should some attribute be updated on entry into <ocl>self</ocl>?");
  addItem("What action should be preformed on exit from <ocl>self</ocl>?");
  addItem("Should some attribute be updated on exit from <ocl>self</ocl>?");
  addItem("What action should be preformed while in <ocl>self</ocl>?");
  addItem("Do state-actions maintain <ocl>self</ocl> as the current state?");

  setNextCategory("Transitions");
  addItem("Should there be another transition into <ocl>self</ocl>?");
  addItem("Can all the transitions into <ocl>self</ocl> be used?");
  addItem("Could some incoming transitions be combined?");
  addItem("Should there be another transition out of <ocl>self</ocl>?");
  addItem("Can all the transitions out of <ocl>self</ocl> be used?");
  addItem("Is each outgoing transition exclusive?");
  addItem("Could some outgoing transitions be combined?");
  }

} /* end class ChInstance */
