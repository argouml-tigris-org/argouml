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


/** Defines a checklist to help designers design better attributes.
 *  Basically, prompts them to think about various aspects of the
 *  design. Needs-More-Work: the checklist items are categorized, but
 *  those categories are not shown yet in the user interface. */

public class ChAttribute extends UMLChecklist {

  public ChAttribute() {
    setNextCategory("Naming");
    addItem("Does the name '{name}' clearly describe the attribute?");
    addItem("Is '{name}' a noun or noun phrase?");
    addItem("Could the name '{name}' be misinterpreted to mean something else?");


    setNextCategory("Encoding");
    addItem("Is the type {type} too restrictive to represent all "+
	    "possible values of {name}?");
    addItem("Does the type {type} allow values for {name} that could never be "+
	    "correct?");
    addItem("Could {name} be combined with some other attribute of "+
	    "{owner} (e.g., {owner.structuralFeature})?");
    addItem("Could {name} be broken down into two or more parts (e.g., a phone"+
	    "number can be broken down into area code, prefix, and number)?");
    addItem("Could {name} be computed from other attributes instead of stored?");

    setNextCategory("Value");
    addItem("Should {name} have an initial (or default) value?");
    addItem("Is the initial value {initialValue} correct?");
    addItem("Could you write an expression to check if {name} is correct? "+
	    "Plausible?");

    setNextCategory("Location");
    addItem("Could {name} be defined in a different class that is associated "+
	    "with {owner}?");
    addItem("Could {name} be moved up the inheritance hierarchy to apply to "+
	    "owner.name and to other classes?");
    addItem("Does {name} apply to all instances of class {owner} including "+
	  "instances of subclasses?");
    addItem("Could {name} be eliminated from the model?");
    addItem("Is there another attribute in the model that should be revised "+
	    "or eliminated because it serves the same purpose as {name}?");

    setNextCategory("Updates");
    addItem("For what reasons will {name} be updated?");

    addItem("Is there some other attribute that must be updated whenever "+
	    "{name} is updated?");

    addItem("Is there a method that should be called when {name} is updated?");
    addItem("Is there a method that should be called when {name} is given a "+
	    "certain kind of value?");


  }

} /* end class ChAttribute */
