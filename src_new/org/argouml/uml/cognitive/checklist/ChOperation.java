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

/** Defines a checklist to help designers design better operations.
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


public class ChOperation extends UMLChecklist {

    public ChOperation() {
	setNextCategory("Naming");
	addItem("Does the name '<ocl>self</ocl>' clearly describe the operation?");
	addItem("Is '<ocl>self</ocl>' a verb or verb phrase?");
	addItem("Could the name '<ocl>self</ocl>' be misinterpreted to mean something else?");
	addItem("Does <ocl>self</ocl> do one thing and do it well?");


	setNextCategory("Encoding");
	addItem("Is the return type '<ocl>self.returnType</ocl>' too restrictive to represent all " +
		"possible values returned by <ocl>self</ocl>?"); 
	addItem("Does '<ocl>self.returnType</ocl>' allow return values that could never be correct?");
	addItem("Could <ocl>self</ocl> be combined with some other operation of " +
		"<ocl>self.owner</ocl> (e.g., <ocl sep=', '>self.owner.behavioralFeature</ocl>)?");
	addItem("Could <ocl>self</ocl> be broken down into two or more parts (e.g., pre-process, " +
		"main processing, and post-processing)?");
	addItem("Could <ocl>self</ocl> be replaced by a series of client calls to " +
		"simpler operations?");
	addItem("Could <ocl>self</ocl> be combined with other operations to reduce the " +
		"number of calls clients must make?");

	setNextCategory("Value");
	addItem("Can <ocl>self</ocl> handle all possible inputs?");
	addItem("Are there special case inputs that must be handled separately?");
	addItem("Could you write an expression to check if the arguments to <ocl>self</ocl> " +
		"are correct? Plausible?");
	addItem("Can you express the preconditions of <ocl>self</ocl>?");
	addItem("Can you express the postconditions of <ocl>self</ocl>?");
	addItem("How will <ocl>self</ocl> behave if preconditions are violated?");
	addItem("How will <ocl>self</ocl> behave if postconditions cannot be achieved?");
  
	setNextCategory("Location");
	addItem("Could <ocl>self</ocl> be defined in a different class that is associated " +
		"with <ocl>self.owner</ocl>?");
	addItem("Could <ocl>self</ocl> be moved up the inheritance hierarchy to apply to " +
		"<ocl>self.owner</ocl> and to other classes?");
	addItem("Does <ocl>self</ocl> apply to all instances of class <ocl>self.owner</ocl> including " +
		"instances of subclasses?");
	addItem("Could <ocl>self</ocl> be eliminated from the model?");
	addItem("Is there another operation in the model that should be revised " +
		"or eliminated because it serves the same purpose as <ocl>self</ocl>?");


	// side effects, recursion, case analysis, ...
    }

} /* end class ChOperation */
