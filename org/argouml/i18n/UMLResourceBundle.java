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


package org.argouml.i18n;
import java.util.*;
import org.argouml.util.*;

/**
 *   This class is the default member of a resource bundle that
 *   provides strings for UML related PropPanels.
 *
 *   If there is not an explicit entry for a key, handleGetObject
 *   just returns the key.  This class should not be called directly
 *   but should be called through the PropPanel.localize() method.
 *
 *   @author Curt Arnold
 *   @since 0.9
 *   @see java.util.ResourceBundle
 *   @see UMLResourceBundle_de
 *   @see org.argouml.util.CheckResourceBundle
 */
public class UMLResourceBundle extends ListResourceBundle {

    private static final Object[][] _contents = {
        { "abstract", "abstract" },
        { "Abstract", "Abstract" },  
        { "active", "active" },
        { "Add actor", "Add actor" },  
        { "Add association" , "Add association" },
        { "Add association end", "Add association end" },  
        { "Add attribute" , "Add attribute" },
        { "Add class", "Add class" },  
        { "Add datatype" , "Add datatype" },
        { "Add enumeration literal", "Add enumeration literal" },  
        { "Add generalization", "Add generalization" },  
        { "Add inner class" , "Add inner class" },
        { "Add interface", "Add interface" },  
        { "Add_Menu_Actor" , "Actor..." } ,
        { "Add_Menu_Class" , "Class..." } ,
        { "Add_Menu_Datatype" , "Datatype..." } ,
        { "Add_Menu_Exception" , "Exception..." } ,
        { "Add_Menu_Interface" , "Interface..." } ,
        { "Add_Menu_Signal" , "Signal..." } ,
        { "Add_Menu_UseCase" , "Use Case..." },
        { "Add operation" , "Add operation" },
        { "Add parameter" , "Add parameter" },
        { "Add realization", "Add realization" },  
        { "Add stereotype", "Add stereotype" },  
        { "Add subpackage", "Add subpackage" },  
        { "Add use case" , "Add use case" },
        { "Association:" , "Association:" },
        { "Association Ends:", "Association Ends:" },  
        { "Associations:" , "Associations:" },
        { "Attributes:" , "Attributes:" },

        { "Base Class:" , "Base Class:" },

        { "changeable" , "changeable" },
        { "Checklist", "Checklist" },         
        { "Class" , "Class" },
        { "Components:" , "Components:" },
        { "Connections:" , "Connections:" },
        { "Constraints", "Constraints" },                  

        { "Delete actor" , "Delete actor" },
        { "Delete association" , "Delete association" },
        { "Delete association end" , "Delete association end" },
        { "Delete attribute" , "Delete attribute" },
        { "Delete class" , "Delete class" },
        { "Delete interface" , "Delete interface" },
        { "Delete operation" , "Delete operation" },
        { "Delete package" , "Delete package" },
        { "Delete parameter" , "Delete parameter" },
        { "Derived:" , "Derived:" },
        { "Documentation", "Documentation" },              

        { "Expression:" , "Expression:" },
        { "Extends:" , "Extends:" },

        { "final", "final" },
        { "Final", "Final" },  

        { "Go back" , "Go back" },
        { "Go forward" , "Go forward" },
        { "Go up", "Go up" },

        { "Implementations:", "Implementations:" },  
        { "Implements:" , "Implements:" },
        { "Incoming:" , "Incoming:" },

        { "Language:" , "Language:" },
        { "Literals:" , "Literals:" },

        { "Modifiers:", "Modifiers:" },
        { "Multiplicity:" , "Multiplicity:" },

        { "Name:" , "Name:" },
        { "Namespace:" , "Namespace:" },
        { "NavClass", "Go to selected classifier" },
        { "NavStereo", "Go to selected stereotype" },
        { "New actor" , "New actor" },
        { "New association" , "New association" },
        { "New attribute" , "New attribute" },
        { "New class" , "New class" },
        { "New data type" , "New data type" },
        { "New interface" , "New interface" },
        { "New operation" , "New operation" },
        { "New signal" , "New signal" },
        { "New stereotype", "New stereotype" },  
        { "none" , "none" },

        { "Operations:" , "Operations:" },
        { "Ordering:" , "Ordering:" },
        { "Outgoing:" , "Outgoing:" },
        { "Owned Elements", "Owned Elements" },  
        { "Owned Elements:", "Owned Elements:" },
        { "Owner:" , "Owner:" },

        { "Properties", "Properties" },          
        { "public", "public" },
        { "Public", "Public" },  

        { "Receiver:" , "Receiver:" },
        { "Receives:" , "Receives:" },
        { "root", "root" },
        { "Root", "Root" },  

        { "Sends:" , "Sends:" },
        { "sorted" , "sorted" },
        { "Source", "Source" },                            
        { "Stereotype:", "Stereotype:" },
        { "Style", "Style" },                              

        { "TaggedValues", "Tagged Values" },            
        { "ToDoItem", "ToDo Item" },
        { "Transition" , "Transition" },
        { "Trigger:" , "Trigger:" },
        { "Type:" , "Type:" },

        { "Visibility:" , "Visibility:" },
    };

    public Object[][] getContents() {
        return _contents;
    }


}
