// $Id$
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


/** Defines a checklist to help designers design better attributes.
 *  Basically, prompts them to think about various aspects of the
 *  design. TODO: the checklist items are categorized, but
 *  those categories are not shown yet in the user interface. 
 *
 *  @deprecated As of ArgoUml version 0.9.1, Now created by loading
 *  strings from UMLCognitiveResourceBundle into a generic
 *  UMLChecklist instance
 *           
 *  @see org.argouml.uml.cognitive.UMLCognitiveResourceBundle
 *  @see UMLChecklist
 * 
 */

public class ChAttribute extends UMLChecklist {

    public ChAttribute() {
	setNextCategory("Naming");
	addItem("Does the name '<ocl>self</ocl>' clearly describe the attribute?");
	addItem("Is '<ocl>self</ocl>' a noun or noun phrase?");
	addItem("Could the name '<ocl>self</ocl>' be misinterpreted to mean something else?");


	setNextCategory("Encoding");
	addItem("Is the type <ocl>self.type</ocl> too restrictive to represent all " +
		"possible values of <ocl>self</ocl>?");
	addItem("Does the type <ocl>self.type</ocl> allow values for <ocl>self</ocl> that could never be " +
		"correct?");
	addItem("Could <ocl>self</ocl> be combined with some other attribute of " +
		"<ocl>self.owner</ocl> (e.g., {owner.structuralFeature})?");
	addItem("Could <ocl>self</ocl> be broken down into two or more parts (e.g., a phone" +
		"number can be broken down into area code, prefix, and number)?");
	addItem("Could <ocl>self</ocl> be computed from other attributes instead of stored?");

	setNextCategory("Value");
	addItem("Should <ocl>self</ocl> have an initial (or default) value?");
	addItem("Is the initial value <ocl>self.initialValue</ocl> correct?");
	addItem("Could you write an expression to check if <ocl>self</ocl> is correct? " +
		"Plausible?");

	setNextCategory("Location");
	addItem("Could <ocl>self</ocl> be defined in a different class that is associated " +
		"with <ocl>self.owner</ocl>?");
	addItem("Could <ocl>self</ocl> be moved up the inheritance hierarchy to apply to " +
		"owner.name and to other classes?");
	addItem("Does <ocl>self</ocl> apply to all instances of class <ocl>self.owner</ocl> including " +
		"instances of subclasses?");
	addItem("Could <ocl>self</ocl> be eliminated from the model?");
	addItem("Is there another attribute in the model that should be revised " +
		"or eliminated because it serves the same purpose as <ocl>self</ocl>?");

	setNextCategory("Updates");
	addItem("For what reasons will <ocl>self</ocl> be updated?");

	addItem("Is there some other attribute that must be updated whenever " +
		"<ocl>self</ocl> is updated?");

	addItem("Is there a method that should be called when <ocl>self</ocl> is updated?");
	addItem("Is there a method that should be called when <ocl>self</ocl> is given a " +
		"certain kind of value?");


    }

} /* end class ChAttribute */
