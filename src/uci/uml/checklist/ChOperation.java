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


package uci.uml.checklist;

import uci.argo.checklist.*;

import uci.uml.Foundation.Core.*;
import uci.uml.Model_Management.*;


public class ChOperation extends UMLChecklist {

  public ChOperation() {
  setNextCategory("Naming");
  addItem("Does the name '{name}' clearly describe the operation?");
  addItem("Is '{name}' a verb or verb phrase?");
  addItem("Could the name '{name}' be misinterperrted to mean something else?");
  addItem("Does {name} do one thing and do it well?");


  setNextCategory("Encoding");
  addItem("Is the return type '{returns}' too restrictive to represent all "+
	  "possible values returned by {name}?"); 
  addItem("Does '$returns' allow return values that could never be correct?");
  addItem("Could {name} be combined with some other operation of "+
	  "{owner} (e.g., {owner.behavioralFeature})?");
  addItem("Could {name} be broken down into two or more parts (e.g., preprocess, "+
	  "main processing, and postprocessing)?");
  addItem("Could {name} be replaced by a series of client calls to "+
	  "simpler operations?");
  addItem("Could {name} be combined with other operations to reduce the "+
	  "number of calls clients must make?");

  setNextCategory("Value");
  addItem("Can {name} handle all possible inputs?");
  addItem("Are there special case inputs that must be handled separately?");
  addItem("Could you write an expression to check if the arguments to {name} "+
	  "are correct? Plausable?");
  addItem("Can you express the preconditions of {name}?");
  addItem("Can you express the postconditions of {name}?");
  addItem("How will {name} behave if preconditions are violated?");
  addItem("How will {name} behave if postconditions cannot be achieved?");
  
  setNextCategory("Location");
  addItem("Could {name} be defined in a different class that is associated "+
	  "with {owner}?");
  addItem("Could {name} be moved up the inheritance hierarchy to apply to "+
	  "{owner} and to other classes?");
  addItem("Does {name} apply to all instances of class {owner} including "+
	  "instances of subclasses?");
  addItem("Could {name} be eliminated from the model?");
  addItem("Is there another operation in the model that should be revised "+
	  "or eliminated because it serves the same purpose as {name}?");


  // side effects, recursion, case analysis, ...
  }

} /* end class ChOperation */
