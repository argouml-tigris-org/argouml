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

package org.argouml.ui;
import java.util.*;
import org.argouml.util.*;
import javax.swing.*;
import java.awt.event.*;


public class TreeResourceBundle extends ListResourceBundle {


   static final Object[][] _contents = {
        { "Package-centric", "Package-centric" },
        { "Diagram-centric", "Diagram-centric" },
        { "Inheritance-centric", "Inheritance-centric" },
        { "Class Associations", "Class Associations" },
        { "Navigable Associations", "Navigable Associations" },
        { "Association-centric", "Association-centric" },
        { "Aggregate-centric", "Aggregate-centric" },
        { "Composite-centric", "Composite-centric" },
        { "Class states", "Class states" },
        { "State-centric", "State-centric" },
        { "State-transitions", "State-transitions" },
        { "Transitions-centric", "Transitions-centric" },
        { "Transitions paths", "Transitions paths" },
        { "UseCase-centric", "UseCase-centric" },
        { "Dependency-centric", "Dependency-centric" },
        { "Features of Class", "Features of Class" },
        { "Methods of Class", "Methods of Class" },
        { "Attributes of Class", "Attributes of Class" },
        { "States of Class", "States of Class" },
        { "Transitions of Class", "Transitions of Class" },

        { "Package->Subpackages", "Package->Subpackages" },
        { "Package->Classifiers", "Package->Classifiers" },
        { "Package->Associations", "Package->Associations" },
        { "Package->Instances", "Package->Instances" },
        { "Package->Links", "Package->Links" },
        { "Package->Collaborations", "Package->Collaborations" },
        { "State Machine->Final States", "State Machine->Final States" },
        { "State Machine->Initial States", "State Machine->Initial States" },
        { "State->Final Substates", "State->Final Substates" },
        { "State->Initial Substates", "State->Initial Substates" },

        { "Namespace->Owned Element", "Namespace->Owned Element" },
        { "Project->Package", "Project->Package" },
        { "Package->Diagram", "Package->Diagram" },
        { "Class->Attribute", "Class->Attribute" },
        { "Class->Operation", "Class->Operation" },
        { "Diagram->Edge", "Diagram->Edge" },
        { "Package->Base Class", "Package->Base Class" },
        { "Element->Dependent Element", "Element->Dependent Element" },
        { "Class->State Machine", "Class->State Machine" },
        { "Element->Required Element", "Element->Required Element" },
        { "Class->Subclass", "Class->Subclass" },
        { "Interaction->Messages", "Interaction->Messages" },
        { "Project->Diagram", "Project->Diagram" },
        { "Link->Stimuli", "Link->Stimuli" },
        { "Stimulus->Action", "Stimulus->Action" },
	{ "Properties", "Properties" },
	{ "Add to Diagram", "Add to Diagram"},
	{ "Click on diagram to add ", "Click on diagram to add "}
   };

     public Object[][] getContents() {
        return _contents;
     }

}
